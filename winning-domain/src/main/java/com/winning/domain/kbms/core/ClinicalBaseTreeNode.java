package com.winning.domain.kbms.core;

import java.util.Date;

import org.apache.ibatis.type.JdbcType;

import com.winning.annotations.kbms.core.Dictionary;
import com.winning.annotations.mybatis.Column;
import com.winning.annotations.mybatis.Exclude;

public class ClinicalBaseTreeNode extends BaseTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1014209865881855419L;

	private Date createTime;
	private String isEnable;
	private String isSubmit;

	@Column(value = "CREATE_TIME", jdbcType = JdbcType.TIMESTAMP)
	@Exclude
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(value = "IS_ENABLE")
	@Dictionary("SF")
	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	@Column(value = "IS_SUBMIT")
	@Dictionary("SF")
	public String getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(String isSubmit) {
		this.isSubmit = isSubmit;
	}
}
