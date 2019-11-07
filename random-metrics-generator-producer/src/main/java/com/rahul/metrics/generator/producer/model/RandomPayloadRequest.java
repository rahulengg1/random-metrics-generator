package com.rahul.metrics.generator.producer.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class RandomPayloadRequest {

	@NotNull(message ="totalPayloadRequest cannot be blank")
	@Min(value = 1, message ="totalPayloadRequest must be greater than 0")
	private Integer totalPayloadRequest;
}
