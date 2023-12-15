package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.NoDECertificate;

@Repository
public interface NoDeCertificateRepository extends JpaRepository<NoDECertificate, Long>{

	Page<NoDECertificate> findByOrderById(PageRequest pageable);

	List<NoDECertificate> findByEmployeeNameContainingIgnoreCase(String input);

	Page<NoDECertificate> findByEmployeeName(String employeename, PageRequest pageable);

}
