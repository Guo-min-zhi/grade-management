<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>英语综合成绩管理系统</title>
    <%@ include file="/WEB-INF/views/common/styles.jsp"%>
    <%@ include file="/WEB-INF/views/common/scripts.jsp"%>
    
</head>

<body>

    <div id="wrapper">
		<%@ include file="/WEB-INF/views/component/header.jsp" %>
		<%@ include file="component/sidebar.jsp" %>
		
        <div id="page-wrapper" style="text-align:center;">
        	<!-- <h1>欢迎使用英语综合成绩管理系统</h1> -->
           <div class="row">
                <div class="col-lg-12" style="margin-top:150px;">
                    <h1 class="page-header">
                    	欢迎使用英语综合成绩管理系统
                    </h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
			
            <%-- <div class="row">
            	<div class="col-lg-12">
					<a href="#" class="thumbnail">
				    	<img data-src="holder.js/100%x180" alt="..." src="${staticFile }/img/welcome.jpg">
				    </a>
				</div>
            </div> --%>
            
        </div>
    </div>
	
</body>
</html>
	