//package org.seleniumhq.selenium.selenium_java;
//This is a program to scrape my grades from the Calvert Hall website and write them to a .csv file for future use
//Written By: Michael West, mwester1111@gmail.com, www.github.com/Season5Ryze
import static org.junit.jupiter.api.Assertions.*;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

class login_school {
	//Create a function that logs in to the school website and then returns the instance of the driver used, pre-loaded to the grades page
	public static WebDriver page_loading() {
		//point to the location of chromedriver, create a new driver
        System.setProperty("webdriver.chrome.driver", "<Path to ChromeDriver>");
        WebDriver driver = new ChromeDriver();
        
        //get the Calvert Hall website
        driver.get("https://calverthall.myschoolapp.com/app#login");
        
        //Initialize our default wait period, this will be used frequently to ensure critical elements load
        WebDriverWait wait = new WebDriverWait(driver, 10);
        
        //Send username and password keys to the login page
        WebElement usernameBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Username")));
        usernameBox.sendKeys("<My Username>");
        WebElement nextBox = driver.findElement(By.id("nextBtn"));
        nextBox.click();
        WebElement passwordBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("i0118")));
        passwordBox.sendKeys("<My Password>");
        WebElement signInBox = driver.findElement(By.id("idSIButton9"));
        signInBox.click();
        WebElement stayBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idSIButton9")));
        stayBox.click();
        
        //navigate to the progress tab and show grades for scraping
        WebElement gradesBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("progress-btn")));
        gradesBox.click();
        WebElement showButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"showHideGrade\"]/div/label[1]")));
        showButton.click();
        return driver;
        // driver.quit();
}
	@Test
	public void scrape() throws IOException{
		//Today's date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");  
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now);
		
		//Number of Classes I am enrolled in
		int class_num = 18;
		
		//Check to see if the csv already exists, this boolean determines what lines we write later on
		boolean exist = true;
		File csv = new File("<Location of .csv>");
		if(csv.exists() != true) {
			exist = false;
		}
		
		//Open the file in append mode
		FileWriter csvWriter = new FileWriter("<Location of .csv>", true);
		
		//Create String arrays to store our grades and classes
		String[] classes = new String[18];
		String[] grades = new String[18];
		
		//Run Our Login function and return the webdriver instance for grade procuring
		WebDriver driver = page_loading();
		
		//Get all of our classes and grades, for grades we chop off the percentage sign
		for(int i = 1; class_num >= i; i++) {
	    	String classXPath = MessageFormat.format("//*[@id=\"coursesContainer\"]/div[{0}]/div[1]/a/h3", i);
	    	WebElement className = driver.findElement(By.xpath(classXPath));
	    	String name = className.getText();
	    	String gradeXPath = MessageFormat.format("//*[@id=\"coursesContainer\"]/div[{0}]/div[4]/h3", i);
	    	WebElement classGrade = driver.findElement(By.xpath(gradeXPath));
	    	String grade = classGrade.getText();
	    	if(grade != "--"){
	    		grade = grade.substring(0, grade.length() - 1);
	    	}
	    	classes[i-1] = name;
	    	grades[i-1] = grade;
	    	System.out.println(classes[i-1] + " " + grades[i-1]);
	    	}
		
		//If the file didn't exist prior to the program running, we write the header in first before the data,
		//else, the data is written in a new row
		if(exist == false) {
			csvWriter.append("Date,");
			for(int i = 0; classes.length > i; i++) {
				csvWriter.append(classes[i] + ",");
			}			
		}
		csvWriter.append("\n"+ date + ",");
		for(int i = 0; grades.length > i; i++) {
			csvWriter.append(grades[i] + ",");
		}
		//Flush and close the file
		csvWriter.flush();
		csvWriter.close();
		}
}
