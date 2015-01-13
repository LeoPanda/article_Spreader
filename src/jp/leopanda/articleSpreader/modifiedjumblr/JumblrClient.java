package jp.leopanda.articleSpreader.modifiedjumblr;


import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.leopanda.articleSpreader.client.common.HostGateException;

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

/**
 * This is the base JumblrClient that is used to make requests to the Tumblr
 * API.  All calls that can be made from other Resource(s) can be made from
 * here.
 * @author jc
 * Google App Engine 上では　HttpUrlSession を用いたPostが上手く動作しないため
 * jumblrのソースをCommonServiceのUrlServiceを使用するように変更を加えた。
 * このjumblrClientでは写真URLのポスト以外の機能は動作しない。
 
 * 
 */
public class JumblrClient {

    private RequestBuilder requestBuilder;
    private String apiKey;

    public JumblrClient() {
        this.requestBuilder = new RequestBuilder(this);
    }

    /**
     * Instantiate a new Jumblr Client with no token
     * @param consumerKey The consumer key for the client
     * @param consumerSecret The consumer secret for the client
     */
    public JumblrClient(String consumerKey, String consumerSecret) {
        this();
        this.requestBuilder.setConsumer(consumerKey, consumerSecret);
        this.apiKey = consumerKey;
    }

    /**
     * Instantiate a new Jumblr Client
     * @param consumerKey The consumer key for the client
     * @param consumerSecret The consumer secret for the client
     * @param token The token for the client
     * @param tokenSecret The token secret for the client
     */
    public JumblrClient(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this(consumerKey, consumerSecret);
        this.setToken(token, tokenSecret);
    }

    //add for GAE 2014.6.29 by leopanda
    public void setService(OAuthService service){
    	this.requestBuilder.setService(service);
    }
    
    /**
     * Set the token for this client
     * @param token The token for the client
     * @param tokenSecret The token secret for the client
     */
    public void setToken(String token, String tokenSecret) {
        this.requestBuilder.setToken(token, tokenSecret);
    }

    /**
     * Set the token for this client.
     * @param token The token for the client.
     */
    public void setToken(final Token token) {
        this.requestBuilder.setToken(token);
    }

    /**
     * Performs an XAuth authentication.
     * @param email the user's login email.
     * @param password the user's login password.
     */
    public void xauth(final String email, final String password) {
        setToken(this.requestBuilder.postXAuth(email, password));
    }

    /**
     * Get the user info for the authenticated User
     * @return The authenticated user
     */
    public User user() {
        return requestBuilder.get("/user/info", null).getUser();
    }



    /**
     * Get the blog info for a given blog
     * @param blogName the Name of the blog
     * @return The Blog object for this blog
     */
    public Blog blogInfo(String blogName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("api_key", this.apiKey);
        return requestBuilder.get(JumblrClient.blogPath(blogName, "/info"), map).getBlog();
    }



    /**
     * Get a specific size avatar for a given blog
     * @param blogName the avatar URL of the blog
     * @param size The size requested
     * @return a string representing the URL of the avatar
     */
    public String blogAvatar(String blogName, Integer size) {
        String pathExt = size == null ? "" : "/" + size.toString();
        return requestBuilder.getRedirectUrl(JumblrClient.blogPath(blogName, "/avatar" + pathExt));
    }

    public String blogAvatar(String blogName) { return this.blogAvatar(blogName, null); }

    /**
     * Like a given post
     * @param postId the ID of the post to like
     * @param reblogKey The reblog key for the post
     */
    public void like(Long postId, String reblogKey) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", postId.toString());
        map.put("reblog_key", reblogKey);
        requestBuilder.post("/user/like", map);
    }

    /**
     * Unlike a given post
     * @param postId the ID of the post to unlike
     * @param reblogKey The reblog key for the post
     */
    public void unlike(Long postId, String reblogKey) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", postId.toString());
        map.put("reblog_key", reblogKey);
        requestBuilder.post("/user/unlike", map);
    }

    /**
     * Follow a given blog
     * @param blogName The name of the blog to follow
     */
    public void follow(String blogName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", JumblrClient.blogUrl(blogName));
        requestBuilder.post("/user/follow", map);
    }

    /**
     * Unfollow a given blog
     * @param blogName the name of the blog to unfollow
     */
    public void unfollow(String blogName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", JumblrClient.blogUrl(blogName));
        requestBuilder.post("/user/unfollow", map);
    }

    /**
     * Delete a given post
     * @param blogName the name of the blog the post is in
     * @param postId the id of the post to delete
     */
    public void postDelete(String blogName, Long postId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", postId.toString());
        requestBuilder.post(JumblrClient.blogPath(blogName, "/post/delete"), map);
    }


    /**
     * Save edits for a given post
     * @param blogName The blog name of the post
     * @param id the Post id
     * @param detail The detail to save
     * @throws HostGateException 
     */
    public void postEdit(String blogName, Long id, Map<String, ?> detail) throws IOException, HostGateException {
        Map<String, Object> sdetail = JumblrClient.safeOptionMap(detail);
        sdetail.put("id", id);
        requestBuilder.postMultipart(JumblrClient.blogPath(blogName, "/post/edit"), sdetail);
    }

    /**
     * Create a post
     * @param blogName The blog name for the post
     * @param detail the detail to save
     * @throws HostGateException 
     */
    public Long postCreate(String blogName, Map<String, ?> detail) throws IOException, HostGateException {
    	Long ret = 0L;
    	ResponseWrapper post = requestBuilder.postMultipart(JumblrClient.blogPath(blogName, "/post"), detail);
    	if ( post != null) {
    		ret = post.getId();
    	}
        return ret;
    }

    /**
     * Set up a new post of a given type
     * @param blogName the name of the blog for this post (or null)
     * @param klass the type of Post to instantiate
     * @return the new post with the client set
     */
    public <T extends Post> T newPost(String blogName, Class<T> klass) throws IllegalAccessException, InstantiationException {
        T post = klass.newInstance();
        post.setClient(this);
        post.setBlogName(blogName);
        return post;
    }

    /**
     **
     **
     */

    private static String blogPath(String blogName, String extPath) {
        return "/blog/" + blogUrl(blogName) + extPath;
    }

    private static String blogUrl(String blogName) {
        return blogName.contains(".") ? blogName : blogName + ".tumblr.com";
    }

    public void setRequestBuilder(RequestBuilder builder) {
        this.requestBuilder = builder;
    }

    public RequestBuilder getRequestBuilder() {
        return requestBuilder;
    }

    private static Map<String, Object> safeOptionMap(Map<String, ?> map) {
        Map<String, Object> mod = new HashMap<String, Object>();
        mod.putAll(map);
        return mod;
    }

}
