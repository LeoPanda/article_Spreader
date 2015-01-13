package jp.leopanda.articleSpreader.client;


import jp.leopanda.articleSpreader.client.common.HostGateException;
import jp.leopanda.articleSpreader.client.common.HostGateServiceAsync;
import jp.leopanda.articleSpreader.client.common.Statics;
import jp.leopanda.articleSpreader.client.common.Listeners.BlogSelectorPanelListener;
import jp.leopanda.articleSpreader.shared.BlogResource;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

/**
 * GoogleユーザーがオーナーのBloggerブログを検索し
 * セレクタに表示するためのクラス。
 * セレクタで選択されたBlogIDをArticleList()クラスへ渡し、
 * 記事の一覧を表示させる。
 * @author LeoPanda
 *
 */
public class BlogSelectorPanel extends HorizontalPanel{
	//画面構成要素
	ListBox blogList = new ListBox();
	//メンバ変数
	private BlogResource[] blogResource_;		//全Bloggerブログ情報
	//PRCハンドル
	private HostGateServiceAsync hgc_ = Statics.getHgc();
	//イベントリスナー
	private BlogSelectorPanelListener listener_;
	//イベントリスナーのセッター
	public void addEventListener(BlogSelectorPanelListener listener){
		listener_ = listener;
	}
	//イベントオブジェクト
	public class BlogSelectorPanelEvent {
		private String selectedBlogId_;
		public BlogSelectorPanelEvent(String selectedBlogId){
			this.selectedBlogId_ = selectedBlogId;
		}
		public String getBlogId(){
			return this.selectedBlogId_;
		}
	}
	/**
	 * コンストラクタ
	 */
	public BlogSelectorPanel(){		
		this.add(new Label("ブログを選択してください。"));
		hgc_.getbloggerInfo(Statics.getLoginToken(),
					new BloggerInforCallBack());//ブログ情報問い合せ
	}
	/*
	 * ブログリソース問い合わせ応答クラス
	 */
	private class BloggerInforCallBack implements AsyncCallback<BlogResource[]> {
		public void onFailure(Throwable caught) {
			if(caught instanceof HostGateException){
				Window.alert("ブログ情報取得に失敗しました。HTTP StatisCode="+
						((HostGateException)caught).getStatus());
				Window.Location.reload();
			}else{
				Window.alert("ブログ情報取得時にRPCエラーが発生しました。ログを確認して下さい。");
			}
		}
		@Override
		//取得成功をトリガとしてリストボックスを生成する
		public void onSuccess(BlogResource[] blogResource) {
			createListBox(blogResource);//リストボックスの生成
			//記事一覧を初期表示させるためにイベントを発生させる
			listener_.onChanged(
					new BlogSelectorPanelEvent(blogResource[0].getId()));
			blogResource_ = blogResource;  //結果をメンバ変数に保持
		}
	}
	/**
	 * Bloggerブログ選択リストボックスの初期化
	 * ブログの一覧をリストボックスへセットする。
	 */
	private void createListBox(BlogResource[] blogResource) {
		if(blogResource == null){
			return;
		}
		for (int i = 0; i < blogResource.length; i++) {
			blogList.addItem(blogResource[i].getName());
		}
		this.add(blogList);
		//ブログを選択し直した時のイベント
		blogList.addChangeHandler(new ChangeHandler() {			
			@Override
			public void onChange(ChangeEvent event) {
				listener_.onChanged(
						new BlogSelectorPanelEvent(
								blogResource_[blogList.getSelectedIndex()].getId()));
			}
		});
	}
}
