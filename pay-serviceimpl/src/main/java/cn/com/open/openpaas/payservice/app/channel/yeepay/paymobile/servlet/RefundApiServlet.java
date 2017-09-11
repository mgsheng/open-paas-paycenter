package cn.com.open.openpaas.payservice.app.channel.yeepay.paymobile.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.open.openpaas.payservice.app.channel.yeepay.paymobile.utils.ConvertUtils;
import cn.com.open.openpaas.payservice.app.channel.yeepay.paymobile.utils.PaymobileUtils;

/**
 * 退款接口 
 * @author: yingjie.wang    
 * @since : 2015-10-10 14:33
 */

public class RefundApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RefundApiServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//UTF-8编码
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out	= response.getWriter();

		String orderid          = formatStr(request.getParameter("orderid"));
		String origyborderid	= formatStr(request.getParameter("origyborderid"));
		String cause			= formatStr(request.getParameter("cause"));

		int amount		= -1;
		int currency	= -1;

		//transtime, amount, identitytype, terminaltype是必传参数
		if(request.getParameter("amount") == null) {
			out.println("[amount=" + amount + "] must be entered!");
			return;
		}
		if(request.getParameter("currency") == null) {
			out.println("[currency=" + currency + "] must be entered!");
			return;
		}
		
		amount			= ConvertUtils.objectToInt(request.getParameter("amount"));
		currency		= ConvertUtils.objectToInt(request.getParameter("currency"));

		//使用TreeMap
		TreeMap<String, Object> treeMap	= new TreeMap<String, Object>();
		treeMap.put("orderid", 		orderid);
		treeMap.put("origyborderid",origyborderid);
		treeMap.put("amount", 		amount);
		treeMap.put("currency", 	currency);
		treeMap.put("cause", 		cause);

		//第一步 生成AESkey及encryptkey
		String AESKey		= PaymobileUtils.buildAESKey();
		String encryptkey	= PaymobileUtils.buildEncyptkey(AESKey);

		//第二步 生成data
		String data			= PaymobileUtils.buildData(treeMap, AESKey);

		//第三步 http请求，退款接口的请求方式为POST
		String merchantaccount				= PaymobileUtils.getMerchantaccount();
		String url							= PaymobileUtils.getRequestUrl(PaymobileUtils.REFUNDAPI_NAME);
		TreeMap<String, String> responseMap	= PaymobileUtils.httpPost(url, merchantaccount, data, encryptkey);

		//第四步 判断请求是否成功
		if(responseMap.containsKey("error_code")) {
			out.println(responseMap);
			return;
		}

		//第五步 请求成功，则获取data、encryptkey，并将其解密
		String data_response						= responseMap.get("data");
		String encryptkey_response					= responseMap.get("encryptkey");
		TreeMap<String, String> responseDataMap	= PaymobileUtils.decrypt(data_response, encryptkey_response);

		System.out.println("请求返回的明文参数：" + responseDataMap);

		//第六步 sign验签
		if(!PaymobileUtils.checkSign(responseDataMap)) {
			out.println("sign 验签失败！");
			out.println("<br><br>responseMap:" + responseDataMap);
			return;
		}

		//第七步 判断请求是否成功
		if(responseDataMap.containsKey("error_code")) {
			out.println(responseDataMap);
			return;
		}

		//第八步 进行业务处理
		request.setAttribute("responseDataMap", responseDataMap);
		RequestDispatcher view	= request.getRequestDispatcher("jsp/44refundApiResponse.jsp");
		view.forward(request, response);
	}

	public String formatStr(String text) {
		return (text == null) ? "" : text.trim();
	}

}
