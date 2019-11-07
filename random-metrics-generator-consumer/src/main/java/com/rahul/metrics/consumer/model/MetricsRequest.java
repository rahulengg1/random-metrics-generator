package com.rahul.metrics.consumer.model;

import java.util.ArrayList;

public class MetricsRequest {

	private String lon;
	private String metricsType;
	ArrayList<?> data = new ArrayList<>();
	private String lat;

	// Getter Methods

	public String getLon() {
		return lon;
	}

	public String getMetricsType() {
		return metricsType;
	}

	public String getLat() {
		return lat;
	}

	// Setter Methods

	public void setLon(String lon) {
		this.lon = lon;
	}

	public void setMetricsType(String metricsType) {
		this.metricsType = metricsType;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public ArrayList<?> getData() {
		return data;
	}

	public void setData(ArrayList<?> data) {
		this.data = data;
	}
	
	
	
}