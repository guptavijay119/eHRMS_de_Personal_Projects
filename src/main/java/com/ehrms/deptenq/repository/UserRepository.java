package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;

@Repository
//@Transactional(transactionManager = "generalTransactionManager")
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query("select u from User u where email=?1")
	User findByEmail(String email);
	
	@Query("Select u from User u where email=:username")
	 public User getUserDetail(@Param("username") String username);

	//Set<User> findByRolesIn(List<Role> roles);

	//Set<User> findDistinctByRolesIn(List<Role> roles);


	//Set<User> findDistinctByRolesInAndIdNot(List<Role> roles, Long id);
	
	User findByEmailAndPimsEmployeeGlobalOrgId(String email, GlobalOrg globalOrg);

	List<User> findBySubDepartmentIn(List<SubDepartment> subDepartmentList);

	Page<User> findByProcessType(String string, Pageable pageable);

	List<User> findByPimsEmployeeNotNull();

	Page<User> findByProcessTypeAndPimsEmployeeIsNotNull(String string, Pageable pageable);

	List<User> findByPimsEmployeeGlobalOrgId(GlobalOrg org);

	List<User> findByPimsEmployeeGlobalOrgId(GlobalOrg org, Sort by);

	List<User> findByPimsEmployeeGlobalOrgIdAndActiveAndProcessType(GlobalOrg org, boolean b, String string, Sort by);

	List<User> findByPimsEmployeeGlobalOrgIdAndActiveAndProcessTypeAndRolesIn(GlobalOrg org, boolean b, String string,
			List<Role> roleList, Sort by);

	List<User> findByProcessTypeAndPimsEmployeeIsNotNull(String string);

	boolean existsByIdAndProcessType(Long id, String string);


}
