package org.bjtuse.egms.web.admin;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.bjtuse.egms.repository.entity.Administrator;
import org.bjtuse.egms.service.AdminManager;
import org.bjtuse.egms.util.ProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
@RequestMapping("/admin/modifyPassword")
public class ModifyPasswordController {
	
	@Autowired
	private AdminManager adminManager;
	
	@RequestMapping(value = "/password", method = RequestMethod.GET)
	public String index(){
		return "/admin/password";
	}
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public void resetPassword(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String loginName = (String)request.getSession().getAttribute("loginName");
		String defaultPassword = ProjectProperties.getProperty("defaultPassword");
		String hashedPassword = new Md5Hash(defaultPassword).toHex();
		Administrator admin = adminManager.findAdminUserByLoginName(loginName);
		admin.setPassword(hashedPassword);
		adminManager.save(admin);
		
		log.info("{} reset password.", loginName);
		response.getWriter().write("true");
	}
	
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modifyPassword(HttpServletRequest request, Model model){
		String loginName = (String)request.getSession().getAttribute("loginName");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		
		Administrator administrator = adminManager.findAdminUserByLoginName(loginName);
		if(StringUtils.equals(administrator.getPassword(), new Md5Hash(oldPassword).toHex())){
			String hashedPassword = new Md5Hash(newPassword).toHex();
			administrator.setPassword(hashedPassword);
			adminManager.save(administrator);
			
			log.info("{} modify password.", loginName);
			model.addAttribute("result", "1");
		}else{
			model.addAttribute("result", "0");
		}
		
		return "/admin/password";
	}
	
}
