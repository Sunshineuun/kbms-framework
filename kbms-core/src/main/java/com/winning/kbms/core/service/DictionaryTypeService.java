package com.winning.kbms.core.service;

import com.winning.domain.kbms.core.DictionaryType;
import com.winning.kbms.core.exceptions.ServiceException;

public interface DictionaryTypeService extends ManagementService<DictionaryType>
{
	public void deleteDictionaryTypeByTypeCode(String typeCode) throws ServiceException;
}