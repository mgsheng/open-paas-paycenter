package cn.com.open.openpaas.payservice.app.channel.alipay;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePayContentBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;

import cn.com.open.openpaas.payservice.app.log.AlipayControllerLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;


/**
 * ali 扫码支付接口
 */
@Controller
@RequestMapping("/alipay/trade/")
public class AlipayFTFController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(AlipayFTFController.class);
	 // 支付宝当面付2.0服务
	    private static AlipayTradeService tradeService;

	    // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
	    private static AlipayTradeService tradeWithHBService;

	    // 支付宝交易保障接口服务
	    private static AlipayMonitorService monitorService;

	    static {
	        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
	         *  Configs会读取classpath下的alipayrisk10.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
	         */
	        Configs.init("zfbinfo.properties");

	        /** 使用Configs提供的默认参数
	         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
	         */
	        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();


	        // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
	        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();


	        /** 如果需要在程序中覆盖Configs提供的默认参数, 可以使用ClientBuilder类的setXXX方法修改默认参数 否则使用代码中的默认设置 */
	        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
//	                            .setGatewayUrl("http://localhost:7777/gateway.do")
	                            .setCharset("GBK")
	                            .setFormat("json")
	                            .build();
	    }

	/**
	 * 支付宝订单回调接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping("precreate ")
	public void precreate (HttpServletRequest request,HttpServletResponse response,Map<String,Object> model) throws MalformedURLException, DocumentException, IOException {
		String backMsg="";
		   // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        //String outTradeNo = "tradepay" + System.currentTimeMillis() + (long)(Math.random() * 10000000L);
         String outTradeNo=request.getParameter("outTradeNo");
        // (必填) 订单标题，粗略描述用户的支付目的。如“喜士多（浦东店）消费”
        //String subject = "条码支付-消费";
         String subject=request.getParameter("subject");
        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        //String totalAmount = "0.01";
        String totalAmount=request.getParameter("totalAmount");
        // (必填) 付款条码，用户支付宝钱包手机app点击“付款”产生的付款条码
//        String authCode = "286648048691290423";   // 未用条码
//        String authCode = "286399918342265510"; // 已用条码
       // String authCode = "287231759284359794"; // 已用条码
         String authCode=request.getParameter("authCode");
        // (不推荐使用) 订单可打折金额，可以配合商家平台配置折扣活动，如果订单部分商品参与打折，可以将部分商品总价填写至此字段，默认全部商品可打折
        // 如果该值未传入,但传入了【订单总金额】,【不可打折金额】 则该值默认为【订单总金额】- 【不可打折金额】
//        String discountableAmount = "1.00"; //

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        //String undiscountableAmount = "0.0";
        String undiscountableAmount=request.getParameter("undiscountableAmount");
        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        //String sellerId = "";
        String sellerId=request.getParameter("sellerId");
        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
       // String body = "购买商品2件共15.00元";
        String body=request.getParameter("body");
        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        //String operatorId = "test_operator_id";
        String operatorId=request.getParameter("operatorId");
        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        //String storeId = "test_store_id";
        String storeId=request.getParameter("storeId");
        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
       /* String providerId = "2088100200300400500";
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId(providerId);*/
        // 支付超时，线下扫码交易定义为5分钟
        //String timeExpress = "5m";
        String timeExpress=request.getParameter("timeExpress");
        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        String goodsId=request.getParameter("goodsId");
        String goodsName=request.getParameter("goodsName");
        GoodsDetail goods1 = GoodsDetail.newInstance(goodsId, goodsName, 1500, 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“xx牙刷”，单价为5.05元，购买了两件
     /*   GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xx牙刷", 505, 2);
        goodsDetailList.add(goods2);
      */
        // 创建请求builder，设置请求参数
        AlipayTradePayContentBuilder builder = new AlipayTradePayContentBuilder()
                .setOutTradeNo(outTradeNo)
                .setSubject(subject)
                .setAuthCode(authCode)
                .setTotalAmount(totalAmount)
                .setStoreId(storeId)
                .setUndiscountableAmount(undiscountableAmount)
                .setBody(body)
                .setOperatorId(operatorId)
                //.setExtendParams(extendParams)
                .setSellerId(sellerId)
                .setGoodsDetailList(goodsDetailList)
                .setTimeExpress(timeExpress);

        // 调用tradePay方法获取当面付应答
        AlipayF2FPayResult result = tradeService.tradePay(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
            	backMsg="支付宝支付成功!";
                log.info("支付宝支付成功!)");
                break;

            case FAILED:
            	backMsg="支付宝支付失败!!!";
                log.error("支付宝支付失败!!!");
                break;

            case UNKNOWN:
            	backMsg="系统异常，订单状态未知!!!";
                log.error("系统异常，订单状态未知!!!");
                break;

            default:
            	backMsg="不支持的交易状态，交易返回异常!!!";
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
		WebUtils.writeJson(response, backMsg);
	  } 
}