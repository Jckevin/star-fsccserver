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

import com.starunion.java.fsccserver.service.InitTerStatus;
import com.starunion.java.fsccserver.thread.TcpServerSocketThread;
import com.starunion.java.fsccserver.util.ConfigManager;
import com.starunion.java.fsccserver.thread.FsTcpSocket;
import com.starunion.java.fsccserver.thread.TcpClientRequestRunnable;
import com.starunion.java.fsccserver.thread.TcpClientRequestThread;
import com.starunion.java.fsccserver.thread.TcpClientResponseThread;

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
		ConfigManager configManager = applicationContext.getBean("configManager", ConfigManager.class);
		configManager.getInstance().setConfigurationPath("conf/appParams.conf");

		/** initial server static data structure. */
		// InitTerStatus initTerStatus = applicationContext.getBean(
		// "initTerStatus", InitTerStatus.class);
		// initTerStatus.initSipUserInfo(ConfigManager.getInstance().getFsAddr());

		/** start TCP socket(server) for clients. */
		// Thread serverThread = new Thread(applicationContext.getBean(
		// "tcpServerSocketThread", TcpServerSocketThread.class));
		// TcpServerSocketThread serverThread =
		// applicationContext.getBean("tcpServerSocketThread",
		// TcpServerSocketThread.class);
		// TcpServerSocketThread serverThread = new
		// TcpServerSocketThread("CcServerTcpThread");
		Thread serverThread = new Thread(
				applicationContext.getBean("tcpServerSocketThread", TcpServerSocketThread.class));
		serverThread.start();

		/** static request message Queue Thread start */
		// TcpClientRequestThread clientReqThread = new
		// TcpClientRequestThread("CcClientReqThread");
		// clientReqThread.start();
		// TcpClientRequestThread clientReqThread1 = new
		// TcpClientRequestThread("CcClientReqThread-1");
		// clientReqThread1.start();
		/** Thread pool, not clearly enough */
		// ExecutorService executorService = Executors.newCachedThreadPool();
		// ExecutorService executorService = Executors.newFixedThreadPool(4);
		// executorService.execute(new TcpClientRequestRunnable("pool-1"));
		// executorService.execute(new TcpClientRequestRunnable("pool-2"));

		// TcpClientResponseThread clientRspThread = new
		// TcpClientResponseThread("CcClientRspThread");
		// clientRspThread.start();
		/** start TCP socket(client) for FreeSWITCH. */
		// Thread fsThread = new
		// Thread(applicationContext.getBean("fsTcpSocket",
		// FsTcpSocket.class));
		// fsThread.start();
	}
}
