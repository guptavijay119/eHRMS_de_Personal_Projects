package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.pims.PimsEmployee;

@Repository
public interface PimsEmployeeRepository extends JpaRepository<PimsEmployee, Long>{

	List<PimsEmployee> findByIdNotIn(List<Long> pimsIdList);

	List<PimsEmployee> findByGlobalOrgIdId(Long globalOrg);

}
