package org.bjtuse.egms.repository.dao;

import org.bjtuse.egms.repository.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeacherDao extends JpaRepository<Teacher, Long>,
		JpaSpecificationExecutor<Teacher> {

	Teacher findTeacherByLoginName(String loginName);
	
	@Modifying
	@Query("update Teacher t set t.status = 0 where t.id=:id")
	void disableTeacher(@Param("id")Long id);
	
	Teacher findTeacherByLoginNameAndStatus(String loginName, Integer status);
}
