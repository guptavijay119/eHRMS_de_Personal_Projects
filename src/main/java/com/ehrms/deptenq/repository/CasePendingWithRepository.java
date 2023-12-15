package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.CasePendingWithPojo;

@Repository
public interface CasePendingWithRepository extends JpaRepository<CasePendingWithPojo, Long> {

	List<CasePendingWithPojo> findByActive(boolean b);
	




}
