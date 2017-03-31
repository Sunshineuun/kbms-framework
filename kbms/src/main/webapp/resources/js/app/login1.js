Ext.onReady(function() {
			Ext.QuickTips.init();

			var loginForm = Ext.create('Ext.FormPanel', {
						width : 320,
						bodyStyle : 'paddingTop:10px;paddingBottom:5px;',
						broader : false,
						buttonAlign : "center",
						items : [{
									xtype : 'textfield',
									name : 'username',
									labelAlign : "right",
									fieldLabel : '用户名',
									allowBlank : false,
									blankText : "用户名必须输入"
								}, {
									xtype : 'textfield',
									name : 'password',
									labelAlign : "right",
									fieldLabel : '密码',
									allowBlank : false,
									inputType : "password",
									blankText : "密码必须输入",
									listeners : {
										specialkey : function(field, e) {
											if (e.getKey() == e.ENTER)
												submitForm();
										}
									}
								}]
					});

			Ext.create("Ext.window.Window", {
						title : "登录界面",
						items : loginForm,
						iconCls : "lock",
						closable : false,
						buttons : [{
									text : "登录",
									handler : submitForm
								}, {
									text : "重置",
									handler : function() {
										loginForm.form.reset();
									}
								}]
					}).show();

			function submitForm() {
				if (!loginForm.isValid())
					return;

				loginForm.getForm().submit({
							url : ctx + "/login/login.do",
							waitMsg : "请稍等，系统正在登录...",
							success : function(form, action) {
								window.location.href = ctx
										+ "/forward/index.do";
							},
							failure : function(form, action) {
								Ext.Msg.show({
											title : '提示',
											msg : action.result.result,
											buttons : Ext.Msg.OK,
											icon : Ext.Msg.WARNING
										});
							}
						});
			}
		});