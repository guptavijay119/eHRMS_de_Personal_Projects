package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.GlobalOrg;


@Repository
public interface IGlobalOrgRepository extends JpaRepository<GlobalOrg, Long> {
	
	
	  // GlobalOrg findByOrganizationCode(String globalOrgCode);
	  
	  GlobalOrg findByGlobalOrgCode(String globalOrgCode);
	  
	  // GlobalOrg findByOrganizationName(String globalOrgName);
	  
	  GlobalOrg findByGlobalOrgName(String globalOrgName);
	  
	  // List<GlobalOrg> findByOrganizationCodeNotNull();
	  
	  List<GlobalOrg> findByGlobalOrgCodeNotNull();

	List<GlobalOrg> findByOrderById();

	List<GlobalOrg> findByActive(boolean b);

	List<GlobalOrg> findByActive(boolean b, Sort by);
	 
	

}
