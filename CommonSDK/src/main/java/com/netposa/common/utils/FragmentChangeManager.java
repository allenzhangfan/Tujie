/*
 * Copyright (C) 2016 LingDaNet.Co.Ltd. All Rights Reserved.
 */
package com.netposa.common.utils;

import android.os.Bundle;

import java.util.HashMap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

/**
 * 作者：陈新明
 * 创建日期：2016/9/26
 * 邮箱：chenxm0902
 * 描述：fragment切换管理类
 */

public class FragmentChangeManager {

    private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
    private TabInfo mLastTab;
    private FragmentActivity mActivity;
    private int mContainerId;

    public FragmentChangeManager(FragmentActivity activity, int containerId) {
        this.mActivity = activity;
        this.mContainerId = containerId;
    }

    /**
     * discription:添加fragment类
     * @param tag Fragragment 的tag
     * @param clss  Fragment类
     * @param args 参数
     */
    public void addFragment(String tag, Class<?> clss, Bundle args) {

        TabInfo info = new TabInfo(tag, clss, args);

        // Check to see if we already have a fragment for this tab, probably
        // from a previously saved state. If so, deactivate it, because our
        // initial state is that a tab isn't shown.
        info.fragment = mActivity.getSupportFragmentManager()
                .findFragmentByTag(tag);
        if (info.fragment != null && !info.fragment.isDetached()) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager()
                    .beginTransaction();
            ft.detach(info.fragment);
            ft.commit();
        }

        mTabs.put(tag, info);
    }

    public Fragment getTagFragment(String tag) {
        Fragment fragmentByTag = mActivity.getSupportFragmentManager()
                .findFragmentByTag(tag);
        return fragmentByTag;
    }

    /**
     * discription： Fragment切换
     * @param tabId 存入Map里的key
     */
    public void onFragmentChanged(String tabId) {
        TabInfo newTab = mTabs.get(tabId);
        if (mLastTab != newTab) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager()
                    .beginTransaction();
//            if (mLastTab != null) {
//                if (mLastTab.fragment != null) {
//                    ft.detach(mLastTab.fragment);
//                }
//            }
//            if (newTab != null) {
//                if (newTab.fragment == null) {
//                    newTab.fragment = Fragment.instantiate(mActivity,
//                            newTab.clss.getName(), newTab.args);
//                    ft.add(mContainerId, newTab.fragment, newTab.tag);
//                } else {
//                    ft.attach(newTab.fragment);
//                }
//            }
//
//            mLastTab = newTab;
//            ft.commit();
//            mActivity.getSupportFragmentManager().executePendingTransactions();

            if (null != mLastTab) {
                if (null != mLastTab.fragment) {
                    //隐藏旧的Fragment
                    ft.hide(mLastTab.fragment);
                }
            }

            if (null != newTab) {
                if (null == newTab.fragment) {
                    //如果是第一次切换，则生成此Fragment实例对象
                    newTab.fragment = Fragment.instantiate(mActivity,
                            newTab.clss.getCanonicalName(), newTab.args);
                }
                if (!newTab.fragment.isAdded()) {
                    //如果没被add过，则add进去
                    ft.add(mContainerId, newTab.fragment, newTab.tag);
                }
                ft.show(newTab.fragment);
            }
            //重置mLastTab信息
            mLastTab = newTab;
            ft.commit();
            mActivity.getSupportFragmentManager().executePendingTransactions();

        }
    }

    //承载fragment信息的类
    static final class TabInfo {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;
        private Fragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }
}
