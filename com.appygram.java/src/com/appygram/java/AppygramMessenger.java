package com.appygram.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.appygram.java.MessageHandler.AppygramEndpoint;
import com.google.gson.Gson;

/**
 * AppygramMessenger.java
 * 
 * Handles delivery of messages and traces, as well as grabs the 
 * topic list.  Is bound to a particular Appygram configuration and 
 * API Key.  Multiples of these can exist in a single application.
 * 
 * @author Carl Scott, <a href="http://solertium.com">Solertium Corporation</a>
 *
 */
public class AppygramMessenger {
	
	protected final Collection<AppygramEventHandler> afterSend;
	protected final AppygramConfig config;
	
	public AppygramMessenger(AppygramConfig config) {
		this.afterSend = new HashSet<AppygramEventHandler>();
		this.config = config;
	}
	
	/**
	 * Creates a new AppygramMessage based on your configuration 
	 * settings, setting defaults for fields such as topic, 
	 * platform, and software.  These can then be set manually.
	 * @return
	 */
	public AppygramMessage create() {
		if (config == null)
			return new AppygramMessage();
		
		AppygramMessage message = new AppygramMessage();
		message.setTopic(config.getTopic());
		message.setPlatform(config.getPlatform());
		message.setSoftware(config.getSoftware());
		
		return message;
	}
	
	/**
	 * Creates a new AppygramTrace based on your configuration 
	 * settings, setting defaults for fields such as topic, 
	 * platform, and software.  These can then be set manually.
	 * @return
	 */
	public AppygramTrace createTrace() {
		return createTrace(null);
	}
	
	/**
	 * Creates a new AppygramTrace based on your configuration 
	 * settings, setting defaults for fields such as topic, 
	 * platform, and software.  These can then be set manually. 
	 * Generates trace information from the supplied exception.
	 * @return
	 */
	public AppygramTrace createTrace(Throwable t) {
		AppygramTrace message;
		if (config == null)
			message = new AppygramTrace();
		else {
			message = new AppygramTrace();
			message.setTopic(config.getTopic());
			message.setPlatform(config.getPlatform());
			message.setSoftware(config.getSoftware());
		}
		
		message.setTrace(t);
		
		return message;
	}
	
	/**
	 * Adds a listener that fires when an Appygram is sent.
	 * @param handler
	 */
	public void addAfterSendHandler(AppygramEventHandler handler) {
		afterSend.add(handler);
	}
	
	/**
	 * Send an Appygram message.
	 * @param message
	 */
	public void send(final AppygramMessage message) {
		if (!isReady())
			return;
		
		if (message.isValid()) {
			final MessageHandler handler = new MessageHandler(this, message);
			if (config.isAllowThreads())
				new Thread(handler).start();
			else
				handler.run();
		}
	}
	
	/**
	 * Load the list of supported topics for your App from 
	 * Appygram.com
	 * @return
	 */
	public List<AppygramTopic> topics() {
		final List<AppygramTopic> topics = new ArrayList<AppygramTopic>();
		
		if (!isReady())
			return topics;
		
		final URL url;
		try {
			url = new URL(config.getUrl() + "/topics/" + config.getKey());
		} catch (MalformedURLException e) {
			return topics;
		}
		
		final HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
		} catch (ClassCastException e) {
			return topics;
		} catch (ProtocolException e) {
			return topics;
		} catch (IOException e) {
			return topics;
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			StringBuilder in = new StringBuilder();
				
			while ((line = reader.readLine()) != null)
				in.append(line);
			
			reader.close();
			
			topics.addAll(new Gson().fromJson(in.toString().trim(), AppygramTopicList.class));
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
		
		return topics;
	}
	
	/**
	 * A convenience method for specifically handling 
	 * anonymous exceptions.  Generates a summary based 
	 * on the exception message, and message contents 
	 * containing the stack trace with lines delimited 
	 * by HTML br tags.  If you want a different config 
	 * for your message, pass in an AppygramTrace object 
	 * instead.
	 * @param e
	 */
	public void trace(Throwable e) {
		AppygramTrace trace = createTrace(e);
		trace.setSummary(e.getMessage());
		trace.setMessage(AppygramMessage.toString(e, "<br/>"));
		
		trace(trace);
	}
	
	/**
	 * Send an Appygram exception trace.  The AppygramTrace 
	 * must have a trace attached, but a message is not 
	 * required.
	 * @param message
	 */
	public void trace(AppygramTrace message) {
		if (!isReady())
			return;
		
		if (message.isValid()) {
			final MessageHandler handler = new MessageHandler(this, message);
			handler.setEndpoint(AppygramEndpoint.Traces);
			if (config.isAllowThreads())
				new Thread(handler).start();
			else
				handler.run();
		}
	}
	
	private boolean isReady() {
		if (config == null) {
			AppygramLogger.get().severe(" (!) Call Appygram.configure to setup Appygram before use.");
			return false;
		} else if (!config.isConfigured()) {
			AppygramLogger.get().severe(" (!) Appygram not configured");
			return false;
		}
		
		return true;
	}
	
	AppygramConfig getConfig() {
		return config;
	}
	
	void fireAfterSendEvent(AppygramEvent event) {
		for (AppygramEventHandler handler : afterSend) {
			try { handler.afterSend(event); }
			catch (Throwable ignored) { }
		}
	}

}
