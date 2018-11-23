package com.netposa.common.inject;

import android.view.View;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yexiaokang on 2017/11/3.
 */

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView {

    /**
     * inject view id
     *
     * @return view id
     */
    int id() default View.NO_ID;

    /**
     * inject view id as a string
     *
     * @return a string view id
     */
    String idString() default "";
}
