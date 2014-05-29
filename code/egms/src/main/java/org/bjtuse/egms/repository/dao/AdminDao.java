package org.bjtuse.egms.repository.dao;

import org.bjtuse.egms.repository.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminDao extends JpaRepository<Administrator, Long>,
		JpaSpecificationExecutor<Administrator> {
	
	Administrator findAdministratorByLoginName(String loginName);
}
