package org.bjtuse.egms.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Entity
@Table(name = "user_admin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Administrator extends IdEntity<Long> {

	
	/** @Fields serialVersionUID: */
	  	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "login_name", length = 13)
	private String loginName;
	
	@Column(name = "password", length = 32)
	private String password;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	private RoleType role;
	
	@Column(name = "name", length = 25)
	private String name;

}
