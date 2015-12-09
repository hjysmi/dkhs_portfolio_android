package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.percent.PercentFrameLayout;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.AgreementTextActivity;
import com.dkhs.portfolio.ui.PersonalIntroduceActivity;
import com.dkhs.portfolio.ui.city.SelectProviceActivity;
import com.dkhs.portfolio.ui.eventbus.BackCityEvent;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.utils.UIUtils;
import com.squareup.otto.Subscribe;

/**
 * 牛人招募之个人资料
 * Created by xuetong on 2015/12/7.
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener {
    private TextView rlt_agreement;
    private PercentFrameLayout fm_city;
    private PercentFrameLayout fm_introduce;
    public static final int RESULT_INTRODUCE_BACK = 1;
    private TextView tv_introduce;
    private TextView tv_city;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvents();
    }


    private void initView(View view) {
        rlt_agreement = (TextView) view.findViewById(R.id.rlt_agreement);
        fm_introduce = (PercentFrameLayout) view.findViewById(R.id.fm_introduce);
        tv_introduce = (TextView) view.findViewById(R.id.tv_introduce);
        fm_city = (PercentFrameLayout) view.findViewById(R.id.fm_city);
        tv_city = (TextView) view.findViewById(R.id.tv_city);
    }

    private void initEvents() {
        rlt_agreement.setOnClickListener(this);
        fm_introduce.setOnClickListener(this);
        fm_city.setOnClickListener(this);
    }

    @Subscribe
    public void getCity(BackCityEvent event) {
        tv_city.setText(event.city);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlt_agreement:
                Intent intent = new Intent(getActivity(), AgreementTextActivity.class);
                startActivity(intent);
                break;
            case R.id.fm_introduce:
                Intent intent_introduce = new Intent(getActivity(), PersonalIntroduceActivity.class);
                startActivityForResult(intent_introduce, RESULT_INTRODUCE_BACK);
                break;
            case R.id.fm_city:
                UIUtils.startAnimationActivity(getActivity(), new Intent(getActivity(), SelectProviceActivity.class));
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case RESULT_INTRODUCE_BACK:
                if (null == data) {
                    return;
                } else {
                    String content = data.getStringExtra(PersonalIntroduceActivity.RESULT_CONTENT);
                    tv_introduce.setText(content);
                }
                break;
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
