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
						name : "children",
						mapping : "childNodes"
					}, {
						name : "expanded",
						mapping : "expanded",
						type : "boolean"
					}]
		});

function addTree(data) {
	var mainNavCmp = Ext.getCmp("mainNav");
	var createStore = function(id) { // 创建树面板数据源
		return Ext.create("Ext.data.TreeStore", {
					defaultRootId : id, // 默认的根节点id
					model : "NavMenu",
					proxy : {
						type : "ajax", // 获取方式
						url : ctx
								+ "/navMenu/getAllNavMenuChildrenByIdWithAuth.do"// 获取树节点的地址
					},
					clearOnLoad : true,
					nodeParam : "id"// 设置传递给后台的参数名,值是树节点的id属性
				});
	};

	for (var i = 0; i < data.length; i++) {
		var store = createStore(data[i].id);
		mainNavCmp.add(Ext.create("Ext.tree.Panel", {
					title : data[i].name,
					iconCls : data[i].iconCls,
					autoScroll : true,
//					useArrows : true,
					lines : true,
					rootVisible : false,
					loadMask : true,
					viewConfig : {
						loadingText : "正在加载..."
					},
					tools : [{
								type : 'refresh',
								tooltip : '刷新',
								handler : function(event, toolEl, panel) {
									panel.up().getStore().reload();
								}
							}],
					store : store,
					listeners : {
						itemclick : function(view, rec, item, index, event) {
							var leaf = rec.get('leaf');
							if (!leaf) // 判断是否为叶子节点
								return;

							addTab(rec.get("text"), ctx + rec.get("url"));
						}
					}
				}));
		mainNavCmp.doLayout();
	}
}
