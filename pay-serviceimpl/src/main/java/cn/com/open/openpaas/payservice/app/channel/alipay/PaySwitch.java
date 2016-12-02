package cn.com.open.openpaas.payservice.app.channel.alipay;

/**
 * 开关0.直连1.tcl支付2.拉卡拉3.易宝4.支付宝当面付
 * 
 * @author admin
 *
 */
public enum PaySwitch{
    //通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错
    //赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
    ALI(0), TCL(1),YEEPAY(2),PAYMAX(3),WEIXIN(4),UNIONPAY(5);
    private final Integer value;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    PaySwitch(Integer value) {
        this.value = value;
    }
    
    public Integer getValue() {
        return value;
    }
}