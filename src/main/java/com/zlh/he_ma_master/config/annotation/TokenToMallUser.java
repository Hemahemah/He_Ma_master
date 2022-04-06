package com.zlh.he_ma_master.config.annotation;

import java.lang.annotation.*;

/**
 * @author lh
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenToMallUser {

    String value() default "user";
}
