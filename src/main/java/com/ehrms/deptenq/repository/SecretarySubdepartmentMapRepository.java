package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.SecretarySubdepartmentMap;

@Repository
public interface SecretarySubdepartmentMapRepository extends JpaRepository<SecretarySubdepartmentMap, Long>{

	List<SecretarySubdepartmentMap> findByUsersecId(Long valueOf);

	boolean existsBySubDepartmentIdAndUsersecId(Long subdepartmentid, Long id);

	SecretarySubdepartmentMap findBySubDepartmentIdAndUsersecId(Long subdepartmentid, Long id);

}
