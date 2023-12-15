package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.Division;
import com.ehrms.deptenq.models.InquiryOfficerDetails;
import com.ehrms.deptenq.models.InquiryOfficerList;


@Repository
public interface InquiryOfficerListRepository extends JpaRepository<InquiryOfficerList, Long> {
	
	List<InquiryOfficerList> findByOrderById();

	List<InquiryOfficerList> findByIdIn(List<Long> asList);

	List<InquiryOfficerList> findByDesignationIn(List<String> asList);

	List<InquiryOfficerList> findByDesignationContainingIgnoreCase(String caseid);


	List<InquiryOfficerList> findByInquiryOfficerlistAndActive(String string, boolean b);

	List<InquiryOfficerList> findByInquiryOfficerlistNotInAndActive(List<String> asList, boolean b);

	List<InquiryOfficerList> findByInquiryOfficerlistNotInAndActive(List<String> asList, boolean b, Sort by);

	List<InquiryOfficerList> findByInquiryOfficerlistAndActive(String string, boolean b, Sort by);

	InquiryOfficerList findByDivision(Division division);

	List<InquiryOfficerList> findByActive(boolean b, Sort by);


	
	  // GlobalOrg findByOrganizationCode(String globalOrgCode);
	  
	 // GlobalOrg findByGlobalOrgCode(String globalOrgCode);
	  
	  // GlobalOrg findByOrganizationName(String globalOrgName);
	  
	  //GlobalOrg findByGlobalOrgName(String globalOrgName);
	  
	  // List<GlobalOrg> findByOrganizationCodeNotNull();
	  
	//  List<GlobalOrg> findByGlobalOrgCodeNotNull();
	 
	

}
