package jp.leopanda.articleSpreader.client.tumblr;

import jp.leopanda.articleSpreader.client.common.HostGateException;
import jp.leopanda.articleSpreader.client.common.HostGateServiceAsync;
import jp.leopanda.articleSpreader.client.common.Statics;
import jp.leopanda.articleSpreader.client.common.Listeners.postTumbulrButtonListener;
import jp.leopanda.articleSpreader.client.common.WndControl;
import jp.leopanda.articleSpreader.shared.TumblrPost;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
/**
 * Tumblr投稿　サブミットボタン
 * @author LeoPanda
 *
 */
public class PostTumblrButton extends Button{

	//メンバ変数
	private TumblrPost postData_; //投稿記事
	//RPCハンドル
	HostGateServiceAsync hgc_ = Statics.getHgc();
	//イベントリスナー
	public postTumbulrButtonListener listener_;
	//イベントリスナーのセッター
	public void addEventListener(postTumbulrButtonListener listener){
		listener_ = listener;
	}

	/**
	 * コンストラクタ
	 * @param tumblr
	 * @param postData
	 */
	public PostTumblrButton(TumblrPost postData){
		this.postData_ = postData;
		this.setText("投稿");
		if(Statics.getTumblrCredential().getAuthToken() == null){
			this.setVisible(false);
		}
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				listener_.onClick();
			}
		});

	}
	/*
	 * サブミット処理
	 * 
	 */
	public void doPost() {
		hgc_.postTumblr(Statics.getTumblrCredential(),postData_, 
				new AsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
						// 投稿成功
						WndControl.alert("投稿しました。");
					}						
					@Override
					public void onFailure(Throwable caught) {
						// RPCエラー
						if(caught instanceof HostGateException){
							TumblrAuthControl cookie = new TumblrAuthControl();
							cookie.removeTumblrAuthToken();
							WndControl.alert("認証エラーです。tumblrにログインし直してください。extMsg:" + caught.getMessage());
							listener_.onAuthError();
						}else{
							WndControl.alert("Async Error:" + caught.getMessage());
						}
					}
				});
	}
	// getter Postdata
	public TumblrPost getPostData() {
		return postData_;
	}
	// setter Postdata
	public void setPostData(TumblrPost postData_) {
		this.postData_ = postData_;
	}
	//アラート表示
	private static native void openAlert(String msg) /*-{
		$wnd.alert(msg);
	}-*/;
}
