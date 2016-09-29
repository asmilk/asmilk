package net.mybluemix.asmilk.data;

import java.io.Serializable;

import com.google.api.client.util.Key;

public class Message implements Serializable {

	private static final long serialVersionUID = 4482259175629436090L;

	@Key("Type")
	private Integer type;
	
	@Key("Content")
	private String content;
	
	@Key("FromUserName")
	private String fromUserName;
	
	@Key("ToUserName")
	private String toUserName;
	
	@Key("LocalID")
	private Long localID;
	
	@Key("ClientMsgId")
	private Long clientMsgId;
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public Long getLocalID() {
		return localID;
	}

	public void setLocalID(Long localID) {
		this.localID = localID;
	}

	public Long getClientMsgId() {
		return clientMsgId;
	}

	public void setClientMsgId(Long clientMsgId) {
		this.clientMsgId = clientMsgId;
	}

	@Override
	public String toString() {
		return "Message [type=" + type + ", content=" + content + ", fromUserName=" + fromUserName + ", toUserName="
				+ toUserName + ", localID=" + localID + ", clientMsgId=" + clientMsgId + "]";
	}
	
}
