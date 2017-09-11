package cn.com.open.openpaas.payservice.app.channel.alipay;


public enum PayError{
    //通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错
    //赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
	DEFAULT("0","系统异常"),
	U1000001("1","必传参数有空值"),
	U1000002("2", "商户id不存在"),
	U1000003("3", "公共验证失败"),
	U1000004("4", "订单金额格式有误"),
	U1000005("5", "所选支付渠道与支付类型不匹配"),
	U1000006("6", "订单处理失败，请重新提交"),
	U1000007("7", "订单已处理，请勿重复提交"),
	U1000008("8", "拉卡拉下单失败"),
	U1000009("9", "拉卡拉下单失败"),
	U1000010("10", "您好：订单号重复,请检查后重新下单"),
	U1000011("11", "获取二维码失败");
   
    public String type;
	public String value;
	
    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
	PayError(String type,String value){
		this.type = type;
		this.value = value;
	}
    public String getValue() {
        return value;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public static PayError getValueByType(String type){
		for(PayError status : PayError.values()){
			if(status.getType() .equals(type)){
				return status;
			}
		}
		return DEFAULT;
	}
	public static PayError getTypeByValue(String value){
		for(PayError status : PayError.values()){
			if(status.getValue().equals(value)){
				return status;
			}
		}
		return DEFAULT;
	}
    
}
