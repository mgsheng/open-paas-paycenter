package cn.com.open.openpaas.payservice.app.channel.alipay;


public enum PaymentType{
    //通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错
    //赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
    ALIPAY(10012,"ALIPAY"),ALIFAF(10015,"ALIFAF"),WEIXIN(10013,"WEIXIN"),UPOP(10014,"UPOP"),CMB(10001,"CMB"),ICBC(10002,"ICBC"),CCB(10003,"CCB"),PAYMAX(10016,"PAYMAX"),PAYMAX_H5(10017,"PAYMAX_H5"),WECHAT_WAP(10018,"WECHAT_WAP"),
    YEEPAY_EHK(10019,"YEEPAY_EHK"),ABC(10004,"ABC"),BOC(10005,"BOC"),BCOM(10006,"BCOM"),PSBC(10007,"PSBC"),CGB(10008,"CGB"),SPDB(10009,"SPDB"),CEB(10010,"CEB"),PAB(10011,"PAB"),DEFAULT(0,"其他"),YEEPAY_GW(10020,"YEEPAY_GW"),BOB(10021,"BOB");
    public int type;
	public String value;
	
    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
	PaymentType(int type,String value){
		this.type = type;
		this.value = value;
	}
    public String getValue() {
        return value;
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public static PaymentType getValueByType(int type){
		for(PaymentType status : PaymentType.values()){
			if(status.getType() == type){
				return status;
			}
		}
		return DEFAULT;
	}
	public static PaymentType getTypeByValue(String value){
		for(PaymentType status : PaymentType.values()){
			if(status.getValue().equals(value)){
				return status;
			}
		}
		return DEFAULT;
	}
    
}
