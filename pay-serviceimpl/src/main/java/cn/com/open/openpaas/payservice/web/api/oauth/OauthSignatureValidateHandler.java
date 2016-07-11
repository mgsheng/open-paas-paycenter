package cn.com.open.openpaas.payservice.web.api.oauth;


import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.web.api.order.UnifyPayController;


public class OauthSignatureValidateHandler {
	private static final Logger log = LoggerFactory.getLogger(OauthSignatureValidateHandler.class);
	final static String  SEPARATOR = "&";
	/**
	 * Signature验证算法
	 * 格式：client_id&access_token&timestamp&signatureNonce
	 * 加密方法:HmacSHA1
	 * @param request
	 * @param app
	 * @return
	 */
	  public static boolean validateSignature(HttpServletRequest request,MerchantInfo merchantInfo){
		  
		    String clientId=request.getParameter("appId");
		    //String accessToken=request.getParameter("access_token");
		    String signature=request.getParameter("signature");
		    String timestamp=request.getParameter("timestamp");
		    String signatureNonce=request.getParameter("signatureNonce");
		    
		    
		    log.info("~~~~~~~~~~~认证参数："+"clientId="+clientId+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce);
		    
		 	StringBuilder encryptText = new StringBuilder();
		 	encryptText.append(clientId);
			encryptText.append(SEPARATOR);
		 	/*encryptText.append(accessToken);
		 	encryptText.append(SEPARATOR);*/
		 	encryptText.append(timestamp);
		 	encryptText.append(SEPARATOR);
		 	encryptText.append(signatureNonce);
		 	try {
				String result=	HMacSha1.HmacSHA1Encrypt(encryptText.toString(), merchantInfo.getPayKey());
				
				log.info("~~~~~~~~~~~认证结果："+"result="+result);
				
				if(signature.equals(result))
					return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return false;
	}  
	  /**
	   * 
	   * @param signature 解密结果
	   * @param encryptText 加密字符串
	   * @param encryptKey  秘钥
	   * @return
	   */
	  public static boolean validateSignature(String signature,String encryptText, String encryptKey){
		 	try {
				String result=	HMacSha1.HmacSHA1Encrypt(encryptText,encryptKey);
				
				log.info("~~~~~~~~~~~认证结果："+"result="+result);
				
				if(signature.equals(result))
					return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return false;
	}  
}
