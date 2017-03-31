Ext.onReady(function() {
	Ext.regModel("NavMenu", {
				fields : [{
							name : "id",
							mapping : "id"
						}, {
							name : "text",
							mapping : "name"
						}, {
							name : "url",
							mapping : "url"
						}, {
							name : "leaf",
							mapping : "leaf",
							type : "boolean"
						}, {
							name : "expanded",
							mapping : "expanded",
							type : "boolean"
						}, {
							name : "orderFlag",
							mapping : "orderFlag"
						}, {
							name : "viewFlag",
							mapping : "viewFlag"
						}, {
							name : "authCode",
							mapping : "authCode"
						}]
			});

	var navMenuTreeStore = Ext.create("Ext.data.TreeStore", {
				defaultRootId : "0", // 默认的根节点id
				model : "NavMenu",
				proxy : {
					type : "ajax", // 获取方式
					url : ctx + "/navMenu/getAllChildrenById.do"// 获取树节点的地址
				},
				clearOnLoad : true,
				nodeParam : "id"
			});

	Ext.apply(Ext.form.field.VTypes, {
				authCodeCheck : function(val, field) {
					var authCodeCmp = Ext.getCmp(field.validData.authCode);
					var leafCmp = Ext.getCmp(field.validData.leaf);

					if (leafCmp.getValue() == 1 || leafCmp.getValue() == "1")
						if (!authCodeCmp.getValue())
							return false;
					return true;
				},
				authCodeCheckText : "权限编码必填！"
			})

	var createEditTreeWin = function(url, view, rec, isAdd) {
		var editTreeWin = Ext.create("Ext.window.Window", {
			width : 320,// 要比表单的宽高大一点
			layout : 'fit',
			frame : true,
			title : isAdd ? "新增目录" : "修改目录",
			modal : true, // 开启遮罩
			items : [{
				xtype : 'form',
				border : false,
				bodyStyle : 'paddingTop:5px;paddingBottom:5px;',
				frame : false,
				defaults : {
					xtype : "textfield",
					labelAlign : "right"
				},
				items : [{
							fieldLabel : "名称",
							name : "name",
							value : isAdd ? null : rec.get("text"),
							allowBlank : false
						}, {
							name : "url",
							fieldLabel : "地址",
							value : isAdd ? null : rec.get("url")
						}, {
							name : "authCode",
							id : "authCode",
							fieldLabel : "权限编码",
							allowBlank : !(!isAdd && rec.get("leaf")),
							value : isAdd ? null : rec.get("authCode"),
							validData : {
								authCode : "authCode",
								leaf : "leaf"
							},
							vtype : "authCodeCheck"
						}, createLocalCombobox([[0, "是"], [1, "否"]], true, {
									name : "leaf",
									id : "leaf",
									value : isAdd ? 0 : rec.get("leaf") ? 1 : 0,
									hidden : isAdd ? false : true,
									fieldLabel : "是否文件夹",
									validData : {
										authCode : "authCode",
										leaf : "leaf"
									},
									vtype : "authCodeCheck"
								}), {
							name : "iconCls",
							value : isAdd ? null : rec.get("iconCls"),
							fieldLabel : "图标"
						}, createLocalCombobox([[0, "否"], [1, "是"]], true, {
									name : "expanded",
									value : isAdd ? 0 : rec.get("expanded")
											? 1
											: 0,
									fieldLabel : "是否展开"
								}), {
							xtype : "numberfield",
							value : isAdd ? 9999 : rec.get("orderFlag"),
							name : "orderFlag",
							fieldLabel : "显示顺序"
						}, createLocalCombobox([[0, "否"], [1, "是"]], true, {
									name : "viewFlag",
									value : isAdd ? 1 : rec.get("viewFlag")
											? 1
											: 0,
									fieldLabel : "是否可见"
								})]
			}],
			buttons : [{
				xtype : 'button',
				text : '提交',
				handler : function() {
					var editForm = editTreeWin.getComponent(0);
					if (!editForm.isValid())
						return;

					var params = new Object();
					if (isAdd) {
						params["parentId"] = rec.get("id");
						params["type"] = 1;
					} else {
						params["id"] = rec.get("id");
						params["parentId"] = rec.get("parentId");
						params["type"] = 1;
					}
					editForm.getForm().submit({
						url : url,
						waitMsg : "请稍等，正在提交新增数据...",
						params : params,
						success : function(form, action) {
							rec.removeAll();

							var refreshId = null;
							var refreshNode = null;
							if (isAdd) {
								refreshId = rec.get("id");
								refreshNode = rec;
							} else {
								refreshId = rec.get("parentId");
								refreshNode = navMenuTreeStore.getNodeById(rec
										.get("parentId"));
							}

							navMenuTreeStore.getProxy().setExtraParam("id",
									refreshId);
							navMenuTreeStore.load({
										node : refreshNode,
										callback : function() {
											view.refresh()
										}
									});
							editTreeWin.destroy();
						},
						failure : function(form, action) {
							error("新增目录失败！");
						}
					});
				}
			}, {
				xtype : 'button',
				text : '取消',
				handler : function() {
					editTreeWin.destroy();
				}
			}]
		}).show();
	};

	var cutCollection = null;
	var createRrightMenu = function(XY, view, rec, isLeaf) {
		Ext.create("Ext.menu.Menu", {
			items : [{
				iconCls : "add",
				text : "新增目录",
				hidden : isLeaf,
				handler : function() {
					createEditTreeWin(ctx + "/navMenu/add.do", view, rec, true);
				}
			}, {
				iconCls : "edit",
				text : "修改目录",
				handler : function() {
					createEditTreeWin(ctx + "/navMenu/update.do", view, rec,
							false);
				}
			}, {
				iconCls : "delete",
				text : "删除目录",
				handler : function() {
					Ext.Msg.confirm('确认', '确定删除此文件吗？', function(btn, text) {
						if (btn == 'yes') {
							Ext.Ajax.request({
										url : ctx + "/navMenu/remove.do",
										params : {
											id : rec.get("id")
										},
										success : function(response, opts) {
											var result = Ext.JSON.decode(response.responseText);
											if(result.success){
												rec.remove();
												view.refresh();
											}else{
												error(result.result);
											}
										},
										failure : function(response, opts) {
											var result = eval(response.responseText);
											error(result.result);
										}
									});
						}
					});
				}
			}, {
				iconCls : "cut",
				text : "剪&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;切",
				handler : function() {
					cutCollection = rec;
				}
			}, {
				iconCls : "paste",
				text : "粘&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;贴",
				disabled : cutCollection ? false : true,
				handler : function() {
					if (cutCollection) {
						Ext.Ajax.request({
							url : ctx + "/navMenu/cutNavMenu.do",
							params : {
								id : cutCollection.get("id"),
								parentId : rec.get("id")
							},
							success : function(response, opts) {
								respWarn(response, function() {
									refreshNavMenuTree(rec);
									refreshNavMenuTree(cutCollection.parentNode);
								}, function(msg) {
									error(msg);
								});
							},
							failure : function(response, opts) {
								error("粘贴出错！");
							}
						});
					}
				}
			}]
		}).showAt(XY);
	}

	function refreshNavMenuTree(node) {
		if (node) {
			var store = treePanel.getStore();
			store.getProxy().setExtraParam("id", node.get("id"));
			store.getProxy().url = ctx + "/navMenu/getAllChildrenById.do";
			store.load({
						node : node,
						callback : function() {
							treePanel.getView().refresh()
						}
					});
		} else {
			var store = treePanel.getStore();
			store.getProxy().setExtraParam("id", "0");
			store.getProxy().url = ctx + "/navMenu/getAllChildrenById.do";
			store.load({
						node : treePanel.getRootNode(),
						callback : function() {
							treePanel.getView().refresh()
						}
					});
		}
	}

	var createAddNavWin = function() {
		var addNavWin = Ext.create("Ext.window.Window", {
			width : 320,// 要比表单的宽高大一点
			layout : 'fit',
			frame : true,
			title : "新增导航菜单",
			modal : true, // 开启遮罩
			items : [{
						xtype : 'form',
						border : false,
						bodyStyle : 'paddingTop:5px;paddingBottom:5px;',
						frame : false,
						defaults : {
							xtype : "textfield",
							labelAlign : "right"
						},
						items : [
								{
									fieldLabel : "名称",
									name : "name",
									allowBlank : false
								},
								createLocalCombobox([[0, "否"], [1, "是"]], true,
										{
											name : "expanded",
											fieldLabel : "是否展开"
										}),
								{
									name : "iconCls",
									fieldLabel : "图标"
								},
								{
									xtype : "numberfield",
									value : 9999,
									name : "orderFlag",
									fieldLabel : "显示顺序"
								},
								createLocalCombobox([[1, "是"], [0, "否"]], true,
										{
											name : "viewFlag",
											fieldLabel : "是否可见"
										})]
					}],
			buttons : [{
						xtype : 'button',
						text : '提交',
						handler : function() {
							var editForm = addNavWin.getComponent(0);
							if (!editForm.isValid())
								return;

							var params = new Object();
							params["parentId"] = "0";
							params["leaf"] = 0;
							params["type"] = 1;

							editForm.getForm().submit({
										url : ctx + "/navMenu/add.do",
										waitMsg : "请稍等，正在提交新增数据...",
										params : params,
										success : function(form, action) {
											navMenuTreeStore.reload();
											addNavWin.destroy();
										},
										failure : function(form, action) {
											Ext.Msg.show({
														title : '提示',
														msg : '新增导航菜单失败！',
														buttons : Ext.Msg.OK,
														icon : Ext.Msg.ERROR
													});
										}
									});
						}
					}, {
						xtype : 'button',
						text : '取消',
						handler : function() {
							addNavWin.destroy();
						}
					}]
		}).show();
	};

	var treePanel = Ext.create("Ext.tree.Panel", {
				title : "导航菜单",
				tbar : [{
							xtype : "button",
							text : '新增导航菜单',
							iconCls : "add",
							handler : function() {
								createAddNavWin();
							}
						}, {
							xtype : "button",
							text : '刷新导航菜单',
							iconCls : "refresh",
							handler : function() {
								refreshNavMenuTree();
							}
						}],
				autoScroll : true,
				rootVisible : false,
				viewConfig : {
					loadingText : "正在加载..."
				},
				store : navMenuTreeStore,
				listeners : {
					itemcontextmenu : function(view, record, item, index, e) {
						e.preventDefault();
						e.stopEvent();
						createRrightMenu(e.getXY(), view, record, record
										.get("leaf"));
					},
					select : function(rowModel, record) {
						if (record.get("leaf")) {
							functionGrid
									.setUrl(ctx
											+ "/navMenu/findFunctionsByNodeId.do?nodeId="
											+ record.get("id")).refresh();
						}
					}
				}
			});

	var functionGrid = Ext.widget("uxgridview", {
		startLoad : false,
		editUrl : ctx + "/navMenu/functionExecute.do",
		title : "功能点",
		border : true,
		inViewportShow : false,
		pageSize : 100,
		columns : [new Ext.grid.RowNumberer({
							width : 8,
							resizable : true
						}), {
					header : "ID",
					editname : "id",
					hidden : true,
					dataIndex : "id",
					searchoptions : {
						sopt : ["eq", "cn"]
					}
				}, {
					header : "功能名称",
					editname : "name",
					editable : true,
					dataIndex : "name",
					searchoptions : {
						sopt : ["eq", "cn"]
					},
					editoptions : {
						allowBlank : false
					}
				}, {
					header : "权限编码",
					editname : "authCode",
					editable : true,
					dataIndex : "authCode",
					searchoptions : {
						sopt : ["eq", "cn"]
					},
					editoptions : {
						allowBlank : false
					}
				}, {
					header : "功能类型",
					editname : "type",
					editable : true,
					hidden : true,
					edittype : "combobox",
					dataIndex : "type",
					editoptions : {
						values : [[2, "普通功能"], [3, "WebService接口服务"]],
						allowBlank : false
					}
				}, {
					header : "权限级别",
					editname : "orderFlag",
					editable : true,
					edittype : "numberfield",
					dataIndex : "orderFlag",
					searchoptions : {
						sopt : ["eq", "cn"]
					},
					editoptions : {
						allowBlank : false
					}
				}],
		jsonReader : {
			root : 'result.result',
			id : "id",
			idName : "id",
			totalProperty : "result.totalCount",
			successProperty : 'success'
		},
		navGrid : {
			add : true,
			edit : true,
			del : true,
			search : false,
			addconfig : {
				beforeSubmit : function() {
					var parentId = treePanel.getSelectionModel().getSelection()[0]
							.get("id");
					return {
						parentId : parentId
					};
				}
			},
			editconfig : {
				beforeSubmit : function() {
					var parentId = treePanel.getSelectionModel().getSelection()[0]
							.get("id");
					return {
						parentId : parentId
					};
				}
			}
		}
	});

	Ext.create('Ext.container.Viewport', {
				layout : 'border',
				items : [{
							region : 'west',
							width : "50%",
							layout : "fit",
							items : [treePanel]
						}, {
							region : 'east',
							width : "50%",
							layout : "fit",
							items : [functionGrid]
						}]
			});

});