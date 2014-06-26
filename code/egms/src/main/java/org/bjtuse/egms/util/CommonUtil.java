package org.bjtuse.egms.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.java.dev.eval.Expression;

import org.bjtuse.egms.repository.entity.CertificateScore;
import org.bjtuse.egms.repository.entity.CertificateType;

public class CommonUtil {
	
	/**
	 * 将百分制转换为五级十段制
	 * @param oneHundredScore
	 * @return
	 */
	public static String translateToFiveLevelGrade(Float oneHundredScore){
		if(oneHundredScore >= 91){
			return "A+";
		}else if(oneHundredScore <= 90 && oneHundredScore >= 86){
			return "A";
		}else if(oneHundredScore <= 85 && oneHundredScore >= 81){
			return "B+";
		}else if(oneHundredScore <= 80 && oneHundredScore >= 76){
			return "B";
		}else if(oneHundredScore <= 75 && oneHundredScore >= 71){
			return "C+";
		}else if(oneHundredScore <= 70 && oneHundredScore >= 66){
			return "C";
		}else if(oneHundredScore <= 65 && oneHundredScore >= 61){
			return "D+";
		}else if(oneHundredScore == 60){
			return "D";
		}else if(oneHundredScore == 59){
			return "F+";
		}else{
			return "F";
		}
	}
	
	
	/**
	 * 判断能否根据公式计算综合成绩
	 * @param x1-是否需要第一学期成绩
	 * @param x2-是否需要第二学期成绩
	 * @param x3-是否需要第三学期成绩
	 * @param x4-是否需要口语成绩
	 * @param x5-是否需要笔试成绩
	 * @param score-当前所有的成绩
	 * @return
	 */
	public static boolean canGetTranslatedScore(boolean x1, boolean x2, boolean x3, boolean x4, boolean x5, CertificateScore score){
		if(x1 && score.getGradeA() == null){
			return false;
		}
		
		if(x2 && score.getGradeB() == null){
			return false;
		}
		
		if(x3 && score.getGradeC() == null){
			return false;
		}
		
		if(x4 && score.getOralScore() == null){
			return false;
		}
		
		if(x5 && score.getWrittenScore() == null){
			return false;
		}
		
		return true;
	}
	
	public static Map<String, BigDecimal> prepareVariables(boolean x1, boolean x2, boolean x3, boolean x4, boolean x5, CertificateScore score){
		Map<String, BigDecimal> variables = new HashMap<String, BigDecimal>();
		
		if(x1){
			variables.put("x1", new BigDecimal(score.getGradeA()));
		}
		
		if(x2){
			variables.put("x2", new BigDecimal(score.getGradeB()));
		}
		
		if(x3){
			variables.put("x3", new BigDecimal(score.getGradeC()));
		}
		
		if(x4){
			variables.put("x4", new BigDecimal(score.getOralScore()));
		}
		
		if(x5){
			variables.put("x5", new BigDecimal(score.getWrittenScore()));
		}
		
		return variables;
	}
	
	public static String transferDateToString(Long time){
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		
		Date date = new Date(time);
		return sdf.format(date);
	}
	
	public static String getSemester(){
		StringBuffer sb = new StringBuffer();
		
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		
		if(month > 7){
			sb.append(year).append("-").append(year + 1).append("学年，第一学期");
		}else{
			sb.append(year - 1).append("-").append(year).append("学年，第二学期");
		}
		
		return sb.toString();
	}
	
	public static String generateHintsByCertificateType(CertificateType certificateType){
		StringBuffer sb = new StringBuffer("当前上传的是【").append(certificateType.getCertificateName()).append("】,需要匹配的字段包含：");
		
		String formula = certificateType.getFormula();
		if(formula.contains("x1")){
			sb.append("【第一学期成绩】");
		}else{
			sb.append("【四六级考试成绩】");
		}
		
		if(formula.contains("x2")){
			sb.append("【第二学期成绩】");
		}
		if(formula.contains("x3")){
			sb.append("【第三学期成绩】");
		}
		if(formula.contains("x4")){
			sb.append("【口语成绩】");
		}
		if(formula.contains("x5")){
			sb.append("【笔试成绩】");
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args){
		System.out.println(translateToFiveLevelGrade(85.1f));
		System.out.println(translateToFiveLevelGrade(85.0f));
		System.out.println(translateToFiveLevelGrade(59.9f));
		
		Expression expression = new Expression("x1 * 0.4 + x2 * 0.6");
		
		Map<String, BigDecimal> v = new HashMap<String, BigDecimal>();
		v.put("x1", new BigDecimal(60));
		v.put("x2", new BigDecimal(80));
		
		System.out.println(expression.eval(v).toString());
	}
}
