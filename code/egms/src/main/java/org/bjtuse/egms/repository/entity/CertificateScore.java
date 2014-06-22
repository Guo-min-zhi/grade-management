package org.bjtuse.egms.repository.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Getter
@Setter
@Entity
@Table(name = "certificate_score")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CertificateScore extends IdEntity<Long> {

	
	/** @Fields serialVersionUID: */
	  	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "basic_info_id")
	private Student studentInfo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "certificate_type_id")
	private CertificateType certificateType;
	
	@Column(name = "source_score")
	private Float sourceScore;
	
	@Column(name = "translated_score")
	private Float translatedScore;
	
	@Column(name = "certificate_photo_url", length = 100)
	private String cerfificatePhotoUrl;
	
	@Column(name = "certificate_acquire_time")
	private Date certificateAcquireTime;
	
	@Column(name = "check_website", length = 100)
	private String checkWebsite;
	
	@Column(name = "check_website_screenshot", length = 100)
	private String checkWebsiteScreenshot;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "verify_times")
	private Integer verifyTimes;
	
	@Column(name = "submit_time")
	private Timestamp submitTime;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "verify_teacher_a")
	private Teacher verifyTeacherA;
	
	@Column(name = "verify_time_a")
	private Timestamp verifyTimeA;
	
	@Column(name = "verify_status_a")
	private Integer verifyStatusA;
	
	@Column(name = "comment_a", length = 100)
	private String commentA;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "verify_teacher_b")
	private Teacher verifyTeacherB;
	
	@Column(name = "verify_time_b")
	private Timestamp verifyTimeB;
	
	@Column(name = "verify_status_b")
	private Integer verifyStatusB;
	
	@Column(name = "comment_b", length = 100)
	private String commentB;
	
	@Column(name = "grade_a")
	private Float gradeA;
	
	@Column(name = "grade_b")
	private Float gradeB;
	
	@Column(name = "grade_c")
	private Float gradeC;
	
	@Column(name = "oral_score")
	private Float oralScore;
	
	@Column(name = "written_score")
	private Float writtenScore;
	
	@Column(name = "grade_final", length = 100)
	private String gradeFinal;
	
	/*
	 * 0-成绩不全，无法计算综合成绩
	 * 1-可以计算综合成绩，未导出
	 * 2-可以计算综合成绩，已导出
	 */
	@Column(name = "grade_status")
	private Integer gradeStatus;
}
