package com.rahul.metrics.consumer.listeners;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.metrics.consumer.model.MetricsRequest;
import com.rahul.metrics.consumer.service.MetricsService;



@Service
public class Consumer {
	
	private Logger log = LoggerFactory.getLogger(Consumer.class);
	
	private ObjectMapper ObjectMapper= new ObjectMapper();
	

	private MetricsService metricsService;
	
	public Consumer(MetricsService metricsService)
	{
		this.metricsService = metricsService;
	}

	@RabbitListener( queues = "queue.metrics.work", concurrency = "3")
	public void Listen(String msg) throws JsonParseException, JsonMappingException, IOException
	{
		
		List<MetricsRequest> request = ObjectMapper.readValue(msg,ObjectMapper.getTypeFactory().constructCollectionType(List.class, MetricsRequest.class));
		log.info("Data is "+request);
		for(MetricsRequest data:request)
		{
			if(data.getLat()==null)
				throw new AmqpRejectAndDontRequeueException("Lat should not be null");
		}
		metricsService.createRecord(request);
		
		
	}
}
