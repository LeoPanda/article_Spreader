package jp.leopanda.articleSpreader.client.tumblr;

import jp.leopanda.articleSpreader.client.common.HostGateServiceAsync;
import jp.leopanda.articleSpreader.client.common.Statics;
import jp.leopanda.articleSpreader.client.common.WndControl;
import jp.leopanda.articleSpreader.client.common.Listeners.TumblrLoginButtonListener;
import jp.leopanda.articleSpreader.shared.TumblrCredential;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

public class TumblrLoginButton extends Button{
	//静的メンバ変数
	private HostGateServiceAsync hgc_ = Statics.getHgc();
	//メンバ変数
	private TumblrCredential tumblr_;
	private TumblrAuthControl authControl = new TumblrAuthControl();
	//イベントリスナー
	public TumblrLoginButtonListener listener_;
	//イベントリスナーのセッター
	public void addEventListener(TumblrLoginButtonListener listener){
		listener_ = listener;
	}
	
	//コンストラクタ
	public TumblrLoginButton(String text){
		this.setText(text);
		tumblr_ = authControl.getCredential();
		if(tumblr_.getAuthToken() == null){
			this.setVisible(true);
		}else{
			this.setVisible(false);
		}
		createHook();
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doGetAuthUrl();
			}
		});
	}
	/*
	 * tumblr 認証URLの取得
	 */
	private void doGetAuthUrl() {			
		hgc_.getTumblrAuthUrl(tumblr_, new AsyncCallback<TumblrCredential>() {	
			@Override
			public void onSuccess(TumblrCredential result) {
				// TODO 自動生成されたメソッド・スタブ
				tumblr_ = result;
				Statics.setTumblrCredential(tumblr_);
				WndControl.openWindow(tumblr_.getAuthUrl());
			}
			@Override
			public void onFailure(Throwable caught) {
				// TODO 自動生成されたメソッド・スタブ
				Window.alert("tumblr get authURL failure.");
			}
		});
	}
	/*
	 * tumblr AuthTokenの取得
	 */
	private void doGetToken() {
		// TODO 自動生成されたメソッド・スタブ
		hgc_.getTumblrAuthToken(tumblr_, new AsyncCallback<TumblrCredential>() {			
			@Override
			public void onSuccess(TumblrCredential result) {
				tumblr_ = result;
				authControl.saveAuthTokenToCookie(tumblr_.getAuthToken(),
											tumblr_.getAuthTokenSecret());
				Statics.setTumblrCredential(tumblr_);
				listener_.onAuthTokenReturned();
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("tumblr get authToken failure.");
			}
		});
		
	}
	/**
	 * 別ウィンドウで開いた先に仕込んだnative javascriptからの
	 * 呼び出しを受け取れるようにフックを作成する
	 */
	private native void createHook() /*-{
		var self = this;
		$wnd.__dologin = $entry(function(queryString) {
			self.@jp.leopanda.articleSpreader.client.tumblr.TumblrLoginButton::gate(Ljava/lang/String;)(queryString);
		});
	}-*/;
	void gate(String queryString){
		String verifier = authControl.getVerifierValue(queryString);
		tumblr_.setRequestVerifier(verifier);
		doGetToken();
	}

	
}
