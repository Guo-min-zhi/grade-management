package org.bjtuse.egms.web.teacher.form;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CertificateFastQueryForm {

	private String studentNumber;
	
	private String studentCollege;
	
	private String studentGrade;
	
	private String studentMajor;
	
	private String studentCertificateStatus;
	
	private Integer pageSize;
	
	private Integer certificateType;
}
