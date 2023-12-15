package com.ehrms.deptenq.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.InquiryOfficerDetails;
import com.ehrms.deptenq.models.PresentingOfficerDetails;
import com.ehrms.deptenq.models.User;

@Repository
public interface IPresentingOfficerRepository extends JpaRepository<PresentingOfficerDetails, Long>{

	PresentingOfficerDetails findByCasedetailsId(Long caseid);
	
	//Page<CaseDetails>  findAll(Pageable pageable);
	
//InquiryOfficerDetails findByCasedetailsId(Long caseid);
	
	
	
	
	// added  as per GAD requirements
    Page<PresentingOfficerDetails> findByCasedetailsId(Long id, Pageable pageable);
    
    PresentingOfficerDetails findByEmployeeId(String employeeId);

    PresentingOfficerDetails  findBySevarthId(String sevarthid);

	List<PresentingOfficerDetails> findBySevarthIdIgnoreCase(String input);

	List<PresentingOfficerDetails> findBySevarthIdLikeIgnoreCase(String input);

	PresentingOfficerDetails findByCasedetails(CaseDetails casedetails);

	boolean existsByCasedetails(CaseDetails casedetails);

	PresentingOfficerDetails findByCasedetailsAndSevarthId(CaseDetails cd, String sevarthId);

	boolean existsByCasedetailsAndSevarthId(CaseDetails cd, String sevarthId);

	List<PresentingOfficerDetails> findByRevenueDivisionDivisionNameContainingIgnoreCase(String search);

	Page<PresentingOfficerDetails> findByCurrentUser(User currentUser, Pageable pageable);

	Page<PresentingOfficerDetails> findByCasedetailsIdAndCurrentUser(Long caseid, User currentUser, Pageable pageable);

	Page<PresentingOfficerDetails> findByCurrentUserIn(List<User> userList, Pageable pageable);

	Page<PresentingOfficerDetails> findByCasedetailsIdAndCurrentUserIn(Long caseid, List<User> userList,
			Pageable pageable);

	Page<PresentingOfficerDetails> findByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg globalOrgId, Pageable pageable);



	Page<PresentingOfficerDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsId(GlobalOrg globalOrgId,
			Long caseid, Pageable pageable);
	
	

}
