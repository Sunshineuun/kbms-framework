package com.winning.kbms.core.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.winning.domain.kbms.core.BaseTreeNode;
import com.winning.kbms.core.service.TreeGridService;
import com.winning.kbms.core.utils.GridSQLBuilder;

public abstract class AbstractTreeGridService<T extends BaseTreeNode> extends AbstractTreeService<T> implements
		TreeGridService<T> {

	public List<T> searchTree(Map<String, Object> params) {
		GridSQLBuilder.buildSqlFragmentAndParams(params);
		List<T> nodes = getDao().queryBySql(getSearchSql(params), getDomainClass(), params);
		if (nodes == null || nodes.isEmpty())
			return Collections.emptyList();

		return assembleTreeByParentId("0", nodes);
	}

	public abstract String getSearchSql(Map<String, Object> params);
}
