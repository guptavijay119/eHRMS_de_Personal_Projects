package com.ehrms.deptenq.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.SuspensionDetails;
import com.ehrms.deptenq.models.User;

@Repository
public interface SuspensionDetailsRepository extends JpaRepository<SuspensionDetails, Long> {

//	          SuspensionDetails findByCasedetailsId(Long caseid);
	          
	         List<SuspensionDetails> findByCasedetailsId(Long caseid);
	          
	         // EmployeeDetails  findByCasedetailsId(Long caseid);
	          
	          Page<SuspensionDetails> findByCasedetailsId(Long id, Pageable pageable);

			SuspensionDetails findByCasedetails(CaseDetails findByCaseNo);
			
			// added 15/09/2022
			boolean existsByCasedetails(CaseDetails casedetails);

			List<SuspensionDetails> findByCasedetailsCaseNo(String string);

			boolean existsByCasedetailsAndSevarthId(CaseDetails cd, String sevarthId);

			SuspensionDetails findByCasedetailsAndSevarthId(CaseDetails cd, String sevarthId);

			List<SuspensionDetails> findByCasedetailsCaseNoAndGlobalorg(String string, GlobalOrg globalOrgId);

			List<SuspensionDetails> findByCasedetailsCaseNoAndCurrentUser(String caseid, User currentUser);

			Page<SuspensionDetails> findByCurrentUser(User currentUser, Pageable pageable);

			Page<SuspensionDetails> findByCasedetailsIdAndCurrentUser(Long caseid, User currentUser, Pageable pageable);

			long countByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspended(GlobalOrg s, boolean b);

//			Page<SuspensionDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSusUnderRuleSusRuleNameContainingIgnoreCaseOrDateOfOrderBetweenOrDateofActualSuspensionBetweenOrDateofDeemedSuspensionBetweenOrDateofExtensionOrderBetween(
//					String search, String search2, String search3, String search4, String search5, String search6,
//					String search7, String search8, String search9, String search10, Pageable pageable);

			Page<SuspensionDetails> findAll(Specification<SuspensionDetails> specification, Pageable pageable);

			Page<SuspensionDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSusUnderRuleSusRuleNameContainingIgnoreCaseOrDateOfOrderOrDateofActualSuspensionOrDateofDeemedSuspensionOrDateofExtensionOrder(
					String search, String search2, String search3, String search4, String search5, String search6,
					String search7, String search8, String search9, String search10, Pageable pageable);

			Page<SuspensionDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSusUnderRuleSusRuleNameContainingIgnoreCaseOrDateOfOrderOrDateofActualSuspensionOrDateofDeemedSuspensionOrDateofExtensionOrder(
					String search, String search2, String search3, String search4, String search5, String search6,
					LocalDate date, LocalDate date2, LocalDate date3, LocalDate date4, Pageable pageable);

			Page<SuspensionDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSusUnderRuleSusRuleNameContainingIgnoreCase(
					String search, String search2, String search3, String search4, String search5, String search6,
					Pageable pageable);

			Page<SuspensionDetails> findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserAndDateOfOrderOrCurrentUserAndDateofActualSuspensionOrCurrentUserAndDateofDeemedSuspensionOrCurrentUserAndDateofExtensionOrder(
					User currentUser, String search, User currentUser2, String search2, User currentUser3,
					String search3, User currentUser4, String search4, User currentUser5, String search5,
					User currentUser6, String search6, User currentUser7, LocalDate date, User currentUser8,
					LocalDate date2, User currentUser9, LocalDate date3, User currentUser10, LocalDate date4,
					Pageable pageable);

			Page<SuspensionDetails> findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndSusUnderRuleSusRuleNameContainingIgnoreCase(
					User currentUser, String search, User currentUser2, String search2, User currentUser3,
					String search3, User currentUser4, String search4, User currentUser5, String search5,
					User currentUser6, String search6, Pageable pageable);

