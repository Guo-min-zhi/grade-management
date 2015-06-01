package org.bjtuse.egms.web.login;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.bjtuse.egms.service.RoleTypeManager;
import org.bjtuse.egms.web.login.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
public class LoginController {
	
	@Autowired
	private RoleTypeManager roleTypeManager;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginGet(@ModelAttribute("loginForm") LoginForm loginForm,
			Model model, HttpServletRequest request){
		model.addAttribute("allRoleType", roleTypeManager.getAllRoleType());
		return "login";
	}
	
	@RequestMapping(value = "/login" ,method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	public String loginPost(
			@ModelAttribute("loginForm") LoginForm loginForm,
			Model model,
			HttpServletRequest request){
		String code = (String) request.getSession().getAttribute("validateCode");
		String submitCode = WebUtils.getCleanParam(request, "validateCode");
		if (StringUtils.isBlank(submitCode) || !StringUtils.equalsIgnoreCase(code,submitCode)) {
			request.setAttribute("loginFailure", "验证码错误，登录失败。");
			model.addAttribute("allRoleType", roleTypeManager.getAllRoleType());
			return "login";
		}
		Subject user = SecurityUtils.getSubject();
		String hashedPassword = new Md5Hash(loginForm.getPassword()).toHex();
		UsernamePasswordToken token = new UsernamePasswordToken(loginForm.getLoginName() + "," + loginForm.getRoleName(),hashedPassword);
		token.setRememberMe(true);
		try{
			user.login(token);
			request.getSession().setAttribute("loginName", loginForm.getLoginName());
			request.getSession().setAttribute("role", loginForm.getRoleName());
			String loginIp = getIpAddr(request);
			log.info("{} login successfully. RoleType:{}. Login Ip Addr: {}.", loginForm.getLoginName(), loginForm.getRoleName(), loginIp);
			return "redirect:/index";
		}catch (AuthenticationException e) {
			token.clear();
			loginForm.setValidateCode("");
			model.addAttribute("allRoleType", roleTypeManager.getAllRoleType());
			request.setAttribute("loginFailure", "用户名或密码错误，登录失败。");
			return "login";
		}
	}
	
	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");       
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {       
			ip = request.getHeader("Proxy-Client-IP");
		}
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {       
	    	ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {       
	    	ip = request.getRemoteAddr();
	    }
	    return ip;       
	}
}
