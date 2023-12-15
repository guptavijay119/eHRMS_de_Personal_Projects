package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.SubDepartment;

@Repository
public interface SubDepartmentRepository extends JpaRepository<SubDepartment, Long>{

	List<SubDepartment> findByOrderById();

	List<SubDepartment> findByGlobalorg(GlobalOrg globalOrgId);

	List<SubDepartment> findByActive(boolean b);

	Page<SubDepartment> findByActive(boolean b, Pageable pageable);

	SubDepartment findBySubdepartmentNameEn(String search);

	List<SubDepartment> findByGlobalorgAndActive(GlobalOrg globalOrgId, boolean b);

	List<SubDepartment> findByGlobalorgIdAndActive(Long valueOf, boolean b);

}
