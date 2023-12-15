package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.CourtCaseDetails;
import com.ehrms.deptenq.models.ProsecutionProposalDetails;
import com.ehrms.deptenq.models.User;

@Repository
public interface ICourtCaseDetailsRepository extends JpaRepository<CourtCaseDetails, Long> {

	CourtCaseDetails findByCasedetails(CaseDetails findByCaseNo);
	
	// added  as per GAD requirements
    Page<CourtCaseDetails> findByCasedetailsId(Long id, Pageable pageable);
    
 //   CourtCaseDetails findByEmployeeId(String employeeId);

   // CourtCaseDetails  findBySevarthId(String sevarthid);

	//List<CourtCaseDetails> findBySevarthIdIgnoreCase(String input);

//	List<CourtCaseDetails> findBySevarthIdLikeIgnoreCase(String input);

	//CourtCaseDetails findByCasedetails(CaseDetails casedetails);

	boolean existsByCasedetails(CaseDetails casedetails);

	List<CourtCaseDetails> findByCasedetailsId(Long caseid);

	List<CourtCaseDetails> findByCasedetailsCaseNo(String string);

	boolean existsByCasedetailsAndSevarthId(CaseDetails cd, String sevarthId);

	CourtCaseDetails findByCasedetailsAndSevarthId(CaseDetails cd, String sevarthId);

	List<CourtCaseDetails> findByCasedetailsCaseNoAndCurrentUser(String string, User currentUser);

	Page<CourtCaseDetails> findByCurrentUser(User currentUser, Pageable pageable);

	Page<CourtCaseDetails> findByCasedetailsIdAndCurrentUser(Long caseid, User currentUser, Pageable pageable);

	boolean existsByCasedetailsAndEmpDataId(CaseDetails casedetails, Long valueOf);

	CourtCaseDetails findByCasedetailsAndEmpDataId(CaseDetails casedetails, Long valueOf);


	
	

}
