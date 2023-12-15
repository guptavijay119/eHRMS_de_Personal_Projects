package com.ehrms.deptenq.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehrms.deptenq.models.Role;

public interface RolesRepository extends JpaRepository<Role, Long>{

	List<Role> findByNameIn(List<String> roleList);

	Set<Role> findByIdIn(List<Long> asList);

}
