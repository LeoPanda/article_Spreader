package jp.leopanda.articleSpreader.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import jp.leopanda.articleSpreader.client.common.HostGateException;
import jp.leopanda.articleSpreader.client.common.HostGateService;
import jp.leopanda.articleSpreader.shared.BlogResource;
import jp.leopanda.articleSpreader.shared.BloggerBlogInfo;
import jp.leopanda.articleSpreader.shared.BloggerPost;
import jp.leopanda.articleSpreader.shared.TumblrPost;
import jp.leopanda.articleSpreader.shared.TumblrCredential;
import jp.leopanda.common.server.UrlService;
import jp.leopanda.common.server.UrlService.ContentType;
import jp.leopanda.common.server.UrlService.Result;
import net.arnx.jsonic.JSON;

import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class HostGateServiceImpl extends RemoteServiceServlet implements HostGateService{
	private static final long serialVersionUID = 1L;
	private static final String API_KEY = "AIzaSyDKNuIBU9vxVAuYTH5eRMpGVYFiof5AdWA";
	public static final int MAX_RESULTS = 15;
	private TumblrService tumblrService = new TumblrService();
	/**
	 *Blogger User情報の取得
	 *<pre>
	 * @param String token : Google認証token
	 * </pre>
	 */
	public BlogResource[] getbloggerInfo(final String token) throws HostGateException{
		String urlStr = "https://www.googleapis.com/blogger/v3/users/self/blogs?key=" + API_KEY;
		BlogResource[] blogResources = null;
		UrlService urlService = new UrlService();		
		Map<Result,String> results = 
			urlService.fetchGet(urlStr, 
							urlService.addToken(token,
							urlService.setHeader(ContentType.HTML)));
		
		if(Integer.valueOf(results.get(Result.RETCODE)) != HttpStatus.SC_OK){
				throw new HostGateException(results.get(Result.RETCODE));
		}
		
		BloggerBlogInfo bInfo = 
				JSON.decode(results.get(Result.BODY),BloggerBlogInfo.class);
		blogResources =  bInfo.getItems();
		return blogResources; 
	}
	/**
	 * ブログのタグ一覧を取得する
	 * @param blogId	ブログID
	 * @param token		ログイントークン
	 * @return　ArrayList<String> ブログのタグ名一覧
	 * @throws HostGateException
	 */
	public ArrayList<String> getCategories(String blogId,String token) throws HostGateException{
		String urlStr 	= "https://www.blogger.com/feeds/" + blogId 
				+ "/posts/default?max-results=1&key=" + API_KEY;

		UrlService urlService = new UrlService();		
		Map<Result,String> results = 
			urlService.fetchGet(urlStr, 
							urlService.addToken(token,
							urlService.setHeader(ContentType.HTML)));
		
		if(Integer.valueOf(results.get(Result.RETCODE)) != HttpStatus.SC_OK){
			throw new HostGateException(results.get(Result.RETCODE));
		}

		ArrayList<String> categoryList = new ArrayList<String>();
		Document doc = Jsoup.parse(results.get(Result.BODY));
		Elements categories = doc.getElementsByTag("category");
		for (Element category : categories){
			categoryList.add(category.attr("term"));
		}
		return categoryList;		
	}
	/**
	 * Blogger 記事の取得
	 * 2013.8現在、Blogger API V3では未来記事を取得できないため、Atom形式のV2を使用している。
	 * 未来記事の場合は記事へのリンクが渡されないので、代替リンクをコメントのリンクから得ている。
	 */
	public BloggerPost[] getbloggerPosts(final String blogId,final String token,String query,int startIndex) throws HostGateException{
			String urlStr = "https://www.blogger.com/feeds/" + blogId 
							+ "/posts/default?max-results=" + String.valueOf(MAX_RESULTS)
							+ "&start-index=" + String.valueOf(startIndex) 
							+ query + "&key="+ API_KEY;

		BloggerPost[] bloggerPosts;
		UrlService urlService = new UrlService();		
		Map<Result,String> results = 
			urlService.fetchGet(urlStr, 
							urlService.addToken(token,
							urlService.setHeader(ContentType.HTML)));
		
		if(Integer.valueOf(results.get(Result.RETCODE)) != HttpStatus.SC_OK){
			throw new HostGateException(results.get(Result.RETCODE));
		}
		
		Document doc = Jsoup.parse(results.get(Result.BODY));
		Elements entrys = doc.getElementsByTag("entry");
		bloggerPosts = new BloggerPost[entrys.size()];
		int i=-1;
		for(Element entry : entrys){
			i++;
			bloggerPosts[i] = new BloggerPost();
			bloggerPosts[i].setFuture(true);
			for (Element article : entry.children()){
				if( article.tagName().equals("id")){bloggerPosts[i].setId(article.text());}
				else if( article.tagName().equals("title")){bloggerPosts[i].setTitle(article.text());}
				else if( article.tagName().equals("published")){bloggerPosts[i].setPublished(article.text());}
				else if( article.tagName().equals("content")){bloggerPosts[i].setContent(article.text());}
				else if( article.tagName().equals("app:draft")){bloggerPosts[i].setIsDraft(article.text());}
				else if( article.tagName().equals("link")){
					bloggerPosts[i] = manipulateFuture(article,bloggerPosts[i]);}
				}
		}
		return bloggerPosts; 
	}
	/**
	 * 記事の投稿日付が未来記事か否かでセットする項目を変更する。
	 * @param article
	 * @param post
	 * @return
	 */
	private BloggerPost manipulateFuture(Element article,BloggerPost post){
		if(article.attr("rel").equals("alternate")){
			post.setUrl(article.attr("href"));
			post.setFuture(false);
		}
		if(article.attr("rel").equals("replies")){
			String linkCandidate = article.attr("href");
			if(linkCandidate.indexOf("#")>0){
				post.setUrl(linkCandidate.substring(0,linkCandidate.indexOf("#")));
			}else{
				post.setUrl(linkCandidate);
			}
		}
		return post;
	}

	/**
	 * Tumblrへのポスト
	 * @param photoUrl
	 * @param linkUrl
	 * @param postDate
	 * @param detail
	 * @throws HostGateException
	 */
	public String postTumblr(TumblrCredential tumblr,TumblrPost postData) throws HostGateException{
		try {
			tumblrService.postPhoto(tumblr,postData);
		} catch (IllegalAccessException e) {
			// TODO 自動生成された catch ブロック
			throw new HostGateException(e.getMessage());
		} catch (InstantiationException e) {
			// TODO 自動生成された catch ブロック
			throw new HostGateException(e.getMessage());
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			throw new HostGateException(e.getMessage());
		} catch (HostGateException e) {
			// TODO 自動生成された catch ブロック
			throw new HostGateException(e.getMessage());
		}
		return "CallBacked";
	}
	/**
	 * Tumblr用認証URLの取得
	 * @param tumblr
	 * @return
	 */
	public TumblrCredential getTumblrAuthUrl(TumblrCredential tumblr){
		return tumblrService.getAuthorizationUrl(tumblr);
	}
	/**
	 * Tumblr accessTokenの取得
	 * @param tumblr
	 * @return
	 */
	public TumblrCredential getTumblrAuthToken(TumblrCredential tumblr){
		return tumblrService.getAccessToken(tumblr);
	}
}
