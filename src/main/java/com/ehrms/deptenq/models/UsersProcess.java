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

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "public", name = "users_process")
@EntityListeners(AuditingEntityListener.class)
@Audited
@DynamicUpdate(true)
@AuditOverride(forClass = Auditable.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UsersProcess extends Auditable<String> {
	@Id
	@SequenceGenerator(schema = "public", allocationSize = 1, initialValue = 1, sequenceName = "usersprocess_id", name = "usersprocess_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersprocess_id")
	@Column(name = "usersprocess_id")
	private long id;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "current_user_id")
	private User user;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "process_name_id")	
	private ProcessName processName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ProcessName getProcessName() {
		return processName;
	}

	public void setProcessName(ProcessName processName) {
		this.processName = processName;
	}

}
