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
                    <h1 class="page-header">成绩管理</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>

            <div class="row">
                <div class="col-lg-12">
                    <table class="searchTable">
                        <tr>
                            <td class="ItemLabel">学号</td>
                            <td class="ItemControl">
                                <input type="text" class="Edit">
                            </td>
                            <td class="ItemLabel">姓名</td>
                            <td class="ItemControl">
                                <input type="text" class="Edit">
                            </td>
                            <td class="ItemLabel">成绩来源</td>
                            <td class="ItemControl">
                                <select name="" id="" class="Edit">
                                    <option value="">四六级</option>
                                    <option value="">机电班</option>
                                </select>
                            </td>
                            <td class="ItemLabel">开始时间</td>
                            <td class="ItemControl">
                                <input type="text" class="Edit">
                            </td>
                            <td class="ItemLabel">结束时间</td>
                            <td class="ItemControl">
                                <input type="text" class="Edit">
                            </td>
                            <td>
                                <button class="btn btn-primary btn-xs">查询</button>
                            </td>
                        </tr>
                    </table>
                </div>
                <hr>
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading"><button class="btn btn-primary btn-xs">导出</button></div>
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <thead>
                                    <tr class="success">
                                        <td>
                                            <input type="checkbox">
                                        </td>
                                        <td>行号</td>
                                        <td>姓名</td>
                                        <td>学号</td>
                                        <td>成绩</td>
                                        <td>类型</td>
                                        <td>删除</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>
                                            <input type="checkbox">
                                        </td>
                                        <td>1</td>
                                        <td>LILI</td>
                                        <td>13126077</td>
                                        <td>98</td>
                                        <td>CET4</td>
                                        <td>
                                            <button type="button" class="btn btn-default btn-xs">
                                              <span class="glyphicon glyphicon-remove"></span>
                                            </button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /.row -->
        </div>
    </div>

</body>
</html>
	