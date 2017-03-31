/*
 * ==============================================
 * (C)2016 Shanghai KingstarWinning Corporation. All rights reserved.
 * 项目名称：kbms 
 * 系统名称： @TODO
 * 文件名称： Test.java
 * 注意事项：
 *
 *
 * $Id: Test.java,v 1.6 2017年3月29日下午2:29:51 qiushengming Exp $
 * ==============================================
 */
package com.winning.sunshine.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class Test {

    private static Logger logger = LoggerFactory.getLogger(Test.class);
    
    public static void main(String[] args) {
        logger.info(System.getProperties().getProperty("file.encoding"));
    }
}

/* Copyright (C) 2017, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */