package jp.leopanda.articleSpreader.client.common;

import jp.leopanda.articleSpreader.client.DebugPanel;
import jp.leopanda.articleSpreader.client.tumblr.TumblrAuthControl;
import jp.leopanda.articleSpreader.shared.TumblrCredential;

import com.google.gwt.core.client.GWT;

/**
 *初期値保存用クラス
 *<pre>
 * @param パラメータなし
 * </pre>
 */
public class Statics {
	//Google client ID = デプロイ先Google　App　Engineのアプリケーション識別子
	private static final String googleClientId 
					= "1075891745451.apps.googleusercontent.com";
	//Tumblr OAuthアクセス諸元の初期値
	private static final String tumblrConsumerKey 	
					= "Q1xp1wThmoznYK4s2jzvCL56MIEWsa1q0Z7S3BZjwbCY7yIB3s";
	private static final String tumblrConsumerSecret 	
					= "8xVtihVs31JZrMMoskdNqREal4OpRFUmcaCeUYdzoLIY684NXz";
	private static final String tumblrBlogName = "leopanda";
	private static TumblrCredential tumblrClient = new TumblrAuthControl().getCredential();
	private static final int minimumIndex = 1;	//StartIndexの最小値
	private static String loginToken;			//Google認証トークン
	private static String bloggerID;			//BloggerのblogID
	private static HostGateServiceAsync hgc = 
				GWT.create(HostGateService.class); //RPCハンドル
	
	private static final boolean DEBUG_MODE = false; // デバックログ画面を出力する場合はtrueをセット
	private static DebugPanel logPanel = new DebugPanel(); //デバック用ログ画面
	public static DebugPanel getLogPanel(){
		return logPanel;
	}
	public static boolean debugMode(){
		return DEBUG_MODE;
	}
	//日付フォーマット
	public enum DateType{
		FullFormat("yyyy-MM-dd'T'HH:mm:ss'.'SSSZ"),
		DateTime("yyyy-MM-dd' 'HH:mm"),
		DateOnly("yyyy-MM-dd"),
		TimeOnly("HH:mm:ss");
		private String format_;
		DateType(String format){
			this.format_ = format;
		}
		public String getFormat(){
			return this.format_;
		}
	}
	public enum PostLink{
		Ameba("http://blog.ameba.jp/ucs/entry/srventryinsertinput.do"),
		Tumblr("http://www.tumblr.com/new/photo");
		private String link_;
		PostLink(String link){
			this.link_ = link;
		}
		public String getLink(){
			return this.link_;
		}
	}
	//getter
	public static String getClientId(){
		return googleClientId;
	}
	public static String getLoginToken() {
		return loginToken;
	}
	public static String getBloggerID() {
		return bloggerID;
	}
	public static int getMinimumIndex(){
		return minimumIndex;
	}
	public static HostGateServiceAsync getHgc() {
		return hgc;
	}
	public static TumblrCredential getTumblrCredential(){
		return tumblrClient;
	}
	public static String getTumblrConsumerKey(){
		return tumblrConsumerKey;
	}
	public static String getTumblrConsumerSecret(){
		return tumblrConsumerSecret;
	}
	public static String getTumblrBlogName(){
		return tumblrBlogName;
	}
	//seter
	public static void setTumblrCredential(TumblrCredential tumblr){
		Statics.tumblrClient = tumblr;
	}
	public static void setLoginToken(String loginToken) {
		Statics.loginToken = loginToken;
	}
	public static void setBloggerID(String bloggerID) {
		Statics.bloggerID = bloggerID;
	}

}
