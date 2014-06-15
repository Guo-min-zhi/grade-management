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
				    	成绩类型信息
                	</strong>
                </h4>
            </div>
            <form:form id="inputForm" action="${ctx}/admin/parameterManage/save?ajax=true" cssClass="form-horizontal" role="form" modelAttribute="gradeType">
	            <c:if test="${not empty gradeType.id }">
	            	<form:hidden path="id"/>
	            </c:if>
	            <div class="modal-body">
	            	<div id="alertMessage">
					</div>
	                <div class="row">
	                    <div class="col-lg-12">
							<div class="form-group">
								<label for="name" class="col-sm-3 control-label">成绩类型名称</label>
								<div class="col-sm-6">
									<form:input path="certificateName" class="form-control" placeholder="成绩类型名称" />
								</div>
							</div>
							
							<div class="form-group">
								<label for="name" class="col-sm-3 control-label">上传类型</label>
								<div class="col-sm-6">
									<form:select path="uploadType" class="form-control">
										<form:option value="">--请选择上传类型--</form:option>
										<form:option value="0">--教师上传--</form:option>
										<form:option value="1">--学生上传--</form:option>
									</form:select>
								</div>
							</div>
							
							<form:hidden path="formula"/>
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
