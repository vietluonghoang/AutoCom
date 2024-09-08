package entities;

import org.openqa.selenium.WebElement;

public class FacePost {

	private String url = "";
	private String caption = "";
	private WebElement postElement;

	public FacePost(String url, String caption, WebElement postElement) {
		super();
		this.url = url;
		this.caption = caption;
		this.postElement = postElement;
	}

	public FacePost(String url, String caption) {
		super();
		this.url = url;
		this.caption = caption;
	}

	public FacePost(String url) {
		super();
		this.url = url;
	}

	public FacePost(WebElement postElement) {
		super();
		this.postElement = postElement;
	}

	public FacePost() {
		super();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public WebElement getPostElement() {
		return postElement;
	}

	public void setPostElement(WebElement postElement) {
		this.postElement = postElement;
	}

}
