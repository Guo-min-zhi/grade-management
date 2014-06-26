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
                    <h2 class="page-header">字段匹配</h2>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-info">
                        <div class="panel-heading"><c:out value="${hints }"></c:out></div>
                        <div class="panel-body">
                        	<div class="col-sm-3">
                        		<table class="table table-bordered">
                        			<thead>
	                                    <tr class="success">
	                                        <td>序号</td>
	                                        <td>Excel字段名称</td>
	                                    </tr>
	                                </thead>
	                                <tbody>
	                                	<c:forEach items="${fields }" var="field" varStatus="status">
	                                		<tr>
		                                		<td>${status.count-1 }</td>
		                                		<td>${field }</td>
	                                		</tr>
	                                	</c:forEach>
	                                </tbody>
                        		</table>
                        	</div>
                        	<div class="col-sm-6">
                        		<form action="${ctx }/teacher/doImportGrade" method="post">
                        			<table class="table table-bordered">
                        				<thead>
		                                    <tr class="success">
		                                        <td>序号</td>
		                                        <td>数据库表字段名称</td>
		                                        <td>对应Excel表字段（填写字段序号） </td>
		                                    </tr>
		                                </thead>
		                                <tbody>
		                                	<tr>
		                                		<td>0</td>
		                                		<td>学号(*)</td>
		                                		<td><input type="text" name="studentNum" class="required"></td>
		                                	</tr>
		                                	<tr>
		                                		<td>1</td>
		                                		<td>姓名</td>
		                                		<td><input type="text" name="studentName"></td>
		                                	</tr>
		                                	<tr>
		                                		<td>2</td>
		                                		<td>四六级考试成绩</td>
		                                		<td><input type="text" name="sourceScore"></td>
		                                	</tr>
		                                	<tr>
		                                		<td>3</td>
		                                		<td>第一学期成绩</td>
		                                		<td><input type="text" name="gradeA"></td>
		                                	</tr>
		                                	<tr>
		                                		<td>4</td>
		                                		<td>第二学期成绩</td>
		                                		<td><input type="text" name="gradeB"></td>
		                                	</tr>
		                                	<tr>
		                                		<td>5</td>
		                                		<td>第三学期成绩</td>
		                                		<td><input type="text" name="gradeC"></td>
		                                	</tr>
		                                	<tr>
		                                		<td>6</td>
		                                		<td>口语成绩</td>
		                                		<td><input type="text" name="oralScore"></td>
		                                	</tr>
		                                	<tr>
		                                		<td>7</td>
		                                		<td>笔试成绩</td>
		                                		<td><input type="text" name="writtenScore"></td>
		                                	</tr>
		                                	<tr>
		                                		<td>&nbsp;</td>
		                                		<td colspan="2">
		                                			<button type="submit" class="btn btn-primary btn-xs">
			                                        	<span class="glyphicon glyphicon-import"></span>&nbsp;导入
			                                        </button>
		                                			<button type="reset" class="btn btn-primary btn-xs">
		                                				<span class="glyphicon glyphicon-refresh"></span>&nbsp;重置
		                                			</button>
		                                		</td>
		                                	</tr>
		                                </tbody>
                        			</table>
                        			<input type="hidden" name="tempFile" value="${tempFile }">
                        			<input type="hidden" name="ct" value="${ct }">
                        		</form>
                        	</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
	