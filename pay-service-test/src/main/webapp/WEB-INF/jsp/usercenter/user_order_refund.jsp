<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>user-order-query(interface)</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="${contextPath}/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="orderRefund" method="post" class="form-horizontal">
            <%-- <form action="${userCenterRegUri}" method="post" class="form-horizontal"> --%>
                <input type="hidden" name="orderRefundUri" id="orderRefundUri" value="${orderRefundUri}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">outTradeNo</label>

                        <div class="col-sm-10">
                            <input type="text" name="merchantOrderId" id="merchantOrderId"
                                   class="form-control" ng-model="merchantOrderId"/>

                            <p class="help-block">商户订单号（必填）</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">merchantId</label>

                        <div class="col-sm-10">
                            <input type="text" name="merchantId" id="merchantId"
                                   class="form-control" ng-model="merchantId"/>

                            <p class="help-block">商户号</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">refundMoney</label>

                        <div class="col-sm-10">
                            <input type="text" name="refundMoney" id="refundMoney"
                                   class="form-control" ng-model="refundMoney"/>

                            <p class="help-block">退款金额（必填）</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">remark</label>

                        <div class="col-sm-10">
                            <input type="text" name="remark" id="remark"
                                   class="form-control" ng-model="remark"/>

                            <p class="help-block">备注</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">sourceUid</label>

                        <div class="col-sm-10">
                            <input type="text" name="sourceUid" id="sourceUid"
                                   class="form-control" ng-model="sourceUid"/>
                            <p class="help-block">业务方用户id</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">sourceUsername</label>

                        <div class="col-sm-10">
                            <input type="text" name="sourceUsername" id="sourceUsername"
                                   class="form-control" ng-model="sourceUsername"/>
                            <p class="help-block">用户名</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">realName</label>

                        <div class="col-sm-10">
                            <input type="text" name="realName" id="realName"
                                   class="form-control" ng-model="realName"/>

                            <p class="help-block">退款人姓名</p>
                        </div>
                    </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label">phone</label>

                        <div class="col-sm-10">
                            <input type="text" name="phone" id="phone"
                                   class="form-control" ng-model="phone"/>

                            <p class="help-block">手机号</p>
                        </div>
                    </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label">goodsId</label>

                        <div class="col-sm-10">
                            <input type="text" name="goodsId" id="goodsId"
                                   class="form-control" ng-model="goodsId"/>

                            <p class="help-block">退款商品id</p>
                        </div>
                    </div>
                    <div class="form-group">  
                        <label class="col-sm-2 control-label">appId</label>

                        <div class="col-sm-10">
                            <input type="text" name="appId" size="50" id="appId" class="form-control"
                                   ng-model="appId"/>

                            <p class="help-block">应用Id,23-测试应用Id</p>
                        </div>
                    </div>
                    <div class="well well-sm">
                         <span class="text-muted">最终发给 pay-service-server的 URL:</span>
                        <br/>

                        <div class="text-primary" id="userOrderRefundUri"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用订单退款接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
		$scope.merchantOrderId="test20160517";
		$scope.appId="23";
		$scope.refundMoney="10";
        $scope.visible = false;
        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>
<script type="text/javascript">
	function btnSubmit(){
		var merchantOrderId=$("#merchantOrderId").val();
		var appId=$("#appId").val();
		var refundMoney=$("refundMoney").val();
		var merchantId=$("merchantId").val();
		var remark=$("remark").val();
		var sourceUid=$("sourceUid").val();
		var sourceUsername=$("sourceUsername").val();
		var realName=$("realName").val();
		var phone=$("phone").val();
		var goodsId=$("goodsId").val();
	    var orderRefundUri=$("#orderRefundUri").val();
		if(outTradeNo==''){
		    alert("请输入outTradeNo业务方唯一订单号");
			return;
		}
		if(appId==''){
		    alert("请输入appId公共参数");
			return;
		}if(refundMoney==''){
		    alert("请输入退款金额");
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
				    var regUri=orderRefundUri+"?"+"merchantOrderId="+merchantOrderId+"&appId="+appId+"&refundMoney="+refundMoney+"&merchantId="+merchantId+"&remark="+remark+"&sourceUid="+sourceUid+"&sourceUsername="+sourceUsername+"&realName="+realName+"&phone="+phone+"&goodsId="+goodsId+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce;
					$("#userOrderRefundUri").html(regUri);
				}else{
				    jQuery("#userOrderRefundUri").html('无效数据，请重新申请');
				}
			}
 		);
	}
</script>

</body>
</html>