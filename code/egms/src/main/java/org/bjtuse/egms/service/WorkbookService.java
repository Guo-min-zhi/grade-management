package org.bjtuse.egms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import net.java.dev.eval.Expression;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.bjtuse.egms.repository.entity.CertificateScore;
import org.bjtuse.egms.repository.entity.CertificateType;
import org.bjtuse.egms.repository.entity.RoleType;
import org.bjtuse.egms.repository.entity.Student;
import org.bjtuse.egms.repository.entity.Teacher;
import org.bjtuse.egms.util.CertificateStatus;
import org.bjtuse.egms.util.ImportGrade;
import org.bjtuse.egms.util.ImportResult;
import org.bjtuse.egms.util.ProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
public class WorkbookService {
	
	@Autowired
	private StudentManager studentManager;
	
	@Autowired
	private CertificateTypeManager certificateTypeManager;
	
	@Autowired
	private CertificateScoreManager certificateScoreManager;
	
	@Autowired
	private TeacherManager teacherManager;
	
	@Autowired
	private RoleTypeManager roleTypeManager;
	
	public ImportResult importStudentAccountInfoFromExcel(MultipartFile file)throws Exception{
		Workbook workbook = null;
		if(file.getOriginalFilename().endsWith("xlsx")){
			workbook = new XSSFWorkbook(file.getInputStream());
		}else{
			workbook = new HSSFWorkbook(file.getInputStream());
		}
		
		Sheet sheet = workbook.getSheetAt(0);
		
		return readStudentAccountInfoFromSheet(sheet);
	}
	
	public ImportResult readStudentAccountInfoFromSheet(Sheet sheet){
		ImportResult importResult = new ImportResult();
		int successCount  = 0;
		List<String> msgs = new ArrayList<String>();
		
		RoleType roleType = roleTypeManager.getRoleTypeByCode("student");
		
		int rowSize = sheet.getLastRowNum();
		//第一行是标题，从第二行开始
		for(int i = 1; i < rowSize; i++){
			Row row = sheet.getRow(i);
			
			String studentNum = null;
			String name = null;
			String gender = null;
			String identityNum = null;
			String phoneNum = null;
			String college = null;
			String major = null;
			String grade = null;
			String classNum = null;
			
			Cell cell0 = row.getCell(0);
			if(cell0 != null){
				cell0.setCellType(Cell.CELL_TYPE_STRING);
				studentNum = cell0.getStringCellValue();
			}
			
			Cell cell1 = row.getCell(1);
			if(cell1 != null){
				name = cell1.getStringCellValue();
			}
			
			Cell cell2 = row.getCell(2);
			if(cell2 != null){
				gender = cell2.getStringCellValue();
			}
			
			Cell cell3 = row.getCell(3);
			if(cell3 != null){
				cell3.setCellType(Cell.CELL_TYPE_STRING);
				identityNum = cell3.getStringCellValue();
			}
			
			Cell cell4 = row.getCell(4);
			if(cell4 != null){
				cell4.setCellType(Cell.CELL_TYPE_STRING);
				phoneNum = cell4.getStringCellValue();
			}
			
			Cell cell5 = row.getCell(5);
			if(cell5 != null){
				college = cell5.getStringCellValue();
			}
			
			Cell cell6 = row.getCell(6);
			if(cell6 != null){
				major = cell6.getStringCellValue();
			}
			
			Cell cell7 = row.getCell(7);
			if(cell7 != null){
				grade = cell7.getStringCellValue();
			}
			
			Cell cell8 = row.getCell(8);
			if(cell8 != null){
				classNum = cell8.getStringCellValue();
			}
			
			Student student = null;
			if(StringUtils.isNotBlank(studentNum)){
				student = studentManager.findUserByLoginName(studentNum);
				
				if(student == null){
					student = new Student();
					student.setLoginName(studentNum.trim());
					student.setPassword(new Md5Hash(ProjectProperties.getProperty("defaultPassword")).toHex());
				}
				
				student.setStatus(1);
				student.setName(name.trim());
				if("男".equals(gender.trim())){
					student.setGender(1);
				}else{
					student.setGender(0);
				}
				student.setIdentityNum(identityNum.trim());
				student.setPhoneNum(phoneNum.trim());
				student.setCollege(college.trim());
				student.setMajor(major.trim());
				student.setGrade(grade.trim());
				student.setClassNum(classNum.trim());
				student.setRole(roleType);
				
				if(student.getId() == null){
					msgs.add("导入学生信息成功，学号:" + studentNum);
					log.info("Import Stucent Account Info Successfully, studentNum:{}", studentNum);
				}else{
					msgs.add("更新学生信息成功，学号:" + studentNum);
					log.info("Update Student Account Info Successfully, studentNum:{}", studentNum);
				}
				
				studentManager.save(student);
				successCount++;
			}
		}
		
		importResult.setSuccessCount(successCount);
		importResult.setMessages(msgs);
		
		return importResult;
	}
	
