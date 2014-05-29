package org.bjtuse.egms.repository.dao;

import java.util.List;

import org.bjtuse.egms.repository.entity.CertificateScore;
import org.bjtuse.egms.repository.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CertificateScoreDao extends JpaRepository<CertificateScore, Long>,
		JpaSpecificationExecutor<CertificateScore> {
	
	List<CertificateScore> findCertificateScoreByStudentInfo(Student student);
	
	@Query("select c from CertificateScore c where c.status in (2,5,6)")
	List<CertificateScore> findCertificateScoreToExport();
	
	@Query("select c from CertificateScore c where c.studentInfo.id = :studentId and c.certificateType.id = :cId and c.status = :status")
	CertificateScore findByStudentCertificateTypeAndStatus(@Param("studentId")Long studentId, @Param("cId")Integer cId, @Param("status")Integer status);
}
