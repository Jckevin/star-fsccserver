package com.starunion.java.fsccserver.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.starunion.java.fsccserver.util.ConstantSystem;

/**
 * @author Lings
 * @date Mar 14, 2016 6:04:17 PM
 * 
 */
@Repository
public class DbUtilTemplate {
	private QueryRunner queryRunner;
	private final static int DB_SUCC = 1;
	private static final Logger logger = LoggerFactory.getLogger(DbUtilTemplate.class);

	public DbUtilTemplate() {

	}

	/**
	 * mostly used method without params.
	 * 
	 * @param sql
	 *            statement for operation [insert,delete,update]
	 * @return need a convert 1->0
	 */

	public int update(DataSource ds, String sql) {
		queryRunner = new QueryRunner(ds);
		try {
			if (queryRunner.update(sql) == DB_SUCC) {
				return ConstantSystem.SUCCESS;
			}
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return ConstantSystem.FAILED;
	}

	/**
	 * mostly used method with params.
	 * 
	 * @param sql
	 *            statement for operation [insert,delete,update]
	 * @return need a convert 1->0
	 */

	public int update(DataSource ds, String sql, Object[] params) {
		queryRunner = new QueryRunner(ds);
		try {
			if (queryRunner.update(sql, params) == DB_SUCC) {
				return ConstantSystem.SUCCESS;
			}
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return ConstantSystem.FAILED;
	}

	/**
	 * method for query sigle object.
	 * 
	 * @param sql
	 *            statement for operation [query]
	 * @param entityClass
	 *            input a class template
	 * @return T class template result
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T findOne(DataSource ds, String sql, Class<T> entityClass) {
		queryRunner = new QueryRunner(ds);
		try {
			return (T) queryRunner.query(sql, new BeanHandler(entityClass));
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return null;
	}

	/**
	 * method for query sigle object with params.
	 * 
	 * @param sql
	 *            statement for operation [query]
	 * @param entityClass
	 *            input a class template
	 * @param params
	 *            for condition of the T
	 * 
	 * @return T class template result
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T findOne(DataSource ds, String sql, Class<T> entityClass, Object[] params) {
		queryRunner = new QueryRunner(ds);
		try {
			return (T) queryRunner.query(sql, new BeanHandler(entityClass), params);
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return null;
	}

	/**
	 * method for query a list of object.
	 * 
	 * @param sql
	 *            statement for operation [query]
	 * @param entityClass
	 *            input a class template
	 * @return T class template result
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> findList(DataSource ds, String sql, Class<T> entityClass) {
		queryRunner = new QueryRunner(ds);
		try {
			return queryRunner.query(sql, new BeanListHandler(entityClass));
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return null;
	}

	/**
	 * method for query a list of object with params.
	 * 
	 * @param sql
	 *            statement for operation [query]
	 * @param entityClass
	 *            input a class template
	 * @param params
	 *            extra params for query
	 * @return T class template result
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> findList(DataSource ds, String sql, Class<T> entityClass, Object[] params) {
		queryRunner = new QueryRunner(ds);
		try {
			return queryRunner.query(sql, new BeanListHandler(entityClass), params);
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return null;
	}

	/**
	 * method for batch operation with params.
	 * 
	 * @param sql
	 *            statement for operation [insert,delete,update]
	 * @param params
	 *            extra params for operate
	 * @return T class template result
	 */
	public int[] batch(DataSource ds, String sql, Object[][] params) {
		queryRunner = new QueryRunner(ds);
		try {
			return queryRunner.batch(sql, params);
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return null;
	}

	/**
	 * getCount need ScalarHandler return columns info
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getCount(DataSource ds, String sql) {
		logger.debug(sql);
		queryRunner = new QueryRunner(ds);
		try {
			return ((Long) queryRunner.query(sql, new ScalarHandler())).intValue();
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return ConstantSystem.FAILED;
	}

	/**
	 * @deprecated
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getCountByConn(DataSource ds, String sql) {
		queryRunner = new QueryRunner();
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return ((Long) queryRunner.query(conn, sql, new ScalarHandler())).intValue();
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		} finally {
			// if use conn, it must be close after invoke,or the conn will be
			// hold and never used by other invoke.
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return ConstantSystem.FAILED;
	}

}
