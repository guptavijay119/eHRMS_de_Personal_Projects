package com.ehrms.deptenq.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ehrms.deptenq.constants.LANGUAGECONSTANTS;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "mahareference",name="process_list_dynamic")
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate(true)
@Audited
@AuditOverride(forClass = Auditable.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProcessListDynamic extends Auditable<String>{
	
	@Id
	@SequenceGenerator(schema = "recruitment",allocationSize=1, initialValue=1, sequenceName="processlist_id_seq", name="processlist_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "processlist_id_seq")
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="module_id")
	private ModuleName moduleName;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="process_id")
	private ProcessName processName;
	
	@Column(name="hreflink")
	private String hreflink;
	
	@Column(name="link_name")
	private String linkName;
	
	@Column(name="link_name_regional")
	private String linkNameRegional;
	
	@Transient
	private String regionalLinkName;
		
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="role_id")
	private Role role;

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkNameRegional() {
		return linkNameRegional;
	}

	public void setLinkNameRegional(String linkNameRegional) {
		this.linkNameRegional = linkNameRegional;
	}

	public String getRegionalLinkName() {
		String lang = LocaleContextHolder.getLocale().getLanguage();
		if(lang.equalsIgnoreCase(LANGUAGECONSTANTS.ENGLISH.label)) {
			this.regionalLinkName = this.linkName;
		} else if(lang.equalsIgnoreCase(LANGUAGECONSTANTS.MARATHI.label)) {
			this.regionalLinkName = this.linkNameRegional;
		} else {
			this.regionalLinkName = this.linkName;
		}
		return this.regionalLinkName;
	}

	public void setRegionalLinkName(String regionalLinkName) {
		this.regionalLinkName = regionalLinkName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ModuleName getModuleName() {
		return moduleName;
	}

	public void setModuleName(ModuleName moduleName) {
		this.moduleName = moduleName;
	}

	public ProcessName getProcessName() {
		return processName;
	}

	public void setProcessName(ProcessName processName) {
		this.processName = processName;
	}

	public String getHreflink() {
		return hreflink;
	}

	public void setHreflink(String hreflink) {
		this.hreflink = hreflink;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	
	

}
