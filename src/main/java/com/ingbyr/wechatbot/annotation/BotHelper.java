package com.ingbyr.wechatbot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 17-2-13.
 *
 * @author ing
 * @version 1
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BotHelper {
    String value() default "default helper";
}
