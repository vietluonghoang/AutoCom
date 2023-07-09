package controllers;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import entities.FaceAccount;
import entities.GeneralSettings;
import utils.Utils;
import utils.WaitFor;

public class FlowController implements Runnable {
	private WebDriver driver;

	private String urlHomepage = "https://www.facebook.com/";
//	private String urlTargetProfilePage = 
//	private String urlTargetProfilePage = "https://www.facebook.com/profile.php?id=100013432717257";

	private String xpathLogoutForm = "//form[contains(@action,'logout.php')]";
	private String xpathCommentBoxes = "//div[./div/div/ul/li/span/div[@aria-label='Comment with an avatar sticker']]/div/div/div/div[contains(@aria-label,'Write a comment')]";
	private String xpathFriendCard = "//div[./*/*/*/div/h2/span/a[contains(text(),'Friends')]]/div/div/div/div/a[span]";
	private FaceAccount targetAccount;
	private ArrayList<String> commentsList;
	private Utils utils = new Utils();

	public FlowController(WebDriver driver, String urlTargetProfilePage, String rawCommentsList) {
		super();
		this.driver = driver;
		targetAccount = new FaceAccount(urlTargetProfilePage);
		commentsList = new ArrayList<String>();
		generateCommentsList(rawCommentsList);
	}

	public void run() {
		// open home page
		driver.get(urlHomepage);
		WaitFor waitForLogIn = new WaitFor(30);
		try {
			waitForLogIn.waitForVisibilityByXpath(driver, xpathLogoutForm);
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		if (isLoggedIn()) {
			// get all target friends first
			collectFriendsList();
			// go through all friends account to put comments
			for (FaceAccount victim : targetAccount.getFriendsList()) {
				putComment(victim);
			}
		} else {

		}
	}

	public boolean isLoggedIn() {
		if (driver.findElements(By.xpath(xpathLogoutForm)).size() > 0) {
			MessageCenter.appendMessageToCenterLog("-- account logged in");
			return true;
		} else {
			MessageCenter.appendMessageToCenterLog("-- account hasn't logged in");
			return false;
		}

	}

	public void collectFriendsList() {
		MessageCenter.appendMessageToCenterLog("--- Checking Friends list...");
		// once logged in, open target profile page
		driver.get(targetAccount.getFriendsLink());
		// Wait for the page to load
		WaitFor wait = new WaitFor(3);
		try {
			wait.waitForTimeout();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		// scroll toward the bottom of the page to load all friends first
		utils.scrollUntilReachToBottom(driver);
		for (WebElement friend : driver.findElements(By.xpath(xpathFriendCard))) {
			targetAccount.addFriendToList(new FaceAccount(friend.getAttribute("href")));
		}
	}

	public void putComment(FaceAccount victim) {
		MessageCenter.appendMessageToCenterLog("--- Opening profile: " + victim.getProfileLink());
		driver.get(victim.getProfileLink());

		// Wait for the page to load
		WaitFor wait = new WaitFor(5);
		try {
			wait.waitForTimeout();
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		List<WebElement> commentBoxes = driver.findElements(By.xpath(xpathCommentBoxes));
		MessageCenter.appendMessageToCenterLog("\t- comment boxes available: " + commentBoxes.size());
		int commentBoxesSize = commentBoxes.size();
		for (int i = 0; i < commentBoxesSize; i++) {
			if (i >= GeneralSettings.maxComment) {
				break;
			}
			// pick random comment from the list
			String textToComment = commentsList.get(utils.getRandomNumber(0, commentsList.size() - 1));
			// Wait before comment
			wait = new WaitFor(GeneralSettings.commentInterval);
			try {
				wait.waitForTimeout();
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			commentBoxes = driver.findElements(By.xpath(xpathCommentBoxes));
			utils.scrollToElementWithOffsetThenClick(commentBoxes.get(i), driver, 150, 5);
			// Wait for the page to load
			wait = new WaitFor(5);
			try {
				wait.waitForTimeout();
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			utils.scrollToElementWithOffset(commentBoxes.get(i), driver, 200, 5);
			// add unix timestamp to comment text to avoid duplication
			textToComment += "\n" + System.currentTimeMillis() / 1000L;
			sendCharByCharToField(commentBoxes.get(i), textToComment);
			MessageCenter.appendMessageToCenterLog("+ sent comment in the boxes: \n" + commentBoxes.get(i).getText());
			utils.scrollToElementWithOffset(commentBoxes.get(i), driver, 200, 5);

			// TODO: need send action
		}
	}

	private void sendCharByCharToField(WebElement element, String textToComment) {
		char[] ch = textToComment.toCharArray();

		// Traverse the character array
		for (int i = 0; i < ch.length; i++) {
			element.sendKeys(ch[i] + "");
		}
	}

	private void generateCommentsList(String rawCommentsList) {
		MessageCenter.appendMessageToCenterLog("--- Checking comments...");
		String[] arrComments = rawCommentsList.split("\\|");
		for (int i = 0; i < arrComments.length; i++) {
			if (!arrComments[i].trim().isBlank()) {
				commentsList.add(arrComments[i].trim());
			}
		}
		for (String com : commentsList) {
			MessageCenter.appendMessageToCenterLog("\t+++ comment: " + com);
		}
	}
}
