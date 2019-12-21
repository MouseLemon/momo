package com.joysupply.util;

import java.security.MessageDigest;

public class StringUtil {

	/**
	 * 密码MD5加密 32位小写
	 */
	public static String MD5(String text) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes("UTF-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString().toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
