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
                    	综合成绩管理
                    </h2>
                </div>
                <!-- /.col-lg-12 -->
            </div>
			
            <div class="row" style=" margin-bottom: 10px; ">
            	<div class="col-lg-12">
            		<form:form cssClass="form-inline" role="form" method="get" modelAttribute="queryComprehensiveForm">
						<table class="searchTable">
	                        <tr>
	                            <td class="ItemLabel">学号:</td>
	                            <td class="ItemControl">
	                            	<form:input path="studentNumber" placeholder="学号" cssClass="Edit" cssStyle="width:130px;"/>
	                                <!-- <input type="text" class="Edit" style=" width: 80px; "> -->
	                            </td>
	                            <td class="ItemLabel">姓名:</td>
	                            <td class="ItemControl">
	                            	<form:input path="studentName" placeholder="姓名" cssClass="Edit" cssStyle="width:80px;"/>
	                                <!-- <input type="text" class="Edit" style=" width: 80px; "> -->
	                            </td>
	                            <td class="ItemLabel">开始时间:</td>
	                            <td class="ItemControl">
	                            	<form:input path="startTime" placeholder="开始时间" cssClass="Edit" cssStyle="width:130px;"/>
	                                <!-- <input type="text" class="Edit" id="startTime" style=" width: 130px; "> -->
	                            </td>
	                            <td class="ItemLabel">结束时间:</td>
	                            <td class="ItemControl">
	                            	<form:input path="endTime" placeholder="结束时间" cssClass="Edit" cssStyle="width:130px;"/>
	                                <!-- <input type="text" class="Edit" id="endTime" style=" width: 130px; "> -->
	                            </td>
	                            <td class="ItemLabel">成绩来源:</td>
	                            <td class="ItemControl">
	                            	<form:select path="certificateType" cssClass="Edit">
	                            		<form:option value="">--成绩类型--</form:option>
	                            		<c:forEach items="${cts }" var="ct">
	                                    	<option value="${ct.id }">${ct.certificateName }</option>
	                                    </c:forEach>
	                            	</form:select>
	                            </td>
	                        </tr>
	                        <tr>
	                        	<td class="ItemLabel">成绩状态</td>
	                        	<td class="ItemControl">
	                        		<form:select path="scoreStatus" cssClass="Edit" cssStyle="width:130px;">
	                        			<form:option value="">--成绩状态--</form:option>
	                        			<form:option value="0">无法计算综合成绩</form:option>
	                        			<form:option value="1">满足条件，未导出</form:option>
	                        			<form:option value="2">满足条件，已导出</form:option>
	                        		</form:select>
	                        		<!-- <select class="Edit" style="width:130px;">
	                        			<option value="0">无法计算综合成绩</option>
	                        			<option value="1">满足条件，未导出</option>
	                        			<option value="2">满足条件，已导出</option>
	                        		</select> -->
	                        	</td>
	                        	<td class="ItemLabel">每页显示:</td>
	                            <td class="ItemControl">
	                            	<form:select path="pageSize" cssStyle="width:80px;">
										<form:option value="10">10</form:option>
										<form:option value="20">20</form:option>
										<form:option value="50">50</form:option>
										<form:option value="100">100</form:option>
									</form:select>
	                            </td>
	                            <td></td>
	                            <td>
	                                <button type="submit" class="btn btn-primary btn-xs">查询</button>
	                            </td>
	                        </tr>
	                        <script type="text/javascript">
								$("#startTime").datetimepicker({
									format : 'yyyy-mm-dd hh:ii:ss'
								});
								$("#endTime").datetimepicker({
									format : 'yyyy-mm-dd hh:ii:ss'
								});
							</script>
	                    </table>
                    </form:form>
				</div>
				<hr>
            </div>
            
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                        	<%-- <form action="${ctx }/teacher/exportComprehensiveExcel" method="post"> --%>
	                        	<button type="button" class="btn btn-primary btn-xs" id="exportComBtn">
									<span class="glyphicon glyphicon-export"></span>&nbsp;导出成绩信息
								</button>
							<!-- </form> -->
                        </div>
                        <table class="table table-bordered" style="font-size:12px;">
                            <thead>
                                <tr class="success">
                                    <th style=" width: 20px; "></th>
                                    <th>学号</th>
                                    <th>姓名</th>
                                    <th>综合成绩</th>
                                    <th>等级分</th>
                                    <th>来源</th>
                                    <th>认定日期</th>
                                    <th>删除</th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach items="${pageObjects.content}" var="certificate" varStatus="status">
	                                <tr id="${certificate.id }">
	                                    <td><c:out value="${status.count }"/></td>
	                                    <td><c:out value="${certificate.studentInfo.loginName }"></c:out></td>
	                                    <td><c:out value="${certificate.studentInfo.name }"></c:out></td>
	                                    <td><c:out value="${certificate.translatedScore }"></c:out></td>
	                                    <td><c:out value="${certificate.gradeFinal }"></c:out></td>
	                                    <td><c:out value="${certificate.certificateType.certificateName }"></c:out></td>
	                                    <td><c:out value="${certificate.submitTime }"></c:out></td>
	                                    <td>
											<a target="_blank" href="${ctx }/teacher/score/${certificate.id}" class="btn btn-info btn-xs btn-info">
												<span class="glyphicon glyphicon-info-sign"></span>&nbsp;详情
											</a>
	                                        <a href="${ctx }/teacher/delete/${certificate.id}" class="btn btn-warning btn-xs btn-delete">
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
    </div>
	<script type="text/javascript">
	$(function(){
		function getUrlVars(){
		    var vars = [], hash;
		    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
		    for(var i = 0; i < hashes.length; i++){
		        hash = hashes[i].split('=');
		        vars.push(hash[0]);
		        vars[hash[0]] = hash[1];
		    }
		    return vars;
		}

		$(".btn-delete").click(function(e){
			var password = prompt("删除需要管理员密码 ");
		    if (password != null && password != "" && password == ${password}){
		    	
		    }else{
		    	e.preventDefault();
		    }
		  
		});
		
		function parameters(){
			var params = "studentNumber="+getUrlVars()["studentNumber"] + 
				"&studentName="+getUrlVars()["studentName"] + 
				"&startTime="+getUrlVars()["startTime"] + 
				"&endTime="+getUrlVars()["endTime"] + 
				"&certificateType="+getUrlVars()["certificateType"] + 
				"&scoreStatus="+getUrlVars()["scoreStatus"];
			
			return params;
		}
		$("#exportComBtn").click(function(){
			location.href = "${ctx }/teacher/exportComprehensiveExcel?"+parameters();
		});
	});
	</script>
</body>
</html>
	