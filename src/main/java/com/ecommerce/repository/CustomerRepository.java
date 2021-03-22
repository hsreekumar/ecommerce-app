package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.entity.CustomerEntity;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> {
	
	public List<CustomerEntity> findByFirstname(String item);
	
	public List<CustomerEntity> findByLastname(String item);
	
	public CustomerEntity findByCustomerId(Integer customerId);
}
