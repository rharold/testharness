package com.spx.regression;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;

import org.openqa.selenium.support.ui.Select;

public class SddSteps {

	private static final String INDEPENDANT = "Independant";
	private static final String DEALER = "Dealer";
	private static final String IMAGE_DIR = "images";
	private static final String PPTX_DIR = "pptx";
	WebDriver driver = new InternetExplorerDriver();
	private static final String TEST_OUTPUT = "C:/sdd_docs/selenium_test_output/";
	//private static final String IDS_HOME = "C:/ProgramData/JLR/IDS/uitests/";
	private String countryCode = "en";
	private String jira = "";
	private String testDir;
	
	private Map<Integer, ExamplesTable> sequences = new HashMap<Integer, ExamplesTable>();
	
	public SddSteps(String testDir) {
		this.testDir = testDir;
	}
	
	/**
	 * Given testing jira $jira I log in with username $username and password $password with language $language
	 * 
	 * Method that will open up the login page insert username and password into the corresponding text fields and click the submit 
	 * button.  The 2 Digit country code can be provided to enable country specific testing.  The jira number must be supplied
	 * to enable correct reporting and location for pptx support
	 * 
	 * @param jira the jira that the test supports
	 * @param username the username to log on with
	 * @param password the password to log on with
	 * @param countryCode the country code for specific language testing
	 */
	@Given("testing jira $jira I log in with username $username and password $password with language $language")
	@TestDescription(description="Method to login username and password and land on the Warning Page.  The 2 Digit country code can be provided to enable country specific testing.  The jira number must be supplied to enable correct reporting and location for pptx support.")
    public void loginWithLang(String jira, String username, String password, String countryCode) {		
		this.countryCode = countryCode;
		
		this.login(jira, username, password);
    }
	
	/**
	 * Given testing jira $jira I log in with username $username and password $password
	 * 
	 * Method that will open up the login page insert username and password into the corresponding text fields and click the submit 
	 * button.  The jira number must be supplied to enable correct reporting and location for pptx support.
	 * 
	 * @param jira the jira that the test supports
	 * @param username the username to log on with
	 * @param password the password to log on with
	 * @param countryCode the country code for specific language tetsting
	 */
	@Given("testing jira $jira I log in with username $username and password $password")
	@TestDescription(description="Method to login username and password and land on the Warning Page.  The jira number must be supplied to enable correct reporting and location for pptx support.")
    public void login(String jira, String username, String password) {		
		//driver.manage().timeouts().pageLoadTimeout(10000, TimeUnit.MILLISECONDS);
		this.jira = jira;
		try {
			gotoLoginPage(INDEPENDANT);
			
			WebElement element = findElement("username", ElementType.NAME);
	        element.sendKeys(username);
	        element = findElement("password", ElementType.NAME);       
	        element.sendKeys(password);
	        element.submit();
	        
	        this.waitForPage(driver, "Warning");
		} catch (Exception e) {
			e.printStackTrace();
        	fail("Failed to find element on page for login");
        }
    }
	
	
	/**
	 * Given testing jira $jira I log in with username $username and password $password
	 * 
	 * Method that will open up the login page insert username and password into the corresponding text fields and click the submit 
	 * button.  The jira number must be supplied to enable correct reporting and location for pptx support.
	 * 
	 * @param jira the jira that the test supports
	 * @param username the username to log on with
	 * @param password the password to log on with
	 * @param countryCode the country code for specific language tetsting
	 */
	@Given("testing jira $jira I log in as Dealer with username $username and password $password")
	@TestDescription(description="Method to login username and password and land on the Warning Page.  The jira number must be supplied to enable correct reporting and location for pptx support.")
    public void loginAsDealer(String jira, String username, String password) {		
		//driver.manage().timeouts().pageLoadTimeout(10000, TimeUnit.MILLISECONDS);
		this.jira = jira;
	     
		try {
			gotoLoginPage(DEALER);
			
			WebElement element = findElement("userid", ElementType.NAME);
	        element.sendKeys(username);
	        element = findElement("password", ElementType.NAME);       
	        element.sendKeys(password);
	        element.submit();
	        
	        this.waitForPage(driver, "Warning");
		} catch (Exception e) {
        	fail("Failed to find element on page for login");
        }
    }
	
