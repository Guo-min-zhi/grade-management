package org.bjtuse.egms.web.login.form;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginForm {
	private String loginName;
	
	private String password;
	
	private String validateCode;
	
	private String roleName;
}
