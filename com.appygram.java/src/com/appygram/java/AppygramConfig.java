package com.appygram.java;

import java.util.Properties;

/**
 * <p>Set initial properties for sending appygrams.  You must provide your 
 * API Key.  Other properties that you can set are:</p>
 * 
 * <ul>
 * <li>Topic: Provide a default topic for Appygram Messages without one. The 
 * default is null, which sends to all your topics.</li>
 * <li>URL: Set the endpoint for your Appygrams.  This is already defaulted 
 * to the currently supported Appygram endpoint, which may be updated in the 
 * future.</li>
 * <li>Platform and Software: Provide details on your specific implementation 
 * to go along with each Appygram sent by the calling code.</li>
 * </ul>
 * 
 * <p>Additionally, you can set run configuration options:</p>
 * 
 * <ul>
 * <li>Log to Console: Print errors about Appygram configuration or sending 
 * Appygrams to the console, in addition to the AppygramLogger log.  Defaults 
 * to false.</li>
 * <li>Allow Threads: Threading can be turned off by setting allowThreads to 
 * false. By default, it's on, meaning that Appygrams will be sent in the 
 * background.</li>
 * </ul>
 * 
 * <p>This information can be set via Properties files using the following keys:</p>
 * 
 * <ul>
 * <li>com.appygram.java.key -- the API key (required)</li>
 * <li>com.appygram.java.topic -- the default topic</li>
 * <li>com.appygram.java.url -- the hostname of the Appygram endpoint</li>
 * <li>com.appygram.java.platform -- your application platform</li>
 * <li>com.appygram.java.software -- your application software</li>
 * <li>com.appygram.java.console -- true to log to console</li>
 * <li>com.appygram.java.thread -- true to allow threads</li>
 * </ul>
 * 
 * @author Carl Scott, <a href="http://solertium.com">Solertium Corporation</a>
 *
 */
public class AppygramConfig {
	
	private static final String APPYGRAM_DEFAULT_URL = "http://arecibo.appygram.com";
	
	/*
	 * Leaving this null tells Appygram to send to all topics
	 */
	private static final String APPYGRAM_DEFAULT_TOPIC = null;
	
	private String url, key, topic, platform, software;
	private boolean allowThreads = true;
	private boolean logToConsole = false;
	
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
	
	public AppygramConfig(Properties properties) {
		final String base = "com.appygram.java.";
		
		this.key = properties.getProperty(base + "key");
		this.topic = properties.getProperty(base + "topic", APPYGRAM_DEFAULT_TOPIC);
		this.url = properties.getProperty(base + "url", APPYGRAM_DEFAULT_URL);
		
		this.platform = properties.getProperty(base + "platform");
		this.software = properties.getProperty(base + "software");
		this.logToConsole = "true".equals(properties.getProperty(base + "console"));
		this.allowThreads = "true".equals(properties.getProperty(base + "thread"));
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
	
	public void setLogToConsole(boolean logToConsole) {
		this.logToConsole = logToConsole;
	}
	
	public boolean isLogToConsole() {
		return logToConsole;
	}
	
	private boolean isUnset(String value) {
		return value == null || "".equals(value);
	}

}
