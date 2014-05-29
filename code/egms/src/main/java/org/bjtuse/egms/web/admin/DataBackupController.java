package org.bjtuse.egms.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin/dataBackup")
public class DataBackupController {
	
	@RequestMapping("/backup")
	public String backup(){
		return "/admin/backup";
	}
}
