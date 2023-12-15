package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.models.UsersProcess;

@Repository
public interface UserProcessRepository extends JpaRepository<UsersProcess, Long>{

	List<UsersProcess> findByUser(User user);

}
