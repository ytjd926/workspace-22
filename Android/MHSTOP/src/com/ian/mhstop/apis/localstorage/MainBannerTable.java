package com.ian.mhstop.apis.localstorage;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Description:
 * @date 2013-12-18
 * @author Ryze
 * @version 1.0
 */
@DatabaseTable(tableName = "MainBannerTable")
public class MainBannerTable  extends BaseTable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2463376835123267802L;
	@DatabaseField
	private String bid;//banner id
	@DatabaseField
	private String webURL;// 网络URL
	@DatabaseField
	private String detail;
	@DatabaseField
	private String bannerInType;//sale=优惠   event=活动
	@DatabaseField
	private int sort;//排序
	
	
	
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	/**
	 * @return the bannerInType
	 */
	public String getBannerInType() {
		return bannerInType;
	}
	/**
	 * @param bannerInType the bannerInType to set
	 */
	public void setBannerInType(String bannerInType) {
		this.bannerInType = bannerInType;
	}
	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}
	/**
	 * @param detail the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}
	/**
	 * @return the webURL
	 */
	public String getWebURL() {
		return webURL;
	}
	/**
	 * @param webURL the webURL to set
	 */
	public void setWebURL(String webURL) {
		this.webURL = webURL;
	}
	
	

}
