package com.ehrms.deptenq.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.ReasonForKeepingCaseAbeyance;

@Repository
public interface ReasonForKeepingCaseAbeyanceRepository extends JpaRepository<ReasonForKeepingCaseAbeyance, Long>{

	List<ReasonForKeepingCaseAbeyance> findByActive(boolean b);

	//InquiryOfficerDetails findByCasedetailsId(Long caseid);
	
	//Page<CaseDetails>  findAll(Pageable pageable);

}
