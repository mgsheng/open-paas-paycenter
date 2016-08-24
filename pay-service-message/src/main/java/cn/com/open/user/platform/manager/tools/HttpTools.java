package cn.com.open.user.platform.manager.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import sun.net.www.protocol.http.HttpURLConnection;

public class HttpTools {

	/**
	 * 连接超时
	 */
	private static int connectTimeOut = 5000;

	/**
	 * 读取数据超时
	 */
	private static int readTimeOut = 10000;

	/**
	 * 请求编码
	 */
	private static String requestEncoding = "UTF-8";

	private static Logger logger = Logger.getLogger(HttpTools.class);

	private static Map<String, Object> headerParamters;

	/**
	 * @return 连接超时(毫秒)
	 * @see com.hengpeng.common.web.HttpRequestProxy#connectTimeOut
	 */
	public static int getConnectTimeOut() {
		return HttpTools.connectTimeOut;
	}

	/**
	 * @return 读取数据超时(毫秒)
	 * @see com.hengpeng.common.web.HttpRequestProxy#readTimeOut
	 */
	public static int getReadTimeOut() {
		return HttpTools.readTimeOut;
	}

	/**
	 * @return 请求编码
	 * @see com.hengpeng.common.web.HttpRequestProxy#requestEncoding
	 */
	public static String getRequestEncoding() {
		return requestEncoding;
	}
	
	/**
	 * @return 请求Header
	 */
	public static Map<String, Object> getHeaderParamters() {
		return headerParamters;
	}

	/**
	 * @param connectTimeOut 连接超时(毫秒)
	 * @see com.hengpeng.common.web.HttpRequestProxy#connectTimeOut
	 */
	public static void setConnectTimeOut(int connectTimeOut) {
		HttpTools.connectTimeOut = connectTimeOut;
	}

	/**
	 * @param readTimeOut 读取数据超时(毫秒)
	 * @see com.hengpeng.common.web.HttpRequestProxy#readTimeOut
	 */
	public static void setReadTimeOut(int readTimeOut) {
		HttpTools.readTimeOut = readTimeOut;
	}

	/**
	 * @param requestEncoding 请求编码
	 * @see com.hengpeng.common.web.HttpRequestProxy#requestEncoding
	 */
	public static void setRequestEncoding(String requestEncoding) {
		HttpTools.requestEncoding = requestEncoding;
	}
	
	/**
	 * @param headerParamters 请求Header
	 */
	public static void setHeaderParamters(Map<String, Object> headerParamters) {
		HttpTools.headerParamters = headerParamters;
	}
	
	
	
	/**
	 * <pre>
	 * 发送带参数的GET的HTTP请求
	 * </pre>
	 * 
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @return HTTP响应的字符串
	 */
	public static String doGet(String reqUrl, Map<?, ?> parameters, String recvEncoding) {
		return doGet(reqUrl, parameters, recvEncoding, "?", "&");
	}
	
	
	/**
	 * 发送带参数的GET的HTTP请求
	 * @param reqUrl    HTTP请求URL
	 * @param parameters  参数映射表
	 * @param recvEncoding  字符编码
	 * @param passParametSymbol 如www.baidu.com?a=1的"?"
	 * @param connectSymbol  如www.baidu.com?a=1&b=2的"&"
	 * @return
	 */
	public static String doGet(String reqUrl, Map<?, ?> parameters, String recvEncoding,String passParametSymbol,String connectSymbol){
		HttpURLConnection url_con = null;
		String responseContent = null;
		try {
			StringBuffer params = new StringBuffer();
			for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();) {
				Entry<?, ?> element = (Entry<?, ?>) iter.next();
				params.append(element.getKey().toString());
				params.append("=");
				params.append(URLEncoder.encode(element.getValue().toString(), HttpTools.requestEncoding));
				params.append(connectSymbol);
			}

			if (params.length() > 0) {
				params = params.deleteCharAt(params.length() - 1);
			}
//			System.out.println(reqUrl+passParametSymbol+params);
			URL url = new URL(reqUrl+passParametSymbol+params);
			url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("GET");
			// Set header
			if(headerParamters!=null){
				for(String key : headerParamters.keySet()){
					url_con.setRequestProperty(key, headerParamters.get(key).toString());
				}
			}
			System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpTools.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
			System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpTools.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
			// url_con.setConnectTimeout(5000);//（单位：毫秒）jdk
			// 1.5换成这个,连接超时
			// url_con.setReadTimeout(5000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
//			url_con.setDoOutput(true);
//			byte[] b = params.toString().getBytes();
//			url_con.getOutputStream().write(b, 0, b.length);
//			url_con.getOutputStream().flush();
//			url_con.getOutputStream().close();
			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer temp = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				temp.append(tempLine);
				temp.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = temp.toString();
			rd.close();
			in.close();
		}
		catch (IOException e) {
			logger.error("网络故障", e);
		}
		finally {
			if (url_con != null) {
				url_con.disconnect();
			}
		}
		return responseContent;
	}

	/**
	 * <pre>
	 * 发送不带参数的GET的HTTP请求
	 * </pre>
	 * 
	 * @param reqUrl HTTP请求URL
	 * @return HTTP响应的字符串
	 */
