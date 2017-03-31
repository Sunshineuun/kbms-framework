package com.winning.kbms.core.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.winning.domain.kbms.core.AppOption;
import com.winning.kbms.core.exceptions.ServiceException;
import com.winning.kbms.core.service.AppOptionService;
import com.winning.mybatis.support.Criteria;

@Service("kbmsAppOptionService")
public class AppOptionServiceImpl extends AbstractManagementService<AppOption> implements AppOptionService {
	@Override
	@CacheEvict(value = "dictionaryCache", key = "#appOption?.lbdm")
	public void add(AppOption appOption) throws ServiceException {
		super.add(appOption);
	}

	@Override
	@CacheEvict(value = "dictionaryCache", key = "#appOption?.lbdm")
	public void update(AppOption appOption) throws ServiceException {
		super.update(appOption);
	}

	@Override
	@CacheEvict(value = "dictionaryCache", key = "#lbdm")
	public void deleteByLbdm(String lbdm) throws ServiceException {
		this.deleteByCriteria(Criteria.create().andEqualTo("lbdm", lbdm));
	}
}
