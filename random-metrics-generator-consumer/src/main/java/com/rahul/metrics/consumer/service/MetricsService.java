package com.rahul.metrics.consumer.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.rahul.metrics.consumer.model.MetricsRequest;

public interface MetricsService {
	
	void createRecord(List<MetricsRequest> request) throws JsonParseException, JsonMappingException, IOException;

}
