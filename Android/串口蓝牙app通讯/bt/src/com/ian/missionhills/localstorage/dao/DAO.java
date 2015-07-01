package com.ian.missionhills.localstorage.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import android.database.sqlite.SQLiteDatabase;

import com.ian.missionhills.localstorage.model.BaseTable;
import com.ian.missionhills.localstorage.model.DBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public abstract class DAO<T extends BaseTable> {

	private static String tag = "com.ian.missionhills.localstorage.dao.DAO";
	public DBHelper dbHelper = DBHelper.getInstance();
	private QueryBuilder<T, String> qb;
	public Dao<T, String> dao;
	SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();

	/**
	 * 初始化 dao
	 */
	public abstract void init();

	public void close() {
		dao = null;
	}

	/**
	 * 查询表中第一条数据
	 * 
	 * @return 返回 表对象
	 */
	public T queryForFirst() {
		try {
			init();
			qb = dao.queryBuilder();
			return qb.queryForFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	/**
	 * 查询表中所有数据
	 * 
	 * @return 返回 表对象 的集合
	 */
	public List<T> queryAll() {
		try {
			init();
			qb = dao.queryBuilder().limit(10);
			return qb.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	/**
	 * 查询表中所有数据
	 * 
	 * @return 返回 表对象 的集合
	 */
	public List<T> queryAllAscOrder(String orderParam) {
		try {
			init();
			qb = dao.queryBuilder().orderBy(orderParam, true);
			return qb.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}
	/**
	 * 查询表中所有数据
	 * 
	 * @return 返回 表对象 的集合
	 */
	public List<T> queryAllAscOrderMore(String orderParam,String orderParam2,String floor) {
		try {
			init();
			qb = dao.queryBuilder().orderBy(orderParam, true).orderBy(orderParam2,true);
			if(!"".equals(floor))
			{
				qb.where().eq("floorNo",floor);
			}
			return qb.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}
	/**
	 * 排序查询 排序字段orderParam， 根据arg参数查询
	 * 
	 * @param orderParam
	 * @param AscTrue
	 *            是否升序排列boolean:AscTrue true升序排列
	 * @param arg
	 *            查询条件
	 * @return List<T> 2013-10-31 jose
	 */

	public List<T> queryByParamAscOrder(String orderParam, boolean AscTrue,
			Object... arg) {
		try {
			init();
			qb = dao.queryBuilder().orderBy(orderParam, AscTrue);
			if (arg != null) {// 参数不为NULL;不为“”
				if (arg.length % 2 != 0)
					throw new IllegalArgumentException();
				for (int i = 0, lenth = arg.length / 2; i < lenth; ++i) {
					qb.where().in((String) arg[2 * i], arg[2 * i + 1]);
				}
				// qb.distinct();
			}
			return qb.query();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}
	public List<T> queryByParamAscOrderBanner(String orderParam, boolean AscTrue,
			Object... arg) {
		try {
			init();
			qb = dao.queryBuilder().orderBy(orderParam, AscTrue);
			Where<T, String> where = qb.where();
			if (arg != null) {// 参数不为NULL;不为“”
				if (arg.length % 2 != 0)
					throw new IllegalArgumentException();
				where.eq((String) arg[0], arg[1]);
				for (int i = 1, lenth = arg.length / 2; i < lenth; ++i) {
					where.and();
					where.in((String) arg[2 * i], arg[2 * i + 1]);
					
				}
				// qb.distinct();
			}
			return qb.query();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}
	
	/**
	 * 排序查询 排序字段orderParam， 根据arg参数查询
	 * 
	 * @param orderParam
	 * @param AscTrue
	 *            是否升序排列boolean:AscTrue true升序排列
	 * @param arg
	 *            查询条件
	 * @return List<T> 2013-10-31 jose
	 */

	public List<T> queryByParamAscOrder(String orderParam,String orderParam2, boolean AscTrue,
			Object... arg) {
		try {
			init();
			qb = dao.queryBuilder().orderBy(orderParam, AscTrue).orderBy(orderParam2, AscTrue);
			if (arg != null) {// 参数不为NULL;不为“”
				if (arg.length % 2 != 0)
					throw new IllegalArgumentException();
				for (int i = 0, lenth = arg.length / 2; i < lenth; ++i) {
					qb.where().in((String) arg[2 * i], arg[2 * i + 1]);
				}
				// qb.distinct();
			}
			return qb.query();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}
	/**
	 * 根据T的ID查询出 表对象
	 * 
	 * @param id
	 * @return 返回 表对象
	 */
	public T queryById(int id) {
		try {
			init();
			qb = dao.queryBuilder();
			qb.where().in("id", id);
			return qb.queryForFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	/**
	 * 条件查询
	 * 
	 * @param arg
	 *            必须是key（表中字段） value（值）的形式
	 * @return 返回T的集合
	 */
	public List<T> queryByParam(Object... arg) {
		if (arg == null) {
			throw new IllegalArgumentException();
		}
		try {
			init();
			qb = dao.queryBuilder();
			Where<T, String> where = qb.where();
			if (arg.length % 2 != 0)
				throw new IllegalArgumentException();
			for (int i = 0, lenth = arg.length / 2; i < lenth; ++i) {

				where.in((String) arg[2 * i], arg[2 * i + 1]);
				where.or();
			}
			return qb.query();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			close();
		}
		return null;

	}

	/**
	 * 商铺排序条件查询
	 * 
	 * @param arg
	 *            必须是key（表中字段） value（值）的形式
	 *            true 表示升序排序
	 * @return 返回T的集合
	 */
	public List<T> queryByParam(String param, Boolean flg,Object... arg) {
		if (arg == null) {
			throw new IllegalArgumentException();
		}
		try {
			init();
			qb = dao.queryBuilder().orderBy(param, flg);
			Where<T, String> where = qb.where();
			if (arg.length % 2 != 0)
				throw new IllegalArgumentException();
			for (int i = 0, lenth = arg.length / 2; i < lenth; ++i) {

				where.in((String) arg[2 * i], arg[2 * i + 1]);
				where.or();
			}
			return qb.query();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			close();
		}
		return null;

	}
	
	
	/**
	 * and查询
	 * 
	 * @param arg
	 *            必须是key（表中字段） value（值）的形式
	 * @return List<T> 2013-12-17 jose
	 */
	public List<T> queryByParamAnd(Object... arg) {
		if (arg == null) {
			throw new IllegalArgumentException();
		}
		try {
			init();
			qb = dao.queryBuilder();
			Where<T, String> where = qb.where();
			if (arg.length % 2 != 0)
				throw new IllegalArgumentException();
			where.eq((String) arg[0], arg[1]);
			for (int i = 1, lenth = arg.length / 2; i < lenth; ++i) {
				where.and();
				where.eq((String) arg[2 * i], arg[2 * i + 1]);
			}
			return qb.query();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			close();
		}
		return null;

	}

	/**
	 * 保存或者更新数据库
	 * 
	 * @param t
	 *            传入表对象
	 * @return 正整数为成功 其余为失败
	 */
	public int saveOrUpdate(T t) {
		try {
			T qt = queryById(t.getId());
			init();
			if (qt != null) {
				return dao.update(t);
			} else {
				return dao.create(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			close();
		}
	}

	public int save(T t) {
		try {

			if (t != null) {
				init();
				return dao.create(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return 0;
	}

	public int update(T t) {
		try {
			if (t != null) {
				init();
				return dao.update(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			close();
		}
		return 0;
	}

	/**
	 * 删除一条数据
	 * 
	 * @param t
	 *            根据表对象
	 */
	public int delete(T t) {
		try {
			if (t != null) {
				init();
				return dao.delete(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return 0;
	}

	public void delete(String... arg) {
		if (arg == null) {
			throw new IllegalArgumentException();
		}

		try {
			List<T> listT = queryByParamAnd(arg);
			if (listT != null) {
				init();
				for (int i = 0; i < listT.size(); i++) {
					T t = listT.get(i);
					dao.delete(t);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public void deleteAll(List<T> list) {
		try {
			if (list != null) {
				init();
				dao.delete(list);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}

	}

/**
 * 事务
 * @param tran
 * 把DB操作放入call方法内
 */
	public void transaction(final Transaction tran) {
		try {
			TransactionManager.callInTransaction(
					dbHelper.getConnectionSource(), new Callable<Void>() {
						public Void call() throws Exception {
							if (tran != null) {
								tran.call();
							}
							return null;
						}
					});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public interface Transaction {
		public void call() throws Exception;
	}

}
