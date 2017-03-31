Ext.warn = function() {
	var msgCt;

	function createBox(t, s) {
		return '<div class="msg"><h3>' + t + '</h3><p>' + s + '</p></div>';
	}
	return {
		msg : function(title, format) {
			if (!msgCt) {
				msgCt = Ext.DomHelper.insertFirst(document.body, {
							id : 'msg-div'
						}, true);
			}
			var s = Ext.String.format.apply(String, Array.prototype.slice.call(
							arguments, 1));
			var m = Ext.DomHelper.append(msgCt, createBox(title, s), true);
			m.hide();
			m.slideIn('t').ghost("t", {
						delay : 1000,
						remove : true
					});
		},

		init : function() {
			if (!msgCt) {
				msgCt = Ext.DomHelper.insertFirst(document.body, {
							id : 'msg-div'
						}, true);
			}
		}
	};
}();

Ext.onReady(Ext.warn.init, Ext.warn);

function warn(msg, title) {
	Ext.Msg.show({
				title : title ? title : '提示',
				msg : msg,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.WARNING
			});
}

function info(msg, title) {
	Ext.Msg.show({
				title : title ? title : '提示',
				msg : msg,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.INFO
			});
}

function error(msg, title) {
	Ext.Msg.show({
				title : title ? title : '提示',
				msg : msg,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.ERROR
			});
}

function respWarn(response, successHandler, failedHandler) {
	var json = null;
	if (response.responseText)
		json = Ext.JSON.decode(response.responseText);

	if (json != null) {
		if (json.success == null) {
			successHandler(json);
			return;
		}

		if (json.success) {
			successHandler(json.result);
		} else {
			failedHandler(json.result);
		}
	} else {
		successHandler();
	}
}