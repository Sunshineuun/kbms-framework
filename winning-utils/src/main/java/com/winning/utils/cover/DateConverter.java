package com.winning.utils.cover;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.beanutils.Converter;

public class DateConverter implements Converter {
	public static final String JSON_DATE_TIME_FORMAT = "yyyyMMddHHmmss";

	@Override
	public Object convert(@SuppressWarnings("rawtypes") Class type, Object value) {
		return toDate(type, value);
	}

	public static Object toDate(Class<?> type, Object value) {
		if (value == null || "".equals(value)){
			return null;
		}
		try {
		    /* 将数据库查出的Timestamp类型转为string */ 
			if (value instanceof java.sql.Timestamp && type.equals(String.class)) {
				DateFormat formatter = null;
				formatter = new SimpleDateFormat(JSON_DATE_TIME_FORMAT,
						new DateFormatSymbols(Locale.CHINA));
				return formatter.format(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return value;
	}
}
