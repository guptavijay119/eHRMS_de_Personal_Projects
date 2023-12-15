package com.ehrms.deptenq.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.springframework.context.i18n.LocaleContextHolder;

import com.ehrms.deptenq.constants.LANGUAGECONSTANTS;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Audited
@Table(schema = "mahareference",name="process_names")
@DynamicUpdate(true)
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class ProcessName {
	@Id
	private long id;
	
	@Column(name="process_name")
	private String name;
	
	@Column(name="process_name_regional")
	private String nameRegional;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="module_id")
	private ModuleName moduleName;
	
	@Column(name="default_href_link")
	private String defaultHrefLink;

	@Transient
	private String regionalText;
	
	
	

	public ProcessName(long id) {
		this.id = id;
	}

	public ProcessName() {
		// TODO Auto-generated constructor stub
	}

	public String getDefaultHrefLink() {
		return defaultHrefLink;
	}

	public void setDefaultHrefLink(String defaultHrefLink) {
		this.defaultHrefLink = defaultHrefLink;
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

	public String getNameRegional() {
		return nameRegional;
	}

	public void setNameRegional(String nameRegional) {
		this.nameRegional = nameRegional;
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

	public ModuleName getModuleName() {
		return moduleName;
	}

	public void setModuleName(ModuleName moduleName) {
		this.moduleName = moduleName;
	}
	

}
