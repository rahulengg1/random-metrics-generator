package com.rahul.metrics.generator.producer.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.rahul.metrics.generator.producer.model.MetricsRequest;
import com.rahul.metrics.generator.producer.model.RandomPayloadRequest;
import com.rahul.metrics.generator.producer.service.RabbitMqProducerService;
import com.rahul.metrics.generator.producer.shared.ProducerProperties;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RabbitMqProducerServiceImpl implements RabbitMqProducerService {

	private final RabbitTemplate rabbitTemplate;

	private final ProducerProperties producerProperties;

	@Override
	public void sendMessage(List<MetricsRequest> request) {
		rabbitTemplate.convertAndSend(producerProperties.getExchange(), producerProperties.getRouting(), request);
	}

	@Override
	public void generateMetrics(RandomPayloadRequest request) {

		int counter = request.getTotalPayloadRequest();

		List<MetricsRequest> metricsRequestList = new ArrayList<MetricsRequest>();
		MetricsRequest metricsRequest = new MetricsRequest();

		
		
		Map<String, String> metricsData = new HashMap<String, String>();
		List<Map<String, String>> metricsDataList = new ArrayList<Map<String, String>>();
		
		DecimalFormat twoDecimalPlace = new DecimalFormat("0.00");
		
		
		while (counter > 0) {
			metricsRequest.setLat(ThreadLocalRandom.current().nextDouble(Double.MIN_VALUE, Double.MAX_VALUE));
			metricsRequest.setLon(ThreadLocalRandom.current().nextDouble(Double.MIN_VALUE, Double.MAX_VALUE));

			if (counter % 2 == 0) {
				metricsRequest.setMetricsType("Temperatue");
				metricsData.put("max", twoDecimalPlace.format(ThreadLocalRandom.current().nextDouble(-100, 100)));
				metricsData.put("min", twoDecimalPlace.format(ThreadLocalRandom.current().nextDouble(-100, 100)));
				metricsDataList.add(metricsData);
			} else {
				metricsRequest.setMetricsType("Speed");
				metricsData.put("avg", twoDecimalPlace.format(ThreadLocalRandom.current().nextDouble(1, 200)));
				metricsDataList.add(metricsData);
			}
			metricsRequest.setData(metricsDataList);
			metricsRequestList.add(metricsRequest);
			rabbitTemplate.convertAndSend(producerProperties.getExchange(), producerProperties.getRouting(), metricsRequestList);

			
			counter--;
			
			metricsRequestList = new ArrayList<MetricsRequest>();
			metricsRequest = new MetricsRequest();
			metricsData = new HashMap<String, String>();
			metricsDataList = new ArrayList<Map<String, String>>();
			
		}

	}

}
