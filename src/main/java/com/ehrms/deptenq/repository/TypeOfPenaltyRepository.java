package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.TypeOfPenaltyInflictedPojo;

@Repository
public interface TypeOfPenaltyRepository extends JpaRepository<TypeOfPenaltyInflictedPojo, Long> {

	List<TypeOfPenaltyInflictedPojo> findByActive(boolean b);
	




}
