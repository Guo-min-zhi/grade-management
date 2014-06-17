<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<nav class="navbar-default navbar-static-side" role="navigation">
	<div class="sidebar-collapse">
		<ul class="nav" id="side-menu">
			<li>
				<a href="${ctx }/teacher/importGrade">
					<i class="fa fa-bar-chart-o fa-fw"></i> 成绩导入
				</a>
			</li>
			<li>
				<a href="${ctx }/teacher/management">
					<i class="fa fa-bar-chart-o fa-fw"></i> 成绩管理
				</a>
			</li>
			<li>
				<a href="${ctx }/teacher/manage">
					<i class="fa fa-bar-chart-o fa-fw"></i> 综合成绩管理
				</a>
			</li>
			<li id="personalInfo">
				<a href="#">
					<i class="fa fa-sitemap fa-fw"></i> 个人信息
					<span class="fa arrow"></span>
				</a>
				<ul class="nav nav-second-level">
					<li><a href="${ctx }/student/info">完善个人信息</a></li>
					<li><a href="${ctx }/teacher/password">修改密码</a></li>
				</ul> <!-- /.nav-second-level -->
			</li>
		</ul>
		<!-- /#side-menu -->
	</div>
	<!-- /.sidebar-collapse -->
</nav>