	@Given("register the click sequence: $sequence")
	@TestDescription(description="Method that will register the click sequence.  The order of which will be the identifier for the corresponding run method.  e.g. When I navigate to using the click sequence 1.  The clicks can be one of the following: the element id, the screen text of an anchor, the text in a span tag.  The page is the page to wait for after a click.  If no page is required eg for a dialog box the use the keyword nopage")
	public void tableSequence(ExamplesTable sequence) {
		int index = this.sequences.size() + 1;
		this.sequences.put(index, sequence);
	}
	
	private void gotoLoginPage(String type) throws TestException {
		driver.get("http://localhost:8080/splash_screen_start.action");	 
		WebElement element = this.findElement("SplashScreen_anchor", ElementType.ID);
		element.click();
		
		waitForPage(driver, "User Authentication");
		
		driver.switchTo().frame("loginIframe");
		
		if(DEALER.equals(type)) {
			element = this.findElement("ar-button", ElementType.ID);
		} else if (INDEPENDANT.equals(type)){
			element = this.findElement("io-button", ElementType.ID);
		} else {
			throw new TestException(new Exception("Unknown type of user"));
		}
		
		element.click();
		waitForPage(driver, "User Authentication");
	}
	
	/**
	 * Given I return to VSOH page
	 * 
	 * Method to take the current session back to the VSOH page.  Equivalant to clicking the 'Session' link
	 * on the tab menu.
	 * 
	 */
	@Given("I return to VSOH page") 
	@TestDescription(description="Method to take the current session back to the VSOH page.  Equivalant to clicking the 'Session' link on the tab menu.")
	public void returnToVsoh() {
		this.clickTag("Session");
	}
	
	@When("I navigate to using the click sequence $sequenceId")
	@TestDescription(description="Method that will run the registered the click sequence.  The sequenceId is the order number that the sequences were registered.")
	public void navigateUsingSequence(int sequenceId) {
		ExamplesTable sequence = this.sequences.get(sequenceId);
		try {						
			for (Map<String,String> row : sequence.getRows()) {
		        String click = row.get("click");
		        String page = row.get("page");
				
				WebElement element = findElement(click, ElementType.NOT_SPECIFIED);
		        element.click();
		        if(!"nopage".equals(page)) {
		        	waitForPage(driver, page);
		        }					
		    }
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to navigate using sequnce");
		}
	}

	/**
	 * When I use the vin $vin and navigate to the vsoh page
	 * 
	 * Method to take the user straight into the VSOH after the login command has been called.  This method will
	 * use the 'Continue Session' option.  The vin will need to be supplied.
	 * 
	 * @param The vin to be supplied
	 */
	@When("I use the vin $vin and navigate to the vsoh page")
	@TestDescription(description="Depricated - use the click sequence: Method to take the user straight into the VSOH after the login command has been called.  This method will use the 'Continue Session' option.  The vin will need to be supplied.")
    public void navigateToVsohandClickBottomButton(String vin) {
		waitForPage(driver, "warning");
        
		try {
			WebElement element = findElement("continue_button", ElementType.ID);
	        element.click();
	        
	        waitForPage(driver, "diagnostic application");
	
	        element = findElement("manualvin_button", ElementType.ID);
	        element.click();
	        
	        waitForPage(driver, "vin entry");
	        
	        element = findElement("vin", ElementType.ID);
	        element.sendKeys(vin);
	        element.submit();
	        
	        waitForPage(driver, "restore_vehicle_session.title");
	        element = findElement("continueSessionButton", ElementType.ID);
	        element.click();
		} catch (Exception e) {
			fail("Failed to find element on page for navigateToVsohandClickBottomButton");
		}                
    }
	
	@When("I enter the text $text into the textbox $textbox")
	@TestDescription(description="Enter text into the specified text box.")
	public void textIntoTextbox(String text, String textBox) {
		this.textIntoTextbox(text, textBox, false);
	}
	
