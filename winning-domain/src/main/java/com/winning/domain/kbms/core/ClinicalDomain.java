package com.winning.domain.kbms.core;

import java.util.Date;

import org.apache.ibatis.type.JdbcType;

import com.winning.annotations.kbms.core.Dictionary;
import com.winning.annotations.mybatis.Column;
import com.winning.annotations.mybatis.Exclude;
import com.winning.domain.kbms.core.ModifyDomain;

public class ClinicalDomain extends ModifyDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = -499170102438909269L;

	private Date createTime = new Date();
	private String isEnable = "1" ;
	private String isSubmit = "0" ;

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
//	@Dictionary("SF")
//	@IncludeLog(value = "是否提交", order = 20, required = true)
	public String getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(String isSubmit) {
		this.isSubmit = isSubmit;
	}

}