	public void importStudentInfoFromExcel(MultipartFile file)throws Exception{
		Workbook workbook = null;
		if(file.getOriginalFilename().endsWith("xlsx")){
			workbook = new XSSFWorkbook(file.getInputStream());
		}else{
			workbook = new HSSFWorkbook(file.getInputStream());
		}
		
		Sheet sheet = workbook.getSheetAt(0);
		
		try{
			readStudentInfoFromSheet(sheet);
		}catch(Exception e){
			throw new Exception(e);
		}
	}
	
	
	//在通过公式计算折算成绩的时候如果 公式有错误就会抛出异常
	private void readStudentInfoFromSheet(Sheet sheet) throws Exception{
		Map<String, CertificateType> certificateTypes = new HashMap<String, CertificateType>();
		certificateTypes.put("cet4", certificateTypeManager.getCertificateTypeByName("国家四级"));
		certificateTypes.put("cet6", certificateTypeManager.getCertificateTypeByName("国家六级"));
		
		RoleType roleType = roleTypeManager.getRoleTypeByCode("student");
		
		int rowSize = sheet.getLastRowNum();
		//第一行是标题，从第二行开始
		for(int i = 1; i < rowSize; i++){
			Row row = sheet.getRow(i);
			
			String course = null;
			String studentNum = null;
			String name = null;
			String gender = null;
			String identityNum = null;
			Double score = null;
			String college= null;
			String major = null;
			String grade = null;
			String classNum = null;
			
			Cell cell4 = row.getCell(4);
			if(cell4 != null){
				course = cell4.getStringCellValue();
			}
			
			Cell cell5 = row.getCell(5);
			if(cell5 != null){
				studentNum = cell5.getStringCellValue();
			}
			
			Cell cell6 = row.getCell(6);
			if(cell6 != null){
				name = cell6.getStringCellValue();
			}
			
			Cell cell7 = row.getCell(7);
			if(cell7 != null){
				gender = cell7.getStringCellValue();
			}
			
			Cell cell8 = row.getCell(8);
			if(cell8 != null){
				identityNum = cell8.getStringCellValue();
			}
			
			Cell cell10 = row.getCell(10);
			if(cell10 != null){
				score = cell10.getNumericCellValue();
			}
			
			Cell cell15 = row.getCell(15);
			if(cell15 != null){
				college= cell15.getStringCellValue();
			}
			
			Cell cell16 = row.getCell(16);
			if(cell16 != null){
				major = cell16.getStringCellValue();
			}
			
			Cell cell18 = row.getCell(18);
			if(cell18 != null){
				grade = cell18.getStringCellValue();
			}
			
			Cell cell19 = row.getCell(19);
			if(cell19 != null){
				classNum = cell19.getStringCellValue();
			}
			
			Student student = null;
			if(StringUtils.isNotBlank(studentNum)){
				student	= studentManager.findUserByLoginName(studentNum.trim());
				
				if(student == null){
					student = new Student();
					student.setLoginName(studentNum.trim());
					student.setPassword(new Md5Hash(ProjectProperties.getProperty("defaultPassword")).toHex());
				}
				
				student.setStatus(1);
				student.setName(name.trim());
				if("男".equals(gender.trim())){
					student.setGender(1);
				}else{
					student.setGender(0);
				}
				student.setIdentityNum(identityNum.trim());
				student.setCollege(college.trim());
				student.setMajor(major.trim());
				student.setGrade(grade.trim());
				student.setClassNum(classNum.trim());
				student.setRole(roleType);
				
				studentManager.save(student);
				if(score >= 425){
					Expression expression = null;
					CertificateType certificateType = null;
					if("大学英语四级".equals(course.trim())){
						certificateType = certificateTypes.get("cet4");
						String formula = certificateType.getFormula();
						expression = new Expression(formula);
					}else if("大学英语六级".equals(course.trim())){
						certificateType = certificateTypes.get("cet6");
						String formula = certificateType.getFormula();
						expression = new Expression(formula);
					}
					student = studentManager.findUserByLoginName(studentNum);
					
					CertificateScore certificateScore = certificateScoreManager.findCertificateScoreByStudentCertificateTypeAndStatus(student.getId(), certificateType.getId(), CertificateStatus.IMPORT);
					
					if(certificateScore == null){
						certificateScore = new CertificateScore();
					}
					
					Map<String, BigDecimal> variables = new HashMap<String, BigDecimal>();
					variables.put("x", new BigDecimal(score));
					Float sourceScore = new BigDecimal(score).setScale(2).floatValue();
					Float translatedScore = expression.eval(variables).setScale(0, BigDecimal.ROUND_UP).floatValue();
					
					certificateScore.setVerifyTimes(0);
					certificateScore.setCertificateType(certificateType);
					certificateScore.setSourceScore(sourceScore);
					certificateScore.setTranslatedScore(translatedScore);
					certificateScore.setStudentInfo(student);
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
					String importTime = df.format(Calendar.getInstance().getTime());
					
					String comment = "从教务处系统导入，导入时间：" + importTime;
					
					certificateScore.setCommentA(comment);
					
					//将状态设置为从教务处系统导入
					certificateScore.setStatus(CertificateStatus.IMPORT);
					
					certificateScoreManager.saveCertificateSocre(certificateScore);
				}
			}
			
			
		}
	}
	
