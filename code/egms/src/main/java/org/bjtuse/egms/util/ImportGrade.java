package org.bjtuse.egms.util;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ImportGrade {
	private Integer studentNum;
	
	private Integer studentName;
	
	private Integer sourceScore;
	
	private Integer gradeA;
	
	private Integer gradeB;
	
	private Integer gradeC;
	
	private Integer oralScore;
	
	private Integer writtenScore;
	
	public boolean checkGrade(){
		if(sourceScore == null && gradeA == null && gradeB == null && gradeC == null && oralScore == null && writtenScore == null){
			return false;
		}
		
		return true;
	}
}
