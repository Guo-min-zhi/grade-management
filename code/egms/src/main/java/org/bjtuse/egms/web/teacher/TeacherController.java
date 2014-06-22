package org.bjtuse.egms.web.teacher;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.bjtuse.egms.repository.entity.CertificateScore;
import org.bjtuse.egms.repository.entity.CertificateType;
import org.bjtuse.egms.repository.entity.Student;
import org.bjtuse.egms.repository.entity.Teacher;
import org.bjtuse.egms.service.CertificateScoreManager;
import org.bjtuse.egms.service.CertificateTypeManager;
import org.bjtuse.egms.service.TeacherManager;
import org.bjtuse.egms.service.WorkbookService;
import org.bjtuse.egms.util.CertificateStatus;
import org.bjtuse.egms.util.ExceptionLog;
import org.bjtuse.egms.util.ImportGrade;
import org.bjtuse.egms.util.ProjectProperties;
import org.bjtuse.egms.util.ImportResult;
import org.bjtuse.egms.util.RequestParamsUtil;
import org.bjtuse.egms.web.admin.ViewExcel;
import org.bjtuse.egms.web.teacher.form.CertificateComplexQueryForm;
import org.bjtuse.egms.web.teacher.form.CertificateFastQueryForm;
import org.bjtuse.egms.web.teacher.form.QueryComprehensiveForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/teacher")
public class TeacherController {

	@Autowired
	private TeacherManager teacherManager;

	@Autowired
	private CertificateScoreManager certificateScoreManager;

	@Autowired
	private CertificateTypeManager certificateTypeManager;

	@Autowired
	private WorkbookService workbookService;

