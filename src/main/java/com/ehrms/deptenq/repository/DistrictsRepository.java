package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.Districts;
import com.ehrms.deptenq.models.States;

@Repository
public interface DistrictsRepository extends JpaRepository<Districts , Long> {


	List<Districts> findByOrderByDistrictName();

	List<Districts> findByState(States states);

	List<Districts> findByStateOrderByDistrictName(States states);

	Districts findByDistrictName(String stringCellValue);

	List<Districts> findByDistrictNameIgnoreCase(String stringCellValue);

	List<Districts> findByState_StateCode(Long valueOf);

	List<Districts> findByStateStateCode(long l);

	List<Districts> findByState_StateCodeOrderByDistrictName(Long valueOf);


	List<Districts> findByDivisionIdId(Long valueOf);

	List<Districts> findByDivisionIdIdAndActive(Long valueOf, boolean b);

	List<Districts> findByDivisionIdIdAndActive(Long valueOf, boolean b, Sort by);

	

}
