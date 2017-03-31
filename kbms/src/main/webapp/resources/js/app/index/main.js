tabPanel = Ext.create('Ext.TabPanel', {
	activeTab : 0,
	autoScroll : true,
	animCollapse : true,
	border : false,
	id : "tabPanel",
	items : [{
		title : "欢迎页面",
		iconCls : "home",
		closable : false,
		id : "homePanel",
		layout : "absolute",
		html : '<div style="background: url(../resources/images/index/welcome-bg.png);height:100%;width:100%;background-size: 100% 100%;">'
				+ '<div style="position: absolute;top: 30%;left:38%;text-align :center;margin-left :auto;margin-right : auto;width:257px;height:161px;background:url(../resources/images/index/welcome-logo.png)"></div></div>'
	}],
	plugins : Ext.create('Ext.ux.TabCloseMenu', {
				closeTabText : '关闭当前页',
				closeOthersTabsText : '关闭其他页',
				closeAllTabsText : '关闭所有页'
			})
});

function addTab(text, url) {
	var tabPage = tabPanel.getComponent(text);
	if (!tabPage) {
		tabPage = tabPanel.add(new Ext.ux.IFrame({
					xtype : 'iframepanel',
					id : text,
					title : text,
					closable : true,
					autoScroll : true,
					loadMask : '页面加载中...',
					border : false
				}))
	}
	tabPanel.setActiveTab(tabPage);
	tabPage.load(url);
}
