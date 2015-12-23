package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.MarketSubpageFragment;

public class MarketSubpageActivity extends ModelAcitivity {

    private static final String TYPE = "type";
    public static Intent getIntent(Context context,MarketSubpageFragment.SubpageType type){
        Intent intent = new Intent(context,MarketSubpageActivity.class);
        intent.putExtra(TYPE,type.ordinal());
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_subpage);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            hideHead();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            MarketSubpageFragment fragment = MarketSubpageFragment.getFragment(extras.getInt(TYPE));
            ft.replace(R.id.fl_content,fragment);
            ft.commit();
        }
    }


}