	public HSSFWorkbook generateStudentInfoWorkbook(List<Student> studentList){
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		
		HSSFSheet hssfSheet = hssfWorkbook.createSheet("学生信息");
		
		HSSFRow firstRow = hssfSheet.createRow(0);
		
		firstRow.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue("序号");
		firstRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue("学号");
		firstRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue("姓名");
		firstRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue("性别");
		firstRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue("学院");
		firstRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue("专业");
		firstRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue("年级");
		firstRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue("班级");
		firstRow.createCell(8, HSSFCell.CELL_TYPE_STRING).setCellValue("身份证号");
		firstRow.createCell(9, HSSFCell.CELL_TYPE_STRING).setCellValue("电话号码");
		
		if(studentList != null && studentList.size() > 0){
			log.info("generateStudentInfoWorkbook-------studentList.size:{}", studentList.size());
			for(int i = 0;i < studentList.size(); i++){
				Student student = studentList.get(i);
				HSSFRow newRow = hssfSheet.createRow(i + 1);
				newRow.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue("" + (i + 1));
				newRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getLoginName());
				
				if(student.getName() != null){
					newRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getName());
				}
				
				if(student.getGender() != null && student.getGender().equals(1)){
					newRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue("男");
				}else{
					newRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue("女");
				}
				
				if(student.getCollege() != null){
					newRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getCollege());
				}
				
