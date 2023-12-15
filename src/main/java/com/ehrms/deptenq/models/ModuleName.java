package com.ehrms.deptenq.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.springframework.context.i18n.LocaleContextHolder;

import com.ehrms.deptenq.constants.LANGUAGECONSTANTS;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "mahareference",name="module_names")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class ModuleName {
	
	@Id
	private long id;
	
	@Column(name="module_name")
	private String name;
	
	@Column(name="module_name_regional")
	private String nameRegional;

	@Transient
	private String regionalText;
	
	
	public String getNameRegional() {
		return nameRegional;
	}

	public void setNameRegional(String nameRegional) {
		this.nameRegional = nameRegional;
	}

	public String getRegionalText() {
		String lang = LocaleContextHolder.getLocale().getLanguage();
		if(lang.equalsIgnoreCase(LANGUAGECONSTANTS.ENGLISH.label)) {
			this.regionalText = this.name;
		} else if(lang.equalsIgnoreCase(LANGUAGECONSTANTS.MARATHI.label)) {
			this.regionalText = this.nameRegional;
		} else {
			this.regionalText = this.name;
		}
		return this.regionalText;
	}

	public void setRegionalText(String regionalText) {
		this.regionalText = regionalText;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

}