//	public static String doGet(String reqUrl, String recvEncoding) {
//		HttpURLConnection url_con = null;
//		String responseContent = null;
//		try {
//			StringBuffer params = new StringBuffer();
//			String queryUrl = reqUrl;
//			int paramIndex = reqUrl.indexOf("?");
//
//			if (paramIndex > 0) {
//				queryUrl = reqUrl.substring(0, paramIndex);
//				String parameters = reqUrl.substring(paramIndex + 1, reqUrl.length());
//				String[] paramArray = parameters.split("&");
//				for (int i = 0; i < paramArray.length; i++) {
//					String string = paramArray[i];
//					int index = string.indexOf("=");
//					if (index > 0) {
//						String parameter = string.substring(0, index);
//						String value = string.substring(index + 1, string.length());
//						params.append(parameter);
//						params.append("=");
//						params.append(URLEncoder.encode(value, HttpTools.requestEncoding));
//						params.append("&");
//					}
//				}
//
//				params = params.deleteCharAt(params.length() - 1);
//			}
//
//			URL url = new URL(queryUrl);
//			url_con = (HttpURLConnection) url.openConnection();
//			url_con.setRequestMethod("GET");
//			System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpTools.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
//			System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpTools.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
//			// url_con.setConnectTimeout(5000);//（单位：毫秒）jdk
//			// 1.5换成这个,连接超时
//			// url_con.setReadTimeout(5000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
//			url_con.setDoOutput(true);
//			byte[] b = params.toString().getBytes();
//			url_con.getOutputStream().write(b, 0, b.length);
//			url_con.getOutputStream().flush();
//			url_con.getOutputStream().close();
//			InputStream in = url_con.getInputStream();
//			BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
//			String tempLine = rd.readLine();
//			StringBuffer temp = new StringBuffer();
//			String crlf = System.getProperty("line.separator");
//			while (tempLine != null) {
//				temp.append(tempLine);
//				temp.append(crlf);
//				tempLine = rd.readLine();
//			}
//			responseContent = temp.toString();
//			rd.close();
//			in.close();
//		}
//		catch (IOException e) {
//			logger.error("网络故障", e);
//		}
//		finally {
//			if (url_con != null) {
//				url_con.disconnect();
//			}
//		}
//
//		return responseContent;
//	}
	public static String doPost(String addurl,List<Map<String,String>> infoList) {
		URL url;
		String returnValue="";
		try
		{
		    //创建连接
		    url = new URL(addurl);
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    conn.setDoOutput(true);
		    conn.setDoInput(true);
		    conn.setUseCaches(false);
		    conn.setRequestMethod("POST");
		    conn.connect();
		    //json参数
		    //DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		    Map<String ,Object> map=new HashMap<String,Object>();
		    List<Map<String,String>> allInfoList= new LinkedList<Map<String,String>>();
		    //JSONObject args = new JSONObject();
		    allInfoList.addAll(infoList);
		    map.put("UserInfoList", allInfoList);
		    JSONObject args1= JSONObject.fromObject(map);
		   /* JSONObject args1 = new JSONObject();
		    args1
		    args.put("UserInfoList","1111");
		    args.put("username","1");
		    args.put("password","123");*/
		    String aa=args1.toString();
		    String a="{\"UserInfoList\":[{\"username\":\"yzher\",\"sourceid\":\"315407\",\"password\":\"27CD3E53E9B1D43C\",\"key\":\"9ebada02676c4ccbbbdaeae27362896b\"}]}";
		    System.out.println(aa);
		     writer.write(a);
		  /*  out.writeBytes(a);
		    out.flush();
		    out.close();*/
		    //获取响应
		    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
		    String lines;
		    StringBuffer sb = new StringBuffer();
		    while((lines = reader.readLine()) != null){
		        lines = new String(lines.getBytes(), "utf-8");
		        sb.append(lines);
		    }
		    reader.close();
		    System.out.println(sb);
		    returnValue=sb.toString();
		    // 关闭连接
			conn.disconnect();
		}
		catch
		(Exception e) {
			returnValue="调用失败";
		    e.printStackTrace();

		}
		return returnValue;
	}
	
	
	
	public static String post(String url,List<Map<String,String>> infoList){  
		HttpClient client = new DefaultHttpClient();
		String returnValue = "";
		HttpPost post = new HttpPost(url);
		JSONObject response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> allInfoList = new LinkedList<Map<String, String>>();
		// JSONObject args = new JSONObject();
		allInfoList.addAll(infoList);
		map.put("UserInfoList", allInfoList);
		JSONObject args1 = JSONObject.fromObject(map);
		try {
			StringEntity s = new StringEntity(args1.toString());
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");
			post.setEntity(s);
			 Calendar cal = Calendar.getInstance();
			  int year = cal.get(Calendar.DAY_OF_YEAR);//获取年份
		         int day=cal.get(Calendar.DATE);//获取日
		        // int day1=cal.get(Calendar.DAY_OF_YEAR);
		         int hour=cal.get(Calendar.HOUR_OF_DAY);//小时
	         String value = "asdf1.23" + String.valueOf(hour) +String.valueOf(day) +String.valueOf(year);
	         String md5value= MD5.Md5(value);
	         post.setHeader("key", md5value.toUpperCase());
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
				HttpEntity entity = res.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(), "utf-8"));
				String lines;
				StringBuffer sb = new StringBuffer();
				while ((lines = reader.readLine()) != null) {
					lines = new String(lines.getBytes(), "utf-8");
					sb.append(lines);
				}
				reader.close();
				System.out.println(sb);
				returnValue = sb.toString();
				logger.info(returnValue);
				// response = new JSONObject(new JSONTokener(new
				// InputStreamReader(entity.getContent(),charset)));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return returnValue;
	   }  
	/**
	 * <pre>
	 * 发送带参数的POST的HTTP请求
	 * </pre>
	 * 
	 * @param reqUrl HTTP请求URL
	 * @param parameters 参数映射表
	 * @return HTTP响应的字符串
	 */
	public static String doPost(String reqUrl, Map<String, String> parameters, String recvEncoding) {
		HttpURLConnection url_con = null;
		String responseContent = null;
		try {
			StringBuffer params = new StringBuffer();
			for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();) {
				Entry<?, ?> element = (Entry<?, ?>) iter.next();
				params.append(element.getKey().toString());
				params.append("=");
				params.append(URLEncoder.encode(element.getValue().toString(), HttpTools.requestEncoding));
				params.append("&");
			}

			if (params.length() > 0) {
				params = params.deleteCharAt(params.length() - 1);
			}

			URL url = new URL(reqUrl);
			url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("POST");
			// Set header
			if(headerParamters!=null){
				for(String key : headerParamters.keySet()){
					url_con.setRequestProperty(key, headerParamters.get(key).toString());
				}
			}
			System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpTools.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
			System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpTools.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
			// url_con.setConnectTimeout(5000);//（单位：毫秒）jdk
			// 1.5换成这个,连接超时
			// url_con.setReadTimeout(5000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
			url_con.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			url_con.getOutputStream().write(b, 0, b.length);
			url_con.getOutputStream().flush();
			url_con.getOutputStream().close();

			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
			in.close();
		}
		catch (IOException e) {
			logger.error("网络故障", e);
		}
		finally {
			if (url_con != null) {
				url_con.disconnect();
			}
		}
		return responseContent;
	}
	public static void main(String[] args) {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("username", "manage");
//		map.put("password", "123123");
//		String temp = HttpTools.doGet("http://localhost:8080/api/user/login.json", map, "utf8");
//		System.out.println(temp);
	}
}