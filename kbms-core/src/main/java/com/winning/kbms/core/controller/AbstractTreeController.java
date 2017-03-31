package com.winning.kbms.core.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winning.domain.kbms.core.BaseTreeNode;
import com.winning.kbms.core.commons.Constants;
import com.winning.kbms.core.domain.JResult;
import com.winning.kbms.core.exceptions.ValidationException;
import com.winning.kbms.core.service.TreeService;
import com.winning.utils.RandomUIDUtils;

public abstract class AbstractTreeController<T extends BaseTreeNode> extends BaseController {
	protected abstract TreeService<T> getTreeService();

	@RequestMapping("/add")
	@ResponseBody
	public JResult add(T node) {
		addValidation(node);

		if (StringUtils.isEmpty(node.getId()))
			node.setId(generateId());

		T parentNode = this.getTreeService().getById(node.getParentId());
		StringBuilder codeSB = new StringBuilder();
		if (parentNode == null)
			codeSB.append(node.getId()).append(Constants.TREE_CODE_SEPARATOR);
		else
			codeSB.append(parentNode.getCode()).append(node.getId()).append(Constants.TREE_CODE_SEPARATOR);
		node.setCode(codeSB.toString());
		getTreeService().add(node);

		return new JResult(true, "添加成功！");
	}

	@RequestMapping("/addParentNode")
	@ResponseBody
	public JResult addParentNode(T node, String currNodeId) {
		addValidation(node);

		if (StringUtils.isEmpty(node.getId()))
			node.setId(generateId());

		T parentNode = this.getTreeService().getById(node.getParentId());
		StringBuilder codeSB = new StringBuilder();
		if (parentNode == null)
			codeSB.append(node.getId()).append(Constants.TREE_CODE_SEPARATOR);
		else
			codeSB.append(parentNode.getCode()).append(node.getId()).append(Constants.TREE_CODE_SEPARATOR);
		node.setCode(codeSB.toString());
		getTreeService().addParentNode(node, currNodeId);

		return new JResult(true, "添加成功！");
	}

	@RequestMapping("/update")
	@ResponseBody
	public JResult update(T node) {
		editValidation(node);

		getTreeService().update(node);

		return new JResult(true, "修改成功！");
	}

	@RequestMapping("/remove")
	@ResponseBody
	public JResult remove(T node) {
		delValidation(node);

		getTreeService().deleteById(node.getId());

		return new JResult(true, "删除成功！");
	}

	/**
	 * 根据ID获取子节点，只获取一层子节点
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/getChildrenById")
	@ResponseBody
	public List<T> getChildrenById(@RequestParam("id") String id) {
		return getTreeService().getChildrenById(id);
	}

	/**
	 * 根据ID获取所有子节点
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/getAllChildrenById")
	@ResponseBody
	public List<T> getAllChildrenById(@RequestParam("id") String id) {
		return getTreeService().getAllChildrenById(id);
	}

	/**
	 * 根据ID获取节点
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/get")
	@ResponseBody
	public T get(@RequestParam("id") String id) {
		return getTreeService().getById(id);
	}

	@RequestMapping("/cutNode")
	@ResponseBody
	public void cutNode(@RequestParam("parentId") String parentId, @RequestParam("cutNodeId") String cutNodeId) {
		getTreeService().cutNode(parentId, cutNodeId);
	}

	protected String generateId() {
		return RandomUIDUtils.getUUID();
	}

	protected void addValidation(T obj) throws ValidationException {
	}

	protected void editValidation(T obj) throws ValidationException {
	}

	protected void delValidation(T obj) throws ValidationException {
	}
}
