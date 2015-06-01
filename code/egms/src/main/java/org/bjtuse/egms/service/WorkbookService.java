package org.bjtuse.egms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.bjtuse.egms.util.CommonUtil;
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
		
		List<Student> studentList = new ArrayList<Student>();
		int rowSize = sheet.getLastRowNum();
		//第一行是标题，从第二行开始
		for(int i = 1; i <= rowSize; i++){
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
				student.setName(name);
				if("男".equals(gender)){
					student.setGender(1);
				}else{
					student.setGender(0);
				}
				student.setIdentityNum(identityNum);
				student.setPhoneNum(phoneNum);
				student.setCollege(college);
				student.setMajor(major);
				student.setGrade(grade);
				student.setClassNum(classNum);
				student.setRole(roleType);
				
				if(student.getId() == null){
					msgs.add("导入学生信息成功，学号:" + studentNum);
					log.info("Import Student Account Info Successfully, studentNum:{}", studentNum);
				}else{
					msgs.add("学号:" + studentNum + "已存在");
					log.info("Update Student Account Info Successfully, studentNum:{}", studentNum);
				}
				
				successCount++;
				studentList.add(student);
//				studentManager.save(student);
			}
		}
		
		if(studentList.size() > 0){
			studentManager.save(studentList);
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
		for(int i = 1; i <= rowSize; i++){
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
				score = getScoreFromCell(cell10, null);
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
		for(int i = 1; i <= rowSize; i++){
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
		int successCount = 0;
		List<String> msgs = new ArrayList<String>();
		
		Workbook workbook = null;
		InputStream is = new FileInputStream(file);
		
		if(file.getName().endsWith("xlsx")){
			workbook = new XSSFWorkbook(is);
		}else{
			workbook = new HSSFWorkbook(is);
		}
		
		Sheet sheet = workbook.getSheetAt(0);
		
		int max = sheet.getLastRowNum();
		for(int i = 1; i <= max; i++){
			Row row = sheet.getRow(i);
			
			String studentNum = null;
			String name = null;
			Double sourceScore = null;
			Double gradeA = null;
			Double gradeB = null;
			Double gradeC = null;
			Double oralScore = null;
			Double writtenScore = null;
			
			if(row.getCell(importGrade.getStudentNum()) != null){
				row.getCell(importGrade.getStudentNum()).setCellType(Cell.CELL_TYPE_STRING);
				studentNum = row.getCell(importGrade.getStudentNum()).getStringCellValue();
			}
			
			if(StringUtils.isNotBlank(studentNum)){
				RoleType roleType = roleTypeManager.getRoleTypeByCode("student");
				Student student	= studentManager.findUserByLoginName(studentNum.trim());
				if(student == null){
					student = new Student();
					student.setLoginName(studentNum.trim());
					student.setPassword(new Md5Hash(ProjectProperties.getProperty("defaultPassword")).toHex());
					student.setStatus(1);
					student.setRole(roleType);
					
					if(importGrade.getStudentName() != null && row.getCell(importGrade.getStudentName()) != null){
						name = row.getCell(importGrade.getStudentName()).getStringCellValue();
					}
					
					student.setName(name);
					
					studentManager.save(student);
				}
				
				CertificateScore certificateScore = null; 
				
				//针对四六级成绩只有一个原始成绩
				if(importGrade.getSourceScore() != null){
					if(row.getCell(importGrade.getSourceScore()) != null){
						//因为同一个人的四六级成绩可以有多条记录，所以每次导入的四六级成绩都新建一条记录
						certificateScore = new CertificateScore();
						//设置成绩状态初始值
						certificateScore.setGradeStatus(0);
						
						Expression expression = new Expression(certificateType.getFormula());
						
						sourceScore = getScoreFromCell(row.getCell(importGrade.getSourceScore()), msgs);
						
						Map<String, BigDecimal> variables = new HashMap<String, BigDecimal>();
						variables.put("x", new BigDecimal(sourceScore));
						Float sourceScoreF = new BigDecimal(sourceScore).setScale(2).floatValue();
						Float translatedScore = expression.eval(variables).setScale(0, BigDecimal.ROUND_UP).floatValue();
						
						certificateScore.setVerifyTimes(0);
						certificateScore.setCertificateType(certificateType);
						certificateScore.setSourceScore(sourceScoreF);
						if(translatedScore <= 0){
							certificateScore.setTranslatedScore(0.0f);
						}else{
							certificateScore.setTranslatedScore(translatedScore);
						}
						certificateScore.setStudentInfo(student);
						certificateScore.setGradeFinal(CommonUtil.translateToFiveLevelGrade(translatedScore));
						
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date now = Calendar.getInstance().getTime();
						String importTime = df.format(now);
						
						String comment = "从教务处系统导入，导入时间：" + importTime;
						
						certificateScore.setCommentA(comment);
						certificateScore.setSubmitTime(new Timestamp(now.getTime()));
						
						//将状态设置为从教务处系统导入
						certificateScore.setStatus(CertificateStatus.IMPORT);
						
						//因为四六级只有一个成绩，所以设置成绩状态为可以计算综合成绩
						certificateScore.setGradeStatus(1);
						
						certificateScoreManager.saveCertificateSocre(certificateScore);
						
						successCount++;
						msgs.add("成功导入成绩，学号:" + studentNum + ",成绩:" + certificateScore.getSourceScore());
					}
				}else{
					certificateScore = certificateScoreManager.findCertificateScoreByStudentCertificateTypeAndStatus(student.getId(), certificateType.getId(), CertificateStatus.IMPORT);
					
					if(certificateScore == null){
						certificateScore = new CertificateScore();
						//设置成绩状态初始值
						certificateScore.setGradeStatus(0);
					}
					
					boolean x1 = false;
					boolean x2 = false;
					boolean x3 = false;
					boolean x4 = false;
					boolean x5 = false;
					
					StringBuffer sb = new StringBuffer("成功导入成绩,学号:").append(studentNum);
					
					String formula = certificateType.getFormula();
					if(formula.contains("x1")){
						x1 = true;
					}
					if(formula.contains("x2")){
						x2 = true;
					}
					if(formula.contains("x3")){
						x3 = true;
					}
					if(formula.contains("x4")){
						x4 = true;
					}
					if(formula.contains("x5")){
						x5 = true;
					}
					
					if(importGrade.getGradeA() != null && row.getCell(importGrade.getGradeA()) != null){
						gradeA = getScoreFromCell(row.getCell(importGrade.getGradeA()), msgs);
						certificateScore.setGradeA(gradeA.floatValue());
						sb.append(",第一学期成绩:").append(certificateScore.getGradeA());
					}
					if(importGrade.getGradeB() != null && row.getCell(importGrade.getGradeB()) != null){
						gradeB = getScoreFromCell(row.getCell(importGrade.getGradeB()), msgs);
						certificateScore.setGradeB(gradeB.floatValue());
						sb.append(",第二学期成绩:").append(certificateScore.getGradeB());
					}
					if(importGrade.getGradeC() != null && row.getCell(importGrade.getGradeC()) != null){
						gradeC = getScoreFromCell(row.getCell(importGrade.getGradeC()), msgs);
						certificateScore.setGradeC(gradeC.floatValue());
						sb.append(",第三学期成绩:").append(certificateScore.getGradeC());
					}
					if(importGrade.getOralScore() != null && row.getCell(importGrade.getOralScore()) != null){
						oralScore = getScoreFromCell(row.getCell(importGrade.getOralScore()), msgs);
						certificateScore.setOralScore(oralScore.floatValue());
						sb.append(",口语成绩:").append(certificateScore.getOralScore());
					}
					if(importGrade.getWrittenScore() != null && row.getCell(importGrade.getWrittenScore()) != null){
						writtenScore = getScoreFromCell(row.getCell(importGrade.getWrittenScore()), msgs);
						certificateScore.setWrittenScore(writtenScore.floatValue());
						sb.append(",笔试成绩:").append(certificateScore.getWrittenScore());
					}
					
					certificateScore.setStudentInfo(student);
					certificateScore.setCertificateType(certificateType);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date now = Calendar.getInstance().getTime();
					String importTime = df.format(now);
					
					String comment = "从教务处系统导入，导入时间：" + importTime;
					
					certificateScore.setCommentA(comment);
					//将状态设置为从教务处系统导入
					certificateScore.setStatus(CertificateStatus.IMPORT);
					
					//判断是否可以计算综合成绩，如果可以则计算出综合成绩
					if(CommonUtil.canGetTranslatedScore(x1, x2, x3, x4, x5, certificateScore)){
						//可以计算综合成绩，设置成绩的状态为1
						certificateScore.setGradeStatus(1);
						
						Map<String, BigDecimal> variables = CommonUtil.prepareVariables(x1, x2, x3, x4, x5, certificateScore);
						
						Expression expression = new Expression(formula);
						Float translatedScore = expression.eval(variables).setScale(0, BigDecimal.ROUND_UP).floatValue();
						
						certificateScore.setVerifyTimes(0);
						certificateScore.setTranslatedScore(translatedScore);
						certificateScore.setGradeFinal(CommonUtil.translateToFiveLevelGrade(translatedScore));
						
						certificateScore.setSubmitTime(new Timestamp(now.getTime()));
					}
					
					certificateScoreManager.saveCertificateSocre(certificateScore);
					successCount++;
					msgs.add(sb.toString());
				}
				
				log.info("Import CertificateType:{}, StudentNum:{}, SourceSocre:{}, GradeA:{}, GradeB:{}, GradeC{}, OralScore:{}, WrittenScore:{}",
									certificateType.getCertificateName(),
									studentNum,
									certificateScore.getSourceScore(),
									certificateScore.getGradeA(),
									certificateScore.getGradeB(),
									certificateScore.getGradeC(),
									certificateScore.getOralScore(),
									certificateScore.getWrittenScore());
			}
		}
		
		importResult.setSuccessCount(successCount);
		importResult.setMessages(msgs);
		is.close();
		return importResult;
	}
	
	
	/**
	 * 生成综合成绩excel
	 * @param studentList
	 * @return
	 */
	public HSSFWorkbook generateComperehensiveScoreWorkbook(List<CertificateScore> scoreList){
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		
		HSSFSheet hssfSheet = hssfWorkbook.createSheet("学生综合成绩");
		
		HSSFRow firstRow = hssfSheet.createRow(0);
		
		firstRow.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue("学号");
		firstRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue("姓名");
		firstRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue("综合成绩");
		firstRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue("等级分");
		firstRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue("来源");
		firstRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue("认定日期");
		firstRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue("学年学期");
		
		if(scoreList != null && scoreList.size() > 0){
			log.info("generateCmprehensiveScoreWorkbook-------scoreList.size:{}", scoreList.size());
			for(int i = 0;i <scoreList.size(); i++){
				CertificateScore score = scoreList.get(i);
				Student student = score.getStudentInfo();
				CertificateType scoreType = score.getCertificateType();
				
				// 更改score的状态：2-可以计算综合成绩，已导出
				if(score.getGradeStatus() == null || score.getGradeStatus() != 0){
					score.setGradeStatus(2);
					certificateScoreManager.saveCertificateSocre(score);					
				}
				
				HSSFRow newRow = hssfSheet.createRow(i + 1);
				newRow.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getLoginName());
				
				if(student.getName() != null){
					newRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(student.getName());
				}
				if(score.getTranslatedScore() != null){
					newRow.createCell(2, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(score.getTranslatedScore());
				}
				if(score.getGradeFinal() != null){
					newRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(score.getGradeFinal());
				}
				if(scoreType != null && scoreType.getCertificateName() != null){
					newRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(scoreType.getCertificateName());
				}
				
				if(score.getCertificateAcquireTime() != null){
					newRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(CommonUtil.transferDateToString(score.getCertificateAcquireTime().getTime()));
				}else if(score.getSubmitTime() != null){
					newRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(CommonUtil.transferDateToString(score.getSubmitTime().getTime()));
				}
				
				newRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue(CommonUtil.getSemester());
			}
		}
		return hssfWorkbook;
	}
	
	/**
	 * 从Excel的Cell中获取分数
	 */
	private double getScoreFromCell(Cell cell, List<String> msgs){
		if(cell != null){
			if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
				return cell.getNumericCellValue();
			}else if(Cell.CELL_TYPE_STRING == cell.getCellType()){
				String temp = cell.getStringCellValue();
				try{
					double score = Double.valueOf(temp);
					
					return score;
				}catch(Exception e){
					String errorMsg = "【错误】成绩：" + temp + "不是数字，导入成绩默认设置为0.0。第" + (cell.getRowIndex() + 1) + "行，第" + (cell.getColumnIndex() + 1) + "列。";
					log.error(errorMsg);
					if(msgs != null){
						msgs.add(errorMsg);
					}
					
					return 0.0;
				}
			}
		}
		
		return 0.0;
	}
}
