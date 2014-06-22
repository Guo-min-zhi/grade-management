<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<div class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">
                	<strong>
				    	${gradeType.certificateName }——编辑计算公式
                	</strong>
                </h4>
            </div>
            <form:form id="inputForm" action="${ctx}/admin/parameterManage/saveFormula" cssClass="form-horizontal" role="form" modelAttribute="gradeType">
	            <c:if test="${not empty gradeType.id }">
	            	<form:hidden path="id"/>
	            </c:if>
	            <div class="modal-body">
	            	<div id="alertMessage">
					</div>
	                <div class="row">
	                    <div class="col-lg-12">
							<div class="form-group">
								<label for="formula" class="col-sm-2 control-label">公式</label>
								<div class="col-sm-9">
									<form:textarea class="form-control" rows="5" path="formula" placeholder="计算公式" maxlength="300" />
								</div>
							</div>
							<form:hidden path="certificateName"/>
							<form:hidden path="uploadType"/>
		            	</div>
		            	<p style="color: red;">注：当综合成绩由多个成绩加权求和得出时，成绩的变量设置如下所示：</p>
		            	<p style="color: red;">x1表示第一个学期成绩，x2表示第二学期成绩，x3表示第三学期成绩，x4表示口成绩，x5表示笔试成绩。</p>
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
