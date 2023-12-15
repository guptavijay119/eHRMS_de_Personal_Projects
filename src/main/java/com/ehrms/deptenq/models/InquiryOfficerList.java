package com.ehrms.deptenq.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="inquiry_officer_list",schema="mahareference")
@DynamicUpdate(true)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class InquiryOfficerList{
	
	@Id
	@Column(name="inquiry_officer_code")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="inquiry_officer")
	private String inquiryOfficerlist;
	
	@Column(name="appellation")
	private String appellation;


	@Column(name="first_name")
	private String firstName;
	
	@Column(name="middel_name")
	private String middelName;
	
	@Column(name="last_name")
	private String lastName;
	
//	@Column(name="name_mr")	
//	private String nameMr;

	@Column(name="original_designation")
	private String designation;
	
	@Column(name="order")
	private int order;
	
	@Column(name="description")
	private String description;
	
	@Column(name="mobile_number")
	private String mobileNumber;
	
	
	@Column(name="email")
	private String email;
	
	@Column(name="last_office_name")
	private String lastOfficeName;
	
	@Column(name="date_of_birth")
	private LocalDate dateofBirth ;
	
	@Column(name="date_of_retirement")
	private LocalDate dateofretirement;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="division_id")
	private Division division;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="district_id")
	private Districts district;
	
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "listOfInquiryOfficer")
	private List<InquiryOfficerDetails> iodetaillist = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "listOfInquiryOfficer")
	private List<InquiryOfficerDetails> iodetaillistdecided = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "listOfInquiryOfficer")
	private List<InquiryOfficerDetails> ioReportIssued = new ArrayList<>();
	
	@Column(name="is_active")
	private boolean active;
	
	
	@Column(name="date_of_appointment")
	private LocalDate dateofappointment;
	
	@Column(name="appointment_upto")
	private LocalDate appointmentUpto;
	
	@Column(name="appointment_order_name")
	private String appointmentOrderName;
	
	@Column(name="appointment_order_file_path")
	private String appointmentOrderFilePath;
	

	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy =
	 * "divisionId") private List<Districts> districList = new ArrayList<>();
	 * 
	 * public List<Districts> getDistricList() { return districList; }
	 * 
	 * public void setDistricList(List<Districts> districList) { this.districList =
	 * districList; }
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getInquiryOfficerlist() {
		return inquiryOfficerlist;
	}

	public void setInquiryOfficerlist(String inquiryOfficerlist) {
		this.inquiryOfficerlist = inquiryOfficerlist;
	}



	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middelName
	 */
	public String getMiddelName() {
		return middelName;
	}

	/**
	 * @param middelName the middelName to set
	 */
	public void setMiddelName(String middelName) {
		this.middelName = middelName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * @param designation the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * @return the appellation
	 */
	public String getAppellation() {
		return appellation;
	}

	/**
	 * @param appellation the appellation to set
	 */
	public void setAppellation(String appellation) {
		this.appellation = appellation;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public Districts getDistrict() {
		return district;
	}

	public void setDistrict(Districts district) {
		this.district = district;
	}

	public List<InquiryOfficerDetails> getIodetaillist() {
		if(iodetaillist.isEmpty()) {
			return iodetaillist;		
		} else {
			return iodetaillist.stream().filter(s->(s.getCasedetails() != null 
					&& s.getCasedetails().getPendingWith() != null 
					&& !s.getCasedetails().getPendingWith().equalsIgnoreCase("Case Decided") 
					
					&& !s.isInquiryReportSubmitted())).collect(Collectors.toList());
		}
			
	}

	public void setIodetaillist(List<InquiryOfficerDetails> iodetaillist) {
		this.iodetaillist = iodetaillist;
	}

	public List<InquiryOfficerDetails> getIodetaillistdecided() {
		if(iodetaillistdecided.isEmpty()) {
			return iodetaillistdecided;
		} else {
			return iodetaillistdecided.stream().filter(s->(s.getCasedetails() != null 
					&& s.getCasedetails().getPendingWith() != null 
					&& s.getCasedetails().getPendingWith().equalsIgnoreCase("Case Decided"))).collect(Collectors.toList());
		}
	}

	public void setIodetaillistdecided(List<InquiryOfficerDetails> iodetaillistdecided) {
		this.iodetaillistdecided = iodetaillistdecided;
	}

	public List<InquiryOfficerDetails> getIoReportIssued() {
		if(iodetaillistdecided.isEmpty()) {
			return ioReportIssued;
		} else {
			return ioReportIssued.stream().filter(s->s.isInquiryReportSubmitted()).collect(Collectors.toList());
		}
		
	}

	public void setIoReportIssued(List<InquiryOfficerDetails> ioReportIssued) {
		this.ioReportIssued = ioReportIssued;
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
