Ext.onReady(function() {
	Ext.QuickTips.init();

	var roleGird = Ext.widget("uxgridview", {
				startLoad : true,
				url : ctx + "/role/roleService/list.do",
				editUrl : ctx + "/role/execute.do",
				inViewportShow : true,
				dictionaryParams : [["SF"]],
				columns : [new Ext.grid.RowNumberer({
									width : 8,
									resizable : true
								}), {
							header : "角色编码",
							editname : "id",
							dataIndex : "ID",
							editable : true,
							search : true,
							searchoptions : {
								sopt : ["eq", "cn"]
							},
							editoptions : {
								allowBlank : false
							}
						}, {
							header : "角色名称",
							editname : "roleName",
							editable : true,
							search : true,
							dataIndex : "ROLE_NAME",
							searchoptions : {
								sopt : ["eq", "cn"]
							},
							editoptions : {
								allowBlank : false
							}
						}, {
							header : "是否可用",
							editname : "enable",
							editable : true,
							edittype : "combobox",
							dictionary : "SF",
							search : true,
							stype : "combobox",
							renderer : function(value) {
								return formatter("SF", value);
							},
							dataIndex : "ENABLE",
							editoptions : {
								value : "1"
							},
							searchoptions : {
								sopt : ["eq"]
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
					add : authc.add,
					edit : authc.edit,
					del : authc.del,
					search : true,
					addconfig : {
						afterFormShow : function(editFormWin) {
							var editForm = editFormWin.getComponent(0);
							editForm.getComponent(0).show();
						}
					},
					editconfig : {
						afterFormShow : function(editFormWin) {
							var editForm = editFormWin.getComponent(0);
							editForm.getComponent(0).hide();
						}
					}
				}
			});

	// 授权
	roleGird.tbarbak.insert(3, {
				xtype : "button",
				frame : true,
				hidden : !authc.authorize,
				text : "授权",
				iconCls : "cog",
				handler : function() {
					var rec = roleGird.getSelectionModel().getSelection()[0];
					if (rec) {
						var roleId = rec.get("ID");
						createAuthorityWin(roleId);
					} else {
						warn("请选择授权角色！");
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
							name : "checked",
							mapping : "check",
							type : "boolean"
						}, {
							name : "parentId",
							mapping : "parentId"
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

	function createAuthorityWin(roleId) {
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
							+ "/navMenu/getNavMeunFunWithRoleAuth.do?id=0&roleId="
							+ roleId// 获取树节点的地址
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
					hidden : !authc.authorize,
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
										url : ctx + "/role/authorizeToRole.do",
										loadMask : "正在提交保存权限...",
										params : {
											roleId : roleId,
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

});