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
                    	综合成绩详细信息
                    </h2>
                </div>
                <!-- /.col-lg-12 -->
            </div>
			
            <div class="row">
            	<div class="col-lg-12">
					<c:if test="${result != null }">
						<div class="alert alert-warning alert-dismissable">
							<button type="button" class="close" data-dismiss="alert"
								aria-hidden="true">&times;</button>
							<strong>${result }</strong>
						</div>
					</c:if>

					<div class="panel panel-primary">
						<div class="panel-heading">
							综合成绩详细信息
						</div>
						<div class="panel-body" id="${certificate.id }">

							<form class="form-horizontal col-md-6" role="form">
								<div class="form-group">
									<label class="col-sm-3 control-label">姓名:</label>
									<div class="col-sm-9">
										<p class="form-control-static">${student.name }</p>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">学院:</label>
									<div class="col-sm-9">
										<p class="form-control-static">${student.college }</p>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">专业:</label>
									<div class="col-sm-9">
										<p class="form-control-static">${student.major }</p>
									</div>
								</div>
							</form>
							<form class="form-horizontal col-md-6" role="form">
								<div class="form-group">
									<label class="col-sm-3 control-label">学号:</label>
									<div class="col-sm-9">
										<p class="form-control-static">${student.loginName }</p>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">年级:</label>
									<div class="col-sm-9">
										<p class="form-control-static">${student.grade }</p>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">班级:</label>
									<div class="col-sm-9">
										<p class="form-control-static">${student.classNum }</p>
									</div>
								</div>
							</form>
							<table class="table">
								<tbody>
									<tr>
										<td><strong>成绩类型:</strong></td>
										<td>${certificate.certificateType.certificateName }</td>
										<td><strong>综合成绩:</strong></td>
										<td>${certificate.translatedScore }</td>
										<td><strong>等级分:</strong></td>
										<td>${certificate.gradeFinal }</td>
									</tr>
									<tr class="notCertificate">
										<td><strong>第一学期成绩:</strong></td>
										<td>${certificate.gradeA }</td>
										<td><strong>第二学期成绩:</strong></td>
										<td>${certificate.gradeB }</td>
										<td><strong>第三学期成绩:</strong></td>
										<td>${certificate.gradeC }</td>
									</tr>
									<tr class="notCertificate">
										<td><strong>口语成绩:</strong></td>
										<td>${certificate.oralScore }</td>
										<td><strong>笔试成绩:</strong></td>
										<td>${certificate.writtenScore }</td>
										<td></td>
										<td></td>
									</tr>
									
								
									
									<tr class="certificate">
										<td><strong>证书成绩:</strong></td>
										<td>${certificate.sourceScore }</td>
										<td><strong>证书获取时间:</strong></td>
										<td>${certificate.certificateAcquireTime }</td>
										<td><strong>成绩上传时间:</strong></td>
										<td>${certificate.submitTime }</td>
										
									</tr>
									<tr class="certificate">
										<td><strong>审核状态</strong></td>
										<td>${mapStatus[certificate.status] }</td>
										<td><strong>审核次数:</strong></td>
										<td>${certificate.verifyTimes }</td>
										<td><strong>证书查真网址:</strong></td>
										<td>${certificate.checkWebsite }</td>
										<td></td>
										<td></td>
									</tr>
									<c:if test="${certificate.verifyTeacherA != null }">
										<tr class="certificate">
											<td><strong>审核人A:</strong></td>
											<td>${certificate.verifyTeacherA.name }</td>
											<td><strong>审核人A审核时间:</strong></td>
											<td>${certificate.verifyTimeA }</td>
											<td><strong>审核人A审核意见:</strong></td>
											<td>${certificate.commentA }</td>
										</tr>
									</c:if>
									<c:if test="${certificate.verifyTeacherB != null }">
										<tr class="certificate">
											<td><strong>审核人B:</strong></td>
											<td>${certificate.verifyTeacherB.name }</td>
											<td><strong>审核人B审核时间:</strong></td>
											<td>${certificate.verifyTimeB }</td>
											<td><strong>审核人B审核意见:</strong></td>
											<td>${certificate.commentB }</td>
										</tr>
									</c:if>

									<tr class="certificate">
										<td><strong>证书原件照片:</strong></td>
										<td colspan="2"><c:choose>
												<c:when test="${certificate.cerfificatePhotoUrl != null}">
													<a href="${certificate.cerfificatePhotoUrl }"
														target="_blank"> <img
														src="${certificate.cerfificatePhotoUrl }" alt="..."
														class="img-thumbnail" style="width: 145px; height: 145px;">
													</a>
												</c:when>
												<c:otherwise>
													<img src="${staticFile }/img/photo.png" alt="..."
														class="img-thumbnail">
												</c:otherwise>
											</c:choose></td>
										<td><strong>证书查真截图:</strong></td>
										<td colspan="2"><c:choose>
												<c:when
													test="${certificate.checkWebsiteScreenshot != null }">
													<a href="${certificate.checkWebsiteScreenshot }"
														target="_blank"> <img
														src="${certificate.checkWebsiteScreenshot }" alt="..."
														class="img-thumbnail" style="width: 145px; height: 145px;">
													</a>
												</c:when>
												<c:otherwise>
													<img src="${staticFile }/img/photo.png" alt="..."
														class="img-thumbnail">
												</c:otherwise>
											</c:choose></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>


					<div class="modal fade" id="infoModal">
						<div class="modal-dialog">
							<form id="comment" action="${ctx }/teacher/nopass" method="post">
								<input type="hidden" value="${certificate.id }"
									name="certificateId">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-hidden="true">&times;</button>
										<h4 class="modal-title">审核意见</h4>
									</div>
									<div class="modal-body">
										<textarea class="form-control" rows="3" id="commentTextarea"
											name="comment" maxlength="100"></textarea>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											data-dismiss="modal">关闭</button>
										<button type="submit" class="btn btn-primary" id="sureBtn">提交</button>
									</div>
								</div>
								<!-- /.modal-content -->
							</form>
						</div>
						<!-- /.modal-dialog -->
					</div>
					<!-- /.modal -->

				</div>
            </div>
            
        </div>
    </div>
	<script type="text/javascript">
	$(document).ready(function(){
		if('${certificate.sourceScore }' == ''){
			$('.certificate').hide();
		}else{
			$('.notCertificate').hide();
		}
	})
	$(function() {
		$('#comment').validate({
			onkeyup:true,
			rules:{
				comment:{
					required:true
				}
			}
		});
		
	});
	</script>
</body>
</html>
	