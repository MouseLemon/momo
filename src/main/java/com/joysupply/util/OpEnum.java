package com.joysupply.util;

/**
 * 操作结果枚举
 * @author hugh
 *
 */
public enum OpEnum {
	SUCCESS("OK"),FAILURE("FAILURE");
	private String value;
	private OpEnum(String value){
		this.value=value;
	}

    public String getValue() { 
     return value;
    }
}
