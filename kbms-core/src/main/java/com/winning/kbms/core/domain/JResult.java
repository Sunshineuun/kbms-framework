package com.winning.kbms.core.domain;

import com.winning.domain.BaseDomain;

/**
 * @标题: JResult.java
 * @包名: com.winning.kbms.core.doamin
 * @描述: TODO
 * @作者: gang.liu
 * @时间: Apr 4, 2014 2:28:55 PM
 * @版权: (c) 2014, 卫宁软件科技有限公司
 */
public class JResult extends BaseDomain {
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	public final static JResult TRUE_RESULT = new JResult(true, "");
	private boolean success;
	private Object result;

	public JResult(boolean success, Object result) {
		this.success = success;
		this.result = result;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
