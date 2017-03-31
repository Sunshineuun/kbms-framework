package com.winning.kbms.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.winning.annotations.enums.DictType;

@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.METHOD)
@Inherited
public @interface Dictionary
{
    String value();

    DictType dictType() default DictType.DICT_TYPE_CODE;
}
