package net.mybluemix.asmilk.security;

import org.springframework.security.core.AuthenticationException;

public class CaptchaException extends AuthenticationException {

	private static final long serialVersionUID = -4421629487792226071L;

	public CaptchaException(String msg) {
		super(msg);
	}

}
