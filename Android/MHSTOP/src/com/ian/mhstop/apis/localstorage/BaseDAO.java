package com.ian.mhstop.apis.localstorage;

import java.sql.SQLException;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

/**
 * @version <1.0>
 * @Company <Ji'nan Ian-soft Tech. Co., Ltd>
 * @Project <MHSTOP>
 * @Author <Ryze>
 * @Date <2014-6-12>
 * @description <TODO>
 */

public class BaseDAO<T extends BaseTable> {

    public BaseDBHelper dbHelper = BaseDBHelper.getInstance();
    private QueryBuilder<T, String> qb;
    public Dao<T, String> dao;
    SQLiteDatabase db = BaseDBHelper.getInstance().getReadableDatabase();

    protected void getDao(Class<?> c) {
        try {
            dao =(Dao) dbHelper.getDao(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询表中第一条数据
     *
     * @return 返回 表对象
     */
    public T queryForFirst() {
        try {
            qb = dao.queryBuilder();
            return qb.queryForFirst();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            qb = dao.queryBuilder();
            return qb.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            qb = dao.queryBuilder().orderBy(orderParam, true);
            return qb.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 排序查询 排序字段orderParam， 根据arg参数查询
     *
     * @param orderParam
     * @param upTrue
     *            是否升序排列boolean:AscTrue true升序排列
     * @param arg
     *            查询条件
     * @return List<T>
     */

    public List<T> queryByParamAscOrder(String orderParam, boolean AscTrue,
                                        Object... arg) {
        try {
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
            qb = dao.queryBuilder();
            qb.where().in("id", id);
            return qb.queryForFirst();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        }
        return null;

    }

    /**
     * and查询
     *
     * @param arg
     *            必须是key（表中字段） value（值）的形式
     * @return List<T>
     */
    public List<T> queryByParamAnd(Object... arg) {
        if (arg == null) {
            throw new IllegalArgumentException();
        }
        try {
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
            if (qt != null) {
                return dao.update(t);
            } else {
                return dao.create(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int save(T t) {
        try {

            if (t != null)
                return dao.create(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(T t) {
        try {
            if (t != null)
                return dao.update(t);
        } catch (SQLException e) {
            e.printStackTrace();

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
            return dao.delete(t);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void deleteAll(List<T> list) {
        try {
            if (list != null) {
                dao.delete(list);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 开启事务
     */
    public void beginTransaction() {
        db.beginTransaction();
    }

    /**
     * 结束事务
     */
    public void endTransaction() {
        db.endTransaction();
    }

    public void synchronization(String URL,String... params){

    }


}
