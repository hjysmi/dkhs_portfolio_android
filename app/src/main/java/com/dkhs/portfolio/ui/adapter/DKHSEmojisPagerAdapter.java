package com.dkhs.portfolio.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dkhs.portfolio.ui.fragment.DKHSEmojiFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import java.util.List;

/**
 * Created by zhangcm on 2015/7/21.
 */
public class DKHSEmojisPagerAdapter extends FragmentStatePagerAdapter{

    private List<DKHSEmojiFragment> fragments;
    public DKHSEmojisPagerAdapter(FragmentManager fm, List<DKHSEmojiFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }
    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public interface OnEmojiconClickedListener {
        void onEmojiconClicked(Emojicon emojicon);
    }
}
