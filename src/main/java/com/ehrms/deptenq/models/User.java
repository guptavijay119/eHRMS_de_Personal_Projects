package com.ehrms.deptenq.models;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.ehrms.deptenq.pims.PimsEmployee;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "public", name = "userdata", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long id;

	private String firstName;
	private String lastName;
	private String email;
	private String password;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "pims_employee_id")

	private PimsEmployee pimsEmployee;
	
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "io_id")
	private InquiryOfficerList io;
	
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "sub_department_id")
	private SubDepartment subDepartment;
	
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "usersec")
	private List<SecretarySubdepartmentMap> secsubList;
	
	
	@Column(name="process_type")
	private String processType;
	/*
	 * @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name="employee_Employee_Id")
	 * 
	 * private Employee employee;
	 */
	/*
	 * @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	 * 
	 * @JoinTable(name = "User_Departments",
	 * joinColumns= @JoinColumn(name="userdata_user_id",referencedColumnName =
	 * "user_id"), inverseJoinColumns
	 * = @JoinColumn(name="departments_departmentid",referencedColumnName =
	 * "departmentid"))
	 * 
	 * @JsonManagedReference private List<Departments> departmentList = new
	 * ArrayList<>();
	 */

//    private int locationTaluka;

	/*
	 * @Transient private boolean approved;
	 */

	/*
	 * public boolean isApproved() { return approved; }
	 * 
	 * 
	 * public void setApproved(boolean approved) { this.approved = approved; }
	 */

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
	private Set<Role> roles;

	// @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade =
	// CascadeType.ALL)
	/*
	 * @Transient private List<Notification> notificationList = new ArrayList<>();
	 */

	@Transient
	private String captcha;

	@Transient
	private String hiddenCaptcha;

	@Transient
	private String realCaptcha;
	
	@Column(name = "is_active")
	@ColumnDefault(value = "false")
	private boolean active;
	
	@Column(name = "login_type")
	private String loginType;

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getHiddenCaptcha() {
		return hiddenCaptcha;
	}

	public void setHiddenCaptcha(String hiddenCaptcha) {
		this.hiddenCaptcha = hiddenCaptcha;
	}

	public String getRealCaptcha() {
		return realCaptcha;
	}

	public void setRealCaptcha(String realCaptcha) {
		this.realCaptcha = realCaptcha;
	}

	public PimsEmployee getPimsEmployee() {
		return pimsEmployee;
	}

	public void setPimsEmployee(PimsEmployee pimsEmployee) {
		this.pimsEmployee = pimsEmployee;
	}

	/**
	 * @return the subDepartment
	 */
	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	/**
	 * @param subDepartment the subDepartment to set
	 */
	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
	}

	/**
	 * @return the processType
	 */
	public String getProcessType() {
		return processType;
	}

	/**
	 * @param processType the processType to set
	 */
	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public List<SecretarySubdepartmentMap> getSecsubList() {
		return secsubList;
	}

	public void setSecsubList(List<SecretarySubdepartmentMap> secsubList) {
		this.secsubList = secsubList;
	}

	public InquiryOfficerList getIo() {
		return io;
	}

	public void setIo(InquiryOfficerList io) {
		this.io = io;
	}

}
