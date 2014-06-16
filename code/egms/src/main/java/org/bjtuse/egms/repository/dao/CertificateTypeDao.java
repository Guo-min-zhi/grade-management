package org.bjtuse.egms.repository.dao;

import java.util.List;

import org.bjtuse.egms.repository.entity.CertificateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CertificateTypeDao extends JpaRepository<CertificateType, Integer>,
		JpaSpecificationExecutor<CertificateType> {
	
	CertificateType findCertificateTypeByCertificateName(String certificateName);
	
	@Query("select ct from CertificateType ct where ct.uploadType = 0")
	List<CertificateType> findCertificateTypesImportByTeacher();
	
	@Query("select ct from CertificateType ct where ct.uploadType = 1")
	List<CertificateType> findCertificateTypesImportByStudent();
}
