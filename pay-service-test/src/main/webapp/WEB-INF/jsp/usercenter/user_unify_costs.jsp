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

            <form action="unifyCosts" method="post" class="form-horizontal">
            <%-- <form action="${userCenterRegUri}" method="post" class="form-horizontal"> --%>
                <input type="hidden" name="unifyCostsUri" id="unifyCostsUri" value="${unifyCostsUri}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">appId</label>

                        <div class="col-sm-10">
                            <input type="text" name="appId" id="appId"
                                   class="form-control" ng-model="appId"/>

                            <p class="help-block">应用ID（必填）</p>
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
                        <label class="col-sm-2 control-label">sourceId</label>

                        <div class="col-sm-10">
                            <input type="text" name="sourceId" id="sourceId"
                                   class="form-control" ng-model="sourceId"/>

                            <p class="help-block">原业务系统用户id（必填）</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">serialNo</label>

                        <div class="col-sm-10">
                            <input type="text" name="serialNo" id="serialNo"
                                   class="form-control" ng-model="serialNo"/>

                            <p class="help-block">流水号</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">amount</label>

                        <div class="col-sm-10">
                            <input type="text" name="amount" id="amount"
                                   class="form-control" ng-model="amount"/>
                            <p class="help-block">扣费金额</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">userName</label>

                        <div class="col-sm-10">
                            <input type="text" name="userName" id="userName"
                                   class="form-control" ng-model="userName"/>
                            <p class="help-block">用户名</p>
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
                <button type="submit" class="btn btn-primary">调用统一扣费接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
		$scope.merchantOrderId="test20160517";
		$scope.appId="10026";
		$scope.merchantId="10001";
		$scope.serialNo="201607141433";
		$scope.amount="1";
		$scope.sourceId="36133476-3827-4188-AE4A-0B9DBFC6AC64";
        $scope.visible = false;
        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>
<script type="text/javascript">
	function btnSubmit(){
		var appId=$("#appId").val();
		var amount=$("amount").val();
		var merchantId=$("merchantId").val();
		var sourceId=$("sourceId").val();
		var userName=$("userName").val();
		var serialNo=$("serialNo").val();
	    var unifyCostsUri=$("#unifyCostsUri").val();
		if(amount==''){
		    alert("请输入amount业务方唯一订单号");
			return;
		}
		if(sourceId==''){
		    alert("请输入sourceId公共参数");
			return;
		}if(appId==''){
		    alert("请输入appId");
			return;
		}if(serialNo==''){
		    alert("请输入流水号");
			return;
		}if(merchantId==''){
		    alert("请输入商户号");
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
				    var regUri=unifyCostsUri+"?"+"merchantId="+merchantId+"&appId="+appId+"&amount="+amount+"&sourceId="+sourceId+"&userName="+userName+"&serialNo="+serialNo+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce;
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