package jp.leopanda.articleSpreader.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AmebaCredential implements Serializable{
	
	private String userId;
	private String password;
	private String xWsse;
	private String postUrl;
	private final String requestUrl = "http://atomblog.ameba.jp/servlet/_atom/blog";

	//getter
	public String getUserId() {
		return userId;
	}
	public String getPassword() {
		return password;
	}
	public String getxWsse() {
		return xWsse;
	}
	public String getPostUrl() {
		return postUrl;
	}
	public String getRequestUrl(){
		return requestUrl;
	}
	//setter
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setxWsse(String xWsse) {
		this.xWsse = xWsse;
	}
	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}
	
	
}
