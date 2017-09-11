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
 * 支付接口 
 * @author: yingjie.wang    
 * @since : 2015-10-08 22:06
 */

//@WebServlet("/PayApiServlet")
public class PayApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PayApiServlet() {
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

		String orderid              = formatStr(request.getParameter("orderid"));
		String productcatalog       = formatStr(request.getParameter("productcatalog"));
		String productname          = formatStr(request.getParameter("productname"));
		String identityid           = formatStr(request.getParameter("identityid"));
		String userip               = formatStr(request.getParameter("userip"));
		String paytool				= formatStr(request.getParameter("paytool"));
		String terminalid           = formatStr(request.getParameter("terminalid"));
		String userua               = formatStr(request.getParameter("userua"));
		String productdesc          = formatStr(request.getParameter("productdesc"));
		String fcallbackurl         = formatStr(request.getParameter("fcallbackurl"));
		String callbackurl          = formatStr(request.getParameter("callbackurl"));
		String paytypes             = formatStr(request.getParameter("paytypes"));
		String cardno				= formatStr(request.getParameter("cardno")); 
		String idcardtype			= formatStr(request.getParameter("idcardtype")); 
		String idcard				= formatStr(request.getParameter("idcard")); 
		String owner 				= formatStr(request.getParameter("owner")); 

		int transtime				= -1; 
		int amount              	= -1; 
		int identitytype        	= -1; 
		int terminaltype        	= -1; 
		int directpaytype		    =  0;
		int orderexpdate        	= -1; 
		int currency	        	= -1; 
		int version		        	= -1;

		//transtime, amount, identitytype, terminaltype是必传参数
		if(request.getParameter("transtime") == null) {
			out.println("[transtime=" + transtime + "] must be entered!");
			return;
		}
		if(request.getParameter("amount") == null) {
			out.println("[amount=" + amount + "] must be entered!");
			return;
		}
		if(request.getParameter("identitytype") == null) {
			out.println("[identitytype=" + identitytype + "] must be entered!");
			return;
		}
		if(request.getParameter("terminaltype") == null) {
			out.println("[terminaltype=" + terminaltype + "] must be entered!");
			return;
		}

		transtime			= ConvertUtils.objectToInt(request.getParameter("transtime"));
		amount				= ConvertUtils.objectToInt(request.getParameter("amount"));
		identitytype		= ConvertUtils.objectToInt(request.getParameter("identitytype"));
		terminaltype		= ConvertUtils.objectToInt(request.getParameter("terminaltype"));
		directpaytype		= ConvertUtils.objectToInt(request.getParameter("directpaytype"));
		version		        = ConvertUtils.objectToInt(request.getParameter("version"));

		//orderexpdate, currency, version是非必传参数
		if(request.getParameter("orderexpdate") == null) {
			//如果该参数不填写，设置为24小时
			orderexpdate	= 24*60;
		} else {
			orderexpdate	= ConvertUtils.objectToInt(request.getParameter("orderexpdate"));
		}
		if(request.getParameter("currency") == null) {
			currency	= 156;
		} else {
			currency	= ConvertUtils.objectToInt(request.getParameter("currency"));
		}
		if(request.getParameter("version") == null) {
			version		= 0;
		} else {
			version		= ConvertUtils.objectToInt(request.getParameter("version"));
		}

		//使用TreeMap
		TreeMap<String, Object> treeMap	= new TreeMap<String, Object>();
		treeMap.put("orderid", 			orderid);
		treeMap.put("productcatalog", 	productcatalog);
		treeMap.put("productname", 		productname);
		treeMap.put("identityid", 		identityid);
		treeMap.put("userip", 			userip);
		treeMap.put("paytool", 			paytool);
//		treeMap.put("directpaytype", 	directpaytype);
		treeMap.put("terminalid", 		terminalid);
//		treeMap.put("userua", 			userua);
		treeMap.put("transtime", 		transtime);
		treeMap.put("amount", 			amount);
		treeMap.put("identitytype", 	identitytype);
		treeMap.put("terminaltype", 	terminaltype);
		treeMap.put("productdesc", 		productdesc);
//		treeMap.put("fcallbackurl", 	fcallbackurl);
		treeMap.put("callbackurl", 		callbackurl);
//		treeMap.put("paytypes", 		paytypes);
		treeMap.put("currency", 		currency);
//		treeMap.put("orderexpdate", 	orderexpdate);
		treeMap.put("version", 			version);
//		treeMap.put("cardno", 			cardno);
//		treeMap.put("idcardtype", 		idcardtype);
//		treeMap.put("idcard", 			idcard);
//		treeMap.put("owner", 			owner);

		System.out.println(treeMap);
		
		//第一步 生成AESkey及encryptkey
		String AESKey		= PaymobileUtils.buildAESKey();
		String encryptkey	= PaymobileUtils.buildEncyptkey(AESKey);

		//第二步 生成data
		String data			= PaymobileUtils.buildData(treeMap, AESKey);

		//第三步 获取商户编号及请求地址，并组装支付链接
		String merchantaccount	= PaymobileUtils.getMerchantaccount();
		String url				= PaymobileUtils.getRequestUrl(PaymobileUtils.PAYAPI_NAME);
		TreeMap<String, String> responseMap	= PaymobileUtils.httpPost(url, merchantaccount, data, encryptkey);

		System.out.println("url=" + url);
		System.out.println("merchantaccount=" + merchantaccount);
		System.out.println("data=" + data);
		System.out.println("encryptkey=" + encryptkey);
		
		//第四步 判断请求是否成功
		if(responseMap.containsKey("error_code")) {
			out.println(responseMap);
			return;
		}
		
		//第五步 请求成功，则获取data、encryptkey，并将其解密
		String data_response						= responseMap.get("data");
		String encryptkey_response					= responseMap.get("encryptkey");
		TreeMap<String, String> responseDataMap	= PaymobileUtils.decrypt(data_response, encryptkey_response);

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
		RequestDispatcher view	= request.getRequestDispatcher("jsp/41payApiResponse.jsp");
		view.forward(request, response);
	}

	public String formatStr(String text) {
		return (text == null) ? "" : text.trim();
	}

}
