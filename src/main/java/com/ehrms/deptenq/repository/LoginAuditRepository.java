package com.ehrms.deptenq.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehrms.deptenq.models.LoginAudit;

public interface LoginAuditRepository extends JpaRepository<LoginAudit, Long>{


	long countDistinctEmailBySuccess(boolean b);

	long countDistinctEmailBySuccessAndEmail(boolean b, String email);



}
