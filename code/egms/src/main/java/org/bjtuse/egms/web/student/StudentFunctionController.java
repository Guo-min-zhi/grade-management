package org.bjtuse.egms.web.student;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bjtuse.egms.repository.entity.CertificateScore;
import org.bjtuse.egms.repository.entity.CertificateType;
import org.bjtuse.egms.repository.entity.Student;
import org.bjtuse.egms.service.CertificateScoreManager;
import org.bjtuse.egms.service.CertificateTypeManager;
import org.bjtuse.egms.service.StudentManager;
import org.bjtuse.egms.util.ExceptionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class StudentFunctionController {

	@Autowired
	private StudentManager studentManager;
	@Autowired
	private CertificateScoreManager certificateScoreManager;
	@Autowired
	private CertificateTypeManager certificateTypeManager;
	
	
	@RequestMapping(value = "/student/modifyInfo", method = RequestMethod.POST)
	@ResponseBody
	public String modifyInfo(String studentId, String studentName,
			String gender, String studentCollege, String studentMajor,String studentGrade, String studentClass,
			String studentIdentity, String studentPhone, ModelMap model, HttpServletRequest request) {

		String loginName = request.getSession().getAttribute("loginName").toString();;
		Student student = studentManager.findUserByLoginName(loginName);
		student.setName(studentName);
		student.setGender(Integer.parseInt(gender));
		student.setCollege(studentCollege);
		student.setMajor(studentMajor);
		student.setGrade(studentGrade);
		student.setClassNum(studentClass);
		student.setIdentityNum(studentIdentity);
		student.setPhoneNum(studentPhone);
		studentManager.save(student);
		return "1";
	}

	@RequestMapping(value = "/student/changePassword", method = RequestMethod.GET)
	public String changePassword(String oldPass, String newPass,
			String againPass, ModelMap model) {

		return "student/studentChangePassword";
	}

	@RequestMapping(value = "/student/uploadPersonPhoto", method = RequestMethod.POST)
	@ResponseBody
	public String uploadPersonPhoto(@RequestParam MultipartFile file, String studentId, 
			ModelMap model, HttpServletRequest request) {
		String loginName = request.getSession().getAttribute("loginName").toString();
		Student student = studentManager.findUserByLoginName(loginName);

		String projectName = request.getContextPath();
		String ctxPath = request.getSession().getServletContext()
				.getRealPath("/")
				+ File.separatorChar + "student-photos";
		System.out.println(ctxPath);
		File fileDir = new File(ctxPath);
		if (!fileDir.exists())
			fileDir.mkdirs();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileTime = sdf.format(new Date());
		String fileExt = file.getOriginalFilename()
				.substring(file.getOriginalFilename().lastIndexOf(".") + 1)
				.toLowerCase();
		// 按照 学号+时间  格式来保存照片
		String newFilename = studentId + fileTime + "." + fileExt;
		File uploadFile = new File(ctxPath + File.separatorChar + newFilename);
		student.setPhoto(projectName+"/student-photos/" +  newFilename);
		studentManager.save(student);
		try {
			FileCopyUtils.copy(file.getBytes(), uploadFile);
		} catch (IOException e) {
			ExceptionLog.log(e);
		}
		return projectName+"/student-photos/" + newFilename;
	}
	
	@RequestMapping(value = "/student/uploadCertificateOriginalPhoto", method = RequestMethod.POST)
	@ResponseBody
	public String uploadCertificateOriginalPhoto(@RequestParam MultipartFile file, String studentId, 
			String type, ModelMap model, HttpServletRequest request) {

		String projectName = request.getContextPath();
		// 证书照片临时存放路径
		String ctxPath = request.getSession().getServletContext().getRealPath("/") + File.separatorChar + "tmp";
		
		File fileDir = new File(ctxPath);
		if (!fileDir.exists())
			fileDir.mkdirs();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileTime = sdf.format(new Date());   // 照片生成时间
		String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();  // 照片名后缀
		// 按照 学号+时间  格式来保存照片
		String newFilename;
		if("certificate".equals(type))
			newFilename = studentId + fileTime + "." + fileExt;
		else
			newFilename = studentId + fileTime+ "-check" + "." + fileExt;
		File uploadFile = new File(ctxPath + File.separatorChar +  newFilename);

		try {
			FileCopyUtils.copy(file.getBytes(), uploadFile);
		} catch (IOException e) {
			ExceptionLog.log(e);
		}
		// 返回临时文件的路径
		return projectName+"/tmp/" + newFilename;
	}
	
	@RequestMapping(value = "/student/certificate", method = RequestMethod.POST)
	public String certificateUpload(Long certificateId, String certificateType, String studentGrade, String studentFinalGrade,
			String certificateOriginalUrl, String certificateURL, String certificateCheckPhotoUrl, 
			String certificateTime, ModelMap model, HttpServletRequest request){
		
		String studentId = request.getSession().getAttribute("loginName").toString();
		
		Student student = studentManager.findUserByLoginName(studentId);
		if(StringUtils.isNotBlank(student.getPhoneNum()) && StringUtils.isNotBlank(student.getPhoto())){
			String projectName = request.getContextPath();
			CertificateScore certificateScore = null;
			if(certificateId == null){
				certificateScore = new CertificateScore();
				certificateScore.setVerifyTimes(0);
			}else{
				certificateScore = certificateScoreManager.findCertificateScoreById(certificateId);
			}
			// 设置对象属性
			certificateScore.setStudentInfo(studentManager.findUserByLoginName(studentId));
			CertificateType certificateTypeObj = certificateTypeManager.getCertificateById(Integer.parseInt(certificateType));
			certificateScore.setCertificateType(certificateTypeObj);
			certificateScore.setSourceScore(Float.parseFloat(studentGrade));
			certificateScore.setTranslatedScore(Float.parseFloat(studentFinalGrade));
			certificateScore.setSubmitTime(new Timestamp(System.currentTimeMillis()));
			
			// 生成新的照片
			File tmpFile = new File(request.getSession().getServletContext().getRealPath("/") + File.separatorChar + "tmp" + File.separatorChar
					+ certificateOriginalUrl.substring(certificateOriginalUrl.lastIndexOf("/")+1));
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String fileTime = sdf.format(new Date());   // 照片生成时间
			String ctxPath = request.getSession().getServletContext().getRealPath("/") + File.separatorChar + "certificate-photos";
			File fileDir = new File(ctxPath);
			if (!fileDir.exists())
				fileDir.mkdirs();
			String photoExt = certificateOriginalUrl.substring(certificateOriginalUrl.lastIndexOf(".")+1).toLowerCase();
			String newFilename = studentId + certificateTypeObj.getCertificateName() + fileTime + "_" + studentFinalGrade+"."+ photoExt;
			File finalPhoto = new File(ctxPath + File.separatorChar +  newFilename);
			try {
				FileUtils.copyFile(tmpFile, finalPhoto);
			} catch (IOException e2) {
				ExceptionLog.log(e2);
			}
			
			//设置对象属性
			certificateScore.setCerfificatePhotoUrl(projectName+"/certificate-photos/"+ newFilename);
			certificateScore.setCheckWebsite(certificateURL);
			
			File tmpFile2 = new File(request.getSession().getServletContext().getRealPath("/") + File.separatorChar + "tmp" + File.separatorChar
					 + certificateCheckPhotoUrl.substring(certificateCheckPhotoUrl.lastIndexOf("/")+1));
			String ctxPath2 = request.getSession().getServletContext().getRealPath("/") + File.separatorChar + "certificate-check";
			File fileDir2 = new File(ctxPath2);
			if (!fileDir2.exists())
				fileDir2.mkdirs();
			String photoExt2 = certificateCheckPhotoUrl.substring(certificateCheckPhotoUrl.lastIndexOf(".")+1).toLowerCase();
			String newFilename2 = studentId + certificateTypeObj.getCertificateName() + fileTime + "_" + studentFinalGrade+"_check."+ photoExt2;
			File finalPhoto2 = new File(ctxPath2 + File.separatorChar +  newFilename2);
			try {
				FileUtils.copyFile(tmpFile2, finalPhoto2);
			} catch (IOException e1) {
				ExceptionLog.log(e1);
			}
			
			certificateScore.setCheckWebsiteScreenshot(projectName+"/certificate-check/"+ newFilename2);
			certificateScore.setStatus(new Integer(1));
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			
			try {
				certificateScore.setCertificateAcquireTime(new java.sql.Date(sdf2.parse(certificateTime).getTime()));
			} catch (ParseException e) {
				ExceptionLog.log(e);
			}
			certificateScore.setVerifyTeacherA(null);
			certificateScore.setVerifyTimeA(null);
			certificateScore.setVerifyStatusA(null);
			certificateScore.setCommentA(null);
			certificateScore.setVerifyTeacherB(null);
			certificateScore.setVerifyTimeB(null);
			certificateScore.setVerifyStatusB(null);
			certificateScore.setCommentB(null);
			certificateScoreManager.saveCertificateSocre(certificateScore);
			model.addAttribute("result", "success");
			List<CertificateType> allCertificateType = certificateTypeManager.getAllCertificateType();
			model.addAttribute("certificateTypes", allCertificateType);
		}else{
			model.addAttribute("result", "notComplete");
			List<CertificateType> allCertificateType = certificateTypeManager.getAllCertificateType();
			model.addAttribute("certificateTypes", allCertificateType);
		}
		
		return "student/certificateManagement";
	}
}
