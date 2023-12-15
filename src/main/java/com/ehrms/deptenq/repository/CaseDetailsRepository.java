package com.ehrms.deptenq.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.ChargesheetDetails;
import com.ehrms.deptenq.models.DetailsKeptAbeyanceCases;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.FinalOutcomeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.InquiryOfficerDetails;
import com.ehrms.deptenq.models.MisconductTypesMaster;
import com.ehrms.deptenq.models.PresentingOfficerDetails;
import com.ehrms.deptenq.models.ProsecutionProposalDetails;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;

@Repository
public interface CaseDetailsRepository extends JpaRepository<CaseDetails, Long> {

	Page<CaseDetails> findAll(Pageable pageable);

	List<CaseDetails> findByCaseNoContainingIgnoreCase(String input);

	CaseDetails findByCaseNo(String caseid);

	boolean existsByCaseNo(String caseNo);

	Page<CaseDetails> findByCurrentUser(User currentUser, Pageable pageable);

	Page<CaseDetails> findByCaseNoContainingIgnoreCase(String caseno, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndCaseNoContainingIgnoreCase(User currentUser, String caseno,
			Pageable pageable);

	CaseDetails findByCaseNoIn(List<String> asList);

	boolean existsByCaseNoContainingIgnoreCase(String caseNo);

	Page<CaseDetails> findByRuleApplicableRuleName(String rule, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableRuleName(User currentUser, String rule, Pageable pageable);

	/*
	 * Page<CaseDetails>
	 * findByRuleApplicableRuleNameAndCaseNoContainingIgnoreCaseAndMisconductTypesMasterMisconductName
	 * (String rule, String caseno, String misconduct Pageable pageable);
	 */

	// Page<CaseDetails>
	// findByRuleApplicableRuleNameAndCaseNoContainingIgnoreCaseAndMisconductTypesMasterMisconductName(String
	// rule, String caseno, String misconduct, Pageable pageable);

	/*
	 * Page<CaseDetails> findByRuleApplicableRuleNameAndCaseNoContainingIgnoreCase(
	 * String rule, String caseno, Pageable pageable);
	 */

	Page<CaseDetails> findByCurrentUserAndRuleApplicableRuleNameAndCaseNoContainingIgnoreCase(User currentUser,
			String rule, String caseno, Pageable pageable);

//	added on 12-10-2022
//	Page<CaseDetails> findByMisconductTypesMasterMisconductName(String misconduct, Pageable pageable);

//	Page<CaseDetails> findByMisconductTypesMasterMisconductName(User currentUser, String misconduct, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableRuleNameAndCaseNoContainingIgnoreCaseAndMisConductTypeMisconductNameContainingIgnoreCase(
			String rule, String caseno, String misconduct, Pageable pageable);

	// Page<CaseDetails> findByMisConductTypeMisconductName(String misconduct,
	// Pageable pageable);

	Page<CaseDetails> findByMisConductTypeMisconductNameContainingIgnoreCase(String misconduct, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableRuleNameAndCaseNoContainingIgnoreCaseAndMisConductTypeMisconductNameContainingIgnoreCase(
			User currentUser, String rule, String caseno, String misconduct, Pageable pageable);

	Page<CaseDetails> findByMisConductTypeMisconductNameContainingIgnoreCase(User currentUser, String misconduct,
			Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCase(
			User currentUser, String search, User currentUser2, String search2, User currentUser3, String search3,
			Pageable pageable);

	Page<CaseDetails> findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCase(
			String search, String search2, String search3, Pageable pageable);

	Page<CaseDetails> findByChargeSheetListIn(List<ChargesheetDetails> chargeSheetList, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCase(
			String search, String search2, String search3, String search4, Pageable pageable);

	Page<CaseDetails> findByEmployeeListInAndRuleApplicableRuleNameContainingIgnoreCaseOrEmployeeListInAndCaseNoContainingIgnoreCaseOrEmployeeListInAndMisConductTypeMisconductNameContainingIgnoreCaseOrEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<EmployeeDetails> empList, String search, List<EmployeeDetails> empList2, String search2,
			List<EmployeeDetails> empList3, String search3, List<EmployeeDetails> empList4, String search4,
			Pageable pageable);

	Page<CaseDetails> findByEmployeeListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<EmployeeDetails> empList, String search, String search2, String search3, String search4,
			Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndEmployeeListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<EmployeeDetails> empList, User currentUser2, String search, User currentUser3,
			String search2, User currentUser4, String search3, User currentUser5, String search4, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, String search, User currentUser2, String search2, User currentUser3, String search3,
			User currentUser4, String search4, Pageable pageable);

	Page<CaseDetails> findByPoListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<PresentingOfficerDetails> poList, String search, String search2, String search3, String search4,
			Pageable pageable);

	Page<CaseDetails> findByIoListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<InquiryOfficerDetails> inqList, String search, String search2, String search3, String search4,
			Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndPoListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<PresentingOfficerDetails> poList, User currentUser2, String search,
			User currentUser3, String search2, User currentUser4, String search3, User currentUser5, String search4,
			Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndIoListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<InquiryOfficerDetails> inqList, User currentUser2, String search, User currentUser3,
			String search2, User currentUser4, String search3, User currentUser5, String search4, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndPoListInOrCurrentUserAndIoListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<PresentingOfficerDetails> poList, User currentUser2,
			List<InquiryOfficerDetails> inqList, User currentUser3, String search, User currentUser4, String search2,
			User currentUser5, String search3, User currentUser6, String search4, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndPoListInOrCurrentUserAndIoListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<PresentingOfficerDetails> poList, User currentUser2,
			List<InquiryOfficerDetails> inqList, User currentUser3, String search, User currentUser4, String search2,
			User currentUser5, String search3, User currentUser6, String search4, Pageable pageable);

	Page<CaseDetails> findByPoListInOrIoListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<PresentingOfficerDetails> poList, List<InquiryOfficerDetails> inqList, String search, String search2,
			String search3, String search4, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseOrFileSubjectContainingIgnoreCase(
			String search, String search2, String search3, String search4, String search5, Pageable pageable);

	Page<CaseDetails> findByPoListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseOrFileSubjectContainingIgnoreCase(
			List<PresentingOfficerDetails> poList, String search, String search2, String search3, String search4,
			String search5, Pageable pageable);

	Page<CaseDetails> findByIoListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseOrFileSubjectContainingIgnoreCase(
			List<InquiryOfficerDetails> inqList, String search, String search2, String search3, String search4,
			String search5, Pageable pageable);

