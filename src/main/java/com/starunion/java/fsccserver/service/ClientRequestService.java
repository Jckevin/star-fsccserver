package com.starunion.java.fsccserver.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starunion.java.fsccserver.dao.DaoIntercomGroup;
import com.starunion.java.fsccserver.dao.DaoIntercomMember;
import com.starunion.java.fsccserver.dao.DaoPrePlayInfo;
import com.starunion.java.fsccserver.dao.DaoUserGpsInfo;
import com.starunion.java.fsccserver.dao.DaoUserGpsTrail;
import com.starunion.java.fsccserver.dao.DaoUserSip;
import com.starunion.java.fsccserver.dao.DaoUserSipBlack;
import com.starunion.java.fsccserver.dao.DaoUserSipCamer;
import com.starunion.java.fsccserver.po.ClientRequestMessageCc;
import com.starunion.java.fsccserver.po.IntercomGroup;
import com.starunion.java.fsccserver.po.IntercomMember;
import com.starunion.java.fsccserver.po.PrePlayInfo;
import com.starunion.java.fsccserver.po.SipActiveInfo;
import com.starunion.java.fsccserver.po.UserGpsInfo;
import com.starunion.java.fsccserver.po.UserGpsTrail;
import com.starunion.java.fsccserver.po.UserSip;
import com.starunion.java.fsccserver.po.UserSipBlack;
import com.starunion.java.fsccserver.po.UserSipCamer;
import com.starunion.java.fsccserver.service.timer.QuartzTaskService;
import com.starunion.java.fsccserver.thread.FsTcpSocket;
import com.starunion.java.fsccserver.util.ConfigManager;
import com.starunion.java.fsccserver.util.ConstantCc;

/**
 * @author Lings
 * @date Mar 3, 2016 3:40:00 PM
 * 
 */

@Service
public class ClientRequestService {
	private static final Logger logger = LoggerFactory.getLogger(ClientRequestService.class);

	@Autowired
	MessageClientReqService reqMsgService;
	@Autowired
	ProcClientReqCmd reqMsgCmdService;
	@Autowired
	private DaoUserSip daoUserSip;
	@Autowired
	private DaoUserSipBlack daoUserSipBlack;
	@Autowired
	private DaoUserSipCamer daoUserSipCamer;
	@Autowired
	private DaoPrePlayInfo daoPrePlayInfo;
	@Autowired
	private DaoIntercomGroup daoIntercomGroup;
	@Autowired
	private DaoIntercomMember daoIntercomMember;
	@Autowired
	private DaoUserGpsInfo daoUserGpsInfo;
	@Autowired
	private DaoUserGpsTrail daoUserGpsTrail;
	@Autowired
	private InitTerStatus initTerStatus;
	@Autowired
	private QuartzTaskService timerTask;
	@Autowired
	private ProcLinuxCommand procLinuxCmd;

	public ClientRequestService() {

	}

	public StringBuffer procClientRequest(String reqLine) {
		StringBuffer rspBuff = new StringBuffer();

		ClientRequestMessageCc message = reqMsgService.parseRequestMessage(reqLine);
		if (message != null) {

			logger.debug("receive client request type [{}]", message.getType());
			switch (message.getType()) {
			case ConstantCc.CC_LOGIN:
				logger.debug("begin process login service");
				rspBuff = makeLastResponse(reqLine, ConstantCc.SUCCESS);
				break;
			case ConstantCc.CC_LOGOUT:
				rspBuff = makeLastResponse(reqLine, ConstantCc.FAILED);
				logger.debug("begin process log out service");
				break;
			case ConstantCc.CC_CTD:
				int res = reqMsgCmdService.procCmdCTD(message.getClientId(),message.getContent());
				if(res==0){
					rspBuff = makeLastResponse(reqLine, ConstantCc.SUCCESS);	
				}else{
					rspBuff = makeLastResponse(reqLine, ConstantCc.FAILED);
				}
				logger.debug("begin process the third party call service");
				break;
			default:
				logger.debug("begin process ctd service");
				reqMsgCmdService.procCmdCTD1(message.getClientId(),message.getContent());
				rspBuff = makeLastResponse(reqLine, ConstantCc.SUCCESS);
				break;
			}

			return rspBuff;
		} else {
			rspBuff = makeLastResponse(reqLine, ConstantCc.FAILED);
			return rspBuff;
		}

	}

	
	private int procTestService(String[] data) {
		// daoUserSip.batchInsertUser(data[1], data[2], data[3], data[4]);
		daoUserGpsTrail.batchInsertGps(data[1]);
		return 0;
	}

	private StringBuffer procReqDisUsersList(String[] data) {
		List<UserGpsInfo> gpsList = new ArrayList<UserGpsInfo>();
		gpsList = daoUserGpsInfo.findAll();
		StringBuffer buff = new StringBuffer();
		int len = gpsList.size();
		for (int i = 0; i < len; i++) {
			buff.append("extenmapdata:");
			buff.append(gpsList.get(i).getExten()).append(":");
			buff.append(gpsList.get(i).getLng()).append(":");
			buff.append(gpsList.get(i).getLat()).append(":");
			buff.append(gpsList.get(i).getType());
			buff.append("\r\n");
		}
		return buff;
	}

	private StringBuffer procExtenGpsShow(String[] data) {
		String name = data[1];
		UserGpsInfo gpsInfo = new UserGpsInfo();
		gpsInfo = daoUserGpsInfo.findByNumber(name);
		StringBuffer buff = new StringBuffer();
		buff.append(data[0]);
		buff.append(":");
		buff.append(gpsInfo.getExten()).append(":");
		buff.append(gpsInfo.getLng()).append(":");
		buff.append(gpsInfo.getLat()).append(":");
		buff.append(gpsInfo.getType());
		buff.append("\r\n");

		return buff;
	}

	private String getBroadPlayMusicName(String musics) {
		StringBuffer buff = new StringBuffer();
		String[] music = musics.split(",");
		int musicLen = music.length;
		int j = 0;
		for (j = 0; j < musicLen; j++) {
			if (j < musicLen) {
				buff.append(ConfigManager.getInstance().getDisMusicPath());
				buff.append(music[j]).append("!");
			} else {
				buff.append(ConfigManager.getInstance().getDisMusicPath()).append(music[j]);
			}
		}
		return buff.toString();
	}

	private int getBroadPlayMusicLen(String musics) {
		int filePlaySecondEstimate = 0;
		String[] music = musics.split(",");
		int musicLen = music.length;

		int j = 0;
		for (j = 0; j < musicLen; j++) {
			String fileName = ConfigManager.getInstance().getDisMusicPath() + music[j];
			File file = new File(fileName);
			logger.debug("get music file length {}", file.length());
			filePlaySecondEstimate += file.length() * 8 / 160;
		}

		return filePlaySecondEstimate;
	}

	private StringBuffer makeLastResponse(String buff, int result) {
		StringBuffer nBuff = new StringBuffer();
		nBuff.append(buff);
		if (result == ConstantCc.SUCCESS) {
			nBuff.append(ConstantCc.DISP_TAIL_SUCC);
		} else if (result == ConstantCc.FAILED) {
			nBuff.append(ConstantCc.DISP_TAIL_FAIL);
		}
		return nBuff;
	}

}
