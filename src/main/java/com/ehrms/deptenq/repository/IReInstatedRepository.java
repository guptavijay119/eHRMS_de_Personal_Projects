package com.ehrms.deptenq.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.ChargesheetDetails;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.ReInstatedDetails;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;

@Repository
public interface IReInstatedRepository extends JpaRepository<ReInstatedDetails, Long>{

	List<ReInstatedDetails> findByCasedetailsId(Long caseid);
	
//	ReInstatedDetails  findByCasedetailsId(Long caseid);
	//Page<CaseDetails>  findAll(Pageable pageable);
	
	Page<ReInstatedDetails> findByCasedetailsId(Long id, Pageable pageable);
	
	
	// added by vijay as per pdf requirements
	
	ReInstatedDetails findByEmployeeId(String employeeId);

//	List<EmployeeDetails>  findByCasedetailsId(Long caseid);
//	EmployeeDetails  findByCasedetailsId(Long caseid);

//	Page<EmployeeDetails> findByCasedetailsId(Long id, Pageable pageable);

	ReInstatedDetails  findBySevarthId(String sevarthid);

	List<ReInstatedDetails> findBySevarthIdIgnoreCase(String input);

	List<ReInstatedDetails> findBySevarthIdLikeIgnoreCase(String input);

	ReInstatedDetails findByCasedetails(CaseDetails casedetails);

	boolean existsByCasedetails(CaseDetails casedetails);

	List<ReInstatedDetails> findByCasedetailsCaseNo(String string);

	Page<ReInstatedDetails> findByOrderById(Pageable pageable);

	boolean existsByCasedetailsAndSevarthId(CaseDetails cd, String sevarthId);

	ReInstatedDetails findByCasedetailsAndSevarthId(CaseDetails cd, String sevarthId);

	List<ReInstatedDetails> findByCasedetailsCaseNoAndGlobalorg(String string, GlobalOrg globalOrgId);

	List<ReInstatedDetails> findByCasedetailsCaseNoAndCurrentUser(String caseid, User currentUser);

	Page<ReInstatedDetails> findByCurrentUserOrderById(User currentUser, Pageable pageable);

	Page<ReInstatedDetails> findByCasedetailsIdAndCurrentUser(Long caseid, User currentUser, Pageable pageable);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeReInstated(GlobalOrg s, boolean b);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeReInstatedAndCasedetailsIsNotNull(GlobalOrg s, boolean b);

	Page<ReInstatedDetails> findByCurrentUser(User currentUser, Pageable pageable);

	Page<ReInstatedDetails> findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndEmployeeReInstated(
			User currentUser, String search, User currentUser2, String search2, User currentUser3, String search3,
			User currentUser4, String search4, User currentUser5, String search5, User currentUser6,
			Boolean booleanObject, Pageable pageable);

	Page<ReInstatedDetails> findByEmployeeReInstatedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUser(
			Boolean booleanObject, String string, User currentUser, Pageable pageable);

	Page<ReInstatedDetails> findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndReInstatedOrderDateOrCurrentUserAndEmployeeReInstated(
			User currentUser, String search, User currentUser2, String search2, User currentUser3, String search3,
			User currentUser4, String search4, User currentUser5, String search5, User currentUser6, LocalDate date,
			User currentUser7, Boolean booleanObject, Pageable pageable);

	Page<ReInstatedDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmployeeReInstated(
			String search, String search2, String search3, String search4, String search5, Boolean booleanObject,
			Pageable pageable);

