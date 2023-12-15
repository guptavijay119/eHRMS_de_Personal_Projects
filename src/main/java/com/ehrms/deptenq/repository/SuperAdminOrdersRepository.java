package com.ehrms.deptenq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.SuperAdminOrders;

@Repository
public interface SuperAdminOrdersRepository extends JpaRepository<SuperAdminOrders, Long>{

}
