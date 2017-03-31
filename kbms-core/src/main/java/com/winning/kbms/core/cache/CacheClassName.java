/*
 * ==============================================
 * (C)2016 Shanghai KingstarWinning Corporation. All rights reserved.
 * 项目名称：kbms-core 
 * 系统名称： @TODO
 * 文件名称： CacheClassName.java
 * 注意事项：
 *
 *
 * $Id: CacheClassName.java,v 1.6 2017-2-13上午9:54:46 yuan Exp $
 * ==============================================
 */
package com.winning.kbms.core.cache;

import java.util.HashMap;
import java.util.Map;

/** 
 * <p>缓存要用到的类名称</p> 
 * <p>说明: </p> 
 * <p>备注:1.规则配置类的知识所对应的domain类；2.日志所要记录模块对应的domain类。 </p> 
 * 
 * @version 1.0
 * @author 公司名 : 上海金仕达卫宁软件科技有限公司（Shanghai KingStar WinningSoft LTD.） <br />
 * 变更履历 <br />
 *
 */
public class CacheClassName {
	/**
	 * 规则配置类的知识所对应的domain类
	 */
	private Map<String, String> ruleClassNameAndMsgMap = new HashMap<String, String>();
	
	/**
	 * 日志所要记录模块所对应的domain类
	 */
	private Map<String, String> logClassNameAndMsgMap = new HashMap<String, String>();
	
	public Map<String, String> getRuleClassNameAndMsgMap() {
		return ruleClassNameAndMsgMap;
	}

	public void setRuleClassNameAndMsgMap(Map<String, String> ruleClassNameAndMsgMap) {
		this.ruleClassNameAndMsgMap = ruleClassNameAndMsgMap;
	}

	public Map<String, String> getLogClassNameAndMsgMap() {
		return logClassNameAndMsgMap;
	}

	public void setLogClassNameAndMsgMap(Map<String, String> logClassNameAndMsgMap) {
		this.logClassNameAndMsgMap = logClassNameAndMsgMap;
	}

	public String getRuleClassNameAndMsgMap(String key) {
        return ruleClassNameAndMsgMap.get(key);
    }
    
    public void addRuleClassNameAndMsgMap(String key, String value) {
    	ruleClassNameAndMsgMap.put(key, value);
    }

    public void removeRuleClassNameAndMsgMap(String key) {
    	ruleClassNameAndMsgMap.remove(key);
    }
    
    public String getLogClassNameAndMsgMap(String key) {
        return logClassNameAndMsgMap.get(key);
    }
    
    public void addLogClassNameAndMsgMap(String key, String value) {
    	logClassNameAndMsgMap.put(key, value);
    }

    public void removeLogClassNameAndMsgMap(String key) {
    	logClassNameAndMsgMap.remove(key);
    }
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */