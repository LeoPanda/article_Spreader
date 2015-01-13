package jp.leopanda.articleSpreader.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TumblrApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import jp.leopanda.articleSpreader.client.common.HostGateException;
import jp.leopanda.articleSpreader.modifiedjumblr.JumblrClient;
import jp.leopanda.articleSpreader.modifiedjumblr.Photo;
import jp.leopanda.articleSpreader.modifiedjumblr.PhotoPost;
import jp.leopanda.articleSpreader.shared.TumblrPost;
import jp.leopanda.articleSpreader.shared.TumblrCredential;


public class TumblrService {
	private static final Logger log = Logger.getLogger(TumblrService.class.getName());
	/**
	 * Tumblr写真タイプのポスト
	 * @param tumblr						TumblrCredencial
	 * @param postData						TumblrPost
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IOException
	 */
	public void postPhoto(TumblrCredential tumblr,TumblrPost postData) throws IllegalAccessException, InstantiationException, IOException ,HostGateException {
		JumblrClient client = new JumblrClient(tumblr.getConsumerId(),tumblr.getConsumerSecret());
		client.setService(getOAuthService(tumblr)); //add for GAE 2014.6.29 leopanda
		client.setToken(tumblr.getAuthToken(), tumblr.getAuthTokenSecret());
		Photo photo = new Photo(postData.getPhotoUrl());
		PhotoPost post = client.newPost(tumblr.getBlogName(), PhotoPost.class);
		post.setDate(postData.getPostDate());
		post.setPhoto(photo);
		post.setLinkUrl(postData.getLinkUrl());
		post.setFuture(postData.isFuture());
		if(postData.isFuture()){
			post.setState("queue");
		}
		post.addTag(postData.getTags());
		post.setCaption(postData.getDetal());
		post.save();
	}
	/**
	 * Tumblr認証URLの取得
	 * @param tumblr
	 * @return
	 */
	public TumblrCredential getAuthorizationUrl(TumblrCredential tumblr){
		OAuthService service = getOAuthService(tumblr);
		Token requestToken = service.getRequestToken();
		tumblr.setRequestToken(requestToken.getToken());
		tumblr.setRequestTokenSecret(requestToken.getSecret());
		tumblr.setAuthUrl(service.getAuthorizationUrl(requestToken));
		return tumblr;
		
	}

	/**
	 *  Tumblr認証Tokenの取得
	 * @param tumblr
	 * @return
	 */
	public TumblrCredential getAccessToken(TumblrCredential tumblr){
		OAuthService service = getOAuthService(tumblr);
		Token requestToken = new Token(tumblr.getRequestToken(),tumblr.getRequestTokenSecret());
		Verifier verivier  = new Verifier(tumblr.getRequestVerifier());
		Token accessToken = service.getAccessToken(requestToken,verivier);
		tumblr.setAuthToken(accessToken.getToken());
		tumblr.setAuthTokenSecret(accessToken.getSecret());
		return tumblr;
	}
	/**
	 * Scribe Tumblr用OAUTHクライアントの作成
	 * @param tumblr
	 * @return
	 */
	private OAuthService getOAuthService(TumblrCredential tumblr){
		LogOutputStream stream = new LogOutputStream(log, Level.INFO);
		return new ServiceBuilder()
			.provider(TumblrApi.class)
			.apiKey(tumblr.getConsumerId())
			.apiSecret(tumblr.getConsumerSecret())
			.callback(tumblr.getRedirectUrl())
// set follow 2 lines when you need debug.
//			.debug()
//			.debugStream(stream)
			.build();
	}
	/**
	 * ログ出力用アウトプットストリームをGAEのコンソールへ表示するためのクラス
	 * @author LeoPanda
	 *
	 */
	private class LogOutputStream extends OutputStream {
	    /** The logger where to log the written bytes. */
	    private Logger logger;

	    /** The level. */
	    private Level level;

	    /** The internal memory for the written bytes. */
	    private String mem;

	    /**
	     * Creates a new log output stream which logs bytes to the specified logger with the specified
	     * level.
	     *
	     * @param logger the logger where to log the written bytes
	     * @param level the level
	     */
	    public LogOutputStream (Logger logger, Level level) {
	        setLogger (logger);
	        setLevel (level);
	        mem = "";
	    }

	    /**
	     * Sets the logger where to log the bytes.
	     *
	     * @param logger the logger
	     */
	    public void setLogger (Logger logger) {
	        this.logger = logger;
	    }

	    /**
	     * Returns the logger.
	     *
	     * @return DOCUMENT ME!
	     */
	    public Logger getLogger () {
	        return logger;
	    }

	    /**
	     * Sets the logging level.
	     *
	     * @param level DOCUMENT ME!
	     */
	    public void setLevel (Level level) {
	        this.level = level;
	    }

	    /**
	     * Returns the logging level.
	     *
	     * @return DOCUMENT ME!
	     */
	    public Level getLevel () {
	        return level;
	    }

	    /**
	     * Writes a byte to the output stream. This method flushes automatically at the end of a line.
	     *
	     * @param b DOCUMENT ME!
	     */
	    public void write (int b) {
	        byte[] bytes = new byte[1];
	        bytes[0] = (byte) (b & 0xff);
	        mem = mem + new String(bytes);

	        if (mem.endsWith ("\n")) {
	            mem = mem.substring (0, mem.length () - 1);
	            flush ();
	        }
	    }

	    /**
	     * Flushes the output stream.
	     */
	    public void flush () {
	        logger.log (level, mem);
	        mem = "";
	    }
	}

}	


