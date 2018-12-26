package com.netposa.component.jq.mvp.ui.adapter;

import com.netposa.common.utils.Utils;
import com.netposa.component.jq.R;
import com.netposa.component.jq.mvp.ui.fragment.CarDeployFragment;
import com.netposa.component.jq.mvp.ui.fragment.FaceDeployFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DailyPagerAdapter extends FragmentPagerAdapter {

    private final int[] mTitles = new int[]{
            R.string.deploy_face,
            R.string.deploy_car};

    private List<Fragment> mFragments = new ArrayList<>();

    @Inject
    public DailyPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments.add(FaceDeployFragment.newInstance());
        mFragments.add(CarDeployFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Utils.getContext().getResources().getString(mTitles[position]);
    }
}
