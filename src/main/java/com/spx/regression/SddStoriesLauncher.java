package com.spx.regression;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestResult;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromRelativeFile;
import org.jbehave.core.io.LoadFromURL;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterConverters;
import org.junit.runner.RunWith;

public class SddStoriesLauncher extends JUnitStories implements Test {

	private String testDir;
	private String resourceDir;
	private String outputDir;
	private List<String> jirasToRun = new ArrayList<String>();
	
	
	public SddStoriesLauncher(String testDir, String resourceDir) {
		this.testDir = testDir;
		this.resourceDir = resourceDir;
	}
	
	public SddStoriesLauncher(String testDir, String resourceDir, String... jiras) {
		this.testDir = testDir;
		this.resourceDir = resourceDir;
		jirasToRun.addAll(Arrays.asList(jiras));
	}
	
	@Override
	protected List<String> storyPaths() {
		List<String> storyPaths = new ArrayList<String>();
		
		File baseTestDir = new File(testDir);
		File[] jiraTests = baseTestDir.listFiles();
		for (File file : jiraTests) {
			if(jirasToRun.isEmpty() || jirasToRun.contains(file.getName())) {
				File storiesDir = getStoriesDir(file);			
				File[] stories = storiesDir.listFiles();
				for (File story : stories) {
					URI storyURI = story.toURI();
					storyPaths.add(storyURI.toString());
				}
			}
		}
				
		return storyPaths;
	}

	private File getStoriesDir(File file) {
		File[] storiesDir = file.listFiles(new FilenameFilter() {
			
			public boolean accept(File dir, String name) {
				if("stories".equalsIgnoreCase(name)) {
					return true;
				}
				return false;
			}
		});
		
		return storiesDir[0];
	}
	
	@Override
    public InjectableStepsFactory stepsFactory() {        
        return new InstanceStepsFactory(configuration(), new SddSteps(testDir));
    }

	@Override
	public Configuration configuration() {
		URL url = null;
				
		try {
			url = new File(this.resourceDir).toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error getting resource with dir : " + this.resourceDir);
		}
		
		Configuration c =  new MostUsefulConfiguration()
            .useStoryLoader(new LoadFromURL()) 
            .useStoryReporterBuilder(new StoryReporterBuilder().withFormats(Format.CONSOLE))
            .useParameterConverters(new ParameterConverters().addConverters(
            		new ParameterConverters.ExamplesTableConverter(new ExamplesTableFactory(new LoadFromRelativeFile(url)))));
   
		return c;
	}

	private URL buildStoryOutputURL() {
		File story = new File(outputDir);
		URL storyURI = null;
		try {
			storyURI = story.toURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return storyURI;
	}

	public int countTestCases() {
		return 0;
	}

	public void run(TestResult arg0) {
		try {
			super.run();
		} catch (Throwable e) {
			e.printStackTrace();
		}		
	}
}
