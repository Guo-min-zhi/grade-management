package org.bjtuse.egms.web.admin;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.bjtuse.egms.repository.entity.Student;
import org.bjtuse.egms.service.RoleTypeManager;
import org.bjtuse.egms.service.StudentManager;
import org.bjtuse.egms.service.WorkbookService;
import org.bjtuse.egms.util.ExceptionLog;
import org.bjtuse.egms.util.ImportResult;
import org.bjtuse.egms.util.ProjectProperties;
import org.bjtuse.egms.util.RequestParamsUtil;
import org.bjtuse.egms.web.admin.form.StudentInfoQueryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/admin/studentManage")
public class StudentManagementController {

	@Autowired
	private StudentManager studentManager;

	@Autowired
	private RoleTypeManager roleTypeManager;
	
	@Autowired
	private WorkbookService workbookService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(
			@ModelAttribute("queryForm") StudentInfoQueryForm queryForm,
			@RequestParam(required = false, value = "page") Integer page,
			Model model, HttpServletRequest request) {
		int pageSize = 15;
		if (queryForm.getPageSize() != null) {
			pageSize = queryForm.getPageSize();
		}

		Page<Student> studentList = studentManager.getPaged(queryForm,
				RequestParamsUtil.getPageRequest(page, pageSize));

		model.addAttribute("pageObjects", studentList);
		model.addAttribute("queryForm", queryForm);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));

		return "/admin/studentManage/list";
	}
	
	@RequestMapping(value = "create")
	public String create(Model model) {
		
		model.addAttribute("student", new Student());
		model.addAttribute("action", "create");

		return "/admin/studentManage/editBaseInfo";
	}
	
	@RequestMapping(value = "edit")
	public String edit(@RequestParam(value = "id", required = false)Long id, Model model) {
		if(id != null){
			model.addAttribute("student", studentManager.findStudentById(id));
			model.addAttribute("action", "edit");
		}
		
		return "/admin/studentManage/editBaseInfo";
	}

	@RequestMapping(value = "save")
	public void save(@ModelAttribute("student") Student student){
		if(student.getId() == null){
			String defaultPassword = ProjectProperties
					.getProperty("defaultPassword");
			
			student.setPassword(new Md5Hash(defaultPassword).toHex());
			student.setStatus(1);
			student.setRole(roleTypeManager.getRoleTypeByCode("student"));
		}
		
		studentManager.save(student);
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public void resetPassword(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "id") Long id)
			throws IOException {
		if (id != null) {
			Student student = studentManager.findStudentById(id);

			String defaultPassword = ProjectProperties
					.getProperty("defaultPassword");
			String hashedPassword = new Md5Hash(defaultPassword).toHex();
			student.setPassword(hashedPassword);
			studentManager.save(student);

			log.info("[{}] reset password for student [{}]", request.getSession()
					.getAttribute("loginName"), student.getLoginName());
			response.getWriter().print(true);
		} else {
			response.getWriter().print(false);
		}
	}
	
	@RequestMapping(value = "/batchImport")
	public String batchImport(){
		return "/admin/studentManage/importExcel";
	}
	
	@RequestMapping(value = "/doImport", method = RequestMethod.POST)
	public String importStudentAccountInfo(@RequestParam(value = "file") MultipartFile file, Model model, HttpServletRequest request){
		log.info("[{}] execute batchImportStudentAccountInfo operation.",	request.getSession().getAttribute("loginName"));
		
		try{
			if(!file.isEmpty()){
				ImportResult importResult = workbookService.importStudentAccountInfoFromExcel(file);
				
				model.addAttribute("result", importResult);
			}
		}catch(Exception e){
			ExceptionLog.log(e);
		}
		
		return "/admin/studentManage/importExcel";
	}

	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@ResponseBody
	public String importStudentInfoFromExcel(
			@RequestParam(value = "file") MultipartFile file,
			HttpServletRequest request) {
		try {
			workbookService.importStudentInfoFromExcel(file);
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}

		return "true";
	}

	@RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
	public void exportStudentInfoToExcel(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		ViewExcel viewExcel = new ViewExcel("学生信息列表.xls");

		Map<String, Object> obj = null;
		HSSFWorkbook hssfWorkbook = workbookService
				.generateStudentInfoWorkbook(studentManager.findAll());
		try {
			viewExcel.buildExcelDocument(obj, hssfWorkbook, request, response);
		} catch (Exception e) {
			ExceptionLog.log(e);
		}

		model.addAttribute("viewExcel", viewExcel);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Long id, HttpServletRequest request) {
		studentManager.disableStudent(id);

		log.info("[{}] delete student whose id is :{}",
				request.getAttribute("loginName"), id);
		return "redirect:/admin/studentManage/list";
	}
}
