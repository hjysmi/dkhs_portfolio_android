package com.dkhs.portfolio.ui.city;

import android.os.Bundle;

import com.dkhs.portfolio.ui.ModelAcitivity;
import com.dkhs.portfolio.ui.fragment.SelectCityFragment;

public class SelectProviceActivity extends ModelAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceContentFragment(SelectCityFragment.newInstance(SelectCityFragment.PROVICE_TYPE, "", ""));
        setTitle("选择省份");
    }
}