	@When("I enter the text $text into the textbox $textbox with submit")
	@TestDescription(description="Enter text into the specified text box and submit the form that the text box belongs to")
	public void textIntoTextbox(String text, String textBox, boolean submit) {
		WebElement element = findElement(textBox, ElementType.ID);
        element.sendKeys(text);
        if(submit) {
        	element.submit();
        }
        
	}
	
	/**
	 * When I use the vin $vin and navigate to the vsoh page
	 * 
	 * Method to take the user straight into the VSOH after the login command has been called.  This method will
	 * use the 'Continue Session' option.  The vin will need to be supplied.
	 * 
	 * @param The vin to be supplied
	 */
	@When("autovin to the vsoh page")
	@TestDescription(description="Depricated - use the click sequence: Method to take the user straight into the VSOH after the login command has been called.  This method will use the 'Continue Session' option.  Will autovin.")
    public void autovinToVsoh() {
		waitForPage(driver, "warning");
        
		try {
			WebElement element = findElement("continue_button", ElementType.ID);
	        element.click();
	        
	        waitForPage(driver, "diagnostic application");
	
	        element = findElement("autovin_button", ElementType.ID);
	        element.click();
	        
	        waitForPage(driver, "restore_vehicle_session.title");
	        element = findElement("continueSessionButton", ElementType.ID);
	        element.click();
		} catch (Exception e) {
			fail("Failed to find element on page for navigateToVsohandClickBottomButton");
		}                
    }
	
	/**
	 * When I use the vin $vin and navigate to the vsoh page
	 * 
	 * Method to take the user straight into the VSOH after the login command has been called.  This method will
	 * use the 'Start New Session' option.  The vin will need to be supplied.
	 * 
	 * @param The vin to be supplied
	 */
	@When("autovin to the vsoh page with new session")
	@TestDescription(description="Depricated - use the click sequence: Method to take the user straight into the VSOH after the login command has been called.  This method will use the 'Start New Session' option.  Will autovin.")
    public void autovinToVsohStartNewSession() {
		waitForPage(driver, "warning");
        
		try {
			WebElement element = findElement("continue_button", ElementType.ID);
	        element.click();
	        
	        waitForPage(driver, "diagnostic application");
	
	        element = findElement("autovin_button", ElementType.ID);
	        element.click();
	        
	        waitForPage(driver, "restore_vehicle_session.title");
	        element = findElement("newSessionButton", ElementType.ID);
	        element.click();
		} catch (Exception e) {
			fail("Failed to find element on page for navigateToVsohandClickBottomButton");
		}                
    }
	
	/**
	 * When I use the vin $vin and start new session
	 * 
	 * Method to take the user to the vin entry then select the 'Start New Session' should be used after the logon command.
	 * The vin will need to be supplied.
	 * 
	 * @param The vin to use
	 */
	@When("I use the vin $vin and start new session")
	@TestDescription(description="Depricated - use the click sequence: Method to take the user to the vin entry then select the 'Start New Session' should be used after the logon command. The vin will need to be supplied.")
    public void startNewSession(String vin) {
		waitForPage(driver, "warning");
        
		try {
			WebElement element = findElement("continue_button", ElementType.ID);
	        element.click();
	        
	        waitForPage(driver, "diagnostic application");
	
	        element = findElement("manualvin_button", ElementType.ID);
	        element.click();
	        
	        waitForPage(driver, "vin entry");
	        
	        element = findElement("vin", ElementType.ID);
	        element.sendKeys(vin);
	        element.submit();
	        
	        waitForPage(driver, "restore_vehicle_session.title");
	        element = findElement("newSessionButton", ElementType.ID);
	        element.click();
		} catch (Exception e) {
			fail("Failed to find element on page for startNewSession");
		}
        
        
    }
	
