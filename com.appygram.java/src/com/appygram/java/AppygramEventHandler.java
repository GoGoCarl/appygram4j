package com.appygram.java;

/**
 * <p>Handles the AppygramEvent once an Appygram is sent.</p>
 * 
 * @author Carl Scott, <a href="http://solertium.com">Solertium Corporation</a>
 *
 */
public interface AppygramEventHandler {
	
	/**
	 * Called after an Appygram is sent.
	 * @param event the Appygram event information
	 */
	public void afterSend(AppygramEvent event);

}
