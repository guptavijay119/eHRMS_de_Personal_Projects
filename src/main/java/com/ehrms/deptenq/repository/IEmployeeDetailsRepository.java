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
import com.ehrms.deptenq.models.Gender;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.Service_Group;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.SuspensionDetails;
import com.ehrms.deptenq.models.TypeOfPenaltyInflictedPojo;
import com.ehrms.deptenq.models.User;



@Repository
public interface IEmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long>{

	/* List<EmployeeDetails> findbyCaseDetails_id(Long id); */
//	findByCaseId(S string);
	EmployeeDetails findByEmployeeId(String employeeId);

//	List<EmployeeDetails>  findByCasedetailsId(Long caseid);
	List<EmployeeDetails>  findByCasedetailsId(Long caseNo);

	Page<EmployeeDetails> findByCasedetailsId(Long id, Pageable pageable);

	List<EmployeeDetails> findBySevarthId(String sevarthid);

	List<EmployeeDetails> findBySevarthIdIgnoreCase(String input);

	List<EmployeeDetails> findBySevarthIdLikeIgnoreCase(String input);

	List<EmployeeDetails> findBySevarthIdContaining(String input);

	List<EmployeeDetails> findBySevarthIdContainingIgnoreCase(String input);

	EmployeeDetails findByCasedetails(CaseDetails findByCaseNo);
	
	List<EmployeeDetails> findByCasedetailsCaseNo(String caseno);
	
	boolean existsByCasedetails(CaseDetails casedetails);

	Page<EmployeeDetails> findByCurrentUser(User currentUser, Pageable pageable);

	Page<EmployeeDetails> findByCurrentUserAndCasedetailsId(User currentUser, Long caseNo, Pageable pageable);

	EmployeeDetails findByCasedetailsAndSevarthId(CaseDetails casedetails, String sevarthId);

	boolean existsByCasedetailsAndSevarthId(CaseDetails casedetails, String sevarthId);

