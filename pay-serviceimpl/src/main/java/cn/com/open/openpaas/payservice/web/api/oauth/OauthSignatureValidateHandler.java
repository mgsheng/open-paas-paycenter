package cn.com.open.openpaas.payservice.web.api.oauth;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.tools.HMacSha1;


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
		 		//String result=	HMacSha1.HmacSHA1Encrypt("appId=10026&businessType=1&goodsName=测试微专业&merchantId=10001&outTradeNo=M20160720000001&signatureNonce=874714&timestamp=2016-07-21T02:22:15Z&totalFee=1&userId=a0fa5df1-63db-4bb5-9268-118f47b3ef8c&userName=admin@mail.open.com.cn",encryptKey);
				//6npRbyNi+f2hUthyMPGNQvqr0pg=
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
