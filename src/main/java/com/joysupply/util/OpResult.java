package com.joysupply.util;

/**
 * 操作结果
 * @author hugh
 *
 */
public class OpResult {
	private String result;
	private String message;

	public OpResult() {
		this.result = OpEnum.SUCCESS.getValue();
		this.message = "";
	}

	public OpResult(String message) {
		this.result = OpEnum.FAILURE.getValue();
		this.message = message;
	}

	public final String getResult() {
		return result;
	} 
	public final String getMessage() {
		return message;
	}

	public final void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "OpResult [result=" + result + ", message=" + message + "]";
	}

}
