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
                    	证书录入
                    </h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
			
            <div class="row">
				<div class="panel panel-primary">
					<div class="panel-heading">证书上传</div>
					<div class="panel-body">
						<c:choose>
							<c:when test="${result == 'success' }">
								<div class="alert alert-success alert-dismissable">
									<button type="button" class="close" data-dismiss="alert"
										aria-hidden="true">&times;</button>
									<strong>成功!</strong> 证书上传成功，请等待审核.
								</div>
							</c:when>
							<c:when test="${result == 'notComplete' }">
								<div class="alert alert-warning alert-dismissable">
									<button type="button" class="close" data-dismiss="alert"
										aria-hidden="true">&times;</button>
									<strong>证书上传无效，请先完善个人信息!</strong>（完善个人信息，上传近照）
								</div>
							</c:when>
						</c:choose>
						<div class="row">
							<form id="certificateInfo" class="form-horizontal" role="form"
								method="post" action="${ctx }/student/certificate">
								<input type="hidden" name="certificateId"
									value="${certificate.id }">
								<div class="form-group">
									<label for="certificateType" class="col-sm-2 control-label">证书类别</label>
									<div class="col-sm-6">
										<select class="form-control" id="certificateType"
											name="certificateType">
											<c:forEach var="certificateType" items="${certificateTypes }">
												<c:choose>
													<c:when
														test="${certificate.certificateType.id == certificateType.id }">
														<option value="${certificateType.id }" selected="selected">${certificateType.certificateName
															}</option>
													</c:when>
													<c:otherwise>
														<option value="${certificateType.id }">${certificateType.certificateName
															}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="studentGrade" class="col-sm-2 control-label">成绩</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="studentGrade"
											placeholder="成绩" name="studentGrade"
											value="${certificate.sourceScore }">
									</div>
								</div>
								<div class="form-group">
									<label for="studentFinalGrade" class="col-sm-2 control-label">折算成绩</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="studentFinalGrade"
											placeholder="折算成绩" name="studentFinalGrade"
											value="${certificate.translatedScore }" readonly>
									</div>
								</div>
								<div class="form-group">
									<label for="inputEmail3" class="col-sm-2 control-label">证书原件照片</label>
									<div class="col-sm-6">
										<input type="file" name="file_upload" id="file_upload" />
										<c:choose>
											<c:when test="${certificate.cerfificatePhotoUrl != null }">
												<img src="${certificate.cerfificatePhotoUrl }" alt="..."
													class="img-thumbnail" id="certificatePhoto"
													style="width: 150px; height: 150px; overflow: hidden;">
												<input type="text" id="certificateOriginalUrl"
													name="certificateOriginalUrl" class="hidden"
													value="${certificate.cerfificatePhotoUrl }">
											</c:when>
											<c:otherwise>
												<img src="${staticFile }/img/photo.png" alt="..."
													class="img-thumbnail" id="certificatePhoto"
													style="width: 150px; height: 150px; overflow: hidden;">
												<input type="text" id="certificateOriginalUrl"
													name="certificateOriginalUrl" class="hidden">
											</c:otherwise>
										</c:choose>
									</div>
								</div>
								<div class="form-group">
									<label for="certificateURL" class="col-sm-2 control-label">证书查真网址</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="certificateURL"
											placeholder="证书查真网址,必须以http://开头" name="certificateURL"
											value="${certificate.checkWebsite }">
									</div>
								</div>
								<div class="form-group">
									<label for="inputEmail3" class="col-sm-2 control-label">证书查真截图</label>
									<div class="col-sm-6">
										<input type="file" name="file_upload" id="file_upload_check" />
										<c:choose>
											<c:when test="${certificate.checkWebsiteScreenshot != null }">
												<img src="${certificate.checkWebsiteScreenshot}" alt="..."
													class="img-thumbnail" id="certificateCheckPhoto"
													style="width: 150px; height: 150px; overflow: hidden;">
												<input type="text" id="certificateCheckPhotoUrl"
													name="certificateCheckPhotoUrl" class="hidden"
													value="${certificate.checkWebsiteScreenshot}">
											</c:when>
											<c:otherwise>
												<img src="${staticFile }/img/photo.png" alt="..."
													class="img-thumbnail" id="certificateCheckPhoto"
													style="width: 150px; height: 150px; overflow: hidden;">
												<input type="text" id="certificateCheckPhotoUrl"
													name="certificateCheckPhotoUrl" class="hidden">
											</c:otherwise>
										</c:choose>
									</div>
								</div>
								<div class="form-group">
									<label for="certificateTime" class="col-sm-2 control-label">证书获取时间</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="certificateTime"
											placeholder="证书获取时间" name="certificateTime"
											value="${certificate.certificateAcquireTime }">
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-offset-2 col-sm-10">
										<button type="submit" class="btn btn-primary">提交</button>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
            
        </div>
    </div>
	<script type="text/javascript">
	$(function(){
		$(function(){
    		$('#certificateInfo').validate({
	   			onkeyup: true,
	   			rules:{
	   				studentGrade:{
	   					required:true,
	   					number:true
	   				},
	   				certificateURL:{
	   					required:true,
	   					url:true
	   				},
	   				certificateTime:{
	   					required:true,
	   					date:true
	   				},
	   				certificateOriginalUrl:{
	   					required:true
	   				},
	   				certificateCheckPhotoUrl:{
	   					required:true
	   				}
	   			},
	   			messages:{
	   				certificateOriginalUrl: "请上传图片",
	   				certificateCheckPhotoUrl: "请上传图片"
	   			}
	   		});
			$("#studentGrade").blur(function(){
    			var grade = $("#studentGrade").val();
    			if(grade && /^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(grade)){
    				$.ajax({
    	    			type: "post",
    					url: "${ctx }/student/calculateFinal",
    					data:{
    						'grade': grade,
    						'certificateTypeId': $("#certificateType").val()
    					},
    					success: function(data){
    						$("#studentFinalGrade").attr("value", data);
    					},
    					error: function(){
    						alert("error occurs when calculate the fianl grade.");
    					}
    	    		});
    			}
    		});
    		$("#headCertificate").addClass("active");
    		$("#certificateTime").datetimepicker({
    			format : 'yyyy-mm-dd',
    			minView: 2,
    			autoclose: true
    		});
    		
    		$('#file_upload').uploadify({
                'swf'      : '${staticFile}/uploadify/uploadify.swf',
                'uploader' : 'uploadCertificateOriginalPhoto',
                // Put your options here
                'fileObjName' : 'file',
                'method': 'POST',
                'formData': {
                	'studentId': '13126075',
                	'type': 'certificate'
                },
                'cancelImg': '${staticFile}/uploadify/uploadify-cancel.png',
                'multi' : false,
                'buttonText' : '上传照片',
                'simUploadLimit' : 1,
                'fileTypeExts' : '*.gif; *.jpg; *.png',
                onUploadSuccess : function(file, data, response) {
                	var html = '<div class="alert alert-success alert-dismissable">'+
    					  '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>'+
    					  '<strong>成功!</strong> 上传图像成功.</div>';
    				$(".panel-body").append(html);
                	$("#certificatePhoto").attr("src", data);
                	$("#certificateOriginalUrl").attr('value', data);
                },
                onUploadError : function(event, queueID, fileObj) {
                    alert("文件:" + fileObj.name + "上传失败");
                },
            });
    		
    		$('#file_upload_check').uploadify({
                'swf'      : '${staticFile}/uploadify/uploadify.swf',
                'uploader' : 'uploadCertificateOriginalPhoto',
                // Put your options here
                'fileObjName' : 'file',
                'method': 'POST',
                'formData': {
                	'studentId': '13126075',
                	'type': 'check'
                },
                'cancelImg': '${staticFile}/uploadify/uploadify-cancel.png',
                'multi' : false,
                'buttonText' : '上传照片',
                'simUploadLimit' : 1,
                'fileTypeExts' : '*.gif; *.jpg; *.png',
                onUploadSuccess : function(file, data, response) {
                	var html = '<div class="alert alert-success alert-dismissable">'+
    					  '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>'+
    					  '<strong>成功!</strong> 上传图像成功.</div>';
    				$(".panel-body").append(html);
                	$("#certificateCheckPhoto").attr("src", data);
                	$("#certificateCheckPhotoUrl").attr('value', data);
                },
                onUploadError : function(event, queueID, fileObj) {
                    alert("文件:" + fileObj.name + "上传失败");
                },
            });
    	});
    	
    	$(document).ready(function(){
			$(":input[placeholder]").placeholder();
		});
	});
	</script>
</body>
</html>
	