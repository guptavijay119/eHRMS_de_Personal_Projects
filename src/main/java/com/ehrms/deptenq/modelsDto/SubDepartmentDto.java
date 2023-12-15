package com.ehrms.deptenq.modelsDto;

public class SubDepartmentDto {
	
	private String id;
	
	private String subDepartment;
	
	private Long globalOrg;
	
	private boolean active;



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
	 * @return the subDepartment
	 */
	public String getSubDepartment() {
		return subDepartment;
	}

	/**
	 * @param subDepartment the subDepartment to set
	 */
	public void setSubDepartment(String subDepartment) {
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
	
	
	

}
