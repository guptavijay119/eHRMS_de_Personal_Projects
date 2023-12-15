package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.Gender;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Long>{

	@Query("select g from Gender g where g.genderName =?1 order by g.id")
	Gender findByGenderName(String gender);

	List<Gender> findByOrderById();

	List<Gender> findByIdIn(List<Long> asList);

	List<Gender> findByActiveOrderById(boolean b);
}
