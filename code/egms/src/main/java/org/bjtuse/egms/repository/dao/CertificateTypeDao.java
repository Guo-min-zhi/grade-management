package org.bjtuse.egms.repository.dao;

import org.bjtuse.egms.repository.entity.CertificateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CertificateTypeDao extends JpaRepository<CertificateType, Integer>,
		JpaSpecificationExecutor<CertificateType> {
	
	CertificateType findCertificateTypeByCertificateName(String certificateName);
}