	Page<ReInstatedDetails> findByEmployeeReInstatedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
			Boolean booleanObject, String string, Pageable pageable);

	Page<ReInstatedDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrReInstatedOrderDateOrEmployeeReInstated(
			String search, String search2, String search3, String search4, String search5, LocalDate date,
			Boolean booleanObject, Pageable pageable);

	Page<ReInstatedDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrReInstatedOrderDateOrEmployeeReInstatedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCase(
			String search, String search2, String search3, String search4, String search5, LocalDate date,
			Boolean booleanObject, String search6, Pageable pageable);

	Page<ReInstatedDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmployeeReInstatedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCase(
			String search, String search2, String search3, String search4, String search5, Boolean booleanObject,
			String search6, Pageable pageable);

	Page<ReInstatedDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmployeeReInstatedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
			String search, String search2, String search3, String search4, String search5, Boolean booleanObject,
			String search6, String search7, Pageable pageable);

	Page<ReInstatedDetails> findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrReInstatedOrderDateOrEmployeeReInstatedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
			String search, String search2, String search3, String search4, String search5, LocalDate date,
			Boolean booleanObject, String search6, String search7, Pageable pageable);

	Page<ReInstatedDetails> findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndEmployeeReInstatedOrCurrentUserAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
			User currentUser, String search, User currentUser2, String search2, User currentUser3, String search3,
			User currentUser4, String search4, User currentUser5, String search5, User currentUser6,
			Boolean booleanObject, User currentUser7, String search6, User currentUser8, String search7,
			Pageable pageable);

	Page<ReInstatedDetails> findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndReInstatedOrderDateOrCurrentUserAndEmployeeReInstatedOrCurrentUserAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
			User currentUser, String search, User currentUser2, String search2, User currentUser3, String search3,
			User currentUser4, String search4, User currentUser5, String search5, User currentUser6, LocalDate date,
			User currentUser7, Boolean booleanObject, User currentUser8, String search6, User currentUser9,
			String search7, Pageable pageable);

	Page<ReInstatedDetails> findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndSevarthIdContainingIgnoreCaseOrCurrentUserInAndFirstNameContainingIgnoreCaseOrCurrentUserInAndMiddleNameContainingIgnoreCaseOrCurrentUserInAndLastNameContainingIgnoreCaseOrCurrentUserInAndReInstatedOrderDateOrCurrentUserInAndEmployeeReInstatedOrCurrentUserInAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
			List<User> userList, String search, List<User> userList2, String search2, List<User> userList3,
			String search3, List<User> userList4, String search4, List<User> userList5, String search5,
			List<User> userList6, LocalDate date, List<User> userList7, Boolean booleanObject, List<User> userList8,
			String search6, List<User> userList9, String search7, Pageable pageable);

	Page<ReInstatedDetails> findByEmployeeReInstatedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUserIn(
			Boolean booleanObject, String string, List<User> userList, Pageable pageable);

	Page<ReInstatedDetails> findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndSevarthIdContainingIgnoreCaseOrCurrentUserInAndFirstNameContainingIgnoreCaseOrCurrentUserInAndMiddleNameContainingIgnoreCaseOrCurrentUserInAndLastNameContainingIgnoreCaseOrCurrentUserInAndEmployeeReInstatedOrCurrentUserInAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
			List<User> userList, String search, List<User> userList2, String search2, List<User> userList3,
			String search3, List<User> userList4, String search4, List<User> userList5, String search5,
			List<User> userList6, Boolean booleanObject, List<User> userList7, String search6, List<User> userList8,
			String search7, Pageable pageable);

	Page<ReInstatedDetails> findByCurrentUserIn(List<User> userList, Pageable pageable);

	List<ReInstatedDetails> findByCurrentUser(User currentUser);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeReInstatedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(
			GlobalOrg s, boolean b, List<EmployeeDetails> empLists);

	long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeReInstatedAndCasedetailsIsNotNull(
			GlobalOrg s, boolean b);

	long countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeReInstatedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(
			GlobalOrg s, boolean b, List<EmployeeDetails> empLists);
	Page<ReInstatedDetails> findByFileNoIsNotNullAndFileNoIsNot(String string, Pageable pageable);

	Page<ReInstatedDetails> findByCurrentUserAndFileNoIsNotNullAndFileNoIsNot(User currentUser, String string,
			Pageable pageable);

	long countDistinctCasedetailsByCurrentUserSubDepartmentAndEmployeeReInstatedAndCasedetailsIsNotNull(SubDepartment s,
			boolean b);

	Page<ReInstatedDetails> findByFileNoIsNotNullAndFileNoIsNotEmpty(Pageable pageable);

	Page<ReInstatedDetails> findByCasedetailsIdInAndCurrentUser(List<Long> asList, User currentUser, Pageable pageable);

	List<ReInstatedDetails> findByEmployeeReInstatedAndCasedetailsIsNotNull(boolean b);

	List<ReInstatedDetails> findByEmployeeReInstatedAndCasedetailsIsNotNullAndCurrentUserPimsEmployeeGlobalOrgId(
			boolean b, GlobalOrg s);

	boolean existsByCasedetailsAndEmpDataId(CaseDetails casedetails, Long valueOf);

	ReInstatedDetails findByCasedetailsAndEmpDataId(CaseDetails casedetails, Long valueOf);

	Page<ReInstatedDetails> findByEmployeeReInstatedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(boolean b,
			String string, Pageable pageable);

	Page<ReInstatedDetails> findByEmployeeReInstatedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserIn(
			boolean b, String string, Pageable pageable, List<User> userList);

	Page<ReInstatedDetails> findByEmployeeReInstatedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(
			boolean b, String string, Pageable pageable, User currentUser);

	Page<ReInstatedDetails> findByFileNoIsNotNullAndFileNoIsNotAndCasedetailsIsNull(String string, Pageable pageable);

	Page<ReInstatedDetails> findByCurrentUserAndFileNoIsNotNullAndFileNoIsNotAndCasedetailsIsNull(User currentUser,
			String string, Pageable pageable);



	Page<ReInstatedDetails> findByCasedetailsIdInAndCurrentUserIn(List<Long> asList, List<User> userList,
			Pageable pageable);

	Page<ReInstatedDetails> findByEmployeeReInstatedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgId(
			boolean b, String string, GlobalOrg org, Pageable pageable);

	Page<ReInstatedDetails> findByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg globalOrgId, Pageable pageable);

	Page<ReInstatedDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsId(GlobalOrg globalOrgId, Long caseid,
			Pageable pageable);


//	ChargesheetDetails findByCaseDetails(CaseDetails caseDetails);

	
	

}
