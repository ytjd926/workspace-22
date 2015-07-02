$package('ian.login');
ian.login = function(){
	return {
		toLogin:function(){
			try{
				var form = $("#loginForm");
				if(form.form('validate')){
					ian.progress('Please waiting','Loading...');
					ian.submitForm(form,function(data){
						ian.closeProgress();
						if(data.success){
					 		window.location= "main.shtml";
				        }else{
				       	   ian.alert('提示',data.msg,'error');  
				        }
				        ian.login.loadVrifyCode();//刷新验证码
					});
				}
			}catch(e){
				
			}
			return false;
		},
		loadVrifyCode:function(){//刷新验证码
			var _url = "ImageServlet?time="+new Date().getTime();
			$(".vc-pic").attr('src',_url);
		},
		init:function(){
			if(window.top != window.self){
				window.top.location =  window.self.location;
			}
			//验证码图片绑定点击事件
			$(".vc-pic").click(ian.login.loadVrifyCode);
			
			var form = $("#loginForm");
			form.submit(ian.login.toLogin);
		}
	}
}();

$(function(){
	ian.login.init();
});		