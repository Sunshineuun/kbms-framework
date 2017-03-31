package com.winning.kbms.core.enums;

public enum OperLogType
{
	/**
	 * @date 2016-08-01 14:21:47
	 * @author {qiushengming}
	 * 临床需要添加提交类型的操作日志。
	 */
    ADD ("新增"), EDIT ("修改"), DEL ("删除"), OTHER ("其他操作"),SUBMIT("提交"),
    MAPPING("映射"),LOGICDEL("逻辑删除"),LOGICREDUCTION("逻辑恢复"),NOTMAPPING("无映射"),
    VALIDSTATUS("审核");

    private String operMsg;

    private OperLogType (String operMsg)
    {
        this.operMsg = operMsg;
    }

    public String getOperMsg ()
    {
        return operMsg;
    }
}
