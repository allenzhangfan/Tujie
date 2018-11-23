package com.jess.arms.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by ygq on 2018/8/13.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeActivityLifecycleForRxLifecycle {
}
