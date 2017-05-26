package cn.com.open.openpaas.payservice.app.channel.zxpt.rsa;

public enum EncryptionModeEnum
{
  RSA1024("rsa1024", "RSA1024加密算法"),  RSA2048("rsa2048", "RSA2048加密算法"),  AES("aes", "AES对称加密算法");
  
  private String code;
  private String desc;
  
  private EncryptionModeEnum(String code, String desc)
  {
    this.code = code;
    this.desc = desc;
  }
  
  public String getCode()
  {
    return this.code;
  }
  
  public void setCode(String code)
  {
    this.code = code;
  }
  
  public String getDesc()
  {
    return this.desc;
  }
  
  public void setDesc(String desc)
  {
    this.desc = desc;
  }
}
