function getCookie(name) {
	var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
	if (arr = document.cookie.match(reg))
		return unescape(arr[2]);
	else
		return null;
}
// 写cookies
function setCookie(name, value) {
	var days = 30;
	var exp = new Date();
	exp.setTime(exp.getTime() + days * 24 * 60 * 60 * 1000);
	document.cookie = name + "=" + escape(value) + ";expires="
			+ exp.toGMTString();
}

function formatter(dictionaryKey, value) {
	var values = dictionary[dictionaryKey];
	for (var i = 0; i < values.length; i++) {
		if (values[i][0] == value)
			return values[i][1];
	}
	if (value == null)
		return "";
	return value;
}

function strLineFeedFormatter(cellvalue, lineLen) {
	if (cellvalue) {
		var result = "";

		var i = 1;
		lineLen = lineLen ? lineLen : 50;
		while (true) {
			if (i * lineLen >= cellvalue.length) {
				result += cellvalue.substring((i - 1) * lineLen,
						cellvalue.length);
				break;
			} else {
				result += cellvalue.substring((i - 1) * lineLen, i * lineLen)
						+ "<br/>";
			}
			i++;
		}
		return result;
	}
	return cellvalue;
}

String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};

function createLocalCombobox(comboboxData, initDefaultValue, configData) {
	var initValue = null;
	if (initDefaultValue) {
		if (comboboxData && comboboxData.length > 0)
			initValue = comboboxData[0][0];
	}

	var config = {
		value : initValue,
		editable : false,
		queryMode : 'local',
		forceSelection : true,
		values : comboboxData,
		triggerAction : 'all',
		displayField : 'label',
		valueField : 'value'
	};
	Ext.apply(config, configData);

	return Ext.create("Ext.ux.Combobox", config);
}

function array2Str(values) {
	if (values == null) {
		return "";
	}

	var ids = "";
	for (var i = 0; i < values.length; i++) {
		if (i < (values.length - 1)) {
			ids = ids + values[i] + ",";
		} else {
			ids = ids + values[i];
		}
	}
	return ids;
}

String.prototype.endWith = function(s) {
	if (s == null || s == "" || this.length == 0 || s.length > this.length)
		return false;
	if (this.substring(this.length - s.length) == s)
		return true;
	else
		return false;
	return true;
}

String.prototype.startWith = function(s) {
	if (s == null || s == "" || this.length == 0 || s.length > this.length)
		return false;
	if (this.substr(0, s.length) == s)
		return true;
	else
		return false;
	return true;
}

function download_file(url, params) {
	var form = document.createElement("form");
	document.body.appendChild(form);
	form.action = url;
	form.method = "post";
	form.style.display = "none";

	var input = null;
	for (var name in params) {
		input = document.createElement("input");
		input.name = name;
		input.value = params[name];
		form.appendChild(input);
	}
	form.submit();
	document.body.removeChild(form);
}

Array.prototype.contains = function(elem) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == elem) {
			return true;
		}
	}
	return false;
}

function ProgressBox(msg, scale) {
	var progressBox = Ext.MessageBox.show({
				title : "进度提示",
				modal : true,
				closable : false,
				width : 550,
				progress : true
			});
	this.msg = msg;
	scale = scale ? scale : 0.05;
	var progressValue = 0;
	var i = 0;
	var task = {
		run : function() {
			progressValue = progressValue
					+ floatMul((1 - progressValue), scale);
			if (msg)
				progressBox.updateProgress(progressValue, msg + "，总耗时：" + i++
								+ "秒");
			else
				progressBox.updateProgress(progressValue);
		},
		interval : 1000
	}
	this.start = function() {
		Ext.TaskManager.start(task);
	}
	this.end = function(nextExec) {
		Ext.TaskManager.stop(task);
		if (msg)
			progressBox.updateProgress(1, msg + "，总耗时：" + i + "秒");
		else
			progressBox.updateProgress(1);

		setTimeout(function() {
					progressBox.hide();
					if (nextExec && typeof(nextExec) == "function")
						nextExec();
				}, 500);
	}
}

function floatMul(arg1, arg2) {
	var m = 0, s1 = null, s2 = null;
	
	s1 = arg1 == null ? '0.0' : arg1.toString();
	s2 = arg2 == null ? '0.0' : arg2.toString();
	
	try {
		m += s1.split(".")[1].length;
	} catch (e) {
	}
	try {
		m += s2.split(".")[1].length;
	} catch (e) {
	}
	return Number(s1.replace(".", "")) * Number(s2.replace(".", ""))
			/ Math.pow(10, m);
}