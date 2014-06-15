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
				    	教师信息
                	</strong>
                </h4>
            </div>
            <form:form id="inputForm" action="${ctx}/admin/teacherManage/save?ajax=true" cssClass="form-horizontal" role="form" modelAttribute="teacher">
	            <c:if test="${not empty teacher.id }">
	            	<form:hidden path="id"/>
	            </c:if>
	            <div class="modal-body">
	            	<div id="alertMessage">
					</div>
	                <div class="row">
	                    <div class="col-lg-12">
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">工号</label>
		                        <div class="col-sm-8">
		                            <form:input path="loginName" cssClass="form-control" placeholder="工号" />
		                        </div>
		                        <c:if test="${not empty teacher.id }">
			                        <a id="resetPassword" class="btn btn-warning">重置密码</a>
					            </c:if>
		                    </div>
		                    
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">姓名</label>
		                        <div class="col-sm-8">
		                        	<form:input path="name" cssClass="form-control" placeholder="姓名" />
		                        </div>
		                    </div>
		                    
		                    <form:hidden path="password"/>
		                    <form:hidden path="role.id"/>
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
