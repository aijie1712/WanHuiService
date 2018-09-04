package com.cmeplaza.basemodule.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.cmeplaza.basemodule.base.BaseFragment;

import java.util.List;

/**
 * Created by Administrator on 2016-11-30
 *
 * @desc 主页的FragmentAdapter
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> titleLists;

    private BaseFragment fragment;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleLists) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleLists = titleLists;
    }

    public BaseFragment getCurrentFragment() {
        return fragment;
    }

    @Override
    public Fragment getItem(int index) {
        return fragmentList.get(index);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        fragment = (BaseFragment) object;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titleLists != null && titleLists.size() > position) {
            return titleLists.get(position);
        }
        return super.getPageTitle(position);
    }
}
