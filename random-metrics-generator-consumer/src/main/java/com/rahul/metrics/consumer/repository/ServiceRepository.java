package com.rahul.metrics.consumer.repository;

import org.springframework.data.repository.CrudRepository;

import com.rahul.metrics.consumer.entity.MetricsEntity;


public interface ServiceRepository extends CrudRepository<MetricsEntity, Long>{

}
