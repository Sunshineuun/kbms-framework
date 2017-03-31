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

	var viewport = Ext.create('Ext.Viewport', {
				layout : 'border',
				title : '知识管理系统'
			});

	var winWidth = viewport.getWidth();
	var winHeight = viewport.getHeight();

	viewport.add({
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