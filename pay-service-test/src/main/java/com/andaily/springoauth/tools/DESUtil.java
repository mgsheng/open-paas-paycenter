package com.andaily.springoauth.tools;
import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class DESUtil {
	 
 
    public static byte[] desEncrypt(byte[] plainText,String desKey) throws Exception {
        SecureRandom sr = new SecureRandom();
        byte rawKeyData[] = desKey.getBytes();
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        byte data[] = plainText;
        byte encryptedData[] = cipher.doFinal(data);
        return encryptedData;
    }
 
    public static byte[] desDecrypt(byte[] encryptText,String desKey) throws Exception {
        SecureRandom sr = new SecureRandom();
        byte rawKeyData[] = desKey.getBytes();
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        byte encryptedData[] = encryptText;
        byte decryptedData[] = cipher.doFinal(encryptedData);
        return decryptedData;
    }
 
    public static String encrypt(String input,String desKey) throws Exception {
        return base64Encode(desEncrypt(input.getBytes(),desKey.substring(0,8)));
    }
 
    public static String decrypt(String input,String desKey) throws Exception {
        byte[] result = base64Decode(input);
        return new String(desDecrypt(result,desKey.substring(0,8)));
    }
 
    public static String base64Encode(byte[] s) {
        if (s == null)
            return null;
        BASE64Encoder b = new sun.misc.BASE64Encoder();
        return b.encode(s);
    }
 
    public static byte[] base64Decode(String s) throws IOException {
        if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer(s);
        return b;
    }
    
    public static String getNewPwd(String password){
    	if(password.contains("+")){
    		password=password.replaceAll("\\+", "%2B");
    	}
    	return password;
    };

    public static void main(String[] args) throws Exception {
        String source = "111111";
        System.out.println("原文: " + source);
        String key = "9ebada02676c4ccbbbdaeae27362896b";
        String encryptData = encrypt(source, key);
        System.out.println("加密后: " + encryptData);
        String decryptData = decrypt(encryptData,  key);
        System.out.println("解密后: " + decryptData);
    }
}
