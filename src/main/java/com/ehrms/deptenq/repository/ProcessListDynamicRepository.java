package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehrms.deptenq.models.ModuleName;
import com.ehrms.deptenq.models.ProcessListDynamic;
import com.ehrms.deptenq.models.ProcessName;
import com.ehrms.deptenq.models.Role;

public interface ProcessListDynamicRepository extends JpaRepository<ProcessListDynamic, Long>{

	List<ProcessListDynamic> findByRoleIn(List<Role> findByNameIn);

	List<ProcessListDynamic> findByRoleInOrderById(List<Role> collect);

	List<ProcessListDynamic> findByProcessNameAndRoleInOrderById(ProcessName processName, List<Role> collect);

	List<ProcessListDynamic> findByProcessNameIsNullAndRoleInOrderById(List<Role> collect);

	List<ProcessListDynamic> findDistinctByRoleInOrderById(List<Role> collect);



	List<ProcessListDynamic> findByProcessNameIsNullAndModuleNameAndRoleInOrderById(ModuleName orElse,
			List<Role> collect);
	
}
