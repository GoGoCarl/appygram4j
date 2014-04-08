package com.appygram.java;

/**
 * <p>An event for when Appygrams are sent.  This contains 
 * the original AppygramMessage sent, a success flag, and 
 * the response message, if available.</p>
 * 
 * @author Carl Scott, <a href="http://solertium.com">Solertium Corporation</a>
 *
 */
public class AppygramEvent {
	
	private AppygramMessage message;
	private boolean isSuccess;
	private String response;
	
	/**
	 * The original message that was sent.
	 * @return
	 */
	public AppygramMessage getMessage() {
		return message;
	}
	
	public void setMessage(AppygramMessage message) {
		this.message = message;
	}

	/**
	 * True if the server got a success response, false otherwise.
	 * @return
	 */
	public boolean isSuccess() {
		return isSuccess;
	}
	
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	/**
	 * The response text given back from the Appygram server.
	 * @return
	 */
	public String getResponse() {
		return response;
	}
	
	public void setResponse(String response) {
		this.response = response;
	}
	
}
