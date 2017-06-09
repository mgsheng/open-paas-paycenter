package cn.com.open.pay.order.service.dev;

public enum StatementType{
	//	Paymax支持下载的对账单交易类别：
	//	ALL（全部）
	//	SUCCESS（交易）
	//	REFUND（退款）
	//	WECHAT_CSB（微信公众号 C2B扫码）
	ALL("ALL"), SUCCESS("SUCCESS"),REFUND("REFUND"), WECHAT_CSB("WECHAT_CSB");
    private final String value;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    StatementType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
