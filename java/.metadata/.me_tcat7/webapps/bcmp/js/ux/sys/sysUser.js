$package('ian.sysUser');
ian.sysUser = function(){
	var _box = null;
	var _this = {
		updatePwdAction:'updatePwd.do',
		editPwdForm:function(){
			return $("#pwdForm");
		},
		editPwdWin:function(){
			return $("#edit-pwd-win");
		},
		savePwd:function(){
			ian.progress();//缓冲条
			if(_this.editPwdForm().form('validate')){
				_this.editPwdForm().attr('action',_this.updatePwdAction);
				ian.saveForm(_this.editPwdForm(),function(data){
					ian.closeProgress();//关闭缓冲条
					_this.editPwdWin().dialog('close');
				});
			 }
		},
		initForm:function(){
			//修改密码
			_this.editPwdWin().find("#btn-pwd-submit").click(function(){
				_this.savePwd();
			});
			_this.editPwdWin().find("#btn-pwd-close").click(function(){	
				$.messager.confirm('Confirm','你确定要关闭窗口?',function(r){  
				    if (r){  
				     	_this.editPwdWin().dialog('close');
				    }  
				});
			});
		},
		config:{
  			dataGrid:{
  				title:'系统用户列表',
	   			url:'dataList.do',
	   			columns:[[
						{field:'id',checkbox:true},
						{field:'email',title:'邮箱',width:120,sortable:true},
						{field:'nickName',title:'昵称',width:80,sortable:true},
						{field:'state',title:'状态',width:80,align:'center',sortable:true,styler:function(value,row,index){
							if(value == 1){
							  return 'color:red;';  
							}
						},
						formatter:function(value,row,index){
							if(value == 0){
								return "可用";
							}
							if(value == 1){
								return "禁用";
							}
						}},
						{field:'createTime',title:'创建时间',width:120,sortable:true},
						{field:'loginCount',title:'登陆次数',align:'right',width:80,sortable:true},
						{field:'loginTime',title:'登陆时间',width:120,sortable:true}
				]],
				toolbar:[
					{id:'btnadd',text:'添加',btnType:'add'},
					{id:'btnedit',text:'修改',btnType:'edit'},
					{id:'btnedit',text:'修改密码',btnType:'editPwd',iconCls:'icon-edit',handler:function(){
							var selected = _box.utils.getCheckedRows();
							if ( _box.utils.checkSelectOne(selected)){
								_this.editPwdForm().resetForm();
								_this.editPwdForm().find("input[name='id']").val(selected[0].id);
								_this.editPwdForm().find("input[name='email']").val(selected[0].email);
								_this.editPwdWin().window('open'); 
							}
						}},
					{id:'btndelete',text:'删除',btnType:'remove'}
				]
			}
		},
		init:function(){
			_this.initForm();
			_box = new YDataGrid(_this.config); 
			_box.init();
		}
	}
	return _this;
}();

$(function(){
	ian.sysUser.init();
});		