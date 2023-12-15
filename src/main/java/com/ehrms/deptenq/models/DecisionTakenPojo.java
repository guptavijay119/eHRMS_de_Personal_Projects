package com.ehrms.deptenq.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema="mahareference",name="decision_taken_list")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 

public class DecisionTakenPojo {
	
	@Id
	@Column(name="decision_id")
	private Long id;
	
	@Column(name="decision_name_mr")
	private String decisionNameMr;
	
	@Column(name="decision_name_en")
	private String decisionNameHindiEn;
	
	@Transient
	private String regionalText;

	@Column(name="decision_code")
	private String decisionCode;
	
	@Column(name="is_active")
	private boolean active;
	
	@Column(name="order")
	private int order;

	// generatin getter and setter
	
	
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

	
	
	public String getRegionalText() {
		return regionalText;
	}

	public void setRegionalText(String regionalText) {
		this.regionalText = regionalText;
	}

	public String getDecisionCode() {
		return decisionCode;
	}

	public void setDecisionCode(String decisionCode) {
		this.decisionCode = decisionCode;
	}

	public String getDecisionNameMr() {
		return decisionNameMr;
	}

	public void setDecisionNameMr(String decisionNameMr) {
		this.decisionNameMr = decisionNameMr;
	}

	public String getDecisionNameHindiEn() {
		return decisionNameHindiEn;
	}

	public void setDecisionNameHindiEn(String decisionNameHindiEn) {
		this.decisionNameHindiEn = decisionNameHindiEn;
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
	
	
	
	
}
