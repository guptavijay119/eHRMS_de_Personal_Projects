package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>{

	Department findByDepartmentCode(String departmentCode);
	
	Department findByDepartmentName(String name);
	
	List<Department> findByDepartmentCodeNotNull();

}
