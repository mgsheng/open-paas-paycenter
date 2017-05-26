package cn.com.open.openpaas.payservice.app.channel.zxpt.rsa;

import java.security.MessageDigest;

public class CoderUtil
{
  public static final String KEY_SHA = "SHA";
  public static final String KEY_MD5 = "MD5";
  
  public static byte[] decryptBASE64(String key)
  {
    return Base64Util.base64ToByteArray(key);
  }
  
  public static String encryptBASE64(byte[] key)
    throws Exception
  {
    return Base64Util.byteArrayToBase64(key);
  }
  
  public static byte[] encryptMD5(byte[] data)
    throws Exception
  {
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    md5.update(data);
    return md5.digest();
  }
  
  public static byte[] encryptSHA(byte[] data)
    throws Exception
  {
    MessageDigest sha = MessageDigest.getInstance("SHA");
    sha.update(data);
    return sha.digest();
  }
}
