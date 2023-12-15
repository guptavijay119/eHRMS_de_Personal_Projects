package com.ehrms.deptenq.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.context.i18n.LocaleContextHolder;

import com.ehrms.deptenq.constants.LANGUAGECONSTANTS;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "mumbaireference",name="state")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "stateCode")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class States {
	
	@Id
	@Column(name="state_id")
	private Long stateCode;
	
	@Column(name="state_name_en")
	private String stateName;

	@Column(name="state_name_hi")
	private String stateNameMr;
	
//	@Column(name="type")
//	private char type;
	
	@Column(name="state_code")
	private String stateNameCode;
	
	@Transient
	private String regionalText;

	@Column(name="is_active")
	private boolean active;

	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getStateNameMr() {
		return stateNameMr;
	}

	public void setStateNameMr(String stateNameMr) {
		this.stateNameMr = stateNameMr;
	}

	public String getRegionalText() {
		String lang = LocaleContextHolder.getLocale().getLanguage();
		if(lang.equalsIgnoreCase(LANGUAGECONSTANTS.ENGLISH.label)) {
			this.regionalText = this.stateName;
		} else if(lang.equalsIgnoreCase(LANGUAGECONSTANTS.MARATHI.label)) {
			this.regionalText = this.stateNameMr;
		} else {
			this.regionalText = this.stateNameMr;
		}
		return regionalText;
	}

	public void setRegionalText(String regionalText) {
		this.regionalText = regionalText;
	}

	public void setStateCode(Long stateCode) {
		this.stateCode = stateCode;
	}

	public States() {
	}

	public States(Long stateCode) {
		this.stateCode = stateCode;
	}

	public Long getStateCode() {
		return stateCode;
	}

	public void setStateCode(long stateCode) {
		this.stateCode = stateCode;
	}



	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getStateNameCode() {
		return stateNameCode;
	}

	public void setStateNameCode(String stateNameCode) {
		this.stateNameCode = stateNameCode;
	}

//	public char getType() {
//		return type;
//	}
//
//	public void setType(char type) {
//		this.type = type;
//	}
	
	

}
