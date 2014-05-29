package org.bjtuse.egms.repository.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.bjtuse.egms.repository.entity.Student;
import org.bjtuse.egms.util.PredicateUtil;
import org.bjtuse.egms.web.admin.form.StudentInfoQueryForm;
import org.springframework.data.jpa.domain.Specification;

public class StudentInfoSpecifications {
	
	public static Specification<Student> studentInfoSpec(final StudentInfoQueryForm queryForm){
		
		return new Specification<Student>() {

			public Predicate toPredicate(Root<Student> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				Path<String> loginNameExp = root.get("loginName");
				Path<String> collegeExp = root.get("college");
				Path<String> gradeExp = root.get("grade");
				Path<String> majorExp = root.get("major");
				Path<Integer> statusExp = root.get("status");
				
				Predicate temp = cb.conjunction();
				if(StringUtils.isNotBlank(queryForm.getStudentNum())){
					temp = PredicateUtil.add(temp, PredicateUtil.like(cb, loginNameExp, queryForm.getStudentNum()), cb);
				}
				
				if(StringUtils.isNotBlank(queryForm.getCollege())){
					temp = PredicateUtil.add(temp, PredicateUtil.like(cb, collegeExp, queryForm.getCollege()), cb);
				}
				
				if(StringUtils.isNotBlank(queryForm.getGrade())){
					temp = PredicateUtil.add(temp, PredicateUtil.like(cb, gradeExp, queryForm.getGrade()), cb);
				}
				
				if(StringUtils.isNotBlank(queryForm.getMajor())){
					PredicateUtil.add(temp, PredicateUtil.like(cb, majorExp, queryForm.getMajor()), cb);
				}
				
				temp = PredicateUtil.add(temp, cb.equal(statusExp, 1), cb);
				
				return temp;
			}
			
		};
	}
}
