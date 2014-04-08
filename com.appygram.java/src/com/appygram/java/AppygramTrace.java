package com.appygram.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * <p>Represents an Appygram message specifically for handling exceptions.  
 * The trace parameter is required, the rest are optional. If you need 
 * more fields, you can supply them via the app_json HashMap.</p>
 * 
 * <p>All data is serialized to JSON when sending to Appygram.</p>
 * 
 * <p>This class can be extended for ease of development.</p>
 * 
 * @author Carl Scott, <a href="http://solertium.com">Solertium Corporation</a>
 *
 */
public class AppygramTrace extends AppygramMessage {
	
	protected AppygramTraceData trace;
	
	public void setTrace(Throwable e) {
		if (e != null)
			setTrace(new AppygramTraceData(e));
		else
			this.trace = null;
	}
	
	public void setTrace(AppygramTraceData trace) {
		this.trace = trace;
	}
	
	public AppygramTraceData getTrace() {
		return trace;
	}
	
	@Override
	public boolean isValid() {
		return trace != null;
	}
	
	protected Gson createGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(trace.getClass(), trace);
		
		return builder.create();
	}

}
