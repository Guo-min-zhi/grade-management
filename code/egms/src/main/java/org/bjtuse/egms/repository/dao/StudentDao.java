package org.bjtuse.egms.repository.dao;

import java.util.List;

import org.bjtuse.egms.repository.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentDao extends JpaRepository<Student, Long>,
		JpaSpecificationExecutor<Student> {
	
	Student findStudentByLoginName(String loginName);
	
	@Modifying
	@Query("update Student s set s.status = 0 where s.id = :id")
	void disableStudent(@Param("id")Long id);
	
	Student findStudentByLoginNameAndStatus(String loginName, Integer status);
	
	
	List<Student> findStudentByStatus(Integer status);
}
