package com.appygram.java;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

/**
 * AppygramTraceData.java
 * 
 * Trace information about an exception, handles serializing 
 * stack trace information into Appygram trace format.
 * 
 * All data is serialized to JSON when sending to Appygram.
 * 
 * @author Carl Scott, <a href="http://solertium.com">Solertium Corporation</a>
 *
 */
public class AppygramTraceData implements JsonSerializer<AppygramTraceData> {
	
	@SerializedName("class")
	protected final String className;
	
	protected final String message;
	protected final List<List<String>> backtrace;
	
	public AppygramTraceData(Throwable e) {
		this.className = e.getClass().getName();
		this.message = e.getMessage();
		this.backtrace = new ArrayList<List<String>>();
		
		for (StackTraceElement el : e.getStackTrace())
			backtrace.add(convert(el));
		int count = 0;
		Throwable t = e;
		while (count++ < 10 && (t = t.getCause()) != null) {
			backtrace.add(Arrays.asList(t.getClass().getName(), "0", t.getMessage()));
			for (StackTraceElement el : t.getStackTrace())
				backtrace.add(convert(el));
		}
	}
	
	private List<String> convert(StackTraceElement el) {
		return Arrays.asList(el.getFileName(), Integer.toString(el.getLineNumber()), el.getMethodName());
	}
	
	/**
	 * FIXME: this is a bit of a hack due to a limitation with 
	 * the Gson encoding of the nested JsonObject.  The attributes 
	 * are coming across incorrectly.  The below works with the 
	 * arecibo connector, but in a future commit this override 
	 * should not be necessary, serialization should natively work.  
	 */
	public JsonElement serialize(AppygramTraceData src, Type type, JsonSerializationContext context) {
		return new JsonPrimitive(new Gson().toJson(src));
	}
	

}
