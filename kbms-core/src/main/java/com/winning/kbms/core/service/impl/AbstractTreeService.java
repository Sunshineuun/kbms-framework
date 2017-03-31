package com.winning.kbms.core.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.winning.domain.kbms.core.BaseTreeNode;
import com.winning.kbms.core.commons.Constants;
import com.winning.kbms.core.dao.BaseDao;
import com.winning.kbms.core.service.TreeService;
import com.winning.mybatis.support.Criteria;

public abstract class AbstractTreeService<T extends BaseTreeNode> extends AbstractQueryService<T> implements
		TreeService<T> {

	@Autowired
	@Qualifier("kbmsBaseDao")
	private BaseDao baseDao;

	public BaseDao getDao() {
		return baseDao;
	}

	@Override
	@Transactional
	public void add(T node) {
		getDao().add(node);
	}

	@Override
	@Transactional
	public void update(T node) {
		getDao().update(node);
	}

	@Override
	@Transactional
	public void deleteById(String id) {
		getDao().deleteByCriteria(Criteria.create().andLike("code", id + Constants.TREE_CODE_SEPARATOR),
				getDomainClass());
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> getChildrenById(String id) {
		return this.queryByCriteria(Criteria.create().andEqualTo("parentId", id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> getAllChildrenById(String id) {
		List<T> list;
		if ("0".equals(id))
			list = getDao().getAll(getDomainClass());
		else
			list = this.queryByCriteria(Criteria.create().andLike("code", id + Constants.TREE_CODE_SEPARATOR)
					.andNotEqualTo("id", id));
		return assembleTreeByParentId(id, list);
	}

	/**
	 * 根据id，组装树
	 * 
	 * @param id
	 * @param treeNodes
	 * @return
	 */
	protected T assembleTreeById(String id, List<T> treeNodes) {
		T rootNode = null;
		for (T node : treeNodes) {
			if (StringUtils.equals(node.getId(), id)) {
				rootNode = node;
				break;
			}
		}

		if (rootNode.getChildren() == null)
			rootNode.setChildren(new ArrayList<BaseTreeNode>());

		rootNode.getChildren().addAll(assembleTreeByParentId(rootNode.getId(), treeNodes));
		return rootNode;
	}

	public int countByCriteria(Criteria criteria) {
		return getDao().countByCriteria(criteria, getDomainClass());
	}

	/**
	 * 根据parentId，组装树
	 * 
	 * @param parentId
	 * @param treeNodes
	 * @return
	 */
	protected List<T> assembleTreeByParentId(String parentId, List<T> treeNodes) {
		List<T> list = new ArrayList<T>();
		for (T node : treeNodes) {
			if (StringUtils.equals(node.getParentId(), parentId)) {
				list.add(node);
				assembleChildTreeNodes(node, treeNodes);
			}
		}
		sort(list);
		return list;
	}

	private void sort(List<T> list) {
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return o1.getOrderFlag() - o2.getOrderFlag();
			}

		});
	}

	@SuppressWarnings("unchecked")
	protected void assembleChildTreeNodes(final T node, List<T> childTreeNodes) {
		for (T childNode : childTreeNodes) {
			if (StringUtils.equals(childNode.getParentId(), node.getId())) {
				if (node.getChildren() == null)
					node.setChildren(new ArrayList<BaseTreeNode>());

				node.getChildren().add(childNode);
				assembleChildTreeNodes(childNode, childTreeNodes);
			}
		}
		if (node.getChildren() != null)
			sort(((List<T>) node.getChildren()));
	}

	@Transactional
	public void addParentNode(T node, String currNodeId) {
		this.add(node);
		T currNode = this.getById(currNodeId);
		if (currNode == null)
			return;

		currNode.setParentId(node.getId());
		StringBuilder codeSB = new StringBuilder();
		codeSB.append(node.getCode()).append(node.getId()).append(Constants.TREE_CODE_SEPARATOR);
		currNode.setCode(codeSB.toString());
		this.update(currNode);
	}

	@Transactional
	public void cutNode(String parentId, String cutNodeId) {
		T cutNode = this.getById(cutNodeId);
		T parentNode = this.getById(parentId);
		List<T> list = this.getAllChildrenById(cutNodeId);
		String oldCutNodeCode = cutNode.getCode();
		String newCode = parentNode.getCode() + cutNode.getId() + Constants.TREE_CODE_SEPARATOR;
		cutNode.setCode(newCode);
		cutNode.setParentId(parentId);

		this.update(cutNode);

		String code;
		for (T node : list) {
			code = StringUtils.replace(node.getCode(), oldCutNodeCode, newCode);
			node.setCode(code);
			this.update(node);
		}
	}
}
