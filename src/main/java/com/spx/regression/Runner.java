package com.spx.regression;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.runner.JUnitCore;

public class Runner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> givenWhenThens = new ArrayList<String>();
		
		
		if("help".equalsIgnoreCase(args[0])) {
			Method[] methods = SddSteps.class.getMethods();
			for (Method method : methods) {
				Annotation[] annotations = method.getAnnotations();
				for (Annotation annotation : annotations) {
					if(annotation.annotationType() == Given.class) {
						Given givenAnnotation = (Given) annotation;
						System.out.println("Given " + givenAnnotation.value());
					}
					if(annotation.annotationType() == When.class) {
						When givenAnnotation = (When) annotation;
						System.out.println("When " + givenAnnotation.value());
					}
					if(annotation.annotationType() == Then.class) {
						Then givenAnnotation = (Then) annotation;
						System.out.println("Then " + givenAnnotation.value());
					}
					if(annotation.annotationType() == TestDescription.class) {
						TestDescription testDescription = (TestDescription) annotation;
						System.out.println("\t" + testDescription.description());
					}
				}
				System.out.println();
			}
			
		} else {
			SddStoriesLauncher sddStoriesLauncher = new SddStoriesLauncher(args[0], args[1]);
			
			JUnitCore jUnitCore = new JUnitCore();
			jUnitCore.run(sddStoriesLauncher);
			System.exit(0);
		}
	}

}
