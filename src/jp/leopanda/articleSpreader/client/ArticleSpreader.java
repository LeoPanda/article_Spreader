package jp.leopanda.articleSpreader.client;

import jp.leopanda.articleSpreader.client.BlogSelectorPanel.BlogSelectorPanelEvent;
import jp.leopanda.articleSpreader.client.common.Statics;
import jp.leopanda.articleSpreader.client.common.Listeners.BlogSelectorPanelListener;
import jp.leopanda.articleSpreader.client.mainPanel.MainPanel;
import jp.leopanda.common.client.GoogleLoginBar;
import jp.leopanda.common.client.GoogleLoginBar.InfoEvent;
import jp.leopanda.common.client.GoogleLoginBar.ScopeName;
import jp.leopanda.common.client.LoginBarListener;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * AmebaからBloggerへブログを移行したが、元のAmebaブログにもBloggerに書いた新着記事の要約をPOSTしたい。
 * その作業を補助するため、このプログラムはBloggerブログの要約記事を生成する。
 * 
 * Googleアカウントへログインすると、そのアカウントに紐付いているBloggerブログの一覧を表示し、
 * ブログを選ぶとそのブログの記事一覧と記事検索パネルを表示する。
 * 一覧に表示された記事タイトルをクリックすると、その記事の要約をHTMLとプレビューで表示する。
 * 
 * @author LeoPanda
 *
 */
public class ArticleSpreader implements EntryPoint {
	// 画面構成要素のメンバ変数
	private HorizontalPanel outerPanel_; 	//外枠
	private GoogleLoginBar loginBar_;		//Google認証用バー
	private MainPanel mainPanel_;			//メイン画面
	/**
	 * メイン処理
	 */
	public void onModuleLoad() {
		// Googleログインバーの表示
		loginBar_ = new GoogleLoginBar(Statics.getClientId(), 
									GoogleLoginBar.addScope(ScopeName.BLOGGER)
									+GoogleLoginBar.addScope(ScopeName.BLOGGERV2));
		loginBar_.addListerner(new LoginControl());
		RootPanel.get("loginBarContainer").add(loginBar_);
		//アウターパネルの割付
		outerPanel_ = new HorizontalPanel();
		RootPanel.get("mainPanel").add(outerPanel_);
	}
	/**
	 * Googleログインバー　イベントハンドラ
	 */
	private class LoginControl implements LoginBarListener {
		@Override
		//ログオフされた
		public void onLoggedOff(InfoEvent event) {
			outerPanel_.removeFromParent();
		}
		@Override
		//ログインを検出後、メイン画面を表示する
		public void onLoggedIn(InfoEvent event) {
			Statics.setLoginToken(loginBar_.getToken());
			//Bloggerブログ選択画面の表示
			BlogSelectorPanel blogSelectorPanel = new BlogSelectorPanel();
			blogSelectorPanel.addEventListener(new OnBlogSelected());
			outerPanel_.add(blogSelectorPanel);
			RootPanel.get("mainPanel").add(outerPanel_);
			//メイン画面の表示
			mainPanel_ = new MainPanel();
			outerPanel_.add(mainPanel_);
			outerPanel_.setWidth("95%");
			outerPanel_.setCellWidth(mainPanel_, "65%");
		}
	}
	/**
	 * ブログ選択画面イベントハンドラ
	 */
	private class OnBlogSelected implements BlogSelectorPanelListener {
		@Override
		//ブログが選択された
		public void onChanged(BlogSelectorPanelEvent event) {
			//メイン画面の初期表示
			mainPanel_.resetPanel(event.getBlogId());
		}
	}

}
