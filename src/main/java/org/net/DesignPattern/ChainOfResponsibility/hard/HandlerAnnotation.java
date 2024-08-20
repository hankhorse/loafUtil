package org.net.DesignPattern.ChainOfResponsibility.hard;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
*  责任链 头部注解
**/
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerAnnotation {
    int offset() default 0;
}