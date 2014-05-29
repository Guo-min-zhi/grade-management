package org.bjtuse.egms.web.admin.form;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class StudentInfoQueryForm {
	private String studentNum;
	
	private String college;
	
	private String grade;
	
	private String major;
	
	private Integer pageSize;
}
