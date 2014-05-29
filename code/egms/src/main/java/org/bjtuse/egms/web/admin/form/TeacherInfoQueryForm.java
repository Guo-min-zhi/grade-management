package org.bjtuse.egms.web.admin.form;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class TeacherInfoQueryForm {
	
	private String loginName;
	
	private String name;
	
	private Integer pageSize;
	
}
