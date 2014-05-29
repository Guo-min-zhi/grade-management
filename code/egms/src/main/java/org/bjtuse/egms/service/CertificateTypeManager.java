package org.bjtuse.egms.service;

import java.util.List;

import org.bjtuse.egms.repository.dao.CertificateTypeDao;
import org.bjtuse.egms.repository.entity.CertificateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class CertificateTypeManager {
	
	@Autowired
	private CertificateTypeDao certificateTypeDao;
	
	public List<CertificateType> getAllCertificateType(){
		return certificateTypeDao.findAll();
	}
	
	public CertificateType getCertificateById(int id){
		return certificateTypeDao.findOne(id);
	}
	
	public CertificateType getCertificateTypeByName(String name){
		return certificateTypeDao.findCertificateTypeByCertificateName(name);
	}
	
	@Transactional(readOnly = false)
	public void save(CertificateType certificateType){
		certificateTypeDao.save(certificateType);
	}
	
	@Transactional(readOnly = false)
	public void delete(Integer id){
		certificateTypeDao.delete(id);
	}
}
