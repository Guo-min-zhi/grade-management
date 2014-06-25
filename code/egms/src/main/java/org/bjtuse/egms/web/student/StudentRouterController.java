package org.bjtuse.egms.web.student;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.java.dev.eval.Expression;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.bjtuse.egms.repository.entity.CertificateScore;
import org.bjtuse.egms.repository.entity.CertificateType;
import org.bjtuse.egms.repository.entity.Student;
import org.bjtuse.egms.service.CertificateScoreManager;
import org.bjtuse.egms.service.CertificateTypeManager;
import org.bjtuse.egms.service.StudentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StudentRouterController {

	@Autowired
	private StudentManager studentManager;
	@Autowired
	private CertificateTypeManager certificateTypeManager;
	@Autowired
	private CertificateScoreManager certificateScoreManager;

	/**
	 * 
	 * @param model
	 * @return  
	 * @description 完善个人信息的GET事件  
	 * @version currentVersion  
	 * @author minzhi  
	 * @createtime 2013-12-7 下午9:41:14
	 */
	@RequestMapping(value = "/student/info", method = RequestMethod.GET)
	public String studentInfo(ModelMap model, HttpServletRequest request) {
		String loginName = request.getSession().getAttribute("loginName").toString();
		Student student = studentManager.findUserByLoginName(loginName);
		model.addAttribute("student", student);
		return "student/info";
	}

	/**
	 * 
	 * @param studentId
	 * @param studentName
	 * @param studentGender
	 * @param studentCollege
	 * @param studentMajor
	 * @param studentClass
	 * @param studentIdentity
	 * @param studentPhone
	 * @param model
	 * @return  
	 * @description   完善个人信息的POST事件，表单提交  
	 * @version currentVersion  
	 * @author minzhi  
	 * @createtime 2013-12-7 下午9:41:43
	 */
	@RequestMapping(value = "/student/info", method = RequestMethod.POST)
	public String saveStudentInfo(String studentId, String studentName,
			String studentGender, String studentCollege, String studentMajor,
			String studentClass, String studentIdentity, String studentPhone,
			String studentGrade,
			ModelMap model, HttpServletRequest request) {
		String loginName = request.getSession().getAttribute("loginName").toString();
		Student student = studentManager.findUserByLoginName(loginName);
		student.setName(studentName);
		student.setGender(Integer.parseInt(studentGender));
		student.setCollege(studentCollege);
		student.setMajor(studentMajor);
		student.setGrade(studentGrade);
		student.setClassNum(studentClass);
		student.setIdentityNum(studentIdentity);
		student.setPhoneNum(studentPhone);
		studentManager.save(student);
		model.addAttribute("student", student);
		model.addAttribute("result", "success");
		return "student/info";
	}

	/**
	 * 
	 * @return  
	 * @description  路由到修改密码页面  
	 * @version currentVersion  
	 * @author minzhi  
	 * @createtime 2013-12-7 下午11:00:33
	 */
	@RequestMapping(value = "/student/password", method = RequestMethod.GET)
	public String studentPassword() {
		return "student/password";
	}
	
	/**
	 * 
	 * @return  
	 * @description 路由到上传个人近照页面   
	 * @version currentVersion  
	 * @author minzhi  
	 * @createtime 2013-12-7 下午11:00:49
	 */
	@RequestMapping(value = "/student/photo", method = RequestMethod.GET)
	public String uploadPhoto(ModelMap model, HttpServletRequest request) {
		
		String loginName = request.getSession().getAttribute("loginName").toString();
		Student student = studentManager.findUserByLoginName(loginName);
		model.addAttribute("head", student.getPhoto());
		return "student/uploadPhoto";
	}
	
	/**
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @param againPassword
	 * @param model
	 * @return  
	 * @description 修改密码功能实现  
	 * @version currentVersion  
	 * @author minzhi  
	 * @createtime 2013-12-7 下午11:01:14
	 */
	@RequestMapping(value = "/student/password", method = RequestMethod.POST)
	public String changePassword(String oldPassword, String newPassword, String againPassword, ModelMap model, HttpServletRequest request) {
		
		String loginName = request.getSession().getAttribute("loginName").toString();
		Student student = studentManager.findUserByLoginName(loginName);
		String password = student.getPassword();
		String hashPassword = new Md5Hash(oldPassword).toHex();
		if(password.equals(hashPassword)){
			
			student.setPassword(new Md5Hash(newPassword).toHex());
			studentManager.save(student);
			model.addAttribute("result", "1");
		}else{
			model.addAttribute("result", "0");
		}
		return "student/studentChangePassword";
	}
	
	/**
	 * 
	 * @param model
	 * @return  
	 * @description route to the certificate upload page
	 * @version currentVersion  
	 * @author minzhi  
	 * @createtime 2013-12-7 下午11:35:50
	 */
	@RequestMapping(value = "/student/certificate", method = RequestMethod.GET)
	public String goCertificate(@RequestParam(required = false, value = "id")Long id, ModelMap model, HttpServletRequest request){
		
		String studentId = request.getSession().getAttribute("loginName").toString();
		Student student = studentManager.findUserByLoginName(studentId);
		if(StringUtils.isNotBlank(student.getPhoneNum()) && StringUtils.isNotBlank(student.getPhoto())){
			CertificateScore certificateScore;
			if(id != null){
				certificateScore = certificateScoreManager.findCertificateScoreById(id);
				model.addAttribute("certificate", certificateScore);
			}
			List<CertificateType> allCertificateType = certificateTypeManager.getCertificateTypesImportByStudent();
			model.addAttribute("certificateTypes", allCertificateType);
		}else{
			model.addAttribute("result", "notComplete");
			List<CertificateType> allCertificateType = certificateTypeManager.getCertificateTypesImportByStudent();
			model.addAttribute("certificateTypes", allCertificateType);
		}
		
		return "student/certificateManagement";
	}
	
	@RequestMapping(value = "/student/calculateFinal", method = RequestMethod.POST)
	@ResponseBody
	public String calculateFinal(Float grade, Integer certificateTypeId){
		System.out.println("-------------calculate the grade");
		CertificateType certificateType = certificateTypeManager.getCertificateById(certificateTypeId);
		Expression expression = new Expression(certificateType.getFormula());
		
		Map<String, BigDecimal> vars = new HashMap<String, BigDecimal>();
		vars.put("x", new BigDecimal(grade));
		
		grade = expression.eval(vars).setScale(0, BigDecimal.ROUND_UP).floatValue();
		return grade.toString();
	}
	
	/**
	 *  路由到“证书查看”页面
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/student/check", method = RequestMethod.GET)
	public String toCheckView(ModelMap model, HttpServletRequest request){
		
		String studentId = request.getSession().getAttribute("loginName").toString();;
		Student student = studentManager.findUserByLoginName(studentId);
		List<CertificateScore> certificateScores = certificateScoreManager.findCertificatesByStudent(student);
		model.addAttribute("certificates", certificateScores);
		Map<Integer, String> status = new HashMap<Integer, String>();
		status.put(1, "待查验");
		status.put(2, "查验通过");
		status.put(3, "查验不通过");
		status.put(4, "一审通过");
		model.addAttribute("mapStatus", status);
		return "student/certificateCheck";
	}
	
	@RequestMapping(value="/student/certificateDelete", method = RequestMethod.GET)
	public String certificateDelete(@RequestParam(required=false, value="id")Long id, ModelMap model){
		
		if(id != null){
			certificateScoreManager.deleteCertificateScoreById(id);
		}
		return "redirect:/student/check";
	}
}
