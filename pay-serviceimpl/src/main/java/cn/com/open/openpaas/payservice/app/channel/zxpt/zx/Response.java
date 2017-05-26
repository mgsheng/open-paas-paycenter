/****************************************************************************  
 * Copyright © 2016 TCL Technology . All rights reserved.
 * @Title: Response.java 
 * @Prject: cr-model
 * @Package: com.tcl.cr.score.packet.base 
 * @Description: TODO
 * @author: heqingqing   
 * @date: 2016年10月14日 下午4:32:15 
 * @version: V1.0   
 * ****************************************************************************
 */
package cn.com.open.openpaas.payservice.app.channel.zxpt.zx;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 
 * @ClassName: Response 
 * @Description: 通用相应报文
 * @author: heqingqing
 * @date: 2016年10月14日 下午4:32:15  
 */
@XStreamAlias("zxpt")
public class Response<T> {

	/**
	 * 响应报文头
	 */
	private ResponseHead head = new ResponseHead();

	/**
	 * 响应报文头
	 */
	private List<T> body;

	public ResponseHead getHead() {
		return head;
	}

	public void setHead(ResponseHead head) {
		this.head = head;
	}

	public List<T> getBody() {
		if (body == null)
			body = new ArrayList<T>();
		return body;
	}

	public void setBody(List<T> body) {
		this.body = body;
	}
}