	/**
	 * When click the $page link
	 * 
	 * Method that will attempt to click the link on the page which is described by the $page supplied.
	 * The method looks to see if the text supplied in contained in the text of the anchor tag.  This may
	 * cause confusion if 2 links are similar eg Session v Sessions.
	 * 
	 * xpath search:
	 * 
	 * $x("//a[contains(.,'text')]")
	 * 
	 * @param page
	 */
	
	@When("click $click")
	@TestDescription(description="Method that will attempt to click specified click. This could be an element id, display text of anchor or text inside span tag.")
	public void clickTag(String click) {
		System.out.println("trying to navidate to " + click);
		try {
			WebElement element = this.findElement(click, ElementType.NOT_SPECIFIED);
			    
			element.click();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Timeout on waiting to click the " + click);
		}
	}
	
	@When("click the drop down $dropDownId with the value $value")
	@TestDescription(description="method to click and select a value from a drop down.  The id is the id of the drop down and value is the display value")
	public void selectSelectBox(String dropDownId, String value) {
		WebElement element = (new WebDriverWait(driver, 50))
				  .until(ExpectedConditions.presenceOfElementLocated(By.id(dropDownId)));
		
		new Select(element).selectByVisibleText(value);
	}
	
	@When("click $click and wait for page $pageTitleToWaitFor")
	@TestDescription(description="Click the click and supply a page title to wait for.  Click could be an element id, display text of anchor or text inside span tag.")
	public void clickAnchorTagWithWait(String click, String pageTitleToWaitFor) {
		try {
			WebElement element = this.findElement(click, ElementType.NOT_SPECIFIED);
			    
			element.click();
		} catch (Exception e) {
			fail("Timeout on waiting to click the " + click + " anchor");
		}
		waitForPage(driver, pageTitleToWaitFor);
	}
	
	/**
	 * When run the $candidate candidate
	 * 
	 * Method to activate a pal 2 candidate from te recommendations page.  This method will not actually run the
	 * activex but will take the session to the launch page of the activex.  The user must supply the candidate id
	 * that they wish to launch.  This can found by inspecting the element in te browser.  
	 * 
	 * @param candidate the candidate to run
	 */
	@When("run the $candidate candidate")
	@TestDescription(description="Method to activate a pal 2 candidate from te recommendations page.  This method will not actually run the activex but will take the session to the launch page of the activex.  The user must supply the candidate id that they wish to launch.  This can found by inspecting the element in te browser. ")
	public void runCandidate(String candidate) {		
		System.out.println("runCandidate: " + candidate);
		this.ensureCandidateLoaded(candidate);
		driver.manage().timeouts().pageLoadTimeout(5000, TimeUnit.MILLISECONDS);
		try {
			driver.get("http://localhost:8080/launch_recommendation.action?candidateId=" + candidate.trim() + "&disclaimerSelection=agree");	
		} catch (Exception e) {
			//the activex can block this call...presume page has loaded by this point and carry on
		}
		
		System.out.println("runCandidate url called: " + candidate);
	}
		
	@When("run pagmcp $candidate candidate")
	@TestDescription(description="")
	public void runPagmcpCandidate(String candidate) {	
		System.out.println("runPagmcpCandidate: " + candidate);
		this.ensureCandidateLoaded(candidate);
		driver.get("http://localhost:8080/pagmcp_launch.action?candidateId=" + candidate.trim());
	}
	
