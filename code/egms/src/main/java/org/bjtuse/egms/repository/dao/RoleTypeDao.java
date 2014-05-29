package org.bjtuse.egms.repository.dao;

import org.bjtuse.egms.repository.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleTypeDao extends JpaRepository<RoleType, Integer>,
		JpaSpecificationExecutor<RoleType> {
	
	RoleType findRoleTypeByRoleCode(String roleCode);
}
