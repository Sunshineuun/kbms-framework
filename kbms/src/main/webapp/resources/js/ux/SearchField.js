Ext.define('Ext.ux.form.SearchField', {
			extend : 'Ext.form.field.Trigger',
			alias : 'widget.searchfield',
			trigger1Cls : Ext.baseCSSPrefix + 'form-clear-trigger',
			trigger2Cls : Ext.baseCSSPrefix + 'form-search-trigger',
			hasSearch : false,
			paramName : 'query',
			initComponent : function() {
				var me = this;

				me.callParent(arguments);
				me.on('specialkey', function(f, e) {
							if (e.getKey() == e.ENTER) {
								me.onTrigger2Click();
							}
						});
			},
			afterRender : function() {
				this.callParent();
				this.triggerCell.item(0).setDisplayed(false);
			},
			onTrigger1Click : function() {
				var me = this;

				if (me.hasSearch) {
					me.setValue('');
					if (me.emptyHandler)
						me.emptyHandler();
					me.hasSearch = false;
					if (me.triggerCell)
						me.triggerCell.item(0).setDisplayed(false);
					me.updateLayout();
				}
			},
			onTrigger2Click : function() {
				var me = this, value = me.getValue();
				if (me.handler)
					me.handler(value);
				me.hasSearch = true;
				if (me.triggerCell)
					me.triggerCell.item(0).setDisplayed(true);
				me.updateLayout();
			},
			handler : null,
			emptyHandler : null
		});