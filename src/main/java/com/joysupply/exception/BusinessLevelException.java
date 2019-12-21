package com.joysupply.exception;

/**
 * 
 * <p>
 * <b>业务层异常处理类<b>
 */
public class BusinessLevelException extends SystemLevelException {
	private static final long serialVersionUID = 7823257970689657520L;

	public BusinessLevelException(String message, Throwable cause) {
		super(message, cause);
		this.recordException("业务层");
	}

	public BusinessLevelException(String message) {
		super(message);
		super.recordException("业务层");
	}

	@Override
	public Throwable initCause(Throwable cause) {
		return super.initCause(cause);
	}

	@Override
	protected void recordException(String level) {
		super.recordException(level);
	}

	@Override
	public String getLocalizedMessage() {
		return super.getLocalizedMessage();
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

}
