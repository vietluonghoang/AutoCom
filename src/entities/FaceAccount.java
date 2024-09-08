package entities;

import java.util.ArrayList;
import java.util.HashMap;

import controllers.MessageCenter;

public class FaceAccount {
	private String profileLink;
	private boolean isCommunityProfile;
	private ArrayList<FaceAccount> friendsList;
	private HashMap<String, FacePost> postsList;

	public FaceAccount(String profileLink, boolean isCommunity) {
		super();
		this.profileLink = profileLink;
		this.isCommunityProfile = isCommunity;
		friendsList = new ArrayList<>();
		MessageCenter.appendMessageToCenterLog("[FB Acc] account created (" + isCommunityProfile + "): " + profileLink);
	}

	public boolean isCommunityProfile() {
		return isCommunityProfile;
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

	public HashMap<String, FacePost> getPostsList() {
		if (postsList == null) {
			postsList = new HashMap<>();
		}
		return postsList;
	}

	public FacePost getPostByURL(String url) {
		if (postsList == null) {
			postsList = new HashMap<>();
		}
		return postsList.get(url);
	}

	public void addPostsToList(FacePost post) {
		if (postsList == null) {
			postsList = new HashMap<>();
			postsList.put(post.getUrl(), post);
		} else if (postsList.get(post.getUrl()) == null) {
			postsList.put(post.getUrl(), post);
		} else {
			postsList.get(post.getUrl()).setUrl(post.getUrl());
			postsList.get(post.getUrl()).setCaption(post.getCaption());
			postsList.get(post.getUrl()).setPostElement(post.getPostElement());
		}
	}

}
