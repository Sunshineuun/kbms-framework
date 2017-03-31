Ext.Loader.setConfig({
			enabled : true
		});
Ext.Loader.setPath('Ext.ux', ctx + '/resources/js/extjs4/ux');
Ext.require(['Ext.ux.form.MultiSelect', 'Ext.ux.form.ItemSelector',
		'Ext.tip.QuickTipManager']);

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

	var userGird = Ext.widget("uxgridview", {
				startLoad : true,
				url : ctx + "/user/userService/list.do",
				editUrl : ctx + "/user/execute.do",
				border : true,
				inViewportShow : true,
				dictionaryParams : [["SF"]],
				columns : [new Ext.grid.RowNumberer({
									width : 8,
									resizable : true
								}), {
							header : "ID",
							editname : "id",
							dataIndex : "ID",
							hidden : true,
							searchoptions : {
								sopt : ["eq", "cn"]
							}
						}, {
							header : "用户名",
							editname : "userName",
							editable : true,
							search : true,
							dataIndex : "USER_NAME",
							searchoptions : {
								sopt : ["eq", "cn"]
							},
							editoptions : {
								allowBlank : false
							}
						}, {
							header : "密码",
							editname : "password",
							dataIndex : "PASSWORD",
							hidden : true,
							editable : true,
							editoptions : {
								inputType : "password",
								allowBlank : false,
								id : "pwd"
							}
						}, {
							header : "密码确认",
							hidden : true,
							editname : "repassword",
							dataIndex : "PASSWORD",
							editable : true,
							editoptions : {
								inputType : "password",
								allowBlank : false,
								id : "repwd",
								validData : {
									password : "pwd",
									repassword : "repwd"
								},
								vtype : "passwordCheck"
							}
						}, {
							header : "真实姓名",
							editname : "realName",
							editable : true,
							search : true,
							dataIndex : "REAL_NAME",
							searchoptions : {
								sopt : ["eq", "cn"]
							},
							editoptions : {
								allowBlank : false
							}
						}, {
							header : "WS-KEY",
							editname : "wsKey",
							editable : false,
							search : true,
							dataIndex : "WS_KEY",
							editoptions : {
								allowBlank : false
							}
						}, {
							header : "是否被锁",
							editname : "isLocked",
							editable : true,
							edittype : "combobox",
							dictionary : "SF",
							search : true,
							stype : "combobox",
							dataIndex : "IS_LOCKED",
							renderer : function(value) {
								return formatter("SF", value);
							},
							searchoptions : {
								sopt : ["eq"]
							}
						}, {
							header : "失效日期",
							editname : "expiredDate",
							editable : true,
							search : true,
							stype : "datefield",
							edittype : "datefield",
							dataIndex : "EXPIRED_DATE",
							searchoptions : {
								sopt : ["ge", "lt"],
								rawToValue : function(rawValue) {
									return rawValue;
								},
								format : "Y-m-d"
							},
							editoptions : {
								format : "Y-m-d"
							}
						}, {
							header : "注销时间",
							search : true,
							stype : "datefield",
							editname : "logoutTime",
							dataIndex : "LOGOUT_TIME",
							searchoptions : {
								sopt : ["ge", "le"]
							}
						}],
				jsonReader : {
					root : 'result.result',
					id : "ID",
					idName : "id",
					totalProperty : "result.totalCount",
					successProperty : 'success'
				},
				navGrid : {
					add : auth.add,
					edit : auth.edit,
					del : auth.del,
					search : true,
					addconfig : {
						afterFormShow : function(editFormWin) {
							var editForm = editFormWin.getComponent(0);
							var pwdCmp = editForm.getComponent(1);
							var repwdCmp = editForm.getComponent(2);
							pwdCmp.enable();
							repwdCmp.enable();
							pwdCmp.show();
							repwdCmp.show();
						}
					},
					editconfig : {
						afterFormShow : function(editFormWin) {
							var editForm = editFormWin.getComponent(0);
							editForm.getComponent(0).hide();
							var pwdCmp = editForm.getComponent(1);
							var repwdCmp = editForm.getComponent(2);
							pwdCmp.disable();
							repwdCmp.disable();
							pwdCmp.hide();
							repwdCmp.hide();
						}
					}
				}
			});

	userGird.tbarbak.insert(3, {
				xtype : "button",
				frame : true,
				hidden : !auth.updatePassword,
				text : "修改密码",
				iconCls : "table_edit",
				handler : function() {
					if (userGird.getSelectionModel().getSelection().length > 0) {
						editPasswordWin.getComponent(0).form.reset();
						editPasswordWin.show();
					} else {
						warn("请选择要修改的用户！");
					}

				}
			});

	// 修改密码的Window
	var editPasswordWin = Ext.create("Ext.window.Window", {
				width : 320,
				layout : 'fit',
				frame : true,
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

						var username = userGird.getSelectionModel()
								.getSelection()[0].get("USER_NAME");

						editForm.getForm().submit({
									url : ctx + "/user/updatePassword.do",
									waitMsg : "请稍等，正在提交数据...",
									params : {
										username : username
									},
									success : function(form, action) {
										Ext.warn.msg("提示", "密码修改成功！");
										editPasswordWin.hide();
									},
									failure : function(form, action) {
										error(action.result.result);
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

	// 授予角色
	userGird.tbarbak.insert(4, {
				xtype : "button",
				frame : true,
				hidden : !auth.grantRoleToUser,
				text : "授予角色",
				iconCls : "cog",
				handler : function() {
					var rec = userGird.getSelectionModel().getSelection()[0];
					if (rec) {
						var userName = rec.get("USER_NAME");
						createAddRolesWin(userName);
					} else {
						warn("请选择授予角色的用户！");
					}

				}
			});

	function createAddRolesWin(userName) {
		var selectedRoleIds = [];
		Ext.Ajax.request({
					async : false,
					url : ctx + "/role/getRoleByUserName.do?userName="
							+ userName,
					success : function(response, opts) {
						var json = Ext.JSON.decode(response.responseText);
						Ext.each(json, function(val) {
									selectedRoleIds.push(val.id)
								});
					},
					failure : function(response, opts) {
						error("角色加载失败！");
					}
				});

		var ds = Ext.create('Ext.data.ArrayStore', {
			fields : ['value', 'label'],
			proxy : {
				type : 'ajax',
				url : ctx
						+ "/dictionary/getDictionaryOnQueryId.do?queryId=selectRole",
				reader : 'array'
			},
			autoLoad : true
		});

		var rolesWin = Ext.create("Ext.window.Window", {
					autoScroll : true,
					title : "授予角色",
					frame : true,
					modal : true,
					items : Ext.widget("form", {
								width : 700,
								bodyPadding : 10,
								height : 300,
								layout : 'fit',
								items : [{
									xtype : 'itemselector',
									name : "roleIds",
									id : "roleitemselector",
									buttons : ["add", "remove"],
									value : selectedRoleIds,
									imagePath : ctx
											+ '/resources/images/icons/',
									store : ds,
									displayField : 'label',
									valueField : 'value',
									fromTitle : "未选的角色",
									toTitle : "选中的角色"
								}]
							}),
					buttons : [{
								xtype : 'button',
								text : '保存',
								handler : function() {
									var formCmp = rolesWin.getComponent(0);
									formCmp.getForm().submit({
												url : ctx
														+ "/user/grantRoleToUser.do",
												params : {
													userName : userName
												},
												waitMsg : "请稍等，正在提交数据...",
												success : function(form, action) {
													Ext.warn.msg("提示",
															"角色保存成功！");
													rolesWin.destroy();
												},
												failure : function(form, action) {
													error("角色保存失败！");
												}
											});
								}
							}, {
								xtype : 'button',
								text : '取消',
								handler : function() {
									rolesWin.destroy();
								}
							}]
				});
		rolesWin.show();
	}

	// 授权
	userGird.tbarbak.insert(5, {
				xtype : "button",
				frame : true,
				text : "授权",
				hidden : !auth.authorize,
				iconCls : "cog",
				handler : function() {
					var rec = userGird.getSelectionModel().getSelection()[0];
					if (rec) {
						var userName = rec.get("USER_NAME");
						createAuthorityWin(userName);
					} else {
						warn("请选择授权用户！");
					}
				}
			});

	Ext.regModel("NavMenu", {
				fields : [{
							name : "id",
							mapping : "id"
						}, {
							name : "text",
							mapping : "name"
						}, {
							name : "leaf",
							mapping : "leaf",
							type : "boolean"
						}, {
							name : "orderFlag",
							mapping : "orderFlag",
							type : "int"
						}, {
							name : "authCode",
							mapping : "authCode"
						}, {
							name : "icon",
							mapping : "iconCls"
						}, {
							name : "parentId",
							mapping : "parentId"
						}, {
							name : "checked",
							mapping : "check",
							type : "boolean"
						}, {
							name : "type",
							mapping : "type"
						}]
			});

	var funcChd = function(node, level, check) {
		if (node.isNode) {
			node.eachChild(function(child) {
						if (check && child.get("orderFlag") < level) {
							child.set("checked", true);
						} else if (!check && child.get("orderFlag") > level) {
							child.set("checked", false);
						}
						funcChd(child, check);
					});
		}
	}

	var chdP = function(node, check) {
		if (node.parentNode) {
			node.parentNode.set("checked", check);
			chdP(node.parentNode, check);
		}
	}

	var chd = function(node, check) {
		node.set("checked", check);
		if (node.isNode) {
			node.eachChild(function(child) {
						chd(child, check);
					});
		}
	}

	function createAuthorityWin(userName) {
		var treePanel = Ext.create("Ext.tree.Panel", {
			autoScroll : true,
			rootVisible : false,
			width : 500,
			height : 400,
			viewConfig : {
				loadingText : "正在加载..."
			},
			store : Ext.create("Ext.data.TreeStore", {
				defaultRootId : "0", // 默认的根节点id
				model : "NavMenu",
				proxy : {
					type : "ajax", // 获取方式
					url : ctx
							+ "/navMenu/getNavMeunFunWithUserAuth.do?id=0&userName="
							+ userName// 获取树节点的地址
				},
				clearOnLoad : true
			}),
			listeners : {
				checkchange : function(node, checked, index) {
					if (node.get("type") == "2") {
						// 设置同级功能节点
						funcChd(node.parentNode, node.get("orderFlag"), checked);
						if (checked)
							chdP(node, checked);
					} else {
						if (!checked)
							chd(node, checked);

						if (checked)
							chdP(node, checked);
					}
				}
			}
		});

		var authorityWin = Ext.create("Ext.window.Window", {
					autoScroll : true,
					title : "授权",
					frame : true,
					modal : true,
					items : [treePanel],
					buttons : [{
						xtype : 'button',
						text : '保存',
						handler : function() {
							var checkedNodes = treePanel.getChecked();
							var nodes = [];
							var node = null;
							for (var i = 0; i < checkedNodes.length; i++) {
								node = checkedNodes[i];
								if (node.get("id") == 0)
									continue;
								nodes.push({
											id : node.get("id"),
											type : node.get("authCode")
													&& node.get("type") == 1
													? 1
													: node.get("authCode")
															? node.get("type")
															: 0,
											authCode : node.get("authCode"),
											parentId : node.get("parentId")
										});
							}
							var jsonNodeStr = Ext.JSON.encode(nodes);
							Ext.Ajax.request({
										url : ctx + "/user/authorizeToUser.do",
										params : {
											userName : userName,
											jsonNodeStr : jsonNodeStr
										},
										success : function(response, opts) {
											Ext.warn.msg("提示", "权限保存成功！");
											authorityWin.destroy();
										},
										failure : function(response, opts) {
											error("权限保存失败！");
										}
									});
						}
					}, {
						xtype : 'button',
						text : '取消',
						handler : function() {
							authorityWin.destroy();
						}
					}]
				});
		authorityWin.show();
	}

	// 修改WS-KEY
	userGird.tbarbak.insert(6, {
		xtype : "button",
		frame : true,
		 hidden : !auth.changeWsKey,
		text : "修改WS-KEY",
		iconCls : "edit",
		handler : function() {
			var userName = null;
			var rec = userGird.getSelectionModel().getSelection()[0];
			if (rec) {
				userName = rec.get("USER_NAME");
			} else {
				warn("请选择用户！");
				return;
			}

			Ext.Msg.confirm('提示', '确定修改WS-KEY?', function(btn) {
				if (btn == 'yes') {
					Ext.Ajax.request({
						async : false,
						url : ctx + "/user/changeWsKey.do?userName=" + userName,
						success : function(response, opts) {
							respWarn(response, function() {
										Ext.warn.msg("提示", "修改成功！");
										userGird.refresh();
									}, function(msg) {
										error(msg);
									})
						},
						failure : function(response, opts) {
							error("WS-KEY 修改失败！");
						}
					});
				}
			});
		}
	});
});