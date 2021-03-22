package com.ecommerce.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.entity.ItemEntity;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long> {
	
}
