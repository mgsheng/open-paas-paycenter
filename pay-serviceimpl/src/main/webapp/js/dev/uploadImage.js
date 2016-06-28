var contextPath;
function uploadClick(clickbtn,contextpath){
	contextPath = contextpath;
	$("#"+clickbtn).click();
}

function fileupload(loadImg,prop){
	$.ajaxFileUpload({
        url:contextPath+'/FileUpload',             		//需要链接到服务器地址
        fileElementId:'iconUpload',                     //文件选择框的id属性  
        success: function (data)             	//相当于java中try语句块的用法  
        {
        	var json = '';
        	if(data.body.innerText != null){
        		json = JSON.parse(data.body.innerText);
        	}
        	else if(data.body.textContent != null){
        		json = JSON.parse(data.body.textContent);
        	}
			var src = $('#'+loadImg).attr('src');
			if (json.message!=null && json.message!=''){
        		if(src!=null){
        			$('#'+loadImg).css('display','');
					$('#'+loadImg).attr('src',src);
        		}
        		else{
        			$('#'+loadImg).css('display','none');
        		}
        	}
        	if(json.webPath!=null && json.webPath!=''){
        		$('#'+loadImg).css('display','');
				$('#'+loadImg).attr('src',json.webPath);
				$("#"+prop).val(json.webPath);
        	}
        },  
        error: function (data, status, e)             //相当于java中catch语句块的用法  
        {  
        	alert(e);
        }
      });
}
