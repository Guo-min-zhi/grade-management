<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<div class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">
                	<strong>
                		<c:if test="${action=='edit'}">编辑</c:if>
				    	<c:if test="${action=='create'}">添加</c:if>
				    	学生信息
                	</strong>
                </h4>
            </div>
            <form:form id="inputForm" action="${ctx}/admin/studentManage/save?ajax=true" cssClass="form-horizontal" role="form" modelAttribute="student">
	            <c:if test="${not empty student.id }">
	            	<form:hidden path="id"/>
	            </c:if>
	            <div class="modal-body">
	            	<div id="alertMessage">
					</div>
	                <div class="row">
	                    <div class="col-lg-12">
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">学号</label>
		                        <div class="col-sm-8">
		                            <form:input path="loginName" cssClass="form-control" placeholder="学号" />
		                        </div>
		                        <c:if test="${not empty student.id }">
			                        <a id="resetPassword" class="btn btn-warning">重置密码</a>
					            </c:if>
		                    </div>
		                    
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">姓名</label>
		                        <div class="col-sm-8">
		                        	<form:input path="name" cssClass="form-control" placeholder="姓名" />
		                        </div>
		                    </div>
		                    
							<div class="form-group">
		                        <label class="col-sm-2 control-label">性别</label>
		                        <div class="col-sm-8">
		                        	<label class="radio-inline">
			                            <form:radiobutton path="gender" value="0"/>女
		                        	</label>
		                        	<label class="radio-inline">
			                            <form:radiobutton path="gender" value="1"/>男
		                        	</label>
		                        </div>
		                    </div>
		                    
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">学院</label>
		                        <div class="col-sm-8">
		                            <form:input path="college" cssClass="form-control" placeholder="学院" />
		                        </div>
		                    </div>
		                    
		                     <div class="form-group">
		                        <label class="col-sm-2 control-label">专业</label>
		                        <div class="col-sm-8">
		                            <form:input path="major" cssClass="form-control" placeholder="专业" />
		                        </div>
		                    </div>
		                    
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">班级</label>
		                        <div class="col-sm-8">
		                            <form:input path="classNum" cssClass="form-control" placeholder="班级" />
		                        </div>
		                    </div>
		                    
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">身份证号</label>
		                        <div class="col-sm-8">
		                            <form:input path="identityNum" cssClass="form-control" placeholder="身份证号" />
		                        </div>
		                    </div>
		                    
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">手机号</label>
		                        <div class="col-sm-8">
		                            <form:input path="phoneNum" cssClass="form-control" placeholder="手机号" />
		                        </div>
		                    </div>
		                    <form:hidden path="password"/>
		                    <form:hidden path="role.id"/>
		                    <form:hidden path="grade"/>
		                    <form:hidden path="photo"/>
		                    <form:hidden path="status"/>
		            	</div>
	                </div>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-warning" data-dismiss="modal">关闭</button>
	                <button type="submit" class="btn btn-success">保存</button>
	            </div>
            </form:form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog --> 
</div>
