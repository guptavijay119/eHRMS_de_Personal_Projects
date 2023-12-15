package com.ehrms.deptenq.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="gradepay",schema = "public")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "gradPayId")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class GradePay {
	
	@Id
	@Column(name="gradepay_id")
	private long gradPayId;
	
	@Column(name="gradepay")
	private int gradepay;
	
	
	  @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)  
	  @JoinColumn(name="payband_id") 
	  private Payband payBand;
	 
	
	@Column(name="gradepay_code")
	private String gradePayCode;
	
	@Column(name="min_basic")
	private int minBasic;

	public long getGradPayId() {
		return gradPayId;
	}

	public void setGradPayId(long gradPayId) {
		this.gradPayId = gradPayId;
	}

	public int getGradepay() {
		return gradepay;
	}

	public void setGradepay(int gradepay) {
		this.gradepay = gradepay;
	}

	/*
	 * public Payband getPayBand() { return payBand; }
	 * 
	 * public void setPayBand(Payband payBand) { this.payBand = payBand; }
	 */

	public String getGradePayCode() {
		return gradePayCode;
	}

	public void setGradePayCode(String gradePayCode) {
		this.gradePayCode = gradePayCode;
	}

	public int getMinBasic() {
		return minBasic;
	}

	public void setMinBasic(int minBasic) {
		this.minBasic = minBasic;
	}
}
