package cn.com.open.openpaas.payservice.app.channel.alipay;

public enum Channel{
    //通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错
    //赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
    ALI(10001), WEIXIN(10002),UPOP(10003), TCL(10004),EBANK(10005),EHK_WEIXIN_PAY(10006),PAYMAX(10007),YEEPAY_EB(10008),ALIFAF(10009),WEIXIN_WECHAT_WAP(10010)
    ,WECHAT_WAP(10011),EHK_BANK(10012),PAYMAX_WECHAT_CSB(10013),TZT(10014),PAYMAX_H5(10015),YEEPAY_WEIXIN(10016),YEEPAY_ALI(10017),YEEPAY_ALL(10018),EHK_ALI_PAY(10019),EHK_INSTALLMENT_LOAN(10020);
    private final Integer value;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    Channel(Integer value) {
        this.value = value;
    }
    
    public Integer getValue() {
        return value;
    }
}
