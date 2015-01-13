#articleSpreader blogger blog 記事分散ツール
##主な機能
*要約記事の自動作成
　元記事へのリンクと代表画像を含む要約記事を自動作成する。
　要約記事に掲載された写真をボタンひとつでtumblrへ投稿する。

##セットアップ方法

このプログラムはGWT(google web tool kit)を使用し、GAE(google application engine)上で動作するよう
設計されています。また、実行にあたっては独自のGoogleアカウントが必要です。
プログラムを利用するためにはGWTをダウンロードし、GAE上にアカウントとプロジェクトを作成してください。

###１）jp.leopanda.articleSpreader.client.common.Statics.javaの下記固定値を設定してください。

```java
	//Google client ID = デプロイ先Google　App　Engineのアプリケーション識別子
	private static final String googleClientId  		= "_________________"; 	// set your own.
	//Tumblr OAuthアクセス諸元の初期値
	private static final String tumblrConsumerKey 		= "_________________"; 	// set your own.
	private static final String tumblrConsumerSecret 	= "_________________"; 	// set your own.
	private static final String tumblrBlogName 			= "_______"; 			// set your own.
```
*googleClientId

googleアカウント用OAuthクライアントID。
Google Developpers console(https://console.developers.google.com/project)へアクセスし、取得してください。
詳しい説明はhttps://developers.google.com/adsense/tutorials/oauth-generic?hl=jaを参照してください。

*tumblrComsumerKey tumblrConsuerSecret

tumblrへ記事を自動投稿する場合に必要です。
http://www.tumblr.com/oauth/appsへアクセスしアプリケーションを登録することで入手できます。
詳しい説明はhttp://kid0725.usamimi.info/api_v2_docs_ja.htmlを参照してください。

*tumblrBlogName

tumblrブログの名前。通常は xxxx.tumblr.comのxxxxに当たります。

###2)jp.loepanda.articleSpreader.server.HostGateServiceImpl.javaの下記固定値を設定してください。

```java
	private static final String API_KEY = "_____________"; //set your own.
```

 *API_EKY
Google Developpers console(https://console.developers.google.com/project)へアクセスし、
プロジェクトの認証情報から　APIキーを取得してください。
 

###3）commonServiceのライブラリを入手してください。
https://github.com/LeoPanda/gwtOauthLoginPanelへアクセスしcommonServiceのライブラリを入手し、
ビルドバスに追加するかsrcフォルダへのソースリンクを作成してください。


