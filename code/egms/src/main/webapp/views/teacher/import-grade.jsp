<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>英语成绩管理系统</title>
	<%@ include file="../common/taglibs.jsp"%>
    <%@ include file="../common/styles.jsp"%>
    <%@ include file="../common/scripts.jsp"%>
    
</head>

<body>

    <div id="wrapper">
		<%@ include file="../component/header.jsp" %>
		<%@ include file="../component/sidebar.jsp" %>
		
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
                            <form role="form" class="form-horizontal">
                                <div class="form-group">
                                     <label for="type" class="col-sm-2 control-label">导入类型：</label>
                                     <div class="col-sm-3">
                                         <select name="" id="" class="form-control">
                                             <option value="">全校考试</option>
                                             <option value="">口语班</option>
                                             <option value="">软件班</option>
                                             <option value="">机电班</option>
                                             <option value="">四六级</option>
                                         </select>
                                     </div>
                                </div>
                                <div class="form-group">
                                    <label for="type" class="col-sm-2 control-label">选择文件：</label>
                                    <div class="col-sm-3">
                                        <input type="text" class="form-control" id="type" disabled>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-3 col-sm-offset-2">
                                        <span class="btn btn-success fileinput-button">
                                            <i class="glyphicon glyphicon-plus"></i>
                                            <span>选择文件</span>
                                            <input type="file" name="files" multiple>
                                        </span>
                                    </div>
                                </div>
                                <div class="form-group">
                                     <div class="col-sm-3 col-sm-offset-2">
                                         <button class="btn btn-primary">导入</button>
                                     </div>
                                </div>
                            </form>
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
                            <p>这个区域，根据上述类型的选择，动态显示以下内容：</p>
                            <p>1、  综合成绩的公式 （该公式由管理员在后台设置）</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
	