package cn.com.open.openpaas.payservice.app.channel.zxpt.rsa;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class RSATest
{
  public static void main(String[] args)
    throws Exception
  {
    String charset = "utf-8";
    String paramsString = "中国";
    URLEncoder.encode(paramsString, charset);
    String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDAra3gkJMBqOvKcqiebejWOHEh3kBQYhsgPMI3lw1VUl8QHcTHyTvQ+sc6hNHz5cYWVRHAEFF7BmMPGa9mNMz038vqJu/awgMjZGz/4oG/Y2KsrAjWtFypd0f2dc1cdNFVqI3lA9fuqgNPkGf//dLkISZVH1PUDSs+dcdu+ulV5wIDAQAB";
    String key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMCtreCQkwGo68pyqJ5t6NY4cSHeQFBiGyA8wjeXDVVSXxAdxMfJO9D6xzqE0fPlxhZVEcAQUXsGYw8Zr2Y0zPTfy+om79rCAyNkbP/igb9jYqysCNa0XKl3R/Z1zVx00VWojeUD1+6qA0+QZ//90uQhJlUfU9QNKz51x2766VXnAgMBAAECgYBehVUlMwdK6ykw2WqqvdRZMrsdGECIrngMKoJEbw+VaaFE8LWWJLv5WuzdYkb01SWF0xmwFNFD/vAdekY3Z3ObWj/OrNL297MT8aTjmH0H79OnZfeL+wChoxKk1FE1pA8iUJB0awLInv72GQ7JEJ+GnkbJb0QsSowyo9B9MwAQoQJBAPRcQhCQ5iVgofZej0Xk8T9bHk5jFU8Duu/jR8AfP/S0vbJuxfw9FU5F++rghK2oyS5yOSXCZV8WjOuN5jwzFUkCQQDJ2zWD8LCnMWQCkdBscwOEA83Mq+cur8yoc7uR2QfNpWOuTtuK1gvoPH91A3m5rl8P9pFshTibklna2IZYJIGvAkAxxtFWWo3nM0YKz7xTuo1CIKeNxDVFATeFQkENa9A1YtP5kwMVnMPITA1DDTU5wtYodfAaNv07X3aZTTCHNsixAkBH0GlVq4fts7C1CVNxgem6SfAp5O62uWzCcYpF9UTFcRXpqbyJxGUwFnXyF25zFQpVD4/lX/AnyQWWynnhWfuZAkEAvn7KG3umWpJA+RzNhUXFGpVTtn8JZdfDLQXtl7K7BEN7+xBsZ9zAjFs++QD1vUqLokqqKu4uUFNGkUlFB71dPg==";
    String data = RSACoderUtil.encrypt(paramsString.getBytes(charset), charset, publicKey);
    System.out.println(data);
    byte[] outdata = RSACoderUtil.decrypt(data, key, charset);
    String decode = new String(outdata, charset);
    String xx = URLDecoder.decode(decode, charset);
    System.out.println(xx);
  }
}
