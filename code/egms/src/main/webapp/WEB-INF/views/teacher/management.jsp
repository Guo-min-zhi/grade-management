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
                    	证书成绩管理
                    </h2>
                </div>
                <!-- /.col-lg-12 -->
            </div>
			
            <div class="row">
            	<div class="col-lg-12">
					<div class="row">
						<form:form cssClass="form-inline" role="form" method="get"
							modelAttribute="queryFastForm">
							<div class="form-group">
								<form:input path="studentNumber" placeholder="学号"
									cssClass="form-control input-sm" />
							</div>
							<div class="form-group">
								<form:input path="studentCollege" placeholder="学院"
									cssClass="form-control input-sm" />
							</div>
							<div class="form-group">
								<form:input path="studentGrade" placeholder="年级"
									cssClass="form-control input-sm" />
							</div>
							<div class="form-group">
								<form:select path="certificateType" cssClass="form-control input-sm">
									<form:option value="">证书类型</form:option>
									<c:forEach var="certificateType" items="${certificateTypes }">
										<option value="${certificateType.id }">${certificateType.certificateName
											}</option>
									</c:forEach>
								</form:select>
							</div>
							<div class="form-group">
								<form:select path="studentCertificateStatus"
									cssClass="form-control input-sm">
									<form:option value="">证书状态</form:option>
									<form:option value="1">待查验</form:option>
									<form:option value="2">查验通过</form:option>
									<form:option value="3">查验不通过</form:option>
									<form:option value="4">一审通过</form:option>
									<form:option value="5">已归档</form:option>
									<form:option value="6">教务系统导入</form:option>
								</form:select>
							</div>
							<div class="form-group input-sm">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;每页显示
								<form:select path="pageSize">
									<form:option value="10">10</form:option>
									<form:option value="20">20</form:option>
									<form:option value="50">50</form:option>
									<form:option value="100">100</form:option>
								</form:select>
								&nbsp;条
							</div>
							<button type="submit" class="btn btn-primary btn-sm">
								<span class="glyphicon glyphicon-search"></span> 快速查询
							</button>
						</form:form>
						<div class="panel-group" id="accordion">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title">
										<a data-toggle="collapse" data-toggle="collapse"
											data-parent="#accordion" href="#collapseOne"> 复杂查询 </a>
									</h4>
								</div>
								<div id="collapseOne" class="panel-collapse collapse">
									<div class="panel-body">
										<form:form cssClass="form-inline" role="form" method="get"
											modelAttribute="queryComplexForm"
											action="${ctx }/teacher/complex">
											<div class="form-group">
												<form:input path="studentNumber" placeholder="学号"
													cssClass="form-control input-sm" />
											</div>
											<div class="form-group">
												<form:input path="studentCollege" placeholder="学院"
													cssClass="form-control input-sm" />
											</div>
											<div class="form-group">
												<form:input path="studentGrade" placeholder="年级"
													cssClass="form-control input-sm" />
											</div>
											<div class="form-group">
												<form:input path="studentMajor" placeholder="专业"
													cssClass="form-control input-sm" />
											</div>
											<div class="form-group">
												<form:input path="studentName" placeholder="姓名"
													cssClass="form-control input-sm" />
											</div>
											<div class="form-group">
												<form:input path="checkTimes" placeholder="审查次数"
													cssClass="form-control input-sm" />
											</div>
											<div class="form-group">
												<form:input path="checkPerson" placeholder="审查人"
													cssClass="form-control input-sm" />
											</div>
											<div class="form-group">
												<form:select path="certificateType" cssClass="form-control input-sm">
													<form:option value="">证书类型</form:option>
													<c:forEach var="certificateType"
														items="${certificateTypes }">
														<option value="${certificateType.id }">${certificateType.certificateName
															}</option>
													</c:forEach>
												</form:select>
											</div>
											<div class="form-group">
												<form:select path="studentCertificateStatus"
													cssClass="form-control input-sm">
													<form:option value="">证书状态</form:option>
													<form:option value="1">待查验</form:option>
													<form:option value="2">查验通过</form:option>
													<form:option value="3">查验不通过</form:option>
													<form:option value="4">一审通过</form:option>
													<form:option value="5">已归档</form:option>
													<form:option value="6">教务系统导入</form:option>
												</form:select>
											</div>
											<div class="form-group input-sm">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;每页显示
												<form:select path="pageSize">
													<form:option value="10">10</form:option>
													<form:option value="20">20</form:option>
													<form:option value="50">50</form:option>
													<form:option value="100">100</form:option>
												</form:select>
												&nbsp;条
											</div>
											<button type="submit" class="btn btn-primary btn-sm">
												<span class="glyphicon glyphicon-search"></span> 复杂查询
											</button>
										</form:form>
									</div>
								</div>
							</div>
						</div>
					</div>
				
					<div class="row">
						<div class="panel panel-default">
							<!-- Default panel contents -->
							<div class="panel-heading">
								<form id="exportForm" action="${ctx }/teacher/exportExcel"
									method="post">
									<div>
										<button type="button" class="btn btn-warning btn-xs" id="archiveBtn">归档</button>
										<button type="button" class="btn btn-danger delete-item btn-xs"
											id="deleteBtn">删除</button>
										<button type="button" class="btn btn-success btn-xs"
											id="passCheckBtn">审核通过</button>
										<input type="submit" class="btn btn-warning btn-xs" value="结果导出" /> <input
											type="hidden" id="certificates" name="certificates" />
									</div>
								</form>
							</div>

							<!-- Table -->
							<table class="table" style=" font-size: 12px; ">
								<thead>
									<tr class="success">
										<th><input type="checkbox" id="chooseAll" /></th>
										<th>证书类型</th>
										<th>姓名</th>
										<th>成绩</th>
										<th>折算成绩</th>
										<th>原件照片</th>
										<th>查真网址</th>
										<th>查真截图</th>
										<th>获取时间</th>
										<th>状态</th>
										<th>上传时间</th>
										<th>审核次数</th>
										<th>详细信息</th>
										<!-- <th>姓名A</th>
									<th>A查验时间</th>
									<th>姓名B</th>
									<th>B查验时间</th>
									<th>审核意见</th> -->
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${pageObjects.content}" var="certificate"
										varStatus="status">
										<tr id="${certificate.id }">
											<td><input type="checkbox" class="checkTr"
												value="${certificate.id }"></td>
											<td><c:out
													value="${certificate.certificateType.certificateName }"></c:out></td>
											<td><c:out value="${certificate.studentInfo.name }"></c:out></td>
											<td><c:out value="${certificate.sourceScore}"></c:out></td>
											<td><c:out value="${certificate.translatedScore}"></c:out></td>
											<td><c:choose>
													<c:when test="${certificate.cerfificatePhotoUrl != null}">
														<a target="_blank"
															href="${certificate.cerfificatePhotoUrl }"
															data-toggle="tooltip" title
															data-original-title="<img src='${certificate.cerfificatePhotoUrl }' alt='...' class='img-thumbnail'>"
															data-html=true> <span
															class="glyphicon glyphicon-ok-sign"></span>
														</a>
													</c:when>
													<c:otherwise>
														<span class="glyphicon glyphicon-remove"></span>
													</c:otherwise>
												</c:choose></td>
											<td><c:choose>
													<c:when test="${certificate.checkWebsite != null}">
														<a href="${certificate.checkWebsite }" target="_blank"
															data-toggle="tooltip"
															title="${certificate.checkWebsite }"
															data-original-title="Another one here too"> <span
															class="glyphicon glyphicon-ok-sign"></span>
														</a>
													</c:when>
													<c:otherwise>
														<span class="glyphicon glyphicon-remove"></span>
													</c:otherwise>
												</c:choose></td>
											<td><c:choose>
													<c:when
														test="${certificate.checkWebsiteScreenshot != null }">
														<a target="_blank"
															href="${certificate.checkWebsiteScreenshot }"
															data-toggle="tooltip" title
															data-original-title="<img src='${certificate.checkWebsiteScreenshot }' alt='...' class='img-thumbnail'>"
															data-html=true> <span
															class="glyphicon glyphicon-ok-sign"></span>
														</a>
													</c:when>
													<c:otherwise>
														<span class="glyphicon glyphicon-remove"></span>
													</c:otherwise>
												</c:choose></td>
											<td><c:out
													value="${certificate.certificateAcquireTime }"></c:out></td>
											<%-- <td><c:out value="${mapStatus[certificate.status] }"></c:out></td> --%>
											<td><c:choose>
													<c:when test="${certificate.status == 4}">
														<a data-toggle="tooltip" title
															data-original-title="'${certificate.verifyTeacherA.name }' 老师审核">
															<c:out value="${mapStatus[certificate.status] }"></c:out>
														</a>
													</c:when>
													<c:otherwise>
														<c:out value="${mapStatus[certificate.status] }"></c:out>
													</c:otherwise>
												</c:choose></td>

											<td><c:out value="${certificate.submitTime }"></c:out></td>
											<td><c:out value="${certificate.verifyTimes }"></c:out></td>
											<td><a
												href="${ctx }/teacher/certificate/${certificate.id}"
												target="_blank"> <span
													class="glyphicon glyphicon-arrow-right"></span>
											</a></td>
											<%-- <td><c:out value="${certificate.verifyTeacherA.name }"></c:out></td>
									<td><c:out value="${certificate.verifyTimeA }"></c:out></td>
									<td><c:out value="${certificate.verifyTeacherB.name }"></c:out></td>
									<td><c:out value="${certificate.verifyTimeB }"></c:out></td>
									<td></td> --%>
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
    </div>
	<script type="text/javascript">
	$(function(){
    	//选中toolbar的active
    	$("#managementBar").addClass("active");
    	//激活hover上来之后的弹出
    	$("a[data-toggle='tooltip']").tooltip();
    	//负责checkbox的全选和全不选
    	$("#chooseAll").change(function(){
			if($("#chooseAll")[0].checked){
				$("input[type='checkbox']").prop("checked", true);
			}else{
				$("input[type='checkbox']").prop("checked", false);
			}
		});
		//点击“归档”按钮发生的事件
    	$("#archiveBtn").click(function(){
    		var certificates = new Array();
    		$.each($(".checkTr"), function(i, o){
    			if(o.checked){
	    			certificates.push($(o).prop("value"));
    			}
    		});
    		var a = certificates;
    		$.ajax({
				type: "post",
				url: "${ctx}/teacher/archive",
				data: {
					certificates : certificates.join(",")
				},
				success: function(data){
		    		window.location.reload();
				},
				error: function(data){
					alert("归档出现错误！");
				},
			});
    	});
		//点击“删除”按钮发生的事件
		$("#deleteBtn").click(function(){
			var certificates = new Array();
    		$.each($(".checkTr"), function(i, o){
    			if(o.checked){
	    			certificates.push($(o).prop("value"));
    			}
    		});
    		var a = certificates;
    		$.ajax({
				type: "post",
				url: "${ctx}/teacher/delete",
				data: {
					certificates : certificates.join(",")
				},
				success: function(data){
		    		window.location.reload();
				},
				error: function(data){
					alert("删除出现错误！");
				},
			});
		});
		//点击“审核通过”按钮发生的事件
		$("#passCheckBtn").click(function(){
			var certificates = new Array();
    		$.each($(".checkTr"), function(i, o){
    			if(o.checked){
	    			certificates.push($(o).prop("value"));
    			}
    		});
    		$.ajax({
				type: "post",
				url: "${ctx}/teacher/pass",
				data: {
					certificates : certificates.join(",")
				},
				success: function(data){
		    		window.location.reload();
				},
				error: function(data){
					alert("审核通过出现错误！");
				},
			});
		});
		
		$("#exportForm").submit(function(){
			var certificates = new Array();
    		$.each($(".checkTr"), function(i, o){
    			if(o.checked){
	    			certificates.push($(o).prop("value"));
    			}
    		});
    		
    		if(certificates.length > 0){
    			$("#certificates").val(certificates.join(","));
    			return true;
    		}else{
    			alert("请选择要导出的记录。");
    			return false;
    		}
		});
    });
    
    $(document).ready(function(){
		$(":input[placeholder]").placeholder();
	});
	</script>
</body>
</html>
	