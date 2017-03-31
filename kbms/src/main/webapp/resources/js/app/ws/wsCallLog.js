Ext.onReady(function() {
			Ext.widget("uxgridview", {
						startLoad : true,
						url : ctx + "/wsCallLog/wsCallLogService/list.do",
						border : true,
						inViewportShow : true,
						columns : [new Ext.grid.RowNumberer({
											width : 8,
											resizable : true
										}), {
									header : "ID",
									editname : "id",
									dataIndex : "ID",
									hidden : true
								}, {
									header : "调用人",
									editname : "username",
									dataIndex : "USERNAME",
									search : true,
									searchoptions : {
										sopt : ["cn", "eq"]
									}
								}, {
									header : "调用服务名称",
									editname : "serviceName",
									dataIndex : "SERVICE_NAME",
									search : true,
									searchoptions : {
										sopt : ["cn", "eq"]
									}
								}, {
									header : "调用日期",
									editname : "callDate",
									dataIndex : "CALL_DATE",
									search : true,
									searchoptions : {
										sopt : ["cn", "eq"]
									}
								}, {
									header : "调用次数",
									editname : "num",
									dataIndex : "NUM"
								}],
						jsonReader : {
							root : 'result.result',
							id : "ID",
							idName : "id",
							totalProperty : "result.totalCount",
							successProperty : 'success'
						},
						navGrid : {
							add : false,
							edit : false,
							del : false,
							search : true
						}
					});
		});