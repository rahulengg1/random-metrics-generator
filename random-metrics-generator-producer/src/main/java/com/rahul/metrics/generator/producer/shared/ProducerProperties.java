package com.rahul.metrics.generator.producer.shared;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties("producer")
@Data
public class ProducerProperties {

	private String exchange;
	private String routing;
		
}
