/*
 * ==============================================
 * (C)2016 Shanghai KingstarWinning Corporation. All rights reserved.
 * 项目名称：kbms-core 
 * 系统名称： @TODO
 * 文件名称： OperType.java
 * 注意事项：
 *
 *
 * $Id: OperType.java,v 1.6 2017-2-10下午2:09:05 yuan Exp $
 * ==============================================
 */
package com.winning.kbms.core.enums;

/** 
 * <p>系统基本操作操作类型</p> 
 * <p>说明: 针对系统界面基本操作</p> 
 * <p>备注: ADD(新增)/EDIT(编辑)/DEL(删除)/LOGICDEL(逻辑删除)</p> 
 * 
 * @version 1.0
 * @author 公司名 : 上海金仕达卫宁软件科技有限公司（Shanghai KingStar WinningSoft LTD.） <br />
 * 变更履历 <br />
 *
 */
public enum OperType {

	 ADD ("1"), EDIT ("2"), DEL ("3"), LOGICDEL("4");
	 
	private String operMsg;

    private OperType (String operMsg)
    {
        this.operMsg = operMsg;
    }

    public String getOperMsg ()
    {
        return operMsg;
    }
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */