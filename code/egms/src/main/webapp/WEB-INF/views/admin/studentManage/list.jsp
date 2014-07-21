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
		<%@ include file="../component/sidebar.jsp" %>
		
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h2 class="page-header">
                    	学生管理
                    	<a class="btn btn-xs btn-success" id="create" title="添加学生">
				     		<span class="glyphicon glyphicon-plus-sign"></span>
				     	</a>
				     	<form action="${ctx }/admin/studentManage/exportExcel" method="post">
					     	<a href="${ctx }/admin/studentManage/batchImport" class="btn btn-primary btn-xs">
					     		<span class="glyphicon glyphicon-import"></span>&nbsp;批量导入
					     	</a>
							<button type="submit" class="btn btn-primary btn-xs">
								<span class="glyphicon glyphicon-export"></span>&nbsp;导出学生信息
							</button>
							<button type="button" class="btn btn-primary btn-xs" id="deletePatchBtn">
								<span class="glyphicon glyphicon-remove"></span>&nbsp;批量删除
							</button>
						</form>
                    </h2>
                </div>
                <!-- /.col-lg-12 -->
            </div>
			
			<script type="text/javascript">
				$('#create').click(function(){
		    		var _this = $(this);
		    		$("#wrapper").simpleRequestForm({
		    			ajaxUrl:"${ctx}/admin/studentManage/create?ajax=true"
		    		});
		    		
		    	});
				
				$('#fileupload').fileupload({
				    add: function (e, data) {
				        $.post('${ctx }/admin/studentManage/importExcel', function (d, s) {
				            data.formData = d; // e.g. {id: 123}
				            data.submit();
				        });
				    } 
				});
			</script>

            <div class="row">
            	<form:form method="get" modelAttribute="queryForm">
	                <div class="col-lg-12">
	                    <table class="searchTable">
	                        <tr>
	                            <td class="ItemLabel">学号</td>
	                            <td class="ItemControl">
	                                <form:input path="studentNum" class="Edit" />
	                            </td>
	                            <td class="ItemLabel">学院</td>
	                            <td class="ItemControl">
	                                <form:input path="college" class="Edit" />
	                            </td>
	                            <td class="ItemLabel">年级</td>
	                            <td class="ItemControl">
	                            	<form:input path="grade" class="Edit" />
	                            </td>
	                            <td class="ItemLabel">专业</td>
	                            <td class="ItemControl">
	                                <form:input path="major" class="Edit" />
	                            </td>
	                            <td>
	                                <button class="btn btn-primary btn-xs" type="submit">
	                                	<span class="glyphicon glyphicon-search"></span>&nbsp;查询
	                                </button>
	                            </td>
	                        </tr>
	                    </table>
	                </div>
                </form:form>
                <hr>
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">学生信息列表</div>
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <thead>
                                    <tr class="success">
                                    	<th style=" width: 20px; "><input type="checkbox" id="chooseAll" /></th>
                                        <td>&nbsp;</td>
                                        <td>学号</td>
                                        <td>姓名</td>
                                        <td>学院</td>
                                        <td>专业</td>
                                        <td>操作</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${pageObjects.content}" var="student"	varStatus="status">
										<tr id="row${status.count }">
											<td><input type="checkbox" class="checkTr" value="${student.id }"></td>
											<td><c:out value="${status.count }"/></td>
											<td><c:out value="${student.loginName }" /></td>
											<td><c:out value="${student.name }" /></td>
											<td><c:out value="${student.college }" /></td>
											<td><c:out value="${student.major }" /></td>
											<td>
												<a class="btn btn-primary btn-xs edit" data-id="${student.id}">
                                               		<span class="glyphicon glyphicon-pencil"></span>&nbsp;修改
                                               	</a>
												&nbsp;
												<a href="${ctx }/admin/studentManage/delete/${student.id}" class="btn btn-warning btn-xs delete-item">
													<span class="glyphicon glyphicon-remove"></span>&nbsp;删除
												</a>
											</td>
										</tr>
									</c:forEach>
                                </tbody>
                            </table>
                            
                            <%@ include file="/WEB-INF/views/component/pagination.jsp"%>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /.row -->
        </div>
    </div>
	<script type="text/javascript">
		$('.edit').click(function(){
	   		var _this = $(this);
	   		$("#wrapper").simpleRequestForm({
	   			ajaxUrl:"${ctx}/admin/studentManage/edit?id=" + $(this).attr("data-id") +"&ajax=true",
	   			addRules: function (){
	   				$('#resetPassword').click(function(){
	   					$.ajax({
	   						url: '${ctx}/admin/studentManage/resetPassword',
	   						type: 'get',
	   						datatype: 'html',
	   						data: {
	   							id: $('#id').val()
	   						},
	   						success: function(data){
	   							if('true' == data){
	   								createAlertMessage('重置密码成功。');
	   							}else{
	   								createAlertMessage('重置密码失败。');
	   							}
	   						}
	   					});
	   				});
	   			}
	   		});
	   	});
		
		$(function(){
			//负责checkbox的全选和全不选
	    	$("#chooseAll").change(function(){
				if($("#chooseAll")[0].checked){
					$("input[type='checkbox']").prop("checked", true);
				}else{
					$("input[type='checkbox']").prop("checked", false);
				}
			});
	    	//点击“批量删除”按钮发生的事件
			$("#deletePatchBtn").click(function(){
				var students = new Array();
	    		$.each($(".checkTr"), function(i, o){
	    			if(o.checked){
	    				students.push($(o).prop("value"));
	    			}
	    		});
	    		var password = prompt("删除需要管理员密码 ");
	    		$.ajax({
					async: false,
					type: "POST",
					url: "${ctx}/teacher/validateP",
					data: {
						password: password
					},
					success: function(pass){
			    		if(pass){
			    			$.ajax({
			    				type: "post",
			    				url: "${ctx}/admin/studentManage/delete",
			    				data: {
			    					students: students.join(",")
			    				},
			    				success: function(data){
			    		    		window.location.reload();
			    				},
			    				error: function(data){
			    					alert("删除出现错误！");
			    				},
			    			});	
			    		}else{
			    			if(password != null){
				    			alert('删除密码错误！');
			    			}
			    		}
					},
					error: function(data){
						alert("删除出现错误！");
					},
				});
			});
		});
	</script>
</body>
</html>
	