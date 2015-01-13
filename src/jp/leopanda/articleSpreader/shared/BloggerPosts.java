package jp.leopanda.articleSpreader.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BloggerPosts implements Serializable{
	public String kind;
	public BloggerPost[] items;
	
	public String getKind(){
		return this.kind;
	}
	
	public BloggerPost[] getItems(){
		return this.items;
	}
}
