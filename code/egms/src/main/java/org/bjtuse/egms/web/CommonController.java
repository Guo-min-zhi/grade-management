package org.bjtuse.egms.web;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bjtuse.egms.service.RoleTypeManager;
import org.bjtuse.egms.util.ValidateCode;
import org.bjtuse.egms.web.login.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CommonController {
	
	@Autowired
	private RoleTypeManager roleTypeManager;
	
	@RequestMapping(value = "/index")
	public String index(HttpSession session){
		String role = (String)session.getAttribute("role");
		if(role != null){
			if(role.equals("admin")){
				return "redirect:/admin/studentManage/list";
			}else if(role.equals("teacher")){
				return "redirect:/teacher/management";
			}else if(role.equals("student")){
				return "redirect:/student/info";
			}
		}
		
		return "/error/403";
	}
	
	@RequestMapping(value = "/certificate", method = RequestMethod.GET)
	public String login(@ModelAttribute("loginForm") LoginForm loginForm, Model model){
		model.addAttribute("allRoleType", roleTypeManager.getAllRoleType());
		
		return "login";
	}
	
	@RequestMapping(value = "/validateCode")
	public void validateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "no-cache");
		String verifyCode = ValidateCode.generateTextCode(ValidateCode.TYPE_NUM_ONLY, 4, null);
		request.getSession().setAttribute("validateCode", verifyCode);
		response.setContentType("image/jpeg");
		BufferedImage bim = ValidateCode.generateImageCode(verifyCode, 1400, 400, 0, true, new Color(0, 136, 204), Color.WHITE, null);
		ImageIO.write(bim, "JPEG", response.getOutputStream());
	}
	
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String error403() {
		return "error/403";
	}
	
//	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public String home(){
//		return "redirect:/login";
//	}
}
