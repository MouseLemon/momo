package com.joysupply.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author hugh
 *         <p>
 * 		<b>系统异常基类<b>
 */
public abstract class SystemLevelException extends RuntimeException {
	protected Log log = LogFactory.getLog(this.getClass());
	private static final long serialVersionUID = 6356177895382840062L;
	protected Throwable cause;
	protected String message;
	protected String level = "";
	protected StringBuffer stringBuffer = null;

	public SystemLevelException(String message) {
		super(message);
		cause = super.getCause();

	}

	public SystemLevelException(String message, Throwable cause) {
		super(message);
		this.cause = cause;
	}

	public Throwable initCause(Throwable cause) {
		this.cause = cause;
		return cause;
	}

	/**
	 * 记录异常信息
	 * 
	 * @param level
	 */
	protected void recordException(String level) {

		stringBuffer = new StringBuffer();
		stringBuffer.append("系统 [" + level + "]异常信息 ： " + message + "\r\n");
		stringBuffer.append("异常消息开始....\r\n");
		stringBuffer.append("异常消息：" + this.getMessage() + "\r\n");
		stringBuffer.append("异常详情\r\n");
		StackTraceElement element = this.getStackTrace()[0];
		if (element == null) {
			stringBuffer.append("无法异常详情\r\n");
		} else {
			stringBuffer.append("类名:" + element.getClassName() + "\r\n");
			stringBuffer.append("文件名：" + element.getFileName() + "\r\n");
			stringBuffer.append("行号：" + element.getLineNumber() + "\r\n");
			stringBuffer.append("方法名：" + element.getMethodName() + "\r\n");
			stringBuffer.append("信息：" + element.toString() + "\r\n");
		}
		stringBuffer.append("异常消息结束.\r\n");
		log.debug(stringBuffer.toString());
	}

	@Override
	public String getLocalizedMessage() {
		return message == null ? stringBuffer.toString().trim() : message;
	}

	@Override
	public String getMessage() {
		return message == null ? stringBuffer.toString().trim() : message;
	}
}
