<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>user-unify-pay(interface)</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="${contextPath}/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="savePayOrderDetail" method="post" class="form-horizontal" target="_blank">
            <%-- <form action="${userCenterRegUri}" method="post" class="form-horizontal"> --%>
                <input type="hidden" name="savePayOrderDetailUri" id="savePayOrderDetailUri" value="${savePayOrderDetailUri}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">regNumber</label>

                        <div class="col-sm-10">
                            <input type="text" name="regNumber" id="regNumber"
                                   class="form-control" ng-model="regNumber"/>

                            <p class="help-block">报名编号</p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">learningCenterCode</label>

                        <div class="col-sm-10">
                            <input type="text" name="learningCenterCode" id="learningCenterCode"
                                   class="form-control" ng-model="learningCenterCode"/>
                           <p class="help-block">学习中心代码</p>
                        </div>
                    </div>
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">learningCenterName</label>

                        <div class="col-sm-10">
                            <input type="text" name="learningCenterName" id="learningCenterName"
                                   class="form-control" ng-model="learningCenterName"/>
                                    <p class="help-block">学习中心名称</p>
                        </div>
                        
                    </div>
                    
                                     
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">province</label>

                        <div class="col-sm-10">
                            <input type="text" name="province" id="province" class="form-control"
                                   size="50" ng-model="province"/>

                            <p class="help-block">省份</p>
                        </div>
                    </div>

					<div class="form-group">  
                        <label class="col-sm-2 control-label">learningBatches</label>

                        <div class="col-sm-10">
                            <input type="text" name="learningBatches" size="50" id="learningBatches" class="form-control"
                                   ng-model="learningBatches"/>

                            <p class="help-block">入学批次</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">learningStatus</label>

                        <div class="col-sm-10">
                            <input type="text" name="learningStatus" id="learningStatus" size="50" class="form-control"
                                   ng-model="learningStatus"/>
                            <p class="help-block">学籍状态</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">specialty</label>

                        <div class="col-sm-10">
                            <input type="text" name="specialty" id="specialty"size="50" class="form-control"
                                   ng-model="specialty"/>
                                    <p class="help-block">专业</p>
                        </div> 
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">studentName</label>

                        <div class="col-sm-10">
                            <input type="text" name="studentName" id="studentName" size="50" class="form-control"
                                   ng-model="studentName"/>

                            <p class="help-block">学生姓名</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">cardNo</label>

                        <div class="col-sm-10">
                            <input type="text" name="cardNo" id="cardNo" size="50" class="form-control"
                                   ng-model="cardNo"/>

                            <p class="help-block">证件编号</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">studentId</label>

                        <div class="col-sm-10">
                            <input type="text" name="studentId" id="studentId" size="50" class="form-control"
                                   ng-model="studentId"/>

                            <p class="help-block">院校学号</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">costAttribution</label>

                        <div class="col-sm-10">
                            <input type="text" name="costAttribution" id="costAttribution" size="50" class="form-control"
                                   ng-model="costAttribution"/>

                            <p class="help-block">资金归属</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">exacct</label>

                        <div class="col-sm-10">
                            <input type="text" name="exacct" id="exacct" size="50" class="form-control"
                                   ng-model="exacct"/>

                            <p class="help-block">费用科目</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">payAmount</label>

                        <div class="col-sm-10">
                            <input type="text" name="payAmount" id="payAmount" size="50" class="form-control" ng-model="payAmount"/>

                            <p class="help-block">缴费金额</p>
                        </div>
                    </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label">payCharge</label>

                        <div class="col-sm-10">
                            <input type="text" name="payCharge" id="payCharge" size="50" class="form-control"
                                   ng-model="payCharge"/>

                            <p class="help-block">手续费</p>
                        </div>
                    </div> 
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">payDate</label>

                        <div class="col-sm-10">
                            <input type="text" name="payDate" id="payDate" size="50" class="form-control"
                                   ng-model="payDate"/>

                            <p class="help-block">缴费时间</p>
                        </div>
                    </div> 
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">payChannel</label>

                        <div class="col-sm-10">
                            <input type="text" name="payChannel" id="payChannel" size="50" class="form-control" ng-model="payChannel"/>

                            <p class="help-block">缴费渠道</p>
                        </div>
                    </div> 
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">projectNumber</label>

                        <div class="col-sm-10">
                            <input type="text" name="projectNumber" id="projectNumber" size="50" class="form-control"
                                   ng-model="projectNumber"/>

                            <p class="help-block">项目编号</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">projectName</label>

                        <div class="col-sm-10">
                            <input type="text" name="projectName" id="projectName" size="50" class="form-control"
                                   ng-model="projectName"/>

                            <p class="help-block">项目名称</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">appId</label>

                        <div class="col-sm-10">
                            <input type="text" name="appId" id="appId" size="50" class="form-control"
                                   ng-model="appId"/>

                            <p class="help-block">扩展字段（可以传递自定义参数，支付成功后会回传）</p>
                        </div>
                    </div>
                       <div class="form-group">
                        <label class="col-sm-2 control-label">level</label>

                        <div class="col-sm-10">
                            <input type="text" name="level" id="level" size="50" class="form-control"
                                   ng-model="level"/>

                            <p class="help-block">层次</p>
                        </div>
                    </div>
                       <div class="form-group">
                        <label class="col-sm-2 control-label">managerCenterName</label>

                        <div class="col-sm-10">
                            <input type="text" name="managerCenterName" id="managerCenterName" size="50" class="form-control"
                                   ng-model="managerCenterName"/>

                            <p class="help-block">管理中心名称</p>
                        </div>
                    </div>
                        <div class="form-group">
                        <label class="col-sm-2 control-label">merchantOrderId</label>

                        <div class="col-sm-10">
                            <input type="text" name="merchantOrderId" id="merchantOrderId" size="50" class="form-control"
                                   ng-model="merchantOrderId"/>

                            <p class="help-block">商户订单号</p>
                        </div>
                    </div>
                    
                    <div class="well well-sm">
                         <span class="text-muted">最终发给 pay-service-server的 URL:</span>
                        <br/>

                        <div class="text-primary" id="userUnifyPayUri"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用保存订单详细信息接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
                
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
		$scope.regNumber="2016050621001004000254435138";
		$scope.learningCenterCode="C0255601";
		$scope.learningCenterName="安徽安庆奥鹏学习中心[1]";
		$scope.province="安徽省";
		$scope.learningBatches="1703";
		$scope.learningStatus="在籍";
		$scope.specialty="项目管理";
		$scope.studentName="李四";
		$scope.cardNo="142114198111111111";
		$scope.studentId="10001";
		$scope.costAttribution="2017年春季";
		$scope.exacct="教材费";
		$scope.payAmount="0.01";
		$scope.payCharge="0.01";
		$scope.payDate="20170-03-10 16:36:10";
		$scope.payChannel="工商渠道";
		$scope.projectNumber="OESSTUDENT";
		$scope.projectName="oes教学";
		$scope.merchantOrderId="M20160803000389";
		$scope.appId="1";
		$scope.level="高起专";
		$scope.managerCenterName="CNY";
        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>
