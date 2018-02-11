package cn.com.open.openpaas.payservice.app.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.alipay.PaySwitch;
import cn.com.open.openpaas.payservice.app.channel.alipay.PaymentType;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.channel.yeepay.HmacUtils;
import cn.com.open.openpaas.payservice.app.channel.yeepay.paymobile.utils.PaymobileUtils;
import cn.com.open.openpaas.payservice.app.channel.zxpt.http.HttpClientUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.rsa.RSACoderUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.sign.MD5;
import cn.com.open.openpaas.payservice.app.channel.zxpt.xml.XOUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.BqsFraudlistRequest;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.DateUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.Request;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.RequestHead;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.ThirdScoreRequest;
import cn.com.open.openpaas.payservice.app.kafka.KafkaProducer;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.thread.PaySendSmsThread;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.SendPostMethod;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;
import cn.com.open.openpaas.payservice.app.zxpt.model.PayZxptInfo;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import net.sf.json.JSONObject;

public class BaseControllerUtil {

	/**
	 * 
	 * 检验参数是否为空
	 * 
	 * @param params
	 * @return
	 */
	public boolean paraMandatoryCheck(List<String> params) {
		for (String param : params) {
			if (nullEmptyBlankJudge(param)) {
				return false;
			}
		}
		return true;
	}

