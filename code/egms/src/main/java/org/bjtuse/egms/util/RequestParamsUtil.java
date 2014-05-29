package org.bjtuse.egms.util;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

public class RequestParamsUtil {
	private static final int FIRST_PAGE = 0; 
	
	public static PageRequest getPageRequest(Integer page, Integer pageSize) {
		if (page == null) {
			page = FIRST_PAGE;
		} else {
			page = page - 1;
		}
		return new PageRequest(page, pageSize, Direction.ASC, "id");
	}
	
	public static String getCurrentURL(HttpServletRequest request) {
		StringBuffer paramString = new StringBuffer("?");
		paramString.append("ajax=true");
		Iterator it = request.getParameterMap().keySet().iterator();
		while (it.hasNext()) {
			String param = (String) it.next();
			if (param.equals("page") || param.equals("ajax"))
				continue;
			Object value = request.getParameterMap().get(param.toString());
			paramString.append("&" + param + "=" + ((String[]) value)[0]);

		}

		String currenturl = request.getRequestURI() + paramString;

		return currenturl;
	}
}
