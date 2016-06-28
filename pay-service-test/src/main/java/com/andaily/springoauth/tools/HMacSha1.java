package com.andaily.springoauth.tools;

import javax.crypto.Mac;  
import javax.crypto.SecretKey;  
import javax.crypto.spec.SecretKeySpec;  

import com.sun.org.apache.xml.internal.security.utils.Base64;
  
public class HMacSha1 {  
  
    private static final String ALGORITHM = "HmacSHA1";    
    private static final String ENCODING = "UTF-8";    
      public static void main(String[] args) {
    	 try {
			String a= HmacSHA1Encrypt("hello中国fdgdftcl！@#￥+","1367812345678");
			System.out.println(a);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    /**  
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名  
     * @param encryptText 被签名的字符串  
     * @param encryptKey  密钥  
     * @return  
     * @throws Exception  
     */    
    public static String HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception     
    {           
    	    Mac mac = Mac.getInstance(ALGORITHM);
    	    mac.init(new SecretKeySpec(encryptKey.getBytes(ENCODING), ALGORITHM));
    	    byte[] signData = mac.doFinal(encryptText.getBytes(ENCODING));

    	    String signature = new String(Base64.encode(signData));
    	    signature = signature.replaceAll(new String("\r"), "");
    	    signature = signature.replaceAll(new String("\n"), "");
        //完成 Mac 操作   
        return      signature;    
    } 
    public static String getNewResult(String result){
    	if(result.contains("+")){
    		result=result.replaceAll("\\+", "%2B");
    	}
    	return result;
    };

}  
