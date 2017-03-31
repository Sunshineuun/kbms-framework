package com.winning.domain.dm;

/**   
 * @标题: ItemResult.java 
 * @包名: com.winning.bi.domain 
 * @描述: 明细项目结果
 * @作者: chenjie 
 * @时间: May 6, 2014 7:07:22 PM 
 * @版权: (c) 2014, 卫宁软件科技有限公司
 */
public class ItemResult {

	public static final String FLAG_XM="xm";//项目
	public static final String FLAG_TR="tr";//结算
	private String name;//项目名称
	private String code;//项目代码
	private Double je;//金额
	private Double sl;//数量
	private Double ts;
	private String flag;//项目类别
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the je
	 */
	public Double getJe() {
		return je;
	}
	/**
	 * @param je the je to set
	 */
	public void setJe(Double je) {
		this.je = je;
	}
	/**
	 * @return the sl
	 */
	public Double getSl() {
		return sl;
	}
	/**
	 * @param sl the sl to set
	 */
	public void setSl(Double sl) {
		this.sl = sl;
	}
	/**
	 * @return the ts
	 */
	public Double getTs() {
		return ts;
	}
	/**
	 * @param ts the ts to set
	 */
	public void setTs(Double ts) {
		this.ts = ts;
	}
	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
}
