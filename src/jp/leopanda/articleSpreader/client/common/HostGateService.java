package jp.leopanda.articleSpreader.client.common;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import jp.leopanda.articleSpreader.shared.BlogResource;
import jp.leopanda.articleSpreader.shared.BloggerPost;
import jp.leopanda.articleSpreader.shared.TumblrPost;
import jp.leopanda.articleSpreader.shared.TumblrCredential;


@RemoteServiceRelativePath("HostGateService")
public interface HostGateService  extends RemoteService {	
	BlogResource[] getbloggerInfo(String token) throws HostGateException;
	ArrayList<String> getCategories(String blogId,String token) throws HostGateException;
	BloggerPost[] getbloggerPosts(String blogId,String token,String query,int statIndex) throws HostGateException;
	String postTumblr(TumblrCredential tumblr,TumblrPost postData) throws HostGateException;
	TumblrCredential getTumblrAuthUrl(TumblrCredential tumblr);
	TumblrCredential getTumblrAuthToken(TumblrCredential tumblr);
	}
