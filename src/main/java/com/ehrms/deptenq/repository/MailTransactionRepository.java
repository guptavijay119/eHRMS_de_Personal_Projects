package com.ehrms.deptenq.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehrms.deptenq.models.MailTransaction;

public interface MailTransactionRepository extends JpaRepository<MailTransaction, Long>{

}
