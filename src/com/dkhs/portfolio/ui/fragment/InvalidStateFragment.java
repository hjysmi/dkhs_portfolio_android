package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dkhs.portfolio.R;

/**
 * @author useradmin
 * @version 1.0
 * @ClassName zwm
 * @Description TODO(通知栏点击事件:当用户未登陆状态时候显示 )
 * @date 2015/4/17.12:10
 */
public class InvalidStateFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invalid_state, container, false);
    }
}