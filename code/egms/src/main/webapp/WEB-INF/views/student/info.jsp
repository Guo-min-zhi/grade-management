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
                    	完善个人信息
                    </h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
			
            <div class="row">
            	<div class="col-md-8 well col-md-offset-1">
				<form class="form-horizontal col-md-8" role="form" action="${ctx }/student/info" method="POST" >
					<c:if test="${result == 'success'}">
						<div class="alert alert-success alert-dismissable">
							<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
							<strong>成功!</strong> 保存成功.
						</div>
					</c:if>
					<div class="form-group">
						<label for="studentId" class="col-sm-2 control-label">学号</label>
						<div class="col-sm-10">
						<c:choose>
							<c:when test="${student.loginName != null && student.loginName != ''}">
								<input type="text" class="form-control" id="studentId" value="${student.loginName }" name="studentId" readonly>
							</c:when>
							<c:otherwise>
								<input type="text" class="form-control" id="studentId" placeholder="学号" name="studentId" maxlength="13">
							</c:otherwise>
						</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label for="studentName" class="col-sm-2 control-label">姓名</label>
						<div class="col-sm-10">
							<input type="text" class="form-control required" id="studentName" value="${student.name }"  name="studentName" maxlength="25">
						</div>
					</div>
					<div class="form-group">
						<label for="studentGender" class="col-sm-2 control-label">性别</label>
						<div class="col-sm-10">
							<c:choose>
								<c:when test="${student.gender == 1}">
									<label class="radio-inline">
										<input type="radio" id="studentGenderM" value="1" name="studentGender" checked > 男
									</label>
									<label class="radio-inline">
										<input type="radio" id="studentGenderF" value="0" name="studentGender"> 女
									</label>
								</c:when>
								<c:when test="${student.gender == 0}">
									<label class="radio-inline">
										<input type="radio" id="studentGenderM" value="1" name="studentGender" > 男
									</label>
									<label class="radio-inline">
										<input type="radio" id="studentGenderF" value="0" name="studentGender" checked> 女
									</label>
								</c:when>
								<c:otherwise>
									<label class="radio-inline">
										<input type="radio" id="studentGenderM" value="1" name="studentGender"> 男
									</label>
									<label class="radio-inline">
										<input type="radio" id="studentGenderF" value="0" name="studentGender"> 女
									</label>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label for="studentCollege" class="col-sm-2 control-label">学院</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="studentCollege" value="${student.college }"  name="studentCollege" maxlength="50">
						</div>
					</div>
					<div class="form-group">
						<label for="studentMajor" class="col-sm-2 control-label">专业</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="studentMajor" value="${student.major}"  name="studentMajor" maxlength="50">
						</div>
					</div>
					<div class="form-group">
						<label for="studentGrade" class="col-sm-2 control-label">年级</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="studentGrade" value="${student.grade}"  name="studentGrade" maxlength="20">
						</div>
					</div>
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-2 control-label">班级</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="studentClass" value="${student.classNum}"  name="studentClass" maxlength="25">
						</div>
					</div>
					<div class="form-group">
						<label for="studentIdentity" class="col-sm-2 control-label">身份证号</label>
						<div class="col-sm-10">
							<input type="text" class="form-control required" id="studentIdentity" value="${student.identityNum}"  name="studentIdentity" maxlength="18">
						</div>
					</div>
					<div class="form-group">
						<label for="studentPhone" class="col-sm-2 control-label">手机号码</label>
						<div class="col-sm-10">
							<input type="text" class="form-control required" id="studentPhone" value="${student.phoneNum}" name="studentPhone" maxlength="11">
						</div>
					</div>
					<%-- <div class="form-group">
						<label for="inputEmail3" class="col-sm-2 control-label">个人近期照片</label>
						<div class="col-sm-10">
							<input type="file" name="file_upload" id="file_upload"/>
							<img src="${staticFile }/img/photo.png" alt="..." class="img-thumbnail" id="photoPosition">
						</div>
					</div> --%>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-primary" id="saveBtn">保存</button>
						</div>
					</div>
				</form>
				<div class="col-md-4">
					<c:if test="${student.photo != null }">
						<img src="${student.photo }" alt="head" class="img-thumbnail col-md-offset-2" style="heigth:160px; width:160px;">
					</c:if>
				</div>
			</div>
            </div>
            
        </div>
    </div>
	<script type="text/javascript">
	$(function() {
		// expand the sidebar.
		$("#personalInfo").addClass("active");
		$("#personalInfo").children("ul").addClass("in");
    	
        $('#file_upload').uploadify({
            'swf'      : '${staticFile}/uploadify/uploadify.swf',
            'uploader' : 'uploadPersonPhoto',
            // Put your options here
            'fileObjName' : 'file',
            'method': 'POST',
            'formData': {
            	'studentId': '13126075',
            	'certificate': 'cet4',
            	'finalGrade': '88'
            },
            'cancelImg': '${staticFile}/uploadify/uploadify-cancel.png',
            'multi' : false,
            'buttonText' : '上传',
            'simUploadLimit' : 1,
            onUploadSuccess : function(file, data, response) {
            	$("#photoPosition").attr("src", data);
                alert("文件上传成功"+data);
            },
            onUploadError : function(event, queueID, fileObj) {
                alert("文件:" + fileObj.name + "上传失败");
            }
        });
    	
    });
    
    $(document).ready(function(){
		$(":input[placeholder]").placeholder();
	})
	</script>
</body>
</html>
	