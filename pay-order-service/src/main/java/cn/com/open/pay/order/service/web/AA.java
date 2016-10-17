package cn.com.open.pay.order.service.web;

public class AA {
              
// 开发语言：JAVA
//适用JDK版本：1.4
// 版    权：支付宝（中国）网络技术有限公司
//───────
//1、代码文件说明
//───────
//alipay-sdk-java*.jar┈┈┈┈┈┈┈支付宝SDK编译文件jar
//alipay-sdk-java*-source.jar┈┈┈ 支付宝SDK源码文件jar
//commons-logging-1.1.1.jar┈┈┈┈┈┈SDK依赖的日志jar
//commons-logging-1.1.1-sources.jar┈┈SDK依赖的日志源码jar
//bcprov-jdk14-137.jar┈┈第三方加解密jar
//
//※注意※
//集成支付宝接口需要引入的文件是：
//alipay-sdk-java*.jar
//commons-logging-1.1.1.jar
//
//若进一步了解代码实现请引入文件：
//alipay-sdk-java*-source.jar
//commons-logging-1.1.1-sources.jar
//
//若使用AlipaySignature.class加解密方法在进行本地调试时，报“java.security.nosuchalgorithmexception cannot find any provider supporting”错误
//请尝试以下方法：
//1.将java目录下的jre\lib\security子目录的java.security文件中增加 security.provider.<n>=org.bouncycastle.jce.provider.BouncyCastleProvider，其中<n>根据前面几个提供者的序号递增，例如在j2sdk1.4.2_13中为6。
//2.将bcprov-jdk14-137.jar复制到java目录下jre\lib\ext目录。(如果不行，就复制到java目录下lib\ext目录，如果再不行，就将其再复制到ext的上级目录下。
//
//─────────
//2、主要类文件功能说明
//─────────
//┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉
//DefaultAlipayClient.java
//
//public DefaultAlipayClient(String serverUrl, String appId, String privateKey)
//功能：构造方法
//输入：serverUrl 非空，请求服务器地址（调试：http://openapi.alipaydev.com/gateway.do 线上：https://openapi.alipay.com/gateway.do ）
//appId 非空，应用ID
//privateKey 非空，私钥
//输出：调用客户端实例对象
//
//
//public Object execute(AlipayRequest request)
//功能：执行请求调用（适用于不需要授权接口调用）
//输入：request 接口请求对象
//输出：Object  请求返回对象，需要调用方转换为返回实例对象。
//
//public Object execute(AlipayRequest request, String authToken)
//功能：执行请求调用（适用于需要授权接口调用）
//输入：request 接口请求对象
//authToken 授权令牌
//输出：Object  请求返回对象，需要调用方转换为返回实例对象。
//
//─────────
//3、调用示例
//─────────
//┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉
//请参考： com.alipay.api.test.PublicTest.java
//
//┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉
//
//─────────
//4、签名相关类
//─────────
//┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉
//
//AlipaySignature.java
//
//public static String rsaSign(Map params, String privateKey, String charset)
//功能：RSA签名
//输入：params 待签名参数map
//privateKey 私钥
//charset 签名编码格式
//输出：签名结果
//
//public static boolean rsaCheckV2(Map params, String publicKey, String charset)
//功能：RSA验签
//输入：params 签名参数内容map
//publicKey 公钥
//charset 签名编码格式
//输出：验签结果
//
//public static boolean rsaCheckContent(String content, String sign, String publicKey, String charset)
//功能：RSA验签
//输入：content 签名参数内容字符串
//sign 签名
//publicKey 公钥
//charset 签名编码格式
//输出：验签结果


}
