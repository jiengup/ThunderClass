package com.example.thunderclass;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class myFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"课件", "试题"};
    private int class_id;
    public myFragmentPagerAdapter(FragmentManager fm, int class_id) {
        super(fm);
        this.class_id = class_id;
    }
    @Override
    public Fragment getItem(int position) {
        if (position == 1)
            return new t_test(class_id);
        return new t_ppt(class_id);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }
    //用来设置tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
