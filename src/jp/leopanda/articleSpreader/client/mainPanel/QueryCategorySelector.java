package jp.leopanda.articleSpreader.client.mainPanel;

import java.util.ArrayList;

import jp.leopanda.articleSpreader.client.common.HostGateException;
import jp.leopanda.articleSpreader.client.common.HostGateServiceAsync;
import jp.leopanda.articleSpreader.client.common.Statics;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
/**
 * 検索用のカテゴリー選択リストボックス
 * @author LeoPanda
 *
 */
public class QueryCategorySelector extends ListBox{
	//RPCハンドル
	HostGateServiceAsync hgc_ = Statics.getHgc();
	/**
	 * セレクタのリセット
	 * Blogger APIからブログのカテゴリ（タグ）を取得しセレクタにセットする
	 * @param blogId String ブログID
	 */
	public void resetCategory(String blogId){
		hgc_.getCategories(blogId, Statics.getLoginToken(),
			new AsyncCallback<ArrayList<String>>() {
				@Override
				public void onFailure(Throwable caught) {
					if (caught instanceof HostGateException) {
						Window.alert("カテゴリリスト取得に失敗しました。HTTP StatisCode="
								+ ((HostGateException) caught).getStatus());
					} else {
						Window.alert("カテゴリリスト取得時にRPCエラーが発生しました。");
					}
				}

				@Override
				public void onSuccess(ArrayList<String> result) {
					setSelectorItem(result);
				}
			});
	}
	/**
	 * セレクタにアイテムをセット
	 * @param categories
	 */
	private void setSelectorItem(ArrayList<String> categories) {
		this.clear();
		this.addItem("");//先頭はブランク
		for (String category : categories) {
			this.addItem(category);
		}
	}
	/**
	 * 選択されたカテゴリ名を返す
	 * @return　選択されたカテゴリ名
	 */
	public String getCategory(){
		return this.getItemText(this.getSelectedIndex());
	}
}
