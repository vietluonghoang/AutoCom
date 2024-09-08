package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import entities.AICommentGenerator;
import entities.FaceAccount;
import entities.FacePost;
import entities.GeneralSettings;
import utils.Utils;
import utils.WaitFor;

public class FlowController implements Runnable {
	private WebDriver driver;

	private String urlHomepage = "https://www.facebook.com/";
//	private String urlTargetProfilePage = 
//	private String urlTargetProfilePage = "https://www.facebook.com/profile.php?id=100013432717257";

	private String xpathLogoutForm = "//form[contains(@action,'logout.php')]";
	private String xpathToPost = "//div[./div/div/div/div/div/div/div/div/div/div/div/div/form/div/div/div/div/div/div/div[contains(@aria-label,'Viết bình luận') or contains(@aria-label,'Write a comment') or contains(@aria-label,'Write an answer') or contains(@aria-label,'Write a public comment')]]/div/div";
//	private String xpathCommentBoxes = "//div[./div/div/ul/li/span/div[@aria-label='Comment with an avatar sticker']]/div/div/div/div[contains(@aria-label,'Write a comment')]";
	private String xpathCommentBoxes = "div/div/div/div/div/div/div/div/div/div/form/div/div/div/div/div/div/div[contains(@aria-label,'Viết bình luận') or contains(@aria-label,'Write a comment') or contains(@aria-label,'Write an answer') or contains(@aria-label,'Write a public comment')]";
//	private String xpathFriendCard = "//div[./*/*/*/div/h2/span/a[contains(text(),'Friends')]]/div/div/div/div/a[span]";
	private String xpathFriendCard = "//div[./*/*/*/div/h2/span/a[contains(text(),'Bạn bè') or contains(text(),'Friends')]]/div/div/div/div/a[span]";
	private String xpathToAttachImageUnderCommentBoxes = "../../../div/ul/li[./span/div[contains(@aria-label,'Attach a photo or video')]]/input";
	private String xpathSendButton = "div/div/div/div/div/div/div/div/div/div/form/div/div/div/div/div/div/div[@id='focused-state-composer-submit']/span/div";
	private String rawCommentsList;
	private FaceAccount targetAccount;
	private AICommentGenerator comGen;
	private ArrayList<String> commentsList;
	private Utils utils = new Utils();

