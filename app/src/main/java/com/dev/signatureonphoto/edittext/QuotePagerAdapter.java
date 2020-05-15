package com.dev.signatureonphoto.edittext;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dev.signatureonphoto.Constants;

import java.util.List;

public class QuotePagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;

    QuotePagerAdapter(FragmentManager fm) {
        super(fm);
    }


    public List<Fragment> getFragments() {
        return fragments;
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return Constants.PAGER_TITLE[position];
    }
}
