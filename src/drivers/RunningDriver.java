package drivers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import utils.Utils;

public class RunningDriver {
	private Utils utils = new Utils();

	private String pathToChromeDriver() throws IOException {
		String executableDriverFileName = "chromedriver";

		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			executableDriverFileName = "chromedriver.exe";
		}
		String pathToChromeDriverExecutableFile = utils.copyFileToTempDir(executableDriverFileName);
		System.out.println("pathToChromeDriverExecutableFile: " + pathToChromeDriverExecutableFile);

		return pathToChromeDriverExecutableFile;
	}

	public WebDriver firefoxDriver() {
		System.out.println("--- Initializing default Firefox browser");
//		System.setProperty("webdriver.chrome.driver", pathToChromeDriver());
		FirefoxOptions options = new FirefoxOptions();
		options.addArguments("--disable-browser-side-navigation");
		options.addArguments("--disable-gpu");
		WebDriver driver = new FirefoxDriver(options);
		return driver;
	}

	//initialize WebDriver using existing profile
	//to get profile path for Firefox, open a new tap, type "about:profiles" to the address bar, then can take the path from the Profile Path row
	public WebDriver firefoxDriver(String profilePath) {
		System.out.println("--- Initializing Chrome browser with existing profile\n" + profilePath );

		FirefoxOptions capabilities = new FirefoxOptions();
		capabilities.addArguments("-profile", profilePath);
		WebDriver driver = new FirefoxDriver(capabilities);
		return driver;
	}

	public WebDriver chromeDriver() throws IOException {
		System.out.println("--- Initializing default Chrome browser");
//		System.setProperty("webdriver.chrome.driver", pathToChromeDriver());
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-browser-side-navigation");
		options.addArguments("--disable-gpu");
		WebDriver driver = new ChromeDriver(options);
		return driver;
	}

	//initialize WebDriver using existing profile
	//to get profile path for Chrome, open a new tap, type "chrome://version" to the address bar, then can take the path from the Profile Path row
	public WebDriver chromeDriver(String profilePath) throws IOException {
		String profileDir = profilePath.substring(profilePath.lastIndexOf("/") + 1);
		String pathToProfileDir = profilePath.replace(profileDir, "");
		System.out.println("--- Initializing Chrome browser with existing profile\n" + profilePath + "\n"
				+ pathToProfileDir + "\n" + profileDir);
//		System.setProperty("webdriver.chrome.driver", pathToChromeDriver());
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--user-data-dir=" + pathToProfileDir);
		options.addArguments("--profile-directory=" + profileDir);
		options.addArguments("--disable-extensions");
		options.addArguments("--disable-browser-side-navigation");
		options.addArguments("--disable-gpu");
		WebDriver driver = new ChromeDriver(options);
		return driver;
	}
}
