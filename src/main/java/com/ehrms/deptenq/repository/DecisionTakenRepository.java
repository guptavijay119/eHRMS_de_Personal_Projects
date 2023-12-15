package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.DecisionTakenPojo;

@Repository
public interface DecisionTakenRepository extends JpaRepository<DecisionTakenPojo, Long> {

	List<DecisionTakenPojo> findByActive(boolean b);

	List<DecisionTakenPojo> findByActive(boolean b, Sort by);
	




}
