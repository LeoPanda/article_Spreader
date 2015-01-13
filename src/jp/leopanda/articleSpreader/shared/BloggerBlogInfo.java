package jp.leopanda.articleSpreader.shared;

import java.io.Serializable;
/*
 * google Blogger API JSON parse receiver
 * https://developers.google.com/blogger/docs/3.0/reference/blogs
 * 
 */
@SuppressWarnings("serial")
public class BloggerBlogInfo implements Serializable{
	public String kind;
	public BlogResource[] items;
	
	public BlogResource[] getItems(){
		return this.items;
	}
}
