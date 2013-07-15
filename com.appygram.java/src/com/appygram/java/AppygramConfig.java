package com.appygram.java;

/**
 * AppygramConfig.java
 * 
 * Set initial properties for sending appygram.  You must provide your API Key. 
 * Other properties that you can set are:
 * 
 * - Topic: Provide a default topic for Appygram Messages without one. The 
 * default is null, which sends to all your topics.
 * - URL: Set the endpoint for your Appygrams.  This is already defaulted 
 * to the currently supported Appygram endpoint, which may be updated in the 
 * future.
 * - Platform and Software: Provide details on your specific implementation 
 * to go along with each Appygram sent by the calling code. 
 * 
 * Finally, you can turn Threading off by setting allowThreads to false. By 
 * default, it's on, meaning that Appygrams will be sent in the background.
 * 
 * @author Carl Scott, <a href="http://solertium.com">Solertium Corporation</a>
 *
 */
public class AppygramConfig {
	
	private static final String APPYGRAM_DEFAULT_URL = "https://arecibo.appygram.com/appygrams";
	
	/*
	 * Leaving this null tells Appygram to send to all topics
	 */
	private static final String APPYGRAM_DEFAULT_TOPIC = null;
	
	private String url, key, topic, platform, software;
	private boolean allowThreads = true;
	
	public AppygramConfig(String key) {
		this(key, APPYGRAM_DEFAULT_TOPIC);
	}
	
	public AppygramConfig(String key, String topic) {
		this(key, topic, APPYGRAM_DEFAULT_URL);
	}
	
	public AppygramConfig(String key, String topic, String url) {
		this.key = key;
		this.topic = topic;
		this.url = url;
	}
	
	public boolean isConfigured() {
		return !(isUnset(url) || isUnset(key));
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public void setToken(String key) {
		this.key = key;
	}

	public String getTopic() {
		return topic;
	}

	/**
	 * The default topic for Appygrams to go to, if one 
	 * is not provided.
	 * @param topic
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	public String getPlatform() {
		return platform;
	}
	
	public void setSoftware(String software) {
		this.software = software;
	}
	
	public String getSoftware() {
		return software;
	}
	
	public void setAllowThreads(boolean allowThreads) {
		this.allowThreads = allowThreads;
	}
	
	public boolean isAllowThreads() {
		return allowThreads;
	}
	
	private boolean isUnset(String value) {
		return value == null || "".equals(value);
	}

}