	public static Boolean analysisOesValue(String obj) {
		if (obj.indexOf("SUCCESS") != -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检验字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean nullEmptyBlankJudge(String str) {
		return null == str || str.isEmpty() || "".equals(str.trim());
	}

	/**
	 * 
	 * 验证密码为 6～20位,字母、数字或者英文符号，最短6位，区分大小写
	 * 
	 * @param value
	 * @return
	 */
	public int judgeInputNotNo(String value) {
		int returnValue = 0;
		if (value.length() > 20 || value.length() < 6) {
			returnValue = 1;
			return returnValue;
		} else {
			// Pattern p = Pattern.compile("[a-zA-Z][a-zA-Z0-9]{5,20}");
			Pattern p = Pattern.compile("[0-9A-Za-z_]*");
			// Pattern p = Pattern.compile("^[a-zA-Z]/w{5,17}$");
			Matcher m = p.matcher(value);
			boolean chinaKey = m.matches();
			if (chinaKey) {
				returnValue = 0;
			} else {
				returnValue = 1;
				return returnValue;
			}
		}
		return returnValue;
	}

	/**
	 * 
	 * 验证密码为 6～20位
	 * 
	 * @param value
	 * @return
	 */
	public int judgePwdNo(String value) {
		int returnValue = 0;
		if (value.length() > 20 || value.length() < 6) {
			returnValue = 1;
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 验证密码不能为纯数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 验证用户名必须为英文、数字、下划线的组合
	 * 
	 * @param value
	 * @return
	 */
	public int judgeInput(String value) {
		int returnValue = 0;
		if (value.length() > 20 || value.length() < 5) {
			returnValue = 1;
			return returnValue;
		} else {
			Pattern p = Pattern.compile("[0-9A-Za-z_]*");
			// Pattern p = Pattern.compile("^[a-zA-Z]/w{5,17}$");
			Matcher m = p.matcher(value);
			boolean chinaKey = m.matches();
			if (chinaKey) {
				returnValue = 0;
			} else {
				returnValue = 2;
				return returnValue;
			}
			Pattern pattern1 = Pattern.compile("[0-9]*");
			Matcher isNum = pattern1.matcher(value);
			if (!isNum.matches()) {
				returnValue = 0;
			} else {
				returnValue = 3;
				return returnValue;
			}
		}
		return returnValue;

	}

	/**
	 * 返回成功json
	 * 
	 * @param response
	 * @param obj
	 *            数据集
	 */
	public void writeSuccessJson(HttpServletResponse response, Map map) {
		WebUtils.writeJson(response, JSONObject.fromObject(map));
	}

	/**
	 * 返回失败json
	 * 
	 * @param response
	 * @param error_code
	 *            错误码
	 */
	public void writeErrorJson(HttpServletResponse response, Map map) {
		WebUtils.writeJson(response, JSONObject.fromObject(map));
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				// System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 判断字符串是否存在于字符串数组中
	 * 
	 * @param stringArray
	 * @param source
	 * @return
	 */
	public static boolean contains(String[] stringArray, String source) {
		// 转换为list
		List<String> tempList = Arrays.asList(stringArray);
		// 利用list的包含方法,进行判断
		if (tempList.contains(source)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据错误码以及错误讯息向前端返回信息
	 * 
	 * @param errorNum
	 * @param response
	 * @param errMsg
	 */
	public void paraMandaChkAndReturn(int errorNum, HttpServletResponse response, String errMsg) {
		Map<String, Object> map = paraMandaChkAndReturnMap(errorNum, response, errMsg);
		writeErrorJson(response, map);
	}

	public Map<String, Object> paraMandaChkAndReturnMap(int errorNum, HttpServletResponse response, String errMsg) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.clear();
		map.put("state", "error");
		map.put("errorCode", errorNum);
		map.put("errMsg", errMsg);
		return map;
	}

	/**
	 * 向前端返回信息
	 * 
	 * @param errorNum
	 * @param response
	 * @param errMsg
	 */
	public void writeMsgToClient(String state, int stateNum, HttpServletResponse response, String searchMsg) {
		Map<String, Object> map = writeMsgToClientMap(state, stateNum, response, searchMsg);
		writeErrorJson(response, map);
	}

	public Map<String, Object> writeMsgToClientMap(String state, int stateNum, HttpServletResponse response,
			String searchMsg) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.clear();
		map.put("state", state);
		map.put("stateCode", stateNum);
		map.put("searchMsg", searchMsg);
		return map;
	}

	/**
	 * 
	 * 验证密码强度
	 * 
	 * @param value
	 * @return
	 */
	public int verifyPassWordStrength(String value) {
		int returnValue = 0;
		if (verifyPassWordString(value) == 0) {
			returnValue = 1;
			return returnValue;
		}
		if (verifyPassWordSAI(value) == 0) {
			returnValue = 2;
			return returnValue;
		}
		if (verifyPassWordSAT(value) == 0) {
			returnValue = 3;
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 
	 * 验证密码是否为纯字母
	 * 
	 * @param value
	 * @return
	 */
	public int verifyPassWordString(String value) {
		int returnValue = 0;

		Pattern p = Pattern.compile("[a-zA-Z]+");
		Matcher m = p.matcher(value);
		boolean chinaKey = m.matches();
		if (chinaKey) {
			returnValue = 0;
		} else {
			returnValue = 1;
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 
	 * 验证密码是否为数字和字母组合
	 * 
	 * @param value
	 * @return ^\w+$
	 */
	public int verifyPassWordSAI(String value) {
		int returnValue = 0;

		Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher m = p.matcher(value);
		boolean chinaKey = m.matches();
		if (chinaKey) {
			returnValue = 0;
		} else {
			returnValue = 1;
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 
	 * 验证密码是否为数字、字符、特殊字符组成切密码大于6位
	 * 
	 * @param value
	 * @return^\w+$
	 */
	public int verifyPassWordSAT(String value) {
		int returnValue = 0;

		Pattern p = Pattern.compile("(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,}");
		Matcher m = p.matcher(value);
		boolean chinaKey = m.matches();
		if (chinaKey) {
			returnValue = 0;
		} else {
			returnValue = 1;
			return returnValue;
		}
		return returnValue;
	}

	public static Safelevel GetPwdSecurityLevel(String pPasswordStr) {
		Safelevel safelevel = Safelevel.VERY_WEAK;
		if (pPasswordStr == null) {
			return safelevel;
		}
		int grade = 0;
		int index = 0;
		char[] pPsdChars = pPasswordStr.toCharArray();

		int numIndex = 0;
		int sLetterIndex = 0;
		int lLetterIndex = 0;
		int symbolIndex = 0;

		for (char pPsdChar : pPsdChars) {
			int ascll = pPsdChar;
			/*
			 * 数字 48-57 <a href=
			 * "https://www.baidu.com/s?wd=A-Z&tn=44039180_cpr&fenlei=mv6quAkxTZn0IZRqIHckPjm4nH00T1Y4njwBuHn3nHuWrARYuHfk0AP8IA3qPjfsn1bkrjKxmLKz0ZNzUjdCIZwsrBtEXh9GuA7EQhF9pywdQhPEUiqkIyN1IA-EUBtdP16LP163PWn"
			 * target="_blank" class="baidu-highlight">A-Z</a> 65 - 90 <a href=
			 * "https://www.baidu.com/s?wd=a-z&tn=44039180_cpr&fenlei=mv6quAkxTZn0IZRqIHckPjm4nH00T1Y4njwBuHn3nHuWrARYuHfk0AP8IA3qPjfsn1bkrjKxmLKz0ZNzUjdCIZwsrBtEXh9GuA7EQhF9pywdQhPEUiqkIyN1IA-EUBtdP16LP163PWn"
			 * target="_blank" class="baidu-highlight">a-z</a> 97 - 122
			 * !"#$%&'()*+,-./ (ASCII码：33~47) :;<=>?@ (ASCII码：58~64) [\]^_`
			 * (ASCII码：91~96) {|}~ (ASCII码：123~126)
			 */
			if (ascll >= 48 && ascll <= 57) {
				numIndex++;
			} else if (ascll >= 65 && ascll <= 90) {
				lLetterIndex++;
			} else if (ascll >= 97 && ascll <= 122) {
				sLetterIndex++;
			} else if ((ascll >= 33 && ascll <= 47) || (ascll >= 58 && ascll <= 64) || (ascll >= 91 && ascll <= 96)
					|| (ascll >= 123 && ascll <= 126)) {
				symbolIndex++;
			}
		}
		if (isContinuous(pPasswordStr)) {
			index = 25;
		} else {
			/*
			 * 一、密码长度: 5 分: 小于等于 4 个字符 10 分: 5 到 7 字符 25 分: 大于等于 8 个字符
			 */
			if (pPsdChars.length <= 8) {
				index = 5;
			} else if (pPsdChars.length <= 16) {
				index = 20;
			} else {
				index = 30;
			}
			grade += index;
			/**
			 * 
			 */

			/*
			 * 二、字母: 0 分: 没有字母 10 分: 全都是小（大）写字母 20 分: 大小写混合字母
			 */
			if (lLetterIndex == 0 && sLetterIndex == 0) {
				index = 0;
			} else if (lLetterIndex != 0 && sLetterIndex != 0) {
				index = 20;
			} else {
				index = 10;
			}
			grade += index;

			/*
			 * 三、数字: 0 分: 没有数字 10 分: 1 个数字 20 分: 大于 1 个数字
			 */
			if (numIndex == 0) {
				index = 0;
			} else {
				index = 20;
			}
			grade += index;

			/*
			 * 四、符号: 0 分: 没有符号 20 分: 1 个符号 25 分: 大于 1 个符号
			 */
			if (symbolIndex == 0) {
				index = 0;
			} else if (symbolIndex == 1) {
				index = 20;
			} else {
				index = 25;
			}
			grade += index;
			/*
			 * 五、奖励: 2 分: 字母和数字 3 分: 字母、数字和符号 5 分: 大小写字母、数字和符号
			 */
			if (sLetterIndex != 0 && lLetterIndex != 0 && numIndex != 0 && symbolIndex != 0) {
				index = 5;
			} else if ((sLetterIndex != 0 || lLetterIndex != 0) && numIndex != 0 && symbolIndex != 0) {
				index = 3;
			} else if ((sLetterIndex != 0 || lLetterIndex != 0) && numIndex != 0) {
				index = 2;
			} else {
				index = 0;
			}
			grade += index;
		}

		/*
		 * 最后的评分标准: >= 90: 非常安全 >= 80: 安全（Secure） >= 70: 非常强 >= 60: 强（Strong） >=
		 * 50: 一般（Average） >= 25: 弱（Weak） >= 0: 非常弱
		 */
		if (grade >= 90) {
			safelevel = Safelevel.VERY_SECURE;
		} else if (grade >= 70) {
			safelevel = Safelevel.VERY_STRONG;
		} else if (grade >= 50) {
			safelevel = Safelevel.STRONG;
		} else if (grade >= 10) {
			safelevel = Safelevel.WEAK;
		}
		return safelevel;
	}

	public enum Safelevel {
		VERY_WEAK, /* 非常弱 */
		WEAK, /* 弱 */
		AVERAGE, /* 一般 */
		STRONG, /* 强 */
		VERY_STRONG, /* 非常强 */
		SECURE, /* 安全 */
		VERY_SECURE /* 非常安全 */
	}

	public static Boolean isContinuous(String str) {
		Boolean allEquals = true;
		String element = str.substring(0, 1);
		for (int i = 0; i < str.length(); i++) {
			if (str.substring(i, i + 1) != element) {
				allEquals = false;
				break;
			}
			element = str.substring(i, i + 1);
		}
		return allEquals;
	}

	/**
	 * IP地址黑白名单
	 * 
	 * @param request
	 * @return
	 */
	public String getRemortIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}

	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            目的地址
	 * @param parameters
	 *            请求参数，Map类型。
	 * @return 远程响应结果
	 */
	public static String sendPost(String url, SortedMap<Object, Object> params2) {
		String result = "";// 返回的结果
		BufferedReader in = null;// 读取响应输入流
		PrintWriter out = null;
		StringBuffer sb = new StringBuffer();// 处理请求参数
		String params = "";// 编码之后的参数
		SortedMap<Object, Object> parameters = params2;
		try {
			// 编码请求参数
			if (parameters.size() == 1) {
				for (Object name : parameters.keySet()) {
					sb.append(name).append("=")
							.append(java.net.URLEncoder.encode((String) parameters.get(name), "UTF-8"));
				}
				params = sb.toString();
			} else {
				for (Object name : parameters.keySet()) {
					sb.append(name).append("=").append(parameters.get(name)).append("&");
				}
				String temp_params = sb.toString();
				params = temp_params.substring(0, temp_params.length() - 1);
			}
			// 创建URL对象
			java.net.URL connURL = new java.net.URL(url);
			// 打开URL连接
			java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL.openConnection();
			// 设置通用属性
			httpConn.setRequestProperty("Accept", "*/*");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
			// 设置POST方式
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			// 获取HttpURLConnection对象对应的输出流
			out = new PrintWriter(httpConn.getOutputStream());
			// 发送请求参数
			// out.write(params);
			out.write(params);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应，设置编码方式
			in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			String line;
			// 读取返回的内容
			while ((line = in.readLine()) != null) {
				result += line;

			}
			// System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            目的地址
	 * @param parameters
	 *            请求参数，Map类型。
	 * @return 远程响应结果
	 */
	public static String sendPost1(String url, Map<String, String> params2) {
		String result = "";// 返回的结果
		BufferedReader in = null;// 读取响应输入流
		PrintWriter out = null;
		StringBuffer sb = new StringBuffer();// 处理请求参数
		String params = "";// 编码之后的参数
		Map<String, String> parameters = params2;
		try {
			// 编码请求参数
			if (parameters.size() == 1) {
				for (Object name : parameters.keySet()) {
					sb.append(name).append("=")
							.append(java.net.URLEncoder.encode((String) parameters.get(name), "UTF-8"));
				}
				params = sb.toString();
			} else {
				for (Object name : parameters.keySet()) {
					sb.append(name).append("=").append(parameters.get(name)).append("&");
				}
				String temp_params = sb.toString();
				params = temp_params.substring(0, temp_params.length() - 1);
			}
			// 创建URL对象
			java.net.URL connURL = new java.net.URL(url);
			// 打开URL连接
			java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL.openConnection();
			// 设置通用属性
			httpConn.setRequestProperty("Accept", "*/*");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
			// 设置POST方式
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			// 获取HttpURLConnection对象对应的输出流
			out = new PrintWriter(httpConn.getOutputStream());
			// 发送请求参数
			// out.write(params);
			out.write(params);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应，设置编码方式
			in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			String line;
			// 读取返回的内容
			while ((line = in.readLine()) != null) {
				result += line;

			}
			// System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 生成加密串
	 * 
	 * @param characterEncoding
	 * @param parameters
	 * @return
	 */
	public static String createSign(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"null".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		String temp_params = sb.toString();
		return sb.toString().substring(0, temp_params.length() - 1);
	}

	public static Map<String, String> getPartner(String other) {
		if (other == null && "".equals(other)) {
			return null;
		} else {
			String others[] = other.split("#");
			Map<String, String> sParaTemp = new HashMap<String, String>();
			for (int i = 0; i < others.length; i++) {
				String values[] = others[i].split(":");
				sParaTemp.put(values[0], values[1]);
			}

			return sParaTemp;
		}
	}

	public static Boolean analysisValue(JSONObject obj) {
		String state = obj.getString("status");
		if (!state.equals("ok")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            目的地址
	 * @param parameters
	 *            请求参数，Map类型。
	 * @return 远程响应结果
	 */
	public static String sendOrderPost(String url, SortedMap<Object, Object> sParaTemp) {
		String result = "";// 返回的结果
		BufferedReader in = null;// 读取响应输入流
		PrintWriter out = null;
		StringBuffer sb = new StringBuffer();// 处理请求参数
		String params = "";
		try {
			Set es = sParaTemp.entrySet();// 所有参与传参的参数按照accsii排序（升序）
			Iterator it = es.iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String k = (String) entry.getKey();
				Object v = entry.getValue();
				if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
					String value = URLEncoder.encode((String) v, "UTF-8");
					sb.append(k + "=" + value + "&");
				}
			}
			String temp_params = sb.toString();
			params = temp_params.substring(0, temp_params.length() - 1);
			// 创建URL对象
			java.net.URL connURL = new java.net.URL(url);
			// 打开URL连接
			java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL.openConnection();
			// 设置通用属性
			httpConn.setRequestProperty("Accept", "*/*");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
			// 设置POST方式
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			// 获取HttpURLConnection对象对应的输出流
			out = new PrintWriter(httpConn.getOutputStream());
			// 发送请求参数
			// out.write(params);
			out.write(params);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应，设置编码方式
			in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			String line;
			// 读取返回的内容
			while ((line = in.readLine()) != null) {
				result += line;

			}
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 获取请求参数中所有的信息
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				// System.out.println("ServletUtil类247行 temp数据的键=="+en+"
				// 值==="+value);
				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}

	public static String formatString(String text) {
		return text == null ? "" : text.trim();
	}

	public void sendSms(MerchantOrderInfo merchantOrderInfo, MerchantInfo merchantInfo, KafkaProducer kafkaProducer) {
		if (merchantInfo.getSmsSwitch() == 1) {
			SortedMap<Object, Object> sParaTemp = new TreeMap<Object, Object>();
			sParaTemp.put("phone", merchantOrderInfo.getPhone());
			sParaTemp.put("content", "支付成功");
			Thread thread = new Thread(new PaySendSmsThread(sParaTemp, kafkaProducer));
			thread.run();
		}
	}

	public String getAreaCode(String areaCode) {
		String newAreaCode = "";
		if (!nullEmptyBlankJudge(areaCode)) {
			if (PaymentType.CMB.getValue().equals(areaCode)) {
				newAreaCode = "CMB";
			} else if (PaymentType.ICBC.getValue().equals(areaCode)) {
				newAreaCode = "ICBC";
			} else if (PaymentType.CCB.getValue().equals(areaCode)) {
				newAreaCode = "CCB";
			} else if (PaymentType.ABC.getValue().equals(areaCode)) {
				newAreaCode = "ABC";
			} else if (PaymentType.BOC.getValue().equals(areaCode)) {
				newAreaCode = "BOC";
			} else if (PaymentType.BCOM.getValue().equals(areaCode)) {
				newAreaCode = "BCOM";
			} else if (PaymentType.PSBC.getValue().equals(areaCode)) {
				newAreaCode = "PSBC";
			} else if (PaymentType.CGB.getValue().equals(areaCode)) {
				newAreaCode = "CGB";
			} else if (PaymentType.SPDB.getValue().equals(areaCode)) {
				newAreaCode = "SPDB";
			} else if (PaymentType.CEB.getValue().equals(areaCode)) {
				newAreaCode = "CEB";
			} else if (PaymentType.PAB.getValue().equals(areaCode)) {
				newAreaCode = "PAB";
			}
		} else {
			newAreaCode = "";
		}
		return newAreaCode;
	}

	/**
	 * 易宝支付
	 * 
	 * @param areaCode
	 * @return
	 */
	public String getYeePayCode(String paymentType) {
		String returnValue = "";
		if (!nullEmptyBlankJudge(paymentType)) {
			if (PaymentType.CMB.getValue().equals(paymentType)) {
				returnValue = "CMBCHINA-NET-B2C";
			} else if (PaymentType.BOC.getValue().equals(paymentType)) {
				returnValue = "BOC-NET-B2C";
			} else if (PaymentType.ICBC.getValue().equals(paymentType)) {
				returnValue = "ICBC-NET-B2C";
			} else if (PaymentType.CCB.getValue().equals(paymentType)) {
				returnValue = "CCB-NET-B2C";
			} else if (PaymentType.ABC.getValue().equals(paymentType)) {
				returnValue = "ABC-NET-B2C";
			} else if (PaymentType.BCOM.getValue().equals(paymentType)) {
				returnValue = "BOCO-NET-B2C";
			} else if (PaymentType.PSBC.getValue().equals(paymentType)) {
				returnValue = "POST-NET-B2C";
			} else if (PaymentType.CGB.getValue().equals(paymentType)) {
				returnValue = "GDB-NET-B2C";
			} else if (PaymentType.SPDB.getValue().equals(paymentType)) {
				returnValue = "SPDB-NET-B2C";
			} else if (PaymentType.CEB.getValue().equals(paymentType)) {
				returnValue = "CEB-NET-B2C";
			} else if (PaymentType.PAB.getValue().equals(paymentType)) {
				returnValue = "PINGANBANK-NET";
			}
		}
		return returnValue;
	}

	public Integer getPaymentId(String areaCode) {
		int returnValue = 0;
		if (PaymentType.CMB.getValue().equals(areaCode)) {
			returnValue = PaymentType.CMB.getType();
		} else if (PaymentType.ICBC.getValue().equals(areaCode)) {
			returnValue = PaymentType.ICBC.getType();
		} else if (PaymentType.CCB.getValue().equals(areaCode)) {
			returnValue = PaymentType.CCB.getType();
		} else if (PaymentType.ABC.getValue().equals(areaCode)) {
			returnValue = PaymentType.ABC.getType();
		} else if (PaymentType.BOC.getValue().equals(areaCode)) {
			returnValue = PaymentType.BOC.getType();
		} else if (PaymentType.BCOM.getValue().equals(areaCode)) {
			returnValue = PaymentType.BCOM.getType();
		} else if (PaymentType.PSBC.getValue().equals(areaCode)) {
			returnValue = PaymentType.PSBC.getType();
		} else if (PaymentType.CGB.getValue().equals(areaCode)) {
			returnValue = PaymentType.CGB.getType();
		} else if (PaymentType.SPDB.getValue().equals(areaCode)) {
			returnValue = PaymentType.SPDB.getType();
		} else if (PaymentType.CEB.getValue().equals(areaCode)) {
			returnValue = PaymentType.CEB.getType();
		} else if (PaymentType.PAB.getValue().equals(areaCode)) {
			returnValue = PaymentType.PAB.getType();
		} else if (PaymentType.ALIPAY.getValue().equals(areaCode)) {
			returnValue = PaymentType.ALIPAY.getType();
		} else if (PaymentType.UPOP.getValue().equals(areaCode)) {
			returnValue = PaymentType.UPOP.getType();
		}
		return returnValue;

	}

	public Boolean getErrorType(String paymentType) {
		Boolean errorType = false;
		if (PaymentType.WECHAT_WAP.getValue().equals(paymentType)) {
			errorType = true;
		}
		if (PaymentType.WEIXIN.getValue().equals(paymentType)) {
			errorType = true;
		}
		if (PaymentType.ALIFAF.getValue().equals(paymentType)) {
			errorType = true;
		}
		if (PaymentType.PAYMAX_WECHAT_CSB.getValue().equals(paymentType)) {
			errorType = true;
		}
		if (PaymentType.EHK_WEIXIN_PAY.getValue().equals(paymentType)) {
			errorType = true;
		}
		return errorType;
	}

	/**
	 * 判断直连银行的选择支付方式是否为直连银行
	 * 
	 * @param paymentType
	 * @return
	 */
	public Boolean ifTruePayMentType(String paymentType) {
		boolean returnValue = false;
		if (paymentType != null && !String.valueOf(PaymentType.WEIXIN.getValue()).equals(paymentType)) {
			returnValue = true;
		} else if (!String.valueOf(PaymentType.UPOP.getValue()).equals(paymentType)) {
			returnValue = true;
		} else if (!String.valueOf(PaymentType.ALIPAY.getValue()).equals(paymentType)) {
			returnValue = true;
		} else if (!String.valueOf(PaymentType.PAYMAX.getValue()).equals(paymentType)) {
			returnValue = true;
		} else if (!String.valueOf(PaymentType.PAYMAX_H5.getValue()).equals(paymentType)) {
			returnValue = true;
		} else if (!String.valueOf(PaymentType.WECHAT_WAP.getValue()).equals(paymentType)) {
			returnValue = true;
		} else {
			returnValue = false;
		}
		return returnValue;
	}

	public Boolean validatePayType(String paymentChannel, String paymentType) {
		Boolean returnValue = false;
		if (nullEmptyBlankJudge(paymentChannel) && nullEmptyBlankJudge(paymentType)) {
			returnValue = true;
		}
		if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.ALI.getValue()))) {
			if (paymentType != null && (PaymentType.ALIPAY.getValue()).equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.WEIXIN.getValue()))) {
			if (paymentType != null && String.valueOf(PaymentType.WEIXIN.getValue()).equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		}

		else if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.EBANK.getValue()))) {
			if (ifTruePayMentType(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.WECHAT_WAP.getValue()))) {
			if (paymentType != null && PaymentType.WECHAT_WAP.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null
				&& paymentChannel.equals(String.valueOf(Channel.PAYMAX_WECHAT_CSB.getValue()))) {
			if (paymentType != null && PaymentType.PAYMAX_WECHAT_CSB.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.PAYMAX_H5.getValue()))) {
			if (paymentType != null && PaymentType.PAYMAX_H5.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null
				&& paymentChannel.equals(String.valueOf(Channel.WEIXIN_WECHAT_WAP.getValue()))) {
			if (paymentType != null && PaymentType.WECHAT_WAP.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.UPOP.getValue()))) {
			if (paymentType != null && PaymentType.UPOP.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.EHK_WEIXIN_PAY.getValue()))) {
			if (paymentType != null && PaymentType.EHK_WEIXIN_PAY.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.EHK_ALI_PAY.getValue()))) {
			if (paymentType != null && PaymentType.EHK_ALI_PAY.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.EHK_BANK.getValue()))) {
			if ((paymentType != null && PaymentType.EHK_BANK.getValue().equals(paymentType))) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.PAYMAX.getValue()))) {
			if (paymentType != null && PaymentType.PAYMAX.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.YEEPAY_EB.getValue()))) {
			if (paymentType != null && PaymentType.YEEPAY_GW.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.ALIFAF.getValue()))) {
			if (paymentType != null && PaymentType.ALIFAF.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.YEEPAY_WEIXIN.getValue()))) {
			if (paymentType != null && PaymentType.YEEPAY_WEIXIN.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.YEEPAY_ALI.getValue()))) {
			if (paymentType != null && PaymentType.YEEPAY_ALI.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null && paymentChannel.equals(String.valueOf(Channel.YEEPAY_ALL.getValue()))) {
			if (paymentType != null && PaymentType.YEEPAY_ALL.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} else if (paymentChannel != null
				&& paymentChannel.equals(String.valueOf(Channel.EHK_INSTALLMENT_LOAN.getValue()))) {
			if (paymentType != null && PaymentType.EHK_INSTALLMENT_LOAN.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		}else if (paymentChannel != null
				&& paymentChannel.equals(String.valueOf(Channel.YEEPAY_POS.getValue()))) {
			if (paymentType != null && PaymentType.YEEPAY_POS.getValue().equals(paymentType)) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		}
		return returnValue;
	}

	/**
	 * 返回易汇金银行编码
	 * 
	 * @param paymentType
	 * @return
	 */
	public String getEhkbankCode(String paymentType) {
		String returnValue = "";
		if (!nullEmptyBlankJudge(paymentType)) {
			if (PaymentType.PSBC.getValue().equals(paymentType)) {
				returnValue = "BANK_CARD-B2C-POST-P2P";
			} else if (PaymentType.CMBC.getValue().equals(paymentType)) {
				returnValue = "BANK_CARD-B2C-CMBC-P2P";
			} else if (PaymentType.BOB.getValue().equals(paymentType)) {
				returnValue = "BANK_CARD-B2C-BCCB-P2P";
			} else if (PaymentType.CMB.getValue().equals(paymentType)) {
				returnValue = "BANK_CARD-B2C-CMBCHINA-P2P";
			} else if (PaymentType.SPDB.getValue().equals(paymentType)) {
				returnValue = "BANK_CARD-B2C-SPDB-P2P";
			} else if (PaymentType.CIB.getValue().equals(paymentType)) {
				returnValue = "BANK_CARD-B2C-CIB-P2P";
			} else if (PaymentType.ABC.getValue().equals(paymentType)) {
				returnValue = "BANK_CARD-B2C-ABC-P2P";
			} else if (PaymentType.CGB.getValue().equals(paymentType)) {
				returnValue = "BANK_CARD-B2C-GDB-P2P";
			} else if (PaymentType.ICBC.getValue().equals(paymentType)) {
				returnValue = "BANK_CARD-B2C-ICBC-P2P";
			} else if (PaymentType.BOC.getValue().equals(paymentType)) {
				returnValue = "BANK_CARD-B2C-BOC-P2P";
			} else if (PaymentType.BCOM.getValue().equals(paymentType)) {
				returnValue = "BANK_CARD-B2C-BOCO-P2P";
			} else if (PaymentType.CCB.getValue().equals(paymentType)) {
				returnValue = "BANK_CARD-B2C-CCB-P2P";
			} else if (PaymentType.PAB.getValue().equals(paymentType)) {
				returnValue = "BANK_CARD-B2C-PINGANBANK-P2P";
			} else if (PaymentType.CEB.getValue().equals(paymentType)) {
				returnValue = "BANK_CARD-B2C-CEB-P2P";
			} else {
				returnValue = paymentType;
			}
		} else {
			returnValue = "";
		}
		return returnValue;
	}

	/**
	 * 返回拉卡拉银行代码
	 * 
	 * @param paymentType
	 * @return
	 */
	public String getPayMaxbank(String paymentType) {
		String returnValue = "";
		if (!nullEmptyBlankJudge(paymentType)) {
			if (PaymentType.CGB.getValue().equals(paymentType)) {
				returnValue = "GDB";
			} else if (PaymentType.PAB.getValue().equals(paymentType)) {
				returnValue = "PABC";
			} else {
				returnValue = paymentType;
			}
		} else {
			returnValue = "";
		}
		return returnValue;
	}

	/**
	 * 返回银行代码
	 * 
	 * @param paymentType
	 * @return
	 */
	public String getDefaultbank(String paymentType) {
		String returnValue = "";
		if (!nullEmptyBlankJudge(paymentType)) {
			if (PaymentType.CMB.getValue().equals(paymentType)) {
				returnValue = "CMB";
			} else if (PaymentType.BOC.getValue().equals(paymentType)) {
				returnValue = "BOCB2C";
			} else if (PaymentType.ICBC.getValue().equals(paymentType)) {
				returnValue = "ICBCB2C";
			} else if (PaymentType.CCB.getValue().equals(paymentType)) {
				returnValue = "CCB";
			} else if (PaymentType.ABC.getValue().equals(paymentType)) {
				returnValue = "ABC";
			} else if (PaymentType.BCOM.getValue().equals(paymentType)) {
				returnValue = "COMM-DEBIT";
			} else if (PaymentType.PSBC.getValue().equals(paymentType)) {
				returnValue = "POSTGC";
			} else if (PaymentType.CGB.getValue().equals(paymentType)) {
				returnValue = "GDB";
			} else if (PaymentType.SPDB.getValue().equals(paymentType)) {
				returnValue = "SPDB";
			} else if (PaymentType.CEB.getValue().equals(paymentType)) {
				returnValue = "CEB-DEBIT";
			} else if (PaymentType.PAB.getValue().equals(paymentType)) {
				returnValue = "SPABANK";
			} else if (PaymentType.BOB.getValue().equals(paymentType)) {
				returnValue = "BJBANK";
			}
		} else {
			returnValue = "";
		}
		return returnValue;
	}

	public String getParmenter(MerchantOrderInfo merchantOrderInfo) {
		String parameters[] = new String[10];
		String openId = "";
		if (!nullEmptyBlankJudge(merchantOrderInfo.getParameter1())) {
			parameters = merchantOrderInfo.getParameter1().split(";");
		}
		for (int i = 0; i < parameters.length; i++) {
			if (!nullEmptyBlankJudge(parameters[i])) {
				String[] openIds = parameters[i].split("=");
				if (openIds[0].equals("open_id")) {
					openId = openIds[1];
					break;
				}
			}
		}
		return openId;
	}

	public Map<String, Object> getParmenters(MerchantOrderInfo merchantOrderInfo) {
		String parameters[] = new String[10];
		Map<String, Object> map = new HashMap<String, Object>();
		if (!nullEmptyBlankJudge(merchantOrderInfo.getParameter1())) {
			parameters = merchantOrderInfo.getParameter1().split(";");
		}
		for (int i = 0; i < parameters.length; i++) {
			if (!nullEmptyBlankJudge(parameters[i])) {
				String[] openIds = parameters[i].split("=");
				if (openIds != null && openIds.length >= 2) {
					map.put(openIds[0], openIds[1]);
				}
			}
		}
		return map;
	}

	public String getYeePayFrpId(String paymentType) {
		String returnValue = "";
		if (!nullEmptyBlankJudge(paymentType)) {
			if (PaymentType.CMB.getValue().equals(paymentType)) {
				returnValue = "CMBCHINA-NET-B2C";
			} else if (PaymentType.BOC.getValue().equals(paymentType)) {
				returnValue = "BOC-NET-B2C";
			} else if (PaymentType.ICBC.getValue().equals(paymentType)) {
				returnValue = "ICBC-NET-B2C";
			} else if (PaymentType.CCB.getValue().equals(paymentType)) {
				returnValue = "CCB-NET-B2C";
			} else if (PaymentType.ABC.getValue().equals(paymentType)) {
				returnValue = "ABC-NET-B2C";
			} else if (PaymentType.BCOM.getValue().equals(paymentType)) {
				returnValue = "BOCO-NET-B2C";
			} else if (PaymentType.PSBC.getValue().equals(paymentType)) {
				returnValue = "POST-NET-B2C";
			} else if (PaymentType.CGB.getValue().equals(paymentType)) {
				returnValue = "GDB-NET-B2C";
			} else if (PaymentType.SPDB.getValue().equals(paymentType)) {
				returnValue = "SPDB-NET-B2C";
			} else if (PaymentType.CEB.getValue().equals(paymentType)) {
				returnValue = "CEB-NET-B2C";
			} else if (PaymentType.PAB.getValue().equals(paymentType)) {
				returnValue = "PINGANBANK-NET";
			} else if (PaymentType.BOB.getValue().equals(paymentType)) {
				returnValue = "BCCB-NET-B2C";
			}else if (PaymentType.CMBC.getValue().equals(paymentType)) {
				returnValue = "CMBC-NET-B2C";
			}else if (PaymentType.CIB.getValue().equals(paymentType)) {
				returnValue = "CIB-NET-B2C";
			}
		}
		return returnValue;
	}
	protected int getSourceType(Map<String, String> bank_map, String payZhifubao, String payWx, String payTcl,
			String payEbank, String wechat_wap, String alifaf, String paymentType, String paymentChannel,
			MerchantOrderInfo merchantOrderInfo) {
		int sourceType=0;
		if(String.valueOf(Channel.ALI.getValue()).equals(paymentChannel)){
			sourceType=Integer.parseInt(payZhifubao);
		}
		else if(String.valueOf(Channel.WEIXIN.getValue()).equals(paymentChannel)){
			sourceType=Integer.parseInt(payWx);	
		}
		else if(String.valueOf(Channel.UPOP.getValue()).equals(paymentChannel)){
			sourceType=Integer.parseInt(payTcl);	
		}
		else if(String.valueOf(Channel.EBANK.getValue()).equals(paymentChannel)){
			payEbank=bank_map.get(paymentType);
			sourceType=Integer.parseInt(payEbank);	
		}else if(String.valueOf(Channel.WECHAT_WAP.getValue()).equals(paymentChannel)){
			sourceType=Integer.parseInt(wechat_wap);	
		}else if(String.valueOf(Channel.PAYMAX.getValue()).equals(paymentChannel)){
			sourceType=PaySwitch.PAYMAX.getValue();	
		}else if(String.valueOf(Channel.EHK_BANK.getValue()).equals(paymentChannel)){
			sourceType=PaySwitch.EHKING.getValue();	
		}else if(String.valueOf(Channel.EHK_WEIXIN_PAY.getValue()).equals(paymentChannel)){
			sourceType=PaySwitch.EHKING.getValue();	
		}else if(String.valueOf(Channel.YEEPAY_EB.getValue()).equals(paymentChannel)){
			sourceType=PaySwitch.YEEPAY.getValue();	
		}else if(String.valueOf(Channel.ALIFAF.getValue()).equals(paymentChannel)){
			sourceType=Integer.parseInt(alifaf);	
		}else if(String.valueOf(Channel.WECHAT_WAP.getValue()).equals(paymentChannel)){
			sourceType=PaySwitch.PAYMAX.getValue();	
		}else if(String.valueOf(Channel.PAYMAX_WECHAT_CSB.getValue()).equals(paymentChannel)){
			sourceType=PaySwitch.PAYMAX.getValue();	
		}else if(String.valueOf(Channel.PAYMAX_H5.getValue()).equals(paymentChannel)){
			sourceType=PaySwitch.PAYMAX.getValue();	
		}else if(String.valueOf(Channel.YEEPAY_WEIXIN.getValue()).equals(paymentChannel)){
			sourceType=PaySwitch.YEEPAY.getValue();	
		}else if(String.valueOf(Channel.YEEPAY_ALI.getValue()).equals(paymentChannel)){
			sourceType=PaySwitch.YEEPAY.getValue();	
		}else if(String.valueOf(Channel.YEEPAY_ALL.getValue()).equals(paymentChannel)){
			sourceType=PaySwitch.YEEPAY.getValue();	
		}else if(String.valueOf(Channel.EHK_ALI_PAY.getValue()).equals(paymentChannel)){
			sourceType=PaySwitch.EHKING.getValue();	
		}else if(String.valueOf(Channel.EHK_INSTALLMENT_LOAN.getValue()).equals(paymentChannel)){
			sourceType=PaySwitch.EHKING.getValue();	
		}else if(String.valueOf(Channel.YEEPAY_POS.getValue()).equals(paymentChannel)){
			sourceType=PaySwitch.YEEPAY.getValue();	
		}
		return sourceType;
	}
	public Map<String, Object> getYeePayValue(PayserviceDev payserviceDev,DictTradeChannelService dictTradeChannelService,MerchantOrderInfo merchantOrderInfo,int channelId){
		  //易宝微信扫码
			//使用TreeMap
		 DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),channelId);
		 Map<String, Object> map=new HashMap<String,Object>();
	  	  if(dictTradeChannels!=null){
	  		String other= dictTradeChannels.getOther();
			Map<String, String> others = new HashMap<String, String>();
			others=getPartner(other);
			//解析自定义参数
			 Map<String, Object> parmenterMap=new HashMap<String,Object>();
			 parmenterMap=getParmenters(merchantOrderInfo);
			TreeMap<String, Object> treeMap	= new TreeMap<String, Object>();
			treeMap.put("orderid", 			merchantOrderInfo.getId());
			treeMap.put("productcatalog", others.get("productcatalog"));
			treeMap.put("productname", 		merchantOrderInfo.getMerchantProductName());
			treeMap.put("identityid", 		parmenterMap.get("identityid").toString());
			treeMap.put("userip", 			merchantOrderInfo.getIp());
			treeMap.put("paytool", 			 others.get("paytool"));
			treeMap.put("terminalid", 		parmenterMap.get("terminalid").toString());
			treeMap.put("transtime", 		(int)(System.currentTimeMillis()/1000));
			treeMap.put("amount", 	        (new Double(merchantOrderInfo.getOrderAmount().doubleValue()*100)).intValue());
			treeMap.put("identitytype", 	Integer.parseInt(parmenterMap.get("identitytype").toString()) );
			treeMap.put("terminaltype", 	Integer.parseInt(parmenterMap.get("terminaltype").toString()));
			treeMap.put("callbackurl", 		dictTradeChannels.getNotifyUrl());
			treeMap.put("currency", 		Integer.parseInt(others.get("currency")));
			treeMap.put("version", 		   Integer.parseInt(others.get("version")));
			treeMap.put("directpaytype",Integer.parseInt(others.get("directpaytype")));
			treeMap.put("productdesc", 		merchantOrderInfo.getMerchantProductName());
			System.out.println(treeMap);
			//第一步 生成AESkey及encryptkey
			String AESKey		= PaymobileUtils.buildAESKey();
			String encryptkey	= PaymobileUtils.buildEncyptkey(AESKey,others.get("yeepayPublicKey"));
			//String encryptkey	= PaymobileUtils.buildEncyptkey(AESKey);
			//第二步 生成data
			//第三步 获取商户编号及请求地址，并组装支付链接
			String merchantaccount	= others.get("merchantaccount");
			String url				= payserviceDev.getYeepay_pay_url();
			String data			= PaymobileUtils.buildData(treeMap, AESKey,merchantaccount,others.get("merchantPrivateKey"));
			//String data			= PaymobileUtils.buildData(treeMap, AESKey);
			TreeMap<String, String> responseMap	= PaymobileUtils.httpPost(url, merchantaccount, data, encryptkey);
			//第四步 判断请求是否成功
			if(responseMap.containsKey("error_code")) {
				map.put("status", "error");
				map.put("error_msg", responseMap.get("error_msg").toString());
				map.put("error_code", responseMap.get("error_code").toString());
				map.put("urlCode", "");
			}else{
				//第五步 请求成功，则获取data、encryptkey，并将其解密
				String data_response						= responseMap.get("data");
				String encryptkey_response					= responseMap.get("encryptkey");
				
				TreeMap<String, String> responseDataMap	= PaymobileUtils.decrypt(data_response, encryptkey_response,others.get("merchantPrivateKey"));
				//TreeMap<String, String> responseDataMap	= PaymobileUtils.decrypt(data_response, encryptkey_response);

				//第六步 sign验签
				if(!PaymobileUtils.checkSign(responseDataMap,others.get("yeepayPublicKey"))) {
			         //验签失败
					map.put("status", "error");
					map.put("error_msg", "验签失败");
					map.put("error_code", "11");
					map.put("urlCode", "");
				}else{
					//第七步 判断请求是否成功
					if(responseDataMap.containsKey("error_code")) {
						map.put("status", "error");
						map.put("error_msg", responseDataMap.get("error_msg").toString());
						map.put("error_code", responseDataMap.get("error_code").toString());
						map.put("urlCode", "");
					}else{
						//第八步 进行业务处理
				    	   map.put("status", "ok");
				    	   String urlCode=responseDataMap.get("payurl");
				    	   map.put("urlCode", urlCode);
					}
				}
			}
	  	 }else{
	  		map.put("status", "error");
			map.put("error_msg", "渠道未开通");
			map.put("error_code", "12");
			map.put("urlCode", "");
	  	 }
	    return  map;
  }
	   /**
     * 获取易宝支付地址
     * @param totalFee
     * @param merchantOrderInfo
     * @return
     */
	public String getYeePayUrl(PayserviceDev payserviceDev,DictTradeChannelService dictTradeChannelService,String totalFee,
			MerchantOrderInfo merchantOrderInfo,String paymentType) {
		DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.YEEPAY_EB.getValue());
		 String other= dictTradeChannels.getOther();
		 Map<String, String> others = new HashMap<String, String>();
		 others=getPartner(other);
		 String p0_Cmd=others.get("p0_Cmd");
		 String p1_MerId=others.get("p1_MerId");
		 String p2_Order=merchantOrderInfo.getId();
		 String p3_Amt=totalFee;
		 String p4_Cur=others.get("p4_Cur");
		 String p5_Pid=merchantOrderInfo.getMerchantProductName();
		 String p6_Pcat="";
		 String p7_Pdesc="";
		 if(!nullEmptyBlankJudge(merchantOrderInfo.getMerchantProductDesc())){
			 p7_Pdesc=merchantOrderInfo.getMerchantProductDesc();
		 }
		 String p8_Url=dictTradeChannels.getNotifyUrl();
		 String p9_SAF=others.get("p9_SAF");
		 String pa_MP="";
		 String pd_FrpId="";
		 if(!nullEmptyBlankJudge(paymentType)&&paymentType.equals(PaymentType.YEEPAY_GW.getValue())){
			 pd_FrpId="";
		 }else{
			 pd_FrpId=getYeePayFrpId(paymentType);
		 }
		
		 String pr_NeedResponse=others.get("pr_NeedResponse");
		 String keyValue=others.get("keyValue");
		 String hmac=HmacUtils.getReqMd5HmacForOnlinePayment(others.get("p0_Cmd"), p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
		 Map<String, String> dataMap=new HashMap<String, String>();
		 dataMap.put("p0_Cmd", p0_Cmd);
		 dataMap.put("p1_MerId", p1_MerId);
		 dataMap.put("p2_Order", p2_Order);
		 dataMap.put("p3_Amt", p3_Amt);
		 dataMap.put("p4_Cur", p4_Cur);
		 dataMap.put("p5_Pid", p5_Pid);
		 dataMap.put("p6_Pcat", p6_Pcat);
		 dataMap.put("p7_Pdesc", p7_Pdesc);
		 dataMap.put("p8_Url", p8_Url);
		 dataMap.put("p9_SAF", p9_SAF);
		 dataMap.put("pa_MP", pa_MP);
		 dataMap.put("pd_FrpId", pd_FrpId);
		 dataMap.put("pr_NeedResponse", pr_NeedResponse);
		 dataMap.put("hmac", hmac);
		 String res =SendPostMethod.buildRequest(dataMap, "post", "ok",payserviceDev.getYeepayCommonReqURL());
		return res;
	}	
	/**
	 * 获取渠道参数信息
	 * @param merchantOrderInfo 订单
	 * @return
	 */
	public Map<String, String> getOtherInfo(DictTradeChannelService dictTradeChannelService, MerchantOrderInfo merchantOrderInfo) {
		DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TZT.getValue());
         String other= dictTradeChannels.getOther();
     	 Map<String, String> others = new HashMap<String, String>();
     	  others=getPartner(other);
     	 return others;
	}
	  public static Map<String, String> getResult(String other){
			if(other==null&&"".equals(other)){
				return null;
			}else{
			String others []=other.split("&");
			Map<String, String> sParaTemp = new HashMap<String, String>();
			String values="";
			String charset="";
			String version="";
			for (int i=0;i<others.length;i++){
			   values=others[i];
			   int j=values.indexOf("=");
			   if(values.substring(0, j).equals("charset")){
				  charset=values.substring(j+1,values.length());  
			   }
			   if(values.substring(0, j).equals("version")){
				   version=values.substring(j+1,values.length());  
			   }
			}
			sParaTemp.put("charset", charset);
			sParaTemp.put("version", version);
			return sParaTemp;
			}
		}
	  
	    
	    public static Map<String, String> getDownlond(String other){
			if(other==null&&"".equals(other)){
				return null;
			}else{
			String others []=other.split("&");
			Map<String, String> sParaTemp = new HashMap<String, String>();
			String values="";
			String charset="";
			String version="";
			String download_url="";
			for (int i=0;i<others.length;i++){
			   values=others[i];
			   int j=values.indexOf("=");
			   if(values.substring(0, j).equals("charset")){
				  charset=values.substring(j+1,values.length());  
			   }
			   if(values.substring(0, j).equals("version")){
				   version=values.substring(j+1,values.length());  
			   }
			   if(values.substring(0, j).equals("download_url")){
				   download_url=values.substring(j+1,values.length());  
			   }
			}
			sParaTemp.put("charset", charset);
			sParaTemp.put("version", version);
			sParaTemp.put("download_url", download_url);
			return sParaTemp;
			}
		}
		