			Page<SuspensionDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSusUnderRuleSusRuleNameContainingIgnoreCaseOrDateOfOrderOrDateofActualSuspensionOrDateofDeemedSuspensionOrDateofExtensionOrderOrEmployeeSuspended(
					String search, String search2, String search3, String search4, String search5, String search6,
					LocalDate date, LocalDate date2, LocalDate date3, LocalDate date4, Boolean booleanObject,
					Pageable pageable);

			Page<SuspensionDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSusUnderRuleSusRuleNameContainingIgnoreCaseOrEmployeeSuspended(
					String search, String search2, String search3, String search4, String search5, String search6,
					Boolean booleanObject, Pageable pageable);

			Page<SuspensionDetails> findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserAndDateOfOrderOrCurrentUserAndDateofActualSuspensionOrCurrentUserAndDateofDeemedSuspensionOrCurrentUserAndDateofExtensionOrderOrCurrentUserAndEmployeeSuspended(
					User currentUser, String search, User currentUser2, String search2, User currentUser3,
					String search3, User currentUser4, String search4, User currentUser5, String search5,
					User currentUser6, String search6, User currentUser7, LocalDate date, User currentUser8,
					LocalDate date2, User currentUser9, LocalDate date3, User currentUser10, LocalDate date4,
					User currentUser11, Boolean booleanObject, Pageable pageable);

			Page<SuspensionDetails> findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserAndEmployeeSuspended(
					User currentUser, String search, User currentUser2, String search2, User currentUser3,
					String search3, User currentUser4, String search4, User currentUser5, String search5,
					User currentUser6, String search6, User currentUser7, Boolean booleanObject, Pageable pageable);

			Page<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
					Boolean booleanObject, String string, Pageable pageable);

			Page<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUser(
					Boolean booleanObject, String string, User currentUser, Pageable pageable);

			long countByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNull(GlobalOrg s,
					boolean b);

			Page<SuspensionDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSusUnderRuleSusRuleNameContainingIgnoreCaseOrDateOfOrderOrDateofActualSuspensionOrDateofDeemedSuspensionOrDateofExtensionOrderOrEmployeeSuspendedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
					String search, String search2, String search3, String search4, String search5, String search6,
					LocalDate date, LocalDate date2, LocalDate date3, LocalDate date4, Boolean booleanObject,
					String search7, String search8, Pageable pageable);

			Page<SuspensionDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSusUnderRuleSusRuleNameContainingIgnoreCaseOrEmployeeSuspendedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
					String search, String search2, String search3, String search4, String search5, String search6,
					Boolean booleanObject, String search7, String search8, Pageable pageable);

			Page<SuspensionDetails> findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserAndEmployeeSuspendedOrCurrentUserAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
					User currentUser, String search, User currentUser2, String search2, User currentUser3,
					String search3, User currentUser4, String search4, User currentUser5, String search5,
					User currentUser6, String search6, User currentUser7, Boolean booleanObject, User currentUser8,
					String search7, User currentUser9, String search8, Pageable pageable);

			Page<SuspensionDetails> findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserAndDateOfOrderOrCurrentUserAndDateofActualSuspensionOrCurrentUserAndDateofDeemedSuspensionOrCurrentUserAndDateofExtensionOrderOrCurrentUserAndEmployeeSuspendedOrCurrentUserAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
					User currentUser, String search, User currentUser2, String search2, User currentUser3,
					String search3, User currentUser4, String search4, User currentUser5, String search5,
					User currentUser6, String search6, User currentUser7, LocalDate date, User currentUser8,
					LocalDate date2, User currentUser9, LocalDate date3, User currentUser10, LocalDate date4,
					User currentUser11, Boolean booleanObject, User currentUser12, String search7, User currentUser13,
					String search8, Pageable pageable);

			Page<SuspensionDetails> findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndSevarthIdContainingIgnoreCaseOrCurrentUserInAndFirstNameContainingIgnoreCaseOrCurrentUserInAndMiddleNameContainingIgnoreCaseOrCurrentUserInAndLastNameContainingIgnoreCaseOrCurrentUserInAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserInAndDateOfOrderOrCurrentUserInAndDateofActualSuspensionOrCurrentUserInAndDateofDeemedSuspensionOrCurrentUserInAndDateofExtensionOrderOrCurrentUserInAndEmployeeSuspendedOrCurrentUserInAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
					List<User> userList, String search, List<User> userList2, String search2, List<User> userList3,
					String search3, List<User> userList4, String search4, List<User> userList5, String search5,
					List<User> userList6, String search6, List<User> userList7, LocalDate date, List<User> userList8,
					LocalDate date2, List<User> userList9, LocalDate date3, List<User> userList10, LocalDate date4,
					List<User> userList11, Boolean booleanObject, List<User> userList12, String search7,
					List<User> userList13, String search8, Pageable pageable);

			Page<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUserIn(
					Boolean booleanObject, String string, List<User> userList, Pageable pageable);

			Page<SuspensionDetails> findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndSevarthIdContainingIgnoreCaseOrCurrentUserInAndFirstNameContainingIgnoreCaseOrCurrentUserInAndMiddleNameContainingIgnoreCaseOrCurrentUserInAndLastNameContainingIgnoreCaseOrCurrentUserInAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserInAndEmployeeSuspendedOrCurrentUserInAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
					List<User> userList, String search, List<User> userList2, String search2, List<User> userList3,
					String search3, List<User> userList4, String search4, List<User> userList5, String search5,
					List<User> userList6, String search6, List<User> userList7, Boolean booleanObject,
					List<User> userList8, String search7, List<User> userList9, String search8, Pageable pageable);

			Page<SuspensionDetails> findByCurrentUserIn(List<User> userList, Pageable pageable);



			Page<SuspensionDetails> findByCurrentUserAndSevarthIdNotInAndFirstNameNotInAndMiddleNameNotInAndLastNameNotIn(
					User currentUser, List<String> sevarthIdList, List<String> firstName, List<String> middleName,
					List<String> lastName, Pageable pageable);

			Page<SuspensionDetails> findByCurrentUserAndFirstNameNotIn(User currentUser, List<String> firstName,
					Pageable pageable);

			long countByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(
					GlobalOrg s, boolean b, List<EmployeeDetails> empLists);

