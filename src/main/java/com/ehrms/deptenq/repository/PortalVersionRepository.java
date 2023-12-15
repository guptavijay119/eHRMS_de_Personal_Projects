package com.ehrms.deptenq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.PortalVersion;

@Repository
public interface PortalVersionRepository extends JpaRepository<PortalVersion, Long>{

}
