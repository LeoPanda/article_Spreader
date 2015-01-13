package jp.leopanda.articleSpreader.client.functionPanel;

import java.util.ArrayList;
import java.util.List;

import jp.leopanda.articleSpreader.client.common.Statics;
import jp.leopanda.articleSpreader.client.common.Listeners.TumblrLoginButtonListener;
import jp.leopanda.articleSpreader.client.common.Listeners.postTumbulrButtonListener;
import jp.leopanda.articleSpreader.client.tumblr.PostTumblrButton;
import jp.leopanda.articleSpreader.client.tumblr.TumblrLoginButton;
import jp.leopanda.articleSpreader.shared.BloggerPost;
import jp.leopanda.articleSpreader.shared.TumblrPost;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * tumblr投稿用画面
 * @author LeoPanda
 *
 */
public class TumblrPanel extends VerticalPanel{
	//画面の構成要素
	private TumblrLoginButton tumblrLoginButton 
							= new TumblrLoginButton("tumblrログイン"); //tumblrログインボタン
	private List<PostTumblrButton> postButtons 
							= new ArrayList<PostTumblrButton>(); 	//画像投稿ボタン
	private TextArea tumblrTags = new TextArea();					//tumblr用タグ入力エリア
	private TextArea bodyArea 	= new TextArea();					//投稿テキスト入力エリア
	private final String initialTags 
							= "landscape,travel,nature,Japan";		//tumblr初期表示タグ
	//コンストラクタ
	public TumblrPanel(BloggerPost bloggerPost){
		inzParts(bloggerPost);
		this.add(tumblrLoginButton);
		this.add(bodyArea);
		this.add(tumblrTags);
		this.add(getTumblrImgList(bloggerPost));
		tumblrLoginCheck();
	}
	//画面構成要素の初期設定
	private void inzParts(BloggerPost bloggerPost){
		tumblrTags.setText(initialTags);
		tumblrTags.setStyleName("tumblrTags");
		bodyArea.setText(getTumblrDisest(bloggerPost));
		bodyArea.setStyleName("tumblrBodyArea");
	}
	//画像投稿リストの作成
	private FlexTable getTumblrImgList(BloggerPost bloggerPost){
		FlexTable tumblrImgList = new FlexTable();
		List<String> imgUrls = getImgUrls(bloggerPost);
		for (int i = 0; i < imgUrls.size(); i++) {
			//turmbr投稿データの作成と投稿ボタンの生成
			TumblrPost tumblrPost = new TumblrPost();	
			tumblrPost.setPhotoUrl(imgUrls.get(i));
			tumblrPost.setLinkUrl(bloggerPost.getUrl());
			tumblrPost.setPostDate(bloggerPost.getPublished());
			tumblrPost.setDetal(getTumblrDisest(bloggerPost));
			tumblrPost.setFuture(bloggerPost.isFuture());
			postButtons.add(createTrmblrButton(tumblrPost));
			//画像のサムネイル
			Image previewImg = new Image();
			previewImg.setUrl(imgUrls.get(i));
			previewImg.setSize("20%", "");
			//画面へ割付
			tumblrImgList.setWidget(i, 0, postButtons.get(i));
			tumblrImgList.setWidget(i, 1, previewImg);
		}
		return tumblrImgList;
	}
	//「tumblrへ投稿」ボタンの生成
	private PostTumblrButton createTrmblrButton(TumblrPost tumblrPost) {
		final PostTumblrButton tumblrButton = new PostTumblrButton(tumblrPost);
		tumblrButton.addEventListener(new postTumbulrButtonListener() {
			@Override
			// tumblr postで認証エラー
			public void onAuthError() {
				tumblrLoginButton.setVisible(true);
				setPostButtonVisible(false);
			}
			@Override
			public void onClick() {
				// TODO 自動生成されたメソッド・スタブ
				tumblrButton.setPostData(
						resetPostData(tumblrButton.getPostData())); //画面で入力し直されたデータの収集
				tumblrButton.doPost();
			}
		});
		return tumblrButton;
	}

	/*
	 * tumblr送信用要約記事の作成と整形
	 * 
	 */
	private String getTumblrDisest(BloggerPost bloggerPost){
		return "<a href=\"" + bloggerPost.getUrl() + "\">" 
							+ bloggerPost.getTitle() + "</a>";
	}
	/*
	 * 画像URLリストの作成
	 */
	private List<String> getImgUrls(BloggerPost bloggerPost){
		List<String> imgUrls = new ArrayList<String>();
		NodeList<Element> imgNode =
				new HTML(bloggerPost.getContent())
						.getElement().getElementsByTagName("img");
		for (int i = 0; i < imgNode.getLength(); i++) {
			imgUrls.add(imgNode.getItem(i).getAttribute("src"));
		}
		return imgUrls;
	}
	/*
	 * 画像投稿ボタンの表示設定
	 */
	public void setPostButtonVisible(boolean visible){
		for (PostTumblrButton tumblrButton : postButtons){
			 tumblrButton.setVisible(visible);
		}
	}
	/*
	 * 画面で入力された値をtumblr投稿データへセットし直す
	 */
	private TumblrPost resetPostData(TumblrPost orgData){
		orgData.setDetal(bodyArea.getText());
		orgData.setTags(tumblrTags.getText());
		return orgData;
	};
	//tumblrログイン認証は済みか否かで表示を変更する
	private void tumblrLoginCheck() {
		if(Statics.getTumblrCredential().getAuthToken() == null){
			tumblrLoginButton.setVisible(true);
			setPostButtonVisible(false);
		}else{
			tumblrLoginButton.setVisible(false);
			setPostButtonVisible(true);
		}
		//tumblr login認証が完了した時の表示設定
		tumblrLoginButton.addEventListener(new TumblrLoginButtonListener() {
			@Override
			public void onAuthTokenReturned() {
				tumblrLoginButton.setVisible(false);
				setPostButtonVisible(true);
			}
		});
	}

}
