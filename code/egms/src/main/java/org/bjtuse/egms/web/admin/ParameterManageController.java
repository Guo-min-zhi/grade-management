package org.bjtuse.egms.web.admin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.java.dev.eval.Expression;

import org.apache.commons.lang3.StringUtils;
import org.bjtuse.egms.repository.entity.CertificateType;
import org.bjtuse.egms.service.CertificateTypeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/admin/parameterManage")
public class ParameterManageController {
	
	@Autowired
	private CertificateTypeManager certificateTypeManager;
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model){
		List<CertificateType> allCertificateTypes = certificateTypeManager.getAllCertificateType();
		
		model.addAttribute("pageObjects", allCertificateTypes);
		return "/admin/parameterManage/list";
	}
	
	@RequestMapping(value = "/formula", method = RequestMethod.GET)
	public String formulaList(Model model){
		List<CertificateType> allCertificateTypes = certificateTypeManager.getAllCertificateType();
		
		model.addAttribute("pageObjects", allCertificateTypes);
		return "/admin/parameterManage/formula";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String save(HttpServletRequest request){
		String id = request.getParameter("id").trim();
		String name = request.getParameter("name").trim();
		
		CertificateType certificateType = null;
		
		if(StringUtils.isNotBlank(id)){
			certificateType = certificateTypeManager.getCertificateById(Integer.parseInt(id));
		}else{
			certificateType = new CertificateType();
		}
		
		if(StringUtils.isNotBlank(name)){
			certificateType.setCertificateName(name);
		}
		
		certificateTypeManager.save(certificateType);
		
		return "redirect:/admin/parameterManage/list";
		
	}
	
	@RequestMapping(value = "/saveFormula", method = RequestMethod.GET)
	public String saveFormula(HttpServletRequest request){
		String id = request.getParameter("id").trim();
		String formula = request.getParameter("formula").trim();
		
		if(StringUtils.isNotBlank(id)){
			CertificateType certificateType = certificateTypeManager.getCertificateById(Integer.parseInt(id));

			if(certificateType != null){
				certificateType.setFormula(formula);
				
				//测试公式是否正确
				try{
					Expression exp = new Expression(formula);
					Map<String, BigDecimal> vars = new HashMap<String, BigDecimal>();
					
					int source = 530;
					vars.put("x", new BigDecimal(source));
					System.out.println("======================测试: score:" + source +"-----translated score:" + exp.eval(vars).intValue());
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
					
				}catch (Exception e) {
					e.printStackTrace();
					return "/admin/parameterManage/wrongFormula";
				}
				
				certificateTypeManager.save(certificateType);
			}
		}
		
		return "redirect:/admin/parameterManage/formula";
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Integer id){
		certificateTypeManager.delete(id);
		
		return "redirect:/admin/parameterManage/list";
	}
	
}
