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

import com.google.gson.Gson;

/**
 * Appygram.java
 * 
 * Usage:
 * 
 * 1) Call Appygram.configure, passing at least an API Key.  You can set other optional 
 * properties as desired.  This must be called first before sending any Appygrams.
 * 
 * 2) Call Appygram.create to generate an AppygramMessage object.
 * 
 * 3) Set properties such as name, email, message, summary and more on your 
 * AppygramMessage.  You must supply the message property at a minimum.
 * 
 * 4) Call Appygram.send, passing your message.
 * 
 * @author Carl Scott, <a href="http://solertium.com">Solertium Corporation</a>
 *
 */
public class Appygram {
	
	private static final Collection<AppygramEventHandler> afterSend = new HashSet<AppygramEventHandler>();
	private static AppygramConfig settings = null;
	
	public static void configure(AppygramConfig config) {
		settings = config;
	}
	
	/**
	 * Creates a new AppygramMessage based on your configuration 
	 * settings, setting defaults for fields such as topic, 
	 * platform, and software.  These can then be set manually.
	 * @return
	 */
	public static AppygramMessage create() {
		if (settings == null)
			return new AppygramMessage();
		
		AppygramMessage message = new AppygramMessage();
		message.setTopic(settings.getTopic());
		message.setPlatform(settings.getPlatform());
		message.setSoftware(settings.getSoftware());
		
		return message;
	}
	
	/**
	 * Adds a listener that fires when an Appygram is sent.
	 * @param handler
	 */
	public static void addAfterSendHandler(AppygramEventHandler handler) {
		afterSend.add(handler);
	}
	
	/**
	 * A convenience method for specifically handling 
	 * anonymous exceptions.
	 * @param e
	 */
	public static void send(Throwable e) {
		AppygramMessage message = new AppygramMessage();
		message.setSummary(e.getMessage());
		message.setMessage(AppygramMessage.toString(e, "\n"));
		
		send(message);
	}
	
	/**
	 * Send an Appygram message.
	 * @param message
	 */
	public static void send(final AppygramMessage message) {
		//TODO: use Logger or something like it.
		if (settings == null) {
			System.out.println(" (!) Call Appygram.configure to setup Appygram before use.");
			return;
		} else if (!settings.isConfigured()) {
			System.out.println(" (!) Appygram not configured");
			return;
		}
		
		if (message.hasMessage()) {
			final MessageHandler handler = new MessageHandler(settings, message);
			if (settings.isAllowThreads())
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
	public static List<AppygramTopic> topics() {
		final List<AppygramTopic> topics = new ArrayList<AppygramTopic>();
		
		//TODO: use Logger or something like it.
		if (settings == null) {
			System.out.println(" (!) Call Appygram.configure to setup Appygram before use.");
			return topics;
		} else if (!settings.isConfigured()) {
			System.out.println(" (!) Appygram not configured");
			return topics;
		}
		
		final URL url;
		try {
			url = new URL(settings.getUrl() + "/topics/" + settings.getKey());
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
	
	static void fireAfterSendEvent(AppygramEvent event) {
		for (AppygramEventHandler handler : afterSend) {
			try { handler.afterSend(event); }
			catch (Throwable ignored) { }
		}
	}

}
