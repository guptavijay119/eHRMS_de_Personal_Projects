package com.ehrms.deptenq.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.DetailsKeptAbeyanceCases;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;

@Repository
public interface DetailsKeptAbeyanceCasesRepository extends JpaRepository<DetailsKeptAbeyanceCases, Long>{

	DetailsKeptAbeyanceCases findByCasedetails(CaseDetails findByCaseNo);
	
              //	 added  as per GAD requirements
    Page<DetailsKeptAbeyanceCases> findByCasedetailsId(Long id, Pageable pageable);
    
    DetailsKeptAbeyanceCases findByEmployeeId(String employeeId);

    DetailsKeptAbeyanceCases  findBySevarthId(String sevarthid);

	List<DetailsKeptAbeyanceCases> findBySevarthIdIgnoreCase(String input);

	List<DetailsKeptAbeyanceCases> findBySevarthIdLikeIgnoreCase(String input);

	//DetailsKeptAbeyanceCases findByCasedetails(CaseDetails casedetails);

	boolean existsByCasedetails(CaseDetails casedetails);

	List<DetailsKeptAbeyanceCases> findByCasedetailsId(Long caseid);

	Page<DetailsKeptAbeyanceCases> findByCurrentUser(User currentUser, Pageable pageable);

	Page<DetailsKeptAbeyanceCases> findByCasedetailsIdAndCurrentUser(Long caseid, User currentUser, Pageable pageable);

	long countByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNull(GlobalOrg s);

	List<DetailsKeptAbeyanceCases> findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNull(GlobalOrg s);

	List<DetailsKeptAbeyanceCases> findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(
			GlobalOrg s, List<EmployeeDetails> empLists);

	List<DetailsKeptAbeyanceCases> findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdIn(
			GlobalOrg s, Set<Long> caseListId);

	List<DetailsKeptAbeyanceCases> findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNull(
			GlobalOrg s);

	List<DetailsKeptAbeyanceCases> findDistinctCasedetailsByCurrentUserSubDepartmentAndCasedetailsIsNotNull(
			SubDepartment s);

	boolean existsByCasedetailsAndEmpDataId(CaseDetails casedetails, Long valueOf);

	DetailsKeptAbeyanceCases findByCasedetailsAndEmpDataId(CaseDetails casedetails, Long valueOf);
	
	
	

}
