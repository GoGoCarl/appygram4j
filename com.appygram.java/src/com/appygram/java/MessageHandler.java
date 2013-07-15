package com.appygram.java;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * MessageHandler.java
 * 
 * Handles delivery of Appygram messages.  Not intended for 
 * public consumption.
 * 
 * @author Carl Scott, <a href="http://solertium.com">Solertium Corporation</a>
 *
 */
class MessageHandler implements Runnable {
	
	private final AppygramConfig settings;
	private final AppygramMessage message;
	
	public MessageHandler(AppygramConfig config, AppygramMessage message) {
		this.settings = config;
		this.message = message;
	}
	
	public void run() {
		Appygram.fireAfterSendEvent(execute());
	}
	
	public AppygramEvent execute() {
		final URL url;
		try {
			url = new URL(settings.getUrl());
		} catch (MalformedURLException e) {
			return failure(e.getMessage());
		}
		
		final HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
		} catch (ClassCastException e) {
			return failure(e.getMessage());
		} catch (ProtocolException e) {
			return failure(e.getMessage());
		} catch (IOException e) {
			return failure(e.getMessage());
		}
		
		final BufferedOutputStream os;
		try {
			os = new BufferedOutputStream(conn.getOutputStream());
			os.write(message.toJSON(settings.getKey(), settings.getTopic()).getBytes());
			os.flush();
			os.close();
		} catch (IOException e) {
			return failure("Could not deliver Appygram: " + e.getMessage());
		}
		
		int responseCode;
		try {
			responseCode = conn.getResponseCode();
		} catch (IOException e) {
			responseCode = 200;
		}
		
		final boolean isSuccess = responseCode >= 200 && responseCode <= 299;
		
		AppygramEvent event = new AppygramEvent();
		event.setMessage(message);
		event.setSuccess(isSuccess);
		
		if (!isSuccess)
			event.setResponse("HTTP Error Code " + responseCode);
		else {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = null;
				StringBuilder in = new StringBuilder();
				
				while ((line = reader.readLine()) != null)
					in.append(line);
			
				reader.close();
				
				event.setResponse(in.toString());
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		
		return event;
	}
	
	private AppygramEvent failure(String error) {
		try {
			throw new Exception("Failure");
		} catch (Exception e) {
			e.printStackTrace();
		}
		AppygramEvent event = new AppygramEvent();
		event.setMessage(message);
		event.setSuccess(false);
		event.setResponse(error);
		
		return event;
	}

}
