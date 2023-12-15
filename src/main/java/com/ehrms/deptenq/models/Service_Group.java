package com.ehrms.deptenq.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(schema = "mumbaireference",name="service_group")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Service_Group {

	@Id
	@Column(name="service_group_id")
	private Long id;
	
	private String service_group_name;
	
	@Column(name="service_group_name_mr")
	private String serviceNameMr;
	
	@Transient
	private String regionalText;
	
	@Column(name="is_active")
	private boolean active;
	
	@OneToMany(mappedBy = "serviceGroupId",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<Designation> designationList = new ArrayList<>();
	
	

	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public String getServiceNameMr() {
		return serviceNameMr;
	}


	public void setServiceNameMr(String serviceNameMr) {
		this.serviceNameMr = serviceNameMr;
	}


	public String getRegionalText() {
		String lang = LocaleContextHolder.getLocale().getLanguage();
		if(lang.equalsIgnoreCase(LANGUAGECONSTANTS.ENGLISH.label)) 
		{
			this.regionalText = this.service_group_name;
		} 
		else if(lang.equalsIgnoreCase(LANGUAGECONSTANTS.MARATHI.label)) 
		{
			this.regionalText = this.serviceNameMr;
		} 
		else 
		{
			this.regionalText = this.service_group_name;
		}
		return this.regionalText;
	}


	public void setRegionalText(String regionalText) {
		this.regionalText = regionalText;
	}


	public Service_Group() {
	}

	
	public Service_Group(Long id) {
		this.id = id;
	}
	
	


	/**
	 * @param id
	 * @param service_group_name
	 */
	public Service_Group(Long id, String service_group_name) {
		this.id = id;
		this.service_group_name = service_group_name;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public List<Designation> getDesignationList() {
		return designationList;
	}
	public void setDesignationList(List<Designation> designationList) {
		this.designationList = designationList;
	}
	
	public String getService_group_name() {
		return service_group_name;
	}
	public void setService_group_name(String service_group_name) {
		this.service_group_name = service_group_name;
	}

}
