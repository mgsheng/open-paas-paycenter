package cn.com.open.openpaas.payservice.app.channel.alipay;


public enum PayStatus{
    //通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错
    //赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
	  SUCCESS(1,"SUCCESS"), 
	  INIT(0,"SUCCESS"),
	  ERROR(2,"SUCCESS"),
	  IN_PROCESS(3,"SUCCESS"),
	  VERIFYERROR(4,"SUCCESS"),
	  REPAYMENTED(5,"REPAYMENTED"),
	  IN_REPAYMENT(6,"IN_REPAYMENT");
    public int type;
	public String value;
	
    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
	PayStatus(int type,String value){
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
	public static PayStatus getValueByType(int type){
		for(PayStatus status : PayStatus.values()){
			if(status.getType() == type){
				return status;
			}
		}
		return INIT;
	}
	public static PayStatus getTypeByValue(String value){
		for(PayStatus status : PayStatus.values()){
			if(status.getValue().equals(value)){
				return status;
			}
		}
		return INIT;
	}
    
}
