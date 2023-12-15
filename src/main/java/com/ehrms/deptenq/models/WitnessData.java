package com.ehrms.deptenq.models;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="witness_list",schema="mahareference")
@DynamicUpdate(true)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class WitnessData {
	
	@Id
	@Column(name="witness_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;	

	@Column(name = "sevarth_id")
	private String sevarthId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "mobile_number")
	private String mobino;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "gender_id")
	private Gender gender;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "date_of_birth")
	private LocalDate birthDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "join_current_org_date")
	private LocalDate joinCurrentOrgdate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "superannuation_date")
	private LocalDate superannuationDate;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "department_name")
	private GlobalOrg globalorg;


	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "service_group")
	private Service_Group servicegroup;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "designation")
	private Designation designation;


	@Column(name = "is_active")
	@ColumnDefault(value = "false")
	private boolean active;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "revenue_division")
	private Division revenueDivision;

	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "district_id")
	private Districts district;


	@Column(name = "office_name", nullable = true)
	private String officeName;
	

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getSevarthId() {
		return sevarthId;
	}


	public void setSevarthId(String sevarthId) {
		this.sevarthId = sevarthId;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getMiddleName() {
		return middleName;
	}


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public Gender getGender() {
		return gender;
	}


	public void setGender(Gender gender) {
		this.gender = gender;
	}


	public LocalDate getBirthDate() {
		return birthDate;
	}


	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}


	public LocalDate getJoinCurrentOrgdate() {
		return joinCurrentOrgdate;
	}


	public void setJoinCurrentOrgdate(LocalDate joinCurrentOrgdate) {
		this.joinCurrentOrgdate = joinCurrentOrgdate;
	}


	public LocalDate getSuperannuationDate() {
		return superannuationDate;
	}


	public void setSuperannuationDate(LocalDate superannuationDate) {
		this.superannuationDate = superannuationDate;
	}


	public GlobalOrg getGlobalorg() {
		return globalorg;
	}


	public void setGlobalorg(GlobalOrg globalorg) {
		this.globalorg = globalorg;
	}


	public Service_Group getServicegroup() {
		return servicegroup;
	}


	public void setServicegroup(Service_Group servicegroup) {
		this.servicegroup = servicegroup;
	}


	public Designation getDesignation() {
		return designation;
	}


	public void setDesignation(Designation designation) {
		this.designation = designation;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public Division getRevenueDivision() {
		return revenueDivision;
	}


	public void setRevenueDivision(Division revenueDivision) {
		this.revenueDivision = revenueDivision;
	}


	public Districts getDistrict() {
		return district;
	}


	public void setDistrict(Districts district) {
		this.district = district;
	}


	public String getOfficeName() {
		return officeName;
	}


	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getMobino() {
		return mobino;
	}


	public void setMobino(String mobino) {
		this.mobino = mobino;
	}


}
