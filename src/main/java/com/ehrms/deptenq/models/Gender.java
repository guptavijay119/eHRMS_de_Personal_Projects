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
@Table(name="gender",schema="mumbaireference")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 

public class Gender {
	
	@Id
	@Column(name="gender_id")
	private long id;
	
	@Column(name="gender_name")
	private String genderName;
	
	@Column(name="gen_name_hi")
	private String gendernameRegional;
	
	@Transient
	private String regionalText;

	@Column(name="gender_code")
	private String genderCode;
	
	@Column(name="is_active")
	private boolean active;
	
	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	public String getGendernameRegional() {
		return gendernameRegional;
	}

	public void setGendernameRegional(String gendernameRegional) {
		this.gendernameRegional = gendernameRegional;
	}

	public String getRegionalText() {
		String lang = LocaleContextHolder.getLocale().getLanguage();
		if(lang.equalsIgnoreCase(LANGUAGECONSTANTS.ENGLISH.label)) {
			this.regionalText = this.genderName;
		} else if(lang.equalsIgnoreCase(LANGUAGECONSTANTS.MARATHI.label)) {
			this.regionalText = this.gendernameRegional;
		} else {
			this.regionalText = this.genderName;
		}
		return this.regionalText;
	}

	public void setRegionalText(String regionalText) {
		this.regionalText = regionalText;
	}

}
