package cn.com.open.openpaas.payservice.app.balance.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.payservice.app.balance.model.UserAccountBalance;
import cn.com.open.openpaas.payservice.app.infrastructure.repository.MerchantInfoRepository;
import cn.com.open.openpaas.payservice.app.infrastructure.repository.UserAccountBalanceRepository;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.app.tools.StringTool;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import net.sf.json.JSONObject;

/**
 * 
 */
@Service("userAccountBalanceService")
public class UserAccountBalanceServiceImpl implements UserAccountBalanceService {

    @Autowired
    private UserAccountBalanceRepository userAccountBalanceRepository;
    @Autowired
	 private PayserviceDev payserviceDev;

	@Override
	public void saveUserAccountBalance(UserAccountBalance userAccountBalance) {
		userAccountBalanceRepository.saveUserAccountBalance(userAccountBalance);
		
	}

	@Override
	public UserAccountBalance findByUserId(String userId) {
		// TODO Auto-generated method stub
		return userAccountBalanceRepository.findByUserId(userId);
	}

	@Override
	public Boolean updateBalanceInfo(UserAccountBalance userAccountBalance) {
		try{
			userAccountBalanceRepository.updateBalanceInfo(userAccountBalance);
			return true;
		}catch(Exception e){
			return false;
		}
		
	}

	@Override
	public UserAccountBalance getBalanceInfo(String sourceId, Integer appId) {
		// TODO Auto-generated method stub
		return userAccountBalanceRepository.getBalanceInfo(sourceId, appId);
	}

	@Override
	public void saveAccountBalance(String sourceId, Integer appId,String payKey,String userName) {
        try {
        	UserAccountBalance  userAccountBalance=userAccountBalanceRepository.getBalanceInfo(sourceId, appId);
    		if(userAccountBalance==null){
    			SortedMap<Object,Object> sParaTemp2 = new TreeMap<Object,Object>();
    			String timestamp=DateTools.getSolrDate(new Date());
    			String signatureNonce=StringTool.getRandom(100,1);
    			sParaTemp2.put("app_id",appId);
    			sParaTemp2.put("timestamp", timestamp);
    			sParaTemp2.put("signatureNonce", signatureNonce);
    			sParaTemp2.put("source_id", sourceId);
    			String params2=createSign(sParaTemp2);
    	   		String signature=HMacSha1.HmacSHA1Encrypt(params2, payKey);
    	   		signature=HMacSha1.getNewResult(signature);
    	   		sParaTemp2.put("signature", signature);
    			String result=sendPost(payserviceDev.getUserCenter_getUserId_url(), sParaTemp2);
    			 JSONObject obj = JSONObject.fromObject(result);
    			 String status = obj.getString("status");
    			 //用户中心返回的用户的唯一ID
    			 String user_id="";
    				if(status.equals("1")){
    					user_id= obj.getString("user_id");
    				}
    			userAccountBalance=new UserAccountBalance();
    			userAccountBalance.setUserName(userName);
    			userAccountBalance.setStatus(1);
    			userAccountBalance.setType(2);
    			userAccountBalance.setSourceId(sourceId);
    			userAccountBalance.setUserId(user_id);
    			userAccountBalance.setAppId(appId);
    			userAccountBalance.setCreateTime(new Date());
    			userAccountBalanceRepository.saveUserAccountBalance(userAccountBalance);
    		}
		} catch (Exception e) {
			// TODO: handle exception
		}
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
}