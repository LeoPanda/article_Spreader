package jp.leopanda.articleSpreader.client.mainPanel;

import java.util.Date;

import jp.leopanda.articleSpreader.client.InProgressPanel;
import jp.leopanda.articleSpreader.client.common.HostGateException;
import jp.leopanda.articleSpreader.client.common.HostGateServiceAsync;
import jp.leopanda.articleSpreader.client.common.Statics;
import jp.leopanda.articleSpreader.client.common.Listeners.ArticleListPanelListener;
import jp.leopanda.articleSpreader.client.functionPanel.OuterPanel;
import jp.leopanda.articleSpreader.server.HostGateServiceImpl;
import jp.leopanda.articleSpreader.shared.BloggerPost;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
/**
 * 記事リストパネル
 * ブログ選択画面で選択されたブログ記事の一覧を表示する。
 * @author LeoPanda
 *
 */
public class ArticleListPanel extends FlexTable{
	//この画面から呼び出すサブ画面
	private InProgressPanel inProgressPanel = new InProgressPanel();//処理中表示パネル
	//RPCハンドル
	private HostGateServiceAsync hgc_ = Statics.getHgc();
	//イベントリスナー
	public ArticleListPanelListener listener_;
	//イベントリスナーのセッター
	public void addEventListener(ArticleListPanelListener listener){
		listener_ = listener;
	}
	/**
	 * タイトル行の表示と記事一覧の取得依頼
	 * @param blogId		選択されたブログのブログID
	 * @param query			記事検索パラメータ
	 * @param startIndex	記事の表示開始位置
	 */
	public void dspPanel(String blogId,String query,int startIndex) {
		this.clear(true);
		this.setText(0, 0, "タイトル");
		this.setText(0, 1, "投稿日");
		this.getRowFormatter().addStyleName(0, "articleTable-head");
		this.getCellFormatter().addStyleName(0, 0, "articleTable-title");
		inProgressPanel.dspPanel();//処理中表示
		//記事一覧情報の取得依頼
		hgc_.getbloggerPosts(blogId,Statics.getLoginToken(),query,startIndex,
				new PostsCallback()
		);
	}
	/**
	 * 記事一覧情報取得後の処理
	 */
	private class PostsCallback implements AsyncCallback<BloggerPost[]> {
		@Override
		//取得エラー
		public void onFailure(Throwable caught) {
			inProgressPanel.hide();//処理中表示解除
			if(caught instanceof HostGateException){
				Window.alert("ブログ記事取得に失敗しました。HTTP StatisCode="+
						((HostGateException)caught).getStatus());
			}else{
				Window.alert("ブログ記事取得時にRPCエラー:" + caught.toString());
			}					
		}
		@Override
		//取得成功
		public void onSuccess(BloggerPost[] result) {
			inProgressPanel.hide();//処理中表示解除
			if(result.length < HostGateServiceImpl.MAX_RESULTS){
				listener_.onLastPage();//最終ページ検出
			}
			//記事一覧の表示
			final BloggerPost[] posts = result;
			for (int i = 0; i < result.length; i++) {
				//記事タイトル
				Anchor title = new Anchor(posts[i].getTitle());
				ArticleListPanel.this.setWidget(i+1, 0, title);
				//投稿日付
				Date postDate = 
						DateTimeFormat.getFormat(
								Statics.DateType.FullFormat.getFormat())
								.parse(posts[i].getPublished());
				ArticleListPanel.this.setText(i+1, 1,
						DateTimeFormat.getFormat(
								Statics.DateType.DateOnly.getFormat())
								.format(postDate)
								 + (posts[i].isFuture() ? "　F": "") );
				//リンク
				final int index = i;
				title.addClickHandler(new ClickHandler() {
					@Override
					//記事がクリックされたら要約ウィンドウを開く
					public void onClick(ClickEvent event) {
						@SuppressWarnings("unused")
						OuterPanel outerPanel =
								new OuterPanel(posts[index]);
					}
				});
			}
		}
	}
}
