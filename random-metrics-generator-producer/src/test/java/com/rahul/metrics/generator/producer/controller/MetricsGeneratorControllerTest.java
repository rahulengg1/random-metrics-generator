package com.rahul.metrics.generator.producer.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.rahul.metrics.generator.producer.model.MetricsRequest;
import com.rahul.metrics.generator.producer.service.impl.RabbitMqProducerServiceImpl;
import com.rahul.metrics.generator.producer.shared.ProducerProperties;

@SpringBootTest
@SpringJUnitConfig
@DirtiesContext
@ConfigurationProperties
class MetricsGeneratorControllerTest {

	@Autowired
	MetricsGeneratorController metricsGeneratorController;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private Queue queue1;

	@Autowired
	private RabbitListenerTestHarness harness;

	private ProducerProperties producerProperties;

	private RabbitMqProducerServiceImpl rabbitMqProducerServiceImpl;

	@BeforeEach
	public void setup() {
		producerProperties = new ProducerProperties();
		producerProperties.setExchange("");
		producerProperties.setRouting(queue1.getName());
		rabbitMqProducerServiceImpl = new RabbitMqProducerServiceImpl(rabbitTemplate, producerProperties);
		
		metricsGeneratorController = new MetricsGeneratorController(rabbitMqProducerServiceImpl);

	}

	@Test
	void testAddRecord() throws Exception {
		List<MetricsRequest> metricsRequestList = new ArrayList<MetricsRequest>();
		MetricsRequest metricsRequest = new MetricsRequest();

		Map<Object, Object> metricsData = new HashMap<Object, Object>();
		List<Object> metricsDataList = new ArrayList<Object>();

		DecimalFormat twoDecimalPlace = new DecimalFormat("0.00");

		metricsRequest.setLat(ThreadLocalRandom.current().nextDouble(Double.MIN_VALUE, Double.MAX_VALUE));
		metricsRequest.setLon(ThreadLocalRandom.current().nextDouble(Double.MIN_VALUE, Double.MAX_VALUE));

		metricsRequest.setMetricsType("Temperatue");
		metricsData.put("max", twoDecimalPlace.format(ThreadLocalRandom.current().nextDouble(-100, 100)));
		metricsData.put("min", twoDecimalPlace.format(ThreadLocalRandom.current().nextDouble(-100, 100)));
		metricsDataList.add(metricsData);

		metricsRequest.setData(metricsDataList);
		metricsRequestList.add(metricsRequest);

		metricsGeneratorController.addRecord(metricsRequestList);

		RabbitListenerTestHarness.InvocationData invocationData = this.harness.getNextInvocationDataFor("listener", 10,
				TimeUnit.SECONDS);
		assertNotNull(invocationData);
		@SuppressWarnings("unchecked")
		List<MetricsRequest> data = (List<MetricsRequest>) invocationData.getArguments()[0];
		assertTrue(data.size() == 1);

		for (MetricsRequest request : data) {
			assertEquals(request.getLat(), metricsRequest.getLat());
			assertEquals(request.getLon(), metricsRequest.getLon());
			assertEquals(request.getMetricsType(), metricsRequest.getMetricsType());
		}
	}

	@TestConfiguration
	@RabbitListenerTest(spy = false, capture = true)
	public static class Config {

		@Bean
		public Queue queue1() {
			return new AnonymousQueue();
		}

		@RabbitListener(id = "listener", queues = "#{queue1.name}")
		public List<MetricsRequest> Listener(List<MetricsRequest> listener) {
			return listener;
		}

	}

}
