Ext.onReady(function() {
	var resourceFileStore = Ext.create('Ext.data.Store', {
				fields : ["ID", "FILE_NAME", "UPLOAD_TIME", "UPLOAD_BY",
						"TYPE", "EXTENSION"],
				pageSize : 50, // 每页显示条数
				remoteSort : true,
				proxy : {
					type : 'ajax',
					url : ctx
							+ "/sysResourceFile/sysResourceFileService/list.do",
					actionMethods : {
						create : 'POST',
						read : 'POST',
						update : 'POST',
						destroy : 'POST'
					},
					reader : {
						root : 'result.result',
						id : "ID",
						totalProperty : "result.totalCount",
						successProperty : 'success'
					}
				},
				autoLoad : true
			});

	var resourceFileGird = Ext.create("Ext.grid.Panel", {
		forceFit : true,
		store : resourceFileStore,
		border : false,
		tbar : Ext.create("Ext.toolbar.Toolbar", {
					items : [{
								xtype : "button",
								text : "上传文件",
								hidden : !authc.add,
								handler : function() {
									showUploadWin();
								}
							}, {
								xtype : "button",
								hidden : true,
								text : "修改文件信息"
							}, {
								xtype : "button",
								text : "删除文件",
								hidden : !authc.del,
								iconCls : "table_delete",
								handler : function() {
									removeFile();
								}
							}]
				}),
		columns : [new Ext.grid.RowNumberer({
							width : 20,
							resizable : true
						}), {
					header : "id",
					hidden : true,
					dataIndex : "ID"
				}, {
					header : "文件名",
					dataIndex : "FILE_NAME"
				}, {
					header : "拓展名",
					dataIndex : "EXTENSION"
				}, {
					header : "文件类型",
					dataIndex : "TYPE"
				}, {
					header : "上传时间",
					dataIndex : "UPLOAD_TIME"
				}, {
					header : "上传人",
					dataIndex : "UPLOAD_BY"
				}, {
					header : "下载",
					xtype : 'actioncolumn',
					width : 30,
					items : [{
						iconCls : "plugin_link",
						tooltip : '文件下载',
						handler : function(grid, rowIndex, colIndex) {
							var rec = grid.getStore().getAt(rowIndex);
							var id = rec.get("ID");
							window.location.href = ctx
									+ "/sysResourceFile/downloadResourcesFile.do?id="
									+ id;
						}
					}]
				}],
		bbar : new Ext.PagingToolbar({
					displayInfo : true,
					emptyMsg : "没有数据",
					store : resourceFileStore
				})
	});

	function showUploadWin() {
		Ext.create("Ext.window.Window", {
			width : 350,
			layout : 'fit',
			frame : true,
			iconCls : "key",
			title : "上传文件",
			modal : true,
			items : [{
						xtype : "form",
						bodyStyle : 'paddingTop:5px;paddingBottom:5px;',
						items : [{
									xtype : "uxcombobox",
									fieldLabel : "文件类型",
									width : 300,
									name : "type",
									editable : false,
									allowBlank : false,
									values : [["1", "操作手册"], ["2", "样例代码"]]
								}, {
									xtype : 'filefield',
									name : 'file',
									width : 300,
									labelAlign : "right",
									fieldLabel : '上传文件',
									allowBlank : false,
									buttonText : '选择'
								}]
					}],
			buttons : [{
				text : '上传',
				handler : function() {
					var windowCmp = this.up("window");
					var form = windowCmp.getComponent(0).getForm();

					if (form.isValid()) {
						form.submit({
							url : ctx
									+ "/sysResourceFile/uploadResourcesFile.do",
							waitMsg : '正在上传文件...',
							success : function(fp, o) {
								windowCmp.destroy();
								resourceFileStore.loadPage(1);
							},
							failure : function(fp, o) {
								error(o.result.result);
							}
						});
					}
				}
			}, {
				text : '取消',
				handler : function() {
					this.up("window").destroy();
				}
			}]
		}).show();
	}

	function removeFile() {
		var selections = resourceFileGird.getSelectionModel().getSelection();
		if (!(selections && selections.length > 0)) {
			warn("请选择需要删除的数据！");
			return;
		}

		Ext.Msg.confirm('提示', '确定删除此文件吗?', function(btn) {
					var selection = selections[0];

					Ext.Ajax.request({
								url : ctx
										+ "/sysResourceFile/removeResourcesFile.do",
								params : {
									id : selection.get("ID")
								},
								success : function(response, opts) {
									respWarn(response, function() {
												resourceFileStore.loadPage(1);
											}, function(msg) {
												error(msg);
											});
								},
								failure : function(response, opts) {
									error("删除失败！");
								}
							});
				});
	}

	Ext.create('Ext.container.Viewport', {
				layout : 'border',
				items : [{
							region : 'center',
							border : false,
							layout : "fit",
							items : resourceFileGird
						}]
			});
});