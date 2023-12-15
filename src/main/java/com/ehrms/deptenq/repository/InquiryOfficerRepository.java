package com.ehrms.deptenq.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.InquiryOfficerDetails;
import com.ehrms.deptenq.models.InquiryOfficerList;
import com.ehrms.deptenq.models.User;

@Repository
public interface InquiryOfficerRepository extends JpaRepository<InquiryOfficerDetails, Long>{

	InquiryOfficerDetails findByCasedetailsId(Long caseid);
	
	// added  as per GAD requirements
    Page<InquiryOfficerDetails> findByCasedetailsId(Long id, Pageable pageable);
    
    InquiryOfficerDetails findByEmployeeId(String employeeId);

    InquiryOfficerDetails  findBySevarthId(String sevarthid);

	List<InquiryOfficerDetails> findBySevarthIdIgnoreCase(String input);

	List<InquiryOfficerDetails> findBySevarthIdLikeIgnoreCase(String input);

	InquiryOfficerDetails findByCasedetails(CaseDetails casedetails);

	boolean existsByCasedetails(CaseDetails casedetails);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerInquiryOfficerlistContainingIgnoreCase(String search);

	Page<InquiryOfficerDetails> findByCurrentUser(User currentUser, Pageable pageable);

	Page<InquiryOfficerDetails> findByCasedetailsIdAndCurrentUser(Long caseid, User currentUser, Pageable pageable);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerId(Long valueOf);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerIn(List<InquiryOfficerList> ioList);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerInAndInquiryReportSubmitted(List<InquiryOfficerList> ioList,
			boolean b);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerIdAndInquiryReportSubmitted(long l, boolean b);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserIn(long l,
			boolean b, List<User> userList);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsCurrentUserIn(
			List<InquiryOfficerList> findByIdIn, boolean b, List<User> userList);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUser(long l,
			boolean b, User currentUser);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsCurrentUser(
			List<InquiryOfficerList> findByIdIn, boolean b, User currentUser);

	List<InquiryOfficerDetails> findByInquiryReportSubmitted(boolean b);

	List<InquiryOfficerDetails> findByInquiryReportSubmittedAndCasedetailsIsNotNull(boolean b);

	Page<InquiryOfficerDetails> findByCurrentUserIn(List<User> userList, Pageable pageable);

	Page<InquiryOfficerDetails> findByCasedetailsIdAndCurrentUserIn(Long caseid, List<User> userList,
			Pageable pageable);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgId(
			List<InquiryOfficerList> ioallList, boolean b, GlobalOrg org);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgId(
			long l, boolean b, GlobalOrg org);

	Page<InquiryOfficerDetails> findByCurrentUserPimsEmployeeGlobalOrgId(GlobalOrg globalOrgId, Pageable pageable);

	Page<InquiryOfficerDetails> findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsId(GlobalOrg globalOrgId,
			Long caseid, Pageable pageable);

	List<InquiryOfficerDetails> findByCasedetailsIsNotNull();

	List<InquiryOfficerDetails> findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsPendingWithNotIn(
			List<InquiryOfficerList> ioallList, boolean b, List<String> asList);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsPendingWithNotIn(
			long l, boolean b, List<String> asList);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsPendingWithNotIn(
			List<InquiryOfficerList> ioallList, boolean b, GlobalOrg org, List<String> asList);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsPendingWithNotIn(
			long l, boolean b, GlobalOrg org, List<String> asList);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsCurrentUserInAndCasedetailsPendingWithNotIn(
			List<InquiryOfficerList> ioallList, boolean b, List<User> userList, List<String> asList);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserInAndCasedetailsPendingWithNotIn(
			long l, boolean b, List<User> userList, List<String> asList);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsCurrentUserAndCasedetailsPendingWithNotIn(
			List<InquiryOfficerList> ioallList, boolean b, User currentUser, List<String> asList);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserAndCasedetailsPendingWithNotIn(
			long l, boolean b, User currentUser, List<String> asList);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsPendingWithNotInAndAndCasedetailsPendingWithIsNotNullAndAndCasedetailsPendingWithIsNot(
			long l, boolean b, List<String> asList, String string);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsPendingWithNotInAndAndCasedetailsPendingWithIsNotNullAndAndCasedetailsPendingWithIsNotAndCasedetailsPendingWithIsNot(
			long l, boolean b, List<String> asList, String string, String string2);

	List<InquiryOfficerDetails> findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsPendingWithNot(
			long l, boolean b, String string);
	
//	List<EmployeeDetails>  findByCasedetailsId(Long caseid);
//	EmployeeDetails  findByCasedetailsId(Long caseid);
//	Page<EmployeeDetails> findByCasedetailsId(Long id, Pageable pageable);
	//Page<CaseDetails>  findAll(Pageable pageable);

}
