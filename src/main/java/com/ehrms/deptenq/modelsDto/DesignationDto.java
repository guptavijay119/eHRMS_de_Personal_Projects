package com.ehrms.deptenq.modelsDto;


import org.springframework.context.i18n.LocaleContextHolder;

import com.ehrms.deptenq.constants.LANGUAGECONSTANTS;
import com.ehrms.deptenq.models.Service_Group;


public class DesignationDto{

	private Long designation_id;

	private String designation_name_en;
	private String designation_name_hi;
	private String designation_name_regional;
	
	private String regionalText;
	private String description;
	private String designation_rank_name_en;

	private String service_group;
	
	private boolean active;

	private boolean isGazetted;



	private String designationCode;

	private String Designation;


	private Long serviceGroupId;


	public DesignationDto(Long designation_id) {	
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


	





	public Long getServiceGroupId() {
		return serviceGroupId;
	}

	public void setServiceGroupId(Long serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}

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
