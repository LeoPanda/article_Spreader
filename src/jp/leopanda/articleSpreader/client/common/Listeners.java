package jp.leopanda.articleSpreader.client.common;

import java.util.EventListener;

import jp.leopanda.articleSpreader.client.BlogSelectorPanel;
import jp.leopanda.articleSpreader.client.mainPanel.HeaderPanel.HeaderPanelEvent;

public class Listeners {
	//ブログ選択パネル用リスナー
	public interface BlogSelectorPanelListener extends EventListener{
		//セレクタの値が変わった
		public void onChanged(BlogSelectorPanel.BlogSelectorPanelEvent event);
	}
	//検索パネル用リスナー
	public interface QueryPanelListener extends EventListener{
		public void onEnterKey();
	}
	//ヘッダーパネル用リスナー
	public interface HeaderPanelListener extends EventListener{
		//「検索」ボタンが押された
		public void onQuery(HeaderPanelEvent event);
		//「前」ボタンが押された
		public void onForwardButton(HeaderPanelEvent event);
		//「後」ボタンが押された
		public void onBackButton(HeaderPanelEvent event);
	}
	//新着記事一覧用リスナー
	public interface ArticleListPanelListener extends EventListener{
		//最終ページに達した
		public void onLastPage();
	}
	//tumblrLoginバー用リスナー
	public interface TumblrLoginBarListener extends EventListener{
		//ログ表示切り替えボタンが押された
		public void onDebugLogButton();
	}
	//tumblrLoginボタン用リスナー
	public interface TumblrLoginButtonListener extends EventListener{
		//AuthTokenが取得された
		public void onAuthTokenReturned();
	}
	//tumblrPostボタン用リスナー
	public interface postTumbulrButtonListener extends EventListener{
		//認証エラーが返された
		public void onAuthError();
		//ボタンが押された
		public void onClick();
	}
}
