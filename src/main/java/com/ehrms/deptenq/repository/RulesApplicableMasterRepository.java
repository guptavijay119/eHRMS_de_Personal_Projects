package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ehrms.deptenq.models.RulesApplicableMaster;

public interface RulesApplicableMasterRepository extends JpaRepository<RulesApplicableMaster, Long> {

	List<RulesApplicableMaster> findByRuleNameContainingIgnoreCase(String input);

	List<RulesApplicableMaster> findByIdNotIn(List<Long> asList);

	List<RulesApplicableMaster> findByActive(boolean b);

	List<RulesApplicableMaster> findByActive(boolean b, Sort by);

	/* List<RulesApplicableMaster> findByOrderById(); */

}
