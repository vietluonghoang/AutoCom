package controllers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import drivers.RunningDriver;

public class DriverCenter {
	private static HashMap<WebDriver, Boolean> driversList;

	public static HashMap<WebDriver, Boolean> getDriversList() {
		if (driversList == null) {
			driversList = new HashMap<WebDriver, Boolean>();
		}
		return driversList;
	}

	public static void terminateAllDrivers() {
		for (WebDriver d : getDriversList().keySet()) {
			if (!d.toString().contains("null")) {
				d.quit();
			}
		}
	}
	
	public static WebDriver getNewChromeDriver() throws IOException {
		WebDriver driver = new RunningDriver().chromeDriver();
		appendDriver(driver);
		return driver;
	}
	public static WebDriver getNewChromeDriver(String pathToProfileDir) throws IOException {
		WebDriver driver = new RunningDriver().chromeDriver(pathToProfileDir);
		appendDriver(driver);
		return driver;
	}
	
	public static WebDriver getNewFirefoxDriver() throws IOException {
		WebDriver driver = new RunningDriver().firefoxDriver();
		appendDriver(driver);
		return driver;
	}
	public static WebDriver getNewFirefoxDriver(String pathToProfileDir) throws IOException {
		WebDriver driver = new RunningDriver().firefoxDriver(pathToProfileDir);
		appendDriver(driver);
		return driver;
	}

	private static void appendDriver(WebDriver driver) {
		getDriversList().put(driver, true);
	}
}
