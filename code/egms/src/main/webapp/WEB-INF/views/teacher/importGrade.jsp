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
                    <h1 class="page-header">成绩导入</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-info">
                        <div class="panel-heading">成绩导入</div>
                        <div class="panel-body">
                            <form role="form" action="${ctx }/teacher/uploadGradeExcel" class="form-horizontal" enctype="multipart/form-data" method="post">
                                <div class="form-group">
                                     <label for="type" class="col-sm-2 control-label">导入类型：</label>
                                     <div class="col-sm-3">
                                         <select name="ct" id="ct" class="form-control">
                                             <option value="">--请选择成绩类型--</option>
                                             <c:forEach items="${cts }" var="ct">
                                             	<option value="${ct.id }">${ct.certificateName }</option>
                                             </c:forEach>
                                         </select>
                                     </div>
                                </div>
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
                            <c:forEach items="${cts }" var="ct">
                            	<input type="hidden" id="${ct.id }" value="${ct.formula }">
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">信息</div>
                        <div class="panel-body">
                            <p>综合成绩计算公式：</p>
                            <p id="formula"></p>
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
	