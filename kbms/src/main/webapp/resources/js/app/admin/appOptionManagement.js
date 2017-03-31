Ext.onReady(function() {
			Ext.QuickTips.init();

			Ext.widget("uxgridview", {
						startLoad : true,
						url : ctx + "/appOption/kbmsAppOptionService/list.do",
						editUrl : ctx + "/appOption/execute.do",
						inViewportShow : true,
						pageSize : 30,
						dictionaryParams : [["SF"]],
						columns : [new Ext.grid.RowNumberer(), {
									header : "ID",
									editname : "id",
									dataIndex : "ID",
									hidden : true,
									searchoptions : {
										sopt : ["eq", "cn"]
									}
								}, {
									header : "类别代码",
									editname : "lbdm",
									dataIndex : "LBDM",
									editable : true,
									search : true,
									searchoptions : {
										sopt : ["eq", "cn"]
									},
									editoptions : {
										allowBlank : false
									}
								}, {
									header : "类别名称",
									editname : "lbmc",
									editable : true,
									search : true,
									dataIndex : "LBMC",
									searchoptions : {
										sopt : ["eq", "cn"]
									},
									editoptions : {
										allowBlank : false
									}
								}, {
									header : "代码",
									editname : "dm",
									editable : true,
									search : true,
									dataIndex : "DM",
									searchoptions : {
										sopt : ["eq", "cn"]
									},
									editoptions : {
										allowBlank : false
									}
								}, {
									header : "名称",
									editname : "mc",
									editable : true,
									search : true,
									dataIndex : "MC",
									searchoptions : {
										sopt : ["eq", "cn"]
									},
									editoptions : {
										allowBlank : false
									}
								}, {
									header : "参数",
									editname : "cs",
									editable : true,
									search : true,
									dataIndex : "CS"
								}, {
									header : "备注",
									editname : "bz",
									editable : true,
									search : false,
									dataIndex : "BZ",
									editoptions : {
										allowBlank : true
									}
								}, {
									header : "系统类别",
									editname : "xtlb",
									editable : true,
									search : true,
									dataIndex : "XTLB",
									hidden : true,
									editoptions : {
										allowBlank : true
									}
								}, {
									header : "是否有效",
									editname : "yxbz",
									editable : true,
									edittype : "combobox",
									dictionary : "SF",
									search : true,
									stype : "combobox",
									renderer : function(value) {
										return formatter("SF", value);
									},
									dataIndex : "YXBZ",
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
		});