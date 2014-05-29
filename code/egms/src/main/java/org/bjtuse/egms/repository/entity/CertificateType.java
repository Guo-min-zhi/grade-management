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
@Table(name = "certificate_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CertificateType extends IdEntity<Integer> {

	
	/** @Fields serialVersionUID: */
	  	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "certificate_name", length = 25)
	private String certificateName;
	
	@Column(name = "formula", length = 300)
	private String formula;

}
