package com.winning.domain.kbms.core;

import java.util.Date;

import org.apache.ibatis.type.JdbcType;

import com.winning.annotations.mybatis.Column;
import com.winning.annotations.mybatis.Table;

@Table (value = "KBMS_SYS_RESOURCE_FILE", resultMapId = "SysResourceFileMap")
public class SysResourceFile extends ModifyDomain
{
    private static final long serialVersionUID = 1L;

    // 文件名称
    private String fileName;

    // 上传时间
    private Date uploadTime;

    // 上传人
    private String uploadBy;

    // 是否有效
    private String enable;

    private String type;

    private String extension;

    @Column ("FILE_NAME")
    public String getFileName ()
    {
        return fileName;
    }

    public void setFileName (String fileName)
    {
        this.fileName = fileName;
    }

    @Column (value = "UPLOAD_TIME", jdbcType = JdbcType.TIMESTAMP)
    public Date getUploadTime ()
    {
        return uploadTime;
    }

    public void setUploadTime (Date uploadTime)
    {
        this.uploadTime = uploadTime;
    }

    @Column ("UPLOAD_BY")
    public String getUploadBy ()
    {
        return uploadBy;
    }

    public void setUploadBy (String uploadBy)
    {
        this.uploadBy = uploadBy;
    }

    public String getEnable ()
    {
        return enable;
    }

    public void setEnable (String enable)
    {
        this.enable = enable;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getExtension ()
    {
        return extension;
    }

    public void setExtension (String extension)
    {
        this.extension = extension;
    }

}
