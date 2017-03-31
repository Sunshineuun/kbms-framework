Ext.onReady(function() {
			var dictionaryTypeGrid = Ext.widget("uxgridview", {
						startLoad : true,
						url : ctx + "/query/dictionaryTypeService/list.do",
						editUrl : ctx + "/dictionaryType/execute.do",
						dictionaryParams : [["SF"]],
						inViewportShow : false,
						title : "字典类型",
						screen : true,
						iconCls : "table",
						columns : [new Ext.grid.RowNumberer({
											width : 8,
											resizable : true
										}), {
									header : "ID",
									editname : "id",
									hidden : true,
									dataIndex : "ID"
								}, {
									header : "类别代码",
									search : true,
									editname : "typeCode",
									editable : true,
									dataIndex : "TYPE_CODE",
									searchoptions : {
										sopt : ["cn", "eq"]
									},
									editoptions : {
										itemId : "typeCode",
										allowBlank : false
									}
								}, {
									header : "类别名称",
									editname : "typeName",
									editable : true,
									dataIndex : "TYPE_NAME",
									search : true,
									editable : true,
									searchoptions : {
										sopt : ["cn", "eq"]
									},
									editoptions : {
										allowBlank : false
									}
								}, {
									header : "是否有效",
									editname : "enable",
									dataIndex : "ENABLE",
									edittype : "combobox",
									search : true,
									stype : "combobox",
									editable : true,
									dictionary : "SF",
									renderer : function(value) {
										return formatter("SF", value);
									},
									searchoptions : {
										sopt : ["eq"]
									}
								}, {
									header : "备注",
									editname : "remark",
									dataIndex : "REMARK",
									edittype : "textarea",
									editable : true
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
									var field = editFormWin
											.queryById("typeCode");
									field.show();
									field.setWidth(275);
								}
							},
							editconfig : {
								afterFormShow : function(editFormWin) {
									editFormWin.queryById("typeCode").hide();
								}
							},
							delconfig : {
								beforeSubmit : function() {
									var typeCode = dictionaryTypeGrid
											.getSelectionModel().getSelection()[0]
											.get("TYPE_CODE");
									return {
										typeCode : typeCode
									};
								}
							}
						}
					});

			var dictionaryGrid = Ext.widget("uxgridview", {
						startLoad : false,
						editUrl : ctx + "/dictionary/execute.do",
						border : true,
						disabled : true,
						iconCls : "table",
						title : "字典值",
						inViewportShow : false,
						columns : [new Ext.grid.RowNumberer({
											width : 8,
											resizable : true
										}), {
									header : "ID",
									editname : "id",
									edittable : true,
									dataIndex : "ID",
									hidden : true,
									searchoptions : {
										sopt : ["eq", "cn"]
									}
								}, {
									header : "值",
									editname : "value",
									search : true,
									editable : true,
									dataIndex : "VALUE",
									editoptions : {
										allowBlank : false
									},
									searchoptions : {
										sopt : ["cn", "eq"]
									}
								}, {
									header : "名称",
									editname : "name",
									search : true,
									editable : true,
									dataIndex : "NAME",
									searchoptions : {
										sopt : ["cn", "eq"]
									},
									editoptions : {
										allowBlank : false
									}
								}, {
									header : "顺序",
									editname : "viewOrder",
									edittype : "numberfield",
									search : true,
									editable : true,
									dataIndex : "VIEW_ORDER",
									editoptions : {
										allowBlank : false
									},
									searchoptions : {
										sopt : ["eq"]
									}
								}, {
									header : "备注",
									editname : "remark",
									search : true,
									edittype : "textarea",
									editable : true,
									dataIndex : "REMARK",
									searchoptions : {
										sopt : ["cn", "eq"]
									}
								}],
						sortname : "ITEM_ORDER",
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
								beforeSubmit : function() {
									var typeCode = dictionaryTypeGrid
											.getSelectionModel().getSelection()[0]
											.get("TYPE_CODE");
									return {
										typeCode : typeCode
									};
								}
							},
							editconfig : {
								beforeSubmit : function() {
									var typeCode = dictionaryTypeGrid
											.getSelectionModel().getSelection()[0]
											.get("TYPE_CODE");
									return {
										typeCode : typeCode
									};
								}
							}
						}
					});

			dictionaryTypeGrid.on("select", function(rowmodel, model) {
						dictionaryGrid.setUrl(ctx
								+ "/query/dictionaryService/list.do?typeCode="
								+ model.get("TYPE_CODE")).refresh();
						dictionaryGrid.enable();
					});

			dictionaryTypeGrid.store.on("refresh", function() {
						dictionaryGrid.clear();
						dictionaryGrid.disable();
					});
			Ext.create('Ext.container.Viewport', {
						layout : 'border',
						items : [{
									region : 'north',
									height : "50%",
									layout : "fit",
									items : [dictionaryTypeGrid]
								}, {
									region : 'south',
									height : "50%",
									layout : "fit",
									items : [dictionaryGrid]
								}]
					});

		});