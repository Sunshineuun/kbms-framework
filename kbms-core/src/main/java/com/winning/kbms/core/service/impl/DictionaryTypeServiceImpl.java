package com.winning.kbms.core.service.impl;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winning.domain.kbms.core.DictionaryType;
import com.winning.kbms.core.exceptions.ServiceException;
import com.winning.kbms.core.service.DictionaryService;
import com.winning.kbms.core.service.DictionaryTypeService;
import com.winning.mybatis.support.Criteria;

@Service("dictionaryTypeService")
public class DictionaryTypeServiceImpl extends AbstractManagementService<DictionaryType> implements
		DictionaryTypeService {

	@Resource(name = "dictionaryService")
	private DictionaryService dictionaryService;

	@Override
	@Transactional
	@CacheEvict(value = "dictionaryCache", key = "#typeCode")
	public void deleteById(String id) throws ServiceException {
		DictionaryType dt = this.getById(id);
		dictionaryService.deleteDictionaryByTypeCode(dt.getTypeCode());
		super.deleteById(id);
		
	};
	
	
	@Transactional
	@CacheEvict(value = "dictionaryCache", key = "#typeCode")
	public void deleteDictionaryTypeByTypeCode(String typeCode) throws ServiceException {
		this.deleteByCriteria(Criteria.create().andEqualTo("typeCode", typeCode));
		dictionaryService.deleteDictionaryByTypeCode(typeCode);
	}
}
