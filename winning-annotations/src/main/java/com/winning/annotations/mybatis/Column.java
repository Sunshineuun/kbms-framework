package com.winning.annotations.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.type.JdbcType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Column {
	String value();

	JdbcType jdbcType() default JdbcType.VARCHAR;

	boolean isAdd() default true;

	boolean isUpdate() default true;

	String resultHandler() default "";
}
