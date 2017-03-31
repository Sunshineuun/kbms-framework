Ext.define('Ext.ux.GridView', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.uxgridview',
	initComponent : function() {
		var me = this;

		me.initGrid();

		this.callParent(arguments);

		me.createTools();
		me.createViewport();
	},
	beforeRequest : null,
	toolTipParams  : null,//配置悬停单元格
	url : null,
	reasonUrl : null,
	editReason : false,
	delReason : false,
	addReduction : false,
	dictUrl : ctx + "/dictionary/loadDictionary.do",
	editUrl : null,
	storeFields : null,
	startLoad : true,
	forceFit : true,
	viewport : null,
	rootVisible : false,
	loadMask : true,
	useArrows : true,
	scroll : true,
	autoScroll : true,
	rowLines : true,
	inViewportShow : false,
	navGrid : null,
	pageSize : 30,
	searchFieldCombobox : null,
	autoTbar : true,
	hasRightMenu : true,
	addButton : null,
	editButton : null,
	delButton : null,
	logicDelButton : null,
	clinicalSubmitButton : null,
	addHandler : null,
	editHandler : null,
	delHandler : null,
	logicDelHandler : null,
	clinicalSubmitHandler : null,
	sortname : null,
	sortorder : null,
	columnLines : true,
	dictionaryParams : null,
	enableColumnHide : false,
	hasPagingToolbar : true,
	editWinWidth : 450,
	viewConfig : {
		stripeRows : true,
		enableTextSelection : true
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
		me.initLogicDelHandler();
		me.initClinicalSubmitHandler();
		me.initExportExcelHandler();

		me.initComplexSearchHandler();

		me.initRightMenu();

		me.doBeforeRequest();
		me.loadDictionary();

		me.createStroe();
		me.createTbar();
		me.createBbar();
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
		// 防止丢失查询条件
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
	getSearchOperCombobox : function(comboboxData, configData) {
		var me = this;

		var config = {
			width : 80
		};
		Ext.apply(config, configData);

		me.searchOperCombobox = createLocalCombobox(comboboxData, true, config);
		return me.searchOperCombobox;
	},
	getSearchFieldCombobox : function() {
		var me = this;

		if (me.searchFieldCombobox)
			return me.searchFieldCombobox;

		var searchColumns = me.getSearchColumns();

		var comboboxData = [];
		for (var i = 0; i < searchColumns.length; i++) {
			comboboxData.push({
						value : searchColumns[i].sname
								|| searchColumns[i].dataIndex,
						label : searchColumns[i].header
					})
		}
		me.searchFieldCombobox = Ext.create("Ext.form.field.ComboBox", {
					fieldLabel : '请选择查询列',
					forceSelection : true,
					editable : false,
					queryMode : 'local',
					labelAlign : "right",
					store : Ext.create('Ext.data.Store', {
								fields : ['value', 'label'],
								data : comboboxData
							}),
					displayField : 'label',
					valueField : 'value',
					listeners : {
						change : function(field, newValue, oldValue, obj) {
							var searchColumn = null;
							for (var i = 0; i < searchColumns.length; i++) {
								if (field.rawValue == searchColumns[i].header)
									searchColumn = searchColumns[i];
							}

							if (searchColumn == null)
								return;
							var sopt = searchColumn.searchoptions
									? (searchColumn.searchoptions.sopt
											? searchColumn.searchoptions.sopt
											: ["eq", "cn"])
									: ["eq", "cn"];

							// 修改条件输入框
							me.tbarbak.remove(me.searchStringCmp);
							var searchStringCmp = me.getSearchStringCmp(
									searchColumn.stype, searchColumn);
							me.tbarbak.insert(me.tbarbak.items.length - 3,
									searchStringCmp);

							me.tbarbak.remove(me.searchOperCombobox);
							var searchOperCombobox = me
									.getSearchOperCombobox(me.getSoptData(sopt));
							me.tbarbak.insert(me.tbarbak.items.length - 4,
									searchOperCombobox);

						}
					}
				});
		return me.searchFieldCombobox;
	},
	getSearchStringCmp : function(stype, searchColumn) {
		var me = this;
		Ext.apply(searchColumn.searchoptions, {
					listeners : {
						specialkey : function(field, e) {
							if (e.getKey() == e.ENTER) {
								me.searchHanlder();
							}
						}
					}
				})
		switch (stype) {
			case "textfield" :
				me.searchStringCmp = Ext.create("Ext.form.field.Text",
						searchColumn.searchoptions);
				break;
			case "datefield" :
				me.searchStringCmp = Ext.create("Ext.form.field.Date",
						searchColumn.searchoptions);
				break;
			case "combobox" :
				var comboboxData = searchColumn.searchoptions.values;
				if (!comboboxData) {
					if (searchColumn.dictionary) {
						comboboxData = dictionary[searchColumn.dictionary];
						var array = [];
						// 加入逻辑判断 如果有字典排除项配置 那么排除掉指定值
						if (searchColumn.dictionaryDebar) {
							var dictionaryDebar = array2Str(searchColumn.dictionaryDebar);
							Ext.each(comboboxData, function(item) {
								if (dictionaryDebar.indexOf(item.toString()) < 0) {
									Ext.Array.include(array, item);
								}
							});
							comboboxData = array;
						}
					} else {
						comboboxData = [];
					}
				}
				me.searchStringCmp = createLocalCombobox(comboboxData, true,
						searchColumn.searchoptions);
				break;
			case "remotecombo" :
				var searchoptions = searchColumn.searchoptions;
				var config = {
					listConfig : {
						loadingText : "正在加载数据...",
						emptyText : "未找到匹配值",
						maxHeight : 250
					},
					forceSelection : true,
					url : ctx
							+ "/dictionary/searchDictionaryOnQueryId.do?queryId="
							+ searchoptions.queryId,
					editable : true,
					queryMode : 'remote',
					queryParam : "searchParam",
					allQuery : "all",
					triggerAction : 'all',
					displayField : 'label',
					valueField : 'value'
				};
				Ext.apply(config, searchoptions);
				me.searchStringCmp = Ext.create("Ext.ux.Combobox", config);
				break;
			default :
				me.searchStringCmp = Ext.create("Ext.form.field.Text",
						searchColumn.searchoptions);
		}
		return me.searchStringCmp;
	},
	searchHanlder : function() {
		var me = this;
		if (me.searchOperCombobox && me.searchStringCmp) {
			var searchString = null;
			if (me.searchStringCmp.getXType() == "datefield")
				searchString = me.searchStringCmp.getRawValue();
			else
				searchString = me.searchStringCmp.getValue();
			var postParams = {
				search : true,
				searchField : me.searchFieldCombobox.getValue(),
				searchOper : me.searchOperCombobox.getValue(),
				searchString : searchString
			};

			me.beforeload(postParams);

			me.refresh();
		}
	},
	refresh : function(pageNo) {
		if (this.url == null)
			return;

		if (pageNo)
			this.store.loadPage(pageNo);
		else
			this.store.loadPage(1);
	},
	setSearchValue : function(value) {
		var me = this;
		if (me.simpleSearchCmp) {
			me.simpleSearchCmp.setValue(value);
			me.simpleSearchCmp.onTrigger2Click();
		}
	},
	getSoptData : function(sopt) {
		var data = [];
		Ext.each(sopt, function(value, index) {
					switch (value) {
						case "cn" :
							data.push(["cn", "包含"]);
							break;
						case "eq" :
							data.push(["eq", "等于"]);
							break;
						case "ne" :
							data.push(["ne", "不等于"]);
							break;
						case "lt" :
							data.push(["lt", "小于等于"]);
							break;
						case "ge" :
							data.push(["ge", "大于等于"]);
							break;
						case "gt" :
							data.push(["gt", "大于"]);
							break;
						case "nc" :
							data.push(["nc", "不包含"]);
							break;
						case "en" :
							data.push(["en","结尾是"]);
							break;
					}
				});
		return data;
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
		if (!me.navGrid.add && !me.navGrid.edit)
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

		if (me.addHandler)
			return;

		me.addHandler = function(o) {
			var beforeFormShow = me.navGrid.addconfig.beforeFormShow;
			if (beforeFormShow && typeof(beforeFormShow) == "function") {
				var r = beforeFormShow(me);
				if (r != null && !r)
					return;
			}

			var editFormWin = me
					.getEditFormWin(me.getEditColumns(), null, true);
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
	},
	getEditFormWin : function(editColumns, selectedRowData, isAdd) {
		var me = this;

		if (!me.editFormWin)
			me.editFormWin = me.createEditFormWin(editColumns, isAdd);

		return me.editFormWin;
	},
	getEditForm : function() {
		return this.editFormWin.getComponent(0);
	},
	addSubmit : function(formCmp) {
		var me = this;
		var postParams = {
			oper : 'add',
			addReduction : me.addReduction
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
						//判断违规信息是否是中文
						var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
						if(me.addReduction && !reg.test(action.result.result)){
							Ext.Msg.show({
								title : '确认信息',
								msg : '您确认还原此数据吗？',
								buttons : Ext.Msg.YESNO,
								icon : Ext.Msg.QUESTION,
								fn : function(btn,text){
									if(btn == "yes"){
										var postParams = {
												oper : 'reduction',
												ids : action.result.result
											};
										Ext.Ajax.request({
											url : me.editUrl,
											params : postParams,
											success : function(response, opts) {
												me.editFormWin.hide();
												me.refresh();
											},
											failure : function(response, opts) {
												error('还原失败！');
											}
										});
									}
									
								} 
							});
						}else{
							var errorMsg = '新增失败！';
							if (action && action.result && action.result.result)
								errorMsg = action.result.result;
							error(errorMsg);
						}
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
		var oper="edit";
		if(me.editReason){
			me.changeLog(id,oper,formCmp);
		}else{
			var postParams = {
				oper : oper
			};
			
			if(data[0].get("IS_SUBMIT") != null){
				postParams["isSubmit"] = data[0].get("IS_SUBMIT");
			}
			
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
		}
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
								}, function(msg) {
									error(msg);
								});
					},
					failure : function(response, opts) {
						error("删除失败！");
					}
				});
	},
	logicDelSubmit : function(ids) {
		var me = this;
		var idKeyName = "id";
		if (me.jsonReader && me.jsonReader.idName)
			idKeyName = me.jsonReader.idName;

		var postParams = {
			oper : 'logicDel'
		};
		postParams[idKeyName] = ids;
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
					},
					failure : function(response, opts) {
						error('删除失败！');
					}
				});
	},
	clinicalSubmit : function(id) {
		var me = this;

		var idKeyName = "id";
		if (me.jsonReader && me.jsonReader.idName)
			idKeyName = me.jsonReader.idName;

		var postParams = {
			oper : 'submit'
		};
		postParams[idKeyName] = id;

		Ext.Ajax.request({
					url : me.editUrl,
					params : postParams,
					success : function(response, opts) {
						respWarn(response, function() {
									me.refresh();
								}, function(msg) {
									error(msg);
								});
					},
					failure : function(response, opts) {
						error("提交失败！");
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
					layout : 'anchor',
					bodyStyle : 'paddingTop:5px;paddingBottom:5px;',
					frame : false
				});
	},
	editWinButtons : [],
	createEditFormWin : function(editColumns, isAdd) {
		var me = this;
		me.editWinButtons.push({
					xtype : 'button',
					text : '提交',
					itemId : "add_win_submit_button",
					handler : function() {
						var editForm = editFormWin.getComponent(0);
						if (!editForm.isValid())
							return;
						me.addSubmit(editForm);
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
					layout : 'fit',
					constrain : true,
					closeAction : "hide",
					frame : true,
					modal : true, // 开启遮罩
					items : [me.createEditForm()],
					buttons : me.editWinButtons
				}, me.editWinOptions);
		var editFormWin = Ext.create("Ext.window.Window", winOptions);

		var editItems = [];
		Ext.Array.each(editColumns, function(column, index, countriesItSelf) {
			var item = null;
			var required = null;
			if (column.editoptions)
				if (column.editoptions.allowBlank == false)
					required = "<span style='color:red;font-weight:bold' data-qtip='必填'>*</span>";

			var configData = {
				labelAlign : "right",
				labelWidth : 120,
				afterLabelTextTpl : required,
				dataIndex : column.initialConfig.dataIndex,
				msgTarget : "side",
				anchor : '98%',
				autoFitErrors : false,
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
							var array = [];
							// 加入逻辑判断 如果有字典排除项配置 那么排除掉指定值
							if (column.dictionaryDebar) {
								var dictionaryDebar = array2Str(column.dictionaryDebar);
								Ext.each(comboboxData, function(item) {
											if (dictionaryDebar.indexOf(item
													.toString()) < 0) {
												Ext.Array.include(array, item);
											}
										});
								comboboxData = array;
							}
						} else {
							comboboxData = [];
						}
					}
					//配置是否初始化combobox中的值
					var isInitValue = column.isInitValue != undefined ? 
							column.isInitValue : true;
					item = createLocalCombobox(comboboxData, isInitValue, configData);
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
					else if (cmp.isXType('uxcombobox'))
						cmp.setComboValue(data[0].get(cmp.dataIndex));
					else {
						cmp.setValue(data[0].get(cmp.dataIndex));
					}
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
			var beforeFormShow = me.navGrid.delconfig.beforeFormShow;
			if (beforeFormShow && typeof(beforeFormShow) == "function") {
				var r = beforeFormShow(me);
				if (r != null && !r)
					return;
			}

			var data = me.getSelectionModel().getSelection();
			if (data.length > 0) {
				var idName = "ID";
				if (me.jsonReader && me.jsonReader.id)
					idName = me.jsonReader.id;
				var ids = "";
				if (me.multiSelect) {
					for (var i = 0; i < data.length; i++) {
						ids += data[i].get(idName);
						if (i < data.length - 1)
							ids += ",";
					}
				}else {
					ids = data[0].get(idName);
				}
				if(me.delReason){
					var oper ="del";
					me.changeLog(ids,oper,null);
				}else {
					Ext.Msg.show({
						title : '确认信息',
						msg : '您确认删除此数据吗？',
						buttons : Ext.Msg.YESNO,
						icon : Ext.Msg.QUESTION,
						fn : function(btn,text){
							if(btn == "yes"){
								if(me.multSelect){
									me.batchDelSubmit(ids);
								}else{
									me.delSubmit(ids);
								}
							}
							
						} 
					});
				}
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
	initLogicDelHandler : function() {
		var me = this;
		if (me.logicDelHandler)
			return;
		me.logicDelHandler = function(o) {
			var data = me.getSelectionModel().getSelection();
			var oper ="logicDel";
			if (data.length > 0) {
				var idName = "ID";
				if (me.jsonReader && me.jsonReader.id)
					idName = me.jsonReader.id;
				var ids = "";
				for (var i = 0; i < data.length; i++) {
					ids += data[i].get(idName);
					if (i < data.length - 1)
						ids += ",";
				}		
				me.changeLog(ids,oper,null);
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
	initClinicalSubmitHandler : function() {
		var me = this;
		if (me.clinicalSubmitHandler)
			return;

		me.clinicalSubmitHandler = function(o) {
			var datas = me.getSelectionModel().getSelection();
			if (datas.length > 0) {
				Ext.Msg.show({
							title : '确认信息',
							msg : '您确定提交此数据吗？',
							buttons : Ext.Msg.YESNO,
							icon : Ext.Msg.QUESTION,
							fn : function(btn, text) {
								if (btn == "yes") {
									var idName = "ID";
									if (me.jsonReader && me.jsonReader.id)
										idName = me.jsonReader.id;
									var ids = "";
									for (var i = 0; i < datas.length; i++) {
										ids += datas[i].get(idName);
										if (i < datas.length - 1)
											ids += ",";
									}
									me.clinicalSubmit(ids);
								}
							}
						});
			} else {
				Ext.Msg.show({
							title : '提示',
							msg : '请选择要提交的数据！',
							buttons : Ext.Msg.OK,
							icon : Ext.Msg.WARNING
						});
			}
		}
	},
	initExportExcelHandler : function() {
		var me = this;
		if (me.exportExcelHandler)
			return;

		me.exportExcelHandler = function() {
			var params = me.getStore().getProxy().extraParams;
			if (params == null)
				params = new Object();

			var exportDefines = [];
			me.dictionaryParams;
			var dictType = 0;
			var columns = me.columns;
			for (var i = 0; i < columns.length; i++) {
				if (!columns[i].hidden && columns[i].initialConfig.header) {
					if (columns[i].dictionary) {
						if (me.dictionaryParams)
							if (me.dictionaryParams[0] 
									&& me.dictionaryParams[0].contains(columns[i].dictionary)) {
								dictType = 0;
							} else if (me.dictionaryParams[1]
									&& me.dictionaryParams[1].contains(columns[i].dictionary)) {
								dictType = 1;
							} else if (me.dictionaryParams[2] 
									&& me.dictionaryParams[2].contains(columns[i].dictionary)) {
								dictType = 2;
							} else {
								dictType = 0;
							}
						exportDefines.push({
									"key" : columns[i].dataIndex,
									"name" : columns[i].dataIndex,
									"title" : columns[i].initialConfig.header,
									"dictionaryCode" : columns[i].dictionary,
									"dictionaryType" : dictType
								});
					} else {
						exportDefines.push({
									"key" : columns[i].dataIndex,
									"name" : columns[i].dataIndex,
									"title" : columns[i].initialConfig.header
								});
					}
				}
			}
			params["exportDefines"] = Ext.JSON.encode(exportDefines);
			var url = me.url;
			url = url.replace("/list.do", "/exportSXSSF.do");
			download_file(url, params);
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
			logicDel : false,
			logicDeltext : "逻辑删除",
			search : true,
			searchtext : "查询",
			clinicalsubmit : false,
			clinicalsubmittext : "提交",
			exportexcel : false,
			exportexceltext : "导出Excel",
			simplesearch : false,
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
						handler : me.addHandler
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
					}, {
						text : me.navGrid.logicDeltext,
						iconCls : "table_delete",
						hidden : !me.navGrid.logicDel,
						handler : me.logicDelHandler
					}, {
						text : me.navGrid.clinicalsubmittext,
						iconCls : "cog",
						hidden : !me.navGrid.clinicalsubmit,
						handler : me.clinicalSubmitHandler
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
	createTools : function() {
		var me = this;
		if (me.screen) {
			var parentCmp = me.ownerCt;
			var win = null;
			var maximizeTool = Ext.create("Ext.panel.Tool", {
						type : "maximize",
						tooltip : '全屏',
						handler : function(event, toolEl, panel) {
							parentCmp = me.ownerCt;
							restoreTool.show();
							maximizeTool.hide();
							win = Ext.create("Ext.window.Window", {
										maximized : true,
										border : false,
										header : false,
										modal : true,
										layout : "fit",
										items : me
									}).show();
						}
					});
			var restoreTool = Ext.create("Ext.panel.Tool", {
						type : "restore",
						hidden : true,
						tooltip : '还原',
						handler : function(event, toolEl, panel) {
							restoreTool.hide();
							maximizeTool.show();
							parentCmp.add(me);
							win.destroy();
						}
					});
			me.tools = [maximizeTool, restoreTool];
		}

	},
	createComplexSearchForm : function() {
		var me = this;

		var hbox = Ext.create('Ext.Panel', {
					border : false,
					layout : {
						type : 'hbox',
						align : 'middle'
					},
					items : [
							createLocalCombobox([["AND", "所有"], ["OR", "任一"]],
									true, {
										width : 70,
										style : {
											marginLeft : "2px;"
										}
									}), {
								text : "+",
								style : {
									marginLeft : "5px;"
								},
								xtype : "button",
								border : false,
								handler : function(o) {
									addHboxPanel(me.complexSearchFormWin
											.getComponent(0).getComponent(0));
								}
							}]
				});
		var vbox = Ext.create('Ext.Panel', {
					border : false,
					layout : {
						type : 'vbox',
						align : 'middle'
					},
					items : [hbox]
				});

		me.complexSearchFormWin = Ext.create("Ext.window.Window", {
			title : "高级查询",
			width : 520,// 要比表单的宽高大一点
			layout : 'fit',
			resizable : false,
			closeAction : "hide",
			modal : false,
			items : [{
						xtype : 'form',
						bodyStyle : 'paddingTop:5px;paddingBottom:5px;',
						border : false,
						frame : false,
						items : vbox
					}],
			buttons : [{
						xtype : 'button',
						text : '查询',
						handler : function() {
							var filters = {};
							var rules = [];
							var hbox = vbox.getComponent(0);
							filters["groupOp"] = hbox.getComponent(0)
									.getValue();
							filters["rules"] = rules;
							var i = 1;
							while (true) {
								var hboxCmp = vbox.getComponent(i);
								if (!hboxCmp)
									break;

								var rule = {};
								rule["field"] = hboxCmp.getComponent(0)
										.getValue();
								rule["op"] = hboxCmp.getComponent(1).getValue();
								rule["data"] = hboxCmp.getComponent(2)
										.getValue();
								rules.push(rule);
								i++;
							}

							me.beforeload({
										search : true,
										filters : Ext.JSON.encode(filters)
									});
							me.refresh();
						}
					}, {
						xtype : 'button',
						text : '重置',
						handler : function(o) {
							vbox.removeAll(false);
							vbox.add(hbox);
						}
					}]
		});
		function addHboxPanel(vbox) {
			var hbox = null;
			var searchColumns = me.getSearchColumns();

			if (searchColumns.length <= 0)
				return;

			var comboboxData = [];
			for (var i = 0; i < searchColumns.length; i++) {
				comboboxData.push({
							value : searchColumns[i].sname
									|| searchColumns[i].initialConfig.dataIndex,
							label : searchColumns[i].initialConfig.header
						})
			}

			var fieldNameComboBox = Ext.create("Ext.form.field.ComboBox", {
				value : comboboxData[0].value,
				forceSelection : true,
				editable : false,
				queryMode : 'local',
				style : {
					marginRight : "5px;"
				},
				store : Ext.create('Ext.data.Store', {
							fields : ['value', 'label'],
							data : comboboxData
						}),
				displayField : 'label',
				valueField : 'value',
				listeners : {
					change : function(field, newValue, oldValue, obj) {
						hbox.remove(hbox.getComponent(1));
						hbox.remove(hbox.getComponent(1));

						var searchColumn = null;
						for (var i = 0; i < searchColumns.length; i++) {
							if (field.rawValue == searchColumns[i].initialConfig.header)
								searchColumn = searchColumns[i];
						}

						if (searchColumn == null)
							return;
						var sopt = searchColumn.searchoptions
								? (searchColumn.searchoptions.sopt
										? searchColumn.searchoptions.sopt
										: ["eq", "cn"])
								: ["eq", "cn"];

						hbox.insert(1, me.getSearchOperCombobox(me
												.getSoptData(sopt), {
											style : {
												marginRight : "5px;"
											}
										}));

						// 修改条件输入框
						hbox.insert(2, createStringCmp(searchColumn.stype,
										searchColumn));
					}
				}
			});

			var searchOperCombobox = me.getSearchOperCombobox(me
							.getSoptData(searchColumns[0].searchoptions.sopt),
					{
						style : {
							marginRight : "5px;"
						}
					});

			var stringCmp = createStringCmp(searchColumns[0].stype,
					searchColumns[0]);

			hbox = Ext.create('Ext.Panel', {
						border : false,
						style : {
							marginTop : "5px;",
							marginLeft : "5px;"
						},
						layout : {
							type : 'hbox',
							align : 'middle'
						},
						items : [fieldNameComboBox, searchOperCombobox,
								stringCmp, {
									text : "-",
									xtype : "button",
									style : {
										marginLeft : "5px;"
									},
									border : false,
									handler : function(o) {
										vbox.remove(hbox);
									}
								}]
					})
			vbox.add(hbox);
		}

		function createStringCmp(stype, searchColumn) {
			var cmp = null;
			switch (stype) {
				case "textfield" :
					cmp = Ext.create("Ext.form.field.Text",
							searchColumn.searchoptions);
					break;
				case "datefield" :
					cmp = Ext.create("Ext.form.field.Date",
							searchColumn.searchoptions);
					break;
				case "combobox" :
					var comboboxData = searchColumn.searchoptions.values;
					if (!comboboxData) {
						if (searchColumn.dictionary) {
							comboboxData = dictionary[searchColumn.dictionary];
						} else {
							comboboxData = [];
						}
					}
					cmp = createLocalCombobox(comboboxData, true,
							searchColumn.searchoptions);
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
								+ searchColumn.dictionary,
						editable : true,
						queryMode : 'remote',
						queryParam : "searchParam",
						allQuery : "all",
						triggerAction : 'all',
						displayField : 'label',
						valueField : 'value'
					};
					cmp = Ext.create("Ext.ux.Combobox",
							searchColumn.searchoptions);
					break;
				default :
					cmp = Ext.create("Ext.form.field.Text",
							searchColumn.searchoptions);
			}
			return cmp;
		}

		return me.complexSearchWin;
	},
	initComplexSearchHandler : function() {
		var me = this;

		if (!me.complexSearchFormWin)
			me.createComplexSearchForm();

		me.complexSearchHandler = function(o) {
			me.complexSearchFormWin.show();
		}
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
	createBbar : function() {
		var me = this;
		if (me.hasPagingToolbar) {
			me.bbar = new Ext.PagingToolbar({
						displayInfo : true,
						emptyMsg : "没有数据",
						store : me.store
					});

			me.pagingToolbar = me.bbar;
		}
	},
	getPagingToolbar : function() {
		return this.pagingToolbar;
	},
	clear : function() {
		var me = this;

		me.store.removeAll();
		me.pagingToolbar.onLoad();
		me.pagingToolbar.updateInfo();
	},
	createTbar : function() {
		var me = this;

		if (!me.autoTbar)
			return;

		me.addButton = {
			xtype : "button",
			hidden : !me.navGrid.add,
			frame : true,
			text : me.navGrid.addtext,
			iconCls : "table_add",
			handler : me.addHandler
		};
		me.editButton = {
			xtype : "button",
			frame : true,
			hidden : !me.navGrid.edit,
			text : me.navGrid.edittext,
			iconCls : "table_edit",
			handler : me.editHandler
		};
		me.delButton = {
			xtype : "button",
			frame : true,
			hidden : !me.navGrid.del,
			text : me.navGrid.deltext,
			iconCls : "table_delete",
			handler : me.delHandler
		};
		me.logicDelButton = {
			xtype : "button",
			frame : true,
			hidden : !me.navGrid.logicDel,
			text : me.navGrid.logicDeltext,
			iconCls : "table_delete",
			handler : me.logicDelHandler
		};
		me.clinicalSubmitButton = {
			xtype : "button",
			frame : true,
			hidden : !me.navGrid.clinicalsubmit,
			text : me.navGrid.clinicalsubmittext,
			iconCls : "cog",
			handler : me.clinicalSubmitHandler
		};
		var tbarItems = [me.addButton, me.editButton, me.delButton,me.logicDelButton,
				me.clinicalSubmitButton];

		if (me.navGrid.exportexcel) {
			if (me.navGrid.add || me.navGrid.edit || me.navGrid.del)
				tbarItems.push({
							xtype : 'tbseparator'
						});
			me.exportExcelButton = {
				xtype : "button",
				frame : true,
				text : me.navGrid.exportexceltext,
				iconCls : "page_excel",
				handler : me.exportExcelHandler
			};
			tbarItems.push(me.exportExcelButton);
		}

		if (me.navGrid.simplesearch) {
			tbarItems.push("->");
			tbarItems.push(me.getSimpleSearchCmp());
		} else if (me.navGrid.search) {
			if (me.navGrid.add || me.navGrid.edit || me.navGrid.del
					|| me.navGrid.exportexcel)
				tbarItems.push({
							xtype : 'tbseparator',
							hidden : !me.navGrid.searchconfig.complex
						});
			me.complexSearchButton = {
				xtype : "button",
				frame : true,
				text : "高级查询",
				hidden : !me.navGrid.searchconfig.complex,
				iconCls : "search",
				handler : me.complexSearchHandler
			};
			tbarItems.push(me.complexSearchButton);

			tbarItems.push("->");
			tbarItems.push(me.getSearchFieldCombobox());
			tbarItems.push({
						text : "查询",
						frame : true,
						iconCls : "search",
						handler : function() {
							me.searchHanlder();
						}
					});
			tbarItems.push({
						xtype : 'tbseparator'
					});
			tbarItems.push({
						text : "查询全部",
						frame : true,
						iconCls : "search",
						handler : function() {
							// 先删除条件选择框
							me.tbarbak.remove(me.searchOperCombobox);
							me.tbarbak.remove(me.searchStringCmp);
							me.searchFieldCombobox.setValue("");

							me.beforeload({
										search : true
									});
							me.refresh();
						}
					});
		}
		var hidden = true;
		// 功能栏的显示与否与navGrid配置的hidden属性直接决定，若未配置那么遍历新增、删除、修改、查询按钮，只要
		//有一个按钮显示那么功能栏则显示
		if (typeof(me.navGrid.hidden) != "undefined") {
			hidden = me.navGrid.hidden;
		} else {
			Ext.each(tbarItems, function(item) {
				if (item && !item.hidden)
					hidden = false;					
				
			});
		}
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
						width : 280,
						fieldLabel : "搜索",
						labelAlign : "right",
						emptyText : me.navGrid.simplesearchtext
								|| "请输入搜索的名称...",
						handler : function(value) {
							var filters = {};
							var rules = [];
							filters["groupOp"] = "OR";
							var searchColumns = me.getSearchColumns();
							for (var i = 0; i < searchColumns.length; i++) {
								var field = searchColumns[i].sname
										|| searchColumns[i].dataIndex;
								var rule = {};
								rule["field"] = field;
								rule["op"] = "cn";
								rule["data"] = value;
								rules.push(rule);
							}
							filters["rules"] = rules;
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
	createStroe : function() {
		var me = this

		var fields = [];
		var columns = me.columns;
		for (var i = 0; i < columns.length; i++) {
			fields.push(columns[i].dataIndex);
		}

		var jsonReader = {
			type : 'json',
			root : 'result.result',
			totalProperty : "result.totalCount",
			successProperty : 'success'
		};
		if (me.jsonReader) {
			Ext.apply(jsonReader, me.jsonReader);
		}

		me.store = Ext.create('Ext.data.Store', {
					fields : fields,
					pageSize : me.pageSize, // 每页显示条数
					remoteSort : true,
					proxy : {
						type : 'ajax',
						url : me.url,
						actionMethods : {
							create : 'POST',
							read : 'POST',
							update : 'POST',
							destroy : 'POST'
						},
						reader : jsonReader
					},
					autoLoad : false,
					sorters : me.sortname && me.sortorder ? [{
								"property" : me.sortname,
								"direction" : me.sortorder
							}] : null,
					listeners : {
						load : function(store, records, successful, eOpts) {
							if (successful == false) {
								var msg = store.getProxy().getReader().rawData.result;
								if (msg)
									error(msg);
								else
									error("服务器出错！");
							}
						}
					}
				});

		me.beforeload({
					search : true
				});
		if (me.startLoad)
			me.store.load();
	},
	createStroe : function() {
		var me = this

		var fields = [];
		var columns = me.columns;
		for (var i = 0; i < columns.length; i++) {
			fields.push(columns[i].dataIndex);
		}

		var jsonReader = {
			type : 'json',
			root : 'result.result',
			totalProperty : "result.totalCount",
			successProperty : 'success'
		};
		if (me.jsonReader) {
			Ext.apply(jsonReader, me.jsonReader);
		}

		me.store = Ext.create('Ext.data.Store', {
					fields : fields,
					pageSize : me.pageSize, // 每页显示条数
					remoteSort : true,
					proxy : {
						type : 'ajax',
						url : me.url,
						timeout: 600000,
						actionMethods : {
							create : 'POST',
							read : 'POST',
							update : 'POST',
							destroy : 'POST'
						},
						reader : jsonReader
					},
					autoLoad : false,
					sorters : me.sortname && me.sortorder ? [{
								"property" : me.sortname,
								"direction" : me.sortorder
							}] : null,
					listeners : {
						load : function(store, records, successful, eOpts) {
							if (successful == false) {
								var msg = store.getProxy().getReader().rawData.result;
								if (msg)
									error(msg);
								else
									error("服务器出错！");
							}
						}
					}
				});

		me.beforeload({
					search : true
				});
		if (me.startLoad)
			me.store.load();
	},
	changeLog : function(ids,oper,formCmp) {
		var me = this;
		Ext.create("Ext.window.Window", {
			autoScroll : true,
			closable : false,
			width : 340,										
			layout : 'anchor',
			frame : true,
			title : "添加说明",
			modal: true,
			items : [{
				xtype : 'form',
				border : false,
				items : [{
					xtype: 'uxcombobox',
					fieldLabel : "说明类型",
					margin: '5 0 5 20',
					labelWidth : 60,
					labelAlign : "left",
					id : "changeStatus",
					values : [["A", "全部"], ["B", "修正知识"],
							  ["C", "补充部分字段"],["D","无效数据"],
							  ["E","误操作"],["F","其它"],["G","修正知识（已验证）"]],
					anchor: '90%',
					value : 'A'
				},{
					xtype: 'textarea',
					labelWidth : 60,
					margin: '5 0 5 20',
					anchor: '90%',
					id : 'changeDsc',
					fieldLabel : "添加说明"	
				}]
			}],
			buttons : [{
				text : '添加',
				handler : function() {
					var windowCmp = this.up("window");
					var editForm = windowCmp.getComponent(0);
					var changeStatus = Ext.getCmp("changeStatus").getValue();
					var changeDsc = Ext.getCmp("changeDsc").getValue();
					editForm.getForm().submit({
						url : me.reasonUrl,
						params : {
							ids : ids,
							changeStatus : changeStatus,
							changeDsc : changeDsc,
							oper : oper
						},
						success : function(response, opts) {
							windowCmp.destroy();
							if(oper=="del"){
								if (me.multiSelect) {
									me.batchDelSubmit(ids);
								} else {
									me.delSubmit(ids);
								}
							}
							if(oper=="edit"){
								me.submitData(formCmp);
							}
							if(oper=="logicDel"){
								me.logicDelSubmit(ids);
							}
						},
						failure : function(response, opts) {
							error('修改失败！');
						}
					});
				}
			}, {
				xtype : 'button',
				text : '取消',
				handler : function() {
					this.up("window").destroy();
				}
			}]
		}).show();
	},
	submitData : function(formCmp) {
		me=this;
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
				oper : 'edit'
			};
		
		if(data[0].get("IS_SUBMIT") != null){
			postParams["isSubmit"] = data[0].get("IS_SUBMIT");
		}
		
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
	}
});
