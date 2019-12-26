package com.appskc.theinternet;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ExceptionTests {

	private WebDriver driver;

	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(@Optional String browser) {
		// create driver
		switch (browser) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
			driver = new ChromeDriver();
			break;

		case "firefox":
			System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver");
			driver = new FirefoxDriver();
			break;

		default:
			System.out.println("Do not know how to start " + browser + ", starting Chrome instead");
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
			driver = new ChromeDriver();
			break;
		}

		// maximize browser window
		driver.manage().window().maximize();
	}

	@Test
	public void notVisibleTest() {
		// open page https://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");

		// find locator for the startButton and click on it
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button"));
		startButton.click();

		// get finish element text
		WebElement finishElement = driver.findElement(By.id("finish"));

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // explicit delay
		wait.until(ExpectedConditions.visibilityOf(finishElement));

		String finishText = finishElement.getText();

		Assert.assertTrue(finishText.contains("Hello World!"), "Finish text: " + finishText);
	}

	@Test
	public void timeoutTest() {
		// open page
		driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");

		// find locator for the startButton and click on it
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button"));
		startButton.click();

		// get finish element text
		WebElement finishElement = driver.findElement(By.id("finish"));

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2)); // explicit delay
		try {
			wait.until(ExpectedConditions.visibilityOf(finishElement));
		} catch (TimeoutException e) {
			System.out.println("Exception caught: " + e.getMessage());
			sleep(4000);
		}

		String finishText = finishElement.getText();

		Assert.assertTrue(finishText.contains("Hello World!"), "Finish text: " + finishText);
	}

	@Test
	public void noSuchElementTest() {
		// open page
		driver.get("https://the-internet.herokuapp.com/dynamic_loading/2");

		// find locator for the startButton and click on it
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button"));
		startButton.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // explicit delay
		
		Assert.assertTrue(wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("finish"), "Hello World!")), "Could not verify expected text");
		
		
//		WebElement finishElement = driver.findElement(By.id("finish"));
//
//		String finishText = finishElement.getText();
//
//		Assert.assertTrue(finishText.contains("Hello World!"), "Finish text: " + finishText);
	}

	@Test
	public void staleElementTest() {
		driver.get("http://the-internet.herokuapp.com/dynamic_controls");

		WebElement checkbox = driver.findElement(By.id("checkbox"));
		WebElement removeButton = driver.findElement(By.xpath("//button[contains(text(),'Remove')]"));

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // explicit delay

		removeButton.click();
//		wait.until(ExpectedConditions.invisibilityOf(checkbox));
//		Assert.assertFalse(checkbox.isDisplayed());

//		Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(checkbox)),
//				"Checkbox is still visible, but shouldn't be");

		Assert.assertTrue(wait.until(ExpectedConditions.stalenessOf(checkbox)),
				"Checkbox is still visible, but shouldn't be");
		
		WebElement addButton = driver.findElement(By.xpath("//button[contains(text(),'Add')]"));
		addButton.click();
		
		WebElement checkbox2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkbox")));
		Assert.assertTrue(checkbox2.isDisplayed(), "Checkbox is not visible, but it should be");

	}

	@Test
	public void disabledElementTest() {
		driver.get("http://the-internet.herokuapp.com/dynamic_controls");

		WebElement enableButton = driver.findElement(By.xpath("//button[contains(text(),'Enable')]"));
		WebElement textField = driver.findElement(By.xpath("(//input)[2]"));

		enableButton.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
		wait.until(ExpectedConditions.elementToBeClickable(textField));

		textField.sendKeys("Hello!");
		Assert.assertEquals(textField.getAttribute("value"), "Hello!");

	}

	void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		// close browser
		driver.quit();
	}
}
