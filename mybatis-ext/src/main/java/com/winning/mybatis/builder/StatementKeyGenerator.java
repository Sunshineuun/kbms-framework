package com.winning.mybatis.builder;

import com.winning.annotations.mybatis.Table;

public class StatementKeyGenerator {

	public static String generateSelectStatementKey(Class<?> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		if (table == null)
			return null;
		return table.resultMapId() + "Select";
	}
}
