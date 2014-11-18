package com.appygram.java;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * <p>Represents an Appygram message.  The message parameter is required, 
 * the rest are optional.  If you need more fields, you can supply them 
 * via the app_json HashMap.</p>
 * 
 * <p>All data is serialized to JSON when sending to Appygram.</p>
 * 
 * <p>This class can be extended for ease of development.</p>
 * 
 * @author Carl Scott, <a href="http://solertium.com">Solertium Corporation</a>
 *
 */
public class AppygramMessage {
	
	protected String name, email, phone, topic, message, 
		platform, software, summary, api_key;
	protected String app_json;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getSoftware() {
		return software;
	}

	public void setSoftware(String software) {
		this.software = software;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getAppJSON() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (app_json == null || "".equals(app_json))
			return map;
		
		return (Map<String, Object>) createGson().fromJson(app_json, map.getClass());
	}

	public void setAppJSON(Map<String, Object> appJson) {
		app_json = createGson().toJson(appJson);
	}
	
	public void setAppJSON(JsonObject json) {
		app_json = json.toString();
	}
	
	public boolean isValid() {
		return message != null && !"".equals(message);
	}

	public String toJSON(String token, String topic) {
		this.api_key = token;
		if (this.topic == null || "".equals(this.topic))
			this.topic = topic;
		
		return createGson().toJson(this);
	}
	
	public String toString() {
		return createGson().toJson(this);
	}
	
	protected Gson createGson() {
		return new Gson();
	}

	/**
	 * Simple convenience method for printing exceptions.
	 * @param e the exception
	 * @param lineBreakRule use \n for plain text, <br/> for html
	 * @return
	 */
	public static String toString(Throwable e, String lineBreakRule) {
		final StringBuilder out = new StringBuilder();
		out.append(e.toString() + lineBreakRule);
		for (StackTraceElement el : e.getStackTrace())
			out.append(el.toString() + lineBreakRule);
		int count = 0;
		Throwable t = e;
		while (count++ < 10 && (t = t.getCause()) != null) {
			out.append(t.toString() + lineBreakRule);
			for (StackTraceElement el : t.getStackTrace())
				out.append(el.toString() + lineBreakRule);
		}
		return out.toString();
	}
	
}