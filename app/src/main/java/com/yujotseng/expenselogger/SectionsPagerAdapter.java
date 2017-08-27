package com.yujotseng.expenselogger;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yu Jo on 8/26/2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentsList = new ArrayList<Fragment>(4);

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    public void addFragment(Fragment fragment) {
        fragmentsList.add(fragment);
    }
}
