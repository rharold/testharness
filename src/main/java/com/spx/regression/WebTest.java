package com.spx.regression;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebTest extends TestCase {

	private static final String TEST_OUTPUT = "C:/sdd_docs/selenium_test_output/";
	
	public void testSymptomSelectionPage() throws Exception {
		WebDriver driver = new InternetExplorerDriver();	
		
		this.login(driver, "sdd2spxar", "sdd2spxar");
        this.skipToVsoh(driver, "SAJAC44P795B32381");   
		
		WebElement element = (new WebDriverWait(driver, 10))
				  .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(.,'Diagnosis')]")));
		    
		element.click();
        waitForPage(driver, "Symptom Selection");
          
        try {
            driver.findElement(By.xpath("//a[contains(.,'Continue')]"));
        } catch (NoSuchElementException e) {
        	File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            this.copyFileToTestOutput(screenshot);
        	
            fail("The continue button should be displayed");
        }
        
        driver.quit();

	}

	private void copyFileToTestOutput(File screenshot) throws IOException {
		File screenshotOut = new File(TEST_OUTPUT + (new Date()).getTime() + ".png");
		FileUtils.copyFile(screenshot, screenshotOut);	
	}

	private void login(WebDriver driver, String username, String password) {
		driver.get("https://topix.landrover.jlrext.com/topix/sdd-view/user/sddLoginForm?loginTypeSelected=true&locale=en_GB&back=http%3A%2F%2Flocalhost%3A8080%2Fuser%2Fredirect_login_request.action%3FredirectAction%3D%2Fuser%2Flogin_authorisation.action&cancel=http%3A%2F%2Flocalhost%3A8080%2Fuser%2Fredirect_login_request.action%3FredirectAction%3Dsplash_screen_start.action&refresh=false&country=GB");	       
        WebElement element = driver.findElement(By.name("username"));
        element.sendKeys(username);
        element = driver.findElement(By.name("password"));       
        element.sendKeys(password);
        element.submit();
	}
	
	public void skipToVsoh(WebDriver driver, String vin) {
		waitForPage(driver, "warning");
        
		WebElement element = driver.findElement(By.id("continue_button"));
        element.click();
        
        waitForPage(driver, "diagnostic application");

        element = driver.findElement(By.id("manualvin_button"));
        element.click();
        
        waitForPage(driver, "vin entry");
        
        element = driver.findElement(By.id("vin"));
        element.sendKeys("SAJAC44P795B32381");
        element.submit();
        
        waitForPage(driver, "restore_vehicle_session.title");
        element = driver.findElement(By.id("continueSessionButton"));
        element.click();
	}
	
	private void waitForPage(WebDriver driver, final String pageTitle) {
		(new WebDriverWait(driver, 20)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().toLowerCase()
						.startsWith(pageTitle.toLowerCase());
			}
		});
	}
}
