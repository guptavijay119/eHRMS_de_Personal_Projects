package com.ehrms.deptenq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.PayCommission;

@Repository
public interface PayCommissionRepository extends JpaRepository<PayCommission, Long>{

}
