package com.ehrms.deptenq.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "mahareference",name = "sub_rules_applicable")
@EntityListeners(AuditingEntityListener.class)
@Audited
@AuditOverride(forClass = Auditable.class)
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class SubRuleApplicableMaster extends Auditable<String>{
	
	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "sub_rule_id_seq", name = "sub_rule_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sub_rule_id_seq")
	@Column(name="sub_rule_id")
	private Long id;
	
	@Column(name = "rule_code")
	private String ruleCode;
	
	@Column(name = "rule_name")
	private String ruleName;
	
	@Column(name = "rule_description")
	private String ruleDescription;
	
    @Column(name = "is_active")
	private boolean isActive;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the ruleCode
	 */
	public String getRuleCode() {
		return ruleCode;
	}

	/**
	 * @param ruleCode the ruleCode to set
	 */
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * @param ruleName the ruleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * @return the ruleDescription
	 */
	public String getRuleDescription() {
		return ruleDescription;
	}

	/**
	 * @param ruleDescription the ruleDescription to set
	 */
	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
