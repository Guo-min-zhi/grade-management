package org.bjtuse.egms.repository.spec;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.bjtuse.egms.repository.entity.CertificateScore;
import org.bjtuse.egms.repository.entity.Student;
import org.bjtuse.egms.util.PredicateUtil;
import org.bjtuse.egms.web.teacher.form.CertificateFastQueryForm;
import org.springframework.data.jpa.domain.Specification;

public class CertificateScoreSpecifications {

	public static Specification<CertificateScore> certificateScoreSpec(final CertificateFastQueryForm queryFastForm){
		
		return new Specification<CertificateScore>() {

			public Predicate toPredicate(Root<CertificateScore> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				
				Join<CertificateScore, Student> certificateScoreJoin =  root.join(root.getModel().getSingularAttribute("studentInfo",Student.class),  JoinType.LEFT);
				Path<String> loginNameExp = certificateScoreJoin.get("loginName");
				Path<String> collegeExp = certificateScoreJoin.get("college");
				Path<String> gradeExp = certificateScoreJoin.get("grade");
				Path<String> majorExp = certificateScoreJoin.get("major");
				
				Predicate temp = cb.conjunction();;
				if(StringUtils.isNotBlank(queryFastForm.getStudentNumber())){
					temp = PredicateUtil.add(temp, PredicateUtil.like(cb, loginNameExp, queryFastForm.getStudentNumber()), cb);
				}
				if(StringUtils.isNotBlank(queryFastForm.getStudentCollege())){
					temp = PredicateUtil.add(temp, PredicateUtil.like(cb, collegeExp, queryFastForm.getStudentCollege()), cb);
				}
				if(StringUtils.isNotBlank(queryFastForm.getStudentGrade())){
					temp = PredicateUtil.add(temp, PredicateUtil.like(cb, gradeExp, queryFastForm.getStudentGrade()), cb);
				}
				if(StringUtils.isNotBlank(queryFastForm.getStudentMajor())){
					temp = PredicateUtil.add(temp, PredicateUtil.like(cb, majorExp, queryFastForm.getStudentMajor()), cb);
				}
				if(StringUtils.isNotBlank(queryFastForm.getStudentCertificateStatus())){
					temp = PredicateUtil.add(temp, cb.equal(root.get("status"), queryFastForm.getStudentCertificateStatus()), cb);
				}else{
					Integer[] values = new Integer[]{1,2,3,4,6};
					temp = PredicateUtil.add(temp, root.get("status").in(values), cb);					
				}
				if(queryFastForm.getCertificateType() != null){
					temp = PredicateUtil.add(temp, cb.equal(root.get("certificateType"), queryFastForm.getCertificateType()), cb);
				}
				
				return temp;
			}
		};
		
	}
}
