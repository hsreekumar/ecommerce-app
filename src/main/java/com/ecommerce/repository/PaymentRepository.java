package com.ecommerce.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.entity.PaymentEntity;

@Repository
public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
	
}
