package jp.leopanda.articleSpreader.client.tumblr;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.leopanda.articleSpreader.client.common.Statics;
import jp.leopanda.articleSpreader.shared.TumblrCredential;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.datepicker.client.CalendarUtil;
/**
 * tumblrCredentialクラス（tumblr認証情報保存クラス）を操作する。
 * @author LeoPanda
 *
 */
public class TumblrAuthControl {
	private final String cookie_AuthToken 		= "articlespreader_tumblr_authtoken";
	private final String cookie_AuthTokenSecret	= "articlespreader_tumblr_authtoken_secret";
	private final String verifierParmName 		= "oauth_verifier";

	/*
	 * tumblr 権限諸元の取得
	 */
	public TumblrCredential getCredential(){
		TumblrCredential tumblr = Statics.getTumblrCredential();
		if(tumblr == null){
			tumblr = new TumblrCredential(
				Statics.getTumblrConsumerKey(),Statics.getTumblrConsumerSecret(),
				Statics.getTumblrBlogName(),
				GWT.getModuleBaseURL() + "tumblrWindow.html");
		}
		//cookie情報の取得
		tumblr.setAuthToken(getAuthTokenFromCookie());
		tumblr.setAuthTokenSecret(getAuthTokenSecretFromCookie());
		return tumblr;
	}
	/*
	 * tumblr authTokenの取り出し
	 */
	private String getAuthTokenFromCookie(){
		return Cookies.getCookie(cookie_AuthToken);
	}
	private String getAuthTokenSecretFromCookie(){
		return Cookies.getCookie(cookie_AuthTokenSecret);
	}
	/*
	 * tumblr AuthTokenの保存
	 */
	public void saveAuthTokenToCookie(String token,String secret){
		Date expires = new Date();
		CalendarUtil.addMonthsToDate(expires, 3);
		Cookies.setCookie(cookie_AuthToken,token,expires);
		Cookies.setCookie(cookie_AuthTokenSecret, secret,expires);
	}
	/*
	 * tumblr authToken Cookie情報の削除
	 */
	public void removeTumblrAuthToken(){
		Cookies.removeCookie(cookie_AuthToken);
		Cookies.removeCookie(cookie_AuthTokenSecret);
	}

	/*
	 *URLの?パラメータからrequest tokenのverifierの値を得る 
	 */
	public String getVerifierValue(String queryString){
		return getParmValue(queryString, verifierParmName);
	}
	/*
	 * URLの?パラメータからkeyを指定して値を得る
	 * @param queryString URL search パラメータ
	 * @param key　valueのkey
	 * @return
	 */
	private String getParmValue(String queryString,String key){
		String ret = null;
		if (queryString != null && queryString.length() > 1) {
      	  Map<String,String> parms = new HashMap<String,String>();
          String qs = queryString.substring(1);
          for (String kvPair : qs.split("&")) {
            parms.put(kvPair.split("=")[0], kvPair.split("=")[1]);
          }
          ret = parms.get(key);
         }
		return ret;
     }
}
