package com.ehrms.deptenq.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.ProsecutionProposalDetails;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;

@Repository
public interface IProsecutionProposalDetailsRepository extends JpaRepository<ProsecutionProposalDetails, Long> {

	List<ProsecutionProposalDetails> findByCasedetailsId(Long caseid);

	// Page<CaseDetails> findAll(Pageable pageable);
	
	
	
	// added  as per GAD requirements
    Page<ProsecutionProposalDetails> findByCasedetailsId(Long id, Pageable pageable);
    
 //   ProsecutionProposalDetails findByEmployeeId(String employeeId);

   // ProsecutionProposalDetails  findBySevarthId(String sevarthid);

	//List<ProsecutionProposalDetails> findBySevarthIdIgnoreCase(String input);

//	List<ProsecutionProposalDetails> findBySevarthIdLikeIgnoreCase(String input);

	ProsecutionProposalDetails findByCasedetails(CaseDetails casedetails);

	boolean existsByCasedetails(CaseDetails casedetails);

	Page<ProsecutionProposalDetails> findByCurrentUser(User currentUser, Pageable pageable);

	Page<ProsecutionProposalDetails> findByCasedetailsIdAndCurrentUser(Long caseid, User currentUser,
			Pageable pageable);

	List<ProsecutionProposalDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNull(GlobalOrg s);

	List<ProsecutionProposalDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(
			GlobalOrg s, List<EmployeeDetails> empLists);

	List<ProsecutionProposalDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdIn(
			GlobalOrg s, Set<Long> caseListId);

	Page<ProsecutionProposalDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrProsecutionProposalReceivedOrDateOfReceiptOrStatusForGrantedDeniedPendingContainingIgnoreCaseOrDateOfOrderReceiptOrDateOfCommunication(
			String search, Boolean booleanObject, LocalDate date, String search2, LocalDate date2, LocalDate date3,
			Pageable pageable);

	Page<ProsecutionProposalDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrProsecutionProposalReceivedOrStatusForGrantedDeniedPendingContainingIgnoreCase(
			String search, Boolean booleanObject, String search2, Pageable pageable);

	Page<ProsecutionProposalDetails> findByCurrentUserIn(List<User> userList, Pageable pageable);

	Page<ProsecutionProposalDetails> findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndProsecutionProposalReceivedOrCurrentUserInAndDateOfReceiptOrCurrentUserInAndStatusForGrantedDeniedPendingContainingIgnoreCaseOrCurrentUserInAndDateOfOrderReceiptOrCurrentUserInAndDateOfCommunication(
			List<User> userList, String search, List<User> userList2, Boolean booleanObject, List<User> userList3,
			LocalDate date, List<User> userList4, String search2, List<User> userList5, LocalDate date2,
			List<User> userList6, LocalDate date3, Pageable pageable);

	Page<ProsecutionProposalDetails> findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndProsecutionProposalReceivedOrCurrentUserInAndStatusForGrantedDeniedPendingContainingIgnoreCase(
			List<User> userList, String search, List<User> userList2, Boolean booleanObject, List<User> userList3,
			String search2, Pageable pageable);

	Page<ProsecutionProposalDetails> findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndProsecutionProposalReceivedOrCurrentUserAndDateOfReceiptOrCurrentUserAndStatusForGrantedDeniedPendingContainingIgnoreCaseOrCurrentUserAndDateOfOrderReceiptOrCurrentUserAndDateOfCommunication(
			User currentUser, String search, User currentUser2, Boolean booleanObject, User currentUser3,
			LocalDate date, User currentUser4, String search2, User currentUser5, LocalDate date2, User currentUser6,
			LocalDate date3, Pageable pageable);

	Page<ProsecutionProposalDetails> findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndProsecutionProposalReceivedOrCurrentUserAndStatusForGrantedDeniedPendingContainingIgnoreCase(
			User currentUser, String search, User currentUser2, Boolean booleanObject, User currentUser3,
			String search2, Pageable pageable);

