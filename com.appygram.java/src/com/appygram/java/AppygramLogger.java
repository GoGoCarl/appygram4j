package com.appygram.java;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * AppygramLogger.java
 * 
 * Handles all Appygram logging.  Will log to console is specified via 
 * configuration properties.
 * 
 * @author Carl Scott, <a href="http://solertium.com">Solertium Corporation</a>
 *
 */
public class AppygramLogger extends Handler {
	
	private static final Logger log = Logger.getLogger(AppygramLogger.class.getName());
	private static final Handler console = new AppygramLogger();
	
	public static Logger get() {
		return log;
	}
	
	static void configure(AppygramConfig config) {
		if (config.isLogToConsole())
			log.addHandler(console);
		else
			log.removeHandler(console);
	}
	
	public void close() throws SecurityException {
		// No need to close System.out
	}
	
	@Override
	public void flush() {
		// No need to flush System.out
		
	}
	
	@Override
	public void publish(LogRecord record) {
		System.out.println(record.getMessage());
	}

}
