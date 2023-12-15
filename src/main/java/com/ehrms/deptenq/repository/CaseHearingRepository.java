package com.ehrms.deptenq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.CaseHearing;
import com.ehrms.deptenq.models.IORequestTransaction;

@Repository
public interface CaseHearingRepository extends JpaRepository<CaseHearing, Long>{

	boolean existsByCasedetailsAndIo(CaseDetails casedetails, IORequestTransaction io);

	CaseHearing findByCasedetailsAndIo(CaseDetails casedetails, IORequestTransaction io);

}
