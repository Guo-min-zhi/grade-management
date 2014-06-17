<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<div class="gigantic pagination">
   	<a href="#" class="first" data-action="first">&laquo;</a>
   	<a href="#" class="previous" data-action="previous">&lsaquo;</a>
    <input type="text" readonly="readonly" data-max-page="40" />
    <a href="#" class="next" data-action="next">&rsaquo;</a>
    <a href="#" class="last" data-action="last">&raquo;</a>
    
</div>

<script type="text/javascript">
	$(".pagination").jqPagination({
		current_page:${pageObjects.number+1},
		max_page:${pageObjects.totalPages},
		paged:function(page){
			$("#wrapper").disable({ 
		        cssClass: 'disabled', 
		        enableOnAjaxComplete: true, 
		        ajaxUrl: "${pageUrl}&page="+page,
		        ajaxCallback: function(data){
		            $("#wrapper").html(data);
		        }
		     });
		}
	});
</script>