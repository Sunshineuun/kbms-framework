package com.winning.kbms.core.service;

import java.util.List;

import com.winning.domain.kbms.core.BaseTreeNode;

public interface TreeService<T extends BaseTreeNode> extends QueryService<T> {
	public void add(T node);

	public void update(T node);

	public void deleteById(String id);

	/**
	 * 根据id获取子节点，只获取一层子节点
	 * 
	 * @param id
	 * @return
	 */
	public List<T> getChildrenById(String id);

	/**
	 * 根据id获取所有子节点
	 * 
	 * @param id
	 * @return
	 */
	public List<T> getAllChildrenById(String parentId);

	public void addParentNode(T node, String currNodeId);

	public void cutNode(String parentId, String cutNodeId);
}
