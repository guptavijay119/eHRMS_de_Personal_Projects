package com.ehrms.deptenq.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "mahareference", name = "subdepartment")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class SubDepartment extends Auditable<String> {

	@Id
//	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "subdepartment_id_seq", name = "subdepartment_id_seq")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subdepartment_id_seq")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "subdepartment_id")
	private Long id;

	@Column(name = "subdepartment_name_en")
	private String subdepartmentNameEn;

	@Column(name = "subdepartment_name_mr")
	private String subdepartmentNameMr;

	@Transient
	private String regionalText;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "globalorg_id")
	private GlobalOrg globalorg;

	@Column(name = "is_active")
	@ColumnDefault(value = "false")
	private boolean active;

	
	
	/*
	 * Generaating getters and setters
	 */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubdepartmentNameEn() {
		return subdepartmentNameEn;
	}

	public void setSubdepartmentNameEn(String subdepartmentNameEn) {
		this.subdepartmentNameEn = subdepartmentNameEn;
	}

	public String getSubdepartmentNameMr() {
		return subdepartmentNameMr;
	}

	public void setSubdepartmentNameMr(String subdepartmentNameMr) {
		this.subdepartmentNameMr = subdepartmentNameMr;
	}

	public String getRegionalText() {
		return regionalText;
	}

	public void setRegionalText(String regionalText) {
		this.regionalText = regionalText;
	}

	public GlobalOrg getGlobalorg() {
		return globalorg;
	}

	public void setGlobalorg(GlobalOrg globalorg) {
		this.globalorg = globalorg;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
