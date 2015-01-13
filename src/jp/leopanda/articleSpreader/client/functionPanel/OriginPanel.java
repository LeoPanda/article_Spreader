package jp.leopanda.articleSpreader.client.functionPanel;

import jp.leopanda.articleSpreader.shared.BloggerPost;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OriginPanel extends VerticalPanel{
	private HTML previewArea = new HTML();	//記事のプレビューエリア
	//コンストラクタ
	public OriginPanel(BloggerPost bloggerPost){
		previewArea.setHTML(bloggerPost.getContent());
		previewArea.setStyleName("previewArea");
		this.add(previewArea);
	}
	

}
