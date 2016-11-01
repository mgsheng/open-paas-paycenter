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

            <form action="accountDownlond" method="post" class="form-horizontal">
            <%-- <form action="${userCenterRegUri}" method="post" class="form-horizontal"> --%>
                <input type="hidden" name="orderQueryUri" id="orderQueryUri" value="${accountDownlondUri}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <!-- <div class="form-group">
                        <label class="col-sm-2 control-label">outTradeNo</label>

                        <div class="col-sm-10">
                            <input type="text" name="outTradeNo" id="outTradeNo"
                                   class="form-control" ng-model="outTradeNo"/>

                            <p class="help-block">业务方唯一订单号（必填）</p>
                        </div>
                    </div> -->
                    <div class="form-group">
                        <label class="col-sm-2 control-label">merchantId</label>

                        <div class="col-sm-10">
                            <input type="text" name="merchantId" id="merchantId"
                                   class="form-control" ng-model="merchantId"/>

                            <p class="help-block">商户号</p>
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
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">startTime</label>

                        <div class="col-sm-10">
                            <input type="text" name="startTime" id="startTime"
                                   class="form-control" ng-model="startTime"/>

                            <p class="help-block">开始时间（必填）格式：yyyy-MM-dd HH:mm:ss</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">endTime</label>

                        <div class="col-sm-10">
                            <input type="text" name="endTime" id="endTime"
                                   class="form-control" ng-model="endTime"/>

                            <p class="help-block">结束时间 yyyy-MM-dd HH:mm:ss</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">businessType</label>

                        <div class="col-sm-10">
                            <!-- <input type="text" name="paymentChannel" id="paymentChannel" size="50" class="form-control"
                                   ng-model="paymentChannel"/> -->
							<select name="marking" id="marking" class="form-control" ng-mode="businessType">
								<option value="1">txt</option>
								<option value="2">excel</option>
							</select>
                            <p class="help-block">对账单文件格式（1：txt 2:excel）</p>
                        </div>
                    </div>
                    
                    
                    <div class="well well-sm">
                         <span class="text-muted">最终发给 pay-service-server的 URL:</span>
                        <br/>

                        <div class="text-primary" id="userOrderQueryUri"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用订单查询接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
		//$scope.outTradeNo="test201607151059";
		$scope.merchantId="10001";
		$scope.appId="10026";
		$scope.startTime="2016-05-18 16:03:25";
		$scope.endTime="2016-07-28 16:03:25";
        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>
<script type="text/javascript">
	function btnSubmit(){
		//var outTradeNo=$("#outTradeNo").val();
		var merchantId=$("#merchantId").val();
		var appId=$("#appId").val();
	    var orderQueryUri=$("#orderQueryUri").val();
	    var startTime=$("#startTime").val();
	    var endTime=$("#endTime").val();
	    var marking=$("#marking").val();
		if(merchantId==''){
		    alert("请输入merchantId商户号");
			return;
		}
		if(appId==''){
		    alert("请输入appId公共参数");
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
					var regUri=orderQueryUri+"?"+"merchantId="+merchantId+"&appId="+appId+"&startTime="+startTime+"&endTime="+endTime+"&marking="+marking+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
					$("#userOrderQueryUri").html(regUri);
				}else{
				    jQuery("#userOrderQueryUri").html('无效数据，请重新申请');
				}
			}
 		);
	}
</script>

</body>
</html>