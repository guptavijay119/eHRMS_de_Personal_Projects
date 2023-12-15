package com.ehrms.deptenq.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.ChargesheetDetails;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;



@Repository
public interface IChargesheetDetailsRepository extends JpaRepository<ChargesheetDetails, Long>{

	List<ChargesheetDetails> findByCaseDetails(CaseDetails orElse);

	List<ChargesheetDetails> findByCaseDetailsId(Long caseid);

	List<ChargesheetDetails> findByCaseDetailsCaseNo(String caseNo);
	
	// added 15/09/2022
	boolean existsByCaseDetails(CaseDetails casedetails);
	
	//ChargesheetDetails  findByCasedetailsId(Long caseid);
	
//	ChargesheetDetails findByCasedetails(CaseDetails findByCaseNo);

	List<ChargesheetDetails> findByCaseDetailsId(CaseDetails findByCaseNo);

	Page<ChargesheetDetails>  findByCaseDetailsId(Long id, Pageable pageable);

	Page<ChargesheetDetails> findByCaseDetailsCaseNo(String caseNo, Pageable pageable);


	boolean existsByCaseDetailsAndSevarthId(CaseDetails cd, String sevarthId);

	ChargesheetDetails findByCaseDetailsAndSevarthId(CaseDetails cd, String sevarthId);

	Page<ChargesheetDetails> findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(LocalDate parse,
			LocalDate parse2, Pageable pageable);

	List<ChargesheetDetails> findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(LocalDate parse,
			LocalDate parse2);

	Page<ChargesheetDetails> findByCurrentUser(User currentUser, Pageable pageable);

	Page<ChargesheetDetails> findByCaseDetailsIdAndCurrentUser(Long caseNo, User currentUser, Pageable pageable);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThan(GlobalOrg s, boolean b,
			LocalDate minusYears);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqual(GlobalOrg s,
			boolean b, LocalDate minusYears);

	List<ChargesheetDetails> findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqualAndCurrentUser(
			LocalDate parse, LocalDate parse2, User currentUser);

	List<ChargesheetDetails> findByChargesheetDateLessThan(LocalDate minusYears);

	List<ChargesheetDetails> findByChargesheetDateGreaterThanEqual(LocalDate minusYears);

	List<ChargesheetDetails> findByChargesheetDateGreaterThanEqualAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
			LocalDate minusYears, boolean b, String customsearch);

	List<ChargesheetDetails> findByChargesheetDateLessThanAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
			LocalDate minusYears, boolean b, String customsearch);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(
			GlobalOrg s, boolean b, LocalDate minusYears);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(
			GlobalOrg s, boolean b, LocalDate minusYears);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsEmployeeListIn(
			GlobalOrg s, boolean b, LocalDate minusYears, List<EmployeeDetails> empLists);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsEmployeeListIn(
			GlobalOrg s, boolean b, LocalDate minusYears, List<EmployeeDetails> empLists);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(
			GlobalOrg s, boolean b, LocalDate minusYears, Set<Long> caseListId);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(
			GlobalOrg s, boolean b, LocalDate minusYears, Set<Long> caseListId);

	long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(
			GlobalOrg s, boolean b, LocalDate minusYears);

	long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(
			GlobalOrg s, boolean b, LocalDate minusYears);

	long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(
			GlobalOrg s, boolean b, LocalDate minusYears, Set<Long> caseListId);

	long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(
			GlobalOrg s, boolean b, LocalDate minusYears, Set<Long> caseListId);

	long countDistinctCasedetailsByCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(
			SubDepartment s, boolean b, LocalDate minusYears);

	long countDistinctCasedetailsByCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(
			SubDepartment s, boolean b, LocalDate minusYears);

	List<ChargesheetDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(
			GlobalOrg s, boolean b, LocalDate minusYears);

	List<ChargesheetDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(
			GlobalOrg s, boolean b, LocalDate minusYears);

	List<ChargesheetDetails> findByChargesheetDateGreaterThanEqualAndChargesheetIssued(LocalDate minusYears, boolean b);

	List<ChargesheetDetails> findByChargesheetDateLessThanAndChargesheetIssued(LocalDate minusYears, boolean b);

	List<ChargesheetDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(
			GlobalOrg s, boolean b, LocalDate minusYears, Set<Long> caseListId);

	List<ChargesheetDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(
			GlobalOrg s, boolean b, LocalDate minusYears, Set<Long> caseListId);

	List<ChargesheetDetails> findDistinctCasedetailsByCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(
			SubDepartment s, boolean b, LocalDate minusYears);

	List<ChargesheetDetails> findDistinctCasedetailsByCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(
			SubDepartment s, boolean b, LocalDate minusYears);

	List<ChargesheetDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(
			GlobalOrg s, boolean b, LocalDate minusYears);

	List<ChargesheetDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(
			GlobalOrg s, boolean b, LocalDate minusYears);

	boolean existsByCaseDetailsAndEmpDataId(CaseDetails cd, Long employeeId);

	ChargesheetDetails findByCaseDetailsAndEmpDataId(CaseDetails cd, Long employeeId);

	List<ChargesheetDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(
			GlobalOrg s, SubDepartment subDep, boolean b, LocalDate minusYears);

	List<ChargesheetDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(
			GlobalOrg s, SubDepartment subDep, boolean b, LocalDate minusYears);

	Page<ChargesheetDetails> findByCurrentUserIn(List<User> userList, Pageable pageable);

	Page<ChargesheetDetails> findByCaseDetailsIdAndCurrentUserIn(Long caseNo, List<User> userList, Pageable pageable);

	List<ChargesheetDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(
			GlobalOrg s, SubDepartment subDep, boolean b, LocalDate minusYears, Set<Long> caseListId);

	List<ChargesheetDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(
			GlobalOrg s, SubDepartment subDep, boolean b, LocalDate minusYears, Set<Long> caseListId);

	Page<ChargesheetDetails> findByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg globalOrgId, Pageable pageable);

	Page<ChargesheetDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCaseDetailsId(GlobalOrg globalOrgId,
			Long caseNo, Pageable pageable);

	List<ChargesheetDetails> findByCurrentUserAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(
			User s, boolean b, LocalDate minusYears);

	List<ChargesheetDetails> findByCurrentUserAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(
			User s, boolean b, LocalDate minusYears);

	List<ChargesheetDetails> findDistinctCasedetailsByCurrentUserAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(
			User s, boolean b, LocalDate minusYears, Set<Long> caseListId);

	List<ChargesheetDetails> findDistinctCasedetailsByCurrentUserAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(
			User s, boolean b, LocalDate minusYears, Set<Long> caseListId);

	List<ChargesheetDetails> findByCurrentUserInAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(
			List<User> s, boolean b, LocalDate minusYears);

	List<ChargesheetDetails> findByCurrentUserInAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(
			List<User> s, boolean b, LocalDate minusYears);

	List<ChargesheetDetails> findDistinctCasedetailsByCurrentUserInAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(
			List<User> s, boolean b, LocalDate minusYears, Set<Long> caseListId);

	List<ChargesheetDetails> findDistinctCasedetailsByCurrentUserInAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(
			List<User> s, boolean b, LocalDate minusYears, Set<Long> caseListId);
	


}
