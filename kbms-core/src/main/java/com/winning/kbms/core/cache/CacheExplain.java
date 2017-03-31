/*
 * ==============================================
 * (C)2016 Shanghai KingstarWinning Corporation. All rights reserved.
 * 项目名称：kbms-core 
 * 系统名称： @TODO
 * 文件名称： CacheExplain.java
 * 注意事项：
 *
 *
 * $Id: CacheExplain.java,v 1.6 2017-2-13上午9:41:47 yuan Exp $
 * ==============================================
 */
package com.winning.kbms.core.cache;

import java.util.HashMap;
import java.util.Map;

/** 
 * <p>Class类功能定义的说明性内容。（请以句号“。”结尾、段中换行请使用“<br/>”符号）</p> 
 * <p>说明: </p> 
 * <p>备注: </p> 
 * 
 * @version 1.0
 * @author 公司名 : 上海金仕达卫宁软件科技有限公司（Shanghai KingStar WinningSoft LTD.） <br />
 * 变更履历 <br />
 *
 */
public class CacheExplain {
	public static Map<String, Map<String, String>> reviewMap = new HashMap<String, Map<String, String>>();
	
	public static Map<String, String> getReviewValue(String key) {
        return reviewMap.get(key);
    }
    
    public static void addReview(String key, Map<String, String> value) {
    	reviewMap.put(key, value);
    }

    public static void removeReview(String key) {
    	reviewMap.remove(key);
    }
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */