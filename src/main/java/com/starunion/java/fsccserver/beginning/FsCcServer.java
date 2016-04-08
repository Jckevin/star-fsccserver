package com.starunion.java.fsccserver.beginning;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.starunion.java.fsccserver.thread.SocketServerTcpJsonThread;
import com.starunion.java.fsccserver.thread.SocketServerTcpThread;
import com.starunion.java.fsccserver.util.StarConfigManager;
import com.starunion.java.fsccserver.thread.fs.SocketFsTcpThread;
import com.starunion.java.fsccserver.thread.fs.ThreadFsNotifyProc;
import com.starunion.java.fsccserver.thread.fs.ThreadNotifySendToClient;
import com.starunion.java.fsccserver.service.InitialService;

/**
 * This project used as an important middler server who connects both the
 * clients and the FreeSWITCH server. The clients can be choose normally and
 * traditional technology like Java(AWT), MFC(VC++) etc.But more excitedly, i
 * wanted choose the WEB systems, for its distribution and convenient. However,
 * web technology is still not mature enough at video (de)encode at the
 * moment,but i am sure web ones have a brightly and great future.
 * 
 * @date 2016-03-01
 * @version V0.0.1
 * @author LingSong
 * 
 */

public class FsCcServer {
	private static final Logger logger = LoggerFactory.getLogger(FsCcServer.class);

	public static ExecutorService executor = Executors.newCachedThreadPool();

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		LoggerContext logContext = (LoggerContext) LogManager.getContext(false);
		File conFile = new File("conf/log4j2.xml");
		logContext.setConfigLocation(conFile.toURI());
		logContext.reconfigure();

		@SuppressWarnings("resource")
		AbstractApplicationContext applicationContext = new FileSystemXmlApplicationContext("conf/appContext.xml");

		/** print version information. */
		logger.debug("FsCcServer Version :V0.0.1@20160301");

		/** initial Local configuration. */
		StarConfigManager configManager = applicationContext.getBean("starConfigManager", StarConfigManager.class);
		configManager.getInstance().setConfigurationPath("conf/appParams.conf");

		/** start TCP socket(server) for clients. */
		Thread serverJsonThread = new Thread(
				applicationContext.getBean("socketServerTcpJsonThread", SocketServerTcpJsonThread.class));
		serverJsonThread.setName("CcServerJsonThread");
		serverJsonThread.start();

		Thread serverThread = new Thread(
				applicationContext.getBean("socketServerTcpThread", SocketServerTcpThread.class));
		serverThread.setName("CcServerThread");
		serverThread.start();

		/** start FS binding socket for subscribe. */
		Thread fsThread = new Thread(applicationContext.getBean("socketFsTcpThread", SocketFsTcpThread.class));
		fsThread.setName("FsSocketThread");
		fsThread.start();

		/** start FS Notify receive thread. */
		Thread fsNotifyThread = new Thread(applicationContext.getBean("threadFsNotifyProc", ThreadFsNotifyProc.class));
		fsNotifyThread.setName("FsNotifyRecvThread");
		fsNotifyThread.start();

		/** start FS Notify send thread. */
		Thread fsNotifySendThread = new Thread(
				applicationContext.getBean("threadNotifySendToClient", ThreadNotifySendToClient.class));
		fsNotifySendThread.setName("FsNotifySendThread");
		fsNotifySendThread.start();

		/** initial server terminal info */
		if (SocketFsTcpThread.isFsConnected) {
			InitialService initServ = applicationContext.getBean("initialService", InitialService.class);
			initServ.initTerInfo();
		} else {
			// use a timer seems not bad.
			try {
				Thread.sleep(10000);
				InitialService initServ = applicationContext.getBean("initialService", InitialService.class);
				initServ.initTerInfo();
				logger.debug("OK");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
