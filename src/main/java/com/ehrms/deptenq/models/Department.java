package com.ehrms.deptenq.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ehrms.deptenq.constants.LANGUAGECONSTANTS;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "public",name="department")
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate(true)
@Audited
@AuditOverride(forClass = Auditable.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Department extends Auditable<String>{
	
	@Id
	@SequenceGenerator(schema = "public",allocationSize = 1, initialValue = 1, sequenceName = "department_id_seq", name = "department_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_id_seq")
	@Column(name="department_id")
	private long id;
	
	@Column(name="department_name")
	private String departmentName;
	
	@Column(name="department_name_regional")
	private String departmentNameRegional;
	
	@Transient
	private String regionalText;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="parent_department_id")
	private Department parentDepartment;

	@Column(name="department_code")
	private String departmentCode;

	public String getDepartmentNameRegional() {
		return departmentNameRegional;
	}

	public void setDepartmentNameRegional(String departmentNameRegional) {
		this.departmentNameRegional = departmentNameRegional;
	}

	public String getRegionalText() {
		String lang = LocaleContextHolder.getLocale().getLanguage();
		if(lang.equalsIgnoreCase(LANGUAGECONSTANTS.ENGLISH.label)) {
			this.regionalText = this.departmentName;
		} else if(lang.equalsIgnoreCase(LANGUAGECONSTANTS.MARATHI.label)) {
			this.regionalText = this.departmentNameRegional;
		} else {
			this.regionalText = this.departmentName;
		}
		return regionalText;
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

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Department getParentDepartment() {
		return parentDepartment;
	}

	public void setParentDepartment(Department parentDepartment) {
		this.parentDepartment = parentDepartment;
	}

	public Department() {
	}

	public Department(long id) {
		this.id = id;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	
}
