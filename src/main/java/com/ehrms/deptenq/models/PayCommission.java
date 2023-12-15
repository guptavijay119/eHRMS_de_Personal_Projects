package com.ehrms.deptenq.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "mumbaireference",name="pay_commission")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "payCommisisonId")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PayCommission {
	
	@Id
	@Column(name="pay_commission_id")
	private long payCommisisonId;
	
	@Column(name="pay_commission_name")
	private String payCommissionName;
	
	@Column(name="pay_commission_description")
	private String payCommissionDescription;
	
	@Column(name="is_active")
	private boolean isActive;
	
	@Column(name="pay_commission_date_from")
	private LocalDate payCommissionDateFrom;

	public long getPayCommisisonId() {
		return payCommisisonId;
	}

	public void setPayCommisisonId(long payCommisisonId) {
		this.payCommisisonId = payCommisisonId;
	}

	public String getPayCommissionName() {
		return payCommissionName;
	}

	public void setPayCommissionName(String payCommissionName) {
		this.payCommissionName = payCommissionName;
	}

	public String getPayCommissionDescription() {
		return payCommissionDescription;
	}

	public void setPayCommissionDescription(String payCommissionDescription) {
		this.payCommissionDescription = payCommissionDescription;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDate getPayCommissionDateFrom() {
		return payCommissionDateFrom;
	}

	public void setPayCommissionDateFrom(LocalDate payCommissionDateFrom) {
		this.payCommissionDateFrom = payCommissionDateFrom;
	}

}
