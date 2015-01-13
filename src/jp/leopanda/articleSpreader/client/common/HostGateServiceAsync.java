package jp.leopanda.articleSpreader.client.common;


import java.util.ArrayList;

import jp.leopanda.articleSpreader.shared.BlogResource;
import jp.leopanda.articleSpreader.shared.BloggerPost;
import jp.leopanda.articleSpreader.shared.TumblrPost;
import jp.leopanda.articleSpreader.shared.TumblrCredential;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HostGateServiceAsync {

	void getbloggerInfo(String token, AsyncCallback<BlogResource[]> callback);
	void getCategories(String blogId,String token,AsyncCallback<ArrayList<String>> callback);
	void getbloggerPosts( String blogId, String token,String query,int startIndex,AsyncCallback<BloggerPost[]> callback);
	void postTumblr(TumblrCredential tumblr,TumblrPost postData,AsyncCallback<String> callback);
	void getTumblrAuthUrl(TumblrCredential tumblr,AsyncCallback<TumblrCredential> callback);
	void getTumblrAuthToken(TumblrCredential tumblr,AsyncCallback<TumblrCredential> callback);
}
