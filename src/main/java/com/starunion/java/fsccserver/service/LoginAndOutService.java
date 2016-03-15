package com.starunion.java.fsccserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.dao.freecc.DaoAgentInfo;
import com.starunion.java.fsccserver.po.freecc.AgentInfo;
import com.starunion.java.fsccserver.util.ConstantCc;

/**
 * @author Lings
 * @date Mar 3, 2016 5:21:22 PM
 * 
 */

@Service
public class LoginAndOutService {
	@Autowired
	DaoAgentInfo daoAgentInfo;

	public int AgentLogin(String id, String pwd) {
		AgentInfo agt = daoAgentInfo.findBy(id);
		if (agt != null && agt.getAgentPwd().equals(pwd)) {
			return ConstantCc.SUCCESS;
		}
		return ConstantCc.FAILED;
	}
}
