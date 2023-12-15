package com.ehrms.deptenq.configuration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("multitanency.mtapp.datasources")
public class ReadProperties {
    private List<String> tanentid;
    private List<String> url;
    private List<String> username;
    private List<String> password;
    private List<String> driverclassname;
	/**
	 * @return the tanentid
	 */
	public List<String> getTanentid() {
		return tanentid;
	}
	/**
	 * @param tanentid the tanentid to set
	 */
	public void setTanentid(List<String> tanentid) {
		this.tanentid = tanentid;
	}
	/**
	 * @return the url
	 */
	public List<String> getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(List<String> url) {
		this.url = url;
	}
	/**
	 * @return the username
	 */
	public List<String> getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(List<String> username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public List<String> getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(List<String> password) {
		this.password = password;
	}
	/**
	 * @return the driverclassname
	 */
	public List<String> getDriverclassname() {
		return driverclassname;
	}
	/**
	 * @param driverclassname the driverclassname to set
	 */
	public void setDriverclassname(List<String> driverclassname) {
		this.driverclassname = driverclassname;
	}
    
   
}
