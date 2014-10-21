package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.ColorTemplate;

public class ScrollTopFragment extends Fragment {

    public static ScrollTopFragment getInstance() {
        return new ScrollTopFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_scroll, null);
        initView(view);
        // view.setBackgroundColor(ColorTemplate.getRaddomColor());
        return view;
    }

    private void initView(View view) {
    }

}
