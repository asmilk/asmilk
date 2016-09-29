package net.mybluemix.asmilk.data;

import java.io.Serializable;

import com.google.api.client.util.Key;

public class BaseRequest implements Serializable {

	private static final long serialVersionUID = -2551679063062031747L;

	@Key("Sid")
	private String sid;

	@Key("Uin")
	private Long uin;	
	
	@Key("Skey")
	private String skey;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Long getUin() {
		return uin;
	}

	public void setUin(Long uin) {
		this.uin = uin;
	}

	public String getSkey() {
		return skey;
	}

	public void setSkey(String skey) {
		this.skey = skey;
	}

	@Override
	public String toString() {
		return "BaseRequest [sid=" + sid + ", uin=" + uin + ", skey=" + skey + "]";
	}
	
}
