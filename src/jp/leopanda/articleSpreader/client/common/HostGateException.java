package jp.leopanda.articleSpreader.client.common;

import java.io.Serializable;
import java.lang.Exception;

@SuppressWarnings("serial")
public class HostGateException extends Exception implements Serializable {
	public HostGateException(){
	}
	private String statusCode;
	public HostGateException(String statusCode) {
	    this.statusCode = statusCode;
	}

	public String getStatus() {
	    return this.statusCode;
	}

}
