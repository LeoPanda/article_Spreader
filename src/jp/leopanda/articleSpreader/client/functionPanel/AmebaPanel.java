package jp.leopanda.articleSpreader.client.functionPanel;

import jp.leopanda.articleSpreader.client.common.Statics;
import jp.leopanda.articleSpreader.client.common.WndControl;
import jp.leopanda.articleSpreader.shared.BloggerPost;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * Ameba投稿用要約記事の表示画面
 * @author LeoPanda
 *
 */
public class AmebaPanel extends VerticalPanel{
	//画面構成要素
	private TextArea digestBody_ 	= new TextArea();
	private HTML previewArea_		= new HTML(); 
	//コンストラクタ
	public AmebaPanel(BloggerPost bloggerPost){
		digestBody_ = getDisest(bloggerPost);
		previewArea_ = getPreviewArea();
		HorizontalPanel buttonArea = new HorizontalPanel();
		buttonArea.add(getRefreshButton());
		buttonArea.add(getAmebaLinkButton());
		this.add(buttonArea);
		this.add(digestBody_);
		this.add(previewArea_);
	}
	//更新ボタンの作成
	private Button getRefreshButton(){
		Button refreshButton = new Button("表示更新");
		refreshButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				previewArea_.setHTML(digestBody_.getText());
			}
		});
		return refreshButton;
	}
	//Amebaの投稿画面へのリンクボタン作成
	private Button getAmebaLinkButton(){
		Button amebaLinkButton = new Button("Ameba投稿画面へ");
		amebaLinkButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				WndControl.openTab(Statics.PostLink.Ameba.getLink());
			}
		});
		return amebaLinkButton;
	}
	//要約記事表示エリアの作成
	private TextArea getDisest(BloggerPost post) {
		TextArea digestBody = new TextArea();
		digestBody.setStyleName("digestBody");
		String digest = getDigestHtml(post);
		digestBody.setText(digest);
		return digestBody;
	}
	//プレビューエリアの作成
	private HTML getPreviewArea(){
		HTML previewArea = new HTML();
		previewArea.setStyleName("previewArea");
		previewArea.setHTML(digestBody_.getText());
		return previewArea;
	}
	//要約記事HTMLの作成
	private String getDigestHtml(BloggerPost post) {
		String urlStr = post.getUrl();
		String digest = post.getContent().replaceAll("</div>", "<br />")
					.replaceAll("<div.+?>", "")
					.split("<a name='more'>")[0]
					.replaceAll(("width=\".+?\""), ("width=\"400\""))
					.replaceAll(("height=\".+?\""), (""))
					.replaceAll("/s\\d+/","/s400/")
					.replaceAll(("<a href=\".+?\""), ("<a href=\"" + urlStr + "\""))
					.replaceAll(("<a .+? href=\".+?\""), ("<a href=\"" + urlStr + "\""));
		digest += "<br/><b><a href=\"" + urlStr + "\">続きはこちら</a></b></br></br>";
		digest = "<div align=\"center\">" + digest + "</div>";
		return digest;
	}

}
