package com.ehrms.deptenq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.WitnessData;

@Repository
public interface WitnessDataRepository extends JpaRepository<WitnessData, Long>{

}
