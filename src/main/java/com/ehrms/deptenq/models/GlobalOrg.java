package com.ehrms.deptenq.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "mumbaireference",name="global_org")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class GlobalOrg {
	
	@Id
	@Column(name="global_org_id")
	private long id;
	
	@Column(name="global_org_code")
	private long globalOrgCode;
	
	@Column(name="global_org_name")
	private String globalOrgName;
	
	@Column(name="global_org_short")
	private String globalOrgShort;
	
	@Column(name="is_parent")
	@ColumnDefault(value="false")
	private boolean parentOrgCode;
	
	@Column(name="is_active")
	@ColumnDefault(value="false")
	private boolean active;
	
	@Column(name="tenant_id")
	private String tenantId;
	
//===================AuditFields==================================================//	

//	@Column(name="created_date")
//	private LocalDate createdDate;
//	
//	@Column(name="created_employee_code")
//	private String createdEmployeeCode;
//	
//	@Column(name="created_global_org_code")
//	private String createdGlobalOrgCode;
//
//	@Column(name="modified_date")
//	private LocalDate modifiedDate;
//	
//	@Column(name="ip_address")
//	private String ipAddress;
//	
//	@Column(name="user_id")
//	private long userId;
//=======================================================================//	

	/**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGlobalOrgCode() {
		return globalOrgCode;
	}

	public void setGlobalOrgCode(long globalOrgCode) {
		this.globalOrgCode = globalOrgCode;
	}

	public String getGlobalOrgName() {
		return globalOrgName;
	}

	public void setGlobalOrgName(String globalOrgName) {
		this.globalOrgName = globalOrgName;
	}

	public boolean isParentOrgCode() {
		return parentOrgCode;
	}

	public void setParentOrgCode(boolean parentOrgCode) {
		this.parentOrgCode = parentOrgCode;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getGlobalOrgShort() {
		return globalOrgShort;
	}

	public void setGlobalOrgShort(String globalOrgShort) {
		this.globalOrgShort = globalOrgShort;
	}

}
