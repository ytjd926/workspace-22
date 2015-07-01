/**
 * Date:2013-10-9
 * Author:Ryze
 * todo: ormlite studentDAO
 */
package com.ian.missionhills.localstorage.dao;

import java.sql.SQLException;

import com.ian.missionhills.localstorage.model.YuLiang;
import com.j256.ormlite.stmt.QueryBuilder;

public class YuLiangDAO extends DAO<YuLiang> {
	public static YuLiangDAO yuLiangDAO;
	QueryBuilder<YuLiang, String> qb;

	public YuLiangDAO() {
        init();
	}

	public static final YuLiangDAO getInstance() {
		if (yuLiangDAO == null) {
			yuLiangDAO = new YuLiangDAO();
		}
		return yuLiangDAO;
	}

    @Override
    public void init() {
        try {
            dao = dbHelper.getDao(YuLiang.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
