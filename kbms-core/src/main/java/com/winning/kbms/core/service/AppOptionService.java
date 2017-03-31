package com.winning.kbms.core.service;

import com.winning.domain.kbms.core.AppOption;
import com.winning.kbms.core.exceptions.ServiceException;

public interface AppOptionService extends ManagementService<AppOption> {
	
	public void deleteByLbdm (String lbdm) throws ServiceException;
}
