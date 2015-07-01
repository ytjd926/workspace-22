package com.ian.mhstop.apis.localstorage;

import com.j256.ormlite.stmt.QueryBuilder;

/**
 * Description:
 * 
 * @date 2013-12-18
 * @author Ryze
 * @version 1.0
 */
public class MainBannerDAO extends BaseDAO<MainBannerTable> {
	public static MainBannerDAO dao;
	QueryBuilder<MainBannerTable, String> qb;

	public MainBannerDAO() {
		getDao(MainBannerTable.class);
	}

	public static final MainBannerDAO getInstance() {
		if (dao == null) {
			dao = new MainBannerDAO();
		}
		return dao;
	}
}
