package com.dkhs.portfolio.ui.city;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.ModelAcitivity;
import com.dkhs.portfolio.ui.fragment.SelectCityFragment;

public class SelectCityActivity extends ModelAcitivity {

    private final static String PARENT_CODE = "parent_code";
    private final static String PARENT_NAME = "parent_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_select_city);
        Intent intent = getIntent();
        String parentCode = intent.getStringExtra(PARENT_CODE);
        String parentName = intent.getStringExtra(PARENT_NAME);
        replaceContentFragment(SelectCityFragment.newInstance(SelectCityFragment.CITY_TYPE, parentName, parentCode));
    }

    public static Intent getItent(Context context,String parentName,String parentCode){
        Intent it = new Intent(context,SelectCityActivity.class);
        it.putExtra(PARENT_CODE,parentCode);
        it.putExtra(PARENT_NAME,parentName);
        return it;
    }

}