	@When("click on javascript href function $function with the parameters $parameters")
	@TestDescription(description="Look for an anchor on the page that has the js function $function and the params provided.")
	public void genericHrefClick(String function, List<String> parameters) {
		String contains = "//a[contains(@href,'" + function + "')]";		        
		
		System.out.println(contains);
		
		List<WebElement> elements = (new WebDriverWait(driver, 50))
				  .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(contains)));
		
		WebElement webElementToClick = null;
		
		for (WebElement webElement : elements) {
			String href = webElement.getAttribute("href");
			System.out.println(href);
			boolean found = true;
			for (String parameter : parameters) {
				if(!href.contains(parameter)) {
					found = false;
				}
			}
			
			if(found) {
				webElementToClick = webElement;
				break;
			}
		}
		
		if(webElementToClick != null) {
			webElementToClick.click();
		} else {
			fail("javascript anchor tag not found for function: " + function + " and parameters: " + parameters.toString());
		}
		
		//element.click();
	}
	
	private void ensureCandidateLoaded(String candidate) {
		(new WebDriverWait(driver, 30))
		  .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@href,'" + candidate + "')]")));		
	}
	
	/**
	 * When check image $image is displayed
	 * 
	 * Method to check that the image is displayed somewhere on the screen.  The image name must be supplied
	 * and the corresponding image must be placed in the test dir under a directory named after the jira 
	 * number.
	 * 
	 * @param image the image name to check
	 */
	@When("check image $image is displayed")
	@TestDescription(description="Method to check that the image is displayed somewhere on the screen.  The image name must be supplied and the corresponding image must be placed in the test dir under a directory named after the jira number.")
	public void checkImageOnScreen(String image) {
		File imageFile = new File(testDir + jira + File.separator + IMAGE_DIR + File.separator + image);    
		Target imageTarget = new ImageTarget(imageFile);
		ScreenRegion s = new DesktopScreenRegion();
		ScreenRegion r = s.wait(imageTarget,60000);
		if(r == null) {
			fail("image: " + imageFile.getName() + " not found");
			return;
		}
	}
	
	private void runActivexSequence(String sequence) {
		System.out.println("In runActivexSequence: " + sequence);
		String sequencePath = "C:/dev/workspace-sts/MySel20Proj/activex/" + sequence + "/" + countryCode;
		File sequenceDir = new File(sequencePath);
		
		File[] imageFiles = sequenceDir.listFiles();
		Arrays.sort(imageFiles, new Comparator<File>() {

			public int compare(File file1, File file2) {
				String index1Suffix = StringUtils.split(file1.getName(), "_")[1];
				String index2Suffix = StringUtils.split(file2.getName(), "_")[1];
				
				int file1Index = Integer.valueOf(index1Suffix.substring(0, index1Suffix.indexOf(".")));
				int file2Index = Integer.valueOf(index2Suffix.substring(0, index2Suffix.indexOf(".")));
				
				return (file1Index < file2Index) ? -1 : 1;
			}
		});
		
		ScreenRegion s = new DesktopScreenRegion();
		for (File image : imageFiles) {
			Target imageTarget = new ImageTarget(image);
			ScreenRegion r = s.wait(imageTarget,60000);
			if(r == null) {
				fail("image: " + image.getName() + " not found");
				return;
			}
			Mouse mouse = new DesktopMouse();
			if(image.getName().contains("Click")) {
				mouse.click(r.getCenter());
			}            
		}
		
		try {
			Thread.sleep(2000);
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	
	/**
	 * When run pptx sequence $sequence
	 * 
	 * Method to run a powerpoint slide seuqence.  The powerpoint slide should contain screenshots with
	 * a command and shape around the image on screen that you wish to activate.  The slides are driven by
	 * the siku-slides lib, more can be read on the use of this at
	 * 
	 * https://code.google.com/p/sikuli-api/wiki/SikuliSlides
	 * 
	 * the sequence name provided is the name of the pptx slide that the user wishes to run without the
	 * .pptx the presentation must be saved in the test config dir under a directory named as the jira number.
	 * 
	 * @param sequence the name of the pptx presentation without the .pptx suffix
	 */
	@When("run pptx sequence $sequence")
	@TestDescription(description="Method to run a powerpoint slide sequence.  The powerpoint slide should contain screenshots with a command and shape around the image on screen that you wish to activate.  The slides are driven by the siku-slides lib, more can be read on the use of this at https://code.google.com/p/sikuli-api/wiki/SikuliSlides the sequence name provided is the name of the pptx slide that the user wishes to run without the .pptx the presentation must be saved in the test config dir under a directory named as the jira number.")
	public void runPPtxSequence(String sequence) {
		System.out.println("in new activex");
		
		Runtime rt = Runtime.getRuntime();
		
		String command = "java -jar C:/software/sikuli-slides-1.2.0.jar " +
				 testDir + File.separator + jira + File.separator + PPTX_DIR + File.separator + sequence + ".pptx -w 60000";
		
		try {
			Process pr = rt.exec(
					command);
			
			IOUtils.copy(pr.getInputStream(), System.out);
			IOUtils.copy(pr.getErrorStream(), System.out);
			pr.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	@When("select the symptoms $symptoms") 
	@TestDescription(description="Method specifically for the symptom selection page.  Provide a list of comma seperated strings that are the names of the symptoms in order you with to select.  Method will auto press continue after all selections completed.")
	public void selectSymptoms(List<String> symptoms) {
		WebElement williamWebElement;
		
		for (String symptom : symptoms) {			
			williamWebElement = this.findElement(symptom, ElementType.SPAN);			
			williamWebElement.click();		
		}

		
		this.clickTag("Continue");
	}
	
	/**
	 * Then finish on $page page
	 * 
	 * Method to assert that the test sequence has finished on the page expected.  The page name 
	 * supplied is the title name of the page
	 * 
	 * @param page the title name of the page
	 */
	@Then("finish on $page page")
	@TestDescription(description="Method to assert that the test sequence has finished on the page expected.  The page name supplied is the title name of the page")
	public void shouldReturnTo(String page) {
		ExpectedConditions.presenceOfElementLocated(By.xpath("//title[text()=\"" + page + "\"]"));
	}
	
	
	/**
	 * Then page should contain text $text in element $element
	 * 
	 * Method to asset at the end of a test sequence that the text supplied is contained in the page
	 * inside the element provided eg td, span, div 
	 * 
	 * @param text the text to look for
	 * @param element inside this element
	 */
	@Then("page should contain text $text in element $element")
	@TestDescription(description="Method to asset at the end of a test sequence that the text supplied is contained in the page inside the element provided eg td, span, div ")
	public void pageShouldContainText(String text, String element) {
		try {
		   (new WebDriverWait(driver, 10))
					  .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + element + "[text()=\"" + text + "\"]")));         
        } catch (Exception e) {
        	File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            this.copyFileToTestOutput(screenshot);
        	
            fail("The continue button should be displayed");
        }
	}
	
	@AfterStories
	public void afterStories() {
	    System.out.println("in afterStories");
	    driver.close();
	    System.out.println("closed driver");
	}
	
	private void copyFileToTestOutput(File screenshot) {
		File screenshotOut = new File(TEST_OUTPUT + (new Date()).getTime() + ".png");
		try {
			FileUtils.copyFile(screenshot, screenshotOut);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void waitForPage(WebDriver driver, final String pageTitle) {
		(new WebDriverWait(driver, 50)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().toLowerCase()
						.startsWith(pageTitle.toLowerCase());
			}
		});
	}
	
	private WebElement findElement(String toFind, ElementType elementType) {
		WebElement element = null;
		int cycle = 1;
		int condition = 1;
		
		while (cycle < 30) {
			try {
				if(elementType == ElementType.ID || condition == 1) {
					element = (new WebDriverWait(driver, 1))
					  .until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));
				}
				if(elementType == ElementType.NAME || condition == 2) {
					element = (new WebDriverWait(driver, 1))
					  .until(ExpectedConditions.presenceOfElementLocated(By.name(toFind)));
				}
				if(elementType == ElementType.ANCHOR || condition == 3) {
					element = (new WebDriverWait(driver, 1))
					  .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[normalize-space(text())=\"" + toFind + "\"]")));
				}
				if(elementType == ElementType.SPAN || condition == 4) {
					element = (new WebDriverWait(driver, 1))
					  .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space(text())=\"" + toFind + "\"]")));
				}
				
				return element;
				
			} catch (TimeoutException e) {
				cycle++;
				if(condition == 4) {
					condition = 1;				
				} else {
					condition++;
				}
			} catch (Exception e) {
				e.printStackTrace();
				fail("findElement failed on non timeout exception");
			}
		}
		
		throw new TimeoutException();
	}
	
}