	List<EmployeeDetails> findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
			String search, String search2, String search3);

	List<EmployeeDetails> findByCasedetailsCaseNoAndGlobalorg(String string, GlobalOrg globalOrgId);

	List<EmployeeDetails> findByCasedetailsCaseNoAndCurrentUser(String caseid, User currentUser);

	long countByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg s);

	List<EmployeeDetails> findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCase(
			String search, String search2, String search3, String search4);

	List<EmployeeDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNull(
			GlobalOrg s, LocalDate now);

	List<EmployeeDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupAndCasedetailsIsNotNull(GlobalOrg s,
			Service_Group service_Group);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupAndCasedetailsIsNotNull(GlobalOrg s,
			Service_Group service_Group);

	List<EmployeeDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(
			GlobalOrg s, LocalDate now, List<EmployeeDetails> empLists);

	List<EmployeeDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupInAndCasedetailsIsNotNull(GlobalOrg s,
			List<Service_Group> groupList);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupInAndCasedetailsIsNotNull(GlobalOrg s,
			List<Service_Group> groupList);

	List<EmployeeDetails> findDistinctByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupAndCasedetailsIsNotNull(
			GlobalOrg s, Service_Group service_Group);

	List<EmployeeDetails> findDistinctCasedetailsIdByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupAndCasedetailsIsNotNull(
			GlobalOrg s, Service_Group service_Group);


	List<EmployeeDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotInAndCasedetailsIsNotNull(
			GlobalOrg s, List<Service_Group> groupList);

	List<EmployeeDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdIn(
			GlobalOrg s, LocalDate now, Set<Long> caseListId);


	List<EmployeeDetails> findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
			LocalDate now, String search);

	long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg s);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNull(
			GlobalOrg s, LocalDate now);

	long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupAndCasedetailsIsNotNull(GlobalOrg s,
			Service_Group service_Group);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdIn(
			GlobalOrg s, LocalDate now, Set<Long> caseListId);

	long countDistinctCasedetailsByCurrentUserSubDepartment(SubDepartment s);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserSubDepartmentAndSuperannuationDateLessThanAndCasedetailsIsNotNull(
			SubDepartment s, LocalDate now);

	List<EmployeeDetails> findByGenderId(Long valueOf);

	List<EmployeeDetails> findByServicegroupId(Long valueOf);

	List<EmployeeDetails> findBySuperannuationDateLessThan(LocalDate now);

	List<EmployeeDetails> findByGenderIn(List<Gender> findByIdIn);

	List<EmployeeDetails> findByGenderInAndCasedetailsCurrentUserIn(List<Gender> findByIdIn, List<User> userList);

	List<EmployeeDetails> findByGenderIdAndCasedetailsCurrentUserIn(long l, List<User> userList);

	List<EmployeeDetails> findByGenderInAndCasedetailsCurrentUser(List<Gender> findByIdIn, User currentUser);

	List<EmployeeDetails> findByGenderIdAndCasedetailsCurrentUser(long l, User currentUser);
	Page<EmployeeDetails> findByGlobalorg(GlobalOrg globalOrgId, Pageable pageable);

	Page<EmployeeDetails> findBySuperannuationDateLessThan(LocalDate now, Pageable pageable);

	Page<EmployeeDetails> findBySuperannuationDateLessThanAndCasedetailsCurrentUserIn(LocalDate now,
			List<User> userList, Pageable pageable);

	Page<EmployeeDetails> findBySuperannuationDateLessThanAndCasedetailsCurrentUser(LocalDate now, User currentUser,
			Pageable pageable);

	Page<EmployeeDetails> findBySuperannuationDateLessThanAndCasedetailsIn(LocalDate now, List<CaseDetails> caseList,
			Pageable pageable);

	Page<EmployeeDetails> findBySuperannuationDateLessThanAndCasedetailsInAndCasedetailsCurrentUserIn(LocalDate now,
			List<CaseDetails> caseList, List<User> userList, Pageable pageable);

	Page<EmployeeDetails> findBySuperannuationDateLessThanAndCasedetailsInAndCasedetailsCurrentUser(LocalDate now,
			List<CaseDetails> caseList, User currentUser, Pageable pageable);

	List<EmployeeDetails> findByServicegroupIdIn(List<Long> asList);

	List<EmployeeDetails> findByServicegroupIn(List<Service_Group> ggList);

	List<EmployeeDetails> findByServicegroupIdInAndCasedetailsCurrentUserIn(List<Long> asList, List<User> userList);

	List<EmployeeDetails> findByServicegroupInAndCasedetailsCurrentUserIn(List<Service_Group> ggList,
			List<User> userList);

	List<EmployeeDetails> findByServicegroupIdInAndCasedetailsCurrentUser(List<Long> asList, User currentUser);

	List<EmployeeDetails> findByServicegroupInAndCasedetailsCurrentUser(List<Service_Group> ggList, User currentUser);

	EmployeeDetails findByIdAndCurrentUser(Long valueOf, User currentUser);

	EmployeeDetails findByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
			String firstName, String middleName, String lastName);

	List<EmployeeDetails> findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);

	List<EmployeeDetails> findBySuperannuationDateLessThanAndCasedetailsCurrentUserIn(LocalDate now,
			List<User> userList);

	List<EmployeeDetails> findBySuperannuationDateLessThanAndCasedetailsCurrentUser(LocalDate now, User currentUser);

	List<EmployeeDetails> findBySevarthIdIn(List<String> sevarthList2);

	List<EmployeeDetails> findBySevarthIdInAndCurrentUserIn(List<String> sevarthList2, List<User> userList);

	List<EmployeeDetails> findBySevarthIdInAndCurrentUser(List<String> sevarthList2, User currentUser);

	List<EmployeeDetails> findBySevarthIdInAndCurrentUserPimsEmployeeGlobalOrgId(List<String> sevarthList2,
			GlobalOrg s);

	List<EmployeeDetails> findBySevarthIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserIn(
			List<String> sevarthList2, GlobalOrg org, List<User> userList);

	List<EmployeeDetails> findBySevarthIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUser(
			List<String> sevarthList2, GlobalOrg org, User currentUser);

	List<EmployeeDetails> findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgId(List<Long> asList, GlobalOrg s);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecided(boolean b);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIsNotNull(
			boolean b);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdIn(
			boolean b, List<Long> asList);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdIn(
			boolean b, List<Long> asList);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIn(
			boolean b, List<TypeOfPenaltyInflictedPojo> pen);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCurrentUserIn(
			boolean b, List<User> userList);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIsNotNullAndCurrentUserIn(
			boolean b, List<User> userList);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserIn(
			boolean b, List<Long> asList, List<User> userList);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserIn(
			boolean b, List<Long> asList, List<User> userList);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCurrentUserIn(
			boolean b, List<TypeOfPenaltyInflictedPojo> pen, List<User> userList);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCurrentUser(
			boolean b, User currentUser);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIsNotNullAndCurrentUser(
			boolean b, User currentUser);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUser(
			boolean b, List<Long> asList, User currentUser);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUser(
			boolean b, List<Long> asList, User currentUser);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCurrentUser(
			boolean b, List<TypeOfPenaltyInflictedPojo> pen, User currentUser);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCurrentUserPimsEmployeeGlobalOrgIdId(
			boolean b, Long valueOf);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIsNotNullAndCurrentUserPimsEmployeeGlobalOrgIdId(
			boolean b, Long valueOf);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserPimsEmployeeGlobalOrgIdId(
			boolean b, List<Long> asList, Long valueOf);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserPimsEmployeeGlobalOrgIdId(
			boolean b, List<Long> asList, Long valueOf);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCurrentUserPimsEmployeeGlobalOrgIdId(
			boolean b, List<TypeOfPenaltyInflictedPojo> pen, Long valueOf);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgId(
			boolean b, boolean c, GlobalOrg s);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstated(boolean b,
			boolean c);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserIn(
			boolean b, boolean c, GlobalOrg org, List<User> userList);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserIn(
			boolean b, boolean c, List<User> userList);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUser(
			boolean b, boolean c,GlobalOrg org, User currentUser);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUser(
			boolean b, boolean c, User currentUser);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNull(
			boolean b, boolean c, boolean d);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserIn(
			boolean b, boolean c, List<User> userList, boolean d, List<User> userList2);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUser(
			boolean b, boolean c, User currentUser, boolean d, User currentUser2);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgId(
			boolean b, boolean c, GlobalOrg org, boolean d, GlobalOrg org2);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserIn(
			boolean b, boolean c, GlobalOrg org, List<User> userList, boolean d, GlobalOrg org2, List<User> userList2);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUser(
			boolean b, boolean c, GlobalOrg org, User currentUser, boolean d, GlobalOrg org2, User currentUser2);

	List<EmployeeDetails> findByServicegroupInAndCurrentUserPimsEmployeeGlobalOrgId(List<Service_Group> ggList,
			GlobalOrg org);

	List<EmployeeDetails> findByChargesheetDetailsChargesheetIssuedAndChargesheetDetailsChargesheetDateLessThanAndCurrentUserPimsEmployeeGlobalOrgId(
			boolean b, LocalDate minusYears, GlobalOrg s);

	List<EmployeeDetails> findByChargesheetDetailsChargesheetIssuedAndChargesheetDetailsChargesheetDateGreaterThanAndCurrentUserPimsEmployeeGlobalOrgId(
			boolean b, LocalDate minusYears, GlobalOrg s);

	List<EmployeeDetails> findByProsecutionProposalDetailsProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgId(
			boolean b, GlobalOrg s);

	List<EmployeeDetails> findByGenderIdAndServicegroupId(Long valueOf, Long valueOf2);

	List<EmployeeDetails> findByServicegroupIdAndGenderId(Long valueOf, Long valueOf2);

	List<EmployeeDetails> findByGenderInAndCurrentUserPimsEmployeeGlobalOrgIdId(List<Gender> findByIdIn, Long valueOf);

	List<EmployeeDetails> findByGenderIdAndCurrentUserPimsEmployeeGlobalOrgIdId(long l, Long valueOf);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotIn(
			boolean b, boolean c, GlobalOrg s, boolean d, GlobalOrg s2, List<Service_Group> groupList);

