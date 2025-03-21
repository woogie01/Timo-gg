package com.tools.seoultech.timoproject.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SocketMapping {

    String endpoint();

    Class<?> requestCls() default Void.class;
}
