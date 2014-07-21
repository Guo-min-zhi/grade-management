package org.bjtuse.egms.service;

import java.util.Iterator;
import java.util.List;

import org.bjtuse.egms.repository.dao.StudentDao;
import org.bjtuse.egms.repository.entity.Student;
import org.bjtuse.egms.repository.spec.StudentInfoSpecifications;
import org.bjtuse.egms.web.admin.form.StudentInfoQueryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Administrator
 *
 */
@Component
@Transactional(readOnly = true)
public class StudentManager {
	
	@Autowired
	private StudentDao studentDao;
	
	public Student findUserByLoginName(String loginName){
		return studentDao.findStudentByLoginName(loginName);
	}
	
	public Student findUserByLoginNameAndStatus(String loginName, Integer status){
		return studentDao.findStudentByLoginNameAndStatus(loginName, status);
	}
	
	@Transactional(readOnly = false)
	public void save(Student student){
		studentDao.save(student);
	}
	
	public Page<Student> getPaged(StudentInfoQueryForm queryForm, Pageable pageable){
		return studentDao.findAll(Specifications.where(StudentInfoSpecifications.studentInfoSpec(queryForm)), pageable);
	}
	
	public Student findStudentById(Long id){
		return studentDao.findOne(id);
	}
	
	public List<Student> findAll(){
		return studentDao.findAll();
	}
	
	public List<Student> findSatisfiedStudent(){
		return studentDao.findStudentByStatus(1);
	}
	
	@Transactional(readOnly = false)
	public void disableStudent(Long id){
		studentDao.disableStudent(id);
	}
	
	/**
	 * 删除学生并没有真正的删除，而是更改了标志位
	 * @param entities
	 */
	@Transactional(readOnly=false)
	public void deletePatchStudent(List<Student> entities){
		Iterator<Student> iterator = entities.iterator();
		while(iterator.hasNext()){
			disableStudent(iterator.next().getId());
		}
	}
	
//	public Page<Student> getStuPaged(CertificateFastQueryForm queryFastForm, Pageable pageable){
//		return studentDao.findAll(Specifications.where(CertificateScoreSpecifications.certificateScoreSpec(queryFastForm)), pageable);
//	}
	
}
