package com.ehrms.deptenq.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "mahareference",name = "reason_keeping_case_abeyance")
@EntityListeners(AuditingEntityListener.class)
@Audited
@AuditOverride(forClass = Auditable.class)
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ReasonForKeepingCaseAbeyance extends Auditable<String>{

	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "reason_keeping_case_abeyance_id_seq", name = "reason_keeping_case_abeyance_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "misconduct_id_seq")
	@Column(name="reason_keeping_case_abeyance_id")
	private Long id;
	

	@Column(name = "reasonkeepingcaseabeyance_code")
	private String reasonKeepingCaseAbeyanceCode;
	
	@Column(name = "reasonkeepingcaseabeyance_name")
	private String reasonKeepingCaseAbeyanceCodeName;
	
	@Column(name = "reasonkeepingcaseabeyance_description")
	private String reasonKeepingCaseAbeyanceDescription;
	
	@Column(name="is_active")
	private boolean active;
	
	
	
	/*
	 * @Column(name = "is_active", nullable=false) private boolean isActive;
	 */
    
  
    //generating Getters & Setters
    
	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReasonKeepingCaseAbeyanceCode() {
		return reasonKeepingCaseAbeyanceCode;
	}

	public void setReasonKeepingCaseAbeyanceCode(String reasonKeepingCaseAbeyanceCode) {
		this.reasonKeepingCaseAbeyanceCode = reasonKeepingCaseAbeyanceCode;
	}

	public String getReasonKeepingCaseAbeyanceCodeName() {
		return reasonKeepingCaseAbeyanceCodeName;
	}

	public void setReasonKeepingCaseAbeyanceCodeName(String reasonKeepingCaseAbeyanceCodeName) {
		this.reasonKeepingCaseAbeyanceCodeName = reasonKeepingCaseAbeyanceCodeName;
	}

	public String getReasonKeepingCaseAbeyanceDescription() {
		return reasonKeepingCaseAbeyanceDescription;
	}

	public void setReasonKeepingCaseAbeyanceDescription(String reasonKeepingCaseAbeyanceDescription) {
		this.reasonKeepingCaseAbeyanceDescription = reasonKeepingCaseAbeyanceDescription;
	}

	/*
	 * public boolean isActive() { return isActive; }
	 * 
	 * public void setActive(boolean isActive) { this.isActive = isActive; }
	 */

	
}
