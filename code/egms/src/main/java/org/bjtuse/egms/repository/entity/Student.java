package org.bjtuse.egms.repository.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "user_student")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Student extends IdEntity<Long> {

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
	
	//0:female 1:male
	@Column(name = "gender")
	private Integer gender;
	
	@Column(name = "college", length = 50)
	private String college;
	
	@Column(name = "major", length = 50)
	private String major;
	
	@Column(name = "grade", length = 20)
	private String grade;
	
	@Column(name = "class_num", length = 25)
	private String classNum;
	
	@Column(name = "phone_num", length = 11)
	private String phoneNum;
	
	@Column(name = "photo", length = 100)
	private String photo;
	
	@Column(name = "identity_num", length = 18)
	private String identityNum;
	
	
	/*
	 * 1:on
	 * 2:off
	 */
	@Column(name = "status")
	private Integer status;
	
}
