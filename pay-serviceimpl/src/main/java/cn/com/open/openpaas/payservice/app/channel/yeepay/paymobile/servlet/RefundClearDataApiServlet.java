package cn.com.open.openpaas.payservice.app.channel.yeepay.paymobile.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import cn.com.open.openpaas.payservice.app.channel.yeepay.paymobile.utils.PaymobileUtils;

/**
 * 支付清算文件下载接口
 * @author: yingjie.wang    
 * @since : 2015-10-10 20:17
 */

public class RefundClearDataApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RefundClearDataApiServlet() {
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

		String startdate    = formatStr(request.getParameter("startdate"));
		String enddate      = formatStr(request.getParameter("enddate"));

		//使用TreeMap
		TreeMap<String, Object> treeMap	= new TreeMap<String, Object>();
		treeMap.put("startdate",startdate);
		treeMap.put("enddate", 	enddate);

		//生成AESkey及encryptkey
		String AESKey			= PaymobileUtils.buildAESKey();
		String encryptkey		= PaymobileUtils.buildEncyptkey(AESKey);

		//生成data
		String data				= PaymobileUtils.buildData(treeMap, AESKey);

		String merchantaccount	= PaymobileUtils.getMerchantaccount();
		String url				= PaymobileUtils.getRequestUrl(PaymobileUtils.REFUNDCLEARDATAAPI_NAME);
		
		//发起请求
		InputStream	responseStream	= PaymobileUtils.clearDataHttpGet(url, merchantaccount, data, encryptkey);
		BufferedReader	reader		= new BufferedReader(new InputStreamReader(responseStream, "UTF-8"));
		String line					= reader.readLine();
		
		//当请求失败时，会返回一个json串。
		if(line.startsWith("{")) {
			Map<String, String> jsonMap	= JSON.parseObject(line,new TypeReference<TreeMap<String, String>>() {});
			if(jsonMap.containsKey("error_code")) {
				out.println(jsonMap);
			} else {
				String dataFromYeepay		= formatStr((String)jsonMap.get("data"));
				String encryptkeyFromYeepay	= formatStr((String)jsonMap.get("encryptkey")); 
				Map<String, String> respMap	= PaymobileUtils.decrypt(dataFromYeepay, encryptkeyFromYeepay);
				out.println(respMap);
			}
			return;
		}

		//获得当前绝对路径	
		String realPath 		= this.getServletConfig().getServletContext().getRealPath("/"); 
		//对账文件的存储路径
		String path				= realPath + File.separator + "YeepayClearData";
		String time				= String.valueOf(System.currentTimeMillis());
		String fileName			= "RefundClearData" + "_" + time + ".txt";
		String filePath			= path + File.separator + fileName;
		File file				= new File(filePath);
		file.getParentFile().mkdirs();
		file.createNewFile();

		File outputFile			= new File(filePath);
		FileWriter fileWriter	= new FileWriter(outputFile);
		BufferedWriter writer	= new BufferedWriter(fileWriter);

		writer.write(line);
		writer.write(System.getProperty("line.separator"));
		while((line = reader.readLine()) != null) {
			writer.write(line);
			writer.write(System.getProperty("line.separator"));
		}
		writer.close();

		request.setAttribute("filePath", filePath);
		RequestDispatcher view	= request.getRequestDispatcher("jsp/46refundClearDataApiResponse.jsp");
		view.forward(request, response);
	}

	public String formatStr(String text) {
		return (text == null) ? "" : text.trim();
	}

}
