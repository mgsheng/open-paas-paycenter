package cn.com.open.openpaas.payservice.app.channel.alipay;

public enum CopyOfPaymentType{
    //通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错
    //赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
    ALIPAY("ALI_PAY"),ALI_FAF("ALI_FAF"),WEIXIN("WEIXIN"),TCL_ALIPAY("TCL_ALIPAY"),TCL_WEIXIN("TCL_WEIXIN"),TCL_UPOP("TCL_UPOP");
    private final String value;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    CopyOfPaymentType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
