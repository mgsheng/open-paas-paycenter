package cn.com.open.openpaas.payservice.app.channel.yeepay.paymobile.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 工具类
 * @author: yingjie.wang    
 * @since : 2015-10-08 16:38
 */

public class PaymobileUtils {
	
	//配置文件路径
	public static final String CONFIG_FILE_PATH			= "cn/com/open/openpaas/payservice/app/channel/yeepay/paymobile/resource";
	
	public static final String PAYAPI_NAME				= "PayApi";
	public static final String QUERYORDERAPI_NAME		= "QueryOrderApi";
	public static final String REFUNDAPI_NAME			= "RefundApi";
	public static final String QUERYREFUNDAPI_NAME		= "QueryRefundApi";
	public static final String CHECKBANKCARDAPI_NAME	= "CheckBankcardApi";
	public static final String PAYCLEARDATAAPI_NAME		= "PayClearDataApi";
	public static final String REFUNDCLEARDATAAPI_NAME	= "RefundClearDataApi";
	public static final String QUERYBANKCARDLISTAPI_NAME	= "QueryBankCardListApi";
	public static final String UNBINDCARDAPI_NAME	= "UnbindCardApi";
	
	//编码格式UTF-8
	public static final String CHARSET 			= "UTF-8";
	
	private static String merchantaccount		= "";
	private static String merchantPrivateKey	= "";
	private static String yeepayPublicKey		= "";
	/*static {
		//初始化merchantInfo
		
		//merchantInfo		= Configuration.getInstance(CONFIG_FILE_PATH);
		//从配置文件中获取商户编号
		merchantaccount 	= PropertiesTool.getAppPropertieByKey("merchantaccount");
		//从配置文件中获取商户私钥
		merchantPrivateKey 	= PropertiesTool.getAppPropertieByKey("merchantPrivateKey");
		//从配置文件中获取易宝公钥
		yeepayPublicKey		= PropertiesTool.getAppPropertieByKey("yeepayPublicKey");
	}
	*/
	//获取请求地址
	public static String getRequestUrl(String apiName) {
		return PropertiesTool.getAppPropertieByKey("PayApi");
	}
	
	//生成AESKey: 16位的随机串
	public static String buildAESKey() {
		return RandomUtil.getRandom(16);
	}
	
	//使用易宝公钥将AESKey加密生成encryptkey
	public static String buildEncryptkey(String AESKey, String publicKey) {
		String encryptkey= "";
		try {
			encryptkey = RSA.encrypt(AESKey, publicKey);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return encryptkey;
	}
	
	//使用易宝公钥将AESKey加密生成encryptkey
	public static String buildEncyptkey(String AESKey,String yeepayPublicKey) {
		return buildEncryptkey(AESKey, yeepayPublicKey);
	}
	//使用易宝公钥将AESKey加密生成encryptkey
		public static String buildEncyptkey(String AESKey) {
			return buildEncryptkey(AESKey, yeepayPublicKey);
		}
	//生成RSA签名：sign
	public static String buildSign(TreeMap<String, Object> treeMap,String privateKey) {
		
		String sign				= "";
		StringBuffer buffer		= new StringBuffer();
		for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
			if (entry.getValue() != null) {
				buffer.append(entry.getValue());
			}
		}
		String signTemp = buffer.toString();

		if (StringUtils.isNotEmpty(privateKey)) {
			sign = RSA.sign(signTemp, privateKey);
		}
		return sign;
	}
	
	//使用商户私钥生成RSA签名：sign
	public static String buildSign(TreeMap<String, Object> treeMap) {
		return buildSign(treeMap, merchantPrivateKey);
	}

	//生成密文：data
	public static String buildData(TreeMap<String, Object> treeMap, String AESKey,String merchantaccount,String merchantPrivateKey) {
		
		String data 		= "";
		
		//将商户编号放入treeMap
		treeMap.put("merchantaccount", merchantaccount);
		
		//生成sign，并将其放入treeMap
		String sign			= buildSign(treeMap,merchantPrivateKey);
		treeMap.put("sign", sign);
		
		String jsonStr		= JSON.toJSONString(treeMap);
		data				= AES.encryptToBase64(jsonStr, AESKey);
		
		return data;
	}
	//生成密文：data
	public static String buildData(TreeMap<String, Object> treeMap, String AESKey) {
		
		String data 		= "";
		
		//将商户编号放入treeMap
		treeMap.put("merchantaccount", merchantaccount);
		
		//生成sign，并将其放入treeMap
		String sign			= buildSign(treeMap);
		treeMap.put("sign", sign);
		
		String jsonStr		= JSON.toJSONString(treeMap);
		data				= AES.encryptToBase64(jsonStr, AESKey);
		
		return data;
	}
	//一键支付post请求
	public static TreeMap<String, String> httpPost(String url, String merchantaccount, String data, String encryptkey) {
		
		TreeMap<String, String> result		= null;
		
		//请求参数为如下三者：merchantaccount、data、enrcyptkey
		Map<String, String> paramMap	= new HashMap<String, String>();
		paramMap.put("data", data);
		paramMap.put("encryptkey", encryptkey);
		paramMap.put("merchantaccount", merchantaccount);
		
		String responseBody	= HttpClient4Utils.sendHttpRequest(url, paramMap, CHARSET, true);
		result				= JSON.parseObject(responseBody, new TypeReference<TreeMap<String, String>>() {});
		
		return result;
	}
	
