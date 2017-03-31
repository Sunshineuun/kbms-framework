function inputFocus(id) {
	Ext.getCmp(id).setBodyStyle("background",
			"url(" + ctx + "/resources/images/login/input-sltd.png)");
}

function inputBlur(id) {
	Ext.getCmp(id).setBodyStyle("background",
			"url(" + ctx + "/resources/images/login/input-unsltd.png)");
}

function submit() {
	var username = Ext.get("username").getValue();
	var password = Ext.get("password").getValue();

	if (username == null || username.trim() == "") {
		warn("用户名必填！");
		return;
	}

	if (password == null || password.trim() == "") {
		warn("密码必填！");
		return;
	}

	Ext.Ajax.request({
				url : ctx + "/login/login.do",
				params : {
					username : username,
					password : password
				},
				success : function(response, opts) {
					respWarn(response, function(data) {
								window.location.href = ctx
										+ "/forward/index.do";
							}, function(data) {
								error(data);
							})
				},
				failure : function(response, opts) {
					error("登录失败！");
				}
			});
}

Ext.onReady(function() {
	var viewport = Ext.create('Ext.container.Viewport', {
				layout : "absolute"
			});

	var winWidth = viewport.getWidth();
	var winHeight = viewport.getHeight();
	viewport.add({
				xtype : "panel",
				x : winWidth * 0.12,
				y : winHeight * 0.35,
				height : 122,
				width : 299,
				border : false,
				bodyStyle : {
					background : "url(" + ctx + "/resources/images/login/logo.png)"
				}
			}, {
				xtype : "panel",
				x : winWidth * 0.62,
				y : winHeight * 0.15,
				height : 437,
				width : 344,
				border : false,
				layout : "absolute",
				bodyStyle : {
					background : "url(" + ctx + "/resources/images/login/login-bg.png)"
				},
				items : [{
					xtype : "panel",
					border : false,
					x : 30,
					y : 115,
					height : 18,
					width : 74,
					bodyStyle : {
						background : "url(" + ctx + "/resources/images/login/login-title.png)"
					}
				}, {
					xtype : "panel",
					border : false,
					x : 30,
					y : 165,
					id : "usernamePanel",
					height : 41,
					width : 285,
					bodyStyle : {
						paddingTop : "10px",
						background : "url(" + ctx + "/resources/images/login/input-unsltd.png)"
					},
					html : '<span class="inputLabel"><font color="#bcbcbc">用户名：</font></span><span class="inputSpan"><input class="inputText" id="username" onfocus="inputFocus(\'usernamePanel\')" onblur="inputBlur(\'usernamePanel\')" type="text"/></span>'
				}, {
					xtype : "panel",
					border : false,
					x : 30,
					y : 219,
					id : "passwordPanel",
					height : 41,
					width : 285,
					bodyStyle : {
						paddingTop : "10px",
						background : "url(" + ctx + "/resources/images/login/input-unsltd.png)"
					},
					html : '<span class="inputLabel"><font color="#bcbcbc">密&nbsp;&nbsp;码：</font></span><span class="inputSpan"><input id="password" onfocus="inputFocus(\'passwordPanel\')" onblur="inputBlur(\'passwordPanel\')" class="inputText" type="password" onkeypress="if(event.keyCode==13){submit()}"/></span>',
					listeners : {
						specialkey : function(field, e) {
							if (e.getKey() == e.ENTER)
								submit();
						}
					}
				}, {
					xtype : "panel",
					border : false,
					x : 27,
					y : 287,
					height : 47,
					id : "submitButton",
					width : 139,
					bodyStyle : {
						cursor : "pointer",
						background : "url(" + ctx + "/resources/images/login/login-normal.png)"
					},
					listeners : {
						mousedown : {
							element : 'el',
							fn : function() {
								Ext
										.getCmp("submitButton")
										.setBodyStyle("background",
												"url(" + ctx + "/resources/images/login/login-pressed.png)");
							}
						},
						mouseup : {
							element : 'el',
							fn : function() {
								Ext
										.getCmp("submitButton")
										.setBodyStyle("background",
												"url(" + ctx + "/resources/images/login/login-normal.png)");
							}
						},
						click : {
							element : 'el',
							fn : function() {
								submit();
							}
						}
					}
				}, {
					xtype : "panel",
					border : false,
					x : 177,
					y : 287,
					height : 47,
					id : "resetButton",
					width : 139,
					bodyStyle : {
						cursor : "pointer",
						background : "url(" + ctx + "/resources/images/login/reset-normal.png)"
					},
					listeners : {
						mousedown : {
							element : 'el',
							fn : function() {
								Ext
										.getCmp("resetButton")
										.setBodyStyle("background",
												"url(" + ctx + "/resources/images/login/reset-pressed.png)");
							}
						},
						mouseup : {
							element : 'el',
							fn : function() {
								Ext
										.getCmp("resetButton")
										.setBodyStyle("background",
												"url(" + ctx + "/resources/images/login/reset-normal.png)");
							}
						}
					}
				}]
			});
});
