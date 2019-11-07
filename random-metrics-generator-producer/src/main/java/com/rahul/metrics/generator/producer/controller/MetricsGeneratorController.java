package com.rahul.metrics.generator.producer.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.metrics.generator.producer.model.MetricsRequest;
import com.rahul.metrics.generator.producer.model.RandomPayloadRequest;
import com.rahul.metrics.generator.producer.service.RabbitMqProducerService;

import io.swagger.annotations.Api;

@RestController
@Api(tags = "Generate Metrics")
@RequestMapping(value = "generate-metrics")
public class MetricsGeneratorController {

	private final RabbitMqProducerService mqProducerService;
	
	
	public MetricsGeneratorController(RabbitMqProducerService mqProducerService) {
		this.mqProducerService = mqProducerService;
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addRecord(@Valid @RequestBody List<MetricsRequest> body) {
       
		mqProducerService.sendMessage(body);
        
		return new ResponseEntity<>(HttpStatus.CREATED);
    }
	
	@PostMapping(value = "/random",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces =MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> sendRandomRequest(@Valid @RequestBody RandomPayloadRequest body) {
       
		mqProducerService.generateMetrics(body);
        
		return new ResponseEntity<>(HttpStatus.CREATED);
    }
	

}
