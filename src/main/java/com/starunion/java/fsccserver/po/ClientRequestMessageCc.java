package com.starunion.java.fsccserver.po;


/** 
* @author Lings  
* @date Mar 4, 2016 10:20:11 AM 
* 
*/
public class ClientRequestMessageCc {

	private String type;
	private String clientId;
	private String content;
	
	public ClientRequestMessageCc(){
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	
}
