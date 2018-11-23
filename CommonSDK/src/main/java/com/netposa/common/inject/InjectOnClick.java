package com.netposa.common.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fine on 2017/11/4.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InjectOnClick {

    /**
     * inject view id
     *
     * @return view id
     */
    int[] ids() default {};

    /**
     * inject view id as a string
     *
     * @return a string view id
     */
    String[] idStrings() default {};
}
