package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.ui.fragment.UserCombinationListFragment;

import org.parceler.Parcels;

public class CombinationListActivity extends ModelAcitivity {


    public static String PARAM_USER_BEAN = "key_userbean";

    private UserEntity mUserBean;


    public static void startActivity(Context context, UserEntity userBean) {
        Intent intent = new Intent(context, CombinationListActivity.class);
        intent.putExtra(PARAM_USER_BEAN, Parcels.wrap(userBean));
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUserBean = Parcels.unwrap(extras.getParcelable(PARAM_USER_BEAN));
        }
        if (null != mUserBean) {
            if (UserEntity.currentUser(mUserBean.getId() + "")) {
                setTitle(R.string.title_activity_my_combination_list);
            } else {
                setTitle(R.string.title_activity_ta_combination_list);
            }
            replaceContentFragment(UserCombinationListFragment.getFragment());
        }


//            if (getIntent().hasExtra(KEY_USER_ID)) {
//                mUserId = getIntent().getStringExtra(KEY_USER_ID);
//                getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, ).commitAllowingStateLoss();
//            }

    }


}
