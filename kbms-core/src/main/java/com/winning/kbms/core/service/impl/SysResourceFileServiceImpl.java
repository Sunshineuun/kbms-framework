package com.winning.kbms.core.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.winning.domain.kbms.core.SysResourceFile;
import com.winning.kbms.core.service.SysResourceFileService;

@Service("sysResourceFileService")
public class SysResourceFileServiceImpl extends AbstractManagementService<SysResourceFile> implements
		SysResourceFileService {

	@Override
	protected String getSearchSql(Map<String, Object> params) throws Exception {
		return "select * from (select id,file_name,to_char(upload_time,'YYYY-MM-DD HH24:mi:ss') upload_time,upload_by,type,extension from KBMS_SYS_RESOURCE_FILE where enable='1')";
	}

	@Override
	protected String getCountSql(Map<String, Object> params) throws Exception {
		return "select count(1) from (select id,file_name,to_char(upload_time,'YYYY-MM-DD HH24:mi:ss') upload_time,upload_by,type,extension from KBMS_SYS_RESOURCE_FILE where enable='1')";
	}
}
