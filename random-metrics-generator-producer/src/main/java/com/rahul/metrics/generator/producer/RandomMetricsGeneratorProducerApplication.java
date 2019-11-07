package com.rahul.metrics.generator.producer;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.metrics.generator.producer.shared.ProducerProperties;

@SpringBootApplication
public class RandomMetricsGeneratorProducerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RandomMetricsGeneratorProducerApplication.class, args);
	}

	
	@Override
	public void run(String... args) throws Exception {
	}

	@Bean
	Exchange exchange(ProducerProperties producerProperties) {
		return ExchangeBuilder.directExchange(producerProperties.getExchange()).build();
	}

	@Bean
	MessageConverter messageConverter(ObjectMapper objectMapper) {
		return new Jackson2JsonMessageConverter(objectMapper);
	}

}
