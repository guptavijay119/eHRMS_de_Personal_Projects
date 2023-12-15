package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.Division;


@Repository
public interface IDivisionRepository extends JpaRepository<Division, Long> {

	List<Division> findByOrderById();

	List<Division> findByActive(boolean b);

	List<Division> findByActive(boolean b, Sort by);
	
	//List<InquiryOfficerList> findByOrderById();
	
	  // GlobalOrg findByOrganizationCode(String globalOrgCode);
	  
	 // GlobalOrg findByGlobalOrgCode(String globalOrgCode);
	  
	  // GlobalOrg findByOrganizationName(String globalOrgName);
	  
	  //GlobalOrg findByGlobalOrgName(String globalOrgName);
	  
	  // List<GlobalOrg> findByOrganizationCodeNotNull();
	  
	//  List<GlobalOrg> findByGlobalOrgCodeNotNull();
	 
	

}
