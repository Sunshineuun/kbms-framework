package com.winning.kbms.core.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.winning.domain.kbms.core.DictionaryType;
import com.winning.kbms.core.service.DictionaryTypeService;
import com.winning.kbms.core.service.ManagementService;

@Controller
@RequestMapping("/dictionaryType")
public class DictionaryTypeController extends AbstractManagementController<DictionaryType> {
	@Resource(name = "dictionaryTypeService")
	private DictionaryTypeService dictionaryTypeService;

	@Override
	protected ManagementService<DictionaryType> getManagementService() {
		return dictionaryTypeService;
	}

}
