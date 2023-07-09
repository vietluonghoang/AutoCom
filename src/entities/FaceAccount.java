package entities;

import java.util.ArrayList;

import controllers.MessageCenter;

public class FaceAccount {
	private String profileLink;
	private ArrayList<FaceAccount> friendsList;

	public FaceAccount(String profileLink) {
		super();
		this.profileLink = profileLink;
		friendsList = new ArrayList<>();
		MessageCenter.appendMessageToCenterLog("\t- account created: " + profileLink);
	}

	public String getProfileLink() {
		return profileLink;
	}

	public void setProfileLink(String profileLink) {
		this.profileLink = profileLink;
	}

	public String getFriendsLink() {
		if (profileLink.substring(profileLink.length() - 1).equals("/")) {
			return profileLink + "friends";
		} else {
			return profileLink + "/friends";
		}
	}

	public ArrayList<FaceAccount> getFriendsList() {
		return friendsList;
	}

	public void setFriendsList(ArrayList<FaceAccount> friendsList) {
		this.friendsList = friendsList;
	}

	public FaceAccount getFriendByLink(String profileLink) {
		for (FaceAccount faceAccount : friendsList) {
			if (profileLink.equals(faceAccount.getProfileLink())) {
				return faceAccount;
			}
		}
		return null;
	}

	public void addFriendToList(FaceAccount acc) {
		friendsList.add(acc);
	}
}
