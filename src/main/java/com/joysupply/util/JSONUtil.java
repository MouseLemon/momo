package com.joysupply.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * JSON
 * @author Administrator
 *
 */
public class JSONUtil {
	private static ObjectMapper objectMapper;
	static {
		objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
	}

	/**
	 * 将JSON结构体转换为entity
	 * 
	 * @param jsonObject
	 * @param t
	 * @return
	 */
	public static <T> T toEntity(Object jsonObject, Class<T> t) {
		try {

			String value = objectMapper.writeValueAsString(jsonObject);
			return objectMapper.readValue(value, t);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将entity转换JSON格式字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String toJsonString(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 将JSON格式字符串转换为JSON对象
	 * @param jsonObjectString
	 * @return
	 */
	public static JSONObject toJsonObject(String jsonObjectString) {
		return JSONObject.parseObject(jsonObjectString);
	}
	
	/**
	 * 对对象转换为JSON对象
	 * @param object
	 * @return
	 */
	public static JSONObject toJsonObject(Object object) {
		return (JSONObject) JSON.toJSON(object);
	}
}
