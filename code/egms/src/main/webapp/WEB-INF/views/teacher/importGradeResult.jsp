<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>英语成绩管理系统</title>
    <%@ include file="../common/styles.jsp"%>
    <%@ include file="../common/scripts.jsp"%>
    
</head>

<body>

    <div id="wrapper">
		<%@ include file="/WEB-INF/views/component/header.jsp" %>
		<%@ include file="component/sidebar.jsp" %>
		
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">成绩导入结果</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-info">
                        <div class="panel-heading">导入结果</div>
                        <div class="panel-body" style="max-height: 550px;overflow-y: scroll;">
                        	<c:if test="${not empty errorMsg }">
                        		<p class="label label-danger">${errorMsg }</p>
                        	</c:if>
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
    	
    	$('#ct').change(function(){
    		$('#formula').html($('#'+$('#ct').val()).val());
    	})
    </script>
</body>
</html>
	