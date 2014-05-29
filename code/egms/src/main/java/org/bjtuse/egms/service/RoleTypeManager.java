package org.bjtuse.egms.service;

import java.util.List;

import org.bjtuse.egms.repository.dao.RoleTypeDao;
import org.bjtuse.egms.repository.entity.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional(readOnly = true)
public class RoleTypeManager {
	
	@Autowired
	private RoleTypeDao roleTypeDao;
	
	public List<RoleType> getAllRoleType(){
		return roleTypeDao.findAll();
	}
	
	public RoleType getRoleTypeByCode(String roleCode){
		return roleTypeDao.findRoleTypeByRoleCode(roleCode);
	}
}
