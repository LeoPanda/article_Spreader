package jp.leopanda.articleSpreader.client.mainPanel;

import jp.leopanda.articleSpreader.client.common.Statics;
import jp.leopanda.articleSpreader.client.common.Listeners.HeaderPanelListener;
import jp.leopanda.articleSpreader.client.common.Listeners.QueryPanelListener;
import jp.leopanda.articleSpreader.server.HostGateServiceImpl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * ヘッダー画面
 * 検索パネルとページコントロール
 * @author LeoPanda
 *
 */
public class HeaderPanel extends VerticalPanel{	
	//画面要素（表示コントロールを行うものだけをメンバ変数化）
	private QueryPanel queryPanel_ = new QueryPanel();
	private Button backBtn_ = new Button();
	private Button forwardBtn_ = new Button();
	//メンバ変数
	private int startIndex_ 
					= Statics.getMinimumIndex();//明細の表示開始位
	private String queryString_ = "";
	private String blogId_;
	//イベントリスナー
	public HeaderPanelListener listener_;
	//イベントリスナーのセッター
	public void addEventListener(HeaderPanelListener listener){
		listener_ = listener;
	}
	//イベントオブジェクト
	public class HeaderPanelEvent {
		private int startLine;
		private String queryString;
		public HeaderPanelEvent(String queryString,int startLine){
			this.startLine = startLine;
			this.queryString = queryString;
		}
		public int getStartLine(){
			return this.startLine;
		}
		public String getQueryString(){
			return this.queryString;
		}
	}
	//各ボタンの可視化ハンドル
	public void setBackBtnVisible(boolean set){
		this.backBtn_.setVisible(set);
	}
	public void setForwardBtnVisible(boolean set){
		this.forwardBtn_.setVisible(set);
	}
	/**
	 * パネルのコンストラクタ
	 */
	public HeaderPanel(){
		this.clear();
		this.setStyleName("h2");
		this.add(getQueryArea()); //検索条件設定エリア
		this.add(getNaviArea());  //ページコントロールエリア
	}
	/*
	 * 検索条件設定エリアの作成
	 */
	private HorizontalPanel getQueryArea() {
		HorizontalPanel queryArea = new HorizontalPanel();
		queryPanel_ = getQueryPanel();  //メンバ変数へセット
		queryArea.add(queryPanel_);
		VerticalPanel buttonArea = new VerticalPanel();
		buttonArea.add(getQueryBtn());
		buttonArea.add(getRestBtn());
		queryArea.add(buttonArea);
		return queryArea;
	}
	/*
	 * ページコントロールエリアの作成
	 */
	private HorizontalPanel getNaviArea() {
		HorizontalPanel naviArea = new HorizontalPanel();
		backBtn_ = getBackBtn();		//メンバ変数へセット
		forwardBtn_ = getForwadBtn();
		naviArea.add(backBtn_);
		naviArea.setHorizontalAlignment(ALIGN_RIGHT);
		naviArea.add(forwardBtn_);
		naviArea.setWidth("100%");
		return naviArea;
	}
	/*
	 * 検索パネルの作成
	 */
	private QueryPanel getQueryPanel() {
		QueryPanel queryPanel = new QueryPanel();
		queryPanel.addEventListener(new QueryPanelListener() {
			@Override
			//検索パネルでエンターキーが押された
			public void onEnterKey() {
				setEventOnQuery();				
			}
		});
		return queryPanel;
	}
	/*
	 * リセットボタンの作成
	 */
	private Button getRestBtn() {
		Button resetBtn = new Button("リセット");
		resetBtn.addClickHandler(new ClickHandler() {
			@Override
			//「リセット」ボタンが押された
			public void onClick(ClickEvent event) {
				queryPanel_.resetPanel(blogId_);
				setEventOnQuery();
			}
		});
		return resetBtn;
	}
	/*
	 * 検索ボタンの作成
	 */
	private Button getQueryBtn() {
		Button queryBtn = new Button("検索");
		queryBtn.addClickHandler(new ClickHandler() {
			@Override
			//「検索」ボタンが押された
			public void onClick(ClickEvent event) {
				setEventOnQuery();
			}
		});
		return queryBtn;
	}
	/*
	 * 次ベージボタンの作成
	 */
	private Button getForwadBtn() {
		Button forwardBtn = new Button("次");
		forwardBtn.addClickHandler(new ClickHandler() {
			@Override
			//「次」ボタンが押された
			public void onClick(ClickEvent event) {
				startIndex_ += HostGateServiceImpl.MAX_RESULTS;
				listener_.onForwardButton(
						new HeaderPanelEvent(queryString_,startIndex_));
			}
		});
		return forwardBtn;
	}
	/*
	 * 前ページボタンの作成
	 */
	private Button getBackBtn() {
		Button backBtn = new Button("前");
		backBtn.addClickHandler( new ClickHandler() {
			@Override
			//「前」ボタンが押された
			public void onClick(ClickEvent event) {
				startIndex_ -= HostGateServiceImpl.MAX_RESULTS;
				if(startIndex_< Statics.getMinimumIndex()){
					startIndex_=Statics.getMinimumIndex();
				}
				listener_.onBackButton(
						new HeaderPanelEvent(queryString_,startIndex_));
			}
		});
		return backBtn;
	}
	/*
	 * パネルの初期化
	 * 引数のブログを読み込みなおして表示し直す
	 */
	public void resetPanel(String blogId){
		blogId_ = blogId;
		queryString_ = "";
		startIndex_ = Statics.getMinimumIndex();
		setBackBtnVisible(false);
		setForwardBtnVisible(true);
		queryPanel_.resetPanel(blogId);
	}
	/*
	 * イベントを発生させて
	 * 検索パネルで入力された検索条件をMailPanelへ通知する。
	 */
	private void setEventOnQuery() {
		//メンバ変数をリセット
		queryString_ = queryPanel_.getQuery();
		startIndex_ = Statics.getMinimumIndex();
		//イベント登録
		listener_.onQuery(
				new HeaderPanelEvent(queryPanel_.getQuery(),
								Statics.getMinimumIndex()));
	}
}