	public FlowController(WebDriver driver, String urlTargetProfilePage, boolean isCommunity, String rawCommentsList) {
		super();
		this.driver = driver;
		targetAccount = new FaceAccount(urlTargetProfilePage, isCommunity);
		commentsList = new ArrayList<String>();
		try {
			comGen = new AICommentGenerator();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.rawCommentsList = rawCommentsList;
	}

	public void run() {
		// open home page
		MessageCenter.appendMessageToCenterLog("-- Opening Facebook....");
		driver.get(urlHomepage);
		MessageCenter.appendMessageToCenterLog("-- Waiting the page to load....");
		WaitFor waitForLogIn = new WaitFor(30);
		try {
			waitForLogIn.waitForVisibilityByXpath(driver, xpathLogoutForm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MessageCenter.appendMessageToCenterLog(e.getMessage());
		}
		if (isLoggedIn()) {
			if (targetAccount.isCommunityProfile()) {
				MessageCenter.appendMessageToCenterLog("-- Community account mode");
				scanThroughPosts(targetAccount);
			} else {
				MessageCenter.appendMessageToCenterLog("-- Personal account mode");
				generateCommentsList();
				// get all target friends first
				collectFriendsList();
				// go through all friends account to put comments
				for (FaceAccount victim : targetAccount.getFriendsList()) {
					scanThroughPosts(victim);
				}
			}
		} else {

		}
	}

	private boolean isLoggedIn() {
		if (driver.findElements(By.xpath(xpathLogoutForm)).size() > 0) {
			MessageCenter.appendMessageToCenterLog("-- account logged in");
			return true;
		} else {
			MessageCenter.appendMessageToCenterLog("-- account hasn't logged in");
			return false;
		}

	}

	private void collectFriendsList() {
		MessageCenter.appendMessageToCenterLog("--- Checking Friends list...");
		// once logged in, open target profile page
		driver.get(targetAccount.getFriendsLink());
		// Wait for the page to load
		WaitFor wait = new WaitFor(3);
		try {
			wait.waitForTimeout();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MessageCenter.appendMessageToCenterLog(e.getMessage());
		}
		// scroll toward the bottom of the page to load all friends first
		utils.scrollUntilReachToBottom(driver);
		List<WebElement> friendsList = driver.findElements(By.xpath(xpathFriendCard));
		MessageCenter.appendMessageToCenterLog("--+ Friend List size: " + friendsList.size());
		for (WebElement friend : friendsList) {
			targetAccount.addFriendToList(new FaceAccount(friend.getAttribute("href"), false));
		}
	}

	private HashMap<String, ArrayList<String>> generateCommentByAI(String topic) throws Exception {
		try {
			comGen.generateComments(topic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return comGen.getResponses();
	}

	private void scanThroughPosts(FaceAccount victim) {
		MessageCenter.appendMessageToCenterLog("--- Opening profile: " + victim.getProfileLink());
		driver.get(victim.getProfileLink());

		MessageCenter.appendMessageToCenterLog("--- Waiting the page to load");
		// Wait for the page to load
		WaitFor wait = new WaitFor(5);
		try {
			wait.waitForTimeout();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MessageCenter.appendMessageToCenterLog(e.getMessage());
		}

		MessageCenter.appendMessageToCenterLog("--- Scanning...");
		int numberOfPostPopulated = 0;
		int numberOfPostsHadCommented = 0;
		int numberOfPostsFound = driver.findElements(By.xpath(xpathToPost)).size();
		MessageCenter.appendMessageToCenterLog("--- Number of Posts Found: " + numberOfPostsFound);
		while (numberOfPostPopulated < numberOfPostsFound
				&& numberOfPostsHadCommented < GeneralSettings.maxPostToComment) {

			for (WebElement post : driver.findElements(By.xpath(xpathToPost))) {
				MessageCenter.appendMessageToCenterLog("--- Number of Posts Populated: " + numberOfPostPopulated);
				MessageCenter.appendMessageToCenterLog(
						"--- Number of Posts to Comment: " + GeneralSettings.maxPostToComment);
				MessageCenter
						.appendMessageToCenterLog("--- Number of Posts had Commented: " + numberOfPostsHadCommented);
				if (numberOfPostsHadCommented > GeneralSettings.maxPostToComment) {
					MessageCenter.appendMessageToCenterLog("--- Max Posts to Comment has been reached");
					break;
				}
				MessageCenter.appendMessageToCenterLog("-- Populating posts....");
				FacePost fPost = new FacePost(post);
				MessageCenter.appendMessageToCenterLog("-- Scanning for URL....");
				for (WebElement url : post.findElements(By.xpath("div[2]/div/div[2]/div/div/span/div/span/span/a"))) {
					MessageCenter.appendMessageToCenterLog("-- URL found. Scrolling to the post....");
					utils.scrollToElementWithOffset(url, driver, 150, 1);
					// post url won't be updated until the mouse hover the link
					utils.mouseHoverOver(url, driver);
					fPost.setUrl(url.getAttribute("href").split("\\?")[0]);
					MessageCenter.appendMessageToCenterLog("url: \n" + fPost.getUrl());
				}
				if (fPost.getUrl().length() > 0) {
					MessageCenter.appendMessageToCenterLog("-- Scanning for caption....");
					for (WebElement caption : post.findElements(By.xpath("div[3]/div[1]/div/div/div/span"))) {
						// check if the caption hasn't been fully visible
						MessageCenter
								.appendMessageToCenterLog("-- Caption found. Checking if the caption appears fully...");
						for (WebElement seemore : caption
								.findElements(By.xpath("div/div/div[@role='button' and text()='See more']"))) {
							MessageCenter.appendMessageToCenterLog("-- Expanding the caption to appear fully...");
							utils.scrollToElementWithOffsetThenClick(seemore, driver, 150, 1);
						}
						fPost.setCaption(caption.getText());
						MessageCenter.appendMessageToCenterLog("caption: \n" + fPost.getCaption());
						if (fPost.getCaption().length() > 0) {
							MessageCenter.appendMessageToCenterLog("-- Checking if the post is a new one...");
							if (victim.getPostByURL(fPost.getUrl()) == null) {
								MessageCenter.appendMessageToCenterLog("-- New post found...");
								// if this is the new post, put a comment and add to the list of posts
								try {
									putComment(fPost);
									victim.addPostsToList(fPost);
									numberOfPostsHadCommented++;
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// if this is an existing post, do nothing
								MessageCenter.appendMessageToCenterLog("-- An existing post found...");
							}
						}
					}
				}
				numberOfPostPopulated++;
			}
			MessageCenter.appendMessageToCenterLog("--- Scrolling to bottom...");
			utils.scrollToBottom(driver);
			MessageCenter.appendMessageToCenterLog("--- Waiting the page to load");
			// Wait for the page to load
			wait = new WaitFor(5);
			numberOfPostsFound = driver.findElements(By.xpath(xpathToPost)).size();
			MessageCenter.appendMessageToCenterLog("--- Number of Posts Found: " + numberOfPostsFound);
		}
		MessageCenter.appendMessageToCenterLog(
				"-- Scanning through completed. Number of posts had comments: " + numberOfPostsHadCommented);
		MessageCenter.appendMessageToCenterLog("-------------------");
	}

	private void putComment(FacePost post) throws Exception {
		MessageCenter.appendMessageToCenterLog("-- Putting comment...");
		MessageCenter.appendMessageToCenterLog("--- Waiting the page to load");
		// Wait for the page to load
		WaitFor wait = new WaitFor(1);

		HashMap<String, ArrayList<String>> responses = generateCommentByAI(post.getCaption());
		int pointsSize = responses.keySet().size();
		int numberOfCommentSent = 0;
		String textToComment = "";

		MessageCenter.appendMessageToCenterLog("-- Number of points: " + pointsSize);
		MessageCenter.appendMessageToCenterLog("-- Number of comments sent: " + numberOfCommentSent);
		MessageCenter.appendMessageToCenterLog("-- Max number of comments to sent: " + GeneralSettings.maxComment);

		// TODO: need more investigation on sending multiple comments in a row
		while (numberOfCommentSent < GeneralSettings.maxComment) {
			int randomIndexForPoint = utils.getRandomNumber(1, pointsSize);
			MessageCenter.appendMessageToCenterLog("-- Randomized index of points: " + randomIndexForPoint);
			int pointIndex = 0;
			for (String point : responses.keySet()) {
				pointIndex++;
				if (pointIndex == randomIndexForPoint) {
					int randomIndexForResponse = utils.getRandomNumber(0, responses.get(point).size() - 1);
					MessageCenter
							.appendMessageToCenterLog("-- Randomized index of response: " + randomIndexForResponse);
					textToComment = responses.get(point).get(randomIndexForResponse);
				}
			}
			MessageCenter.appendMessageToCenterLog("-- Comment to sent: \n" + textToComment);
			for (WebElement commentBox : post.getPostElement().findElements(By.xpath(xpathCommentBoxes))) {
				MessageCenter.appendMessageToCenterLog("-- Scrolling to the comment box....");
				utils.scrollToElementWithOffsetThenClick(commentBox, driver, 150, 2);
				MessageCenter.appendMessageToCenterLog("-- Sending comment...");
				sendCharByCharToField(commentBox, textToComment);
			}
			numberOfCommentSent++;
		}
//		for (int i = 0; i < commentBoxesSize; i++) {
//			if (i >= GeneralSettings.maxComment) {
//				break;
//			}
//			// pick random comment from the list
//			String textToComment = commentsList.get(utils.getRandomNumber(0, commentsList.size() - 1));
//			// Wait before comment
//			wait = new WaitFor(5);
//			try {
//				wait.waitForTimeout();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				MessageCenter.appendMessageToCenterLog(e.getMessage());
//			}
//			commentBoxes = driver.findElements(By.xpath(xpathCommentBoxes));
//			utils.scrollToElementWithOffsetThenClick(commentBoxes.get(i), driver, 150, 5);
//			// Wait for the page to load
//			wait = new WaitFor(5);
//			try {
//				wait.waitForTimeout();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				MessageCenter.appendMessageToCenterLog(e.getMessage());
//			}
//
//			// send text to the comment box
//			utils.scrollToElementWithOffset(commentBoxes.get(i), driver, 200, 5);
//			// add unix timestamp to comment text to avoid duplication
//			textToComment += "\n" + System.currentTimeMillis() / 1000L;
//			sendCharByCharToField(commentBoxes.get(i), textToComment);
//			MessageCenter.appendMessageToCenterLog("+ sent comment in the boxes: \n" + commentBoxes.get(i).getText());
//			utils.scrollToElementWithOffset(commentBoxes.get(i), driver, 200, 5);

		// Upload image
//			List<WebElement> attachImageButtons = commentBoxes.get(i)
//					.findElements(By.xpath(xpathToAttachImageUnderCommentBoxes));
//			MessageCenter.appendMessageToCenterLog("+ finding the attach image button: " + attachImageButtons.size());
//
//			MessageCenter.appendMessageToCenterLog("--- Scanning images folder: " + GeneralSettings.pathToImagesFolder);
//			ArrayList<String> expectedFileExtensions = new ArrayList<>();
//			expectedFileExtensions.add("png");
//			expectedFileExtensions.add("jpg");
//			expectedFileExtensions.add("jpeg");
//			ArrayList<String> pathToImages = utils.getAllFileFromFolderByExtension(GeneralSettings.pathToImagesFolder,
//					expectedFileExtensions);
//
//			for (WebElement attImgBtn : attachImageButtons) {
//				// randomize images
//				String imagePath = pathToImages.get(utils.getRandomNumber(0, pathToImages.size() - 1));
//				MessageCenter.appendMessageToCenterLog("--- Attaching image: \n\t" + imagePath);
//				attImgBtn.sendKeys(imagePath);
//			}
//
//			// Wait for the image to upload
//			wait = new WaitFor(30);
//			try {
//				wait.waitForTimeout();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				MessageCenter.appendMessageToCenterLog(e.getMessage());
//			}

		// click on the send comment button
		for (WebElement sendButton : post.getPostElement().findElements(By.xpath(xpathSendButton))) {
			MessageCenter.appendMessageToCenterLog("-- Send button found. Clicking...");
//				sendButton.click();
		}
		// delay after sending a comment
		wait = new WaitFor(GeneralSettings.commentInterval);
		try {
			wait.waitForTimeout();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MessageCenter.appendMessageToCenterLog(e.getMessage());
		}
	}

	private void sendCharByCharToField(WebElement element, String textToComment) {
		char[] ch = textToComment.toCharArray();

		// Traverse the character array
		for (int i = 0; i < ch.length; i++) {
			element.sendKeys(ch[i] + "");
		}
	}

	private void generateCommentsList() {
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
