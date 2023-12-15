package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehrms.deptenq.models.MisconductTypesMaster;
import com.ehrms.deptenq.models.RulesApplicableMaster;

public interface MisconductTypesMasterRepository extends JpaRepository<MisconductTypesMaster, Long> {

	
	List<MisconductTypesMaster> findByMisconductNameContainingIgnoreCase(String input);

	List<MisconductTypesMaster> findByIdIn(List<Long> asList);

//	List<com.ehrms.deptenq.controller.MisconductTypesMaster> findByMisconductNameContainingIgnoreCase(String input);
}
