package cn.com.open.openpaas.payservice.app.channel.yeepay.paymobile.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.open.openpaas.payservice.app.channel.yeepay.paymobile.utils.PaymobileUtils;

/**
 * 回调 
 * @author: yingjie.wang
 * @since : 2015-10-10 22:10
 */

public class CallbackApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CallbackApiServlet() {
		super();
	}

	//一键支付的页面回调方式为GET
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//UTF-8编码
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out	= response.getWriter();

		String data			= formatStr(request.getParameter("data"));
		String encryptkey	= formatStr(request.getParameter("encryptkey"));

		//解密data
		TreeMap<String, String>	dataMap	= PaymobileUtils.decrypt(data, encryptkey);

		System.out.println("返回的明文参数：" + dataMap);

		//sign验签
		if(!PaymobileUtils.checkSign(dataMap)) {
			out.println("sign 验签失败！");
			out.println("<br><br>dataMap:" + dataMap);
			return;
		}

		request.setAttribute("dataMap", dataMap);
		RequestDispatcher view	= request.getRequestDispatcher("jsp/callbackApi.jsp");
		view.forward(request, response);
	}
	
	//一键支付的后台回调方式为POST
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//UTF-8编码
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out	= response.getWriter();

		String data			= formatStr(request.getParameter("data"));
		String encryptkey	= formatStr(request.getParameter("encryptkey"));

		//解密data
		TreeMap<String, String>	dataMap	= PaymobileUtils.decrypt(data, encryptkey);

		System.out.println("返回的明文参数：" + dataMap);

		//sign验签
		if(!PaymobileUtils.checkSign(dataMap)) {
			out.println("sign 验签失败！");
			out.println("<br><br>dataMap:" + dataMap);
			return;
		}

		//回写SUCCESS
		out.println("SUCCESS");
		out.flush();
		out.close();
	}

	public String formatStr(String text) {
		return (text == null) ? "" : text.trim();
	}

}
