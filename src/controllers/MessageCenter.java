package controllers;

import java.sql.Timestamp;

import javax.swing.JTextArea;

public class MessageCenter {
	private static JTextArea messageCenterTextArea;
	private static JTextArea messageSideTextArea;
	private static boolean isAutoscroll = true;
	private static int maxLinesCenterLog = 5000;
	private static int maxLinesSideLog = 1000;
	private static int centerLogLinesCounter = 0;
	private static int sideLogLinesCounter = 0;
	private static Timestamp timestamp;

	public static String getAllCenterMessages() {
		if (messageCenterTextArea != null) {
			return messageCenterTextArea.getText();
		}
		return "";
	}

	public static String getAllSideMessages() {
		if (messageSideTextArea != null) {
			return messageSideTextArea.getText();
		}
		return "";
	}

	public static void setMessageCenterTextArea(JTextArea messageTextArea) {
		MessageCenter.messageCenterTextArea = messageTextArea;
	}

	public static void setMessageSideTextArea(JTextArea messageTextArea) {
		MessageCenter.messageSideTextArea = messageTextArea;
	}

	public static void clearCenterLog() {
		MessageCenter.messageCenterTextArea.setText("");
	}

	public static void clearSideLog() {
		MessageCenter.messageSideTextArea.setText("");
	}

	public static void toggleAutoscroll() {
		if (isAutoscroll) {
			isAutoscroll = false;
		} else {
			isAutoscroll = true;
		}
	}

	public static boolean isAutoscrollEnabled() {
		return isAutoscroll;
	}

	public static void appendMessageToCenterLog(String message) {
		if (MessageCenter.messageCenterTextArea != null) {
			if (centerLogLinesCounter > maxLinesCenterLog) {
				clearCenterLog();
				centerLogLinesCounter = 0;
			}
			MessageCenter.messageCenterTextArea.append("\n" + appendTimestamp(message));
			if (isAutoscroll) {
				MessageCenter.messageCenterTextArea
						.setCaretPosition(MessageCenter.messageCenterTextArea.getDocument().getLength());
			}
			centerLogLinesCounter += 1;
		}
	}

	public static void appendMessageToSideLog(String message) {
		if (MessageCenter.messageSideTextArea != null) {
			if (sideLogLinesCounter > maxLinesSideLog) {
				clearSideLog();
				sideLogLinesCounter = 0;
			}
			MessageCenter.messageSideTextArea.append("\n" + appendTimestamp(message));
			if (isAutoscroll) {
				MessageCenter.messageSideTextArea
						.setCaretPosition(MessageCenter.messageSideTextArea.getDocument().getLength());
			}
			sideLogLinesCounter += 1;
		}
	}

	private static String appendTimestamp(String message) {
		timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp + " - " + message;
	}
}
