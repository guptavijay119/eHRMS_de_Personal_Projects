package com.ehrms.deptenq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.Payband;

@Repository
public interface PayBandRepository extends JpaRepository<Payband, Long>{

}
