package com.winning.domain.dm;

import com.winning.domain.BaseDomain;

/**   
 * @标题: ItemMessage.java 
 * @包名: com.winning.bi.domain 
 * @描述: Item Result
 * @作者: chenjie 
 * @时间: Apr 24, 2014 2:14:38 PM 
 * @版权: (c) 2014, 卫宁软件科技有限公司
 */
public class ItemMessage extends BaseDomain {

	/** 
	* @Fields serialVersionUID : TODO
	*/ 
	private static final long serialVersionUID = 1L;
	
	private Double sl;//数量
	private Double je;//金额
	private Double zxd;//置信度
	private Double xgd;//相关度
	private Double yc_je;//预测金额
	private Double yc_sl;//预测数量
	private String bz;
	private Double je_xs;//危险系数大于1都是危险的
	private Double sl_xs;
	/**
	 * @return the zxd
	 */
	public Double getZxd() {
		return zxd;
	}
	/**
	 * @param zxd the zxd to set
	 */
	public void setZxd(Double zxd) {
		this.zxd = zxd;
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
	 * @return the bz
	 */
	public String getBz() {
		return bz;
	}
	/**
	 * @param bz the bz to set
	 */
	public void setBz(String bz) {
		this.bz = bz;
	}
	/**
	 * @return the yc_je
	 */
	public Double getYc_je() {
		return yc_je;
	}
	/**
	 * @param yc_je the yc_je to set
	 */
	public void setYc_je(Double yc_je) {
		this.yc_je = yc_je;
	}
	/**
	 * @return the yc_sl
	 */
	public Double getYc_sl() {
		return yc_sl;
	}
	/**
	 * @param yc_sl the yc_sl to set
	 */
	public void setYc_sl(Double yc_sl) {
		this.yc_sl = yc_sl;
	}
	/**
	 * @return the je_xs
	 */
	public Double getJe_xs() {
		return je_xs;
	}
	/**
	 * @param je_xs the je_xs to set
	 */
	public void setJe_xs(Double je_xs) {
		this.je_xs = je_xs;
	}
	/**
	 * @return the sl_xs
	 */
	public Double getSl_xs() {
		return sl_xs;
	}
	/**
	 * @param sl_xs the sl_xs to set
	 */
	public void setSl_xs(Double sl_xs) {
		this.sl_xs = sl_xs;
	}
    public Double getXgd ()
    {
        return xgd;
    }
    public void setXgd (Double xgd)
    {
        this.xgd = xgd;
    }
}
