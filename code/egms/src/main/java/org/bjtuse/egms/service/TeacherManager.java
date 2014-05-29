package org.bjtuse.egms.service;

import java.util.List;

import org.bjtuse.egms.repository.dao.TeacherDao;
import org.bjtuse.egms.repository.entity.Teacher;
import org.bjtuse.egms.repository.spec.TeacherInfoSpecifications;
import org.bjtuse.egms.web.admin.form.TeacherInfoQueryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional(readOnly = true)
public class TeacherManager {

	@Autowired
	private TeacherDao teacherDao;
	
	public Teacher findTeacherByLoginName(String loginName){
		return teacherDao.findTeacherByLoginName(loginName);
	}
	
	public Teacher findTeacherByLoginNameAndStatus(String loginName, Integer status){
		return teacherDao.findTeacherByLoginNameAndStatus(loginName, status);
	}
	
	@Transactional(readOnly = false)
	public void saveTeacher(Teacher teacher){
		teacherDao.save(teacher);
	}
	
	public Page<Teacher> getPaged(TeacherInfoQueryForm queryForm, Pageable pageable){
		return teacherDao.findAll(Specifications.where(TeacherInfoSpecifications.teacherInfoSpec(queryForm)), pageable);
	}
	
	public Teacher findTeacherById(Long id){
		return teacherDao.findOne(id);
	}
	
	public List<Teacher> findAll(){
		return teacherDao.findAll();
	}
	
	/*
	 * 由于Teacher实体与CertificateScore有引用关系
	 * 所以如果真的在数据库中删除教师的记录会导致程序
	 * 报错，所以教师的删除操作是将教师用户的状态在数据
	 * 库中设置为0
	 */
	@Transactional(readOnly = false)
	public void disableTeacher(Long id){
		teacherDao.disableTeacher(id);
	}
	
}
