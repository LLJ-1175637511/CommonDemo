package com.llj.commondemo.sliding_conflict.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * @Author donkingliang
 * @Description
 * @Date 2020/11/5
 */
public class FragmentPagerAdapter extends FragmentStateAdapter {

    private List<? extends Fragment> mFragments;

    public FragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<? extends Fragment> fragments) {
        super(fragmentActivity);
        this.mFragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}
