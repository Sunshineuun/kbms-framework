package com.winning.domain.kbms.core;

import java.util.Date;

import org.apache.ibatis.type.JdbcType;

import com.winning.annotations.mybatis.Column;
import com.winning.annotations.mybatis.Id;
import com.winning.domain.BaseDomain;

public class ModifyDomain extends BaseDomain {
	private static final long serialVersionUID = -1938456834768127078L;

	private String id;
	private Date updateTime;
	private String updateBy;

	@Id("ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(value = "UPDATE_TIME", jdbcType = JdbcType.TIMESTAMP)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column("UPDATE_BY")
	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

}
