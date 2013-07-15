package com.appygram.java;

/**
 * AppygramEventHandler.java
 * 
 * Handles the AppygramEvent once an Appygram is sent.
 * 
 * @author Carl Scott, <a href="http://solertium.com">Solertium Corporation</a>
 *
 */
public interface AppygramEventHandler {
	
	/**
	 * Called after an Appygram is sent.
	 * @param event
	 */
	public void afterSend(AppygramEvent event);

}
