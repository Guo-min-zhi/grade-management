$(function(){
	$(".delete-item").click(function(event) {
		if (confirm('确认删除？')) {
			return true;
		} else {
			return false;
		}
	});
})

function createAlertMessage(message){
	var alertMessage = '<div class="alert alert-info alert-dismissable">' +
							'<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>'+
								'<strong>'+message+'</strong>'+
						'</div>';
						
	$('#alertMessage').html(alertMessage);
}