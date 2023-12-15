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
@Table(schema = "mahareference",name = "suspension_rules")
@EntityListeners(AuditingEntityListener.class)
 @Audited
@AuditOverride(forClass = Auditable.class)
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class SuspensionRules extends Auditable<String>{
	
	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "suspension_rules_seq", name = "suspension_rules_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suspension_rules_seq")
	@Column(name="sus_rule_id")
	private Long id;
	
	@Column(name = "sus_rule_code")
	private String susRuleCode;
	
	@Column(name = "sus_rule_name")
	private String susRuleName;
	
	@Column(name = "sus_rule_description")
	private String susRuleDescription;
	
    @Column(name = "is_active")
	private boolean isActive;
    
    @Column(name = "order")
    private int order;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSusRuleCode() {
		return susRuleCode;
	}

	public void setSusRuleCode(String susRuleCode) {
		this.susRuleCode = susRuleCode;
	}

	public String getSusRuleName() {
		return susRuleName;
	}

	public void setSusRuleName(String susRuleName) {
		this.susRuleName = susRuleName;
	}

	public String getSusRuleDescription() {
		return susRuleDescription;
	}

	public void setSusRuleDescription(String susRuleDescription) {
		this.susRuleDescription = susRuleDescription;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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
