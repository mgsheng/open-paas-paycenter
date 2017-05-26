package cn.com.open.openpaas.payservice.app.tools;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

  
public class HMacSha1 {  
  
    private static final String ALGORITHM = "HmacSHA1";    
    private static final String ENCODING = "UTF-8";    
      public static void main(String[] args) {
    	 try {
    		 PasswordEncoder passwordEncoder = new ShaPasswordEncoder();
    		 String a=	 passwordEncoder.encodePassword("abc123", null);
    		 String data="sdfsdfesdfsfd";
			String b= HmacSHA1Encrypt("appId=10023&businessType=1&clientIp=127.0.0.1&goodsName=考务报名费用&merchantId=10006&outTradeNo=10011704270000040b6d2a8903304b76ba3c3b030a6158ce&paymentChannel=10001&paymentType=ALIPAY&signatureNonce=13&timestamp=2017-05-02T11:34:59Z&totalFee=16000.0&userId=09f1b037d24243a3bab25b06d7b8480f&userName=register51016","616ab89e64338da160dfd60c0de9240");
			System.out.println(b);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//AfV4oUzmtns5XUgrtEsHERclREY=
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
