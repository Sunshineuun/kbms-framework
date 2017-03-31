package com.winning.kbms.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.winning.kbms.core.commons.LogMsgHandler;
import com.winning.kbms.core.enums.OperLogType;
import com.winning.kbms.core.enums.RunTime;

@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.METHOD)
@Inherited
public @interface Log
{
    OperLogType value();

    String msg() default "";

    Class <? extends LogMsgHandler> handleClass();

    RunTime runTime() default RunTime.ASYNC;
}
