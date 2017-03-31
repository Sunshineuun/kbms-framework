package com.winning.kbms.core.domain;

import java.io.Serializable;
import java.util.Date;

import com.winning.kbms.core.annotations.Log;

public class LogData implements Serializable {
	private static final long serialVersionUID = 1L;
	private Object[] args;
	private Log log;
	private Date operTime;
	private String userName;
	private Object result;
	private Object otherParam;
	private Class<?> domainClass;

	public LogData(String userName, Object[] args, Object result, Log log, Date operTime, Class<?> domainClass) {
		this.userName = userName;
		this.args = args;
		this.log = log;
		this.operTime = operTime;
		this.result = result;
		this.domainClass = domainClass;
	}

	public Class<?> getDomainClass() {
		return domainClass;
	}

	public void setDomainClass(Class<?> domainClass) {
		this.domainClass = domainClass;
	}

	public Object getOtherParam() {
		return otherParam;
	}

	public void setOtherParam(Object otherParam) {
		this.otherParam = otherParam;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}