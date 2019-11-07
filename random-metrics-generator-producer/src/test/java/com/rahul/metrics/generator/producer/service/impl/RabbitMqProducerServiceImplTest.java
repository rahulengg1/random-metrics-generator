package com.rahul.metrics.generator.producer.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import com.rahul.metrics.generator.producer.model.MetricsRequest;
import com.rahul.metrics.generator.producer.model.RandomPayloadRequest;
import com.rahul.metrics.generator.producer.shared.ProducerProperties;

@SpringBootTest
@DirtiesContext
@SpringJUnitWebConfig
@SpringJUnitConfig
class RabbitMqProducerServiceImplTest {

	private RabbitMqProducerServiceImpl rabbitMqProducerServiceImpl;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private ProducerProperties producerProperties;

	@Autowired
	private Queue queue1;

	@Autowired
	private RabbitListenerTestHarness harness;

	@BeforeEach
	public void setup() {
		producerProperties = new ProducerProperties();
		producerProperties.setExchange("");
		producerProperties.setRouting(queue1.getName());
		rabbitMqProducerServiceImpl = new RabbitMqProducerServiceImpl(rabbitTemplate, producerProperties);

	}

	@Test
	void testSendMessage() throws Exception {

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

		rabbitMqProducerServiceImpl.sendMessage(metricsRequestList);
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

	@Test
	void testGenerateMetrics() throws Exception {
		RandomPayloadRequest request = new RandomPayloadRequest();
		request.setTotalPayloadRequest(10);
		rabbitMqProducerServiceImpl.generateMetrics(request);

		RabbitListenerTestHarness.InvocationData invocationData = this.harness.getNextInvocationDataFor("listener", 10,
				TimeUnit.SECONDS);

		@SuppressWarnings("unchecked")
		List<MetricsRequest> data = (List<MetricsRequest>) invocationData.getArguments()[0];

		assertNotNull(invocationData);
		assertTrue(((Object[]) invocationData.getArguments()).length == 1);

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