	 	public static ThirdScoreRequest initThirdScoreRequest(PayZxptInfo payZxptInfo) {
	 	    ThirdScoreRequest thirdScoreRequest = new ThirdScoreRequest();
	 		thirdScoreRequest.setOrderId(payZxptInfo.getId());
	 		thirdScoreRequest.setCertNo(payZxptInfo.getCertNo());
	 		thirdScoreRequest.setCertType(payZxptInfo.getCertType());
	 		thirdScoreRequest.setName(payZxptInfo.getUserName());
	 		thirdScoreRequest.setReserve(payZxptInfo.getReserve());
	 		thirdScoreRequest.setCardNo(payZxptInfo.getCardNo());
	 		thirdScoreRequest.setScoreChannel("2");
	 		thirdScoreRequest.setScoreMethod("1");
	 		thirdScoreRequest.setMobile(payZxptInfo.getPhone());
	 		thirdScoreRequest.setReasonNo(payZxptInfo.getReasonNo());
	 		thirdScoreRequest.setEntityAuthCode(payZxptInfo.getEntityAuthCode());
	 		thirdScoreRequest.setAuthDate(DateTools.dateToString(payZxptInfo.getAuthDate(), DateTools.FORMAT_ONE));
	 		return thirdScoreRequest;
	 	}
public static BqsFraudlistRequest init1301Request(PayZxptInfo payZxptInfo) {
			
			//请求消息体
			BqsFraudlistRequest bRequest=new BqsFraudlistRequest();
			bRequest.setChannelNo("1");
			bRequest.setCertNo(payZxptInfo.getCertNo());
			bRequest.setName(payZxptInfo.getUserName());
			bRequest.setMobile(payZxptInfo.getPhone());
			bRequest.setEntityAuthCode(payZxptInfo.getEntityAuthCode());
			bRequest.setEntityAuthDate(DateTools.dateToString(payZxptInfo.getAuthDate(), DateTools.FORMAT_ONE));
			bRequest.setOrderId(payZxptInfo.getId());
			return bRequest;
			
		}
		
