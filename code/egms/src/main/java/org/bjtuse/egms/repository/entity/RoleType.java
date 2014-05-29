package org.bjtuse.egms.repository.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Getter
@Setter
@Entity
@Table(name = "role_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RoleType extends IdEntity<Integer> {

	
	/** @Fields serialVersionUID: */
	  	
	private static final long serialVersionUID = 1L;
	
	
	@Column(name = "role_name", length = 30)
	private String roleName;
	
	@Column(name = "role_code", length = 30)
	private String roleCode;
}
