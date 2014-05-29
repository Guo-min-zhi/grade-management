package org.bjtuse.egms.repository.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.bjtuse.egms.repository.entity.Teacher;
import org.bjtuse.egms.util.PredicateUtil;
import org.bjtuse.egms.web.admin.form.TeacherInfoQueryForm;
import org.springframework.data.jpa.domain.Specification;

public class TeacherInfoSpecifications {
	
	
	public static Specification<Teacher> teacherInfoSpec(final TeacherInfoQueryForm queryForm){
		
		return new Specification<Teacher>() {

			public Predicate toPredicate(Root<Teacher> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				Path<String> loginNameExp = root.get("loginName");
				Path<String> nameExp = root.get("name");
				Path<Integer> statusExp = root.get("status");
				
				Predicate temp = cb.conjunction();
				if(StringUtils.isNotBlank(queryForm.getLoginName())){
					temp = PredicateUtil.add(temp, PredicateUtil.like(cb, loginNameExp, queryForm.getLoginName()), cb);
				}
				
				if(StringUtils.isNotBlank(queryForm.getName())){
					temp = PredicateUtil.add(temp, PredicateUtil.like(cb, nameExp, queryForm.getName()), cb);
				}
				
				temp = PredicateUtil.add(temp, cb.equal(statusExp, 1), cb);
				
				return temp;
			}
			
		};
	}
}
