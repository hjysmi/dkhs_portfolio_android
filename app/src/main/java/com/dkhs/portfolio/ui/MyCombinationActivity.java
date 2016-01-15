/**
 * @Title MyCombinationActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-26 下午3:10:51
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.MyCombinationFragmnet;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MyCombinationActivity
 * @Description 我的组合
 * @date 2014-8-26 下午3:10:51
 */
public class MyCombinationActivity extends ModelAcitivity implements OnClickListener {
    private TextView btnRefresh;

    private MyCombinationFragmnet listFragment;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hadFragment();
//        setContentView(R.layout.activity_mycombination);
        setTitle(R.string.my_combination);

        initTitleView();
        replaceCombinationListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // loadCombinationData();
    }

    private void initTitleView() {
        TextView btnMore = getRightButton();
        btnMore.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_title_add), null,
                null, null);
        btnMore.setOnClickListener(this);

        btnRefresh = getSecondRightButton();
        btnRefresh.setOnClickListener(this);
        btnRefresh.setVisibility(View.VISIBLE);
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh_selector),
                null, null, null);

    }

    public void rotateRefreshButton() {
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refreshing), null,
                null, null);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_around_center_point);
        btnRefresh.startAnimation(animation);
    }

    public void stopRefreshAnimation() {

        btnRefresh.clearAnimation();
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh), null,
                null, null);
    }

    private void replaceCombinationListView() {
        listFragment = new MyCombinationFragmnet();
//        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, listFragment).commit();
        replaceContentFragment(listFragment);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == RIGHTBUTTON_ID) {
            // listFragment.setListDelStatus(false);
            // btnMore.setVisibility(View.GONE);
            // btnTwo.setVisibility(View.VISIBLE);
            addNewCombination();
            // clickRightButton();
        } else if (id == SECONDRIGHTBUTTON_ID) {
            clickSecondButton();
            // clickSecondButton();
        }

    }

    // private void clickRightButton() {
    // if (btnMore.getTag() != null && btnMore.getTag().equals("cancel")) {
    // // mCombinationAdapter.setDelStatus(false);
    // listFragment.setListDelStatus(false);
    // setButtonAdd();
    // } else {
    // addNewCombination();
    // }
    //
    // }

    private void addNewCombination() {
        // startActivity(PositionAdjustActivity.newIntent(this, null));
        if (null != listFragment)
            listFragment.createNewCombination();

    }

    // private void setButtonAdd() {
    // btnMore.setTag("add");
    // btnMore.setText("");
    // btnMore.setBackgroundResource(R.drawable.ic_title_add);
    // // btnRefresh.setVisibility(View.VISIBLE);
    // }

    // public void setButtonCancel() {
    // // btnMore.setText(R.string.cancel);
    // // btnMore.setTag("cancel");
    // // btnMore.setBackgroundDrawable(null);
    // // btnMore.setVisibility(View.VISIBLE);
    // // btnTwo.setVisibility(View.GONE);
    // // btnMore.setText("取消");
    // // btnRefresh.setVisibility(View.GONE);
    // }

    // public void setButtonFinish() {
    // btnMore.setText("完成");
    // }

    // private void setButtonRefresh() {
    // // btnRefresh.setTag("refresh");
    // // btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);
    // }

    private void clickSecondButton() {
        listFragment.refresh();

    }

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_mycombination;
    }
}
