package jp.leopanda.articleSpreader.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
/**
 * Tumblr用投稿データ
 * @author LeoPanda
 *
 */
public class TumblrPost implements Serializable{
	private String photoUrl;
	private String linkUrl;
	private String postDate;
	private String detal;
	private String tags;	
	private boolean isFuture = false;
	
	//getter
	public String getPhotoUrl() {
		return photoUrl;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public String getDetal() {
		return detal;
	}
	public String getPostDate() {
		return postDate;
	}
	public String getTags(){
		return this.tags;
	}
	public boolean isFuture() {
		return isFuture;
	}
	//setter
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	public void setDetal(String detal) {
		this.detal = detal;
	}
	public void setTags(String tags){
		this.tags = tags;
	}
	public void setFuture(boolean isFuture) {
		this.isFuture = isFuture;
	}
}
