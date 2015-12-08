package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.AgreementTextActivity;

/**
 * 牛人招募之个人资料
 * Created by xuetong on 2015/12/7.
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener {
    private TextView rlt_agreement;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvents();
    }


    private void initView(View view) {
        rlt_agreement = (TextView) view.findViewById(R.id.rlt_agreement);

    }

    private void initEvents() {
        rlt_agreement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlt_agreement:
                Intent intent = new Intent(getActivity(), AgreementTextActivity.class);
                startActivity(intent);
                break;
        }

    }
}
