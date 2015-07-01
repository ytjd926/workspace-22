package com.ian.missionhills.localstorage.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class BaseTable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7168578254865988697L;
	// 自增长的ID
	@DatabaseField(generatedId = true)
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
