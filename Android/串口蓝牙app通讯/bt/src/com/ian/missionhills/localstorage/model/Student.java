/**
 * Date:2013-10-9
 * Author:Ryze
 * todo: ormlite student��
 */
package com.ian.missionhills.localstorage.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

//tableName ����
@DatabaseTable(tableName = "student")
public class Student extends BaseTable implements Serializable {
	private static final long serialVersionUID = 8064644060790491360L;
	@DatabaseField
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
