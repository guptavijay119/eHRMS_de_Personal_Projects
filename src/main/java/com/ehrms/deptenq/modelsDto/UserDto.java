package com.ehrms.deptenq.modelsDto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.ehrms.deptenq.models.SecretarySubdepartmentMap;

public class UserDto {
	
	private String id;
	
	private String email;
	
	private Long subDepartment;
	
	private Long globalOrg;
	
	private boolean active;
	
	private Long pimsid1;
	
	private Long pimsid2;
	
	private String loginType;
	
	private List<SecretarySubdepartmentMapDto> secsubList;


	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	/**
	 * @return the pimsid1
	 */
	public Long getPimsid1() {
		return pimsid1;
	}

	/**
	 * @param pimsid1 the pimsid1 to set
	 */
	public void setPimsid1(Long pimsid1) {
		this.pimsid1 = pimsid1;
	}

	/**
	 * @return the pimsid2
	 */
	public Long getPimsid2() {
		return pimsid2;
	}

	/**
	 * @param pimsid2 the pimsid2 to set
	 */
	public void setPimsid2(Long pimsid2) {
		this.pimsid2 = pimsid2;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the subDepartment
	 */
	public Long getSubDepartment() {
		return subDepartment;
	}

	/**
	 * @param subDepartment the subDepartment to set
	 */
	public void setSubDepartment(Long subDepartment) {
		this.subDepartment = subDepartment;
	}

	/**
	 * @return the globalOrg
	 */
	public Long getGlobalOrg() {
		return globalOrg;
	}

	/**
	 * @param globalOrg the globalOrg to set
	 */
	public void setGlobalOrg(Long globalOrg) {
		this.globalOrg = globalOrg;
	}

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

	public List<SecretarySubdepartmentMapDto> getSecsubList() {
		return secsubList;
	}

	public void setSecsubList(List<SecretarySubdepartmentMapDto> secsubList) {
		this.secsubList = secsubList;
	}
	
	

}
