package com.winning.domain.kbms.core;

import com.winning.annotations.mybatis.Column;
import com.winning.annotations.mybatis.Table;

@Table (value = "KBMS_DICTIONARY_TYPE", resultMapId = "DictionaryTypeMap")
public class DictionaryType extends ModifyDomain
{
    private static final long serialVersionUID = 1L;

    // 类别代码
    private String typeCode;

    // 类别名称
    private String typeName;

    // 有效标志
    private String enable;

    // 备注
    private String remark;

    @Column("TYPE_CODE")
    public String getTypeCode ()
    {
        return typeCode;
    }

    public void setTypeCode (String typeCode)
    {
        this.typeCode = typeCode;
    }

    @Column("TYPE_NAME")
    public String getTypeName ()
    {
        return typeName;
    }

    public void setTypeName (String typeName)
    {
        this.typeName = typeName;
    }

    public String getEnable ()
    {
        return enable;
    }

    public void setEnable (String enable)
    {
        this.enable = enable;
    }

    public String getRemark ()
    {
        return remark;
    }

    public void setRemark (String remark)
    {
        this.remark = remark;
    }

}
