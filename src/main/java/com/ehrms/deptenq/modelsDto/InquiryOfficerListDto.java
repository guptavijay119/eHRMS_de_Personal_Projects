package com.ehrms.deptenq.modelsDto;


import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;


public class InquiryOfficerListDto{
	

	private Long id;
	
	private String inquiryOfficerlist;
	
	private String appellation;


	private String firstName;
	
	private String middleName;
	
	private String lastName;
	

	private String designation;
	
	private Integer order;
	
	private String description;
	
	private String mobileNumber;
	
	
	private String email;
	

	private String lastOfficeName;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateofBirth ;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateofretirement;
	

	private Long division;

	private Long district;
	

	
	private boolean active;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateofappointment;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate appointmentUpto;
	
	private String appointmentOrderName;
	
	private String appointmentOrderFilePath;


	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getInquiryOfficerlist() {
		return inquiryOfficerlist;
	}



	public void setInquiryOfficerlist(String inquiryOfficerlist) {
		this.inquiryOfficerlist = inquiryOfficerlist;
	}



	public String getAppellation() {
		return appellation;
	}



	public void setAppellation(String appellation) {
		this.appellation = appellation;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getMiddelName() {
		return middleName;
	}



	public void setMiddelName(String middleName) {
		this.middleName = middleName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public String getDesignation() {
		return designation;
	}



	public void setDesignation(String designation) {
		this.designation = designation;
	}



	public Integer getOrder() {
		return order;
	}



	public void setOrder(Integer order) {
		this.order = order;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getMobileNumber() {
		return mobileNumber;
	}



	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getLastOfficeName() {
		return lastOfficeName;
	}



	public void setLastOfficeName(String lastOfficeName) {
		this.lastOfficeName = lastOfficeName;
	}



	public LocalDate getDateofBirth() {
		return dateofBirth;
	}



	public void setDateofBirth(LocalDate dateofBirth) {
		this.dateofBirth = dateofBirth;
	}



	public LocalDate getDateofretirement() {
		return dateofretirement;
	}



	public void setDateofretirement(LocalDate dateofretirement) {
		this.dateofretirement = dateofretirement;
	}



	public Long getDivision() {
		return division;
	}



	public void setDivision(Long division) {
		this.division = division;
	}



	public Long getDistrict() {
		return district;
	}



	public void setDistrict(Long district) {
		this.district = district;
	}



	public boolean isActive() {
		return active;
	}



	public void setActive(boolean active) {
		this.active = active;
	}



	public String getMiddleName() {
		return middleName;
	}



	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}



	public LocalDate getDateofappointment() {
		return dateofappointment;
	}



	public void setDateofappointment(LocalDate dateofappointment) {
		this.dateofappointment = dateofappointment;
	}



	public LocalDate getAppointmentUpto() {
		return appointmentUpto;
	}



	public void setAppointmentUpto(LocalDate appointmentUpto) {
		this.appointmentUpto = appointmentUpto;
	}



	public String getAppointmentOrderName() {
		return appointmentOrderName;
	}



	public void setAppointmentOrderName(String appointmentOrderName) {
		this.appointmentOrderName = appointmentOrderName;
	}



	public String getAppointmentOrderFilePath() {
		return appointmentOrderFilePath;
	}



	public void setAppointmentOrderFilePath(String appointmentOrderFilePath) {
		this.appointmentOrderFilePath = appointmentOrderFilePath;
	}
	

}