	/**
	 *  路由到修改密码页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/password", method = RequestMethod.GET)
	public String toPassword() {
		return "teacher/password";
	}

	/**
	 * 路由到成绩管理页面
	 * 
	 * @param queryFastForm
	 * @param queryComplexForm
	 * @param page
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/management", method = RequestMethod.GET)
	public String toManagement(
			@ModelAttribute("queryFastForm") CertificateFastQueryForm queryFastForm,
			@ModelAttribute("queryComplexForm") CertificateComplexQueryForm queryComplexForm,
			@RequestParam(required = false, value = "page") Integer page,
			ModelMap model, HttpServletRequest request) {
		int pageSize = 10;
		if (queryFastForm.getPageSize() != null) {
			pageSize = queryFastForm.getPageSize();
		}
		Page<CertificateScore> certificateScoreList;

		certificateScoreList = certificateScoreManager.getPaged(queryFastForm,
				RequestParamsUtil.getPageRequest(page, pageSize));

		List<CertificateType> types = certificateTypeManager
				.getAllCertificateType();

		model.addAttribute("certificateTypes", types);
		Map<Integer, String> status = new HashMap<Integer, String>();
		status.put(1, "待查验");
		status.put(2, "查验通过");
		status.put(3, "查验不通过");
		status.put(4, "一审通过");
		status.put(5, "已归档");
		status.put(CertificateStatus.IMPORT, "教务处系统导入");
		model.addAttribute("mapStatus", status);
		model.addAttribute("pageObjects", certificateScoreList);
		model.addAttribute("queryForm", queryFastForm);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));
		return "teacher/management";
	}

	/**
	 *  修改密码的具体实现
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @param againPassword
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public String modifyTeacherPassword(String oldPassword, String newPassword,
			String againPassword, ModelMap model, HttpServletRequest request) {
		String loginName = request.getSession().getAttribute("loginName")
				.toString();
		Teacher teacher = teacherManager.findTeacherByLoginName(loginName);
		if (teacher != null
				&& StringUtils.equals(teacher.getPassword(), new Md5Hash(
						oldPassword).toHex())) {
			teacher.setPassword(new Md5Hash(newPassword).toHex());
			teacherManager.saveTeacher(teacher);
			model.addAttribute("result", "1");
		} else {
			model.addAttribute("result", "0");
		}
		return "teacher/teacherChangePassword";
	}

	/**
	 * 跳转到证书详情页面
	 * 
	 * @param certificateId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/certificate/{certificateId}", method = RequestMethod.GET)
	public String certificateInfo(@PathVariable Long certificateId,
			ModelMap model, HttpServletRequest request) {

		String loginName = request.getSession().getAttribute("loginName").toString();
		Teacher verifyTeacher = teacherManager.findTeacherByLoginName(loginName);
		
		CertificateScore certificateScore = certificateScoreManager
				.findCertificateScoreById(certificateId);
		if (certificateScore.getVerifyTeacherB() != null
				&& !certificateScore.getVerifyTeacherB().getLoginName()
						.equals(verifyTeacher.getLoginName())) {
			// 该用户为A老师，并且B老师已经评阅
			model.addAttribute("permission", "0");
		} else if (certificateScore.getVerifyTeacherB() != null
				&& certificateScore.getVerifyTeacherB().getLoginName()
						.equals(verifyTeacher.getLoginName())
				&& certificateScore.getStatus() == CertificateStatus.NOPASSCHECKED) {
			// 该用户为B老师, 但A老师已经将证书改为不通过
			model.addAttribute("permission", "0");
		} else {
			model.addAttribute("permission", "1");
		}
		Student student = certificateScore.getStudentInfo();
		Map<Integer, String> status = new HashMap<Integer, String>();
		status.put(1, "待查验");
		status.put(2, "查验通过");
		status.put(3, "查验不通过");
		status.put(4, "一审通过");
		status.put(5, "已归档");
		status.put(CertificateStatus.IMPORT, "教务处系统导入");
		model.addAttribute("mapStatus", status);
		model.addAttribute("certificate", certificateScore);
		model.addAttribute("student", student);
		return "teacher/certificateInfo";
	}

	@RequestMapping(value = "/nopass", method = RequestMethod.POST)
	public String noPass(Long certificateId, String comment, ModelMap model,
			HttpServletRequest request) {
		String loginName = request.getSession().getAttribute("loginName")
				.toString();
		CertificateScore certificateScore = certificateScoreManager
				.findCertificateScoreById(certificateId);
		Teacher verifyTeacher = teacherManager
				.findTeacherByLoginName(loginName);

		if (certificateScore.getVerifyTeacherA() == null
				|| (certificateScore.getVerifyTeacherA() != null
						&& certificateScore.getVerifyTeacherA().getLoginName()
								.equals(verifyTeacher.getLoginName()) && certificateScore
						.getVerifyTeacherB() == null)) {
			certificateScore.setVerifyTeacherA(verifyTeacher);
			certificateScore.setStatus(CertificateStatus.NOPASSCHECKED);
			certificateScore.setCommentA(comment);
			certificateScore
					.setVerifyTimes(certificateScore.getVerifyTimes() + 1);
			Timestamp verifyTime = new Timestamp(System.currentTimeMillis());
			certificateScore.setVerifyTimeA(verifyTime);
			certificateScoreManager.saveCertificateSocre(certificateScore);

			log.info(
					"Operator:{}, Result: NOPASS, Target:[certificateid:{}, studentName:{}]",
					loginName, certificateId, certificateScore.getStudentInfo()
							.getName());
		} else if (certificateScore.getVerifyTeacherB() == null
				|| (certificateScore.getVerifyTeacherB() != null && certificateScore
						.getVerifyTeacherB().getLoginName()
						.equals(verifyTeacher.getLoginName()))) {
			certificateScore.setVerifyTeacherB(verifyTeacher);
			certificateScore.setStatus(CertificateStatus.NOPASSCHECKED);
			certificateScore
					.setVerifyTimes(certificateScore.getVerifyTimes() + 1);
			Timestamp verifyTime = new Timestamp(System.currentTimeMillis());
			certificateScore.setVerifyTimeB(verifyTime);
			certificateScore.setCommentB(comment);
			certificateScoreManager.saveCertificateSocre(certificateScore);

			log.info(
					"Operator:{}, Result: NOPASS, Target:[certificateid:{}, studentName:{}]",
					loginName, certificateId, certificateScore.getStudentInfo()
							.getName());
		}
		return "redirect:/teacher/certificate/" + certificateId;
	}

	@RequestMapping(value = "/passCertificate", method = RequestMethod.POST)
	public String passCertificate(Long certificateId,
			HttpServletRequest request, RedirectAttributes attr) {
		String loginName = request.getSession().getAttribute("loginName")
				.toString();
		CertificateScore certificateScore = certificateScoreManager
				.findCertificateScoreById(certificateId);
		Teacher verifyTeacher = teacherManager
				.findTeacherByLoginName(loginName);

		if (certificateScore.getVerifyTeacherA() == null
				|| (certificateScore.getVerifyTeacherA() != null
						&& certificateScore.getVerifyTeacherA().getLoginName()
								.equals(verifyTeacher.getLoginName()) && certificateScore
						.getVerifyTeacherB() == null)) {
			certificateScore.setCommentA(null);
			certificateScore.setVerifyTeacherA(verifyTeacher);
			certificateScore.setStatus(CertificateStatus.ONCECHECK);
			certificateScore
					.setVerifyTimes(certificateScore.getVerifyTimes() + 1);
			Timestamp verifyTime = new Timestamp(System.currentTimeMillis());
			certificateScore.setVerifyTimeA(verifyTime);
			certificateScoreManager.saveCertificateSocre(certificateScore);

			log.info(
					"Operator:{}, Result: PASS, Target:[certificateid:{}, studentName:{}]",
					loginName, certificateId, certificateScore.getStudentInfo()
							.getName());
		} else if (certificateScore.getVerifyTeacherB() == null
				|| (certificateScore.getVerifyTeacherB() != null && certificateScore
						.getVerifyTeacherB().getLoginName()
						.equals(verifyTeacher.getLoginName()))) {
			certificateScore.setCommentB(null);
			certificateScore.setVerifyTeacherB(verifyTeacher);
			certificateScore.setStatus(CertificateStatus.PASSCHECKED);
			certificateScore
					.setVerifyTimes(certificateScore.getVerifyTimes() + 1);
			Timestamp verifyTime = new Timestamp(System.currentTimeMillis());
			certificateScore.setVerifyTimeB(verifyTime);
			certificateScoreManager.saveCertificateSocre(certificateScore);

			log.info(
					"Operator:{}, Result: PASS, Target:[certificateid:{}, studentName:{}]",
					loginName, certificateId, certificateScore.getStudentInfo()
							.getName());
		} else if (certificateScore.getVerifyTeacherA().getLoginName()
				.equals(loginName)) {
			attr.addFlashAttribute("result", "已经有两位老师对该证书进行过审核了。");
		}
		return "redirect:/teacher/certificate/" + certificateId;
	}

	@RequestMapping(value = "/pass", method = RequestMethod.POST)
	@ResponseBody
	public String passCertificates(String certificates,
			HttpServletRequest request) {
		String loginName = request.getSession().getAttribute("loginName")
				.toString();
		Teacher verifyTeacher = teacherManager
				.findTeacherByLoginName(loginName);
		String[] strs = certificates.split(",");
		List<CertificateScore> lists = new ArrayList<CertificateScore>();

		Iterator<String> iterator = Arrays.asList(strs).iterator();
		while (iterator.hasNext()) {
			Long cid = Long.parseLong(iterator.next());
			CertificateScore certificateScore = certificateScoreManager
					.findCertificateScoreById(cid);
			if (certificateScore.getVerifyTeacherA() == null) {
				// 该证书还没有老师评阅,所以该老师为第一个审核通过该证书的老师，不设置状态，只有两个老师都通过才设置状态
				certificateScore.setVerifyTeacherA(verifyTeacher);
				certificateScore.setVerifyTimes(certificateScore
						.getVerifyTimes() + 1);
				Timestamp verifyTime = new Timestamp(System.currentTimeMillis());
				certificateScore.setVerifyTimeA(verifyTime);
				certificateScoreManager.saveCertificateSocre(certificateScore);

				log.info(
						"Operator:{}, Result: PASS, Target:[certificateid:{}, studentName:{}]",
						loginName, certificateScore.getId(), certificateScore
								.getStudentInfo().getName());
			} else if (!certificateScore.getVerifyTeacherA().getLoginName()
					.equals(loginName)
					&& certificateScore.getVerifyTeacherB() == null) {
				// 已经有一个老师审核通过该证书，该老师为第二位老师
				certificateScore.setVerifyTeacherB(verifyTeacher);
				certificateScore.setStatus(CertificateStatus.PASSCHECKED);
				certificateScore.setVerifyTimes(certificateScore
						.getVerifyTimes() + 1);
				Timestamp verifyTime = new Timestamp(System.currentTimeMillis());
				certificateScore.setVerifyTimeB(verifyTime);
				certificateScoreManager.saveCertificateSocre(certificateScore);

				log.info(
						"Operator:{}, Result: PASS, Target:[certificateid:{}, studentName:{}]",
						loginName, certificateScore.getId(), certificateScore
								.getStudentInfo().getName());
			}
			lists.add(certificateScore);
		}
		certificateScoreManager.savePatchCertificateScore(lists);
		return "success";
	}

	@RequestMapping(value = "/archive", method = RequestMethod.POST)
	@ResponseBody
	public String archiveCertificates(String certificates,
			HttpServletRequest request) {

		String[] strs = certificates.split(",");
		List<CertificateScore> lists = new ArrayList<CertificateScore>();

		Iterator<String> iterator = Arrays.asList(strs).iterator();
		while (iterator.hasNext()) {
			Long cid = Long.parseLong(iterator.next());
			CertificateScore certificateScore = certificateScoreManager
					.findCertificateScoreById(cid);
			certificateScore.setStatus(CertificateStatus.ARCHIVED);

			log.info(
					"Operator:{}, Result: ARCHIVED, Target:[certificateid:{}, studentName:{}]",
					request.getSession().getAttribute("loginName"),
					certificateScore.getId(), certificateScore.getStudentInfo()
							.getName());
			lists.add(certificateScore);
		}
		certificateScoreManager.savePatchCertificateScore(lists);
		return "success";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteCertificates(String certificates, HttpServletRequest request) {
		String[] strs = certificates.split(",");
		Iterator<String> iterator = Arrays.asList(strs).iterator();
		List<CertificateScore> lists = new ArrayList<CertificateScore>();
		while (iterator.hasNext()) {
			Long cid = Long.parseLong(iterator.next());
			CertificateScore certificateScore = certificateScoreManager.findCertificateScoreById(cid);
			lists.add(certificateScore);
			
			log.info(
					"Operator:{}, Result: DELETE, Target:[certificateid:{}, studentName:{}]",
					request.getSession().getAttribute("loginName"),
					certificateScore.getId(), certificateScore.getStudentInfo()
							.getName());
		}
		certificateScoreManager.deletePatchCertificateScore(lists);
		return "success";
	}

	@RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
	public void exportStudentInfoToExcel(Model model,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "certificates") String certificates) {
		String certificate[] = certificates.split(",");
		List<CertificateScore> certificateScores = new ArrayList<CertificateScore>();
		for (int i = 0; i < certificate.length; i++) {
			CertificateScore c = certificateScoreManager
					.findCertificateScoreById(Long.parseLong(certificate[i]));
			if (c != null) {
				certificateScores.add(c);
			}
		}

		ViewExcel viewExcel = new ViewExcel("证书信息列表.xls");

		Map<String, Object> obj = null;
		HSSFWorkbook hssfWorkbook = workbookService
				.generateCertificateScoreInfoWorkbook(certificateScores);
		try {
			viewExcel.buildExcelDocument(obj, hssfWorkbook, request, response);
		} catch (Exception e) {
			ExceptionLog.log(e);
		}

		model.addAttribute("viewExcel", viewExcel);
	}

	@RequestMapping(value = "/complex", method = RequestMethod.GET)
	public String complexQuery(
			@ModelAttribute("queryFastForm") CertificateFastQueryForm queryFastForm,
			@ModelAttribute("queryComplexForm") CertificateComplexQueryForm queryComplexForm,
			@RequestParam(required = false, value = "page") Integer page,
			ModelMap model, HttpServletRequest request) {
		int pageSize = 10;
		if (queryComplexForm.getPageSize() != null) {
			pageSize = queryComplexForm.getPageSize();
		}
		Page<CertificateScore> certificateScoreList;

		certificateScoreList = certificateScoreManager.getComplexPaged(
				queryComplexForm,
				RequestParamsUtil.getPageRequest(page, pageSize));

		List<CertificateType> types = certificateTypeManager
				.getAllCertificateType();

		model.addAttribute("certificateTypes", types);
		Map<Integer, String> status = new HashMap<Integer, String>();
		status.put(1, "待查验");
		status.put(2, "查验通过");
		status.put(3, "查验不通过");
		status.put(4, "一审通过");
		status.put(5, "已归档");
		status.put(CertificateStatus.IMPORT, "教务处系统导入");
		model.addAttribute("mapStatus", status);
		model.addAttribute("pageObjects", certificateScoreList);
		model.addAttribute("queryForm", queryComplexForm);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));
		return "teacher/teacherManagement";
	}
	
	/**
	 *  路由到成绩导入页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "importGrade", method = RequestMethod.GET)
	public String importGrade(Model model){
		List<CertificateType> cts = certificateTypeManager.getCertificateTypesImportByTeacher();
		
		model.addAttribute("cts", cts);
		return "teacher/importGrade";
	}
	
	@RequestMapping(value = "/uploadGradeExcel", method = RequestMethod.POST)
	public String uploadGradeExcel(@RequestParam(value = "file") MultipartFile file, Model model, HttpServletRequest request){
		try{
			if(!file.isEmpty()){
				String ct = request.getParameter("ct");
//				String loginName = request.getSession().getAttribute("loginName").toString();
				String loginName = "admin";
				String ctxPath = request.getSession().getServletContext().getRealPath("/")
						+ File.separatorChar + "temp" + File.separator + loginName;
				//先将导入成绩的Excel存起来，待会真正导入之后再删除
				File fileDir = new File(ctxPath);
				if (!fileDir.exists()){
					fileDir.mkdirs();
				}else{
					//清空临时文件夹
					File[] files = fileDir.listFiles();
					for(File f : files){
						f.delete();
					}
					fileDir.mkdirs();
				}
				
				String fileExt = file.getOriginalFilename()
						.substring(file.getOriginalFilename().lastIndexOf(".") + 1)
						.toLowerCase();
				
				String newFileName = UUID.randomUUID().toString() + "." + fileExt;
				
				File uploadFile = new File(ctxPath + File.separatorChar + newFileName);
				try {
					FileCopyUtils.copy(file.getBytes(), uploadFile);
				} catch (IOException e) {
					ExceptionLog.log(e);
				}
				//保存临时文件结束
				List<String> fieldNames = workbookService.getFiledNamesFromExcel(file);
				
				model.addAttribute("tempFile", newFileName);
				model.addAttribute("fields", fieldNames);
				model.addAttribute("ct", ct);
			}
		}catch(Exception e){
			ExceptionLog.log(e);
		}
		
		return "teacher/matchField";
	}
	
	@RequestMapping(value = "doImportGrade", method = RequestMethod.POST)
	public String doImportGrade(Model model, HttpServletRequest request){
		try{
//			String loginName = request.getSession().getAttribute("loginName").toString();
			String loginName = "admin";
			String tempFile = request.getParameter("tempFile");
			String ct = request.getParameter("ct");
			
			CertificateType certificateType = certificateTypeManager.getCertificateById(Integer.parseInt(ct));
			
			String ctxPath = request.getSession().getServletContext().getRealPath("/")
					+ File.separatorChar + "temp" + File.separator + loginName;
			
			ImportGrade importGrade = new ImportGrade();
			String studentNum = request.getParameter("studentNum").trim();
			if(StringUtils.isBlank(studentNum)){
				model.addAttribute("errorMsg", "学号必须有对应的Excel字段！");
				
				return "teacher/importGradeResult";
			}else{
				importGrade.setStudentNum(Integer.parseInt(studentNum));
			}
			
			String studentName = request.getParameter("studentName").trim();
			if(StringUtils.isNotBlank(studentName)){
				importGrade.setStudentName(Integer.parseInt(studentName));
			}
			
			String sourceScore =  request.getParameter("sourceScore").trim();
			if(StringUtils.isNotBlank(sourceScore)){
				importGrade.setSourceScore(Integer.parseInt(sourceScore));
			}
			
			String gradeA = request.getParameter("gradeA").trim();
			if(StringUtils.isNotBlank(gradeA)){
				importGrade.setGradeA(Integer.parseInt(gradeA));
			}
			
			String gradeB = request.getParameter("gradeB").trim();
			if(StringUtils.isNotBlank(gradeB)){
				importGrade.setGradeB(Integer.parseInt(gradeB));
			}
			
			String gradeC = request.getParameter("gradeC").trim();
			if(StringUtils.isNotBlank(gradeC)){
				importGrade.setGradeC(Integer.parseInt(gradeC));
			}
			
			String oralScore = request.getParameter("oralScore");
			if(StringUtils.isNotBlank(oralScore)){
				importGrade.setOralScore(Integer.parseInt(oralScore));
			}
			
			String writtenScore = request.getParameter("writtenScore");
			if(StringUtils.isNotBlank(writtenScore)){
				importGrade.setWrittenScore(Integer.parseInt(writtenScore));
			}
			
			if(!importGrade.checkGrade()){
				model.addAttribute("errorMsg", "没有选择相应的成绩字段！");
				
				return "teacher/importGradeResult";
			}
			
			File tempExcel = new File(ctxPath + File.separatorChar + tempFile);
			
			ImportResult importResult = workbookService.importGradeFromExcel(tempExcel, importGrade, certificateType);
			
			model.addAttribute("result", importResult);
		}catch (NumberFormatException e) {
			ExceptionLog.log(e);
			model.addAttribute("errorMsg", "Excel表字段序号填写错误！");
		}catch (Exception e) {
			ExceptionLog.log(e);
			model.addAttribute("errorMsg", "导入成绩错误！");
		}
		
		return "teacher/importGradeResult";
	}
	
	/**
	 * 路由到综合成绩管理页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="manage", method=RequestMethod.GET)
	public String manageComprehensive(
			@ModelAttribute("queryComprehensiveForm") QueryComprehensiveForm queryComprehensiveForm,
			@RequestParam(required = false, value = "page") Integer page,
			Model model, HttpServletRequest request){
		// 获得成绩来源里的成绩类型
		List<CertificateType> cts = certificateTypeManager.getCertificateTypesImportByTeacher();
		model.addAttribute("cts", cts);
		
		int pageSize = 10;
		if(queryComprehensiveForm.getPageSize() != null){
			pageSize = queryComprehensiveForm.getPageSize();
		}
		Page<CertificateScore> certificateScoreList;
		certificateScoreList = certificateScoreManager.getComprehensivePaged(queryComprehensiveForm, RequestParamsUtil.getPageRequest(page, pageSize));
		model.addAttribute("pageObjects", certificateScoreList);
		model.addAttribute("queryComprehensiveForm", queryComprehensiveForm);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));
		model.addAttribute("password", ProjectProperties.getProperty("deletePassword"));
		return "teacher/manageComprehensive";
	}
	
	/**
	 * 根据id删除成绩 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String deleteOneCertificate(@PathVariable("id") Long id, HttpServletRequest request){
		certificateScoreManager.deleteCertificateScoreById(id);
		
		log.info("[{}] delete student whose id is : {}", request.getAttribute("loginName"), id);
		return "redirect:/teacher/manage";
	}
	
	/**
	 * 导出查询出来的综合成绩信息
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportComprehensiveExcel", method = RequestMethod.GET)
	public void exportComprehensiveExcel(Model model,
			HttpServletRequest request, HttpServletResponse response,
			String studentNumber, String studentName,
			String startTime, String endTime,
			Integer certificateType, Integer scoreStatus) {
		ViewExcel viewExcel = new ViewExcel("学生综合成绩列表.xls");

		QueryComprehensiveForm queryComprehensiveForm = new QueryComprehensiveForm();
		queryComprehensiveForm.setStudentNumber(studentNumber);
		queryComprehensiveForm.setStudentName(studentName);
		queryComprehensiveForm.setStartTime(startTime);
		queryComprehensiveForm.setEndTime(endTime);
		queryComprehensiveForm.setCertificateType(certificateType);
		queryComprehensiveForm.setScoreStatus(scoreStatus);
		
		Map<String, Object> obj = null;
		HSSFWorkbook hssfWorkbook = workbookService
				.generateComperehensiveScoreWorkbook(certificateScoreManager.findComprehensiveScoreToExport(queryComprehensiveForm));
		try {
			viewExcel.buildExcelDocument(obj, hssfWorkbook, request, response);
		} catch (Exception e) {
			ExceptionLog.log(e);
		}

		model.addAttribute("viewExcel", viewExcel);
	}
	
	/**
	 * 跳转到 综合成绩详情页面
	 * 
	 * @param certificateId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/score/{certificateId}", method = RequestMethod.GET)
	public String comprehensiveScoreInfo(@PathVariable Long certificateId,
			ModelMap model, HttpServletRequest request) {

		CertificateScore certificateScore = certificateScoreManager
				.findCertificateScoreById(certificateId);
		Student student = certificateScore.getStudentInfo();
		Map<Integer, String> status = new HashMap<Integer, String>();
		status.put(1, "待查验");
		status.put(2, "查验通过");
		status.put(3, "查验不通过");
		status.put(4, "一审通过");
		status.put(5, "已归档");
		status.put(CertificateStatus.IMPORT, "教务处系统导入");
		model.addAttribute("mapStatus", status);
		model.addAttribute("certificate", certificateScore);
		model.addAttribute("student", student);
		
		return "teacher/scoreInfo";
	}
}
