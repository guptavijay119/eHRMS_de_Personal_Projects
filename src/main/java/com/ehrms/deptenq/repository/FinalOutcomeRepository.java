package com.ehrms.deptenq.repository;


import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.FinalOutcomeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.TypeOfPenaltyInflictedPojo;
import com.ehrms.deptenq.models.User;

@Repository
public interface FinalOutcomeRepository extends JpaRepository<FinalOutcomeDetails,Long>{

	List<FinalOutcomeDetails> findByCasedetails(CaseDetails findByCaseNo);

//	InquiryOfficerDetails findByCasedetailsId(Long caseid);
	
	//Page<CaseDetails>  findAll(Pageable pageable);
	
	// added  as per GAD requirements
    Page<FinalOutcomeDetails> findByCasedetailsId(Long id, Pageable pageable);
    
   // FinalOutcomeDetails findByEmployeeId(String employeeId);

   // FinalOutcomeDetails  findBySevarthId(String sevarthid);

	//List<FinalOutcomeDetails> findBySevarthIdIgnoreCase(String input);

	//List<FinalOutcomeDetails> findBySevarthIdLikeIgnoreCase(String input);

	//FinalOutcomeDetails findByCasedetails(CaseDetails casedetails);

	boolean existsByCasedetails(CaseDetails casedetails);

	List<FinalOutcomeDetails> findByCasedetailsId(Long caseid);


	FinalOutcomeDetails findByCasedetailsAndSevarthId(CaseDetails cd, String sevarthId);

	Page<FinalOutcomeDetails> findByCurrentUser(User currentUser, Pageable pageable);

	Page<FinalOutcomeDetails> findByCasedetailsIdAndCurrentUser(Long caseid, User currentUser, Pageable pageable);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndWhetherCaseFinallyDecided(GlobalOrg s, boolean b);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndWhetherCaseFinallyDecidedAndCasedetailsIsNotNull(GlobalOrg s,
			boolean b);

	List<FinalOutcomeDetails> findByWhetherCaseFinallyDecided(boolean b);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndWhetherCaseFinallyDecidedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(
			GlobalOrg s, boolean b, List<EmployeeDetails> empLists);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndWhetherCaseFinallyDecidedAndCasedetailsIsNotNullAndCasedetailsIdIn(
			GlobalOrg s, boolean b, Set<Long> caseListId);

	long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndWhetherCaseFinallyDecidedAndCasedetailsIsNotNull(
			GlobalOrg s, boolean b);

	long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndWhetherCaseFinallyDecidedAndCasedetailsIsNotNullAndCasedetailsIdIn(
			GlobalOrg s, boolean b, Set<Long> caseListId);

	long countDistinctCasedetailsByCurrentUserSubDepartmentAndWhetherCaseFinallyDecidedAndCasedetailsIsNotNull(
			SubDepartment s, boolean b);

	Page<FinalOutcomeDetails> findByCurrentUserAndFileNoIsNotNull(User currentUser, Pageable pageable);

	Page<FinalOutcomeDetails> findByFileNoIsNotNullAndFileNoIsNot(String string, Pageable pageable);

	List<FinalOutcomeDetails> findByCasedetailsRuleApplicableIdNotInAndWhetherCaseFinallyDecidedAndTypeOfPenaltyInflictedIdIn(
			List<Long> asList, boolean b, List<Long> asList2);

	boolean existsByCasedetailsAndSevarthId(CaseDetails cd, String sevarthId);

	List<FinalOutcomeDetails> findByWhetherCaseFinallyDecidedAndTypeOfPenaltyInflictedIsNotNull(boolean b);

	List<FinalOutcomeDetails> findByWhetherCaseFinallyDecidedAndTypeOfPenaltyInflictedIdIn(boolean b,
			List<Long> asList);

	List<FinalOutcomeDetails> findByWhetherCaseFinallyDecidedAndDecisionTakenIdIn(boolean b, List<Long> asList);

	List<FinalOutcomeDetails> findByWhetherCaseFinallyDecidedAndTypeOfPenaltyInflictedIn(boolean b,
			List<TypeOfPenaltyInflictedPojo> pen);

	boolean existsByCasedetailsAndEmpDataId(CaseDetails casedetails, Long valueOf);

	FinalOutcomeDetails findByCasedetailsAndEmpDataId(CaseDetails casedetails, Long valueOf);

	Page<FinalOutcomeDetails> findByCurrentUserIn(List<User> userList, Pageable pageable);

	Page<FinalOutcomeDetails> findByCasedetailsIdAndCurrentUserIn(Long caseid, List<User> userList, Pageable pageable);

	

	List<FinalOutcomeDetails> findByCasedetailsRuleApplicableIdNotInAndWhetherCaseFinallyDecidedAndTypeOfPenaltyInflictedIdInAndCurrentUserPimsEmployeeGlobalOrgId(
			List<Long> asList, boolean b, List<Long> asList2, GlobalOrg org);

	Page<FinalOutcomeDetails> findByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg globalOrgId, Pageable pageable);

	Page<FinalOutcomeDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsId(GlobalOrg globalOrgId,
			Long caseid, Pageable pageable);

	List<FinalOutcomeDetails> findByWhetherCaseFinallyDecidedAndCasedetailsIsNotNull(boolean b);

	List<FinalOutcomeDetails> findByCasedetailsIsNotNullAndWhethercasekeptinabeyance(boolean b);


	

}
