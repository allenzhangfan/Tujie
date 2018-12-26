package com.netposa.tujie.mvp.ui.adapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * <p>Fragments适配器 </p>
 *
 * @author 张华洋 2017/9/27 10:14
 * @version V1.1
 * @name ResourcePagerAdapter
 */
public class HomeFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;

    public HomeFragmentAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
