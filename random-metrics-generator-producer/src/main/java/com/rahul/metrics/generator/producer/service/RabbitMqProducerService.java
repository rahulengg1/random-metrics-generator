package com.rahul.metrics.generator.producer.service;

import java.util.List;

import com.rahul.metrics.generator.producer.model.MetricsRequest;
import com.rahul.metrics.generator.producer.model.RandomPayloadRequest;

public interface RabbitMqProducerService {
	void sendMessage(List<MetricsRequest> request);
	void generateMetrics(RandomPayloadRequest request);
}
