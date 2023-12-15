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
@Table(schema="mahareference",name="type_of_penalty_list")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 

public class TypeOfPenaltyInflictedPojo {
	
	@Id
	@Column(name="type_of_penalty_id")
	private Long id;
	
	@Column(name="type_of_penalty_name_mr")
	private String typeOfPenaltyNameMr;
	
	@Column(name="type_of_penalty_name_en")
	private String typeOfPenaltyNameEn;
	
	@Transient
	private String regionalText;

	@Column(name="type_of_penalty_code")
	private String typeOfPenaltyCode;
	
	@Column(name = "is_active")
	@ColumnDefault(value = "false")
	private boolean active;
	
	// generatin getter and setter

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeOfPenaltyNameMr() {
		return typeOfPenaltyNameMr;
	}

	public void setTypeOfPenaltyNameMr(String typeOfPenaltyNameMr) {
		this.typeOfPenaltyNameMr = typeOfPenaltyNameMr;
	}

	public String getTypeOfPenaltyNameEn() {
		return typeOfPenaltyNameEn;
	}

	public void setTypeOfPenaltyNameEn(String typeOfPenaltyNameEn) {
		this.typeOfPenaltyNameEn = typeOfPenaltyNameEn;
	}

	public String getRegionalText() {
		return regionalText;
	}

	public void setRegionalText(String regionalText) {
		this.regionalText = regionalText;
	}

	public String getTypeOfPenaltyCode() {
		return typeOfPenaltyCode;
	}

	public void setTypeOfPenaltyCode(String typeOfPenaltyCode) {
		this.typeOfPenaltyCode = typeOfPenaltyCode;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	
	
	
}