	Page<ProsecutionProposalDetails> findByProsecutionProposalReceivedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
			Boolean booleanObject, String string, Pageable pageable);

	Page<ProsecutionProposalDetails> findByProsecutionProposalReceivedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUserIn(
			Boolean booleanObject, String string, List<User> userList, Pageable pageable);

	Page<ProsecutionProposalDetails> findByProsecutionProposalReceivedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUser(
			Boolean booleanObject, String string, User currentUser, Pageable pageable);


	List<ProsecutionProposalDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNull(
			GlobalOrg s);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdIn(
			GlobalOrg s, Set<Long> caseListId);

	Page<ProsecutionProposalDetails> findByFileNoIsNotNullAndFileNoIsNot(String string, Pageable pageable);

	Page<ProsecutionProposalDetails> findByCurrentUserAndFileNoIsNotNullAndFileNoIsNot(User currentUser, String string,
			Pageable pageable);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByCurrentUserSubDepartmentAndCasedetailsIsNotNull(
			SubDepartment s);



	Page<ProsecutionProposalDetails> findByFileNoIsNotNull(Pageable pageable);

	ProsecutionProposalDetails findByFileNo(String fileNo);

	ProsecutionProposalDetails findByCasedetailsAndSevarthId(CaseDetails cd, String sevarthId);

	boolean existsByCasedetailsAndSevarthId(CaseDetails cd, String sevarthId);

	boolean existsByCasedetailsAndEmpDataId(CaseDetails casedetails, Long valueOf);

	ProsecutionProposalDetails findByCasedetailsAndEmpDataId(CaseDetails casedetails, Long valueOf);

	Page<ProsecutionProposalDetails> findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(
			boolean b, String string, Pageable pageable);

	Page<ProsecutionProposalDetails> findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserIn(
			boolean b, String string, Pageable pageable, List<User> userList);

	Page<ProsecutionProposalDetails> findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(
			boolean b, String string, Pageable pageable, User currentUser);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNull(
			boolean b, GlobalOrg s);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdIn(
			boolean b, GlobalOrg s, Set<Long> caseListId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserSubDepartmentAndCasedetailsIsNotNull(
			boolean b, SubDepartment s);



	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNull(
			boolean b, GlobalOrg s);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsIsNotNull(
			boolean b);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsIsNotNullAndCurrentUserIn(
			boolean b, List<User> userList);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsIsNotNullAndCurrentUser(
			boolean b, User currentUser);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsIsNotNullAndCasedetailsCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCase(
			boolean b, String customsearch);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsIsNotNullAndCurrentUserPimsEmployeeGlobalOrgId(
			boolean b, GlobalOrg globalOrgId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIsNotNull(
			boolean b, GlobalOrg s, SubDepartment subDep);

	Page<ProsecutionProposalDetails> findByCasedetailsIdAndCurrentUserIn(Long caseid, List<User> userList,
			Pageable pageable);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIsNotNullAndCasedetailsIdIn(
			boolean b, GlobalOrg s, SubDepartment subDep, Set<Long> caseListId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIsNotNullAndCasedetailsIdIn(
			GlobalOrg s, SubDepartment subDep, Set<Long> caseListId);

	Page<ProsecutionProposalDetails> findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgId(
			boolean b, String string, GlobalOrg org, Pageable pageable);

	Page<ProsecutionProposalDetails> findByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg globalOrgId, Pageable pageable);

	Page<ProsecutionProposalDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsId(GlobalOrg globalOrgId,
			Long caseid, Pageable pageable);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsCurrentUserAndCasedetailsIsNotNull(
			boolean b, User s);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserAndCasedetailsIsNotNullAndCasedetailsIdIn(
			boolean b, User s, Set<Long> caseListId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByCurrentUserAndCasedetailsIsNotNullAndCasedetailsIdIn(
			User s, Set<Long> caseListId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdNotIn(
			boolean b, GlobalOrg s, Set<Long> caseId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(
			boolean b, GlobalOrg s, Set<Long> caseListId, Set<Long> caseId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(
			GlobalOrg s, Set<Long> caseListId, Set<Long> caseId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIsNotNullAndCasedetailsIdNotIn(
			boolean b, GlobalOrg s, SubDepartment subDep, Set<Long> caseId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(
			boolean b, GlobalOrg s, SubDepartment subDep, Set<Long> caseListId, Set<Long> caseId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(
			GlobalOrg s, SubDepartment subDep, Set<Long> caseListId, Set<Long> caseId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserSubDepartmentAndCasedetailsIsNotNullAndCasedetailsIdNotIn(
			boolean b, SubDepartment s, Set<Long> caseId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(
			boolean b, User s, Set<Long> caseListId, Set<Long> caseId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByCurrentUserAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(
			User s, Set<Long> caseListId, Set<Long> caseId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsIsNotNullAndCasedetailsIdNotIn(
			boolean b, Set<Long> caseId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsIsNotNullAndCasedetailsCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCaseAndCasedetailsIdNotIn(
			boolean b, String customsearch, Set<Long> caseId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsCurrentUserAndCasedetailsIsNotNullAndCasedetailsIdNotIn(
			boolean b, User s, Set<Long> caseId);

	List<ProsecutionProposalDetails> findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsIsNotNullAndCurrentUserInAndCasedetailsIdNotIn(
			boolean b, List<User> userList, Set<Long> caseId);

	List<ProsecutionProposalDetails> findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(
			boolean b, String string);

	List<ProsecutionProposalDetails> findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(
			boolean b, String string, User s);

	List<ProsecutionProposalDetails> findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgId(
			boolean b, String string, GlobalOrg s);

	List<ProsecutionProposalDetails> findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserIn(
			boolean b, String string, List<User> s);

	Page<ProsecutionProposalDetails> findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgIdActive(
			boolean b, String string, boolean c, Pageable pageable);

	List<ProsecutionProposalDetails> findByLastNameContainingIgnoreCaseAndCasedetailsIsNullOrFirstNameContainingIgnoreCaseAndCasedetailsIsNullOrMiddleNameContainingIgnoreCaseAndCasedetailsIsNull(
			String input, String input2, String input3);

	List<ProsecutionProposalDetails> findDistinctFileNoByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndGlobalorgIdAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNull(
			String firstName, String middleName, String lastName, Long valueOf, String string);

	List<ProsecutionProposalDetails> findBySevarthIdContainingIgnoreCaseAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNull(
			String input, String string, boolean b);

	List<ProsecutionProposalDetails> findByLastNameContainingIgnoreCaseAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNullOrFirstNameContainingIgnoreCaseAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNullOrMiddleNameContainingIgnoreCaseAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNull(
			String input, String string, boolean b, String input2, String string2, boolean c, String input3,
			String string3, boolean d);

	List<ProsecutionProposalDetails> findDistinctFileNoByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndGlobalorgIdAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNull(
			String firstName, String middleName, String lastName, Long valueOf, String string, String string2,
			boolean b);

	List<ProsecutionProposalDetails> findBySevarthIdContainingIgnoreCaseAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledIntheCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNull(
			String input, String string, boolean b);

	List<ProsecutionProposalDetails> findDistinctFileNoByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndGlobalorgIdAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledIntheCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNull(
			String firstName, String middleName, String lastName, Long valueOf, String string, String string2,
			boolean b);

	List<ProsecutionProposalDetails> findByLastNameContainingIgnoreCaseAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledIntheCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNullOrFirstNameContainingIgnoreCaseAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledIntheCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNullOrMiddleNameContainingIgnoreCaseAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledIntheCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNull(
			String input, String string, boolean b, String input2, String string2, boolean c, String input3,
			String string3, boolean d);

	Page<ProsecutionProposalDetails> findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgIdActiveAndCurrentUserPimsEmployeeGlobalOrgId(
			boolean b, String string, boolean c, Pageable pageable, GlobalOrg org);


}