//			long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNull(
//					GlobalOrg s, boolean b);

			long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(
					GlobalOrg s, boolean b, List<EmployeeDetails> empLists);
			Page<SuspensionDetails> findByCurrentUserAndFileNoIsNotNull(User currentUser, Pageable pageable);

			Page<SuspensionDetails> findByFileNoIsNotNull(Pageable pageable);

		

			Page<SuspensionDetails> findByCurrentUserAndFileNoIsNotNullAndFileNoIsNotEmpty(User currentUser,
					Pageable pageable);

			Page<SuspensionDetails> findByFileNoIsNotNullAndFileNoIsNotEmpty(Pageable pageable);

			Page<SuspensionDetails> findByFileNoIsNotNullAndFileNoIsNot(String string, Pageable pageable);

			Page<SuspensionDetails> findByCurrentUserAndFileNoIsNotNullAndFileNoIsNot(User currentUser, String string,
					Pageable pageable);

			long countDistinctCasedetailsByCurrentUserSubDepartmentAndEmployeeSuspendedAndCasedetailsIsNotNull(
					SubDepartment s, boolean b);

			List<SuspensionDetails> findByFileNoContainingIgnoreCase(String input);

			List<SuspensionDetails> findByFileNoContainingIgnoreCaseAndCurrentUser(String input, User currentUser);

			SuspensionDetails findByFileNoAndCurrentUser(String caseid, User currentUser);

			List<SuspensionDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNull(
					GlobalOrg s, boolean b);

			SuspensionDetails findByCasedetailsAndEmpDataId(CaseDetails casedetails, Long valueOf);

			boolean existsByCasedetailsAndEmpDataId(CaseDetails casedetails, Long valueOf);

			List<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsIsNotNull(boolean b);


			List<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsIsNotNullAndSevarthIdNotIn(boolean b,
					List<String> sevarthList);

			List<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsIsNotNullAndSevarthIdNotInAndCurrentUserPimsEmployeeGlobalOrgId(
					boolean b, List<String> sevarthList, GlobalOrg s);

			List<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(boolean b,
					String string);

			Page<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(boolean b,
					String string, Pageable pageable);

			Page<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserIn(
					boolean b, String string, Pageable pageable, List<User> userList);

			Page<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(
					boolean b, String string, Pageable pageable, User currentUser);

			Page<SuspensionDetails> findByFileNoIsNotNullAndFileNoIsNotAndCasedetailsIsNull(String string,
					Pageable pageable);

			Page<SuspensionDetails> findByCurrentUserAndFileNoIsNotNullAndFileNoIsNotAndCasedetailsIsNull(
					User currentUser, String string, Pageable pageable);

			Page<SuspensionDetails> findByFileNoIsNotNullAndFileNoIsNotEmptyAndCasedetailsIsNull(Pageable pageable);

			Page<SuspensionDetails> findByFileNoIsNotNullAndCasedetailsIsNull(Pageable pageable);

			Page<SuspensionDetails> findByCasedetailsIdAndCurrentUserIn(Long caseid, List<User> userList,
					Pageable pageable);

			Page<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgId(
					boolean b, String string, GlobalOrg org, Pageable pageable);

			Page<SuspensionDetails> findByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg globalOrgId, Pageable pageable);

			Page<SuspensionDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsId(GlobalOrg globalOrgId,
					Long caseid, Pageable pageable);

			List<SuspensionDetails> findByFileNoContainingIgnoreCaseAndCurrentUserIn(String input, List<User> userList);

			Page<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(
					boolean b, String string, User currentUser, Pageable pageable);

			List<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(
					boolean b, String string, User s);

			List<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgId(
					boolean b, String string, GlobalOrg s);

			List<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserIn(
					boolean b, String string, List<User> s);

			SuspensionDetails findBySevarthIdAndCasedetailsIsNull(String input);

			List<SuspensionDetails> findBySevarthIdAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNull(
					String input, String string);

			List<SuspensionDetails> findBySevarthIdContainingIgnoreCaseAndCasedetailsIsNull(String input);

			List<SuspensionDetails> findDistinctCasedetailsBySevarthIdAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNull(
					String input, String string);

			List<SuspensionDetails> findDistinctFileNoBySevarthIdAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNull(
					String input, String string);

			List<SuspensionDetails> findByLastNameContainingIgnoreCaseAndCasedetailsIsNull(String input);

	

			List<SuspensionDetails> findDistinctFileNoByFirstNameAndMiddleNameAndLastNameAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNull(
					String firstName, String middleName, String lastName, String string);

			List<SuspensionDetails> findDistinctFileNoByFirstNameAndMiddleNameAndLastNameAndGlobalorgIdAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNull(
					String firstName, String middleName, String lastName, Long valueOf, String string);

			Page<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndGlobalorgActive(
					boolean b, String string, boolean c, Pageable pageable);

			List<SuspensionDetails> findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseAndCasedetailsIsNull(
					String input,String input1,String input2);

			List<SuspensionDetails> findDistinctFileNoByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndGlobalorgIdAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNull(
					String firstName, String middleName, String lastName, Long valueOf, String string);

			List<SuspensionDetails> findByLastNameContainingIgnoreCaseAndCasedetailsIsNullOrFirstNameContainingIgnoreCaseAndCasedetailsIsNullOrMiddleNameContainingIgnoreCaseAndCasedetailsIsNull(
					String input, String input2, String input3);

			Page<SuspensionDetails> findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndGlobalorgActiveAndCurrentUserPimsEmployeeGlobalOrgId(
					boolean b, String string, boolean c, Pageable pageable, GlobalOrg org);

		
	       
				/*
				 * List<EmployeeDetails> findByCasedetailsId(Long caseid); EmployeeDetails
				 * findByCasedetailsId(Long caseid);
				 */
	          
				/*
				 * EmployeeDetails findByEmployeeId(String employeeId);
				 * 
				 * // List<EmployeeDetails> findByCasedetailsId(Long caseid); EmployeeDetails
				 * findByCasedetailsId(Long caseid);
				 * 
				 * Page<EmployeeDetails> findByCasedetailsId(Long id, Pageable pageable);
				 */


	
}
