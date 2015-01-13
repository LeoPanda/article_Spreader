package jp.leopanda.articleSpreader.shared;

import java.io.Serializable;
/*
 * google Blogger API JSON parse receiver
 * https://developers.google.com/blogger/docs/3.0/reference/blogs
 * 
 */
@SuppressWarnings("serial")
public class BlogResource implements Serializable{
	public String id;
	public String name;
	public String url;
	
	public BlogResource(){
		
	}
	
	public BlogResource(final String id, final String name, final String url){
		this.id = id;
		this.name = name;
		this.url = url;
	}
	
	public String getId(){ return this.id; }
	public String getName(){ return this.name; }
	public String getUrl(){ return this.url; }
}
