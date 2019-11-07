package com.rahul.metrics.consumer.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.metrics.consumer.entity.MetricsEntity;
import com.rahul.metrics.consumer.model.MetricsRequest;
import com.rahul.metrics.consumer.repository.ServiceRepository;
import com.rahul.metrics.consumer.service.MetricsService;

@Service
public class MetricsServiceImpl implements MetricsService{
	
	
	private ServiceRepository serviceRepository;

	
	private ObjectMapper ObjectMapper= new ObjectMapper();
	
	@Autowired
	public MetricsServiceImpl(ServiceRepository serviceRepository) {
		super();
		this.serviceRepository = serviceRepository;
	}




	@Override
	public void createRecord(List<MetricsRequest> request)throws JsonParseException, JsonMappingException, IOException {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		List<MetricsEntity> userEntityList= new ArrayList<MetricsEntity>();
		for(MetricsRequest data:request)
		{
		MetricsEntity metricsEntity = mapper.map(data, MetricsEntity.class);
		metricsEntity.setData(ObjectMapper.writeValueAsString(data.getData()));
		userEntityList.add(metricsEntity);
		}
		serviceRepository.saveAll(userEntityList);
	}

}
