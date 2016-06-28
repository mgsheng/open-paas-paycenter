jQuery(document).ready(function() {       
   loadIdentifyType();
});

function loadIdentifyType(){
	var type=0;
	$.post("/dev/user/loadIdentifyType.json",
	  null,
	  function(data,status){
		var statusMap = data.statusMap;
		var type=document.getElementById("typen").value;
		if(null != statusMap){
			var html = '<option value="0">请选择</option>';
			for(var key in statusMap){
				if(key ==type){
					html += '<option value="'+key+'" selected="selected">'+statusMap[key]+'</option>'; 
					$('#identifyT').html(statusMap[key]);
				}else{
					html += '<option value="'+key+'">'+statusMap[key]+'</option>'; 
				}
				
			}
			$('.identifyType').html(html);
		}
		//可以不加载该方法，所以异常则直接跳过
		try{
			loadStatusSelectDataCallback();
		}
		catch(e){
			
		}
	  });

}