<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.logging.SimpleFormatter"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/common/styles.jsp"%>
<%@ include file="/WEB-INF/views/common/scripts.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>外语成绩认定系统——登录</title>
	
	<script type="text/javascript">
		function reloadValidateCode(){
			$("#validateCodeImg").attr("src","${ctx}/validateCode?data=" + new Date() + Math.floor(Math.random()*24));
		}
		$(document).ready(function(){
			$(":input[placeholder]").placeholder();
		})
	</script>
</head>

<body style="background-image:url(${staticFile}/img/bg.png);">
	<%-- <div style="height:80px;width:100%;background:url(${staticFile}/img/banner.png);background-repeat:no-repeat;">
	</div> --%>
	<div class="bs-header" id="content">
		<div class="container">
			<h1>英语水平考试证书认定系统</h1>
		</div>
	</div>
	<div class="container">
        <div class="row">
        	<div class="col-md-6 col-md-offset-1">
	       		<img src="${staticFile }/img/exam.jpg" alt="..." class="img-thumbnail">
        	</div>
            <div class="col-md-4">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">英语水平考试证书认定系统</h3>
                    </div>
                    <div class="panel-body">
						<form:form id="loginForm" action="${ctx}/login" method="post" modelAttribute="loginForm" role="form">
							<fieldset>
								<div class="form-group">
									<form:input path="loginName" placeholder="用户名" cssClass="form-control" />
								</div>
								<div class="form-group">
									<form:password path="password" placeholder="密码" cssClass="form-control" />
								</div>
								<div class="form-group">
									<form:select path="roleName" cssClass="form-control">
										<form:options items="${allRoleType }" itemLabel="roleName"
											itemValue="roleCode" />
									</form:select>
								</div>
								<div class="form-group">
									<div class="row">
										<div class="col-sm-6">
											<form:input path="validateCode" placeholder="验证码" cssClass="form-control" />
										</div>
										<div class="col-sm-6">
											<img id="validateCodeImg" src="${ctx}/validateCode"
												style="width: 80px; height: 34px;"> <a href="#"
												onclick="javascript:reloadValidateCode();">看不清？</a>
										</div>
									</div>
								</div>
								<div class="form-group">
									<button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
								</div>
							</fieldset>
						</form:form>

                    </div>
                </div>
            </div>
        </div>
		<%
			String error = (String) request.getAttribute("loginFailure");
			if (error != null) {
		%>
		<div class="alert alert-danger alert-dismissable"
			style="margin-bottom: 0px;">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			<strong><%=error%></strong>
		</div>
		<%
			}
		%>
	</div>
	
	<%-- <div class="container border" style="background-color: white; width:700px;height:420px;background-size:cover;">
		<%
			String error = (String)request.getAttribute("loginFailure");
			if(error != null){
		%>
			<div class="alert alert-danger alert-dismissable" style="margin-bottom: 0px;">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong><%=error %></strong>
			</div>
		<%
			}
		%>
	</div> --%>
	<script type="text/javascript">
		$('#loginForm').validate();
	</script>
</body>
</html>
