package cn.com.open.openpaas.payservice.app.channel.zxpt.rsa;

public enum SignTypeEnum
{
  SHA1WITHRSA("RSA", "SHA1withRSA"),  SHA256WITHRSA("RSA2", "SHA256withRSA");
  
  private String code;
  private String desc;
  
  private SignTypeEnum(String code, String desc)
  {
    this.code = code;
    this.desc = desc;
  }
  
  public boolean isSHA1WITHRSA()
  {
    return equals(SHA1WITHRSA);
  }
  
  public boolean isSHA256WITHRSA()
  {
    return equals(SHA256WITHRSA);
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
