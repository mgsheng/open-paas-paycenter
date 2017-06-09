package cn.com.open.pay.order.service.dev;

public enum ChannelCategory{
	//	Paymax支持下载的支付渠道类别：
	//	ALIPAY（支付宝）
	//	WECHAT（微信）
	//	APPLE（苹果）
	//	LAKALA（拉卡拉）
	ALIPAY("ALIPAY"), WECHAT("WECHAT"),APPLE("APPLE"), LAKALA("LAKALA");
    private final String value;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    ChannelCategory(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
