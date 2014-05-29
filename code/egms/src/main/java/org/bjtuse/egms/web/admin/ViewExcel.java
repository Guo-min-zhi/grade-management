package org.bjtuse.egms.web.admin;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bjtuse.egms.util.ExceptionLog;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ViewExcel extends AbstractExcelView {

	private String fileName = null;
	
	public ViewExcel(String fileName){
		this.fileName = fileName;
	}
	
	@Override
	public void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		fileName = encodeFileName(fileName, request);//处理中文文件名 
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName); 
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
	}
	
	public String encodeFileName(String filename, HttpServletRequest request){
		try{
			filename = URLEncoder.encode(filename, "UTF-8");
		}catch(UnsupportedEncodingException e){
			ExceptionLog.log(e);
		}
		
		return filename;
	}

}
