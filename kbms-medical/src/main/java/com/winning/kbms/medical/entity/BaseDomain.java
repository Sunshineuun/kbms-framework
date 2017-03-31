package com.winning.kbms.medical.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 */
@MappedSuperclass
public class BaseDomain implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * 标识
	 */
	protected Integer id;

	/**
	 * 创建日期
	 */
	protected Date createDate;

	/**
	 * 更新日期
	 */
	protected Date updateDate;

	/**
	 * 备注
	 */
	protected String comments;

	/**
	 * 是否使用
	 */
	protected Integer enable;

	
	public BaseDomain()
	{
		super();
		this.enable = 1;
	}


	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	@Column(name = "Comments", length = 3999)
	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreateDate", length = 23)
	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UpdateDate", length = 23)
	public Date getUpdateDate()
	{
		return updateDate;
	}

	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}


	@Column(name = "Enable", columnDefinition = "int default 1")
	public Integer getEnable()
	{
		return enable;
	}

	public void setEnable(Integer enable)
	{
		this.enable = enable;

	}

	public Object eagerLoad()
	{
		return this;
	}

	@Override
	public boolean equals(Object baseDomain)
	{
		if (baseDomain == null)
		{
			return false;
		}
		if (this.id == null || this.id == 0
				|| ((BaseDomain) baseDomain).id == null
				|| ((BaseDomain) baseDomain).id == 0)
		{
			return false;
		}
		else
		{
			if (baseDomain.getClass().equals(this.getClass())
					&& ((BaseDomain) baseDomain).id.equals(this.id))
			{
				return true;
			}
		}
		return false;
	}

	protected Float convertFloat(Float param)
	{
		if (param != null && param.isNaN())
		{
			return 0f;
		}
		return param;
	}

	protected Double convertDouble(Double param)
	{
		if (param != null && param.isNaN())
		{
			return 0d;
		}
		return param;
	}

}
