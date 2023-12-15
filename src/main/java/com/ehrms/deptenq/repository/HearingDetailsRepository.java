package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.HearingDetails;

@Repository
public interface HearingDetailsRepository extends JpaRepository<HearingDetails, Long>{

	List<HearingDetails> findByHearingStatus(String string, Sort by);

}
