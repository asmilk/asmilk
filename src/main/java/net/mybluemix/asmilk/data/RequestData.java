package net.mybluemix.asmilk.data;

import java.io.Serializable;

import com.google.api.client.util.Key;

import net.minidev.json.JSONObject;

public class RequestData implements Serializable {

	private static final long serialVersionUID = 8946200857282215591L;
	
	@Key("BaseRequest")
	private BaseRequest baseRequest;
	
	@Key("Code")
	private Integer code;

	@Key("FromUserName")
	private String fromUserName;
	
	@Key("ToUserName")
	private String toUserName;
	
	@Key("ClientMsgId")
	private Long clientMsgId;
	
	@Key("SyncKey")
	private JSONObject syncKey;
	
	@Key("Msg")
	private Message message;
	
	@Key("Scene")
	private Integer scene;

	public BaseRequest getBaseRequest() {
		return baseRequest;
	}

	public void setBaseRequest(BaseRequest baseRequest) {
		this.baseRequest = baseRequest;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
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

	public Long getClientMsgId() {
		return clientMsgId;
	}

	public void setClientMsgId(Long clientMsgId) {
		this.clientMsgId = clientMsgId;
	}

	public JSONObject getSyncKey() {
		return syncKey;
	}

	public void setSyncKey(JSONObject syncKey) {
		this.syncKey = syncKey;
	}

	public Integer getScene() {
		return scene;
	}

	public void setScene(Integer scene) {
		this.scene = scene;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "RequestData [baseRequest=" + baseRequest + ", code=" + code + ", fromUserName=" + fromUserName
				+ ", toUserName=" + toUserName + ", clientMsgId=" + clientMsgId + ", syncKey=" + syncKey + ", message="
				+ message + ", scene=" + scene + "]";
	}
	
}
