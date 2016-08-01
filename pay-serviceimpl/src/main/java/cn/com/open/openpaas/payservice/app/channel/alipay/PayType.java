package cn.com.open.openpaas.payservice.app.channel.alipay;

public enum PayType{
	  //通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错
    //赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
    PAY(1,"PAY"),COSTS(2,"COSTS"),ALL(0,"ALL");
    public int type;
	public String value;
	

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
	PayType(int type,String value){
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
	public static PayType getValueByType(int type){
		for(PayType status : PayType.values()){
			if(status.getType() == type){
				return status;
			}
		}
		return ALL;
	}
	public static PayType getTypeByValue(String value){
		for(PayType status : PayType.values()){
			if(status.getValue().equals(value)){
				return status;
			}
		}
		return ALL;
	}
}
