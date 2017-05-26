/****************************************************************************  
 * Copyright © 2016 TCL Technology . All rights reserved.
 * @Title: Request.java 
 * @Prject: cr-model
 * @Package: com.tcl.cr.score.packet.base 
 * @Description: TODO
 * @author: heqingqing   
 * @date: 2016年10月14日 下午4:31:17 
 * @version: V1.0   
 * ****************************************************************************
 */
package cn.com.open.openpaas.payservice.app.channel.zxpt.zx;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 
 * @ClassName: Request 
 * @Description: 通用请求报文
 * @author: heqingqing
 * @date: 2016年10月14日 下午4:31:17  
 */
@XStreamAlias("zxpt")
public class Request<T>{

	/**
	 * 请求报文头
	 */
	@XStreamAlias("head")
	private RequestHead head = new RequestHead();

	/**
	 * 请求报文体
	 */
	@XStreamAlias("body")
	private List<T> body = new ArrayList<T>();

	public RequestHead getHead() {
		return head;
	}

	public void setHead(RequestHead head) {
		this.head = head;
	}

	public List<T> getBody() {
		return body;
	}

	public void setBody(List<T> body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "Request [head=" + head + ", body=" + body + "]";
	}

}
