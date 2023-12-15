package com.ehrms.deptenq.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="court_name",schema="mahareference")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 

public class CourtName {
	
	@Id
	@Column(name="court_id")
	private long id;
	
	@Column(name="court_name")
	private String courtName;
	
	@Column(name="court_name_hi")
	private String courtnameRegional;
	
	@Transient
	private String regionalText;

	@Column(name="court_code")
	private String courtCode;
	
	@Column(name="is_active")
	private boolean active;

	
	// generatin getter and setter
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	public String getCourtnameRegional() {
		return courtnameRegional;
	}

	public void setCourtnameRegional(String courtnameRegional) {
		this.courtnameRegional = courtnameRegional;
	}

	public String getRegionalText() {
		return regionalText;
	}

	public void setRegionalText(String regionalText) {
		this.regionalText = regionalText;
	}

	public String getCourtCode() {
		return courtCode;
	}

	public void setCourtCode(String courtCode) {
		this.courtCode = courtCode;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	
	
}
