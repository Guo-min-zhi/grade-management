package org.bjtuse.egms.web.admin;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.bjtuse.egms.repository.entity.Teacher;
import org.bjtuse.egms.service.RoleTypeManager;
import org.bjtuse.egms.service.TeacherManager;
import org.bjtuse.egms.service.WorkbookService;
import org.bjtuse.egms.util.ExceptionLog;
import org.bjtuse.egms.util.ProjectProperties;
import org.bjtuse.egms.util.RequestParamsUtil;
import org.bjtuse.egms.web.admin.form.TeacherInfoQueryForm;
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
@RequestMapping("/admin/teacherManage")
public class TeacherManagementController {

	@Autowired
	private TeacherManager teacherManager;

	@Autowired
	private RoleTypeManager roleTypeManager;
	
	@Autowired
	private WorkbookService workbookService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(
			@ModelAttribute("queryForm") TeacherInfoQueryForm queryForm,
			@RequestParam(required = false, value = "page") Integer page,
			Model model, HttpServletRequest request) {
		int pageSize = 15;
		if (queryForm.getPageSize() != null) {
			pageSize = queryForm.getPageSize();
		}

		 Page<Teacher> teacherList = teacherManager.getPaged(queryForm,
				RequestParamsUtil.getPageRequest(page, pageSize));

		model.addAttribute("pageObjects", teacherList);
		model.addAttribute("queryForm", queryForm);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));

		return "/admin/teacherManage/list";
	}
	
	@RequestMapping(value = "create")
	public String create(Model model) {
		
		model.addAttribute("teacher", new Teacher());
		model.addAttribute("action", "create");

		return "/admin/teacherManage/editBaseInfo";
	}
	
	@RequestMapping(value = "edit")
	public String edit(@RequestParam(value = "id", required = false)Long id, Model model) {
		if(id != null){
			model.addAttribute("teacher", teacherManager.findTeacherById(id));
			model.addAttribute("action", "edit");
		}
		
		return "/admin/teacherManage/editBaseInfo";
	}

	@RequestMapping(value = "save")
	public void save(@ModelAttribute("teacher") Teacher teacher){
		if(teacher.getId() == null){
			String defaultPassword = ProjectProperties
					.getProperty("defaultPassword");
			
			teacher.setPassword(new Md5Hash(defaultPassword).toHex());
			teacher.setStatus(1);
			teacher.setRole(roleTypeManager.getRoleTypeByCode("teacher"));
		}
		
		teacherManager.saveTeacher(teacher);
	}
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public void resetPassword(@RequestParam(value = "id") Long id,
			HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		if (id != null) {
			Teacher teacher = teacherManager.findTeacherById(id);

			String defaultPassword = ProjectProperties
					.getProperty("defaultPassword");
			teacher.setPassword(new Md5Hash(defaultPassword).toHex());
			teacherManager.saveTeacher(teacher);

			log.info("[{}] reset password for teacher [{}]", 
					request.getSession().getAttribute("loginName"),
					teacher.getLoginName());
			response.getWriter().print(true);
		} else {
			response.getWriter().print(false);
		}
	}

	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@ResponseBody
	public String importTeacherInfoFromExcel(
			@RequestParam(value = "file") MultipartFile file,
			HttpServletRequest request) {
		try {
			if(!file.isEmpty()){
				workbookService.importTeacherInfoFromExcel(file);
			}
		} catch (Exception e) {
			ExceptionLog.log(e);
			return "false";
		}

		return "true";
	}

	@RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
	public void exportStudentInfoToExcel(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		ViewExcel viewExcel = new ViewExcel("教师信息列表.xls");

		Map<String, Object> obj = null;
		HSSFWorkbook hssfWorkbook = workbookService
				.generateTeacherInfoWorkbook(teacherManager.findAll());
		try {
			viewExcel.buildExcelDocument(obj, hssfWorkbook, request, response);
		} catch (Exception e) {
			ExceptionLog.log(e);
		}

		model.addAttribute("viewExcel", viewExcel);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Long id, HttpServletRequest request) {
		teacherManager.disableTeacher(id);

		log.info("[{}] delete teacher whose id is :{}", request.getSession().getAttribute("loginName"), id);
		return "redirect:/admin/teacherManage/list";
	}

}
