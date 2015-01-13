package jp.leopanda.articleSpreader.client.mainPanel;

import jp.leopanda.articleSpreader.client.common.Statics;
import jp.leopanda.articleSpreader.client.common.Listeners.ArticleListPanelListener;
import jp.leopanda.articleSpreader.client.common.Listeners.HeaderPanelListener;
import jp.leopanda.articleSpreader.client.mainPanel.HeaderPanel.HeaderPanelEvent;

import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * メイン画面
 * ヘッダー画面と明細画面をコントロールする
 * @author LeoPanda
 *
 */
public class MainPanel extends VerticalPanel{
	//画面の構成要素(メソッドをまたがって操作する必要のあるものをメンバ変数化け)
	HeaderPanel headerPanel_ 
					= new HeaderPanel();		//検索パネル付き一覧操作ヘッダー
	ArticleListPanel articleListPanel_ 
					= new ArticleListPanel();	//記事一覧
	//メンバ変数
	private String blogId_;
	/**
	 * パネルのコンストラクタ
	 */
	public MainPanel(){
		//パネルの初期表示
		headerPanel_ = getHeaderPanel();
		articleListPanel_ = getArticlePanel();
		this.add(headerPanel_);
		this.add(articleListPanel_);	
	}
	/*
	 * ヘッダー画面の作成
	 */
	private HeaderPanel getHeaderPanel() {
		final HeaderPanel headerPanel = new HeaderPanel();
		headerPanel.setWidth("100%");
		headerPanel.setBackBtnVisible(false);
		headerPanel.setForwardBtnVisible(true);
		//イベント処理
		headerPanel.addEventListener(new HeaderPanelListener() {			
			@Override
			// 「検索」ボタンが押された
			public void onQuery(HeaderPanelEvent event) {
				headerPanel.setBackBtnVisible(false);
				headerPanel.setForwardBtnVisible(true);
				articleListPanel_.dspPanel(blogId_,
						event.getQueryString(),event.getStartLine());
			}
			@Override
			// 「次」ボタンが押された
			public void onForwardButton(HeaderPanelEvent event) {
				headerPanel.setBackBtnVisible(true);
				articleListPanel_.dspPanel(blogId_,
						event.getQueryString(),event.getStartLine());
			}
			@Override
			// 「前」ボタンが押された
			public void onBackButton(HeaderPanelEvent event) {
				headerPanel.setForwardBtnVisible(true);
				if(event.getStartLine()==Statics.getMinimumIndex()){
					headerPanel.setBackBtnVisible(false);
				}
				articleListPanel_.dspPanel(blogId_,
						event.getQueryString(),event.getStartLine());
			}
		});
		return headerPanel;
	}
	/*
	 * 記事一覧画面の作成
	 */
	private ArticleListPanel getArticlePanel() {
		ArticleListPanel articleListPanel = new ArticleListPanel();
		articleListPanel.setWidth("100%");
		//記事一覧パネルのイベント処理
		articleListPanel.addEventListener(new ArticleListPanelListener() {
			@Override
			// 最終ページが検出された
			public void onLastPage() {
				headerPanel_.setForwardBtnVisible(false);
			}			
		});
		return articleListPanel;
	}
	//画面の初期化
	public void resetPanel(String blogId){
		blogId_ = blogId;
		headerPanel_.resetPanel(blogId);
		articleListPanel_.dspPanel(blogId, 
				"", Statics.getMinimumIndex());		
	}
}
