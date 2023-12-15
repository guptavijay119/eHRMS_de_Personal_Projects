package com.ehrms.deptenq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.HearingParticipants;

@Repository
public interface HearingParticipantsRepository extends JpaRepository<HearingParticipants, Long>{

}
