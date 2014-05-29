package org.bjtuse.egms.repository.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.bjtuse.egms.repository.entity.CertificateScore;
import org.bjtuse.egms.repository.entity.Student;
import org.bjtuse.egms.repository.entity.Teacher;
import org.bjtuse.egms.util.PredicateUtil;
import org.bjtuse.egms.web.teacher.form.CertificateComplexQueryForm;
import org.springframework.data.jpa.domain.Specification;

public class CertificateScoreComplexSpecification {

	public static Specification<CertificateScore> certificateScoreComplexSpec(final CertificateComplexQueryForm queryComplexForm){
		return new Specification<CertificateScore>() {

			public Predicate toPredicate(Root<CertificateScore> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				Join<CertificateScore, Student> certificateScoreJoin =  root.join(root.getModel().getSingularAttribute("studentInfo",Student.class),  JoinType.LEFT);
				Join<CertificateScore, Teacher> certificateTeacherJoinA = root.join(root.getModel().getSingularAttribute("verifyTeacherA", Teacher.class), JoinType.LEFT);
				Join<CertificateScore, Teacher> certificateTeacherJoinB = root.join(root.getModel().getSingularAttribute("verifyTeacherB", Teacher.class), JoinType.LEFT);
				
				Predicate temp = cb.conjunction();;
				if(StringUtils.isNotBlank(queryComplexForm.getStudentNumber())){
					temp = PredicateUtil.add(temp, cb.equal(certificateScoreJoin.get("loginName"), queryComplexForm.getStudentNumber()), cb);
				}
				if(StringUtils.isNotBlank(queryComplexForm.getStudentCollege())){
					temp = PredicateUtil.add(temp, cb.equal(certificateScoreJoin.get("college"), queryComplexForm.getStudentCollege()), cb);
				}
				if(StringUtils.isNotBlank(queryComplexForm.getStudentGrade())){
					temp = PredicateUtil.add(temp, cb.equal(certificateScoreJoin.get("grade"), queryComplexForm.getStudentGrade()), cb);
				}
				if(StringUtils.isNotBlank(queryComplexForm.getStudentMajor())){
					temp = PredicateUtil.add(temp, cb.equal(certificateScoreJoin.get("major"), queryComplexForm.getStudentMajor()), cb);
				}
				if(StringUtils.isNotBlank(queryComplexForm.getStudentName())){
					temp = PredicateUtil.add(temp, cb.equal(certificateScoreJoin.get("name"), queryComplexForm.getStudentName()), cb);
				}
				if(queryComplexForm.getCheckTimes() != null){
					temp = PredicateUtil.add(temp, cb.equal(root.get("verifyTimes"), queryComplexForm.getCheckTimes()), cb);
				}
				if(StringUtils.isNotBlank(queryComplexForm.getCheckPerson())){
					Predicate temp2 = cb.equal(certificateTeacherJoinA.get("name"), queryComplexForm.getCheckPerson());
					temp2 = PredicateUtil.or(temp2, cb.equal(certificateTeacherJoinB.get("name"), queryComplexForm.getCheckPerson()), cb);
					temp = PredicateUtil.add(temp, temp2, cb);
				}
				if(StringUtils.isNotBlank(queryComplexForm.getStudentCertificateStatus())){
					temp = PredicateUtil.add(temp, cb.equal(root.get("status"), queryComplexForm.getStudentCertificateStatus()), cb);
				}
				if(queryComplexForm.getCertificateType() != null){
					temp = PredicateUtil.add(temp, cb.equal(root.get("certificateType"), queryComplexForm.getCertificateType()), cb);
				}
				return temp;
			}
		};
	}
}
