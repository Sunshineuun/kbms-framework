(function() {

	Ext.onReady(function() {
		function setCookie(name, value) {
			var days = 30;
			var exp = new Date();
			exp.setTime(exp.getTime() + days * 24 * 60 * 60 * 1000);
			document.cookie = name + "=" + escape(value) + ";expires="
					+ exp.toGMTString();
		}

		function getStore() {
			if (Ext.isIE) {
				return Ext.create('Ext.data.Store', {
					fields : ['value', 'name'],
					data : [{
								value : 'ext-theme-access/ext-theme-access-all.css',
								name : 'Accessibility'
							}, {
								value : 'ext-theme-classic/ext-theme-classic-all.css',
								name : 'Classic'
							}, {
								value : 'ext-theme-gray/ext-theme-gray-all.css',
								name : 'Gray'
							}]
				});
			} else {
				return Ext.create('Ext.data.Store', {
					fields : ['value', 'name'],
					data : [{
								value : 'ext-theme-access/ext-theme-access-all.css',
								name : 'Accessibility'
							}, {
								value : 'ext-theme-classic/ext-theme-classic-all.css',
								name : 'Classic'
							}, {
								value : 'ext-theme-gray/ext-theme-gray-all.css',
								name : 'Gray'
							}, {
								value : 'ext-theme-neptune/ext-theme-neptune-all.css',
								name : 'Neptune'
							}]
				});
			}
		}

		var toolbar;

		setTimeout(function() {
			var themeValue = getCookie("themeCss");
			if (!themeValue)
				themeValue = "ext-theme-classic/ext-theme-classic-all.css";

			toolbar = Ext.widget({
						xtype : 'toolbar',
						border : true,
						rtl : false,
						id : 'options-toolbar',
						style : "left : 500px;",
						floating : true,
						fixed : true,
						preventFocusOnActivate : true,
						draggable : {
							constrain : true
						},
						items : [{
									xtype : 'combo',
									rtl : false,
									width : 170,
									labelWidth : 35,
									fieldLabel : '主题',
									displayField : 'name',
									valueField : 'value',
									labelStyle : 'cursor:move;',
									margin : '0 5 0 0',
									store : getStore(),
									value : themeValue,
									listeners : {
										select : function(combo) {
											var theme = combo.getValue();
											setCookie("themeCss", theme);
											window.location.reload();
										}
									}
								}, {
									xtype : 'tool',
									type : 'close',
									rtl : false,
									handler : function() {
										toolbar.destroy();
									}
								}],

						constraintInsets : '0 -'
								+ (Ext.getScrollbarSize().width + 4) + ' 0 0'
					});
			toolbar.show();
			toolbar
					.alignTo(
							document.body,
							Ext.optionsToolbarAlign || 'tr-tr',
							[
									(Ext.getScrollbarSize().width + 4)
											* (Ext.rootHierarchyState.rtl
													? 1
													: -1) - 500,
									-(document.body.scrollTop || document.documentElement.scrollTop)]);

			var constrainer = function() {
				toolbar.doConstrain();
			};

			Ext.EventManager.onWindowResize(constrainer);
			toolbar.on('destroy', function() {
						Ext.EventManager.removeResizeListener(constrainer);
					});
		}, 100);

	});
})();