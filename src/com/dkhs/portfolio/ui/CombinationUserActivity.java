/**
 * @Title UserCombinationActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-6 下午4:40:09
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.fragment.FragmentNetValueTrend;
import com.dkhs.portfolio.ui.fragment.UserCombinationListFragment;
import com.dkhs.portfolio.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName UserCombinationActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-11-6 下午4:40:09
 * @version 1.0
 */
public class CombinationUserActivity extends ModelAcitivity {

    private String mUserId;
    private String mUserName;
    private boolean isMyInfo;

    private ImageView ivHeader;
    private TextView tvUName;
    private TextView tvCText;
    private TextView tvUCreateTime;
    private TextView tvUserDesc;
    private Context context;

    public static Intent getIntent(Context context, String username, String userId, boolean isMyInfo) {
        Intent intent = new Intent(context, CombinationUserActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("username", username);
        intent.putExtra("is_my_info", isMyInfo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_user_combination);
        context = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        initViews();
        initData();

    }

    private void handleExtras(Bundle extras) {
        mUserId = extras.getString("user_id");
        mUserName = extras.getString("username");
        isMyInfo = extras.getBoolean("is_my_info");

    }

    private void initViews() {
        ivHeader = (ImageView) findViewById(R.id.iv_uheader);
        tvUName = (TextView) findViewById(R.id.tv_user_name);
        tvUCreateTime = (TextView) findViewById(R.id.tv_user_create_time);
        tvCText = (TextView) findViewById(R.id.tv_combination_text);
        tvUserDesc = (TextView) findViewById(R.id.tv_user_desc);

        if (isMyInfo) {
            setTitle("我的资料");
            tvCText.setText(R.string.text_my_manager_combin);
        } else {
            setTitle("组合创建人资料");
            tvCText.setText(R.string.text_other_manager_combin);
        }

        replaceCombinationListView();

    }

    private void replaceCombinationListView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rl_combination_list, UserCombinationListFragment.getFragment(mUserName, mUserId))
                .commit();

    }

    private void initData() {

        userInfoListener.setLoadingDialog(context);
        new UserEngineImpl().getBaseUserInfo(mUserId, userInfoListener);
    }

    ParseHttpListener userInfoListener = new ParseHttpListener<UserEntity>() {

        @Override
        protected UserEntity parseDateTask(String jsonData) {

            return DataParse.parseObjectJson(UserEntity.class, jsonData);
        }

        @Override
        protected void afterParseData(UserEntity object) {
            if (null != object) {
                updateUserView(object);
            }

        }
    };

    protected void updateUserView(UserEntity object) {
        BitmapUtils bitmapUtils = new BitmapUtils(this);
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_user_head);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_user_head);
        if (null != object.getAvatar_md() && object.getAvatar_md().length() > 35) {
            bitmapUtils.display(ivHeader, object.getAvatar_md());
        }
        tvUName.setText(object.getUsername());
        tvUCreateTime.setText(TimeUtils.getSimpleDay(object.getDate_joined()) + "   进驻谁牛");

        tvUserDesc.setText(getString(R.string.format_sign_text_title, object.getDescription()));

    }

}