	Page<CaseDetails> findByPoListInOrIoListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseOrFileSubjectContainingIgnoreCase(
			List<PresentingOfficerDetails> poList, List<InquiryOfficerDetails> inqList, String search, String search2,
			String search3, String search4, String search5, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserAndFileSubjectContainingIgnoreCase(
			User currentUser, String search, User currentUser2, String search2, User currentUser3, String search3,
			User currentUser4, String search4, User currentUser5, String search5, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndPoListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserAndFileSubjectContainingIgnoreCase(
			User currentUser, List<PresentingOfficerDetails> poList, User currentUser2, String search,
			User currentUser3, String search2, User currentUser4, String search3, User currentUser5, String search4,
			User currentUser6, String search5, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndIoListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserAndFileSubjectContainingIgnoreCase(
			User currentUser, List<InquiryOfficerDetails> inqList, User currentUser2, String search, User currentUser3,
			String search2, User currentUser4, String search3, User currentUser5, String search4, User currentUser6,
			String search5, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndPoListInOrCurrentUserAndIoListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserAndFileSubjectContainingIgnoreCase(
			User currentUser, List<PresentingOfficerDetails> poList, User currentUser2,
			List<InquiryOfficerDetails> inqList, User currentUser3, String search, User currentUser4, String search2,
			User currentUser5, String search3, User currentUser6, String search4, User currentUser7, String search5,
			Pageable pageable);

	List<CaseDetails> findByCaseNoContainingIgnoreCaseAndGlobalorg(String input, GlobalOrg globalOrgId);

	CaseDetails findByCaseNoAndGlobalorg(String string, GlobalOrg globalOrgId);

	List<CaseDetails> findByCaseNoContainingIgnoreCaseAndCurrentUser(String input, User currentUser);

	CaseDetails findByCaseNoAndCurrentUser(String caseid, User currentUser);

	boolean existsByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg s);

	long countByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg s);

	boolean existsByCurrentUserPimsEmployeeGlobalOrgIdAndSubDepartment(GlobalOrg globalOrgId,
			SubDepartment subDepartment);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndSubDepartment(GlobalOrg globalOrgId, SubDepartment subDepartment);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableId(GlobalOrg s, long l);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotIn(GlobalOrg s, List<Long> asList);

	Page<CaseDetails> findByChargeSheetListInAndCurrentUser(List<ChargesheetDetails> chargeSheetList, User currentUser,
			Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndEmployeeListInAndChargeSheetListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<EmployeeDetails> empList, List<ChargesheetDetails> chargeSheetList,
			User currentUser2, String search, User currentUser3, String search2, User currentUser4, String search3,
			User currentUser5, String search4, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndChargeSheetListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<ChargesheetDetails> chargeSheetList, User currentUser2, String search,
			User currentUser3, String search2, User currentUser4, String search3, User currentUser5, String search4,
			Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListIn(
			User currentUser, String search, List<ChargesheetDetails> chargeSheetList, User currentUser2,
			String search2, List<ChargesheetDetails> chargeSheetList2, User currentUser3, String search3,
			List<ChargesheetDetails> chargeSheetList3, User currentUser4, String search4,
			List<ChargesheetDetails> chargeSheetList4, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListIn(
			User currentUser, String search, List<EmployeeDetails> empList, User currentUser2, String search2,
			List<EmployeeDetails> empList2, User currentUser3, String search3, List<EmployeeDetails> empList3,
			User currentUser4, String search4, List<EmployeeDetails> empList4, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListIn(
			User currentUser, String search, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList,
			User currentUser2, String search2, List<ChargesheetDetails> chargeSheetList2,
			List<EmployeeDetails> empList2, User currentUser3, String search3,
			List<ChargesheetDetails> chargeSheetList3, List<EmployeeDetails> empList3, User currentUser4,
			String search4, List<ChargesheetDetails> chargeSheetList4, List<EmployeeDetails> empList4,
			Pageable pageable);

	Page<CaseDetails> findByRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCaseNoContainingIgnoreCaseAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListIn(
			String search, List<EmployeeDetails> empList, String search2, List<EmployeeDetails> empList2,
			String search3, List<EmployeeDetails> empList3, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListIn(
			String search, List<ChargesheetDetails> chargeSheetList, String search2,
			List<ChargesheetDetails> chargeSheetList2, String search3, List<ChargesheetDetails> chargeSheetList3,
			Pageable pageable);

	Page<CaseDetails> findByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListIn(
			String search, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList, String search2,
			List<ChargesheetDetails> chargeSheetList2, List<EmployeeDetails> empList2, String search3,
			List<ChargesheetDetails> chargeSheetList3, List<EmployeeDetails> empList3, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCaseNoContainingIgnoreCaseAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListIn(
			String search, List<EmployeeDetails> empList, String search2, List<EmployeeDetails> empList2,
			String search3, List<EmployeeDetails> empList3, String search4, List<EmployeeDetails> empList4,
			Pageable pageable);

	Page<CaseDetails> findByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListIn(
			String search, List<ChargesheetDetails> chargeSheetList, String search2,
			List<ChargesheetDetails> chargeSheetList2, String search3, List<ChargesheetDetails> chargeSheetList3,
			String search4, List<ChargesheetDetails> chargeSheetList4, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListIn(
			String search, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList, String search2,
			List<ChargesheetDetails> chargeSheetList2, List<EmployeeDetails> empList2, String search3,
			List<ChargesheetDetails> chargeSheetList3, List<EmployeeDetails> empList3, String search4,
			List<ChargesheetDetails> chargeSheetList4, List<EmployeeDetails> empList4, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdNotInOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<Long> asList, String search, String search2, String search3, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCase(
			List<Long> asList, String search, String search2, String search3, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(List<Long> asList,
			String search, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(List<Long> asList,
			String customsearch, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<Long> asList, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList, String search,
			Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<Long> asList, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList, String search,
			List<ChargesheetDetails> chargeSheetList2, List<EmployeeDetails> empList2, String search2,
			List<ChargesheetDetails> chargeSheetList3, List<EmployeeDetails> empList3, String search3,
			List<ChargesheetDetails> chargeSheetList4, List<EmployeeDetails> empList4, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<Long> asList, String customsearch, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<Long> asList, String customsearch, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search,
			Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search,
			Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<Long> asList, List<ChargesheetDetails> chargeSheetList,
			List<EmployeeDetails> empList, String search, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<Long> asList, List<ChargesheetDetails> chargeSheetList,
			List<EmployeeDetails> empList, String search, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<Long> asList, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList, String search,
			Pageable pageable);

	Page<CaseDetails> findByFinaloutListInAndGlobalorgGlobalOrgName(List<FinalOutcomeDetails> finalList,
			String customsearch, Pageable pageable);

	Page<CaseDetails> findByFinaloutListInAndGlobalorgGlobalOrgNameAndCurrentUser(List<FinalOutcomeDetails> finalList,
			String customsearch, User currentUser, Pageable pageable);

	Page<CaseDetails> findByCurrentUserIn(List<User> userList, Pageable pageable);

	Page<CaseDetails> findByCurrentUserInAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<User> userList, List<Long> asList, String customsearch, Pageable pageable);

	Page<CaseDetails> findByCurrentUserInAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<User> userList, List<Long> asList, String customsearch, Pageable pageable);

	Page<CaseDetails> findByChargeSheetListInAndCurrentUserIn(List<ChargesheetDetails> chargeList, List<User> userList,
			Pageable pageable);

	Page<CaseDetails> findByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<User> userList, String search, List<User> userList2, String search2, List<User> userList3,
			String search3, List<User> userList4, String search4, Pageable pageable);

	Page<CaseDetails> findByCurrentUserInAndEmployeeListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<User> userList, List<EmployeeDetails> empList, List<User> userList2, String search,
			List<User> userList3, String search2, List<User> userList4, String search3, List<User> userList5,
			String search4, Pageable pageable);

	Page<CaseDetails> findByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListIn(
			List<User> userList, String search, List<EmployeeDetails> empList, List<User> userList2, String search2,
			List<EmployeeDetails> empList2, List<User> userList3, String search3, List<EmployeeDetails> empList3,
			List<User> userList4, String search4, List<EmployeeDetails> empList4, Pageable pageable);

	Page<CaseDetails> findByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<User> userList, List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search,
			Pageable pageable);

	Page<CaseDetails> findByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListIn(
			List<User> userList, String search, List<ChargesheetDetails> chargeSheetList, List<User> userList2,
			String search2, List<ChargesheetDetails> chargeSheetList2, List<User> userList3, String search3,
			List<ChargesheetDetails> chargeSheetList3, List<User> userList4, String search4,
			List<ChargesheetDetails> chargeSheetList4, Pageable pageable);

	Page<CaseDetails> findByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<User> userList, List<Long> asList, List<ChargesheetDetails> chargeSheetList,
			List<EmployeeDetails> empList, String search, Pageable pageable);

	Page<CaseDetails> findByCurrentUserInAndRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<User> userList, List<Long> asList, List<ChargesheetDetails> chargeSheetList,
			List<EmployeeDetails> empList, String search, Pageable pageable);

	Page<CaseDetails> findByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListIn(
			List<User> userList, String search, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList,
			List<User> userList2, String search2, List<ChargesheetDetails> chargeSheetList2,
			List<EmployeeDetails> empList2, List<User> userList3, String search3,
			List<ChargesheetDetails> chargeSheetList3, List<EmployeeDetails> empList3, List<User> userList4,
			String search4, List<ChargesheetDetails> chargeSheetList4, List<EmployeeDetails> empList4,
			Pageable pageable);

	Page<CaseDetails> findByFinaloutListInAndGlobalorgGlobalOrgNameAndCurrentUserIn(List<FinalOutcomeDetails> finalList,
			String customsearch, List<User> userList, Pageable pageable);

	Page<CaseDetails> findByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserInAndFileSubjectContainingIgnoreCase(
			List<User> userList, String search, List<User> userList2, String search2, List<User> userList3,
			String search3, List<User> userList4, String search4, List<User> userList5, String search5,
			Pageable pageable);

	Page<CaseDetails> findByCurrentUserInAndPoListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserInAndFileSubjectContainingIgnoreCase(
			List<User> userList, List<PresentingOfficerDetails> poList, List<User> userList2, String search,
			List<User> userList3, String search2, List<User> userList4, String search3, List<User> userList5,
			String search4, List<User> userList6, String search5, Pageable pageable);

	Page<CaseDetails> findByCurrentUserInAndIoListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserInAndFileSubjectContainingIgnoreCase(
			List<User> userList, List<InquiryOfficerDetails> inqList, List<User> userList2, String search,
			List<User> userList3, String search2, List<User> userList4, String search3, List<User> userList5,
			String search4, List<User> userList6, String search5, Pageable pageable);

	Page<CaseDetails> findByCurrentUserInAndPoListInOrCurrentUserInAndIoListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserInAndFileSubjectContainingIgnoreCase(
			List<User> userList, List<PresentingOfficerDetails> poList, List<User> userList2,
			List<InquiryOfficerDetails> inqList, List<User> userList3, String search, List<User> userList4,
			String search2, List<User> userList5, String search3, List<User> userList6, String search4,
			List<User> userList7, String search5, Pageable pageable);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg globalOrgId, Pageable pageable);

	Page<CaseDetails> findByCurrentUserSubDepartmentIn(List<SubDepartment> subDepartmentList, Pageable pageable);

	Page<CaseDetails> findByCurrentUserSubDepartmentInOrCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentIsNull(
			List<SubDepartment> subDepartmentList, GlobalOrg globalOrgId, Pageable pageable);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCaseNoContainingIgnoreCase(GlobalOrg globalOrgId,
			String caseno, Pageable pageable);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeListIn(GlobalOrg s, List<EmployeeDetails> empLists);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdAndEmployeeListIn(GlobalOrg s, long l,
			List<EmployeeDetails> empLists);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotInAndEmployeeListIn(GlobalOrg s,
			List<Long> asList, List<EmployeeDetails> empLists);

	long countDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeListIn(GlobalOrg s,
			List<EmployeeDetails> empLists);

	long countDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeListInAndEmployeeListNotIn(GlobalOrg s,
			List<EmployeeDetails> empLists, List<EmployeeDetails> empLists2);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdAndIdIn(GlobalOrg s, long l, Set<Long> caseListId);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotInAndIdIn(GlobalOrg s, List<Long> asList,
			Set<Long> caseListId);

	Page<CaseDetails> findByEmployeeListIn(List<EmployeeDetails> empListRetired, Pageable pageable);

	Page<CaseDetails> findDistinctIdByEmployeeListIn(List<EmployeeDetails> empListRetired, Pageable pageable);

	Page<CaseDetails> findDistinctIdByChargeSheetListIn(List<ChargesheetDetails> chargeList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<Long> asList, String customsearch, Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(List<Long> asList,
			String customsearch, Pageable pageable);

	Page<CaseDetails> findDistinctIdByEmployeeListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<EmployeeDetails> empList, String search, String search2, String search3, String search4,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCaseNoContainingIgnoreCaseAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListIn(
			String search, List<EmployeeDetails> empList, String search2, List<EmployeeDetails> empList2,
			String search3, List<EmployeeDetails> empList3, String search4, List<EmployeeDetails> empList4,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search, Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search, Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListIn(
			String search, List<ChargesheetDetails> chargeSheetList, String search2,
			List<ChargesheetDetails> chargeSheetList2, String search3, List<ChargesheetDetails> chargeSheetList3,
			String search4, List<ChargesheetDetails> chargeSheetList4, Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<Long> asList, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList, String search,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<Long> asList, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList, String search,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListIn(
			String search, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList, String search2,
			List<ChargesheetDetails> chargeSheetList2, List<EmployeeDetails> empList2, String search3,
			List<ChargesheetDetails> chargeSheetList3, List<EmployeeDetails> empList3, String search4,
			List<ChargesheetDetails> chargeSheetList4, List<EmployeeDetails> empList4, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<User> userList, List<Long> asList, String customsearch, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<User> userList, List<Long> asList, String customsearch, Pageable pageable);

	Page<CaseDetails> findDistinctIdByChargeSheetListInAndCurrentUserIn(List<ChargesheetDetails> chargeList,
			List<User> userList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<User> userList, String search, List<User> userList2, String search2, List<User> userList3,
			String search3, List<User> userList4, String search4, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndEmployeeListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<User> userList, List<EmployeeDetails> empList, List<User> userList2, String search,
			List<User> userList3, String search2, List<User> userList4, String search3, List<User> userList5,
			String search4, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListIn(
			List<User> userList, String search, List<EmployeeDetails> empList, List<User> userList2, String search2,
			List<EmployeeDetails> empList2, List<User> userList3, String search3, List<EmployeeDetails> empList3,
			List<User> userList4, String search4, List<EmployeeDetails> empList4, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<User> userList, List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListIn(
			List<User> userList, String search, List<ChargesheetDetails> chargeSheetList, List<User> userList2,
			String search2, List<ChargesheetDetails> chargeSheetList2, List<User> userList3, String search3,
			List<ChargesheetDetails> chargeSheetList3, List<User> userList4, String search4,
			List<ChargesheetDetails> chargeSheetList4, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<User> userList, List<Long> asList, List<ChargesheetDetails> chargeSheetList,
			List<EmployeeDetails> empList, String search, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			List<User> userList, List<Long> asList, List<ChargesheetDetails> chargeSheetList,
			List<EmployeeDetails> empList, String search, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListIn(
			List<User> userList, String search, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList,
			List<User> userList2, String search2, List<ChargesheetDetails> chargeSheetList2,
			List<EmployeeDetails> empList2, List<User> userList3, String search3,
			List<ChargesheetDetails> chargeSheetList3, List<EmployeeDetails> empList3, List<User> userList4,
			String search4, List<ChargesheetDetails> chargeSheetList4, List<EmployeeDetails> empList4,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<Long> asList, String customsearch, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<Long> asList, String customsearch, Pageable pageable);

	Page<CaseDetails> findDistinctIdByChargeSheetListInAndCurrentUser(List<ChargesheetDetails> chargeList,
			User currentUser, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, String search, User currentUser2, String search2, User currentUser3, String search3,
			User currentUser4, String search4, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListIn(
			User currentUser, String search, List<ChargesheetDetails> chargeSheetList, User currentUser2,
			String search2, List<ChargesheetDetails> chargeSheetList2, User currentUser3, String search3,
			List<ChargesheetDetails> chargeSheetList3, User currentUser4, String search4,
			List<ChargesheetDetails> chargeSheetList4, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<Long> asList, List<ChargesheetDetails> chargeSheetList,
			List<EmployeeDetails> empList, String search, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
			User currentUser, List<Long> asList, List<ChargesheetDetails> chargeSheetList,
			List<EmployeeDetails> empList, String search, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListIn(
			User currentUser, String search, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList,
			User currentUser2, String search2, List<ChargesheetDetails> chargeSheetList2,
			List<EmployeeDetails> empList2, User currentUser3, String search3,
			List<ChargesheetDetails> chargeSheetList3, List<EmployeeDetails> empList3, User currentUser4,
			String search4, List<ChargesheetDetails> chargeSheetList4, List<EmployeeDetails> empList4,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUser(User currentUser, Pageable pageable);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserIn(List<EmployeeDetails> empListRetired,
			List<User> userList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUser(List<EmployeeDetails> empListRetired,
			User currentUser, Pageable pageable);

	long countDistinctIdByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg s);

	long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableId(GlobalOrg s, long l);

	long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotIn(GlobalOrg s,
			List<Long> asList);

	long countDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeListNotIn(GlobalOrg s,
			List<EmployeeDetails> empLists);

	List<CaseDetails> findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeListNotIn(GlobalOrg s,
			List<EmployeeDetails> empLists);

	long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdAndIdIn(GlobalOrg s, long l,
			Set<Long> caseListId);

	long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotInAndIdIn(GlobalOrg s,
			List<Long> asList, Set<Long> caseListId);

	long countDistinctIdByCurrentUserSubDepartment(SubDepartment s);

	long countDistinctCasedetailsByCurrentUserSubDepartmentAndRuleApplicableId(SubDepartment s, long l);

	long countDistinctCasedetailsByCurrentUserSubDepartmentAndRuleApplicableIdNotIn(SubDepartment s, List<Long> asList);

	Page<CaseDetails> findDistinctIdByIoListIn(List<InquiryOfficerDetails> inqList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByIoListInAndCurrentUserIn(List<InquiryOfficerDetails> inqList, List<User> userList,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByIoListInAndCurrentUser(List<InquiryOfficerDetails> inqList, User currentUser,
			Pageable pageable);

	Page<CaseDetails> findByRuleApplicableId(Long valueOf, Pageable pageable);

	Page<CaseDetails> findByMisConductTypeId(Long valueOf, Pageable pageable);

	Page<CaseDetails> findByDivisionId(Long valueOf, Pageable pageable);

	Page<CaseDetails> findByDistrictId(Long valueOf, Pageable pageable);

	long countDistinctIdByChargeSheetListIn(
			List<ChargesheetDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull);

	long countDistinctIdByAbeyanceListIn(List<DetailsKeptAbeyanceCases> abeyList);

	long countDistinctIdByProsecutionProposalIn(List<ProsecutionProposalDetails> proseList);

	long countDistinctIdByEmployeeListIn(List<EmployeeDetails> empList);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableId(GlobalOrg s,
			long l);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotIn(GlobalOrg s,
			List<Long> asList);

	List<CaseDetails> findDistinctIdByEmployeeListIn(List<EmployeeDetails> empList);

	List<CaseDetails> findDistinctIdByChargeSheetListIn(
			List<ChargesheetDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull);

	List<CaseDetails> findDistinctIdByProsecutionProposalIn(List<ProsecutionProposalDetails> proseList);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdNotIn(List<Long> asList, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdNotIn(List<Long> asList, Pageable pageableall);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdIn(List<Long> asList, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdIn(List<Long> asList, Pageable pageableall);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdAndIdIn(GlobalOrg s,
			long l, Set<Long> caseListId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotInAndIdIn(
			GlobalOrg s, List<Long> asList, Set<Long> caseListId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserSubDepartmentAndRuleApplicableId(SubDepartment s, long l);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserSubDepartmentAndRuleApplicableIdNotIn(SubDepartment s,
			List<Long> asList);

	List<CaseDetails> findByDivision_Id(Long i);

	List<CaseDetails> findByCurrentUserAndDivision_Id(User currentUser, Long valueOf);

	List<CaseDetails> findByCurrentUserInAndDivision_Id(List<User> userList, Long valueOf);

	long countDistinctIdByCurrentUserIn(List<User> userList);

	Page<CaseDetails> findDistinctIdByCurrentUserIn(List<User> userList, Pageable pageable);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndDivision_Id(GlobalOrg globalOrgId, Long valueOf,
			Pageable pageable);

	List<CaseDetails> findByRuleApplicable_Id(Long valueOf);

	List<CaseDetails> findByCurrentUserAndRuleApplicable_Id(User currentUser, Long valueOf);

	List<CaseDetails> findByCurrentUserAndMisConductTypeIn(User currentUser, List<MisconductTypesMaster> miscType);

	List<CaseDetails> findByCurrentUserInAndMisConductTypeIn(List<User> userList, List<MisconductTypesMaster> miscType);

	List<CaseDetails> findByMisConductTypeIn(List<MisconductTypesMaster> miscType);

	Page<CaseDetails> findByMisConductTypeIn(List<MisconductTypesMaster> miscType, Pageable pageable);

	long countDistinctIdByMisConductTypeIn(List<MisconductTypesMaster> miscTypeAdmin);

	List<CaseDetails> findDistinctIdByMisConductTypeIn(List<MisconductTypesMaster> miscTypeAdmin);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndMisConductTypeIn(List<User> userList,
			List<MisconductTypesMaster> miscType, Pageable pageable);

	Page<CaseDetails> findDistinctIdByCurrentUserAndMisConductTypeIn(User currentUser,
			List<MisconductTypesMaster> miscType, Pageable pageable);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserIn(List<EmployeeDetails> empList,
			List<User> userList);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUser(List<EmployeeDetails> empList, User currentUser);

	List<CaseDetails> findDistinctIdByIoListIn(List<InquiryOfficerDetails> ioListall);

	List<CaseDetails> findDistinctIdByIoListInAndCurrentUserIn(List<InquiryOfficerDetails> ioList22,
			List<User> userList);

	List<CaseDetails> findDistinctIdByIoListInAndCurrentUser(List<InquiryOfficerDetails> ioList22, User currentUser);

	List<CaseDetails> findDistinctIdByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg s);

	Page<CaseDetails> findByCurrentUserAndGlobalorg(User currentUser, GlobalOrg globalOrgId, Pageable pageable);

	List<CaseDetails> findByCurrentUserAndRuleApplicable_IdAndGlobalorg(User currentUser, GlobalOrg globalOrgId,
			Long valueOf);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserIn(GlobalOrg org, List<User> userList,
			Pageable pageable);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUser(GlobalOrg org, User currentUser,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByFinaloutListIn(List<FinalOutcomeDetails> finaloutlist, Pageable pageable);

	Page<CaseDetails> findDistinctIdByFinaloutListInAndCurrentUserIn(List<FinalOutcomeDetails> finaloutlist,
			List<User> userList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByFinaloutListInAndCurrentUser(List<FinalOutcomeDetails> finaloutlist,
			User currentUser, Pageable pageable);

	List<CaseDetails> findDistinctIdByFinaloutListIn(List<FinalOutcomeDetails> finalOutList);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicable_Id(GlobalOrg globalOrgId, Long valueOf,
			Pageable pageable);

	Page<CaseDetails> findByRuleApplicable_Id(Long valueOf, Pageable pageable);

	Long countByDistrict_Id(Long valueOf);

	List<CaseDetails> findByDistrict_Id(Long valueOf);

	List<CaseDetails> findByEmployeeListIn(List<EmployeeDetails> listSuspended);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndDistrict_Id(GlobalOrg globalOrgId, Long valueOf,
			Pageable pageable);

	Page<CaseDetails> findByDistrict_Id(Long valueOf, Pageable pageable);

	List<CaseDetails> findByCurrentUserAndDistrict_Id(User currentUser, Long valueOf);

	List<CaseDetails> findByCurrentUserInAndDistrict_Id(List<User> userList, Long valueOf);

	List<CaseDetails> findByCurrentUserInAndRuleApplicable_Id(List<User> userList, Long valueOf);

	Page<CaseDetails> findByDivision_Id(Long valueOf, Pageable pageable);

	Page<CaseDetails> findByIdIn(Set<Long> keySet, Pageable pageable);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(List<EmployeeDetails> empList,
			GlobalOrg s);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(List<EmployeeDetails> empList,
			GlobalOrg org, Pageable pageable);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserIn(
			List<EmployeeDetails> empList, GlobalOrg org, Pageable pageable, List<User> userList);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUser(
			List<EmployeeDetails> empList, GlobalOrg org, Pageable pageable, User currentUser);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(
			List<EmployeeDetails> empListFil, Pageable pageable, GlobalOrg org);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserIn(List<EmployeeDetails> empListx, Pageable pageable,
			List<User> userList);

	Page<CaseDetails> findByEmployeeListInAndCurrentUserIn(List<EmployeeDetails> empList, Pageable allPage,
			List<User> userList);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUser(List<EmployeeDetails> empListx, Pageable pageable,
			User currentUser);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdId(
			List<EmployeeDetails> empList, Long valueOf);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdId(
			List<EmployeeDetails> empListx, Pageable pageable, Long valueOf);

	Page<CaseDetails> findDistinctIdByGlobalorgId(Long valueOf, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdAndMisConductTypeId(Long valueOf, Long valueOf2, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdAndDivisionIdAndDistrictId(Long valueOf, Long valueOf2, Long valueOf3,
			Pageable pageable);

	Page<CaseDetails> findByMisConductTypeIdAndDivisionIdAndDistrictId(Long valueOf, Long valueOf2, Long valueOf3,
			Pageable pageable);

	Page<CaseDetails> findByGlobalorgIdAndDivisionIdAndDistrictId(Long valueOf, Long valueOf2, Long valueOf3,
			Pageable pageable);

	Page<CaseDetails> findByGlobalorgIdAndMisConductTypeId(Long valueOf, Long valueOf2, Pageable pageable);

	Page<CaseDetails> findByGlobalorgIdAndRuleApplicableId(Long valueOf, Long valueOf2, Pageable pageable);

	Page<CaseDetails> findByGlobalorgIdAndDivisionIdAndDistrictIdAndMisConductTypeId(Long valueOf, Long valueOf2,
			Long valueOf3, Long valueOf4, Pageable pageable);

	Page<CaseDetails> findByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableId(Long valueOf, Long valueOf2,
			Long valueOf3, Long valueOf4, Pageable pageable);

	Page<CaseDetails> findByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndMisConductTypeId(Long valueOf,
			Long valueOf2, Long valueOf3, Long valueOf4, Long valueOf5, Pageable pageable);

	Page<CaseDetails> findByGlobalorgIdAndRuleApplicableIdAndMisConductTypeId(Long valueOf, Long valueOf2,
			Long valueOf3, Pageable pageable);

	Page<CaseDetails> findByDivisionIdAndDistrictId(Long valueOf, Long valueOf2, Pageable pageable);

	Page<CaseDetails> findByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(
			Long valueOf, Long valueOf2, Long valueOf3, Long valueOf4, Long valueOf5, List<EmployeeDetails> empList,
			Pageable pageable);

	Page<CaseDetails> findByGlobalorgIdAndEmployeeListIn(Long valueOf, List<EmployeeDetails> empList,
			Pageable pageable);

	Page<CaseDetails> findByGlobalorgIdAndMisConductTypeIdAndEmployeeListIn(Long valueOf, Long valueOf2,
			List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findByGlobalorgIdAndRuleApplicableIdAndEmployeeListIn(Long valueOf, Long valueOf2,
			List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdAndEmployeeListIn(Long valueOf, List<EmployeeDetails> empList,
			Pageable pageable);

	Page<CaseDetails> findByMisConductTypeIdAndEmployeeListIn(Long valueOf, List<EmployeeDetails> empList,
			Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long valueOf, Long valueOf2,
			List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findByRuleApplicableIdAndDivisionIdAndDistrictIdAndEmployeeListIn(Long valueOf, Long valueOf2,
			Long valueOf3, List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findByMisConductTypeIdAndDivisionIdAndDistrictIdAndEmployeeListIn(Long valueOf, Long valueOf2,
			Long valueOf3, List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findByDivisionIdAndDistrictIdAndEmployeeListIn(Long valueOf, Long valueOf2,
			List<EmployeeDetails> empList, Pageable pageable);

	List<CaseDetails> findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeListIn(GlobalOrg s,
			List<EmployeeDetails> empLists);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserSubDepartment(List<EmployeeDetails> empListSuspended,
			SubDepartment s);

	Page<CaseDetails> findDistinctIdByProsecutionProposalIn(List<ProsecutionProposalDetails> proseList,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByProsecutionProposalInAndCurrentUserIn(List<ProsecutionProposalDetails> proseList,
			Pageable pageable, List<User> userList);

	Page<CaseDetails> findDistinctIdByProsecutionProposalInAndCurrentUser(List<ProsecutionProposalDetails> proseList,
			Pageable pageable, User currentUser);

	Page<CaseDetails> findDistinctIdByProsecutionProposalInAndCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCase(
			List<ProsecutionProposalDetails> proseList, Pageable pageable, String customsearch);

	List<CaseDetails> findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartment(GlobalOrg s,
			SubDepartment subDep);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartment(
			List<EmployeeDetails> empListSuspended, GlobalOrg s, SubDepartment subDep);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableId(
			GlobalOrg s, SubDepartment subDep, long l);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdNotIn(
			GlobalOrg s, SubDepartment subDep, List<Long> asList);

	List<CaseDetails> findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndEmployeeListIn(
			GlobalOrg s, SubDepartment subDep, List<EmployeeDetails> empLists);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdAndIdIn(
			GlobalOrg s, SubDepartment subDep, long l, Set<Long> caseListId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdNotInAndIdIn(
			GlobalOrg s, SubDepartment subDep, List<Long> asList, Set<Long> caseListId);

	List<CaseDetails> findByDivision_IdAndCurrentUserPimsEmployeeGlobalOrgId(Long valueOf, GlobalOrg org);

	Page<CaseDetails> findByDivision_IdAndCurrentUserPimsEmployeeGlobalOrgId(Long valueOf, GlobalOrg org,
			Pageable pageable);

	List<CaseDetails> findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgId(Long valueOf, GlobalOrg org);


	Page<CaseDetails> findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgId(Long valueOf, GlobalOrg org,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgId(
			List<InquiryOfficerDetails> inqOffDeFil, GlobalOrg org, Pageable pageable);

	List<CaseDetails> findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgId(
			List<InquiryOfficerDetails> ioList22, GlobalOrg org);

	List<CaseDetails> findByMisConductTypeInAndCurrentUserPimsEmployeeGlobalOrgId(List<MisconductTypesMaster> findAll,
			GlobalOrg org);

	Page<CaseDetails> findByMisConductTypeInAndCurrentUserPimsEmployeeGlobalOrgId(List<MisconductTypesMaster> miscType,
			GlobalOrg org, Pageable pageable);

	List<CaseDetails> findDistinctIdByMisConductTypeInAndCurrentUserPimsEmployeeGlobalOrgId(
			List<MisconductTypesMaster> miscTypeAdmin, GlobalOrg org);

	List<CaseDetails> findByRuleApplicable_IdAndCurrentUserPimsEmployeeGlobalOrgId(Long valueOf, GlobalOrg org);

	Page<CaseDetails> findByRuleApplicable_IdAndCurrentUserPimsEmployeeGlobalOrgId(Long valueOf, GlobalOrg org,
			Pageable pageable);



	Page<CaseDetails> findDistinctIdByFinaloutListInAndCurrentUserPimsEmployeeGlobalOrgId(
			List<FinalOutcomeDetails> finaloutlist, GlobalOrg org, Pageable pageable);

	List<CaseDetails> findDistinctIdByCurrentUser(User s);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserAndRuleApplicableId(User s, long l);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserAndRuleApplicableIdNotIn(User s, List<Long> asList);

	List<CaseDetails> findDistinctIdByCurrentUserAndEmployeeListIn(User s, List<EmployeeDetails> empLists);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserAndRuleApplicableIdAndIdIn(User s, long l,
			Set<Long> caseListId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserAndRuleApplicableIdNotInAndIdIn(User s, List<Long> asList,
			Set<Long> caseListId);

	Page<CaseDetails> findDistinctIdByRuleApplicableId(Long valueOf, Pageable pageable);

	Page<CaseDetails> findDistinctIdByMisConductTypeId(Long valueOf, Pageable pageable);

	Page<CaseDetails> findDistinctIdByDivisionIdAndDistrictId(Long valueOf, Long valueOf2, Pageable pageable);

	Page<CaseDetails> findDistinctIdByDistrictId(Long valueOf, Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdAndMisConductTypeId(Long valueOf, Long valueOf2,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdAndDivisionIdAndDistrictId(Long valueOf, Long valueOf2,
			Long valueOf3, Pageable pageable);

	Page<CaseDetails> findDistinctIdByMisConductTypeIdAndDivisionIdAndDistrictId(Long valueOf, Long valueOf2,
			Long valueOf3, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictId(Long valueOf, Long valueOf2, Long valueOf3,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndMisConductTypeId(Long valueOf, Long valueOf2, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndRuleApplicableId(Long valueOf, Long valueOf2, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndMisConductTypeId(Long valueOf,
			Long valueOf2, Long valueOf3, Long valueOf4, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableId(Long valueOf,
			Long valueOf2, Long valueOf3, Long valueOf4, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndMisConductTypeId(
			Long valueOf, Long valueOf2, Long valueOf3, Long valueOf4, Long valueOf5, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndRuleApplicableIdAndMisConductTypeId(Long valueOf, Long valueOf2,
			Long valueOf3, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(
			Long valueOf, Long valueOf2, Long valueOf3, Long valueOf4, Long valueOf5, List<EmployeeDetails> empList,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndEmployeeListIn(Long valueOf, List<EmployeeDetails> empList,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndMisConductTypeIdAndEmployeeListIn(Long valueOf, Long valueOf2,
			List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndRuleApplicableIdAndEmployeeListIn(Long valueOf, Long valueOf2,
			List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdAndEmployeeListIn(Long valueOf, List<EmployeeDetails> empList,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByMisConductTypeIdAndEmployeeListIn(Long valueOf, List<EmployeeDetails> empList,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long valueOf, Long valueOf2,
			List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdAndDivisionIdAndDistrictIdAndEmployeeListIn(Long valueOf,
			Long valueOf2, Long valueOf3, List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByMisConductTypeIdAndDivisionIdAndDistrictIdAndEmployeeListIn(Long valueOf,
			Long valueOf2, Long valueOf3, List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByDivisionIdAndDistrictIdAndEmployeeListIn(Long valueOf, Long valueOf2,
			List<EmployeeDetails> empList, Pageable pageable);

	List<CaseDetails> findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(GlobalOrg s, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(
			List<EmployeeDetails> empListSuspended, GlobalOrg s, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByChargeSheetListInAndIdNotIn(
			List<ChargesheetDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull,
			Set<Long> caseId);

	List<CaseDetails> findDistinctIdByProsecutionProposalInAndIdNotIn(List<ProsecutionProposalDetails> proseList,
			Set<Long> caseId);

	List<CaseDetails> findDistinctIdByEmployeeListInAndIdNotIn(List<EmployeeDetails> empList, Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotInAndIdNotIn(
			GlobalOrg s, List<Long> asList, Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdAndIdNotIn(
			GlobalOrg s, long l, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeListInAndIdNotIn(GlobalOrg s,
			List<EmployeeDetails> empLists, Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdAndIdInAndIdNotIn(
			GlobalOrg s, long l, Set<Long> caseListId, Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotInAndIdInAndIdNotIn(
			GlobalOrg s, List<Long> asList, Set<Long> caseListId, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndIdNotIn(
			GlobalOrg s, SubDepartment subDep, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndIdNotIn(
			List<EmployeeDetails> empListSuspended, GlobalOrg s, SubDepartment subDep, Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdAndIdNotIn(
			GlobalOrg s, SubDepartment subDep, long l, Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdNotInAndIdNotIn(
			GlobalOrg s, SubDepartment subDep, List<Long> asList, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndEmployeeListInAndIdNotIn(
			GlobalOrg s, SubDepartment subDep, List<EmployeeDetails> empLists, Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdAndIdInAndIdNotIn(
			GlobalOrg s, SubDepartment subDep, long l, Set<Long> caseListId, Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdNotInAndIdInAndIdNotIn(
			GlobalOrg s, SubDepartment subDep, List<Long> asList, Set<Long> caseListId, Set<Long> caseId);

	long countDistinctIdByCurrentUserSubDepartmentAndIdNotIn(SubDepartment s, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserSubDepartmentAndIdNotIn(
			List<EmployeeDetails> empListSuspended, SubDepartment s, Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserSubDepartmentAndRuleApplicableIdAndIdNotIn(SubDepartment s,
			long l, Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserSubDepartmentAndRuleApplicableIdNotInAndIdNotIn(
			SubDepartment s, List<Long> asList, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByCurrentUserAndEmployeeListInAndIdNotIn(User s, List<EmployeeDetails> empLists,
			Set<Long> caseId);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(List<EmployeeDetails> empListSuspended,
			User s, Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserAndRuleApplicableIdAndIdInAndIdNotIn(User s, long l,
			Set<Long> caseListId, Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserAndRuleApplicableIdNotInAndIdInAndIdNotIn(User s,
			List<Long> asList, Set<Long> caseListId, Set<Long> caseId);

	Page<CaseDetails> findByIdNotIn(Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByChargeSheetListInAndIdNotIn(List<ChargesheetDetails> chargeSheetList,
			Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInAndIdNotIn(
			String search, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList, String search2,
			List<ChargesheetDetails> chargeSheetList2, List<EmployeeDetails> empList2, String search3,
			List<ChargesheetDetails> chargeSheetList3, List<EmployeeDetails> empList3, String search4,
			List<ChargesheetDetails> chargeSheetList4, List<EmployeeDetails> empList4, Pageable pageableall,
			Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndIdNotIn(List<EmployeeDetails> empListRetired,
			Pageable pageableall, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<Long> asList, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList, String search,
			Pageable pageableall, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<Long> asList, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList, String search,
			Pageable pageableall, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search, Pageable pageableall,
			Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search, Pageable pageableall,
			Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCaseNoContainingIgnoreCaseAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListInAndIdNotIn(
			String search, List<EmployeeDetails> empList, String search2, List<EmployeeDetails> empList2,
			String search3, List<EmployeeDetails> empList3, String search4, List<EmployeeDetails> empList4,
			Pageable pageableall, Set<Long> caseId);

	Page<CaseDetails> findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			String search, String search2, String search3, String search4, Pageable pageableall, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<Long> asList, String search, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<Long> asList, String search, Pageable pageableall, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<Long> asList, String search, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByEmployeeListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<EmployeeDetails> empList, String search, String search2, String search3, String search4,
			Pageable pageableall, Set<Long> caseId);

	Page<CaseDetails> findByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(List<Long> asList,
			String customsearch, Pageable pageableall, Set<Long> caseId);

	Page<CaseDetails> findByRuleApplicableIdInAndIdNotIn(List<Long> asList, Pageable pageableall, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdInAndIdNotIn(List<Long> asList, Pageable pageable,
			Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdNotInAndIdNotIn(List<Long> asList, Pageable pageable,
			Set<Long> caseId);

	Page<CaseDetails> findByRuleApplicableIdNotInAndIdNotIn(List<Long> asList, Pageable pageableall, Set<Long> caseId);

	Page<CaseDetails> findByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndIdNotIn(
			String search, List<ChargesheetDetails> chargeSheetList, String search2,
			List<ChargesheetDetails> chargeSheetList2, String search3, List<ChargesheetDetails> chargeSheetList3,
			String search4, List<ChargesheetDetails> chargeSheetList4, Pageable pageableall, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndIdNotIn(
			String search, List<ChargesheetDetails> chargeSheetList, String search2,
			List<ChargesheetDetails> chargeSheetList2, String search3, List<ChargesheetDetails> chargeSheetList3,
			String search4, List<ChargesheetDetails> chargeSheetList4, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByProsecutionProposalInAndIdNotIn(List<ProsecutionProposalDetails> proseList,
			Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByProsecutionProposalInAndCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<ProsecutionProposalDetails> proseList, Pageable pageable, String customsearch, Set<Long> caseId);


	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdId(
			List<EmployeeDetails> finalList, Pageable allPage, GlobalOrg org);

	List<CaseDetails> findDistinctIdByCurrentUserAndIdNotIn(User s, Set<Long> caseId);


	List<CaseDetails> findDistinctCasedetailsByCurrentUserAndRuleApplicableIdAndIdNotIn(User s, long l,
			Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserAndRuleApplicableIdNotInAndIdNotIn(User s, List<Long> asList,
			Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserAndIdNotIn(User currentUser, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<User> userList, List<Long> asList, String customsearch, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<User> userList, List<Long> asList, String customsearch, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByChargeSheetListInAndCurrentUserInAndIdNotIn(List<ChargesheetDetails> chargeList,
			List<User> userList, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findByChargeSheetListInAndCurrentUserInAndIdNotIn(List<ChargesheetDetails> chargeList,
			List<User> userList, Pageable pageableall, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(List<EmployeeDetails> empListRetired,
			List<User> userList, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<User> userList, String search, List<User> userList2, String search2, List<User> userList3,
			String search3, List<User> userList4, String search4, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndEmployeeListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<User> userList, List<EmployeeDetails> empList, List<User> userList2, String search,
			List<User> userList3, String search2, List<User> userList4, String search3, List<User> userList5,
			String search4, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListInAndIdNotIn(
			List<User> userList, String search, List<EmployeeDetails> empList, List<User> userList2, String search2,
			List<EmployeeDetails> empList2, List<User> userList3, String search3, List<EmployeeDetails> empList3,
			List<User> userList4, String search4, List<EmployeeDetails> empList4, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<User> userList, List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search,
			Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndIdNotIn(
			List<User> userList, String search, List<ChargesheetDetails> chargeSheetList, List<User> userList2,
			String search2, List<ChargesheetDetails> chargeSheetList2, List<User> userList3, String search3,
			List<ChargesheetDetails> chargeSheetList3, List<User> userList4, String search4,
			List<ChargesheetDetails> chargeSheetList4, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<User> userList, List<Long> asList, List<ChargesheetDetails> chargeSheetList,
			List<EmployeeDetails> empList, String search, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<User> userList, List<Long> asList, List<ChargesheetDetails> chargeSheetList,
			List<EmployeeDetails> empList, String search, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInAndIdNotIn(
			List<User> userList, String search, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList,
			List<User> userList2, String search2, List<ChargesheetDetails> chargeSheetList2,
			List<EmployeeDetails> empList2, List<User> userList3, String search3,
			List<ChargesheetDetails> chargeSheetList3, List<EmployeeDetails> empList3, List<User> userList4,
			String search4, List<ChargesheetDetails> chargeSheetList4, List<EmployeeDetails> empList4,
			Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserInAndIdNotIn(List<User> userList, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			User currentUser, List<Long> asList, String customsearch, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			User currentUser, List<Long> asList, String customsearch, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByChargeSheetListInAndCurrentUserAndIdNotIn(List<ChargesheetDetails> chargeList,
			User currentUser, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(List<EmployeeDetails> empListRetired,
			User currentUser, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			User currentUser, String search, User currentUser2, String search2, User currentUser3, String search3,
			User currentUser4, String search4, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserAndEmployeeListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			User currentUser, List<EmployeeDetails> empList, User currentUser2, String search, User currentUser3,
			String search2, User currentUser4, String search3, User currentUser5, String search4, Pageable pageable,
			Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListInAndIdNotIn(
			User currentUser, String search, List<EmployeeDetails> empList, User currentUser2, String search2,
			List<EmployeeDetails> empList2, User currentUser3, String search3, List<EmployeeDetails> empList3,
			User currentUser4, String search4, List<EmployeeDetails> empList4, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			User currentUser, List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search,
			Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			User currentUser, List<Long> asList, List<ChargesheetDetails> chargeSheetList, String search,
			Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndIdNotIn(
			User currentUser, String search, List<ChargesheetDetails> chargeSheetList, User currentUser2,
			String search2, List<ChargesheetDetails> chargeSheetList2, User currentUser3, String search3,
			List<ChargesheetDetails> chargeSheetList3, User currentUser4, String search4,
			List<ChargesheetDetails> chargeSheetList4, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			User currentUser, List<Long> asList, List<ChargesheetDetails> chargeSheetList,
			List<EmployeeDetails> empList, String search, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			User currentUser, List<Long> asList, List<ChargesheetDetails> chargeSheetList,
			List<EmployeeDetails> empList, String search, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInAndIdNotIn(
			User currentUser, String search, List<ChargesheetDetails> chargeSheetList, List<EmployeeDetails> empList,
			User currentUser2, String search2, List<ChargesheetDetails> chargeSheetList2,
			List<EmployeeDetails> empList2, User currentUser3, String search3,
			List<ChargesheetDetails> chargeSheetList3, List<EmployeeDetails> empList3, User currentUser4,
			String search4, List<ChargesheetDetails> chargeSheetList4, List<EmployeeDetails> empList4,
			Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserAndIdNotIn(GlobalOrg org, User currentUser,
			Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInAndIdNotIn(GlobalOrg org,
			List<User> userList, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(GlobalOrg og, Pageable pageable,
			Set<Long> caseId);

	List<CaseDetails> findByDivision_IdAndIdNotIn(Long valueOf, Set<Long> caseId);

	Page<CaseDetails> findByDivision_IdAndIdNotIn(Long valueOf, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findByDivision_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long valueOf, GlobalOrg org,
			Set<Long> caseId);

	Page<CaseDetails> findByDivision_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long valueOf, GlobalOrg org,
			Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findByCurrentUserInAndDivision_IdAndIdNotIn(List<User> userList, Long valueOf, Set<Long> caseId);

	long countDistinctIdByCurrentUserInAndIdNotIn(List<User> userList, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndIdNotIn(List<User> userList, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findByCurrentUserAndDivision_IdAndIdNotIn(User currentUser, Long valueOf, Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserAndIdNotIn(User currentUser, Pageable pageable, Set<Long> caseId);

	long countDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(GlobalOrg globalOrgId, Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndDivision_IdAndIdNotIn(GlobalOrg globalOrgId,
			Long valueOf, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findByDistrict_IdAndIdNotIn(Long valueOf, Set<Long> caseId);

	Page<CaseDetails> findByDistrict_IdAndIdNotIn(Long valueOf, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long valueOf, GlobalOrg org,
			Set<Long> caseId);

	Page<CaseDetails> findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long valueOf, GlobalOrg org,
			Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findByCurrentUserAndDistrict_IdAndIdNotIn(User currentUser, Long valueOf, Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserAndGlobalorgAndIdNotIn(User currentUser, GlobalOrg globalOrgId,
			Pageable allPages, Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndDistrict_IdAndIdNotIn(GlobalOrg globalOrgId,
			Long valueOf, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findByCurrentUserInAndDistrict_IdAndIdNotIn(List<User> userList, Long valueOf, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(
			List<EmployeeDetails> empListFil, GlobalOrg org, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(List<EmployeeDetails> empList,
			List<User> userList, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(
			List<EmployeeDetails> empListFil, Pageable pageable, GlobalOrg org, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByIoListInAndIdNotIn(List<InquiryOfficerDetails> ioListall, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByIoListInAndIdNotIn(List<InquiryOfficerDetails> inqOffDeFil, Pageable pageable,
			Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(
			List<InquiryOfficerDetails> inqOffDeFil, GlobalOrg org, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(
			List<InquiryOfficerDetails> ioList22, GlobalOrg org, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(List<InquiryOfficerDetails> ioListall,
			List<User> userList, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(List<InquiryOfficerDetails> inqOffDeFil,
			List<User> userList, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByIoListInAndCurrentUserAndIdNotIn(List<InquiryOfficerDetails> ioListall,
			User currentUser, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByIoListInAndCurrentUserAndIdNotIn(List<InquiryOfficerDetails> inqOffDeFil,
			User currentUser, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findByMisConductTypeInAndIdNotIn(List<MisconductTypesMaster> findAll, Set<Long> caseId);

	Page<CaseDetails> findByMisConductTypeInAndIdNotIn(List<MisconductTypesMaster> miscType, Pageable pageable,
			Set<Long> caseId);

	List<CaseDetails> findDistinctIdByMisConductTypeInAndIdNotIn(List<MisconductTypesMaster> miscTypeAdmin,
			Set<Long> caseId);

	List<CaseDetails> findByMisConductTypeInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(
			List<MisconductTypesMaster> findAll, GlobalOrg org, Set<Long> caseId);

	Page<CaseDetails> findByMisConductTypeInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(
			List<MisconductTypesMaster> miscType, GlobalOrg org, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByMisConductTypeInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(
			List<MisconductTypesMaster> miscTypeAdmin, GlobalOrg org, Set<Long> caseId);

	List<CaseDetails> findByCurrentUserInAndMisConductTypeInAndIdNotIn(List<User> userList,
			List<MisconductTypesMaster> findAll, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserInAndMisConductTypeInAndIdNotIn(List<User> userList,
			List<MisconductTypesMaster> miscType, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findByCurrentUserAndMisConductTypeInAndIdNotIn(User currentUser,
			List<MisconductTypesMaster> findAll, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByCurrentUserAndMisConductTypeInAndIdNotIn(User currentUser,
			List<MisconductTypesMaster> miscType, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findByRuleApplicable_IdAndIdNotIn(Long valueOf, Set<Long> caseId);

	Page<CaseDetails> findByRuleApplicable_IdAndIdNotIn(Long valueOf, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findByCurrentUserAndRuleApplicable_IdAndIdNotIn(User currentUser, Long valueOf, Set<Long> caseId);

	List<CaseDetails> findByCurrentUserInAndRuleApplicable_IdAndIdNotIn(List<User> userList, Long valueOf,
			Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicable_IdAndIdNotIn(GlobalOrg globalOrgId,
			Long valueOf, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findByRuleApplicable_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long valueOf,
			GlobalOrg org, Set<Long> caseId);

	Page<CaseDetails> findByRuleApplicable_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long valueOf,
			GlobalOrg org, Pageable pageable, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
			List<EmployeeDetails> empList, Long valueOf, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
			List<EmployeeDetails> empListFil, Pageable pageable, Long valueOf, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
			List<EmployeeDetails> finalList, Pageable pageable, GlobalOrg org, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(List<EmployeeDetails> finalList,
			Pageable pageable, List<User> userList, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(List<EmployeeDetails> finalList,
			Pageable allPage, User currentUser, Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserAndDivision_IdAndIdNotIn(User currentUser, Long valueOf, Pageable pageable,
			Set<Long> caseId);

	long countDistinctIdByCurrentUserAndIdNotIn(User currentUser, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByCurrentUserInAndIdNotIn(List<User> userList, Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserAndRuleApplicable_IdAndIdNotIn(User currentUser, Long valueOf, Pageable pageable,
			Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserAndDistrict_IdAndIdNotIn(User currentUser, Long valueOf, Pageable pageable,
			Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long valueOf,
			Long valueOf2, Long valueOf3, List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndEmployeeListIn(
			Long valueOf, Long valueOf2, Long valueOf3, Long valueOf4, List<EmployeeDetails> empList,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndMisConductTypeIdAndEmployeeListIn(
			Long valueOf, Long valueOf2, Long valueOf3, Long valueOf4, List<EmployeeDetails> empList,
			Pageable pageable);

	List<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg s);

	List<CaseDetails> findByCurrentUser(User s);

	Page<CaseDetails> findByIdNotInAndCurrentUserIn(Pageable allPages, Set<Long> caseId, List<User> userList);

	Page<CaseDetails> findByIdNotInAndAndGlobalorgGlobalOrgNameContainingIgnoreCase(Pageable pageable, Set<Long> caseId,
			String customsearch);

	Page<CaseDetails> findByCurrentUserInAndDivision_IdAndIdNotIn(List<User> userList, Long valueOf, Pageable pageable,
			Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserInAndDistrict_IdAndIdNotIn(List<User> userList, Long valueOf, Pageable pageable,
			Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserInAndRuleApplicable_IdAndIdNotIn(List<User> userList, Long valueOf,
			Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByProsecutionProposalInAndCurrentUserInAndIdNotIn(
			List<ProsecutionProposalDetails> proseList, Pageable pageable, List<User> userList, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByProsecutionProposalInAndCurrentUserAndIdNotIn(
			List<ProsecutionProposalDetails> proseList, Pageable pageable, User currentUser, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByEmployeeListInAndIdNotInAndCurrentUser(List<EmployeeDetails> empProList,
			Set<Long> caseId, User s);

	List<CaseDetails> findByCaseNoContainingIgnoreCaseAndCurrentUserIn(String input, List<User> userList);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			List<EmployeeDetails> empProList, Pageable pageable, String customsearch, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndIdNotInAndCurrentUserIn(List<EmployeeDetails> empProList,
			Pageable pageable, Set<Long> caseId, List<User> userList);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndIdNotInAndCurrentUser(List<EmployeeDetails> empProList,
			Pageable pageable, Set<Long> caseId, User currentUser);

	boolean existsByCaseNoAndCurrentUser(String caseNo, User currentUser);

	Page<CaseDetails> findByCaseNoContainingIgnoreCaseAndActiveForTransfer(String caseno, boolean b, Pageable pageable);

	Page<CaseDetails> findByActiveForTransfer(boolean b, Pageable pageable);

	Page<CaseDetails> findByCurrentUserSubDepartmentInOrCurrentUserPimsEmployeeGlobalOrgIdAndActiveForTransferAndCurrentUserSubDepartmentIsNull(
			List<SubDepartment> subDepartmentList, GlobalOrg globalOrgId, boolean b, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndCaseNoContainingIgnoreCaseAndActiveForTransfer(User currentUser,
			String caseno, boolean b, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndActiveForTransfer(User currentUser, boolean b, Pageable pageable);

	List<CaseDetails> findByActiveForTransfer(boolean b);

	Page<CaseDetails> findByCaseNoContainingIgnoreCaseAndIdNotIn(String caseno, List<Long> caseIdList,
			Pageable pageable);

	Page<CaseDetails> findByIdNotIn(List<Long> caseIdList, Pageable pageable);

	Page<CaseDetails> findByCurrentUserSubDepartmentInOrCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentIsNullAndIdNotIn(
			List<SubDepartment> subDepartmentList, GlobalOrg globalOrgId, List<Long> caseIdList, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndCaseNoContainingIgnoreCaseAndIdNotIn(User currentUser, String caseno,
			List<Long> caseIdList, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndIdNotIn(User currentUser, List<Long> caseIdList, Pageable pageable);

	List<CaseDetails> findDistinctIdByEmployeeListInAndIdNotIn(List<EmployeeDetails> empList, List<Long> caseIdx);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndIdNotIn(List<EmployeeDetails> empListx, Pageable pageable,
			List<Long> caseIdx);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
			List<EmployeeDetails> empList, Long valueOf, List<Long> caseIdx);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
			List<EmployeeDetails> empListx, Pageable pageable, Long valueOf, List<Long> caseIdx);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(List<EmployeeDetails> empList,
			List<User> userList, List<Long> caseIdx);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(List<EmployeeDetails> empListx,
			Pageable pageable, List<User> userList, List<Long> caseIdx);

	List<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(List<EmployeeDetails> empList,
			User currentUser, List<Long> caseIdx);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(List<EmployeeDetails> empListx,
			Pageable pageable, User currentUser, List<Long> caseIdx);

	Page<CaseDetails> findByCurrentUserSubDepartmentInAndIdNotIn(List<SubDepartment> subList, Set<Long> caseIdx,
			Pageable pageable);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotInAndSubDepartment(GlobalOrg org,
			Pageable pageable, Set<Long> caseId, SubDepartment subDepartment);

	Page<CaseDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInAndIdNotInAndSubDepartment(GlobalOrg org,
			List<User> userList, Pageable pageable, Set<Long> caseId, SubDepartment subDepartment);

	Page<CaseDetails> findDistinctIdByIdNotIn(Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByIdNotInAndAndGlobalorgGlobalOrgNameContainingIgnoreCase(Pageable pageable,
			Set<Long> caseId, String customsearch);

	Page<CaseDetails> findDistinctIdByCurrentUserAndEmployeeListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
			User currentUser, List<EmployeeDetails> empList, User currentUser2, String search, User currentUser3,
			String search2, User currentUser4, String search3, User currentUser5, String search4, Pageable pageable,
			Set<Long> caseId);

	Page<CaseDetails> findDistinctIdByDivisionId(Long valueOf, Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdAndDivisionId(Long valueOf, Long valueOf2, Pageable pageable);

	Page<CaseDetails> findDistinctIdByMisConductTypeIdAndDivisionId(Long valueOf, Long valueOf2, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionId(Long valueOf, Long valueOf2, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionIdAndMisConductTypeId(Long valueOf, Long valueOf2,
			Long valueOf3, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableId(Long valueOf, Long valueOf2,
			Long valueOf3, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndMisConductTypeId(Long valueOf,
			Long valueOf2, Long valueOf3, Long valueOf4, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(
			Long valueOf, Long valueOf2, Long valueOf3, Long valueOf4, List<EmployeeDetails> empList,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndEmployeeListIn(Long valueOf,
			Long valueOf2, Long valueOf3, List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionIdAndMisConductTypeIdAndEmployeeListIn(Long valueOf,
			Long valueOf2, Long valueOf3, List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByRuleApplicableIdAndDivisionIdAndEmployeeListIn(Long valueOf, Long valueOf2,
			List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByMisConductTypeIdAndDivisionIdAndEmployeeListIn(Long valueOf, Long valueOf2,
			List<EmployeeDetails> empList, Pageable pageable);

	Page<CaseDetails> findDistinctIdByDivisionIdAndEmployeeListIn(Long valueOf, List<EmployeeDetails> empList,
			Pageable pageable);

	Page<CaseDetails> findDistinctIdByGlobalorgIdAndDivisionIdAndEmployeeListIn(Long valueOf, Long valueOf2,
			List<EmployeeDetails> empList, Pageable pageable);


	List<CaseDetails> findByIdIn(List<Long> asList);

	List<CaseDetails> findByIdInAndCurrentUserIn(List<Long> asList, List<User> userList);

	List<CaseDetails> findByIdInAndCurrentUser(List<Long> asList, User currentUser);
	
	
	@Query(value = "SELECT * FROM departmentalenquiry.case_details WHERE cast(case_id as text) like CONCAT('%', :input, '%')",nativeQuery = true)
	List<CaseDetails> findCustomSuper(@Param("input") String input);
	
	
	@Query(value = "SELECT * FROM departmentalenquiry.case_details WHERE cast(case_id as text) like CONCAT('%', :input, '%') and current_user_id in (:userIdList)",nativeQuery = true)
	List<CaseDetails> findByIdInAndCurrentUserInCustom(@Param("input")String input, @Param("userIdList")List<Long> userIdList);
	
	
	@Query(value = "SELECT * FROM departmentalenquiry.case_details WHERE cast(case_id as text) like CONCAT('%', :input, '%') and current_user_id = :id",nativeQuery = true)
	List<CaseDetails> findByIdInAndCurrentUserCustom(@Param("input")String input, @Param("id")Long id);
	
	@Query(value = "SELECT * FROM departmentalenquiry.case_details WHERE cast(case_id as text) like CONCAT('%', :input, '%') and global_org_id = :id",nativeQuery = true)
	List<CaseDetails> findByIdInAndGlobalOrgCustom(@Param("input")String input, @Param("id")Long id);

	Page<CaseDetails> findById(Long valueOf, Pageable pageable);

	Page<CaseDetails> findByCurrentUserAndId(User currentUser, Long valueOf, Pageable pageable);

	Page<CaseDetails> findByIdAndIdNotIn(Long valueOf, Pageable pageable, Set<Long> caseId);

	Page<CaseDetails> findByIdAndIdNotInAndCurrentUserPimsEmployeeGlobalOrgId(Long valueOf, Pageable pageable,
			Set<Long> caseId, GlobalOrg og);

	List<CaseDetails> findByCurrentUserIn(List<User> userList);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserInAndRuleApplicableIdAndIdNotIn(List<User> s, long l,
			Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserInAndRuleApplicableIdNotInAndIdNotIn(List<User> s,
			List<Long> asList, Set<Long> caseId);

	List<CaseDetails> findDistinctIdByCurrentUserInAndEmployeeListInAndIdNotIn(List<User> s,
			List<EmployeeDetails> empLists, Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserInAndRuleApplicableIdAndIdInAndIdNotIn(List<User> s, long l,
			Set<Long> caseListId, Set<Long> caseId);

	List<CaseDetails> findDistinctCasedetailsByCurrentUserInAndRuleApplicableIdNotInAndIdInAndIdNotIn(List<User> s,
			List<Long> asList, Set<Long> caseListId, Set<Long> caseId);

	Page<CaseDetails> findByCurrentUserAndIdNotIn(User currentUser, Set<Long> caseId, Pageable pageable);

	Long countByCurrentUserIn(List<User> userList);

	Long countByCurrentUser(User s);

	Page<CaseDetails> findDistinctIdByEmployeeListInAndIdNotInAndCurrentUserPimsEmployeeGlobalOrgId(
			List<EmployeeDetails> empProList, Pageable pageable, Set<Long> caseId, GlobalOrg org);

	
	

}
