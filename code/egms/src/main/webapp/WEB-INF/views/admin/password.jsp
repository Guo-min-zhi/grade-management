<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>英语成绩管理系统</title>
    <%@ include file="/WEB-INF/views/common/styles.jsp"%>
    <%@ include file="/WEB-INF/views/common/scripts.jsp"%>
    
</head>

<body>

    <div id="wrapper">
		<%@ include file="/WEB-INF/views/component/header.jsp" %>
		<%@ include file="component/sidebar.jsp" %>
		
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h2 class="page-header">
                    	修改密码
                    </h2>
                </div>
                <!-- /.col-lg-12 -->
            </div>
			
            <div class="row">
            	<div class="col-lg-12">
					<form id="password" class="form-horizontal" role="form"
						method="post" action="${ctx }/admin/modifyPassword/modify">
						<div class="form-group">
							<label for="oldPassword" class="col-sm-2 control-label">原密码</label>
							<div class="col-sm-6">
								<input type="password" class="form-control" id="oldPassword"
									placeholder="原密码" name="oldPassword" required>
							</div>
						</div>
						<hr />
						<div class="form-group">
							<label for="newPassword" class="col-sm-2 control-label">新密码</label>
							<div class="col-sm-6">
								<input type="password" class="form-control" id="newPassword"
									placeholder="新密码" name="newPassword" required>
							</div>
						</div>
						<div class="form-group">
							<label for="againPassword" class="col-sm-2 control-label">确认密码</label>
							<div class="col-sm-6">
								<input type="password" class="form-control" id="againPassword"
									placeholder="确认密码" name="againPassword" required>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<button type="submit" class="btn btn-primary">修改</button>
							</div>
						</div>
						<div id="alertMessage"></div>
						<c:if test="${result == '1'}">
							<div class="alert alert-success alert-dismissable">
								<button type="button" class="close" data-dismiss="alert"
									aria-hidden="true">&times;</button>
								<strong>成功!</strong> 修改密码成功.
							</div>
						</c:if>
						<c:if test="${result == '0'}">
							<div class="alert alert-danger alert-dismissable">
								<button type="button" class="close" data-dismiss="alert"
									aria-hidden="true">&times;</button>
								<strong>失败!</strong> 修改密码失败.
							</div>
						</c:if>
					</form>
				</div>
            </div>
            
        </div>
    </div>
	<script type="text/javascript">
	$(function(){
		// expand the sidebar.
		$("#personalInfo").addClass("active");
		$("#personalInfo").children("ul").addClass("in");
		
		// validate the password.
    	$('#password').validate({
    		onkeyup:true,
    		submitHandler:function(form){
    			var newPassword = $("#newPassword").val();
    			var againPassword = $("#againPassword").val();
    			if(newPassword != againPassword){
    				var html = '<div class="alert alert-danger alert-dismissable">'+
    				  '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>'+
    				  '两次输入密码不一致.</div>';
    				$("#alertMessage").html(html);
    				return;
    			}
    			form.submit();
    		}
    	});
	});
	</script>
</body>
</html>
	