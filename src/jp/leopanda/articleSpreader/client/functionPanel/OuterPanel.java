package jp.leopanda.articleSpreader.client.functionPanel;

import java.util.Date;

import jp.leopanda.articleSpreader.client.common.Statics;
import jp.leopanda.articleSpreader.client.common.WndControl;
import jp.leopanda.articleSpreader.shared.BloggerPost;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * 記事へのアクションを指示するポップアップ画面
 * @author LeoPanda
 *
 */
public class OuterPanel extends PopupPanel{
//	コンストラクタ
	public OuterPanel(BloggerPost bloggerPost){
		VerticalPanel innerPanel 
						= new VerticalPanel();  //ポップアップウィンドウの内枠
		innerPanel.add(getHeader(bloggerPost));
		innerPanel.add(getFuncTabPanel(bloggerPost));
		this.add(innerPanel);
		this.setPopupPosition(400, 50);
		this.show();
	}
//	機能の種類
	private enum Function{
		Ameba("Ameba用要約記事"),
		Tumblr("Tumblr投稿"),
		Origin("オリジナル記事");
		public final String value;
		private Function(String value){
			this.value = value;
		}
	}
	//ヘッダの作成
	private HorizontalPanel getHeader(BloggerPost bloggerPost){
		HorizontalPanel header = new HorizontalPanel();
		new FixedTextBoxes(bloggerPost).set(header);//記事タイトル表示パーツのセット
		Button 	closeButton = new Button("x");		//「閉じる」ボタン
		closeButton.addClickHandler(new CloseWindow());
		header.add(closeButton);		
		return header;
	}
	//機能別タブパネルの作成
	private TabPanel getFuncTabPanel(BloggerPost bloggerPost){
		TabPanel tabPanel = new TabPanel();
		tabPanel.setStyleName("funcTab");
		tabPanel.add(new AmebaPanel(bloggerPost),Function.Ameba.value);
		tabPanel.add(new TumblrPanel(bloggerPost),Function.Tumblr.value);
		tabPanel.add(new OriginPanel(bloggerPost),Function.Origin.value);
		tabPanel.selectTab(0);
		return tabPanel;
	}
	/*
	 * 閉じるボタン　アクションハンドラ
	 */
	private class CloseWindow implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			OuterPanel.this.hide();
			OuterPanel.this.clear();
		}
	}
	/*
	 * 記事タイトル表示パーツ
	 * （他システム投稿画面へのコピペ操作がしやすいようにテキストボックスにしておく）
	 */
	private class FixedTextBoxes{
		private TextBox titleBox	= new TextBox();	//記事タイトルエリア
		private TextBox	dateBox		= new TextBox();	//投稿日付エリア
		private TextBox	timeBox		= new TextBox();	//投稿時間エリア
		private String 	title$_		= new String("");	//記事タイトル
		private String 	postDate$_ 	= new String("");	//投稿日付
		private String 	postTime$_ 	= new String("");	//投稿時間

		//コンストラクタ
		public FixedTextBoxes(BloggerPost bloggerPost) {
			title$_ = bloggerPost.getTitle();
			postDate$_ = getDateFormatString(bloggerPost.getPublished(), 
								Statics.DateType.DateOnly);
			postTime$_ = getDateFormatString(bloggerPost.getPublished(),
								Statics.DateType.TimeOnly);
			titleBox.setText(title$_);
			dateBox.setText(postDate$_);
			timeBox.setText(postTime$_);
			DoNotChangePostDate handler = new DoNotChangePostDate();
			titleBox.addChangeHandler(handler);
			dateBox.addChangeHandler(handler);
			timeBox.addChangeHandler(handler);
			titleBox.setStyleName("titleBox");
			dateBox.setStyleName("dateBox");
			timeBox.setStyleName("timeBox");
		}
		/*
		 * 内容を変更できないようにする
		 */
		private class DoNotChangePostDate implements ChangeHandler {
			@Override
			public void onChange(ChangeEvent event) {
				WndControl.alert("変更できません。");
				titleBox.setText(title$_);
				dateBox.setText(postDate$_);
				timeBox.setText(postTime$_);				
			}
		}
		/*
		 * StringをDate変換してDateTypeの文字列を得る
		 */
		private String getDateFormatString(String dateString,Statics.DateType dateType){
			Date postDateTime = DateTimeFormat.getFormat(
					Statics.DateType.FullFormat.getFormat()).parse(dateString);
			return DateTimeFormat.getFormat(
					dateType.getFormat()).format(postDateTime);
		}
		/*
		 * テキストボックスの配置
		 */
		public void set(HorizontalPanel target){
			target.add(titleBox);
			target.add(dateBox);
			target.add(timeBox);
		}
	}
}
