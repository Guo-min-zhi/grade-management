package org.bjtuse.egms.web.admin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import net.java.dev.eval.Expression;

import org.bjtuse.egms.repository.entity.CertificateScore;
import org.bjtuse.egms.repository.entity.CertificateType;
import org.bjtuse.egms.service.CertificateTypeManager;
import org.bjtuse.egms.util.CommonUtil;
import org.bjtuse.egms.util.ExceptionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Controller
@RequestMapping("/admin/parameterManage")
public class ParameterManageController {
	
	@Autowired
	private CertificateTypeManager certificateTypeManager;
	
	
	@RequestMapping(value = "/gradeTypeList", method = RequestMethod.GET)
	public String list(Model model){
		List<CertificateType> allCertificateTypes = certificateTypeManager.getAllCertificateType();
		
		Map<Integer, String> status = new HashMap<Integer, String>();
		status.put(0, "教师上传");
		status.put(1, "学生上传");
		
		model.addAttribute("mapStatus", status);
		model.addAttribute("pageObjects", allCertificateTypes);
		return "/admin/parameterManage/gradeTypeList";
	}
	
	@RequestMapping(value = "create")
	public String create(Model model) {
		
		model.addAttribute("gradeType", new CertificateType());
		model.addAttribute("action", "create");

		return "/admin/parameterManage/gradeTypeInfo";
	}
	
	@RequestMapping(value = "edit")
	public String edit(@RequestParam(value = "id", required = false)Integer id, Model model) {
		if(id != null){
			model.addAttribute("gradeType", certificateTypeManager.getCertificateById(id));
			model.addAttribute("action", "edit");
		}
		
		return "/admin/parameterManage/gradeTypeInfo";
	}
	
	@RequestMapping(value = "/formulaList", method = RequestMethod.GET)
	public String formulaList(Model model){
		List<CertificateType> allCertificateTypes = certificateTypeManager.getAllCertificateType();
		
		model.addAttribute("pageObjects", allCertificateTypes);
		return "/admin/parameterManage/formulaList";
	}
	
	@RequestMapping(value = "editFormula")
	public String editFormula(@RequestParam(value = "id", required = false)Integer id, Model model) {
		if(id != null){
			model.addAttribute("gradeType", certificateTypeManager.getCertificateById(id));
		}
		
		return "/admin/parameterManage/formulaInfo";
	}
	
	@RequestMapping(value = "save")
	public void save(@ModelAttribute("gradeType") CertificateType certificateType){
		certificateTypeManager.save(certificateType);
	}
	
	@RequestMapping(value = "/saveFormula")
	public void saveFormula(@ModelAttribute("gradeType") CertificateType certificateType, Model model){
		
		if(certificateType != null){
			//测试公式是否正确
//			try{
//				Expression exp = new Expression(certificateType.getFormula());
//				Map<String, BigDecimal> vars = new HashMap<String, BigDecimal>();
//				
//				int source = 530;
//				vars.put("x", new BigDecimal(source));
//				System.out.println("======================测试: score:" + source +"-----translated score:" + exp.eval(vars).intValue());
//					if(certificateType.getCertificateName().contains("GRE")){
//						int source = 1340;
//						vars.put("x", new BigDecimal(source));
//						System.out.println("======================GRE: score:" + source +"-----translated score:" + exp.eval(vars).intValue());
//					}else if(certificateType.getCertificateName().contains("GMAT")){
//						int source = 725;
//						vars.put("x", new BigDecimal(source));
//						System.out.println("======================GMAT: score:" + source +"-----translated score:" + exp.eval(vars).intValue());
//					}else if(certificateType.getCertificateName().contains("雅思")){
//						float source = 7.0f;
//						vars.put("x", new BigDecimal(source));
//						System.out.println("======================雅思: score:" + source +"-----translated score:" + exp.eval(vars).intValue());
//					}else if(certificateType.getCertificateName().contains("托福")){
//						int source = 100;
//						vars.put("x", new BigDecimal(source));
//						System.out.println("======================托福: score:" + source +"-----translated score:" + exp.eval(vars).intValue());
//					}else if(certificateType.getCertificateName().contains("国家六级")){
//						int source = 540;
//						vars.put("x", new BigDecimal(source));
//						System.out.println("======================国家六级: score:" + source +"-----translated score:" + exp.eval(vars).intValue());
//					}else if(certificateType.getCertificateName().contains("国家四级")){
//						int source = 530;
//						vars.put("x", new BigDecimal(source));
//						System.out.println("======================国家四级: score:" + source +"-----translated score:" + exp.eval(vars).intValue());
//					}
//				
//			}catch (Exception e) {
//				e.printStackTrace();
//				model.addAttribute("error", "公式填写错误!");
//			}
			
			certificateTypeManager.save(certificateType);
		}
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Integer id){
		certificateTypeManager.delete(id);
		
		return "redirect:/admin/parameterManage/gradeTypeList";
	}
	
	@RequestMapping(value = "checkFormula", method = RequestMethod.GET)
	public void checkFormula(@RequestParam("formula")String formula, HttpServletResponse response){
		try{
			Expression exp = new Expression(formula);
			CertificateScore s = new CertificateScore();
			boolean x1 = false;
			boolean x2 = false;
			boolean x3 = false;
			boolean x4 = false;
			boolean x5 = false;
			
			if(formula.contains("x1")){
				x1 = true;
				s.setGradeA(90f);
			}
			if(formula.contains("x2")){
				x2 = true;
				s.setGradeB(90f);
			}
			if(formula.contains("x3")){
				x3 = true;
				s.setGradeC(90f);
			}
			if(formula.contains("x4")){
				x4 = true;
				s.setOralScore(90f);
			}
			if(formula.contains("x5")){
				x5 = true;
				s.setWrittenScore(90f);
			}
			
			Map<String, BigDecimal> vars;
			if(x1 || x2 || x3 || x4 || x5){
				vars = CommonUtil.prepareVariables(x1, x2, x3, x4, x5, s);
			}else{
				vars = new HashMap<String, BigDecimal>();
				
				int source = 530;
				vars.put("x", new BigDecimal(source));
			}
			
			log.info("======================测试公式:-----translated score:" + exp.eval(vars).intValue());
			response.getWriter().write("true");
		}catch(Exception e){
			ExceptionLog.log(e);
		}
	}
	
}