				if(student.getMajor() != null){
					newRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getMajor());
				}
				
				if(student.getGrade() != null){
					newRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getGrade());
				}
				
				if(student.getClassNum() != null){
					newRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getClassNum());
				}
				
				if(student.getIdentityNum() != null){
					newRow.createCell(8, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getIdentityNum());
				}
				
				if(student.getPhoneNum() != null){
					newRow.createCell(9, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getPhoneNum());
				}
			}
		}
		
		
		return hssfWorkbook;
	}
	
	public ImportResult importTeacherInfoFromExcel(MultipartFile file)throws Exception{
		Workbook workbook = null;
		
		log.info("importTeacherInfoFromExcel--------fileName:{}", file.getOriginalFilename());
		if(file.getOriginalFilename().endsWith("xlsx")){
			workbook = new XSSFWorkbook(file.getInputStream());
		}else{
			workbook = new HSSFWorkbook(file.getInputStream());
		}
		
		Sheet sheet = workbook.getSheetAt(0);
		
		return readTeacherInfoFromSheet(sheet);
	}
	
	private ImportResult readTeacherInfoFromSheet(Sheet sheet){
		ImportResult importResult = new ImportResult();
		int successCount = 0;
		List<String> msgs = new ArrayList<String>();
		
		int rowSize = sheet.getLastRowNum();
		
		log.info("readTeacherInfoFromSheet--------rowSize:{}", rowSize);
		//第一行是标题，从第二行开始
		for(int i = 1; i < rowSize; i++){
			Row row = sheet.getRow(i);
			String loginName = null;
			String name = null;
			
			Cell cell0 = row.getCell(0);
			if(cell0 != null){
				cell0.setCellType(Cell.CELL_TYPE_STRING);
				loginName = cell0.getStringCellValue();
			}
			
			Cell cell1 = row.getCell(1);
			if(cell1 != null){
				name = cell1.getStringCellValue();
			}
			
			Teacher teacher= null;
			if(StringUtils.isNotBlank(loginName)){
				teacher = teacherManager.findTeacherByLoginName(loginName);
				
				if(teacher == null){
					teacher = new Teacher();
					teacher.setLoginName(loginName);
					teacher.setPassword(new Md5Hash(ProjectProperties.getProperty("defaultPassword")).toHex());
				}
				
				teacher.setStatus(1);
				teacher.setName(name);
				teacher.setRole(roleTypeManager.getRoleTypeByCode("teacher"));
				
				if(teacher.getId() == null){
					msgs.add("导入教师信息成功，工号:" + loginName);
					log.info("Import Teacher Account Info Successfully, teacherNum:{}", loginName);
				}else{
					msgs.add("更新教师信息成功，工号:" + loginName);
					log.info("Update Teacher Account Info Successfully, teacherNum:{}", loginName);
				}
				
				successCount++;
				teacherManager.saveTeacher(teacher);
			}
			
		}
		
		importResult.setSuccessCount(successCount);
		importResult.setMessages(msgs);
		
		return importResult;
	}
	
	public HSSFWorkbook generateTeacherInfoWorkbook(List<Teacher> teacherList){
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		
		HSSFSheet hssfSheet = hssfWorkbook.createSheet("学生信息");
		
		HSSFRow firstRow = hssfSheet.createRow(0);
		
		firstRow.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue("序号");
		firstRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue("教师工号");
		firstRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue("教师姓名");
		
		if(teacherList != null && teacherList.size() > 0){
			log.info("generateTeacherInfoWorkbook-------teacherList.size:{}", teacherList.size());
			
			for(int i = 0;i < teacherList.size(); i++){
				Teacher teacher = teacherList.get(i);
				HSSFRow newRow = hssfSheet.createRow(i + 1);
				newRow.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue("" + (i + 1));
				newRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(teacher.getLoginName());
				newRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(teacher.getName());
			}
		}
		
		
		return hssfWorkbook;
	}
	
	public HSSFWorkbook generateCertificateScoreInfoWorkbook(List<CertificateScore> certificateScoreList){
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		
		HSSFSheet hssfSheet = hssfWorkbook.createSheet("证书信息");
		
		HSSFRow firstRow = hssfSheet.createRow(0);
		
		firstRow.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue("序号");
		firstRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue("证书类型");
		firstRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue("姓名");
		firstRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue("学号");
		firstRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue("原始成绩");
		firstRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue("折算成绩");
		firstRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue("证书获取时间");
		firstRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue("状态");
		firstRow.createCell(8, HSSFCell.CELL_TYPE_STRING).setCellValue("身份证号");
		firstRow.createCell(9, HSSFCell.CELL_TYPE_STRING).setCellValue("学院");
		firstRow.createCell(10, HSSFCell.CELL_TYPE_STRING).setCellValue("专业");
		firstRow.createCell(11, HSSFCell.CELL_TYPE_STRING).setCellValue("年级");
		firstRow.createCell(12, HSSFCell.CELL_TYPE_STRING).setCellValue("班级");
		
		if(certificateScoreList != null && certificateScoreList.size() > 0){
			log.info("generateCertificateScoreInfoWorkbook-------certificateScoreList.size:{}", certificateScoreList.size());
			for(int i = 0;i < certificateScoreList.size(); i++){
				CertificateScore certificateScore = certificateScoreList.get(i);
				Student student = certificateScore.getStudentInfo();
				CertificateType certificateType = certificateScore.getCertificateType();
				
				HSSFRow newRow = hssfSheet.createRow(i + 1);
				newRow.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue("" + (i + 1));
				if(certificateType.getCertificateName() != null){
					newRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(certificateType.getCertificateName());
				}

				if(student.getName() != null){
					newRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getName());
				}
				
				newRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getLoginName());
				newRow.createCell(4, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(certificateScore.getSourceScore());
				newRow.createCell(5, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(certificateScore.getTranslatedScore());
				if(certificateScore.getCertificateAcquireTime() != null){
					newRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue( new SimpleDateFormat("yyyy-MM-dd").format(certificateScore.getCertificateAcquireTime()));
				}
				
				if(CertificateStatus.TOBECHECKED.equals(certificateScore.getStatus())){
					newRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue("待检查");
				}else if(CertificateStatus.PASSCHECKED.equals(certificateScore.getStatus())){
					newRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue("查验通过");
				}else if(CertificateStatus.NOPASSCHECKED.equals(certificateScore.getStatus())){
					newRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue("查验不通过");
				}else if(CertificateStatus.ONCECHECK.equals(certificateScore.getStatus())){
					newRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue("一审通过");
				}else if(CertificateStatus.ARCHIVED.equals(certificateScore.getStatus())){
					newRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue("已归档");
				}else if(CertificateStatus.IMPORT.equals(certificateScore.getStatus())){
					newRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue("教务处系统导入");
				}
				
				if(student.getIdentityNum() != null){
					newRow.createCell(8, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getIdentityNum());
				}
				
				if(student.getCollege() != null){
					newRow.createCell(9, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getCollege());
				}
				
				if(student.getMajor() != null){
					newRow.createCell(10, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getMajor());
				}
				
				if(student.getGrade() != null){
					newRow.createCell(11, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getGrade());
				}
				
				if(student.getClassNum() != null){
					newRow.createCell(12, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getClassNum());
				}
				
			}
		}
		
		return hssfWorkbook;
	}
	
	public List<String> getFiledNamesFromExcel(MultipartFile file) throws Exception{
		List<String> fieldNames = new ArrayList<String>();
		
		Workbook workbook = null;
		if(file.getOriginalFilename().endsWith("xlsx")){
			workbook = new XSSFWorkbook(file.getInputStream());
		}else{
			workbook = new HSSFWorkbook(file.getInputStream());
		}
		
		Sheet sheet = workbook.getSheetAt(0);
		
		Row firstRow = sheet.getRow(sheet.getFirstRowNum());
		int max = firstRow.getLastCellNum();
		for(int i = 0; i < max; i++){
			Cell cell = firstRow.getCell(i);
			if(cell != null){
				cell.setCellType(Cell.CELL_TYPE_STRING);
				fieldNames.add(cell.getStringCellValue());
			}else{
				fieldNames.add("");
			}
		}
		
		return fieldNames;
	}
	
	public ImportResult importGradeFromExcel(File file, ImportGrade importGrade, CertificateType certificateType) throws Exception{
		ImportResult importResult = new ImportResult();
		
		Workbook workbook = null;
		InputStream is = new FileInputStream(file);
		
		if(file.getName().endsWith("xlsx")){
			workbook = new XSSFWorkbook(is);
		}else{
			workbook = new HSSFWorkbook(is);
		}
		
		Sheet sheet = workbook.getSheetAt(0);
		
		int max = sheet.getLastRowNum();
		for(int i = 1; i < max; i++){
			Row row = sheet.getRow(i);
			
			String studentNum = null;
			String name = null;
			Double sourceScore = null;
			Double gradeA = null;
			Double gradeB = null;
			Double gradeC = null;
			
			if(row.getCell(importGrade.getStudentNum()) != null){
				studentNum = row.getCell(importGrade.getStudentNum()).getStringCellValue();
			}
			
			if(StringUtils.isNotBlank(studentNum)){
				RoleType roleType = roleTypeManager.getRoleTypeByCode("student");
				Student student	= studentManager.findUserByLoginName(studentNum.trim());
				if(student == null){
					student = new Student();
					student.setLoginName(studentNum.trim());
					student.setPassword(new Md5Hash(ProjectProperties.getProperty("defaultPassword")).toHex());
				}
				
				student.setStatus(1);
				student.setRole(roleType);
				
				if(importGrade.getStudentName() != null && row.getCell(importGrade.getStudentName()) != null){
					name = row.getCell(importGrade.getStudentName()).getStringCellValue();
				}
				
				student.setName(name);
				
				if(importGrade.getSourceScore() != null){
					if(row.getCell(importGrade.getSourceScore()) != null){
						sourceScore = row.getCell(importGrade.getSourceScore()).getNumericCellValue();
					}
				}else{
					if(importGrade.getGradeA() != null && row.getCell(importGrade.getGradeA()) != null){
						gradeA = row.getCell(importGrade.getGradeA()).getNumericCellValue();
					}
					
					if(importGrade.getGradeB() != null && row.getCell(importGrade.getGradeB()) != null){
						gradeB = row.getCell(importGrade.getGradeB()).getNumericCellValue();
					}
					
					if(importGrade.getGradeC() != null && row.getCell(importGrade.getGradeC()) != null){
						gradeC = row.getCell(importGrade.getGradeC()).getNumericCellValue();
					}
				}
				
			}
		}
		
		return importResult;
	}
}
