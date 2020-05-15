package com.dev.signatureonphoto.features.template;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dev.signatureonphoto.features.template.fragment.FragmentTemplate;


public class PagerAdapter extends FragmentStatePagerAdapter {
    private String[] mangTitle = {"Background"};
    private FragmentTemplate fragmentTemplate;


    public PagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentTemplate=new FragmentTemplate();
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0) return fragmentTemplate;
        return null;
    }

    @Override
    public int getCount() {
        return mangTitle.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mangTitle[position];
    }
}
