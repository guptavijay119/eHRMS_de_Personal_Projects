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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.context.i18n.LocaleContextHolder;

import com.ehrms.deptenq.constants.LANGUAGECONSTANTS;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "mumbaireference",name = "designation")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "designation_id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Designation{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long designation_id;

//	@NotEmpty(message = "{validation.proposal.designation}")
	private String designation_name_en;
	private String designation_name_hi;
	private String designation_name_regional;
	
	@Transient
	private String regionalText;
	private String description;
	private String designation_rank_name_en;

	// @NotEmpty(message = "{validation.proposal.designation}")
	private String service_group;
	
	@Column(name="is_active")
	private boolean active;

	@Column(name="is_gazetted_1")
	private boolean isGazetted;
	public Designation(Long designation_id, boolean isGazetted) {
		this.designation_id = designation_id;
		this.isGazetted = isGazetted;
	}


	@Column(name="designation_code")
	private String designationCode;

	private String Designation;

	/*
	 * @OneToMany(mappedBy = "designation", fetch = FetchType.LAZY, cascade =
	 * CascadeType.ALL) List<RRules> rrulesList = new ArrayList<>();
	 */
	
	/*
	 * @OneToMany(mappedBy = "designationObj", fetch = FetchType.LAZY, cascade =
	 * CascadeType.ALL) private List<Proposal> proposalList = new ArrayList<>();
	 */
	
	/*
	 * @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "probationId") private Probation probation;
	 */

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "service_group_id")
	private Service_Group serviceGroupId;

	/*
	 * @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "payband_id") private Payband payBand;
	 */

//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "designation")
//	private List<FilePromotions> filePromo;
//
//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "designation")
//	private List<FileProbations> fileProba;
//
//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "designation")
//	private List<FilePermanencyCertificate> filePerman;
//
//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "designation")
//	private List<Employee> employee;

	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name="global_org_id")
	private GlobalOrg globalOrg;
	
	/*
	 * @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name="minimum_degree_level") private DegreeLevel
	 * minimumDegreeLevel;
	 */

	
	
	
	/*
	 * public DegreeLevel getMinimumDegreeLevel() { return minimumDegreeLevel; }
	 * 
	 * public void setMinimumDegreeLevel(DegreeLevel minimumDegreeLevel) {
	 * this.minimumDegreeLevel = minimumDegreeLevel; }
	 */

	public Designation() {
		
	}

	public Designation(Long designation_id) {	
		this.designation_id = designation_id;
	}

	public Long getDesignation_id() {
		return designation_id;
	}

	public void setDesignation_id(Long designation_id) {
		this.designation_id = designation_id;
	}

	public String getDesignation_name_en() {
		return designation_name_en;
	}

	public void setDesignation_name_en(String designation_name_en) {
		this.designation_name_en = designation_name_en;
	}

	public String getDesignation_name_hi() {
		return designation_name_hi;
	}

	public void setDesignation_name_hi(String designation_name_hi) {
		this.designation_name_hi = designation_name_hi;
	}

	public String getDesignation_name_regional() {
		return designation_name_regional;
	}

	public void setDesignation_name_regional(String designation_name_regional) {
		this.designation_name_regional = designation_name_regional;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDesignation_rank_name_en() {
		return designation_rank_name_en;
	}

	public void setDesignation_rank_name_en(String designation_rank_name_en) {
		this.designation_rank_name_en = designation_rank_name_en;
	}

	public String getService_group() {
		return service_group;
	}

	public void setService_group(String service_group) {
		this.service_group = service_group;
	}

	
	

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public GlobalOrg getGlobalOrg() {
		return globalOrg;
	}

	public void setGlobalOrg(GlobalOrg globalOrg) {
		this.globalOrg = globalOrg;
	}

	public boolean isGazetted() {
		return isGazetted;
	}

	public void setGazetted(boolean isGazetted) {
		this.isGazetted = isGazetted;
	}


	public String getDesignationCode() {
		return designationCode;
	}

	public void setDesignationCode(String designationCode) {
		this.designationCode = designationCode;
	}

	public String getDesignation() {
		return Designation;
	}

	public void setDesignation(String designation) {
		Designation = designation;
	}

	/*
	 * public List<RRules> getRrulesList() { return rrulesList; }
	 * 
	 * public void setRrulesList(List<RRules> rrulesList) { this.rrulesList =
	 * rrulesList; }
	 */

	/*
	 * public List<Proposal> getProposalList() { return proposalList; }
	 * 
	 * public void setProposalList(List<Proposal> proposalList) { this.proposalList
	 * = proposalList; }
	 * 
	 * public Probation getProbation() { return probation; }
	 * 
	 * public void setProbation(Probation probation) { this.probation = probation; }
	 */
	
	
	public Service_Group getServiceGroupId() {
		return serviceGroupId;
	}

	public void setServiceGroupId(Service_Group serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}

	/*
	 * public Payband getPayBand() { return payBand; }
	 * 
	 * public void setPayBand(Payband payBand) { this.payBand = payBand; }
	 */
//	public List<FilePromotions> getFilePromo() {
//		return filePromo;
//	}
//
//	public void setFilePromo(List<FilePromotions> filePromo) {
//		this.filePromo = filePromo;
//	}
//
//	public List<FileProbations> getFileProba() {
//		return fileProba;
//	}
//
//	public void setFileProba(List<FileProbations> fileProba) {
//		this.fileProba = fileProba;
//	}
//
//	public List<FilePermanencyCertificate> getFilePerman() {
//		return filePerman;
//	}
//
//	public void setFilePerman(List<FilePermanencyCertificate> filePerman) {
//		this.filePerman = filePerman;
//	}
//
//	public List<Employee> getEmployee() {
//		return employee;
//	}
//
//	public void setEmployee(List<Employee> employee) {
//		this.employee = employee;
//	}


	public String getRegionalText() {
		String lang = LocaleContextHolder.getLocale().getLanguage();
		if(lang.equalsIgnoreCase(LANGUAGECONSTANTS.ENGLISH.label)) {
			this.regionalText = this.designation_name_en;
		} else if(lang.equalsIgnoreCase(LANGUAGECONSTANTS.MARATHI.label)) {
			this.regionalText = this.designation_name_hi;
		} else {
			this.regionalText = this.designation_name_en;
		}
		return this.regionalText;
	}

	public void setRegionalText(String regionalText) {
		
		this.regionalText = regionalText;
	}
	
	
}
