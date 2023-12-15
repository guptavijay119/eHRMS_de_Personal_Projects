package com.ehrms.deptenq.modelsDto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class WitnessDataDto {

	private Long id;	

	private String sevarthid;

	private String firstName;

	private String middleName;

	private String lastName;
	
	private String email;
	
	private String mobino;


	private Long genderid;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate joinCurrentOrgdate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate superannuationDate;

	private Long orgid;



	private Long groupid;


	private Long desigid;



	private boolean active;

	private Long divid;

	

	private Long distid;


	private String officeName;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getSevarthid() {
		return sevarthid;
	}


	public void setSevarthid(String sevarthid) {
		this.sevarthid = sevarthid;
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


	public Long getGenderid() {
		return genderid;
	}


	public void setGenderid(Long genderid) {
		this.genderid = genderid;
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


	public Long getOrgid() {
		return orgid;
	}


	public void setOrgid(Long orgid) {
		this.orgid = orgid;
	}


	public Long getGroupid() {
		return groupid;
	}


	public void setGroupid(Long groupid) {
		this.groupid = groupid;
	}


	public Long getDesigid() {
		return desigid;
	}


	public void setDesigid(Long desigid) {
		this.desigid = desigid;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public Long getDivid() {
		return divid;
	}


	public void setDivid(Long divid) {
		this.divid = divid;
	}


	public Long getDistid() {
		return distid;
	}


	public void setDistid(Long distid) {
		this.distid = distid;
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
