package com.ehrms.deptenq.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.Service_Group;

@Repository
public interface Service_GroupRepository extends JpaRepository<Service_Group, Long>{

	@Query("select s from Service_Group as s where s.service_group_name in ?1")
	List<Service_Group> findByGroupName(List<String> groupNames);

	List<Service_Group> findByOrderById();

	List<Service_Group> findByIdIn(Set<Long> dtoList);

	List<Service_Group> findByIdInOrderById(Set<Long> dtoList);

	List<Service_Group> findByIdIn(List<Long> groupNames);

	List<Service_Group> findByIdIn(List<Long> asList, Sort by);

	

}
