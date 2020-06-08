package org.henrietteharmse.tutorial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class SimpleLoggingTest {
	private static Logger logger = LoggerFactory.getLogger(SimpleLoggingTest.class);
	// Why This Failure marker
//	private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
	
	public static void main(String[] args) {
//		logger.trace(WTF_MARKER, "Hello from the SimpleLoggingTest class");
        logger.trace("Hello from the SimpleLoggingTest class");	
    }
}
