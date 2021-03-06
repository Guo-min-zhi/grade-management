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
                    	教师管理
                    	<a class="btn btn-xs btn-success" id="create" title="添加教师">
				     		<span class="glyphicon glyphicon-plus-sign"></span>
				     	</a>
				     	
				     	<form action="${ctx }/admin/teacherManage/exportExcel" method="post">
					     	<a href="${ctx }/admin/teacherManage/batchImport" class="btn btn-primary btn-xs">
					     		<span class="glyphicon glyphicon-import"></span>&nbsp;批量导入
					     	</a>
							<button type="submit" class="btn btn-primary btn-xs">
								<span class="glyphicon glyphicon-export"></span>&nbsp;导出教师信息
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
		    			ajaxUrl:"${ctx}/admin/teacherManage/create?ajax=true"
		    		});
		    		
		    	});
			</script>

            <div class="row">
            	<form:form method="get" modelAttribute="queryForm">
	                <div class="col-lg-12">
	                    <table class="searchTable">
	                        <tr>
	                            <td class="ItemLabel">工号</td>
	                            <td class="ItemControl">
	                                <form:input path="loginName" class="Edit" />
	                            </td>
	                            <td class="ItemLabel">姓名</td>
	                            <td class="ItemControl">
	                                <form:input path="name" class="Edit" />
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
                        <div class="panel-heading">教师信息列表</div>
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <thead>
                                    <tr class="success">
                                        <td>&nbsp;</td>
                                        <td>工号</td>
                                        <td>姓名</td>
                                        <td>操作</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${pageObjects.content}" var="teacher"	varStatus="status">
										<tr id="row${status.count }">
											<td><c:out value="${status.count }"/></td>
											<td><c:out value="${teacher.loginName }" /></td>
											<td><c:out value="${teacher.name }" /></td>
											<td>
												<a class="btn btn-primary btn-xs edit" data-id="${teacher.id}">
                                               		<span class="glyphicon glyphicon-pencil"></span>&nbsp;修改
                                               	</a>
												&nbsp;
												<a href="${ctx }/admin/teacherManage/delete/${teacher.id}" class="btn btn-warning btn-xs delete-item">
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
	   			ajaxUrl:"${ctx}/admin/teacherManage/edit?id=" + $(this).attr("data-id") +"&ajax=true",
	   			addRules: function (){
	   				$('#resetPassword').click(function(){
	   					$.ajax({
	   						url: '${ctx}/admin/teacherManage/resetPassword',
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
	</script>
</body>
</html>
	