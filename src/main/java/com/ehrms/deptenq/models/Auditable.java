package com.ehrms.deptenq.models;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.envers.RevisionEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@RevisionEntity
public abstract class Auditable<U> {

    @CreatedBy
    @Column(updatable = false)
    protected U createdBy;

    @CreatedDate
 //   @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedBy
    protected U lastModifiedBy;

    @LastModifiedDate
//    @Temporal(TemporalType.TIMESTAMP)
    protected LocalDateTime lastModifiedDate;
    
     
    protected String remoteIp;
    
//    public String getRemoteIp() {
//		
//		String ipAddress = "";
//		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
//			ipAddress = ((WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
//					.getRemoteAddress();
//		}
//		return ipAddress;
//    	
//	}
//
//	public void setRemoteIp(String remoteIp) {
//
//		remoteIp = "";
//		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
//			remoteIp = ((WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
//					.getRemoteAddress();
//		}
//		this.remoteIp = remoteIp;
// 
//	}
	
    
    
    
	@PrePersist
	public void prePresistRemoteIpAddress() {
//			this.remoteIp = ((WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
//					.getRemoteAddress();

		   ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		    HttpServletRequest req = sra.getRequest();  
		this.remoteIp = req.getRemoteAddr();
	}
	
	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	@PreUpdate
	public void preUpdateRemoteIpAddress() {
//			this.remoteIp = ((WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
//					.getRemoteAddress();

		   ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		    HttpServletRequest req = sra.getRequest();  
		this.remoteIp = req.getRemoteAddr();
	}

	public U getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(U createdBy) {
        this.createdBy = createdBy;
    }

 

    public U getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(U lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

 
}