//	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserSubDepartment(
//			boolean b, boolean c, SubDepartment s, boolean d, SubDepartment s2);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartment(
			boolean b, boolean c, GlobalOrg s, SubDepartment subDep, boolean d, GlobalOrg s2, SubDepartment subDep2);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndSuperannuationDateLessThanAndCasedetailsIsNotNull(
			GlobalOrg s, SubDepartment subDep, LocalDate now);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserSubDepartmentOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserSubDepartment(
			boolean b, boolean c, SubDepartment s, boolean d, SubDepartment s2);

	Page<EmployeeDetails> findByCurrentUserIn(List<User> userList, Pageable pageable);

	Page<EmployeeDetails> findByCurrentUserInAndCasedetailsId(List<User> userList, Long caseNo, Pageable pageable);

	List<EmployeeDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndServicegroupNotInAndCasedetailsIsNotNull(
			GlobalOrg s, SubDepartment subDep, List<Service_Group> groupList);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndServicegroupNotIn(
			boolean b, boolean c, GlobalOrg s, SubDepartment subDep, boolean d, GlobalOrg s2, SubDepartment subDep2,
			List<Service_Group> groupList);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdIn(
			GlobalOrg s, SubDepartment subDep, LocalDate now, Set<Long> caseListId);

	List<EmployeeDetails> findBySuperannuationDateLessThanAndCurrentUserPimsEmployeeGlobalOrgId(LocalDate now,
			GlobalOrg org);

	Page<EmployeeDetails> findByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg globalOrgId, Pageable pageable);

	Page<EmployeeDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsId(GlobalOrg globalOrgId, Long caseNo,
			Pageable pageable);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserAndSuperannuationDateLessThanAndCasedetailsIsNotNull(
			User s, LocalDate now);

	List<EmployeeDetails> findByCurrentUserAndServicegroupNotInAndCasedetailsIsNotNull(User s,
			List<Service_Group> groupList);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserAndServicegroupNotIn(
			boolean b, boolean c, User s, boolean d, User s2, List<Service_Group> groupList);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdIn(
			User s, LocalDate now, Set<Long> caseListId);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(
			boolean b, boolean c, GlobalOrg s, boolean d, GlobalOrg s2, Set<Long> caseId);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdNotIn(
			GlobalOrg s, LocalDate now, Set<Long> caseId);

	List<EmployeeDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(
			GlobalOrg s, List<Service_Group> groupList, Set<Long> caseId);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotInAndCasedetailsIdNotIn(
			boolean b, boolean c, GlobalOrg s, Set<Long> caseId, boolean d, GlobalOrg s2, List<Service_Group> groupList,
			Set<Long> caseId2);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(
			GlobalOrg s, LocalDate now, Set<Long> caseListId, Set<Long> caseId);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIdNotIn(
			boolean b, boolean c, GlobalOrg s, SubDepartment subDep, Set<Long> caseId, boolean d, GlobalOrg s2,
			SubDepartment subDep2, Set<Long> caseId2);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdNotIn(
			GlobalOrg s, SubDepartment subDep, LocalDate now, Set<Long> caseId);

	List<EmployeeDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(
			GlobalOrg s, SubDepartment subDep, List<Service_Group> groupList, Set<Long> caseId);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndServicegroupNotInAndCasedetailsIdNotIn(
			boolean b, boolean c, GlobalOrg s, SubDepartment subDep, Set<Long> caseId, boolean d, GlobalOrg s2,
			SubDepartment subDep2, List<Service_Group> groupList, Set<Long> caseId2);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(
			GlobalOrg s, SubDepartment subDep, LocalDate now, Set<Long> caseListId, Set<Long> caseId);

	long countDistinctCasedetailsByCurrentUserSubDepartmentAndCasedetailsIdNotIn(SubDepartment s, Set<Long> caseId);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserSubDepartmentAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserSubDepartmentAndCasedetailsIdNotIn(
			boolean b, boolean c, SubDepartment s, Set<Long> caseId, boolean d, SubDepartment s2, Set<Long> caseId2);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserSubDepartmentAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdNotIn(
			SubDepartment s, LocalDate now, Set<Long> caseId);

	List<EmployeeDetails> findByCurrentUserAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(User s,
			List<Service_Group> groupList, Set<Long> caseId);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserAndServicegroupNotInAndCasedetailsIdNotIn(
			boolean b, boolean c, User s, Set<Long> caseId, boolean d, User s2, List<Service_Group> groupList,
			Set<Long> caseId2);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(
			User s, LocalDate now, Set<Long> caseListId, Set<Long> caseId);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhethercasekeptinabeyance(boolean b);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhethercasekeptinabeyanceAndCurrentUser(
			boolean b, User currentUser);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhethercasekeptinabeyanceAndCurrentUserIn(
			boolean b, List<User> userList);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhethercasekeptinabeyanceAndCurrentUserPimsEmployeeGlobalOrgIdId(
			boolean b, GlobalOrg org);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserAndCasedetailsIdNotIn(
			boolean b, boolean c, User s, Set<Long> caseId, boolean d, User s2, Set<Long> caseId2);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdNotIn(
			User s, LocalDate now, Set<Long> caseId);

	List<EmployeeDetails> findByServicegroupIdInAndCasedetailsIdNotIn(List<Long> asList, Set<Long> caseId);

	List<EmployeeDetails> findByServicegroupInAndCasedetailsIdNotIn(List<Service_Group> ggList, Set<Long> caseId);

	List<EmployeeDetails> findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(
			List<Long> asList, GlobalOrg org, Set<Long> caseId);

	List<EmployeeDetails> findByServicegroupInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(
			List<Service_Group> ggList, GlobalOrg org, Set<Long> caseId);

	List<EmployeeDetails> findByServicegroupIdInAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(List<Long> asList,
			List<User> userList, Set<Long> caseId);

	List<EmployeeDetails> findByServicegroupInAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(
			List<Service_Group> ggList, List<User> userList, Set<Long> caseId);

	List<EmployeeDetails> findByServicegroupIdInAndCasedetailsCurrentUserAndCasedetailsIdNotIn(List<Long> asList,
			User currentUser, Set<Long> caseId);

	List<EmployeeDetails> findByServicegroupInAndCasedetailsCurrentUserAndCasedetailsIdNotIn(List<Service_Group> ggList,
			User currentUser, Set<Long> caseId);


	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecided(boolean b);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(
			boolean b, boolean c, GlobalOrg s, Set<Long> caseId, boolean d, GlobalOrg s2, Set<Long> caseId2);

	List<EmployeeDetails> findBySuperannuationDateLessThanAndCasedetailsIdNotIn(LocalDate now, Set<Long> caseId);

	



	

	List<EmployeeDetails> findBySuperannuationDateLessThanAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(
			LocalDate now, GlobalOrg org, Set<Long> caseId);

	List<EmployeeDetails> findBySuperannuationDateLessThanAndCasedetailsCurrentUserAndCasedetailsIdNotIn(LocalDate now,
			User currentUser, Set<Long> caseId);

	List<EmployeeDetails> findBySuperannuationDateLessThanAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(
			LocalDate now, List<User> userList, Set<Long> caseId);

	List<EmployeeDetails> findByGenderInAndCasedetailsIdNotIn(List<Gender> findByIdIn, Set<Long> caseId);

	List<EmployeeDetails> findByGenderIdAndCasedetailsIdNotIn(long l, Set<Long> caseId);

	List<EmployeeDetails> findByGenderInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
			List<Gender> findByIdIn, Long valueOf, Set<Long> caseId);

	List<EmployeeDetails> findByGenderIdAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(long l,
			Long valueOf, Set<Long> caseId);

	List<EmployeeDetails> findByGenderInAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(List<Gender> findByIdIn,
			List<User> userList, Set<Long> caseId);

	List<EmployeeDetails> findByGenderIdAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(long l, List<User> userList,
			Set<Long> caseId);

	List<EmployeeDetails> findByGenderInAndCasedetailsCurrentUserAndCasedetailsIdNotIn(List<Gender> findByIdIn,
			User currentUser, Set<Long> caseId);

	List<EmployeeDetails> findByGenderIdAndCasedetailsCurrentUserAndCasedetailsIdNotIn(long l, User currentUser,
			Set<Long> caseId);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhethercasekeptinabeyanceAndCasedetailsIdNotIn(
			boolean b, Set<Long> caseId);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhethercasekeptinabeyanceAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
			boolean b, GlobalOrg org, Set<Long> caseId);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhethercasekeptinabeyanceAndCurrentUserInAndCasedetailsIdNotIn(
			boolean b, List<User> userList, Set<Long> caseId);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhethercasekeptinabeyanceAndCurrentUserAndCasedetailsIdNotIn(
			boolean b, User currentUser, Set<Long> caseId);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhethercasekeptinabeyanceAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
			boolean b, long org, Set<Long> caseId);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(
			GlobalOrg s, Set<Long> caseListId, Set<Long> caseId, boolean b);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(
			GlobalOrg s, Set<Long> caseId, boolean b);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserAndCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(
			User s, Set<Long> caseId, boolean b);

	List<EmployeeDetails> findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCasedetailsIdNotIn(
			LocalDate now, String customsearch, Set<Long> caseId);

	List<EmployeeDetails> findDistinctCasedetailsByCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(
			Set<Long> caseId, boolean b);

	List<EmployeeDetails> findDistinctCasedetailsByCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceivedAndAndCasedetailsCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCase(
			Set<Long> caseId, boolean b, String customsearch);

	List<EmployeeDetails> findDistinctCasedetailsByCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceivedAndCurrentUserIn(
			Set<Long> caseId, boolean b, List<User> userList);

	List<EmployeeDetails> findDistinctCasedetailsByCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceivedAndCurrentUser(
			Set<Long> caseId, boolean b, User currentUser);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCasedetailsIdNotIn(
			boolean b, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIsNotNullAndCasedetailsIdNotIn(
			boolean b, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCasedetailsIdNotIn(
			boolean b, List<Long> asList, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCasedetailsIdNotIn(
			boolean b, List<Long> asList, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCasedetailsIdNotIn(
			boolean b, List<TypeOfPenaltyInflictedPojo> pen, List<Long> caseIdx);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
			boolean b, Long valueOf, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIsNotNullAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
			boolean b, Long valueOf, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
			boolean b, List<Long> asList, Long valueOf, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
			boolean b, List<Long> asList, Long valueOf, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
			boolean b, List<TypeOfPenaltyInflictedPojo> pen, Long valueOf, List<Long> caseIdx);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCurrentUserInAndCasedetailsIdNotIn(
			boolean b, List<User> userList, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIsNotNullAndCurrentUserInAndCasedetailsIdNotIn(
			boolean b, List<User> userList, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserInAndCasedetailsIdNotIn(
			boolean b, List<Long> asList, List<User> userList, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserInAndCasedetailsIdNotIn(
			boolean b, List<Long> asList, List<User> userList, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCurrentUserInAndCasedetailsIdNotIn(
			boolean b, List<TypeOfPenaltyInflictedPojo> pen, List<User> userList, List<Long> caseIdx);

	List<EmployeeDetails> findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCurrentUserAndCasedetailsIdNotIn(
			boolean b, User currentUser, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIsNotNullAndCurrentUserAndCasedetailsIdNotIn(
			boolean b, User currentUser, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserAndCasedetailsIdNotIn(
			boolean b, List<Long> asList, User currentUser, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserAndCasedetailsIdNotIn(
			boolean b, List<Long> asList, User currentUser, List<Long> caseIdx);

	List<EmployeeDetails> findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCurrentUserAndCasedetailsIdNotIn(
			boolean b, List<TypeOfPenaltyInflictedPojo> pen, User currentUser, List<Long> caseIdx);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(
			boolean b, boolean c, User s, Set<Long> caseId, boolean d, User s2, Set<Long> caseId2, String string);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotInAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(
			boolean b, boolean c, GlobalOrg s, Set<Long> caseId, boolean d, GlobalOrg s2, List<Service_Group> groupList,
			Set<Long> caseId2, String string);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(
			boolean b, boolean c, GlobalOrg s, Set<Long> caseId, boolean d, GlobalOrg s2, Set<Long> caseId2,
			String string);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserSubDepartmentAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserSubDepartmentAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(
			boolean b, boolean c, SubDepartment s, Set<Long> caseId, boolean d, SubDepartment s2, Set<Long> caseId2,
			String string);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserAndServicegroupNotInAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(
			boolean b, boolean c, User s, Set<Long> caseId, boolean d, User s2, List<Service_Group> groupList,
			Set<Long> caseId2, String string);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndSuspensionDetailsCurrentStatus(
			boolean b, boolean c, boolean d, String string);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndSuspensionDetailsCurrentStatus(
			boolean b, boolean c, GlobalOrg org, boolean d, GlobalOrg org2, String string);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserInAndSuspensionDetailsCurrentStatus(
			boolean b, boolean c, List<User> userList, boolean d, List<User> userList2, String string);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserAndSuspensionDetailsCurrentStatus(
			boolean b, boolean c, User currentUser, boolean d, User currentUser2, String string);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserAndSuspensionDetailsCurrentStatus(
			boolean b, boolean c, GlobalOrg org, User currentUser, boolean d, GlobalOrg org2, User currentUser2,
			String string);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInAndSuspensionDetailsCurrentStatus(
			boolean b, boolean c, GlobalOrg org, List<User> userList, boolean d, GlobalOrg org2, List<User> userList2,
			String string);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserInAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserInAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(
			boolean b, boolean c, List<User> s, Set<Long> caseId, boolean d, List<User> s2, Set<Long> caseId2,
			String string);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserInAndCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(
			List<User> s, Set<Long> caseId, boolean b);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserInAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdNotIn(
			List<User> s, LocalDate now, Set<Long> caseId);

	List<EmployeeDetails> findByCurrentUserInAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(
			List<User> s, List<Service_Group> groupList, Set<Long> caseId);

	List<EmployeeDetails> findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserInAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserInAndServicegroupNotInAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(
			boolean b, boolean c, List<User> s, Set<Long> caseId, boolean d, List<User> s2,
			List<Service_Group> groupList, Set<Long> caseId2, String string);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserInAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(
			List<User> s, Set<Long> caseListId, Set<Long> caseId, boolean b);

	List<EmployeeDetails> findDistinctCasedetailsByCurrentUserInAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(
			List<User> s, LocalDate now, Set<Long> caseListId, Set<Long> caseId);

	List<EmployeeDetails> findBySevarthIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenId(
			String input, boolean b, long l);

	List<EmployeeDetails> findBySevarthIdAndFinalOutcomeDetailsWhetherCaseFinallyDecided(String input, boolean b);

	EmployeeDetails findBySevarthIdAndCasedetailsId(String sevarthid, Long valueOf);

	List<EmployeeDetails> findDistinctCasedetailsBySevarthIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenId(
			String input, boolean b, long l);

	List<EmployeeDetails> findDistinctCasedetailsBySevarthIdAndFinalOutcomeDetailsWhetherCaseFinallyDecided(
			String input, boolean b);

	List<EmployeeDetails> findDistinctCasedetailsBySevarthId(String input);

	List<EmployeeDetails> findDistinctCasedetailsBySevarthIdAndFinalOutcomeDetailsIsNullOrSevarthIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdOrSevarthIdAndFinalOutcomeDetailsWhetherCaseFinallyDecided(
			String input, String input2, boolean b, long l, String input3, boolean c);

	List<EmployeeDetails> findByLastNameContainingIgnoreCase(String input);

	List<EmployeeDetails> findByLastNameContainingIgnoreCaseOrderByCasedetailsGlobalorgId(String input);

	List<EmployeeDetails> findDistinctCasedetailsByFirstNameAndMiddleNameAndLastNameAndLocaldateAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenId(
			String firstName, String middleName, String lastName, LocalDate parse, boolean b, long l);

	List<EmployeeDetails> findDistinctCasedetailsByFirstNameAndMiddleNameAndLastNameAndLocaldateAndFinalOutcomeDetailsWhetherCaseFinallyDecided(
			String firstName, String middleName, String lastName, LocalDate parse, boolean b);

	List<EmployeeDetails> findDistinctCasedetailsByFirstNameAndMiddleNameAndLastNameAndLocaldateAndFinalOutcomeDetailsIsNullOrFirstNameAndMiddleNameAndLastNameAndLocaldateAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdOrFirstNameAndMiddleNameAndLastNameAndLocaldateAndFinalOutcomeDetailsWhetherCaseFinallyDecided(
			String firstName, String middleName, String lastName, LocalDate parse, String firstName2,
			String middleName2, String lastName2, LocalDate parse2, boolean b, long l, String firstName3,
			String middleName3, String lastName3, LocalDate parse3, boolean c);

	List<EmployeeDetails> findDistinctCasedetailsByFirstNameAndMiddleNameAndLastNameAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenId(
			String firstName, String middleName, String lastName, LocalDate parse, Long valueOf, boolean b, long l);

	List<EmployeeDetails> findDistinctCasedetailsByFirstNameAndMiddleNameAndLastNameAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsIsNullOrFirstNameAndMiddleNameAndLastNameAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdOrFirstNameAndMiddleNameAndLastNameAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsWhetherCaseFinallyDecided(
			String firstName, String middleName, String lastName, LocalDate parse, Long valueOf, String firstName2,
			String middleName2, String lastName2, LocalDate parse2, Long valueOf2, boolean b, long l, String firstName3,
			String middleName3, String lastName3, LocalDate parse3, Long valueOf3, boolean c);

	List<EmployeeDetails> findDistinctCasedetailsByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenId(
			String firstName, String middleName, String lastName, LocalDate parse, Long valueOf, boolean b, long l);

	List<EmployeeDetails> findDistinctCasedetailsByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndLocaldateAndFinalOutcomeDetailsWhetherCaseFinallyDecided(
			String firstName, String middleName, String lastName, LocalDate parse, boolean b);

	List<EmployeeDetails> findDistinctCasedetailsByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsIsNullOrFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdOrFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsWhetherCaseFinallyDecided(
			String firstName, String middleName, String lastName, LocalDate parse, Long valueOf, String firstName2,
			String middleName2, String lastName2, LocalDate parse2, Long valueOf2, boolean b, long l, String firstName3,
			String middleName3, String lastName3, LocalDate parse3, Long valueOf3, boolean c);

	List<EmployeeDetails> findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrderByCasedetailsGlobalorgId(
			String input,String input1,String input2);

	List<EmployeeDetails> findDistinctCasedetailsByCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgId(
			Set<Long> caseId, boolean b, GlobalOrg org);




	

	
	

}
