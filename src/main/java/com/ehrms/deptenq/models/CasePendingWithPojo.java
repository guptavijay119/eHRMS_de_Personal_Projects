package com.ehrms.deptenq.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema="mahareference",name="case_pending_list")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 

public class CasePendingWithPojo {
	
	@Id
	@Column(name="case_pending_id")
	private Long id;
	
	@Column(name="case_pending_name_mr")
	private String casePendingNameMr;
	
	@Column(name="case_pending_name_en")
	private String casePendingNameEn;
	
	@Transient
	private String regionalText;

	@Column(name="case_pending_code")
	private String casePendingCode;
	
	@Column(name = "is_active")
	@ColumnDefault(value = "false")
	private boolean active;
	
	
	// generating  getters &  setters
	
	

	public String getCasePendingNameMr() {
		return casePendingNameMr;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public void setCasePendingNameMr(String casePendingNameMr) {
		this.casePendingNameMr = casePendingNameMr;
	}

	public String getCasePendingNameEn() {
		return casePendingNameEn;
	}

	public void setCasePendingNameEn(String casePendingNameEn) {
		this.casePendingNameEn = casePendingNameEn;
	}

	public String getRegionalText() {
		return regionalText;
	}

	public void setRegionalText(String regionalText) {
		this.regionalText = regionalText;
	}

	public String getCasePendingCode() {
		return casePendingCode;
	}

	public void setCasePendingCode(String casePendingCode) {
		this.casePendingCode = casePendingCode;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}


	
	
	
	
	
	
}
