package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.ui.fragment.CombinationRankFragment;
import com.dkhs.portfolio.ui.fragment.UserCombinationListFragment;

import java.security.Key;

public class CombinationListActivity extends ModelAcitivity {


    public  static String KEY_USER_ID="KEY_USER_ID";

    private String mUserId;


    public static  void startActivity(Context context,String user_id){
        Intent intent =new Intent(context,CombinationListActivity.class);
        intent.putExtra(KEY_USER_ID,user_id);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination_list);

        if(UserEntity.currentUser(mUserId)){
            setTitle(R.string.title_activity_my_combination_list);
        }else{
            setTitle(R.string.title_activity_ta_combination_list);
        }

        if (getIntent().hasExtra(KEY_USER_ID)){
            mUserId=   getIntent().getStringExtra(KEY_USER_ID);
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, UserCombinationListFragment.getFragment(mUserId)).commitAllowingStateLoss();
        }

    }


}