	//get请求
	public static TreeMap<String, String> httpGet(String url, String merchantaccount, String data, String encryptkey) {
		
		TreeMap<String, String> result		= null;
		
		//请求参数为如下三者：merchantaccount、data、enrcyptkey
		Map<String, String> paramMap	= new HashMap<String, String>();
		paramMap.put("data", data);
		paramMap.put("encryptkey", encryptkey);
		paramMap.put("merchantaccount", merchantaccount);
		
		String responseBody	= HttpClient4Utils.sendHttpRequest(url, paramMap, CHARSET, false);
		result				= JSON.parseObject(responseBody, new TypeReference<TreeMap<String, String>>() {});
		
		return result;
	}
	
	//解密data，获得明文参数
	public static TreeMap<String, String> decrypt(String data, String encryptkey, String merchantPrivateKey) {
		
		TreeMap<String, String> result	= null;
		
		//1.使用商户密钥解密encryptKey。
		String AESKey 	= "";
		try {
			AESKey = RSA.decrypt(encryptkey, merchantPrivateKey);
			System.out.println("AESKey:"+AESKey);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		//2.使用AESKey解开data，取得明文参数；解密后格式为json
		String jsonStr	= AES.decryptFromBase64(data, AESKey);
		
		//3.将JSON格式数据转换成TreeMap格式
		result			= JSON.parseObject(jsonStr,new TypeReference<TreeMap<String, String>>() {});
		
		return result;
	}
	
	//解密data，获得明文参数
	public static TreeMap<String, String> decrypt(String data, String encryptkey) {
		return decrypt(data, encryptkey, merchantPrivateKey);
	}
	//sign验签
	public static boolean checkSign(String params, String signYeepay, String publicKey) {
		return RSA.checkSign(params, signYeepay, publicKey);
	}
	
	// sign验签
	public static boolean checkSign(TreeMap<String, String> dataMap) {

		//获取明文参数中的sign。
		String signYeepay	= StringUtils.trimToEmpty(dataMap.get("sign"));

		//将明文参数中sign之外的其他参数，拼接成字符串
		StringBuffer buffer	= new StringBuffer();
		for(Entry<String, String> entry : dataMap.entrySet()) {
			String key		= formatStr(entry.getKey());
			String value	= formatStr(entry.getValue());
			if("sign".equals(key)) {
				continue;
			}
			buffer.append(value);
		}
		
		//result为true时表明验签通过
		boolean result = checkSign(buffer.toString(), signYeepay, yeepayPublicKey);

		return result;
	}
	// sign验签
	public static boolean checkSign(TreeMap<String, String> dataMap,String yeepayPublicKey) {

		//获取明文参数中的sign。
		String signYeepay	= StringUtils.trimToEmpty(dataMap.get("sign"));

		//将明文参数中sign之外的其他参数，拼接成字符串
		StringBuffer buffer	= new StringBuffer();
		for(Entry<String, String> entry : dataMap.entrySet()) {
			String key		= formatStr(entry.getKey());
			String value	= formatStr(entry.getValue());
			if("sign".equals(key)) {
				continue;
			}
			buffer.append(value);
		}
		
		//result为true时表明验签通过
		boolean result = checkSign(buffer.toString(), signYeepay, yeepayPublicKey);

		return result;
	}
	public static InputStream clearDataHttpGet(String url, String merchantaccount, String data, String encryptkey) {
		return ClearDataUtils.httpGet(url, merchantaccount, data, encryptkey);
	}
	
	//getter
	public static String getMerchantaccount() {
		return merchantaccount;
	}
	
	//getter
	public static String getMerchantPrivateKey() {
		return merchantPrivateKey;
	}
	
	//getter
	public static String getYeepayPublicKey() {
		return yeepayPublicKey;
	}
	
	//字符串格式化
	public static String formatStr(String text) {
		return (text == null) ? "" : text.trim();
	}

	//test
	public static void main(String[] args) {
		
		TreeMap<String, String> responseDataMap = decrypt("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");
	    System.out.println(responseDataMap);
	}
}
