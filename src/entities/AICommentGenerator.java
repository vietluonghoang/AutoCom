package entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import controllers.DriverCenter;
import controllers.MessageCenter;
import utils.WaitFor;

public class AICommentGenerator {

	private String topic = "Bạn hãy kể một câu chuyện ngắn khoảng 100 từ để truyền cảm hứng về triết lý cuộc sống gia đình";
	private HashMap<String, ArrayList<String>> responses;
	private WebDriver driver;
	private float commentGeneratingTimeout = 180f;
	private float commentGeneratingInterval = 2f;
	private int minCommentLength = 10;
	private String komentorCenterUrl = "https://app.wordware.ai/share/41551601-ac8e-4320-a73b-892fd9205c24/playground";
	private String xpathToTopicTextArea = "//textarea[@name='topic']";
	private String xpathToRunAppButton = "//button[text() = 'Run App']";
	private String xpathToFinishedTag = "//div[text() = 'Finished']";
	private String xpathToComments = "//pre/div/div/div[./span/span/span[text() = 'comments: ']]/div/div/div/div/span";

	public AICommentGenerator(String topic) throws IOException {
		super();
		this.topic = topic;
		if (this.driver == null) {
			driver = DriverCenter.getNewFirefoxDriver();
		}
	}

	public AICommentGenerator() throws IOException {
		super();
		if (this.driver == null) {
			driver = DriverCenter.getNewFirefoxDriver();
		}
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public HashMap<String, ArrayList<String>> getResponses() throws Exception {
		if (responses == null) {
			generateComments(topic);
		}
		return responses;
	}

	private void addResponse(String points, String response) {
		if (responses == null) {
			responses = new HashMap<>();
		}
		ArrayList<String> comments = responses.get(points);
		if (comments == null) {
			comments = new ArrayList<>();
		}

		comments.add(response.replaceAll("^\"|\"$", ""));
		responses.put(points, comments);
	}

	private void resetResponse() {
		responses = new HashMap<>();
	}

	public void generateComments(String topic) throws Exception {

		if (!topic.isBlank()) {
			this.topic = topic;
		}
		MessageCenter.appendMessageToCenterLog("[AIComGen] - init topic: " + this.topic);
		MessageCenter.appendMessageToCenterLog("[AIComGen] - Resetting responses....");
		resetResponse();

		driver.get(komentorCenterUrl);
		WebElement taTopic = driver.findElement(By.xpath(xpathToTopicTextArea));
		MessageCenter.appendMessageToCenterLog("[AIComGen] - Clearing topic input...");
		taTopic.clear();
		MessageCenter.appendMessageToCenterLog("[AIComGen] - Adding topic... \n" + getTopic());
		taTopic.sendKeys(getTopic());
		WebElement btnRunApp = driver.findElement(By.xpath(xpathToRunAppButton));
		MessageCenter.appendMessageToCenterLog("[AIComGen] - Running...");
		btnRunApp.click();

		MessageCenter.appendMessageToCenterLog("[AIComGen] - Waiting for generating comments...");
		WaitFor wait = new WaitFor(commentGeneratingTimeout, commentGeneratingInterval);
		wait.waitForVisibilityByXpath(driver, xpathToFinishedTag);
		MessageCenter.appendMessageToCenterLog("[AIComGen] - Comments generated...");

		// it's difficult to specify the number of points and which comments are for a
		// point. Thus, all points are set to "a" temporarilly
		for (WebElement com : driver.findElements(By.xpath(xpathToComments))) {
			if (!com.getText().isBlank() && com.getText().length() > minCommentLength) {
				MessageCenter.appendMessageToCenterLog(
						"[AIComGen] - Adding comments... (" + com.getText().length() + ")\n" + com.getText());
				addResponse("a", com.getText());
			}
		}
	}

}
