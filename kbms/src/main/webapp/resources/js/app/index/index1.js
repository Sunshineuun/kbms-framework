Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.apply(Ext.form.field.VTypes, {
				passwordCheck : function(val, field) {
					var passwordCmp = Ext.getCmp(field.validData.password);
					var repasswordCmp = Ext.getCmp(field.validData.repassword);

					if (passwordCmp.getRawValue() != repasswordCmp
							.getRawValue())
						return false;

					return true;
				},
				passwordCheckText : "密码输入不正确！"
			})

	var findCount = 1;
	var cacheCount = 0;
	var searchName = null;

	Ext.create('Ext.Viewport', {
		layout : 'border',
		title : '知识管理系统',
		items : [{
			region : 'north',
			xtype : 'container',
			height : 50,
			id : 'app-header',
			layout : {
				type : 'hbox',
				align : 'middle'
			},
			defaults : {
				xtype : 'component'
			},
			items : [{
				id : 'app-header-title',
				flex : 8,
				html : '<img style="margin-left:20px;margin-top:5px;height:40px;" src="'
						+ ctx
						+ '/resources/images/index/logo_wn.png"/><font style="margin-left:20px;font-size: 25px;">知识管理系统</font>'
			}, {
				xtype : "toolbar",
				border : false,
				style : "background: none",
				layout : {
					align : 'top'
				},
				flex : 1,
				height : 50,
				items : [{
							xtype : "button",
							height : 40,
							border : false,
							scale : "large",
							iconAlign : "top",
							tooltip : "欢迎，" + currUser.realName,
							icon : ctx + '/resources/images/index/user.png',
							handler : function() {
								currUserInfoWin.show();
							}
						}, {
							xtype : "button",
							height : 40,
							border : false,
							scale : "large",
							iconAlign : "top",
							tooltip : "修改密码",
							icon : ctx
									+ '/resources/images/index/change_pwd.png',
							handler : function() {
								editPasswordWin.show();
							}
						}, {
							xtype : "button",
							height : 40,
							border : false,
							scale : "large",
							iconAlign : "top",
							tooltip : "注销",
							icon : ctx + '/resources/images/index/quit.png',
							handler : function() {
								window.location.href = ctx + "/login/logout.do";
							}
						}]
			}]
		}, {
			title : "导航栏",
			region : "west",
			width : 250,
			iconCls : "application_go",
			id : "mainNav",
			split : true,
			collapsible : true,
			layout : 'accordion',
			tbar : [{
				xtype : "searchfield",
				fieldLabel : "搜索",
				labelWidth : 40,
				labelAlign : "right",
				width : 225,
				emptyText : "请输入搜索的名称...",
				handler : function(name) {
					var mainNav = Ext.getCmp("mainNav");
					if (searchName == name) {
						findCount++;
					} else {
						findCount = 1;
					}
					cacheCount = findCount;
					searchName = name;

					var treePanel = null;
					for (var i = 0;; i++) {
						treePanel = mainNav.getComponent(i);
						if (treePanel) {
							var fNode = findNode(treePanel.getStore()
											.getRootNode(), name);
							if (fNode == null)
								continue;
							treePanel.expand();
							expandNode(fNode);
							treePanel.getSelectionModel().select(fNode);
							break;
						} else {
							break;
						}
					}
				}
			}],
			layoutConfig : {
				animate : true
			}
		}, {
			region : "center",
			layout : "fit",
			items : tabPanel
		}]
	});

	function expandNode(node) {
		if (node.parentNode) {
			node.parentNode.expand(true);
			expandNode(node.parentNode);
		}
	}

	function findNode(node, name) {
		var fNode = null;
		if (node.isNode) {
			node.eachChild(function(child) {
						// 用户管理
						if (child.get("text").indexOf(name) > -1) {
							cacheCount--;
							fNode = child;
						}
						if (fNode && cacheCount == 0)
							return false;
						else
							fNode = findNode(child, name);
					});
		}
		if (fNode && cacheCount == 0)
			return fNode;
	}

	// 修改密码的Window
	var editPasswordWin = Ext.create("Ext.window.Window", {
				width : 320,
				layout : 'fit',
				frame : true,
				iconCls : "key",
				closeAction : "hide",
				title : "修改密码",
				modal : true,
				items : [{
							xtype : 'form',
							border : false,
							bodyStyle : 'paddingTop:5px;paddingBottom:5px;',
							frame : false,
							defaults : {
								xtype : "textfield",
								labelAlign : "right",
								inputType : "password",
								allowBlank : false
							},
							items : [{
										fieldLabel : "旧密码",
										id : "oldPassword",
										name : "oldPassword"
									}, {
										fieldLabel : "新密码",
										id : "password",
										name : "password"
									}, {
										fieldLabel : "密码确认",
										id : "repassword",
										name : "repassword",
										validData : {
											password : "password",
											repassword : "repassword"
										},
										vtype : "passwordCheck"
									}]
						}],
				buttons : [{
					xtype : 'button',
					text : '提交',
					handler : function() {
						var editForm = editPasswordWin.getComponent(0);
						if (!editForm.isValid())
							return;

						editForm.getForm().submit({
							url : ctx + "/user/updateCurrUserPassword.do",
							waitMsg : "请稍等，正在提交数据...",
							success : function(form, action) {
								Ext.warn.msg("提示", "密码修改成功！");
								editPasswordWin.hide();
							},
							failure : function(form, action) {
								if (action != null && action.result != null
										&& action.result.result != null)
									error(action.result.result);
								else
									error("密码修改出错！");
							}
						});
					}
				}, {
					xtype : 'button',
					text : '取消',
					handler : function() {
						editPasswordWin.hide();
					}
				}]

			});

	// 当前用户信息Window
	var currUserInfoWin = Ext.create("Ext.window.Window", {
		frame : true,
		iconCls : "user",
		closeAction : "hide",
		title : "当前用户信息",
		modal : true,
		width : 400,
		height : 300,
		layout : "fit",
		items : [{
			layout : {
				type : 'vbox'
			},
			border : false,
			padding : "5px",
			bodyStyle : 'background:none',
			items : [{
				layout : "hbox",
				border : false,
				bodyStyle : 'background:none',
				items : [{
							xtype : "panel",
							width : 120,
							height : 120,
							bodyStyle : 'background:none'
						}, {
							layout : "vbox",
							border : false,
							bodyStyle : 'background:none',
							items : [{
										width : 218,
										height : 30,
										border : false,
										bodyStyle : 'background:none',
										padding : "5px 0px 5px 20px",
										html : '<B>登录名：</B>'
												+ currUser.userName
									}, {
										width : 218,
										height : 30,
										border : false,
										bodyStyle : 'background:none',
										padding : "5px 0px 5px 20px",
										html : '<B>真实姓名：</B>'
												+ currUser.realName
									}, {
										width : 218,
										height : 30,
										bodyStyle : 'background:none',
										padding : "5px 0px 5px 20px",
										border : false,
										html : '<B>失效日期：</B>'
												+ (currUser.expiredDate
														? currUser.expiredDate
														: "永久")
									}]
						}]
			}, {
				layout : "vbox",
				bodyStyle : 'background:none',
				border : false,
				items : [{
					layout : "hbox",
					border : false,
					bodyStyle : 'background:none',
					items : [{
						height : 30,
						width : 320,
						bodyStyle : 'background:none',
						padding : "5px 0px 5px 10px",
						border : false,
						html : "<B>WS-KEY：</B><span id='wsKey'>"
								+ (currUser.wsKey ? currUser.wsKey : "无")
								+ "</span>"
					}, {
						height : 30,
						bodyStyle : 'background:none',
						border : false,
						padding : "0px 0px 0px 5px",
						items : [{
							xtype : "button",
							text : "重新生成",
							handler : function() {
								Ext.Msg.confirm('提示', '确定重新生成WS-KEY?',
										function(btn) {
											if (btn == 'yes') {
												Ext.Ajax.request({
													async : false,
													url : ctx
															+ "/user/changeWsKey.do?userName="
															+ currUser.userName,
													success : function(
															response, opts) {
														respWarn(
																response,
																function(wsKey) {
																	Ext.warn
																			.msg(
																					"提示",
																					"生成成功！");
																	var wsKeySpan = Ext
																			.get("wsKey");
																	wsKeySpan
																			.clean();
																	wsKeySpan
																			.setHTML(wsKey);
																},
																function(msg) {
																	error(msg);
																})
													},
													failure : function(
															response, opts) {
														error("WS-KEY 修改失败！");
													}
												});
											}
										});
							}
						}]
					}]
				}, {
					height : 30,
					bodyStyle : 'background:none',
					padding : "5px 0px 5px 10px",
					border : false,
					html : '<B>角色：</B>' + getCurrUserRolesStr(currUser.roles)
				}]
			}]
		}]
	});

	function getCurrUserRolesStr(rolesArray) {
		if (!rolesArray)
			return "";
		var rolesStr = "";
		for (var i = 0; i < rolesArray.length; i++) {
			if (i < (rolesArray.length - 1)) {
				rolesStr += rolesArray[i].roleName + ",";
			} else {
				rolesStr += rolesArray[i].roleName;
			}
		}
		return rolesStr;
	}

	Ext.Ajax.request({
				params : {
					id : "0"
				},
				url : ctx + "/navMenu/getNavMenuChildrenByIdWithAuth.do",
				method : "post",
				callback : function(options, success, response) {
					addTree(eval(response.responseText));
				}
			});

	var hideMask = function() {
		Ext.get('loading').remove();
		Ext.fly('loading-mask').animate({
					opacity : 0,
					remove : true
				});
	};

	Ext.defer(hideMask, 250);
});