package com.winning.kbms.core.aop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.winning.kbms.core.annotations.Log;
import com.winning.kbms.core.cache.CacheClassName;
import com.winning.kbms.core.commons.LogMsgHandler;
import com.winning.kbms.core.domain.LogData;

@Order(1)
@Aspect
public class OperLogAspect implements InitializingBean {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource(name="cacheClassName")
    private CacheClassName cacheClassName;
	
	@Autowired(required = false)
	private List<LogMsgHandler> logMsgHandlers;

	private final List<LogData> cacheLogDatas = new ArrayList<LogData>(100);
	private final WriteLogThread writeLogThread = new WriteLogThread();
	private final long WAIT_TIME = 10 * 1000;
	private final int MAX_CACHE_SIZE = 100;

	@Around("@annotation(com.winning.kbms.core.annotations.Log) && @annotation(log)")
	public Object doAroundCacheMethed(ProceedingJoinPoint pjp, Log log) throws Throwable {
		Object result = null;
		switch (log.runTime()) {
		case BEFORE:
			beforeWriteLog(pjp, log);
			result = pjp.proceed();
			break;
		case AFTER:
			result = pjp.proceed();
			afterWriteLog(pjp, result, log);
			break;
		case ASYNC:
			result = pjp.proceed();
			asyncWriteLog(pjp, result, log);
			break;
		default:
			result = pjp.proceed();
			asyncWriteLog(pjp, result, log);
			break;
		}
		return result;
	}

	class WriteLogThread extends Thread {
		public WriteLogThread() {
			this.setName("日志处理线程");
		}

		@Override
		public void run() {
			while (true) {
				try {
					if (cacheLogDatas.isEmpty()) {
						synchronized (writeLogThread) {
							try {
								writeLogThread.wait(WAIT_TIME);
							} catch (InterruptedException e) {
							}
						}
					} else {
						List<LogData> tempList = null;
						synchronized (cacheLogDatas) {
							tempList = new ArrayList<LogData>(Arrays.asList(new LogData[cacheLogDatas.size()]));
							Collections.copy(tempList, cacheLogDatas);
							cacheLogDatas.clear();
						}
						for (LogData logData : tempList) {
							try {
								LogMsgHandler logMsgHandler = getLogMsgHandler(logData.getLog().handleClass());
								if (logMsgHandler != null)
									logMsgHandler.handle(logData);
							} catch (Exception e) {
								logger.error("日志线程添加失败！", e);
							}
						}
					}
				} catch (Exception e) {
					logger.error("日志线程添加失败！", e);
				}
			}
		}
	}

	// 在方法执行之前添加日志
	private void beforeWriteLog(ProceedingJoinPoint pjp, Log log) {
		LogData logData = null;
		try {
			LogMsgHandler logMsgHandler = this.getLogMsgHandler(log.handleClass());

			logData = logMsgHandler.initLogData(pjp, null, log);
			logMsgHandler.handle(logData);
		} catch (Exception e) {
			logger.error("日志添加失败！", e);
		}
	}

	// 在方法执行之后添加日志
	private void afterWriteLog(ProceedingJoinPoint pjp, Object result, Log log) {
		try {
			LogMsgHandler logMsgHandler = this.getLogMsgHandler(log.handleClass());
			if (logMsgHandler == null)
				return;

			LogData logData = logMsgHandler.initLogData(pjp, result, log);
			logMsgHandler.handle(logData);
		} catch (Exception e) {
			logger.error("日志添加失败！", e);
		}
	}

	// 异步添加日志
	private void asyncWriteLog(ProceedingJoinPoint pjp, Object result, Log log) {
		try {
			synchronized (cacheLogDatas) {
				LogMsgHandler logMsgHandler = getLogMsgHandler(log.handleClass());
				if (logMsgHandler == null)
					return;

				LogData logData = logMsgHandler.initLogData(pjp, result, log);
				if (logData != null)
					cacheLogDatas.add(logData);

				// 当最大缓存大于最大值时，唤醒日志执行线程
				if (cacheLogDatas.size() >= MAX_CACHE_SIZE) {
					synchronized (writeLogThread) {
						writeLogThread.notify();
					}
				}
			}
		} catch (Exception e) {
			logger.error("日志线程添加失败！", e);
		}
	}

	private LogMsgHandler getLogMsgHandler(Class<? extends LogMsgHandler> clazz) {
		for (LogMsgHandler lmh : logMsgHandlers) {
			if (lmh.getClass() == clazz) {
				return lmh;
			}
		}
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (logMsgHandlers == null || logMsgHandlers.isEmpty())
			return;

		writeLogThread.setPriority(Thread.MIN_PRIORITY);
		writeLogThread.start();
	}
}
