package jp.leopanda.articleSpreader.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BloggerPost implements Serializable{
		private String id;
		private String published;
		private String url;
		private String alternateUrl;
		private String title;
		private String content;
		private String isDraft;
		private boolean isFuture = false;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getPublished() {
			return published;
		}
		public void setPublished(String published) {
			this.published = published;
		}
		public String getUrl() {
			return (url == null ? alternateUrl:url);
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public void setAlternateUrl(String alternateUrl) {
			this.alternateUrl = alternateUrl;
		}
		public String getAlternateUrl() {
			return alternateUrl;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getIsDraft() {
			return isDraft;
		}
		public void setIsDraft(String isDraft) {
			this.isDraft = isDraft;
		}
		public boolean isFuture() {
			return this.isFuture;
		}
		public void setFuture(boolean isFuture) {
			 this.isFuture = isFuture;
		}
		

}
