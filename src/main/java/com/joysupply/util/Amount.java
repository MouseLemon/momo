package com.joysupply.util;

import java.io.Serializable;
import java.math.BigDecimal;

public class Amount implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private BigDecimal value;
	/**
	 * 提供默认精度
	 * */
	private final static int scale = 100;
	/**
	 * double类型构造函数
	 * */
	public Amount(String value) {
		this.value = new BigDecimal(value);
	}
	public BigDecimal getValue() {
		return this.value;
	}
	
	/**
	 * 返回两个值相加
	 * */
	public static String add(String v1,String v2) {
		Amount a1 = new Amount(v1);
		Amount a2 = new Amount(v2);
		double value = a1.getValue().add(a2.getValue()).doubleValue();
		return result(value);
	}

	/**
	 * 返回两个数相减
	 * */
	public static String sub(String v1,String v2) {
		Amount a1 = new Amount(v1);
		Amount a2 = new Amount(v2);
		double value = a1.getValue().subtract(a2.getValue()).doubleValue();
		return result(value);
	}
	
	/**
	 * 返回两个数相乘
	 * */
	public static String mul(String v1,String v2) {
		Amount a1 = new Amount(v1);
		Amount a2 = new Amount(v2);
		double value = a1.getValue().multiply(a2.getValue()).doubleValue();
		return result(value);
	}
	
	/**
	 * 返回两个数相除
	 * */
	public static String div(String v1,String v2) {
		Amount a1 = new Amount(v1);
		Amount a2 = new Amount(v2);
		if (scale < 0) {
			throw new IllegalArgumentException("精度指定错误,请指定一个>=0的精度");
        }
		double value = a1.getValue().divide(a2.getValue(),scale,BigDecimal.ROUND_HALF_UP).doubleValue();
		return result(value);
	}
	private static String result(double value) {
		BigDecimal bg = new BigDecimal(value);
		bg.setScale(2, BigDecimal.ROUND_HALF_UP);
		return String.format("%.2f",bg.doubleValue());
	}
}
