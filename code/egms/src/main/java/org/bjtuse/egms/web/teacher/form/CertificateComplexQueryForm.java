package org.bjtuse.egms.web.teacher.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CertificateComplexQueryForm {

	private String studentNumber;
	
	private String studentCollege;
	
	private String studentGrade;
	
	private String studentMajor;
	
	private String studentName;
	
	private Integer checkTimes;
	
	private String checkPerson;
	
	private String studentCertificateStatus;
	
	private Integer pageSize;
	
	private Integer certificateType;
}
