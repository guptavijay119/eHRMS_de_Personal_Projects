package com.ehrms.deptenq.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "departmentalenquiry", name = "hearing_participants")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class HearingParticipants extends Auditable<String>{
	
	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "hearing_participants_id_seq", name = "hearing_participants_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hearing_participants_id_seq")
	@Column(name = "hearing_participants_id")
	private Long id;
	

	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="hearing_details_id")
	private HearingDetails hearing;
	
	@Column(name="participant_type")
	private String participantType;
	
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="io_id")
	private InquiryOfficerList io;
	
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="po_id")
	private PresentingOfficerDetails po;
	
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="emp_id")
	private EmployeeDetails emp;
	
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="witness_id")
	private WitnessData witness;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public HearingDetails getHearing() {
		return hearing;
	}


	public void setHearing(HearingDetails hearing) {
		this.hearing = hearing;
	}


	public String getParticipantType() {
		return participantType;
	}


	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}


	public InquiryOfficerList getIo() {
		return io;
	}


	public void setIo(InquiryOfficerList io) {
		this.io = io;
	}


	public PresentingOfficerDetails getPo() {
		return po;
	}


	public void setPo(PresentingOfficerDetails po) {
		this.po = po;
	}


	public EmployeeDetails getEmp() {
		return emp;
	}


	public void setEmp(EmployeeDetails emp) {
		this.emp = emp;
	}


	public WitnessData getWitness() {
		return witness;
	}


	public void setWitness(WitnessData witness) {
		this.witness = witness;
	}
	


}
