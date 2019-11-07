package com.rahul.metrics.generator.producer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;

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

@SpringBootTest
@SpringJUnitConfig
@DirtiesContext
class RandomMetricsGeneratorProducerApplicationTests {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private Queue queue1;

	@Autowired
	private RabbitListenerTestHarness harness;

	@Test
	public void testTwoWay() throws Exception {
		assertEquals("TESTMESSAGE", this.rabbitTemplate.convertSendAndReceive(this.queue1.getName(), "TestMessage"));

		RabbitListenerTestHarness.InvocationData invocationData = this.harness.getNextInvocationDataFor("listener", 10,
				TimeUnit.SECONDS);
		assertNotNull(invocationData);
		assertEquals(invocationData.getArguments()[0], "TestMessage");
		assertEquals(invocationData.getResult(), "TESTMESSAGE");
	}

	@TestConfiguration
	@RabbitListenerTest(spy = false, capture = true)
	public static class Config {

		@Bean
		public Queue queue1() {
			return new AnonymousQueue();
		}

		@RabbitListener(id = "listener", queues = "#{queue1.name}")
		public String Listener(String listener) {
			return listener.toUpperCase();
		}

	}

	
	
}
