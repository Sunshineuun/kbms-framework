package com.winning.domain.kbms.core;

import java.util.List;

import org.apache.ibatis.type.JdbcType;

import com.winning.annotations.mybatis.Column;
import com.winning.annotations.mybatis.Exclude;

public class BaseTreeNode extends ModifyDomain {
	private static final long serialVersionUID = 1L;

	private String name;
	private boolean leaf; // 是否是叶子节点
	private boolean expanded; // 是否展开
	private int orderFlag;// 顺序
	private String parentId;
	private String iconCls;
	private List<BaseTreeNode> children;
	private String code;

	@Exclude
	public List<BaseTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<BaseTreeNode> children) {
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	@Column(value = "expanded", jdbcType = JdbcType.BOOLEAN, resultHandler = "com.winning.kbms.core.commons.BooleanResultHandler")
	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	@Column("ORDER_FLAG")
	public int getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(int orderFlag) {
		this.orderFlag = orderFlag;
	}

	@Column("PARENT_ID")
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column("ICON_CLS")
	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
