package com.ehrms.deptenq.modelsDto;

public class ChangePassword {
	
	private String oldpassword;
	
	private String newpassword;
	
	private String confirmnewpassword;

	/**
	 * @return the confirmnewpassword
	 */
	public String getConfirmnewpassword() {
		return confirmnewpassword;
	}

	/**
	 * @param confirmnewpassword the confirmnewpassword to set
	 */
	public void setConfirmnewpassword(String confirmnewpassword) {
		this.confirmnewpassword = confirmnewpassword;
	}

	/**
	 * @return the oldpassword
	 */
	public String getOldpassword() {
		return oldpassword;
	}

	/**
	 * @param oldpassword the oldpassword to set
	 */
	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}

	/**
	 * @return the newpassword
	 */
	public String getNewpassword() {
		return newpassword;
	}

	/**
	 * @param newpassword the newpassword to set
	 */
	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}
	
	

}
