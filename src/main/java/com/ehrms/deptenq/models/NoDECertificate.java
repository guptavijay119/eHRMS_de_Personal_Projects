package com.ehrms.deptenq.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;



@Entity
@Table(schema ="departmentalenquiry", name="no_de_certificate")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditOverride(forClass = Auditable.class)
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class NoDECertificate extends Auditable<String>{
	
	@Id
	@SequenceGenerator(schema = "departmentalenquiry",allocationSize=1, initialValue=1, sequenceName="no_de_certificate_id_seq", name="no_de_certificate_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "no_de_certificate_id_seq")
	private Long id;
	@Column(name="certificate_content", length = 10485760,columnDefinition = "TEXT")
	private String grData;
	
	@Column(name="finalize")
	@ColumnDefault(value="false")
	private boolean finalize;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "emp_id")
	private EmployeeDetails emp;
	
	@Column(name="sevarthid")
	private String sevarthid;
	
	@Column(name="certificate_type")
	private Long certificateType;
	
	@Column(name="employee_name")
	private String employeeName;
	
	@Column(name="designation_name")
	private String designationName;
	
	
	@Column(name="superannuation_date")
	private String superannuationDate;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "org_id")
	private GlobalOrg org;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "subdepartment_id")
	private SubDepartment subDepartment;
	
	@Column(name="counter")
	private Integer counter;

	public String getSevarthid() {
		return sevarthid;
	}

	public void setSevarthid(String sevarthid) {
		this.sevarthid = sevarthid;
	}

	public EmployeeDetails getEmp() {
		return emp;
	}

	public void setEmp(EmployeeDetails emp) {
		this.emp = emp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGrData() {
		return grData;
	}

	public void setGrData(String grData) {
		this.grData = grData;
	}

	public boolean isFinalize() {
		return finalize;
	}

	public void setFinalize(boolean finalize) {
		this.finalize = finalize;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDesignationName() {
		return designationName;
	}

	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	public GlobalOrg getOrg() {
		return org;
	}

	public void setOrg(GlobalOrg org) {
		this.org = org;
	}

	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
	}

	public Integer getCounter() {
		return counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
	}

	public Long getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(Long certificateType) {
		this.certificateType = certificateType;
	}

	public String getSuperannuationDate() {
		return superannuationDate;
	}

	public void setSuperannuationDate(String superannuationDate) {
		this.superannuationDate = superannuationDate;
	}



	

}
