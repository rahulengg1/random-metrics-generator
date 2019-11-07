package com.rahul.metrics.generator.producer.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetricsRequest implements Serializable { 

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3538543325652390280L;

	@NotNull(message ="lat cannot be blank")
	@DecimalMin(""+Double.MIN_VALUE)
	@JsonProperty("lat")
	private Double lat;
	
	@DecimalMin(""+Double.MIN_VALUE)
	@NotNull(message ="lon cannot be blank")
	@JsonProperty("lon")	
	private Double lon;

	@NotNull(message ="Metrics type cannot be blank")
	@NotEmpty(message ="Metrics type cannot be blank")
	@JsonProperty("metricsType")
	private String metricsType;

	@NotNull(message ="Metrics data cannot be blank")
	@NotEmpty(message ="Metrics data cannot be blank")
	@JsonProperty("data")
	private List<?> data;
}
