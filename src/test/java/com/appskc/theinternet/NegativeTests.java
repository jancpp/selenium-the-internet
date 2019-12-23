package com.appskc.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NegativeTests {

	@Test
	void incorrectUsernameTest() {
		System.out.println("Starting incorrectUsernameTest");

//		create driver
		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
		WebDriver driver = new ChromeDriver();

//		maximize browser window
		driver.manage().window().maximize();

//		open test page
		String url = "https://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Page is open.");

//		enter username
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("incorrectUsername");

//		enter password
		WebElement password = driver.findElement(By.name("password"));
		password.sendKeys("SuperSecretPassword!");

//		click login button
		WebElement loginButton = driver.findElement(By.tagName("button"));
		loginButton.click();

//		verifications:
//		unsuccessful login message when invalid username is ente
		WebElement errorMessage = driver.findElement(By.id("flash"));
		String expectedMessage = "Your username is invalid!";
		String actualMessage = errorMessage.getText();
		
		Assert.assertTrue(actualMessage.contains(expectedMessage), "Actual message does not contain expected message."
				+ "\nActual message: " + actualMessage + "\nExpected message: " + expectedMessage);

//		close browser
		driver.quit();
	}

	@Test
	void incorrectPasswordTest() {
		System.out.println("Starting incorrectPasswordTest");

//		create driver
		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
		WebDriver driver = new ChromeDriver();

//		maximize browser window
		driver.manage().window().maximize();

//		open test page
		String url = "https://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Page is open.");

//		enter username
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("tomsmith");

//		enter password
		WebElement password = driver.findElement(By.name("password"));
		password.sendKeys("IncorrectPassword!");

//		click login button
		WebElement loginButton = driver.findElement(By.tagName("button"));
		loginButton.click();

//		verifications:
//		unsuccessful login message
		WebElement errorMessage = driver.findElement(By.xpath("//div[@id='flash']"));
		String expectedMessage = "Your password is invalid!";
		String actualMessage = errorMessage.getText();
		Assert.assertTrue(actualMessage.contains(expectedMessage), "Actual message does not contain expected message."
				+ "\nActual message: " + actualMessage 
				+ "\nExpected message: " + expectedMessage);

//		close browser
		driver.quit();
	}
}
