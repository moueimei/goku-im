package com.goku.im.net.web.server.annotation;

import java.lang.annotation.*;

/**
 * @author moueimei
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    public String url() default "";
}