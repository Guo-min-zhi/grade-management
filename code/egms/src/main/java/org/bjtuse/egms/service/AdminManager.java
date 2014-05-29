package org.bjtuse.egms.service;

import org.bjtuse.egms.repository.dao.AdminDao;
import org.bjtuse.egms.repository.entity.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional(readOnly = true)
public class AdminManager {
	
	@Autowired
	private AdminDao adminDao;
	
	public Administrator findAdminUserByLoginName(String loginName){
		return adminDao.findAdministratorByLoginName(loginName);
	}
	
	@Transactional(readOnly = false)
	public void save(Administrator administrator){
		adminDao.save(administrator);
	}
}
