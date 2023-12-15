package com.ehrms.deptenq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.GradePay;

@Repository
public interface GradePayRepository extends JpaRepository<GradePay, Long>{

}
