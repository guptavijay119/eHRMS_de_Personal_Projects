package com.ehrms.deptenq.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "mumbaireference",name="payband")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Payband{
	
	@Id
	@Column(name="payband_id")
	private long id;
	
	@Column(name="low_limit")
	@ColumnDefault(value="0")
	private int lowLimit;
	
	@Column(name="high_limit")
	@ColumnDefault(value="0")
	private int highLimit;
	
	@Column(name="payband_name")
	private String paybandName;
	
	@Column(name="payband_code")
	private String paybandCode;
	
	@Column(name="payband_description")
	private String paybandDescription;
	
	@Column(name="is_active")
	@ColumnDefault(value="true")
	private boolean isActive;
	
	@Column(name="fixed")
	@ColumnDefault(value="0")
	private int fixed;
	
//	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//	@JoinColumn(name="pay_commission_id")
//	private PayCommission payCommision;
	
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="service_group_id")
	private Service_Group serviceGroup;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public int getLowLimit() {
		return lowLimit;
	}


	public void setLowLimit(int lowLimit) {
		this.lowLimit = lowLimit;
	}


	public int getHighLimit() {
		return highLimit;
	}


	public void setHighLimit(int highLimit) {
		this.highLimit = highLimit;
	}


	public String getPaybandName() {
		return paybandName;
	}


	public void setPaybandName(String paybandName) {
		this.paybandName = paybandName;
	}


	public String getPaybandCode() {
		return paybandCode;
	}


	public void setPaybandCode(String paybandCode) {
		this.paybandCode = paybandCode;
	}


	public String getPaybandDescription() {
		return paybandDescription;
	}


	public void setPaybandDescription(String paybandDescription) {
		this.paybandDescription = paybandDescription;
	}


	public boolean isActive() {
		return isActive;
	}


	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}


	public int getFixed() {
		return fixed;
	}


	public void setFixed(int fixed) {
		this.fixed = fixed;
	}


//	public PayCommission getPayCommision() {
//		return payCommision;
//	}
//
//
//	public void setPayCommision(PayCommission payCommision) {
//		this.payCommision = payCommision;
//	}


	public Service_Group getServiceGroup() {
		return serviceGroup;
	}


	public void setServiceGroup(Service_Group serviceGroup) {
		this.serviceGroup = serviceGroup;
	}

}
