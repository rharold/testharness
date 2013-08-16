package com.spx.regression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class PropertiesTest extends TestCase {

	String regexPattern = "\\{\\s.\\}|\\{.\\s\\}|\\{\\s.\\s\\}|\\{\\D\\}";
	
	public void testCheckTrailingSpace() {		
		String testString = "DTC Help for {0} {1 } hello {2}";
		checkPropertyString(testString, true);
	}
	
	public void testCheckDoubleSpace() {		
		String testString = "DTC Help for {0} {1} hello { 2 }";
		checkPropertyString(testString, true);
	}
	
	public void testCheckPreSpace() {		
		String testString = "DTC Help for { 0} {1} hello {2}";
		checkPropertyString(testString, true);
	}
	
	public void testCheckAlpha() {		
		String testString = "DTC Help for {a}";
		checkPropertyString(testString, true);
	}
	
	public void testAllGood() {		
		String testString = "DTC Help for {0} {1} hello {2}";
		checkPropertyString(testString, false);
		
		testString = "{0} {1} hello {2}";
		checkPropertyString(testString, false);
		
		testString = "{0}wefwefw wefwe {1} hello wefwef{2}wefw";
		checkPropertyString(testString, false);
	
	}

	private void checkPropertyString(String testString, boolean expectToFail) {
		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(testString);
		
		boolean found = false;
		
		if(matcher.find()) {
			found = true;
			System.out.println("Failed illegal property " + testString);
		}
		
		if(expectToFail && !found) {
			System.out.println("Failed illegal property " + testString);
			fail(testString);
		}
		
		if(found && !expectToFail) {
			System.out.println("Failed illegal property " + testString);
			fail(testString);
		}
	}
	
}
