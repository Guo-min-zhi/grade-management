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
                    	公式设置
                    </h2>
                </div>
                <!-- /.col-lg-12 -->
            </div>
			
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">成绩类型列表</div>
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <thead>
                                    <tr class="success">
                                        <td>&nbsp;</td>
										<td>证书名称</td>
										<td>公式</td>
                                        <td>操作</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${pageObjects}" var="gradeType" varStatus="status">
										<tr id="row${status.count }">
											<td><c:out value="${status.count }"/></td>
											<td><c:out value="${gradeType.certificateName }" /></td>
											<td><c:out value="${gradeType.formula}" /></td>
											<td>
												<a class="btn btn-primary btn-xs edit" data-id="${gradeType.id}">
                                               		<span class="glyphicon glyphicon-pencil"></span>&nbsp;修改
                                               	</a>
											</td>
										</tr>
									</c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /.row -->
        </div>
    </div>
	<script type="text/javascript">
		$("#parameter").addClass("active");
		$("#parameter").children("ul").addClass("in");
	
		$('.edit').click(function(){
	   		var _this = $(this);
	   		$("#wrapper").simpleRequestForm({
	   			ajaxUrl:"${ctx}/admin/parameterManage/editFormula?id=" + $(this).attr("data-id") +"&ajax=true",
	   			addRules:function(){
	   				$('#formula').rules("add", {
	   					remote:{
	   						type:"get",
	   						url:"${ctx}/admin/parameterManage/checkFormula",
	   						data:{
	   							formula:function(){
	   								return $('#formula').val();
	   							}
	   						},
	   						dataType:"html",
	   						dataFilter:function(data, type){
	   							if(data == "true"){
	   								return true;
	   							}else{
	   								return false;
	   							}
	   						}
	   					},
	   					messages:{
	   						remote:"公式填写错误"
	   					}
	   				})
	   			}
	   		});
	   		
	   	});
	</script>
</body>
</html>
	