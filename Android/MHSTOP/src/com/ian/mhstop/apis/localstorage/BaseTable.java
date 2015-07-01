package com.ian.mhstop.apis.localstorage;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

/**
 * @version <1.0>
 * @Company <Ji'nan Ian-soft Tech. Co., Ltd>
 * @Project <MHSTOP>
 * @Author <Ryze>
 * @Date <2014-6-12>
 * @description <TODO>
 */

public class BaseTable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7823991427310358982L;
	// 自增长的ID
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private Long updateTime;// 上次更新的时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
