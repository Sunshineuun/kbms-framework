package com.winning.kbms.core.commons;

import org.aspectj.lang.ProceedingJoinPoint;

import com.winning.kbms.core.annotations.Log;
import com.winning.kbms.core.domain.LogData;

public interface LogMsgHandler {
	public LogData initLogData(ProceedingJoinPoint pjp, Object result, Log log);

	public void handle(LogData logData);
}
