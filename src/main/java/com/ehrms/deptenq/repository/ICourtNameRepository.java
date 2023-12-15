package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.CourtName;
import com.ehrms.deptenq.models.InquiryOfficerList;

@Repository
public interface ICourtNameRepository extends JpaRepository<CourtName, Long> {
	
	List<CourtName> findByOrderById();
	
	
	
	




}
