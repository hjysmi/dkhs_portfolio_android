package com.dkhs.portfolio.ui;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class EveryDayValueActivity extends ModelAcitivity{
	public static final String EXTRA_COMBINATION = "extra_combination";
	private CombinationBean mCombinationBean;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_everyday_record);
		setTitle(R.string.privacy_everyday_record);
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
	}
	
	private void handleExtras(Bundle extras) {
        mCombinationBean = (CombinationBean) extras.getSerializable(EXTRA_COMBINATION);

    }
	public static Intent newIntent(Context context, CombinationBean combinationBean) {
        Intent intent = new Intent(context, EveryDayValueActivity.class);

        intent.putExtra(EXTRA_COMBINATION, combinationBean);

        return intent;
    }
}
