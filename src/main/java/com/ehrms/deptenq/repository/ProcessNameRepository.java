package com.ehrms.deptenq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ProcessNameRepository extends JpaRepository<com.ehrms.deptenq.models.ProcessName, Long>{

}
