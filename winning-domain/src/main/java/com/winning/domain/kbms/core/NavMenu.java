package com.winning.domain.kbms.core;

import java.util.Date;

import org.apache.ibatis.type.JdbcType;

import com.winning.annotations.mybatis.Column;
import com.winning.annotations.mybatis.Exclude;
import com.winning.annotations.mybatis.Table;

@Table(value = "kbms_nav_menu", resultMapId = "NavMenuMap", selectSql="SELECT ID, DECODE(MODEL_ID,'0',NAME,NAME||'('||MODEL_ID||')') NAME,LEAF,EXPANDED,ORDER_FLAG,URL,PARENT_ID,ICON_CLS,AUTH_CODE,TYPE,VIEW_FLAG FROM KBMS_NAV_MENU")
public class NavMenu extends SyncTreeNode {

	private static final long serialVersionUID = 1L;
	private String url;
	private int type;
	private String authCode;
	private boolean viewFlag;
	private boolean check;
	private String modelId;    //模块ID

	@Column(value = "IS_CHECK", jdbcType = JdbcType.BOOLEAN, isAdd = false, isUpdate = false)
	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column("AUTH_CODE")
	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	@Column(value = "VIEW_FLAG", jdbcType = JdbcType.BOOLEAN)
	public boolean isViewFlag() {
		return viewFlag;
	}

	public void setViewFlag(boolean viewFlag) {
		this.viewFlag = viewFlag;
	}

	@Override
	@Exclude
	public String getUpdateBy() {
		return super.getUpdateBy();
	}

	@Override
	@Exclude
	public Date getUpdateTime() {
		return super.getUpdateTime();
	}

	@Override
	@Exclude
	public String getCode() {
		return super.getCode();
	}

	@Column(value="MODEL_ID", isUpdate=false)
    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
}
