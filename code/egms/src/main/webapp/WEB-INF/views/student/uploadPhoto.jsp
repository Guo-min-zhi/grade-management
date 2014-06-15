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
    <script type="text/javascript">
	$(function() {
		// expand the sidebar.
		$("#personalInfo").addClass("active");
		$("#personalInfo").children("ul").addClass("in");
    	
		// the action of upload photo.
        $('#file_upload').uploadify({
            'swf'      : '${staticFile}/uploadify/uploadify.swf',
            'uploader' : 'uploadPersonPhoto',
            'fileObjName' : 'file',
            'method': 'POST',
            'formData': {
            	'studentId': '13126075',
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
            	$("#photoPosition").attr("src", data);
            },
            onUploadError : function(event, queueID, fileObj) {
                alert("文件:" + fileObj.name + "上传失败");
            },
        });
    });
	</script>
</head>

<body>

    <div id="wrapper">
		<%@ include file="/WEB-INF/views/component/header.jsp" %>
		<%@ include file="component/sidebar.jsp" %>
		
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">
                    	个人近期2寸照片
                    </h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
			
            <div class="row">
            	<div class="col-md-8 well col-md-offset-1">
					<div class="panel panel-primary">
						<div class="panel-heading">个人近期2寸照片</div>
						<div class="panel-body">
							<input type="file" name="file_upload" id="file_upload"/>
							<c:choose>
								<c:when test="${head != null }">
									<img src="${head }" alt="head" class="img-thumbnail" id="photoPosition" style="width:175px;height:235px; ">
								</c:when>
								<c:otherwise>
									<img src="${staticFile }/img/photo.png" alt="head" class="img-thumbnail" id="photoPosition" style="width:175px;height:235px; ">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
            </div>
            
        </div>
    </div>
</body>
</html>
	