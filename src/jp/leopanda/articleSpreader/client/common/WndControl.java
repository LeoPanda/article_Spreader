package jp.leopanda.articleSpreader.client.common;

/**
 * ネイティブJavaScriptでブラウザを直接コントロールする共通メソッド
 * @author LeoPanda
 *
 */
public class WndControl {

	//open tab window
	public static native void openTab(String url) /*-{
		$wnd.open(url);
	}-*/;
	//open popup window
	public static native void openWindow(String url)/*-{
	$wnd.open(url,'popupWindow','width=800,height=600');
	}-*/;
	//open alert window
	public static native void alert(String msg) /*-{
		$wnd.alert(msg);
	}-*/;


}
