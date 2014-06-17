package org.bjtuse.egms.web.teacher.form;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QueryComprehensiveForm {

	private String studentNumber;
	
	private String studentName;
	
	private String startTime;
	
	private String endTime;
	
	private Integer pageSize;
	
	private Integer certificateType;
}
