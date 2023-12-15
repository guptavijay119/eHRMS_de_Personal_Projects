package com.ehrms.deptenq.models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="login_audit",schema = "public")
public class LoginAudit {
	
	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "login_audit_id_seq", name = "login_audit_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "login_audit_id_seq")
	private long id;
	
	private String email;
	
	private boolean success;
	
	private LocalDateTime datetime;
	
	private String ipaddress;

	/**
	 * @return the ipaddress
	 */
	public String getIpaddress() {
		return ipaddress;
	}

	/**
	 * @param ipaddress the ipaddress to set
	 */
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the datetime
	 */
	public LocalDateTime getDatetime() {
		return datetime;
	}

	/**
	 * @param datetime the datetime to set
	 */
	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}
	

}
