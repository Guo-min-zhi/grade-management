<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<nav class="navbar-default navbar-static-side" role="navigation">
	<div class="sidebar-collapse">
		<ul class="nav" id="side-menu">
			<li>
				<a href="${ctx }/admin/studentManage/list">
					<i class="fa fa-bar-chart-o fa-fw"></i>学生管理
				</a>
			</li>
			<li>
				<a href="${ctx }/admin/teacherManage/list">
					<i class="fa fa-bar-chart-o fa-fw"></i>教师管理
				</a>
			</li>
			<li>
				<a href="#">
					<i class="fa fa-bar-chart-o fa-fw"></i> 数据总览
				</a>
			</li>
			
			<li>
				<a href="#">
					<i class="fa fa-sitemap fa-fw"></i> 参数设定
					<span class="fa arrow"></span>
				</a>
				<ul class="nav nav-second-level">
					<li><a href="${ctx }/admin/parameterManage/gradeTypeList">成绩类型管理</a></li>
					<li><a href="${ctx }/admin/parameterManage/formulaList">公式设置</a></li>
					<li><a href="https://java.net/projects/eval/pages/Home" target="_blank">公式编写帮助文档</a></li>
				</ul> <!-- /.nav-second-level -->
			</li>
		</ul>
		<!-- /#side-menu -->
	</div>
	<!-- /.sidebar-collapse -->
</nav>