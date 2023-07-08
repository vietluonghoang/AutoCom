package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class Utils {

	public String javascriptExecuter(WebDriver driver, String script) {
		if (driver instanceof JavascriptExecutor) {
			String result = (String) ((JavascriptExecutor) driver).executeScript(script);
			return result;
		} else {
			throw new IllegalStateException("This driver does not support JavaScript!");
		}
	}

	public String getCurrentFrame(WebDriver driver) {
		return javascriptExecuter(driver, "return self.name");
	}

	public void scrollToElement(WebElement element, WebDriver driver) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.perform();
	}

	public void scrollElementToViewport(WebElement element, WebDriver driver) {
		String script = "arguments[0].scrollIntoView();";
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript(script, element);
		} else {
			throw new IllegalStateException("This driver does not support JavaScript!");
		}
	}

	public void scrollElementToViewport(WebElement element, WebDriver driver, boolean isToTop) {
		String script = "arguments[0].scrollIntoView(true);";
		if (!isToTop) {
			script = "arguments[0].scrollIntoView(false);";
		}
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript(script, element);
		} else {
			throw new IllegalStateException("This driver does not support JavaScript!");
		}
	}

	public void scrollElementToViewportThenClick(WebElement element, WebDriver driver, boolean isToTop) {
		String script = "arguments[0].scrollIntoView(true);";
		if (!isToTop) {
			script = "arguments[0].scrollIntoView(false);";
		}
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript(script, element);
		} else {
			throw new IllegalStateException("This driver does not support JavaScript!");
		}
		element.click();
	}

	public void scrollToTop(WebDriver driver) {
		javascriptExecuter(driver, "document.documentElement.scrollTop = 0;");
	}

	public void scrollToElement(WebDriver driver, WebElement scrollWrapper, WebElement elementToScroll) {
		String script = "arguments[0].scrollTop = arguments[1].offsetTop;";
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript(script, scrollWrapper, elementToScroll);
		} else {
			throw new IllegalStateException("This driver does not support JavaScript!");
		}
	}

	public void scrollToElementByArrowKeyThenClick(WebElement elementToClick, WebElement elementToScroll) {
		boolean isClicked = false;
		while (!isClicked) {
			try {
				elementToClick.click();
				isClicked = true;
			} catch (Exception e) {
				// TODO: handle exception
				elementToScroll.click();
				elementToScroll.sendKeys(Keys.ARROW_UP);
			}
		}

	}

	public void mouseHoverOver(WebElement element, WebDriver driver) {
		// Instantiate Action Class
		Actions actions = new Actions(driver);
		// Mouse hover over element
		actions.moveToElement(element).perform();
	}

	public void openUrlWithTimeout(WebDriver driver, String url, int timeout) {
		driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
		try {
			driver.get(url);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	public String removeInvisibleCharacters(String input) {
		String output = input.trim().replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "");
		return output;
	}

	public String escapeQuotationMarkForXpath(String input) {
		String[] splittedText = input.trim().split("'");
		String output = "";
		for (String text : splittedText) {
			output += ",\\\"'\\\",\\\"" + text + "\\\"";
		}
		return output.replaceAll("^,\\\\\\\"'\\\\\\\",\\\\\\\"", "\"");
	}

	public String copyFileToTempDir(String fileName) throws IOException {
		String tempDir = System.getProperty("java.io.tmpdir");
		tempDir += File.separator + "tempDir";
		File dir = new File(tempDir);
		if (!dir.exists()) {
			dir.mkdir();
		}

		File file = new File(dir.getAbsoluteFile(), fileName);
		if (!file.exists()) {
			InputStream is = (getClass().getResourceAsStream(File.separator + fileName));
			Files.copy(is, file.getAbsoluteFile().toPath());
		} else {
		}
		file.setExecutable(true);
		file.deleteOnExit();
		return file.getAbsolutePath();
	}

	public String copyFileToTempDir(InputStream is, String fileName) throws IOException {
		String tempDir = System.getProperty("java.io.tmpdir");
		tempDir += File.separator + "tempDir";
		File dir = new File(tempDir);
		if (!dir.exists()) {
			dir.mkdir();
		}

		File file = new File(dir.getAbsoluteFile(), fileName);
		if (!file.exists()) {
			Files.copy(is, file.getAbsoluteFile().toPath());
		} else {
			Files.copy(is, file.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
		}

		file.setExecutable(true);
		file.deleteOnExit();
		return file.getAbsolutePath();
	}

	public String copyFileToTempDir(Path sourceFilePath, String fileName) throws IOException {
		String tempDir = System.getProperty("java.io.tmpdir");
		tempDir += File.separator + "tempDir";
		File dir = new File(tempDir);
		if (!dir.exists()) {
			dir.mkdir();
		}

		File file = new File(dir.getAbsoluteFile(), fileName);
		if (!file.exists()) {
			Files.copy(sourceFilePath, file.getAbsoluteFile().toPath());
		} else {
			Files.copy(sourceFilePath, file.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
		}

		file.setExecutable(true);
		file.deleteOnExit();
		return file.getAbsolutePath();
	}

	public ImageIcon getImageIconFromResourceFile(String fileName) throws IOException {
		ImageIcon icon;
		InputStream stream = getClass().getResourceAsStream(File.separator + fileName);
		if (stream != null) {
			icon = new ImageIcon(ImageIO.read(stream));
			return icon;
		} else {
			return null;
		}

	}

	public BufferedImage getBufferedImageFromResourceFile(String fileName) throws IOException {
		BufferedImage icon;
		InputStream stream = getClass().getResourceAsStream(File.separator + fileName);
		if (stream != null) {
			icon = ImageIO.read(stream);
			return icon;
		} else {
			return null;
		}

	}

	public long getNumberOfDaySinceEpochTime() {
		LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
		LocalDateTime then = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0);
		Duration duration = Duration.between(then, now);

		return duration.toDays();
	}

	public long getNumberOfDaySinceEpochTime(String dateInDDMMYYYFormat, String separator) {
		String[] inputDate = dateInDDMMYYYFormat.trim().split(separator);
		LocalDateTime now = LocalDateTime.of(Integer.parseInt(inputDate[2]), Integer.parseInt(inputDate[1]),
				Integer.parseInt(inputDate[0]), 0, 0);
		LocalDateTime then = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0);
		Duration duration = Duration.between(then, now);

		return duration.toDays();
	}

	public String getLast2Digits(String result) {
		String populatedResult = removeInvisibleCharacters(result.trim()).trim().toLowerCase();

		return populatedResult.substring(populatedResult.length() - 2);
	}

}
