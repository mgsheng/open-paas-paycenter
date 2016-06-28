<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>dynamicLogin(interface)</title>
</head>
<body>
<a href="${contextPath}/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <!-- <form action="userCenterReg" method="post" class="form-horizontal"> -->
            <form action="${dynamicLogin}" method="post" class="form-horizontal">
                <input type="hidden" name="dynamicLogin" value="${dynamicLogin}"/>

                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                  <div class="form-group">
                        <label class="col-sm-2 control-label">grant_type</label>

                        <div class="col-sm-10">
                            <input type="text" name="grant_type" readonly="readonly"
                                   class="form-control" ng-model="grant_type"/>

                            <p class="help-block">固定值 'client_credentials'<font color="red">(必传)</font></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">client_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="client_id"
                                   class="form-control" ng-model="client_id"/>
                        </div>
                        <p class="help-block" style="margin-left: 170px">客户端从Oauth Server申请的client_id<font color="red">(必传)</font></p>
                    </div>
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">access_token</label>

                        <div class="col-sm-10">
                            <input type="text" name="access_token"
                                   class="form-control" ng-model="access_token"/>
                        </div>
                        <p class="help-block"  style="margin-left: 170px">客户端从Oauth Server 获得访问的token<font color="red">(必传)</font></p>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">scope</label>

                        <div class="col-sm-10">
                            <select name="scope" ng-model="scope" class="form-control" >
                                <option value="read">read</option>
                                <option value="write">write</option>
                                <option value="read,write">read,write</option>
                            </select>
                           
                        </div>
                         <p class="help-block"  style="margin-left: 170px">权限（read，write） <font color="red">(必传)</font></p>
                    </div>                    
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">source_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="source_id" class="form-control"
                                   size="50" ng-model="source_id"/>

                            <p class="help-block">OES学习平台用户id <font color="red">(必传)</font></p>
                        </div>
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用动态密码登录接口</button>
                <%--
                <span class="text-muted">将重定向到 'spring-oauth-server' 的注册页面</span>
            --%>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.dynamicLogin = '${dynamicLogin}';
        $scope.grant_type = 'client_credentials';
        $scope.scope = 'read,write';
        $scope.client_id = 'b62c4d6b6d3b462d9af0194e241512dd';
        //$scope.clientSecret='4c7a43395e9f4f3fa0b6639ca56bdf27';
        $scope.access_token="e23b86ea-1d4d-427c-a3da-c13a56c87192";
        $scope.source_id='2';
        $scope.nickname="xiaogang";        
        $scope.real_name="小刚";
        $scope.identify_type="1";
        $scope.identify_no="130629199003150366";
        $scope.user_type="2";
        $scope.sex="1";  

        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>

</body>
</html>