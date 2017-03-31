/*
 * ==============================================
 * (C)2016 Shanghai KingstarWinning Corporation. All rights reserved.
 * 项目名称： 知识库管理系统
 * 系统名称： KBMS 3.0
 * 文件名称： Aaa.java
 * 注意事项：
 * Id: Aaa.java,v4.0 2016-6-06 wfm Exp
 * ==============================================
 */
package com.winning.domain.kbms.clinical;

import com.winning.annotations.mybatis.Column;
import com.winning.annotations.mybatis.Table;
import com.winning.domain.kbms.core.ModifyDomain;

/**
 * <p>
 * KBMS系统临时表实体类
 * </p>
 * 
 * @version 4.0
 * @author 公司名 : 上海金仕达卫宁软件科技有限公司（Shanghai KingStar WinningSoft LTD.） <br />
 *         变更履历 <br />
 *         2016-06-06 : 王富民: 代码作成<br />
 */
@Table(resultMapId = "aaaMap", value = "aaa")
public class Aaa extends ModifyDomain {
    private static final long serialVersionUID = 7439450825020234964L;
    private String code;
    private String costType;
    private Integer price2;
    private Integer price3;
    
    @Column ("CODE")
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    @Column ("COST_TYPE")
    public String getCostType() {
        return costType;
    }
    
    public void setCostType(String costType) {
        this.costType = costType;
    }
    
    @Column ("PRICE2")
    public Integer getPrice2() {
        return price2;
    }
    
    public void setPrice2(Integer price2) {
        this.price2 = price2;
    }
    
    @Column ("PRICE3")
    public Integer getPrice3() {
        return price3;
    }
    
    public void setPrice3(Integer price3) {
        this.price3 = price3;
    }
}
/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */