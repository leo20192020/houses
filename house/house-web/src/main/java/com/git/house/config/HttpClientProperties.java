package com.git.house.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="spring.httpclient")
public class HttpClientProperties {

	private Integer connectTimeOut=1000;
	private Integer socketTimeOut=10000;
	private String agent="agent";
	private Integer maxConnPerRout=10;
	private Integer maxConnTotal=100;
	public Integer getConnectTimeOut() {
		return connectTimeOut;
	}
	public void setConnectTimeOut(Integer connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}
	public Integer getSocketTimeOut() {
		return socketTimeOut;
	}
	public void setSocketTimeOut(Integer socketTimeOut) {
		this.socketTimeOut = socketTimeOut;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public Integer getMaxConnPerRout() {
		return maxConnPerRout;
	}
	public void setMaxConnPerRout(Integer maxConnPerRout) {
		this.maxConnPerRout = maxConnPerRout;
	}
	public Integer getMaxConnTotal() {
		return maxConnTotal;
	}
	public void setMaxConnTotal(Integer maxConnTotal) {
		this.maxConnTotal = maxConnTotal;
	}
	
	
}
