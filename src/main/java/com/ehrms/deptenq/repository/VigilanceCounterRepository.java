package com.ehrms.deptenq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.VigilanceCertificateCounter;

@Repository
public interface VigilanceCounterRepository extends JpaRepository<VigilanceCertificateCounter, Long>{

	boolean existsByOrg(GlobalOrg globalOrgId);

	VigilanceCertificateCounter findByOrg(GlobalOrg globalOrgId);

}
