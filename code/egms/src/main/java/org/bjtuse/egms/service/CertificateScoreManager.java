package org.bjtuse.egms.service;

import java.util.List;

import org.bjtuse.egms.repository.dao.CertificateScoreDao;
import org.bjtuse.egms.repository.entity.CertificateScore;
import org.bjtuse.egms.repository.entity.Student;
import org.bjtuse.egms.repository.spec.CertificateScoreComplexSpecification;
import org.bjtuse.egms.repository.spec.CertificateScoreSpecifications;
import org.bjtuse.egms.repository.spec.ComprehensiveScoreSpecification;
import org.bjtuse.egms.web.teacher.form.CertificateComplexQueryForm;
import org.bjtuse.egms.web.teacher.form.CertificateFastQueryForm;
import org.bjtuse.egms.web.teacher.form.QueryComprehensiveForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional(readOnly = true)
public class CertificateScoreManager {
	
	@Autowired
	private CertificateScoreDao certificateScoreDao;
	
	@Transactional(readOnly = false)
	public void saveCertificateSocre(CertificateScore certificateScore){
		certificateScoreDao.save(certificateScore);
	}
	
	public List<CertificateScore> findCertificatesByStudent(Student student){
		return certificateScoreDao.findCertificateScoreByStudentInfo(student);
	}
	
	public CertificateScore findCertificateScoreById(Long id){
		return certificateScoreDao.findOne(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteCertificateScoreById(Long id){
		certificateScoreDao.delete(id);
	}
	
	/**
	 * 快速查询分页
	 * @param queryFastForm
	 * @param pageable
	 * @return
	 */
	public Page<CertificateScore> getPaged(CertificateFastQueryForm queryFastForm, Pageable pageable){
		return certificateScoreDao.findAll(Specifications.where(CertificateScoreSpecifications.certificateScoreSpec(queryFastForm)), pageable);
	}
	
	@Transactional(readOnly = false)
	public void savePatchCertificateScore(List<CertificateScore> entities){
		certificateScoreDao.save(entities);
	}
	@Transactional(readOnly=false)
	public void deletePatchCertificateScore(List<CertificateScore> entities){
		certificateScoreDao.delete(entities);
	}
	
	//查找需要导出的证书数据，需要导出的证书状态为：PASSCHECKED-2，ARCHIVED-5，IMPORT-6
	public List<CertificateScore> findCertificateScoreToExport(){
		return certificateScoreDao.findCertificateScoreToExport();
	}
	
	public List<CertificateScore> findComprehensiveScoreToExport(QueryComprehensiveForm queryComprehensiveForm){
		return certificateScoreDao.findAll(Specifications.where(ComprehensiveScoreSpecification.comprehensiveSpecification(queryComprehensiveForm)));
	}
	
	/**
	 * 复杂查询分页
	 * @param queryComplexForm
	 * @param pageable
	 * @return
	 */
	public Page<CertificateScore> getComplexPaged(CertificateComplexQueryForm queryComplexForm, Pageable pageable){
		return certificateScoreDao.findAll(Specifications.where(CertificateScoreComplexSpecification.certificateScoreComplexSpec(queryComplexForm)), pageable);
	}
	
	public CertificateScore findCertificateScoreByStudentCertificateTypeAndStatus(Long studentId, Integer certificateTypeId, Integer status){
		return certificateScoreDao.findByStudentCertificateTypeAndStatus(studentId, certificateTypeId, status);
	}
	
	/**
	 * 综合成绩查询分页
	 * @param queryComprehensiveForm
	 * @param pageable
	 * @return
	 */
	public Page<CertificateScore> getComprehensivePaged(QueryComprehensiveForm queryComprehensiveForm, Pageable pageable){
		return certificateScoreDao.findAll(Specifications.where(ComprehensiveScoreSpecification.comprehensiveSpecification(queryComprehensiveForm)), pageable);
	}
}
