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
@Table(schema = "mahareference",name = "misconduct_types")
@EntityListeners(AuditingEntityListener.class)
@Audited
@AuditOverride(forClass = Auditable.class)
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MisconductTypesMaster extends Auditable<String>{

	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "misconduct_id_seq", name = "misconduct_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "misconduct_id_seq")
	@Column(name="misconduct_id")
	private Long id;
	
	@Column(name = "misconduct_code")
	private String misconductCode;
	
	@Column(name = "misconduct_name")
	private String misconductName;
	
	@Column(name = "misconduct_description")
	private String misconductDescription;
	
    @Column(name = "is_active")
	private boolean isActive;
    
    @Column(name="order")
    private int order;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMisconductCode() {
		return misconductCode;
	}

	public void setMisconductCode(String misconductCode) {
		this.misconductCode = misconductCode;
	}

	public String getMisconductName() {
		return misconductName;
	}

	public void setMisconductName(String misconductName) {
		this.misconductName = misconductName;
	}

	public String getMisconductDescription() {
		return misconductDescription;
	}

	public void setMisconductDescription(String misconductDescription) {
		this.misconductDescription = misconductDescription;
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
