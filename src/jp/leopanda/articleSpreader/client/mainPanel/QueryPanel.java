package jp.leopanda.articleSpreader.client.mainPanel;

import jp.leopanda.articleSpreader.client.common.Statics;
import jp.leopanda.articleSpreader.client.common.Listeners.QueryPanelListener;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
/**
 * 検索設定パネル
 * 検索条件を入力し検索文字列を返す
 * @author LeoPanda
 *
 */
public class QueryPanel extends TabPanel{	
	//画面の構成要素(クラスをまたがってコントロールされるものだけをメンバ変数化)
	DateBox dateFrom_ = new DateBox();
	DateBox dateTo_ = new DateBox();
	QueryCategorySelector categorySelector_ = new QueryCategorySelector();
	TextBox textQuery_ = new TextBox();
	//イベントリスナー
	public QueryPanelListener listener_;
	// イベントリスナーのセッター
	public void addEventListener(QueryPanelListener listener) {
		listener_ = listener;
	}
	/*
	 * コンストラクタ
	 */
	public QueryPanel(){
		//検索機能別にタブを追加
		this.add(getRegularQueryPanel(), "定形検索");
		this.add(getFreeQueryPanel(),"全文検索");
		//パネル初期化
		this.setWidth("317px");
		this.selectTab(0);
		//タブが切替えられたら以前の内容をクリアする
		this.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				dateFrom_.setValue(null);
				dateTo_.setValue(null);
				textQuery_.setText(null);
				categorySelector_.setSelectedIndex(0);
			}
		});
	}

	/*
	 * 全文検索画面の作成
	 */
	private HorizontalPanel getFreeQueryPanel() {
		HorizontalPanel freeQueryPanel = new HorizontalPanel();
		textQuery_.setStyleName("textQuery_");
		textQuery_.addKeyPressHandler(new EnterKeyListener());
		freeQueryPanel.add(textQuery_);
		return freeQueryPanel;
	}
	/*
	 * 定形検索画面の作成
	 */
	private VerticalPanel getRegularQueryPanel() {
		VerticalPanel regularQueryPanel = new VerticalPanel();
		//日付ボックス
		HorizontalPanel datePanel = new HorizontalPanel();
		datePanel.add(new Label("投稿日付"));
	    dateFrom_ = getDateBox();
	    dateTo_ = getDateBox();
		datePanel.add(dateFrom_);
		datePanel.add(new Label("-"));
		datePanel.add(dateTo_);
		regularQueryPanel.add(datePanel);
		//カテゴリ選択
		HorizontalPanel categoryPanel = new HorizontalPanel();
		categoryPanel.add(new Label("ラベル選択"));
		categoryPanel.add(categorySelector_);
		regularQueryPanel.add(categoryPanel);
		return regularQueryPanel;
	}
	//日付入力エリアの作成
	private DateBox getDateBox() {
		DateBox dateBox = new DateBox();
		DateTimeFormat dateFormat = 
				DateTimeFormat.getFormat(Statics.DateType.DateOnly.getFormat());
	    dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
	    dateBox.setStyleName("dateInput");
		return dateBox;
	}
	/*
	 * エンターキー押下を拾うイベント処理クラス
	 * 
	 * @param event
	 */
	private class EnterKeyListener implements KeyPressHandler {
		@Override
		public void onKeyPress(KeyPressEvent event) {
			int keyCode = event.getUnicodeCharCode();
			if (keyCode == 0) {
				// Probably Firefox
				keyCode = event.getNativeEvent().getKeyCode();
			}
			if (keyCode == KeyCodes.KEY_ENTER) {
				listener_.onEnterKey();
			}
		}
	}

	/*
	 * 入力された検索条件から検索文字列を作成して返す
	 * @return 検索文字列
	 */
	public String getQuery(){
		String query= "";
		if((""+dateFrom_.getTextBox().getText()).length()>0){
			query="&published-min=" + dateFrom_.getTextBox().getText();
		}
		if((""+dateTo_.getTextBox().getText()).length()>0){
			query+=(query.length() > 0 ? "&" : "") 
					+ "published-max=" + dateTo_.getTextBox().getText();
		}
		if((""+categorySelector_.getCategory()).length()>0){
			query+=(query.length() > 0 ? "&" : "") 
					+"category="+categorySelector_.getCategory();
		}
		if((""+textQuery_.getText()).length()>0){
			textQuery_.setText(textQuery_.getText()
					.replaceAll(" ", ",").replaceAll("　", ","));
			query+=(query.length() > 0 ? "&" : "") 
					+"q="+textQuery_.getText();
		}
		GWT.log("query="+query);
		return URL.encode((query.length() > 0 ? "&" : "")+query);
	}
	/*
	 * パネルの初期化
	 * ブログを読み込みなおして検索条件入力画面を初期化する
	 * @param blogId
	 */
	public void resetPanel(String blogId){
		dateFrom_.setValue(null);
		dateTo_.setValue(null);
		textQuery_.setText(null);
		categorySelector_.resetCategory(blogId);
	}
}
