package com.ehrms.deptenq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.DErules;

@Repository
public interface DERulesRepository extends JpaRepository<DErules, Long> {

}
