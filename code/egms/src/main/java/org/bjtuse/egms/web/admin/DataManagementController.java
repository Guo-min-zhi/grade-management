package org.bjtuse.egms.web.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bjtuse.egms.repository.entity.CertificateScore;
import org.bjtuse.egms.repository.entity.CertificateType;
import org.bjtuse.egms.repository.entity.Student;
import org.bjtuse.egms.service.CertificateScoreManager;
import org.bjtuse.egms.service.CertificateTypeManager;
import org.bjtuse.egms.service.WorkbookService;
import org.bjtuse.egms.util.CertificateStatus;
import org.bjtuse.egms.util.ExceptionLog;
import org.bjtuse.egms.util.RequestParamsUtil;
import org.bjtuse.egms.web.teacher.form.CertificateComplexQueryForm;
import org.bjtuse.egms.web.teacher.form.CertificateFastQueryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/dataManage")
public class DataManagementController {

	@Autowired
	private CertificateScoreManager certificateScoreManager;
	
	@Autowired
	private CertificateTypeManager certificateTypeManager;
	
	@Autowired
	private WorkbookService workbookService;
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public String toManagement(
			@ModelAttribute("queryFastForm") CertificateFastQueryForm queryFastForm,
			@ModelAttribute("queryComplexForm") CertificateComplexQueryForm queryComplexForm,
			@RequestParam(required=false, value="page")Integer page,
			ModelMap model,
			HttpServletRequest request){
		int pageSize = 10;
		if(queryFastForm.getPageSize() != null){
			pageSize = queryFastForm.getPageSize();
		}
		Page<CertificateScore> certificateScoreList;
		
		certificateScoreList = certificateScoreManager.getPaged(queryFastForm, RequestParamsUtil.getPageRequest(page, pageSize));
		
		List<CertificateType> types = certificateTypeManager.getAllCertificateType();
		
		model.addAttribute("certificateTypes", types);
		Map<Integer, String> status = new HashMap<Integer, String>();
		status.put(1, "待查验");
		status.put(2, "查验通过");
		status.put(3, "查验不通过");
		status.put(4, "已删除");
		status.put(5, "已归档");
		status.put(CertificateStatus.IMPORT, "教务处系统导入");
		model.addAttribute("mapStatus", status);
		model.addAttribute("pageObjects", certificateScoreList);
		model.addAttribute("queryForm", queryFastForm);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));
		
		return "admin/dataManage/list";
	}
	@RequestMapping(value="/complex", method = RequestMethod.GET)
	public String complexQuery(
			@ModelAttribute("queryFastForm") CertificateFastQueryForm queryFastForm,
			@ModelAttribute("queryComplexForm") CertificateComplexQueryForm queryComplexForm,
			@RequestParam(required=false, value="page")Integer page,
			ModelMap model,
			HttpServletRequest request){
		int pageSize = 10;
		if(queryComplexForm.getPageSize() != null){
			pageSize = queryComplexForm.getPageSize();
		}
		Page<CertificateScore> certificateScoreList;
		
		certificateScoreList = certificateScoreManager.getComplexPaged(queryComplexForm, RequestParamsUtil.getPageRequest(page, pageSize));
		
		List<CertificateType> types = certificateTypeManager.getAllCertificateType();
		
		model.addAttribute("certificateTypes", types);
		Map<Integer, String> status = new HashMap<Integer, String>();
		status.put(1, "待查验");
		status.put(2, "查验通过");
		status.put(3, "查验不通过");
		status.put(4, "已删除");
		status.put(5, "已归档");
		status.put(CertificateStatus.IMPORT, "教务处系统导入");
		model.addAttribute("mapStatus", status);
		model.addAttribute("pageObjects", certificateScoreList);
		model.addAttribute("queryForm", queryComplexForm);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));
		return "admin/dataManage/list";
	}
	@RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
	public void exportStudentInfoToExcel(Model model, HttpServletRequest request, HttpServletResponse response){
		ViewExcel viewExcel = new ViewExcel("证书信息列表.xls");
		
		Map<String, Object> obj = null;
		HSSFWorkbook hssfWorkbook = workbookService.generateCertificateScoreInfoWorkbook(certificateScoreManager.findCertificateScoreToExport());
		try{
			viewExcel.buildExcelDocument(obj, hssfWorkbook, request, response);
		}catch(Exception e){
			ExceptionLog.log(e);
		}
		
		model.addAttribute("viewExcel", viewExcel);
	}
	@RequestMapping(value="/info/{certificateId}", method = RequestMethod.GET)
	public String certificateInfo(@PathVariable Long certificateId, ModelMap model){
		CertificateScore certificateScore = certificateScoreManager.findCertificateScoreById(certificateId);
		Student student = certificateScore.getStudentInfo();
		Map<Integer, String> status = new HashMap<Integer, String>();
		status.put(1, "待查验");
		status.put(2, "查验通过");
		status.put(3, "查验不通过");
		status.put(4, "已删除");
		status.put(5, "已归档");
		status.put(CertificateStatus.IMPORT, "教务处系统导入");
		model.addAttribute("mapStatus", status);
		model.addAttribute("certificate", certificateScore);
		model.addAttribute("student", student);
		return "admin/dataManage/info";
	}
}
