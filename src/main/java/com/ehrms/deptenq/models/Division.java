package com.ehrms.deptenq.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="division_master_location")
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate(true)
@Audited
@AuditOverride(forClass = Auditable.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Division extends Auditable<String>{
	
	@Id
	@Column(name="division_code")
	private long id;
	
	@Column(name="division_name")
	private String divisionName;
	
	@Column(name="is_active")
	private boolean active;
	
	@Column(name="order")
	private int order;
	
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

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	

}