<script type="text/javascript">
	function btnSubmit(){
		var outTradeNo=$("#outTradeNo").val();
		var userName=$("#userName").val();
		var userId=$("#userId").val();
		var appId=$("#appId").val();
		var merchantId=$("#merchantId").val();
		var goodsId=$("#goodsId").val();
		var goodsName=$("#goodsName").val();
		var goodsTag=$("#goodsTag").val();
		var goodsDesc=$("#goodsDesc").val();
		var showUrl=$("#showUrl").val();	
		var buyerRealName=$("#buyerRealName").val();
		var buyerCertNo=$("#buyerCertNo").val();
		var inputCharset=$("#inputCharset").val();
		var paymentOutTime=$("#paymentOutTime").val();
		var paymentType=$("#paymentType").val();
		var paymentChannel=$("#paymentChannel").val();
		var totalFee=$("#totalFee").val();
		var feeType=$("#feeType").val();
		var clientIp=$("#clientIp").val();
		var parameter=$("#parameter").val();
		var userUnifyPayUri=$("#unifyPayUri").val();
		
		if(outTradeNo==''){
		    alert("请输入outTradeNo业务方唯一订单号");
			return;
		}if(userId==''){
		    alert("请输入userId业务方用户Id");
			return;
		}
		if(merchantId==''){
		    alert("请输入merchantId商户号");
			return;
		}
		if(appId==''){
		    alert("请输入appId公共参数");
			return;
		}
		if(goodsName==''){
		    alert("请输入goodsName商品名称");
			return;
		}
		if(totalFee==''){
		    alert("请输入totalFee订单金额（输入整数）");
			return;
		}
		$.post("${contextPath}/getSignature",
			{
				appId:appId
			},
			function(data){
				if(data.flag){
				    var signature=data.signature;
				    var timestamp=data.timestamp;
				    var signatureNonce=data.signatureNonce;
				    var regUri=userUnifyPayUri+"?"+"outTradeNo="+outTradeNo+"&userName="+userName+"&userId="+userId+"&appId="+appId+"&merchantId="+merchantId+"&goodsId="+goodsId+"&goodsName="+goodsName+"&goodsDesc="+goodsDesc+"&goodsTag="+goodsTag+"&showUrl="+showUrl+"&buyerRealName="+buyerRealName+"&buyerCertNo="+buyerCertNo+"&inputCharset="+inputCharset+"&paymentOutTime="+paymentOutTime+"&paymentType="+paymentType+"&paymentChannel="+paymentChannel+"&totalFee="+totalFee+"&feeType="+feeType+"&parameter="+parameter+"&clientIp="+clientIp+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce;
					$("#userUnifyPayUri").html(regUri);
				}else{
				    jQuery("#userUnifyPayUri").html('无效数据，请重新申请');
				}
			}
 		);
	}
	
</script>

</body>
</html>