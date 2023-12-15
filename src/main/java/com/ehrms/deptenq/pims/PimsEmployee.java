package com.ehrms.deptenq.pims;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ehrms.deptenq.models.GlobalOrg;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@Table(schema = "pims",name="employee")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PimsEmployee {
	
	@Id
	@Column(name="employee_id")
	private long id;
	
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="global_org_id")
	private GlobalOrg globalOrgId;
	
	@Column(name="name_en")
	private String employeeName;
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public GlobalOrg getGlobalOrgId() {
		return globalOrgId;
	}

	public void setGlobalOrgId(GlobalOrg globalOrgId) {
		this.globalOrgId = globalOrgId;
	}
	
	
	
}
