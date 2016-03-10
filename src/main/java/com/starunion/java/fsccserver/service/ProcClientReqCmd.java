package com.starunion.java.fsccserver.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.beginning.FsCcServer;
import com.starunion.java.fsccserver.thread.FsCmdRequestCallable;
import com.starunion.java.fsccserver.util.ClientDataMap;
import com.starunion.java.fsccserver.util.ConstantCc;

/**
 * @author Lings
 * @date Mar 4, 2016 5:38:54 PM
 * 
 */
@Service
public class ProcClientReqCmd {
	private static final Logger logger = LoggerFactory.getLogger(ProcClientReqCmd.class);

	@Autowired
	FsCmdRequestCallable task;

	public ProcClientReqCmd() {

	}

	public int procCmdCTD1(String caller, String callee) {
		StringBuffer buff = new StringBuffer();
		buff.append("111bgapi originate {origination_caller_id_number=000}user/");
		buff.append(caller);
		buff.append(" &bridge(user/");
		buff.append(callee);
		buff.append(")");
		buff.append(ConstantCc.DISP_FSCMD_TAIL);
		// fsSocket.fsSendCommand(buff.toString());
		logger.debug("make command : {}", buff.toString());

		// ExecutorService executor = Executors.newCachedThreadPool();
		// FsCmdRequestCallable task = new FsCmdRequestCallable();
		task.setSendCmd(buff.toString());
		// Future<String> result = executor.submit(task);
		Future<String> result = FsCcServer.executor.submit(task);
		// executor.shutdown();

		logger.debug("主线程在执行任务1111");

		try {
			logger.debug("task运行结果1111" + result.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		// ClientDataMap.fsCmdRequestQueue.add(buff.toString()) ;
		// ClientDataMap.fsCmdRequestQueue.add(buff.toString());
		// int status = 0;
		// while(status == 0){
		// try {
		// ClientDataMap.fsCmdResponseQueue.wait();
		// logger.debug("receive response... ");
		// logger.debug("receive response {}
		// ",ClientDataMap.fsCmdResponseQueue.peek());
		// status = 1;
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		logger.debug("haha ... i jump out the while...");

		return 0;
	}

	public int procCmdCTD(String caller, String callee) {
		StringBuffer buff = new StringBuffer();
		buff.append("bgapi originate {origination_caller_id_number=000}user/");
		buff.append(caller);
		buff.append(" &bridge(user/");
		buff.append(callee);
		buff.append(")");
		buff.append(ConstantCc.DISP_FSCMD_TAIL);
		// fsSocket.fsSendCommand(buff.toString());
		logger.debug("make command : {}", buff.toString());

		// ExecutorService executor = Executors.newCachedThreadPool();
		// FsCmdRequestCallable task = new FsCmdRequestCallable();
		task.setSendCmd(buff.toString());
		// Future<String> result = executor.submit(task);
		Future<String> result = FsCcServer.executor.submit(task);
		// executor.shutdown();

		logger.debug("主线程在执行任务");

		try {
			logger.debug("task运行结果" + result.get(5000, TimeUnit.MILLISECONDS));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ClientDataMap.fsCmdRequestQueue.add(buff.toString()) ;
		// ClientDataMap.fsCmdRequestQueue.add(buff.toString());
		// int status = 0;
		// while(status == 0){
		// try {
		// ClientDataMap.fsCmdResponseQueue.wait();
		// logger.debug("receive response... ");
		// logger.debug("receive response {}
		// ",ClientDataMap.fsCmdResponseQueue.peek());
		// status = 1;
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		logger.debug("haha ... i jump out the while...");

		return 0;
	}
}