	    /**
	     * 第三方征信评分
	     * @param payZxptInfo
	     * @return
	     */
	    public String getThirdScore(PayZxptInfo payZxptInfo,PayserviceDev payserviceDev){
	    	//第三方评分交易码1006
			String paket = "";
			String sign = "";
			String score="";
			Request<ThirdScoreRequest> zxptrequest = new Request<ThirdScoreRequest>();
			RequestHead head = initHead("1006",DateUtil.getDateTime(new Date()),payserviceDev);
			zxptrequest.setHead(head);
			List<ThirdScoreRequest> list = new ArrayList<ThirdScoreRequest>();
			ThirdScoreRequest rquestdetail = initThirdScoreRequest(payZxptInfo);
			list.add(rquestdetail);
			zxptrequest.setBody(list);
			String requestXml = XOUtil.objectToXml(zxptrequest, Request.class, RequestHead.class, ThirdScoreRequest.class);
//			XStream xstream=XMLUtil.fromXML(requestXml);
			//System.out.println(requestXml);
			try {
				paket = RSACoderUtil.encrypt(requestXml.getBytes(payserviceDev.getZxpt_charset()), payserviceDev.getZxpt_charset(), payserviceDev.getZxpt_public_key());
			    sign = MD5.sign(paket, "123456", "utf-8");
			//http请求参数
			Map<String, String> zxptParams = new HashMap<String, String>();
			//参数加密串
			zxptParams.put("packet", paket);
			//MD5签名
			zxptParams.put("checkValue", sign);
			//交易码
			zxptParams.put("tranCode", "1006");
			//商户号 奥鹏
			zxptParams.put("sender", payserviceDev.getZxpt_sender());
			//SIT环境
			String url = payserviceDev.getZxpt_third_url();
			//http请求
			String responsexml = HttpClientUtil.httpPost(url, zxptParams);
			//解密
			String decode = new String(RSACoderUtil.decrypt(responsexml, payserviceDev.getZxpt_key(), payserviceDev.getZxpt_charset()), payserviceDev.getZxpt_charset());
			String decodexml = URLDecoder.decode(decode, payserviceDev.getZxpt_charset());
			    Document doc = null;    
		        doc = DocumentHelper.parseText(decodexml);    
		        Element root = doc.getRootElement();// 指向根节点    
		        // normal解析    
		        Element body1 = root.element("body");
		        Element thirdscoreresponse1 = body1.element("thirdscoreresponse"); 
		        if(thirdscoreresponse1==null){
		        	score="0";
		        }else{
			        Element map1= thirdscoreresponse1.element("map");
			            List lstTime = map1.elements("entry");// 所有的Item节点    
			            for (int i = 0; i < lstTime.size(); i++) {    
			                Element etime = (Element) lstTime.get(i);    
			                Element start = etime.element("string"); 
			                if(start.getTextTrim().equals("records")){
			                	Element end = etime.element("list");
			                	 Element listmap = end.element("map"); 
			                	 List listentry = listmap.elements("entry");
			                	 for(int j=0;j<listentry.size();j++){
			                		 Element etime1 = (Element) listentry.get(j);
			                		 List aa = etime1.elements("string");
			                		 for(int k=0;k<aa.size();k++){
			                			 Element credooScores = (Element) aa.get(k); 
			                			// Element credooScore=credooScores.element("string");
			                			 if( credooScores.getTextTrim().equals("credooScore")){
			                				 System.out.println("start1.getTextTrim()=" + credooScores.getTextTrim()); 
			                				 Element Scores = (Element) aa.get(k+1);
			                				 score=Scores.getTextTrim();
			                				 System.out.println("score.getTextTrim()=" + Scores.getTextTrim()); 
			                				 break;
			                			 }
			                			 break;
			                		 }
			                	}
			                 }
			            }    
		        }
		    
			}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		  }
		  return score;
	    }
	    public static RequestHead initHead(String tranCode,String tranId,PayserviceDev payserviceDev) {
	 		//请求消息头
	 		RequestHead rh = new RequestHead();
	 		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	 		SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
	 		//初始化报文头
	 		rh.setVersion(payserviceDev.getZxpt_version());
	 		rh.setCharSet(payserviceDev.getZxpt_charset());
	 		rh.setSource(payserviceDev.getZxpt_source());
	 		rh.setDes(payserviceDev.getZxpt_des());
	 		rh.setApp(payserviceDev.getZxpt_app());
	 		rh.setTranCode(tranCode);
	 		rh.setTranId(tranId);
	 		rh.setTranRef(payserviceDev.getZxpt_tranref());
	 		rh.setReserve(payserviceDev.getZxpt_tranref());
	 		rh.setTranTime(df2.format(new Date()));
	 		rh.setTimeStamp(df.format(new Date()));
	 		return rh;
	 	}  	
		
}
