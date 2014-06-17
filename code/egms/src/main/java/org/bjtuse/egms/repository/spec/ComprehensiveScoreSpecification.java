package org.bjtuse.egms.repository.spec;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.bjtuse.egms.web.teacher.form.QueryComprehensiveForm;
import org.springframework.data.jpa.domain.Specification;

public class ComprehensiveScoreSpecification {

	public static Specification<CertificateScore> comprehensiveSpecification(final QueryComprehensiveForm queryComprehensiveForm){
		return new Specification<CertificateScore>() {

			public Predicate toPredicate(Root<CertificateScore> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				Join<CertificateScore, Student> certificateScoreJoin =  root.join(root.getModel().getSingularAttribute("studentInfo",Student.class),  JoinType.LEFT);
				Path<Date> updateStampExp = root.get("submitTime");
				Predicate temp = cb.conjunction();
				// 学号查询
				if(StringUtils.isNotBlank(queryComprehensiveForm.getStudentNumber())){
					temp = PredicateUtil.add(temp, cb.equal(certificateScoreJoin.get("loginName"), queryComprehensiveForm.getStudentNumber()), cb);
				}
				// 姓名查询
				if(StringUtils.isNotBlank(queryComprehensiveForm.getStudentName())){
					temp = PredicateUtil.add(temp, cb.equal(certificateScoreJoin.get("name"), queryComprehensiveForm.getStudentNumber()), cb);
				}
				// 成绩类型查询
				if(queryComprehensiveForm.getCertificateType() != null){
					temp = PredicateUtil.add(temp, cb.equal(root.get("certificateType"), queryComprehensiveForm.getCertificateType()), cb);
				}
				// 时间段查询
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String startTime = queryComprehensiveForm.getStartTime();
				String endTime = queryComprehensiveForm.getEndTime();
				if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
					Date start = null;
					Date end = null;
					try {
						start = sdf.parse(startTime);
						end = sdf.parse(endTime);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					temp = PredicateUtil.add(temp, cb.between(updateStampExp, start, end), cb);
				}else if(StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)){
					Date start = null;
					try {
						start = sdf.parse(startTime);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					temp = PredicateUtil.add(temp, cb.greaterThan(updateStampExp, start), cb);
				}else if(StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)){
					Date end = null;
					try {
						end = sdf.parse(endTime);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					temp = PredicateUtil.add(temp, cb.lessThan(updateStampExp, end), cb);
				}
				
				return temp;
			}
		};
	}
	
}
