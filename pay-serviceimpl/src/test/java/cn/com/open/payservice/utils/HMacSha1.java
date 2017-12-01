package cn.com.open.payservice.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.util.encoders.Base64;

public class HMacSha1 {  
  
    private static final String ALGORITHM = "HmacSHA1";    
    private static final String ENCODING = "UTF-8";    
      public static void main(String[] args) {
    	 try {
			String a= HmacSHA1Encrypt("appId=1&timestamp=2016-09-19T05:07:43Z&signatureNonce=310835&outTradeNo=1000100037257316&userId=982602&merchantId=10003&goodsName=学历缴费&paymentType=ALIPAY&paymentChannel=10001&totalFee=1000&businessType=1&parameter=R=1;S=01;U=10200;L=C0201001;O=346;","9ebada02676c4ccbbbdaeae27362896b");
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
