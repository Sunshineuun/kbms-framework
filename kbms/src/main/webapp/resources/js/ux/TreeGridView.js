Ext.define('Ext.ux.TreeGridView', {
	extend : 'Ext.tree.Panel',
	alias : 'widget.uxtreegridview',
	initComponent : function() {
		var me = this;

		me.initGrid();

		this.callParent(arguments);

		me.createViewport();
	},
	beforeRequest : null,
	toolTipParams  : null, //鼠标悬停
	url : null,
	dictUrl : ctx + "/dictionary/loadDictionary.do",
	editUrl : null,
	storeFields : null,
	startLoad : true,
	forceFit : true,
	viewport : null,
	inViewportShow : false,
	navGrid : null,
	searchFieldCombobox : null,
	autoTbar : true,
	hasRightMenu : true,
	addButton : null,
	editButton : null,
	delButton : null,
	editHandler : null,
	sortname : null,
	sortorder : null,
	delHandler : null,
	columnLines : true,
	dictionaryParams : null,
	enableColumnHide : false,
	editWinWidth : 340,
	viewConfig : {
		stripeRows : true
	},
	loadDictionary : function() {
		var me = this;
		if (!me.dictUrl || !me.dictionaryParams)
			return;

		var params = {};

		if (me.dictionaryParams[0])
			params["typeCodeStr"] = array2Str(me.dictionaryParams[0]);

		if (me.dictionaryParams[1])
			params["queryIdStr"] = array2Str(me.dictionaryParams[1]);

		if (!Ext.isEmpty(params)) {
			Ext.Ajax.request({
						async : false,
						url : me.dictUrl,
						params : params,
						success : function(response, opts) {
							var obj = Ext.decode(response.responseText);
							if (obj) {
								if (dictionary == null)
									dictionary = new Object();

								for (var key in obj) {
									dictionary[key] = eval(obj[key]);
								}
							}
						},
						failure : function(response, opts) {
							error('数据字典加载失败！');
						}
					});
		}
	},
	initGrid : function() {
		var me = this;

		me.initNavGrid();

		me.initToolTip();
		
		me.initAddHandler();
		me.initEditHandler();
		me.initDelHandler();

		me.initRightMenu();

		me.doBeforeRequest();
		me.loadDictionary();

		me.createStroe();

		me.createTbar();
	},
	doBeforeRequest : function() {
		var me = this;

		if (me.beforeRequest != null && typeof(me.beforeRequest) == "function")
			me.beforeRequest.call();
	},
	beforeload : function(postParams) {
		var me = this;

		var beforeSubmit = me.navGrid.searchconfig.beforeSubmit;
		var addParams = null;
		if (beforeSubmit && typeof(beforeSubmit) == "function")
			addParams = beforeSubmit();

		me.store.on('beforeload', function(store, options) {
					Ext.apply(postParams, addParams);
					if (me.searchParams)
						Ext.apply(postParams, me.searchParams);
					store.proxy.extraParams = postParams;
				});
	},
	setUrl : function(url, params) {
		var me = this;

		me.url = url;
		me.store.on('beforeload', function(store, options) {
					store.proxy.url = url;
					me.searchParams = params;
					store.proxy.extraParams = params;
				});
		return me;
	},
	addActionColumn : function(actionCol, index) {
		this.headerCt.insert(this.columns.length + index, actionCol);
		this.getView().refresh();
	},
	refresh : function() {
		this.store.load();
	},
	setSearchValue : function(value) {
		var me = this;
		if (me.simpleSearchCmp) {
			me.simpleSearchCmp.setValue(value);
			me.simpleSearchCmp.onTrigger2Click();
		}
	},
	getSearchColumns : function() {
		var me = this;
		if (!me.navGrid.simplesearch && !me.navGrid.search)
			return null;

		var searchColumns = [];
		var columns = me.columns;
		for (var i = 0; i < columns.length; i++) {
			if (columns[i].search)
				searchColumns.push(columns[i]);
		}
		return searchColumns;
	},
	getEditColumns : function() {
		var me = this;
		if (!me.navGrid.edit)
			return null;

		var editColumns = [];
		var columns = me.columns;
		for (var i = 0; i < columns.length; i++) {
			if (columns[i].editable)
				editColumns.push(columns[i]);
		}
		return editColumns;
	},
	initAddHandler : function() {
		var me = this;

		if (!me.addRootHandler) {
			me.addRootHandler = function(o) {
				var beforeFormShow = me.navGrid.addconfig.beforeFormShow;
				if (beforeFormShow && typeof(beforeFormShow) == "function") {
					var r = beforeFormShow(me);
					if (r != null && !r)
						return;
				}

				me.addFlag = "addRoot";

				var editFormWin = me.getEditFormWin(me.getEditColumns(), null,
						"addRoot");
				editFormWin.setTitle(me.addButton.text);
				editFormWin.setIconCls("table_add");
				editFormWin.getComponent(0).form.reset();

				var afterFormShow = me.navGrid.addconfig.afterFormShow;
				if (afterFormShow && typeof(afterFormShow) == "function")
					afterFormShow(editFormWin, me);

				editFormWin.queryById("add_win_submit_button").show();
				editFormWin.queryById("edit_win_submit_button").hide();
				editFormWin.show();
			};
		}

		if (!me.addChildHandler) {
			me.addChildHandler = function(o) {
				var selectedDatas = me.getSelectionModel().getSelection();
				if (selectedDatas == null || selectedDatas.length == 0) {
					warn("请选择需要新增的节点！");
					return;
				}

				var beforeFormShow = me.navGrid.addconfig.beforeFormShow;
				if (beforeFormShow && typeof(beforeFormShow) == "function") {
					var r = beforeFormShow(me, selectedData);
					if (r != null && !r)
						return;
				}

				me.addFlag = "addChild";

				var editFormWin = me.getEditFormWin(me.getEditColumns());
				editFormWin.setTitle(me.addButton.text);
				editFormWin.setIconCls("table_add");
				editFormWin.getComponent(0).form.reset();

				var afterFormShow = me.navGrid.addconfig.afterFormShow;
				if (afterFormShow && typeof(afterFormShow) == "function")
					afterFormShow(editFormWin, me);

				editFormWin.queryById("add_win_submit_button").show();
				editFormWin.queryById("edit_win_submit_button").hide();
				editFormWin.show();
			}
		}

		if (!me.addParentHandler) {
			me.addParentHandler = function(o) {
				var selectedDatas = me.getSelectionModel().getSelection();
				if (selectedDatas == null || selectedDatas.length == 0) {
					warn("请选择需要新增的节点！");
					return;
				}

				var beforeFormShow = me.navGrid.addconfig.beforeFormShow;
				if (beforeFormShow && typeof(beforeFormShow) == "function") {
					var r = beforeFormShow(me, selectedData);
					if (r != null && !r)
						return;
				}

				me.addFlag = "addParent";

				var editFormWin = me.getEditFormWin(me.getEditColumns());
				editFormWin.setTitle(me.addButton.text);
				editFormWin.setIconCls("table_add");
				editFormWin.getComponent(0).form.reset();

				var afterFormShow = me.navGrid.addconfig.afterFormShow;
				if (afterFormShow && typeof(afterFormShow) == "function")
					afterFormShow(editFormWin, me);

				editFormWin.queryById("add_win_submit_button").show();
				editFormWin.queryById("edit_win_submit_button").hide();
				editFormWin.show();
			}
		}

	},
	getEditFormWin : function(editColumns) {
		var me = this;

		if (!me.editFormWin)
			me.editFormWin = me.createEditFormWin(editColumns);

		return me.editFormWin;
	},
	getEditForm : function() {
		return this.editFormWin.getComponent(0);
	},
	addRootSubmit : function(formCmp) {
		var me = this;
		var postParams = {
			oper : 'addRoot',
			parentId : "0"
		};
		var beforeSubmit = me.navGrid.addconfig.beforeSubmit;
		if (beforeSubmit && typeof(beforeSubmit) == "function")
			Ext.apply(postParams, beforeSubmit(formCmp.form.getValues()));

		formCmp.getForm().submit({
					url : me.editUrl,
					waitMsg : "请稍等，正在提交新增数据...",
					params : postParams,
					success : function(form, action) {
						me.editFormWin.hide();
						me.refresh();

						var afterSubmit = me.navGrid.addconfig.afterSubmit;
						if (afterSubmit && typeof(afterSubmit) == "function")
							afterSubmit();
					},
					failure : function(form, action) {
						var errorMsg = '新增失败！';
						if (action && action.result && action.result.result)
							errorMsg = action.result.result;
						error(errorMsg);
					}
				});
	},
	addChildSubmit : function(formCmp, selectedData) {
		var me = this;
		var postParams = {
			oper : 'addChild',
			parentId : selectedData.get("id")
		};
		var beforeSubmit = me.navGrid.addconfig.beforeSubmit;
		if (beforeSubmit && typeof(beforeSubmit) == "function")
			Ext.apply(postParams, beforeSubmit(formCmp.form.getValues()));

		formCmp.getForm().submit({
					url : me.editUrl,
					waitMsg : "请稍等，正在提交新增数据...",
					params : postParams,
					success : function(form, action) {
						me.editFormWin.hide();
						me.refresh();

						var afterSubmit = me.navGrid.addconfig.afterSubmit;
						if (afterSubmit && typeof(afterSubmit) == "function")
							afterSubmit();
					},
					failure : function(form, action) {
						var errorMsg = '新增失败！';
						if (action && action.result && action.result.result)
							errorMsg = action.result.result;
						error(errorMsg);
					}
				});
	},
	addParentSubmit : function(formCmp, selectedData) {
		var me = this;
		var postParams = {
			oper : 'addParent',
			parentId : selectedData.get("parentId"),
			currNodeId : selectedData.get("id")
		};
		var beforeSubmit = me.navGrid.addconfig.beforeSubmit;
		if (beforeSubmit && typeof(beforeSubmit) == "function")
			Ext.apply(postParams, beforeSubmit(formCmp.form.getValues()));

		formCmp.getForm().submit({
					url : me.editUrl,
					waitMsg : "请稍等，正在提交新增数据...",
					params : postParams,
					success : function(form, action) {
						me.editFormWin.hide();
						me.refresh();

						var afterSubmit = me.navGrid.addconfig.afterSubmit;
						if (afterSubmit && typeof(afterSubmit) == "function")
							afterSubmit();
					},
					failure : function(form, action) {
						var errorMsg = '新增失败！';
						if (action && action.result && action.result.result)
							errorMsg = action.result.result;
						error(errorMsg);
					}
				});
	},
	editSubmit : function(formCmp) {
		var me = this;

		var data = me.getSelectionModel().getSelection();
		var idName = "ID";
		var idKeyName = "id";
		if (me.jsonReader && me.jsonReader.id)
			idName = me.jsonReader.id;
		if (me.jsonReader && me.jsonReader.idName)
			idKeyName = me.jsonReader.idName;
		var id = null;
		if (idName) {
			id = data[0].get(idName);
		}
		var postParams = {
			oper : 'edit',
			parentId : data[0].get("parentId")
		};
		postParams[idKeyName] = id;
		var beforeSubmit = me.navGrid.editconfig.beforeSubmit;
		if (beforeSubmit && typeof(beforeSubmit) == "function")
			Ext.apply(postParams, beforeSubmit(formCmp.form.getValues()));

		formCmp.getForm().submit({
					url : me.editUrl,
					waitMsg : "请稍等，正在提交修改数据...",
					params : postParams,
					success : function(form, action) {
						me.editFormWin.hide();
						me.store.load();

						var afterSubmit = me.navGrid.editconfig.afterSubmit;
						if (afterSubmit && typeof(afterSubmit) == "function")
							afterSubmit();
					},
					failure : function(form, action) {
						var errorMsg = '修改失败！';
						if (action && action.result && action.result.result)
							errorMsg = action.result.result;
						error(errorMsg);
					}
				});
	},
	delSubmit : function(id) {
		var me = this;

		var idKeyName = "id";
		if (me.jsonReader && me.jsonReader.idName)
			idKeyName = me.jsonReader.idName;

		var postParams = {
			oper : 'del'
		};
		postParams[idKeyName] = id;

		var beforeSubmit = me.navGrid.delconfig.beforeSubmit;
		if (beforeSubmit && typeof(beforeSubmit) == "function")
			Ext.apply(postParams, beforeSubmit());

		Ext.Ajax.request({
					url : me.editUrl,
					params : postParams,
					success : function(response, opts) {
						respWarn(response, function() {
									me.refresh();
									var afterSubmit = me.navGrid.delconfig.afterSubmit;
									if (afterSubmit
											&& typeof(afterSubmit) == "function")
										me.navGrid.delconfig.afterSubmit();
								});
					},
					failure : function(response, opts) {
						error("删除失败！");
					}
				});
	},
	batchDelSubmit : function(ids) {
		var me = this;

		var idKeyName = "ids";
		if (me.jsonReader && me.jsonReader.idName)
			idKeyName = me.jsonReader.idName;

		var postParams = {
			oper : 'batchDel'
		};
		postParams[idKeyName] = ids;

		var beforeSubmit = me.navGrid.delconfig.beforeSubmit;
		if (beforeSubmit && typeof(beforeSubmit) == "function")
			Ext.apply(postParams, beforeSubmit());

		Ext.Ajax.request({
					url : me.editUrl,
					params : postParams,
					success : function(response, opts) {
						if (response.responseText) {
							var jsonMsg = Ext.JSON
									.decode(response.responseText);
							if (!jsonMsg.success) {
								error(jsonMsg.result);
								return;
							}
						}

						me.refresh();

						var afterSubmit = me.navGrid.delconfig.afterSubmit;
						if (afterSubmit && typeof(afterSubmit) == "function")
							me.navGrid.delconfig.afterSubmit();
					},
					failure : function(response, opts) {
						error('删除失败！');
					}
				});
	},
	createEditForm : function() {
		return Ext.create("Ext.form.Panel", {
					border : false,
					autoScroll : true,
					bodyStyle : 'paddingTop:5px;paddingBottom:5px;',
					frame : false
				});
	},
	editWinButtons : [],
	createEditFormWin : function(editColumns) {
		var me = this;
		me.editWinButtons.push({
					xtype : 'button',
					text : '提交',
					itemId : "add_win_submit_button",
					handler : function() {
						var editForm = editFormWin.getComponent(0);
						if (!editForm.isValid())
							return;
						if (me.addFlag == "addRoot")
							me.addRootSubmit(editForm);
						else if (me.addFlag == "addChild") {
							var selectedDatas = me.getSelectionModel()
									.getSelection();
							me.addChildSubmit(editForm, selectedDatas[0]);
						} else if (me.addFlag == "addParent") {
							var selectedDatas = me.getSelectionModel()
									.getSelection();
							me.addParentSubmit(editForm, selectedDatas[0]);
						}
					}
				});

		me.editWinButtons.push({
					xtype : 'button',
					text : '提交',
					itemId : "edit_win_submit_button",
					handler : function() {
						var editForm = editFormWin.getComponent(0);
						if (!editForm.isValid())
							return;

						me.editSubmit(editForm);
					}
				});

		me.editWinButtons.push({
					xtype : 'button',
					itemId : "edit_win_cancel_button",
					text : '取消',
					handler : function() {
						editFormWin.hide();
					}
				});

		var winOptions = Ext.apply({
					width : me.editWinWidth,// 要比表单的宽高大一点
					height : me.editWinHeight,
					layout : 'fit',
					constrain : true,
					closeAction : "hide",
					frame : true,
					modal : true, // 开启遮罩
					items : [me.createEditForm()],
					buttons : me.editWinButtons,
					listeners : {
						resize : function(win, winWidth) {
							var i = 0;
							var formCmp = win.getComponent(0);
							var cmp = null;
							while (true) {
								cmp = formCmp.getComponent(i++);
								if (cmp) {
									cmp
											.setWidth(winWidth
													- (me.editWinWidth - cmp
															.getWidth()));
								} else {
									break;
								}
							}
							win.doLayout();
							me.editWinWidth = winWidth;
						}
					}
				}, me.editWinOptions);
		var editFormWin = Ext.create("Ext.window.Window", winOptions);

		var editItems = [];
		Ext.Array.each(editColumns, function(column, index, countriesItSelf) {
			var item = null;
			var configData = {
				labelAlign : "right",
				labelWidth : 120,
				dataIndex : column.initialConfig.dataIndex,
				fieldLabel : column.initialConfig.header,
				name : column.editname || column.initialConfig.dataIndex
			};
			if (column.editoptions)
				Ext.apply(configData, column.editoptions);
			switch (column.edittype) {
				case "numberfield" :
					item = Ext.create("Ext.form.field.Number", configData);
					break;
				case "datefield" :
					item = Ext.create("Ext.form.field.Date", configData);
					break;
				case "textarea" :
					item = Ext.create("Ext.form.field.TextArea", configData);
					break;
				case "combobox" :
					var comboboxData = column.editoptions
							? column.editoptions.values
							: null;
					if (!comboboxData) {
						if (column.dictionary) {
							comboboxData = dictionary[column.dictionary];
						} else {
							comboboxData = [];
						}
					}
					item = createLocalCombobox(comboboxData, true, configData)
					break;
				case "remotecombo" :
					var config = {
						listConfig : {
							loadingText : "正在加载数据...",
							emptyText : "未找到匹配值",
							maxHeight : 250
						},
						forceSelection : true,
						url : ctx
								+ "/dictionary/searchDictionaryOnQueryId.do?queryId="
								+ column.dictionary,
						editable : true,
						queryMode : 'remote',
						queryParam : "searchParam",
						allQuery : "all",
						triggerAction : 'all',
						displayField : 'label',
						valueField : 'value'
					};
					Ext.apply(config, configData);
					item = Ext.create("Ext.ux.Combobox", config);
					break;
				case "htmleditor" :
					item = Ext.create("Ext.form.field.HtmlEditor", configData);
					break;
				default :
					item = Ext.create("Ext.form.field.Text", configData);
			}
			editItems.push(item);
		});
		editFormWin.getComponent(0).add(editItems);
		return editFormWin;
	},
	initEditHandler : function() {
		var me = this;
		if (me.editHandler)
			return;

		me.editHandler = function(o) {
			var beforeFormShow = me.navGrid.editconfig.beforeFormShow;
			if (beforeFormShow && typeof(beforeFormShow) == "function") {
				var r = beforeFormShow(me);
				if (r != null && !r)
					return;
			}

			var data = me.getSelectionModel().getSelection();
			if (data.length > 0) {
				var editFormWin = me.getEditFormWin(me.getEditColumns());
				var editForm = editFormWin.getComponent(0);
				editForm.form.reset();

				var afterFormShow = me.navGrid.editconfig.afterFormShow;
				if (afterFormShow && typeof(afterFormShow) == "function")
					afterFormShow(editFormWin, me);

				var i = 0;
				while (true) {
					var cmp = editForm.getComponent(i);

					if (!cmp)
						break;

					if (cmp.setEditValue
							&& typeof(cmp.setEditValue) == "function")
						cmp.setEditValue(cmp, data[0].get(cmp.dataIndex));
					else
						cmp.setValue(data[0].get(cmp.dataIndex));
					i++;
				}

				var afterFormSetValue = me.navGrid.editconfig.afterFormSetValue;
				if (afterFormSetValue
						&& typeof(afterFormSetValue) == "function")
					afterFormSetValue(editFormWin, me, data[0]);

				editFormWin.setTitle(me.editButton.text);
				editFormWin.setIconCls("table_edit");
				editFormWin.queryById("edit_win_submit_button").show();
				editFormWin.queryById("add_win_submit_button").hide();
				editFormWin.show();
			} else {
				warn("请选择要修改的数据！");
			}
		}
	},
	initDelHandler : function() {
		var me = this;
		if (me.delHandler)
			return;
		me.delHandler = function(o) {
			var data = me.getSelectionModel().getSelection();
			if (data.length > 0) {
				Ext.Msg.show({
							title : '确认信息',
							msg : '您确定删除此数据吗？',
							buttons : Ext.Msg.YESNO,
							icon : Ext.Msg.QUESTION,
							fn : function(btn, text) {
								if (btn == "yes") {
									var idName = "ID";
									if (me.jsonReader && me.jsonReader.id)
										idName = me.jsonReader.id;
									if (me.multiSelect) {
										var ids = "";
										for (var i = 0; i < data.length; i++) {
											ids += data[i].get(idName);
											if (i < data.length - 1)
												ids += ",";
										}
										me.batchDelSubmit(ids);
									} else {
										var id = null;
										id = data[0].get(idName);
										me.delSubmit(id);
									}

								}
							}
						});
			} else {
				Ext.Msg.show({
							title : '提示',
							msg : '请选择要删除的数据！',
							buttons : Ext.Msg.OK,
							icon : Ext.Msg.WARNING
						});
			}
		}
	},
	initNavGrid : function() {
		var me = this;
		var navGrid = {
			edit : false,
			edittext : "修改",
			add : false,
			addtext : "新增",
			del : false,
			deltext : "删除",
			search : true,
			editconfig : {},
			addconfig : {},
			delconfig : {},
			searchconfig : {
				complex : true
			}
		};
		Ext.applyIf(me.navGrid, navGrid);
	},
	initToolTip : function() {
		var me = this;
		if(me.toolTipParams){
			me.on('itemmouseenter',function(view, record, item, index, e, eOpts){
				//悬浮框创建
				if(view.tip == null){
					view.tip = Ext.create('Ext.tip.ToolTip',{
						autoHide : false,
			            mouseOffset : [ -50, -25 ],
						target : view.el,
						delegate : view.itemSelector,
						renderTo : Ext.getBody()
					});
				};
				var column = view.getGridColumns()[e.getTarget(view.cellSelector).cellIndex];
				if(Ext.Array.contains(me.toolTipParams,column.dataIndex)){
					view.el.clean();
 					view.tip.update(record.data[column.dataIndex]);
				}else{
 					view.tip.destroy();
 					view.tip = null;
 					}
			});
		}
	},
	initRightMenu : function() {
		var me = this;
		if (me.hasRightMenu) {
			var array = [{
						text : me.navGrid.addtext,
						iconCls : "table_add",
						hidden : !me.navGrid.add,
						menu : [{
									handler : me.addRootHandler,
									text : "新增根节点"
								}, {
									handler : me.addChildHandler,
									text : "新增子节点"
								}, {
									handler : me.addParentHandler,
									text : "新增父节点"
								}]
					}, {
						text : me.navGrid.edittext,
						iconCls : "table_edit",
						hidden : !me.navGrid.edit,
						handler : me.editHandler
					}, {
						text : me.navGrid.deltext,
						iconCls : "table_delete",
						hidden : !me.navGrid.del,
						handler : me.delHandler
					}];
			me.rightMenu = new Ext.menu.Menu({
						items : array
					});

			me.addListener('itemcontextmenu', function(his, record, item,
							index, e) {
						e.preventDefault();
						e.stopEvent();// 取消浏览器默认事件
						me.rightMenu.showAt(e.getXY());// 菜单打开的位置
					});
		}
	},
	getRightMenu : function() {
		return this.rightMenu;
	},
	createViewport : function() {
		var me = this;
		if (!me.inViewportShow)
			return;
		me.viewport = Ext.create("Ext.container.Viewport", {
					layout : 'fit',
					items : [me]
				});
	},
	createTbar : function() {
		var me = this;

		if (!me.autoTbar)
			return;

		me.addButton = {
			hidden : !me.navGrid.add,
			text : me.navGrid.addtext,
			iconCls : "table_add",
			menu : [{
						handler : me.addRootHandler,
						text : "新增根节点"
					}, {
						handler : me.addChildHandler,
						text : "新增子节点"
					}, {
						handler : me.addParentHandler,
						text : "新增父节点"
					}]
		};
		me.editButton = {
			hidden : !me.navGrid.edit,
			text : me.navGrid.edittext,
			iconCls : "table_edit",
			handler : me.editHandler
		};
		me.delButton = {
			hidden : !me.navGrid.del,
			text : me.navGrid.deltext,
			iconCls : "table_delete",
			handler : me.delHandler
		};
		var tbarItems = [{
					xtype : "splitbutton",
					text : "操作",
					hidden : !(me.navGrid.add || me.navGrid.edit || me.navGrid.del),
					iconCls : "cog",
					reorderable : false,
					menu : [me.addButton, me.editButton, me.delButton]
				}];

		if (me.navGrid.exportexcel) {
			if (me.navGrid.add || me.navGrid.edit || me.navGrid.del)
				tbarItems.push({
							xtype : 'tbseparator'
						});
			me.exportExcelButton = {
				xtype : "button",
				frame : true,
				text : me.navGrid.exportexceltext,
				iconCls : "cog",
				handler : me.exportExcelHandler
			};
			tbarItems.push(me.exportExcelButton);
		}

		if (me.navGrid.search) {
			tbarItems.push("->");
			tbarItems.push(me.getSimpleSearchCmp());
		}
		var hidden = true;
		Ext.each(tbarItems, function(item) {
					if (item && !item.hidden)
						hidden = false;
				});

		me.tbar = Ext.create("Ext.toolbar.Toolbar", {
					hidden : hidden,
					autoScroll : true,
					items : tbarItems
				});
		me.tbarbak = me.tbar;
	},
	getSimpleSearchCmp : function() {
		var me = this
		if (!me.simpleSearchCmp) {
			me.simpleSearchCmp = Ext.widget("searchfield", {
						width : 180,
						emptyText : me.navGrid.simplesearchtext
								|| "请输入搜索的名称...",
						handler : function(value) {

							me.beforeload({
										search : true,
										filters : Ext.JSON.encode(filters)
									});
							me.refresh();
						},
						emptyHandler : function() {
							me.beforeload({
										search : true
									});
							me.refresh();
						}
					});
		}
		return me.simpleSearchCmp;
	},
	getSimpleSearchCmp : function() {
		var me = this
		if (!me.simpleSearchCmp) {
			me.simpleSearchCmp = Ext.widget("searchfield", {
						xtype : "searchfield",
						fieldLabel : "搜索",
						labelWidth : 35,
						width : 250,
						emptyText : me.navGrid.simplesearchtext
								|| "请输入搜索的名称...",
						handler : function(value) {
							if (value) {
								var filters = {};
								var rules = [];
								filters["groupOp"] = "OR";
								var searchColumns = me.getSearchColumns();
								for (var i = 0; i < searchColumns.length; i++) {
									var field = searchColumns[i].sname;
									var rule = {};
									rule["field"] = field;
									rule["op"] = "cn";
									rule["data"] = value;
									rules.push(rule);
								}
								filters["rules"] = rules;

								Ext.apply(me.getStore().proxy.extraParams, {
											search : true,
											filters : Ext.JSON.encode(filters)
										});

								me.getStore().load({
									callback : function(records, options,
											success) {
										expandNode(me.getStore().getRootNode(),
												value);
									}
								});
								if (me.searchParams)
									me.getStore().proxy.extraParams = me.searchParams;
								else
									me.getStore().proxy.extraParams = {
										search : false
									};
							} else {
								me.getStore().load();
							}
						},
						emptyHandler : function() {
							me.getStore().load();
						}
					});
			function expandNode(node, name) {
				if (node) {
					if (node.childNodes && node.childNodes.length > 0) {
						if (node.childNodes[0].get("text").indexOf(name) > -1)
							return;
						node.childNodes[0].expand(false);
						expandNode(node.childNodes[0], name)
					}
				}
			}
		}
		return me.simpleSearchCmp;
	},
	createStroe : function() {
		var me = this

		if (me.store)
			return;

		var jsonReader = {
			id : 'id',
			text : 'text',
			children : "children"
		};
		if (me.jsonReader) {
			Ext.apply(jsonReader, me.jsonReader);
		}

		var fields = [];
		var columns = me.columns;
		var field;
		var idFlag = false;
		for (var i = 0; i < columns.length; i++) {
			field = {};
			if (columns[i].dataIndex == "id") {
				idFlag = true;
				field["mapping"] = jsonReader.id;
			}
			if (columns[i].dataIndex == "text") {
				field["mapping"] = jsonReader.text;
			} else {
				field["mapping"] = columns[i].dataIndex;
			}

			field["name"] = columns[i].dataIndex;
			fields.push(field);
		}

		if (!idFlag) {
			field["name"] = "id";
			field["mapping"] = jsonReader.id;
			fields.push(field);
		}
		console.debug(fields);
		me.store = Ext.create('Ext.data.TreeStore', {
					defaultRootId : "0", // 默认的根节点id
					proxy : {
						actionMethods : {
							create : 'POST',
							read : 'POST',
							update : 'POST',
							destroy : 'POST'
						},
						type : "ajax", // 获取方式
						url : me.url,
						extraParams : {
							search : false
						}
					},
					nodeParam : "id",
					fields : fields,
					listeners : {
						load : function(store, records, successful, eOpts) {
							if (successful == false) {
								var msg = store.getProxy().getReader().rawData.result;
								if (msg)
									error(msg);
							}
						}
					}
				});
		console.debug(me.store);
	}
});
