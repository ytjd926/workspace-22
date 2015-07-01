/**
 * Date:2013-10-9
 * Author:Ryze
 * todo: ormlite student��
 */
package com.ian.missionhills.localstorage.model;

import java.io.Serializable;
import java.util.Date;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

//tableName ����
@DatabaseTable(tableName = "yuliang")
public class YuLiang extends BaseTable implements Serializable {
	private static final long serialVersionUID = 8064644060790491360L;
	@DatabaseField
	private long zyl;
	@DatabaseField
	private long fsyl;
	@DatabaseField
	private String time;
	@DatabaseField 
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getFsyl() {
		return fsyl;
	}

	public void setFsyl(long fsyl) {
		this.fsyl = fsyl;
	}

	public String getTime() {
	Date pushTime = new Date();
		return DateUtil.getDateTimeStr(pushTime,"yyyy-MM-dd HH:mm");
	}

	public void setTime(String time) {
		this.time = time;
	}

	public long getZyl() {
		return zyl;
	}

	public void setZyl(long zyl) {
		this.zyl = zyl;
	}

}
