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
                    <h1 class="page-header">
                    	查看证书
                    </h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
			
            <div class="row">
						<div class="panel panel-primary">
							<div class="panel-heading">证书审查结果</div>
							<table class="table table-hover" style="font-size: 12px;">
								<thead>
									<tr>
										<th></th>
										<th>证书类型</th>
										<th>证书成绩</th>
										<th>折算成绩</th>
										<th>审核状态</th>
										<th>原件照片</th>
										<th>查真截图</th>
										<th>审核意见A</th>
										<th>审核意见B</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="certificate" items="${certificates }" begin="0"
										varStatus="index">
										<tr id="${certificate.id }">
											<td>${index.count }</td>
											<td>${certificate.certificateType.certificateName }</td>
											<td>${certificate.sourceScore }</td>
											<td>${certificate.translatedScore }</td>
											<td>${mapStatus[certificate.status] }</td>
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
											<td>${certificate.commentA }</td>
											<td>${certificate.commentB }</td>
											<td><c:choose>
													<c:when test="${certificate.status == 1}">
														<a type="button" class="btn btn-default btn-modify btn-xs"
															href="${ctx }/student/certificate?id=${certificate.id }">修改</a>
														<a type="button" class="btn btn-default btn-delete btn-xs"
															href="${ctx }/student/certificateDelete?id=${certificate.id }"
															onclick="{if(confirm('您确定删除吗?')){return true;}return false;}">删除</a>
													</c:when>
													<c:when test="${certificate.status == 3}">
														<a type="button" class="btn btn-default btn-modify btn-xs"
															href="${ctx }/student/certificate?id=${certificate.id }">修改</a>
														<a type="button" class="btn btn-default btn-delete btn-xs"
															href="${ctx }/student/certificateDelete?id=${certificate.id }"
															onclick="{if(confirm('您确定删除吗?')){return true;}return false;}">删除</a>
													</c:when>

												</c:choose></td>
										</tr>
									</c:forEach>

									<!-- <tr>
							<td>1</td>
							<td>四级</td>
							<td>98</td>
							<td>77</td>
							<td>上传的证书不清晰</td>
							<td><button type="button" class="btn btn-default">删除</button></td>
						</tr>
						<tr>
							<td>2</td>
							<td>六级</td>
							<td>98</td>
							<td>77</td>
							<td>成绩不通过</td>
							<td><button type="button" class="btn btn-default">删除</button></td>
						</tr> -->
								</tbody>
							</table>
						</div>
			</div>
            
        </div>
    </div>
	<script type="text/javascript">
	$(function(){
   		//激活hover上来之后的弹出
    	$("a[data-toggle='tooltip']").tooltip();
	});
	</script>
</body>
</html>
	