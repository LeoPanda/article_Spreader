package jp.leopanda.articleSpreader.shared;

import java.io.Serializable;



/**
 * For Tumblr OAuth 1.0 
 * 
 * @author LeoPanda
 *
 */

@SuppressWarnings("serial")
public class TumblrCredential implements Serializable{

	private String consumerId;
	private String consumerSecret;
	private String requestToken;
	private String requestTokenSecret;
	private String authUrl;
	private String requestVerifier;
	private String authToken;
	private String authTokenSecret;
	private String blogName;
	private String redirectUrl;
	
	/*
	 * constractor
	 */
	public TumblrCredential(String consumerId,String consumerSecret,String blogName,String redirectUrl){
		this.consumerId = consumerId;
		this.consumerSecret = consumerSecret;
		this.blogName = blogName;
		this.redirectUrl = redirectUrl;
	}
	public TumblrCredential() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	/* getter */
	public String getConsumerId() {
		return consumerId;
	}
	public String getConsumerSecret() {
		return consumerSecret;
	}
	public String getAuthToken() {
		return authToken;
	}
	public String getRequestToken() {
		return requestToken;
	}
	public String getRequestTokenSecret() {
		return requestTokenSecret;
	}
	public String getRequestVerifier() {
		return requestVerifier;
	}
	public String getAuthTokenSecret() {
		return authTokenSecret;
	}
	public String getAuthUrl() {
		return authUrl;
	}
	public String getBlogName(){
		return blogName;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	/* setter */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public void setAuthTokenSecret(String authTokenSecret) {
		this.authTokenSecret = authTokenSecret;
	}
	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}
	public void setRequestTokenSecret(String requestTokenSecret) {
		this.requestTokenSecret = requestTokenSecret;
	}
	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}
	public void setRequestVerifier(String requestVerifier) {
		this.requestVerifier = requestVerifier;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
}
