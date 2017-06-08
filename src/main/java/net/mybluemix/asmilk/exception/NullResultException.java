package net.mybluemix.asmilk.exception;

import org.springframework.validation.BindingResult;

public class NullResultException extends RuntimeException {
	
	private static final long serialVersionUID = -4725478165649216812L;
	
	private final BindingResult bindingResult;

	public NullResultException(String message, BindingResult result) {
		super(message);
		this.bindingResult = result;
	}
	
	public BindingResult getBindingResult() {
		return bindingResult;
	}

}
