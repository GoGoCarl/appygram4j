package com.appygram.java;

/**
 * <p>Represents a topic set up on Appygram.com.  The ID 
 * and display name are available to the caller.</p>
 * 
 * @author Carl Scott, <a href="http://solertium.com">Solertium Corporation</a>
 *
 */
public class AppygramTopic {
	
	private final String id, name;
	
	public AppygramTopic(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}

}
