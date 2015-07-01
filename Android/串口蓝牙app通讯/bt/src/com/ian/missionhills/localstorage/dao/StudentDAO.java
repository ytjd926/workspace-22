/**
 * Date:2013-10-9
 * Author:Ryze
 * todo: ormlite studentDAO
 */
package com.ian.missionhills.localstorage.dao;

import java.sql.SQLException;

import com.ian.missionhills.localstorage.model.Student;
import com.j256.ormlite.stmt.QueryBuilder;

public class StudentDAO extends DAO<Student> {
	private static String tag = "com.ian.missionhills.localstorage.dao.StudentDAO";
	public static StudentDAO studentDAO;
	QueryBuilder<Student, String> qb;

	public StudentDAO() {
        init();
	}

	public static final StudentDAO getInstance() {
		if (studentDAO == null) {
			studentDAO = new StudentDAO();
		}
		return studentDAO;
	}

    @Override
    public void init() {
        try {
            dao = dbHelper.getDao(Student.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
