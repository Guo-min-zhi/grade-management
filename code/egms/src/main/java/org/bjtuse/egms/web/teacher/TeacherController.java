package org.bjtuse.egms.web.teacher;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.bjtuse.egms.util.RequestParamsUtil;
import org.bjtuse.egms.web.admin.ViewExcel;
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
import org.springframework.web.bind.annotation.ResponseBody;
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

	@RequestMapping(value = "/password", method = RequestMethod.GET)
	public String toPassword() {
		return "teacher/teacherChangePassword";
	}

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
		return "teacher/teacherManagement";
	}

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

	@RequestMapping(value = "/certificate/{certificateId}", method = RequestMethod.GET)
	public String certificateInfo(@PathVariable Long certificateId,
			ModelMap model, HttpServletRequest request) {

		String loginName = request.getSession().getAttribute("loginName")
				.toString();
		Teacher verifyTeacher = teacherManager
				.findTeacherByLoginName(loginName);

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
}
