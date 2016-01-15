package com.dkhs.portfolio.ui;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.AuthPageEventBean;
import com.dkhs.portfolio.bean.PersonalEventBean;
import com.dkhs.portfolio.bean.PersonalNewEventBean;
import com.dkhs.portfolio.bean.ProInfoBean;
import com.dkhs.portfolio.bean.ProVerificationBean;
import com.dkhs.portfolio.bean.QualificationToPersonalEvent;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.fragment.PersonalFragment;
import com.dkhs.portfolio.ui.fragment.QualificationFragment;
import com.dkhs.portfolio.ui.fragment.SubmitFragment;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

/**
 * 牛人招募
 * Created by xuetong on 2015/12/7.
 */
public class BetterRecruitActivity extends ModelAcitivity implements View.OnClickListener {
    private FragmentManager fm;
    private TextView tv_qualification;
    private TextView tv_personal;
    private TextView tv_submit;
    int width;
    QualificationFragment qualificationFragment;
    SubmitFragment submitFragment;
    PersonalFragment personalFragment;
    private ImageView iv_jt;
    private static final int index1 = 1;
    private static final int index2 = 2;
    private static final int index3 = 3;
    private int type_qua = 0;
    private int index = 0;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_betterrecruit);
        BusProvider.getInstance().register(this);
        setTitle("牛人招募");
        initViews();
        initValues(saveInstanceState);
        initEvents();
        handleType();
    }

    private void initViews() {
        tv_qualification = (TextView) findViewById(R.id.tv_qualification);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_personal = (TextView) findViewById(R.id.tv_personal);
        iv_jt = (ImageView) findViewById(R.id.iv_jt);
        fm = getSupportFragmentManager();
        width = UIUtils.getDisplayMetrics().widthPixels;
    }

    private ProVerificationBean verificationBean;

    private void initValues(Bundle saveInstanceState) {
        if (null == saveInstanceState) {
            type_qua = getIntent().getIntExtra("type", 0);
            verificationBean = Parcels.unwrap(getIntent().getExtras().getParcelable("proverification_bean"));
            index = index1;
        } else {
            type_qua = saveInstanceState.getInt(KEY_CODE, 0);
            index = saveInstanceState.getInt(KEY_INDEX, 0);
            verificationBean = Parcels.unwrap(saveInstanceState.getParcelable(KEY_VER));
            qualificationFragment = (QualificationFragment) fm.findFragmentByTag("qualificationFragment");
            personalFragment = (PersonalFragment) fm.findFragmentByTag("personalFragment");
            if (index == index2) {
                ObjectAnimator.ofFloat(iv_jt, "translationX", 0, (int) (0.35 * width)).setDuration(200).start();
            } else if (index == index3) {
                ObjectAnimator.ofFloat(iv_jt, "translationX", (int) (0.7 * width)).setDuration(200).start();
            }
        }
        showFragment(index, null, -1);

    }

    private static final String KEY_CODE = "key_code";
    private static final String KEY_VER = "key_ver";
    private static final String KEY_INDEX = "key_index";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CODE, type_qua);
        outState.putInt(KEY_INDEX, index);
        outState.putParcelable(KEY_CODE, Parcels.wrap(verificationBean));
    }

    private void initEvents() {
        tv_personal.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        tv_qualification.setOnClickListener(this);
        tv_qualification.setEnabled(true);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                needShowDialog();
            }
        });
    }

    private void initTab() {
        tv_qualification.setEnabled(false);
        tv_personal.setEnabled(false);
        tv_submit.setEnabled(false);
    }

    private void needShowDialog() {
        if (submitFragment != null && submitFragment.isVisible()) {
            finish();
        } else {
            showDialog();
        }
    }

    private void showDialog() {
        MAlertDialog builder = PromptManager.getAlertDialog(this);
        builder.setMessage(R.string.dialog_msg_exit_page)
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                finish();

            }
        });


        builder.show();
    }

    private void showFragment(int i, ProInfoBean bean, int type) {
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        initTab();
        index = i;
        switch (i) {
            case index1:
                tv_qualification.setEnabled(true);
                if (qualificationFragment == null) {
                    qualificationFragment = new QualificationFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", type_qua);
                    if (verificationBean != null) {
                        bundle.putParcelable(QualificationFragment.KEY_PROVERIFICATIONBEAN, Parcels.wrap(verificationBean));
                    }
                    qualificationFragment.setArguments(bundle);
                    ft.add(R.id.fm_main, qualificationFragment, "qualificationFragment");
                } else {
                    ft.show(qualificationFragment);
                }
                break;
            case index2:
                tv_personal.setEnabled(true);
                tv_qualification.setEnabled(true);
                if (personalFragment == null) {
                    personalFragment = new PersonalFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(PersonalFragment.KEY_PERINFOBEAN, Parcels.wrap(bean));
                    if (verificationBean != null) {
                        bundle.putParcelable(PersonalFragment.KEY_PROVERIFICATIONBEAN, Parcels.wrap(verificationBean));
                    }
                    personalFragment.setArguments(bundle);
                    ft.add(R.id.fm_main, personalFragment, "personalFragment");
                } else {
                    BusProvider.getInstance().post(new PersonalNewEventBean(bean));
                    ft.show(personalFragment);
                }

                break;
            case index3:
                tv_submit.setEnabled(true);
                submitFragment = SubmitFragment.newInstance(type);
                ft.add(R.id.fm_main, submitFragment, "submitFragment");
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {
        if (qualificationFragment != null) {
            ft.hide(qualificationFragment);
        }
        if (personalFragment != null) {
            ft.hide(personalFragment);
        }
        if (submitFragment != null) {
            ft.hide(submitFragment);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_qualification:
                index = index1;
                ObjectAnimator.ofFloat(iv_jt, "translationX", 0).setDuration(200).start();
                showFragment(index, null, -1);
                break;
            case R.id.tv_personal:
                index = index2;
                ObjectAnimator.ofFloat(iv_jt, "translationX", (int) (0.375 * width)).setDuration(200).start();
                showFragment(index, null, -1);
                break;
            case R.id.tv_submit:
                //   showFragment(index3);
                break;
        }
    }

    @Subscribe
    public void toPersonalFragment(QualificationToPersonalEvent event) {
        index = index2;
        ObjectAnimator.ofFloat(iv_jt, "translationX", 0, (int) (0.35 * width)).setDuration(200).start();
        showFragment(index, event.proInfoBean, -1);
    }

    @Subscribe
    public void tosubmitFragment(PersonalEventBean event) {
        index = index3;
        ObjectAnimator.ofFloat(iv_jt, "translationX", (int) (0.7 * width)).setDuration(200).start();
        showFragment(index, null, event.verified_status);
        // ObjectAnimator.ofFloat(iv_jt, "translationX", (int) (0.5 * width), (int) (0.7 * width)).setDuration(200).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    private void handleType() {
        if (PortfolioApplication.hasUserLogin()) {
            UserEntity user = GlobalParams.LOGIN_USER;
            int verified_status = user.verified_status;//0, '审核中' 1, '已认证' 2, '审核失败'
            if (verified_status == UserEntity.VERIFIEDSTATUS.VERIFYING.getTypeid()) {//认证中
                BusProvider.getInstance().post(new PersonalEventBean(UserEntity.VERIFIEDSTATUS.VERIFYING.getTypeid()));
            } else if (verified_status == UserEntity.VERIFIEDSTATUS.SUCCESS.getTypeid()) {//认证成功
                BusProvider.getInstance().post(new PersonalEventBean(UserEntity.VERIFIEDSTATUS.SUCCESS.getTypeid()));
            }
        }
    }


    @Subscribe
    public void authPageEvent(AuthPageEventBean bean) {
        tv_personal.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        needShowDialog();
    }

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_better_recruit;
    }
}
