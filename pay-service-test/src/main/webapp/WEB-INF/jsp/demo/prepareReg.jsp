<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>UserCenterPassword</title>
</head>
<body>

<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="prepareReg" method="post" class="form-horizontal">
            
                <div>
                    <div class="form-group">
                            <label class="col-sm-2 control-label">client_id</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="client_id" required="required"
                                       ng-model="clientId"/>

                                <p class="help-block">客户端从 Oauth Server 申请的client_id, 有的Oauth服务器中又叫 appKey</p>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">access_token</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="access_token" required="required"
                                       ng-model="accessToken"/>

                                <p class="help-block">客户端从Oauth Server 获得访问的token</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">grant_type</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="grant_type" readonly="readonly"
                                       ng-model="grantType"/>

                                <p class="help-block">固定值 'client_credentials'</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">scope</label>

                            <div class="col-sm-10">
                                <select name="scope" ng-model="scope" class="form-control">
                                    <option value="read">read</option>
                                    <option value="write">write</option>
                                    <option value="read,write">read,write</option>
                                </select>
                            </div>
                        </div>   
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">带着以上参数访问注册页面</button>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.userCenterPasswordUri = '${userCenterPasswordUri}';
        $scope.grantType = 'client_credentials';
        $scope.scope = 'read,write';

        $scope.clientId = 'b62c4d6b6d3b462d9af0194e241512dd';
        $scope.clientSecret='4c7a43395e9f4f3fa0b6639ca56bdf27';
        $scope.access_token='69a567a0-a841-49f5-9587-6aee021b8cdf';

        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>

<div>
	<p>接入用户中心注册接口说明：</p>
	<p>1。访问注册页面,并传入参数
	{ client_id	客户端从 Oauth Server 申请的client_id
	  client_secret   客户端从 Oauth Server 申请的client_secret
	  grant_type     固定值:'client_credentials'
	  scope         (read,write) }
	 </p>
	<p>2.输入用户信息点击登录，调用用户中心信息验证接口UserCenterVerify,查看接口点击 <a href="userCenterVerify">userCenterVerify</a></p>
	<p>3.如果返回json中status=0,则返回错误信息</p>
	<p>4.如果返回json中status=1,则调用用户中心注册接口UserCenterReg,查看接口点击 <a href="userCenterReg">userCenterReg</a></p>
	<p>5.如果返回json中status=0,则返回错误信息;如果status=1,则提交注册成功</p>
</div>
</body>
</html>