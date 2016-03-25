package com.starunion.java.fsccserver.dao.freecc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.starunion.java.fsccserver.dao.DbUtilTemplate;
import com.starunion.java.fsccserver.po.freepbx.UserSip;
import com.starunion.java.fsccserver.util.ConstantSystem;

/**
 * @author Lings
 * @date Mar 14, 2016 6:00:25 PM
 * 
 */
@Repository
public class DaoStarAddrBook extends DbUtilTemplate {

	// public List<DaoStarAddrBook> findAll() {
	// List<DaoStarAddrBook> list = new ArrayList<DaoStarAddrBook>();
	// list = findList(DaoStarAddrBook.class, "select * from sip_users order by
	// id");
	// return list;
	// }
	@Autowired
	DataSource dsStarCc;

	public int add() {
		String sql = "insert into addr_book (workId,name,workPost,workBranch,mobile,exten) values (?,?,?,?,?,?)";
		Object[] params = { 1, "凌松", "研发工程师", "研发部", "13811505291", "809" };
		return update(dsStarCc, sql, params);
	}

}
