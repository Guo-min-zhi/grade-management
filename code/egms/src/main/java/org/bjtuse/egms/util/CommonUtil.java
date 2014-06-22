package org.bjtuse.egms.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.bjtuse.egms.repository.entity.CertificateScore;

import net.java.dev.eval.Expression;

public class CommonUtil {
	
	/**
	 * 将百分制转换为五级制
	 * @param oneHundredScore
	 * @return
	 */
	public static String translateToFiveLevelGrade(Float oneHundredScore){
		if(oneHundredScore >= 85){
			return "优";
		}else if(oneHundredScore < 85 && oneHundredScore >=75){
			return "良";
		}else if(oneHundredScore < 75 && oneHundredScore >= 68){
			return "中";
		}else if(oneHundredScore < 68 && oneHundredScore >= 60){
			return "及格";
		}else{
			return "不及格";
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
