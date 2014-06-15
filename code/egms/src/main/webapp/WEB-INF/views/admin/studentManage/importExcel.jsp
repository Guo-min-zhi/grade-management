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
                    <h1 class="page-header">批量导入学生信息</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-info">
                        <div class="panel-heading">学生信息导入<a href="${ctx }/template/学生信息导入模板.xlsx">(下载学生信息模板)</a></div>
                        <div class="panel-body">
                            <form role="form" class="form-horizontal" enctype="multipart/form-data" method="post" action="${ctx }/admin/studentManage/doImport">
                                <div class="form-group">
                                    <label for="type" class="col-sm-2 control-label">选择文件：</label>
                                    <div class="col-sm-3">
                                        <input type="text" class="form-control" id="filename" disabled>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-3 col-sm-offset-2">
                                        <span class="btn btn-success fileinput-button">
                                            <i class="glyphicon glyphicon-plus"></i>
                                            <span>选择文件</span>
                                            <input type="file" name="file" id="file">
                                        </span>
                                    </div>
                                </div>
                                <div class="form-group">
                                     <div class="col-sm-3 col-sm-offset-2">
                                         <button type="submit" class="btn btn-primary">
                                         	<span class="glyphicon glyphicon-import"></span>&nbsp;导入
                                         </button>
                                     </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">导入结果</div>
                        <div class="panel-body" style="max-height: 550px;overflow-y: scroll;">
                            <c:if test="${not empty result }">
                            	<p class="label label-success">导入成功记录数：${result.successCount }</p>
                            	
                            	<c:if test="${not empty result.messages }">
                            		<h4>导入结果详细信息</h4>
                            		<hr>
                            		<c:forEach items="${result.messages }" var="msg" varStatus="status">
                            			<p>${status.count }. ${msg }</p>
                            		</c:forEach>
                            	</c:if>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
    	$('#file').change(function(){
    		$('#filename').val($('#file')[0].files[0].name)
    	});
    </script>
</body>
</html>