package cn.com.open.user.platform.manager.tools;
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
    	String encrypt = base64Encode(desEncrypt(input.getBytes(),desKey.substring(0,8)));
    	encrypt = encrypt.replaceAll(new String("\r"), "");
    	encrypt = encrypt.replaceAll(new String("\n"), "");
        return encrypt;
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
    public static String getNewSecert(String password){
    	if(password.contains("+")){
    		password=password.replaceAll("\\+", "%2B");
    	}
    	return password;
    };
 
    public static byte[] base64Decode(String s) throws IOException {
        if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        s = new String(s.getBytes("UTF-8"));
        byte[] b = decoder.decodeBuffer(s);
        return b;
    }

    public static void main(String[] args) throws Exception {
        String source = "testmooc1111#king_hs8761@sina.cn#18727392111#78d8f1862a7e4f54b71f5962f8d17d6c#20160606135522#13456";
        System.out.println("原文: " + source);
        String key = "945fa18c666a4e0097809f6727bc6997";
        String encryptData = encrypt(source, key);
        System.out.println("加密后: " + encryptData);
        String decryptData = decrypt("PPFdOqK+PNQxkkuRSvRXmiMhCiZuoOQzVyEVrblHik8nKACafGQGvLASOU90NR9xVP20ewVpY/3d1UFnFLoaIOmFE4XjJSKFuo8I49b9XikNQOg+ALZj3NP2lduxT9yf",  key);
        System.out.println("解密后: " + decryptData);
    }
}
