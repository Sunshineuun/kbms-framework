Ext.define('Ext.ux.Combobox', {
			extend : 'Ext.form.field.ComboBox',
			alias : 'widget.uxcombobox',
			initComponent : function() {
				var me = this;
				me.createStore();

				this.callParent();

				if (me.url) {
					me.on("expand", function() {
								me.getStore().load();
							})
				}
			},
			url : null,
			hasEmpty : false,
			labelAlign : "right",
			values : null,
			trigger1Cls:'x-form-clear-trigger',    
		    trigger2Cls:'x-form-arrow-trigger',  
		    onTrigger1Click: function() {
	    		this.clearValue(); 
	    		this.fireEvent('clear', this);
		    },  
		    onTrigger2Click: function() {
		    	this.onTriggerClick();
		    },
			changeLocalStore : function(values, initValue) {
				var me = this;
				if (!me.url && values) {
					me.store.loadData(values);
					if (initValue && values.length > 0)
						me.setValue(values[0][0]);
					else
						me.setValue(null);
				}
			},
			changeRemoteStore : function(url) {
				var me = this;
				if (me.url && url) {
					me.url = url;
					me.store.setProxy({
								type : "ajax",
								url : me.url
							});
				}
			},
			bindParams : null,
			setComboValue : function(value, doSelect) {
				var me = this;

				if (me.queryMode == "remote") {
					if (me.store.proxy.extraParams)
						Ext.apply(me.store.proxy.extraParams, {
									"searchParam" : value
								});
					else
						me.store.proxy.extraParams = {
							"searchParam" : value
						};
					me.store.load();

					Ext.apply(me.store.proxy.extraParams, {
								"searchParam" : null
							});

					me.setValue(value, doSelect);
				} else {
					me.setValue(value, doSelect);
				}
			},
			createStore : function() {
				var me = this;
				if (me.url == null) {
					if (me.hasEmpty)
						me.values.push([]);

					me.queryMode == "local";
					me.store = Ext.create('Ext.data.ArrayStore', {
								fields : [me.valueField, me.displayField],
								data : me.values
							});
				} else if (me.url) {
					me.queryMode == "remote";
					me.store = Ext.create('Ext.data.Store', {
								pageSize : me.limit ? me.limit : 25,
								proxy : {
									type : "ajax",
									actionMethods : "post",
									url : me.url,
									reader : {
										type : "array",
										root : "result"
									}
								},
								fields : [me.valueField, me.displayField],
								listeners : {
									beforeload : function(store, options) {
										if (me.bindParams) {
											if (store.proxy.extraParams)
												Ext
														.apply(
																store.proxy.extraParams,
																me.bindParams());
											else
												store.proxy.extraParams = me
														.bindParams();
										}

										if (me.hasEmpty) {
											Ext.apply(store.proxy.extraParams,
													{
														hasEmpty : true
													});
										} else {
											Ext.apply(store.proxy.extraParams,
													{
														hasEmpty : false
													});
										}
									}
								}
							});
				}
			},
			queryMode : 'local',
			forceSelection : true,
			triggerAction : 'all',
			displayField : 'label',
			valueField : 'value'
		});