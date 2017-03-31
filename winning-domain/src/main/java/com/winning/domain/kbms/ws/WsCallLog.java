package com.winning.domain.kbms.ws;

import com.winning.annotations.mybatis.Column;
import com.winning.annotations.mybatis.Table;
import com.winning.domain.kbms.core.ModifyDomain;

@Table(value = "KBMS_LOG_WS_CALL", resultMapId = "WsCallLogMap")
public class WsCallLog extends ModifyDomain {
	private static final long serialVersionUID = 1L;

	// 调用人
	private String username;

	// 调用服务名称
	private String serviceName;

	// 调用日期
	private String callDate;

	// 调用次数
	private int num;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column("SERVICE_NAME")
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Column("CALL_DATE")
	public String getCallDate() {
		return callDate;
	}

	public void setCallDate(String callDate) {
		this.callDate = callDate;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
