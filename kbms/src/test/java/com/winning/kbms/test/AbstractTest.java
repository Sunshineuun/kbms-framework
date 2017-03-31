/*
 * ==============================================
 * (C)2016 Shanghai KingstarWinning Corporation. All rights reserved.
 * 项目名称： 知识库管理系统
 * 系统名称： KBMS 3.0
 * 文件名称： AbstractTest.java
 * 注意事项：
 * Id: AbstractTest.java,v4.0 2016-6-06 wfm Exp
 * ==============================================
 */
package com.winning.kbms.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * KBMS系统测试父类
 * </p>
 * 
 * @version 4.0
 * @author 公司名 : 上海金仕达卫宁软件科技有限公司（Shanghai KingStar WinningSoft LTD.） <br />
 *         变更履历 <br />
 *         2016-06-06 : 王富民: 代码作成<br />
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:spring-configs/applicationContext*.xml" })
public abstract class AbstractTest {

    /** 复写的测试类 */
    @Test
//    @Transactional
    public abstract void test();
}
/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */