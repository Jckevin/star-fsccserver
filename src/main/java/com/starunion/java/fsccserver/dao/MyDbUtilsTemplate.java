package com.starunion.java.fsccserver.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.starunion.java.fsccserver.util.ConstantCc;

/**
 * @author Lings
 * @date Mar 14, 2016 6:04:17 PM
 * 
 */
@Repository
public class MyDbUtilsTemplate {
	@Autowired
	DataSource dataSource;
	private QueryRunner queryRunner;
	private final static int DB_SUCC = 1;
	private static final Logger logger = LoggerFactory.getLogger(MyDbUtilsTemplate.class);

	public MyDbUtilsTemplate() {

	}

	/**
	 * mostly used method without params.
	 * 
	 * @param sql
	 *            statement for operation [insert,delete,update]
	 * @return need a convert 1->0
	 */
	public int update(String sql) {
		queryRunner = new QueryRunner(dataSource);
		try {
			if (queryRunner.update(sql) == DB_SUCC) {
				return ConstantCc.SUCCESS;
			}
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return ConstantCc.FAILED;
	}

	public int update(DataSource ds, String sql) {
		queryRunner = new QueryRunner(dataSource);
		try {
			if (queryRunner.update(ds.getConnection(), sql) == DB_SUCC) {
				return ConstantCc.SUCCESS;
			}
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return ConstantCc.FAILED;
	}

	/**
	 * mostly used method with params.
	 * 
	 * @param sql
	 *            statement for operation [insert,delete,update]
	 * @return need a convert 1->0
	 */
	public int update(String sql, Object[] params) {
		queryRunner = new QueryRunner(dataSource);
		try {
			if (queryRunner.update(sql, params) == DB_SUCC) {
				return ConstantCc.SUCCESS;
			}
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return ConstantCc.FAILED;
	}

	public int update(DataSource ds, String sql, Object[] params) {
		queryRunner = new QueryRunner(dataSource);
		try {
			if (queryRunner.update(ds.getConnection(), sql, params) == DB_SUCC) {
				return ConstantCc.SUCCESS;
			}
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return ConstantCc.FAILED;
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
	public <T> T findFirst(String sql, Class<T> entityClass) {
		queryRunner = new QueryRunner(dataSource);
		try {
			return (T) queryRunner.query(sql, new BeanHandler(entityClass));
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T findFirst(DataSource ds, String sql, Class<T> entityClass) {
		queryRunner = new QueryRunner(dataSource);
		try {
			return (T) queryRunner.query(ds.getConnection(), sql, new BeanHandler(entityClass));
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
	public <T> T findFirst(String sql, Class<T> entityClass, Object[] params) {
		queryRunner = new QueryRunner(dataSource);
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
	public <T> List<T> findList(String sql, Class<T> entityClass) {
		queryRunner = new QueryRunner(dataSource);
		try {
			return queryRunner.query(sql, new BeanListHandler(entityClass));
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> findList(DataSource ds, String sql, Class<T> entityClass) {
		queryRunner = new QueryRunner(dataSource);
		try {
			return queryRunner.query(ds.getConnection(), sql, new BeanListHandler(entityClass));
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
	public <T> List<T> findList(String sql, Class<T> entityClass, Object[] params) {
		queryRunner = new QueryRunner(dataSource);
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
	public int[] batch(String sql, Object[][] params) {
		queryRunner = new QueryRunner(dataSource);
		try {
			return queryRunner.batch(sql, params);
		} catch (SQLException e) {
			logger.error("Error occured while attempting to update data", e);
		}
		return null;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
