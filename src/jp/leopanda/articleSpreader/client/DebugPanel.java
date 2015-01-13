package jp.leopanda.articleSpreader.client;

import jp.leopanda.articleSpreader.client.common.Statics;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * デバッグ用ログ画面
 * 表示させるためにはStaticsクラスのDEBUG_MODEをtrueにセットしてください。
 * @author LeoPanda
 *
 */
public class DebugPanel extends VerticalPanel{
	//画面の構成要素
	TextArea logArea = new TextArea();
	Button clearButton = new Button("ログのクリア");
	//メンバ変数
	private String log_ = new String("lunched.\n");
	private String cookie_log = "articlespreader_tumblr_";
	/**
	 * パネルのコンストラクタ
	 */
	public DebugPanel(){
		HorizontalPanel header = new HorizontalPanel();
		HTML title = new HTML("<h3>Debug log</h3>");
		header.add(title);
		header.add(clearButton);
		this.add(header);
		logArea.setWidth("1000px");
		logArea.setVisibleLines(20);
		this.add(logArea);
		log_ = Cookies.getCookie(cookie_log);
		clearButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				log_ = "";
				logArea.setText(null);
				Cookies.removeCookie(cookie_log);
			}
		});
	}
	/*
	 * ログのセット
	 */
	public void addLog(String log){
		if (Statics.debugMode()) {
			log_ += log + "\n";
			logArea.setText(log_);
			Cookies.setCookie(cookie_log, log_);
		}
	}
	
}
