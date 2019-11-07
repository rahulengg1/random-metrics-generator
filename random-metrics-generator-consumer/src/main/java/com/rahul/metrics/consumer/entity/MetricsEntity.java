package com.rahul.metrics.consumer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="metrics")
@Data
@Getter
@Setter
public class MetricsEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4709240425478345226L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, length = 50)
	private String lat;
	
	@Column(nullable = false, length = 50)
	private String lon;
	
	@Column(name="metrics_type", nullable = false, length = 120)
	private String metricsType;
	
	@Column(name="metrics_data")
	private String data;

}
