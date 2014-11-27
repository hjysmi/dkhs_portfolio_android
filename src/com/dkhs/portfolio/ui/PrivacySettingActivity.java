package com.dkhs.portfolio.ui;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PromptManager;

public class PrivacySettingActivity extends ModelAcitivity implements OnClickListener {

    private boolean is_passive = true;
    private CombinationBean mCombinationBean;
    private MyCombinationEngineImpl mMyCombinationEngineImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_setting);
        setTitle(R.string.privacy_setting);
        // handle intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        initView();
        mMyCombinationEngineImpl = new MyCombinationEngineImpl();
    }

    private void handleExtras(Bundle extras) {
        mCombinationBean = (CombinationBean) extras.getSerializable(EXTRA_COMBINATION);

    }

    private void initView() {
        combination_ranking = (Switch) findViewById(R.id.combination_ranking);
        open_position = (Switch) findViewById(R.id.open_position);
        if (mCombinationBean.getIspublic().equals("0")) {
            combination_ranking.setChecked(true);
            open_position.setClickable(true);
        } else {
            combination_ranking.setChecked(false);
            open_position.setClickable(false);
        }
        combination_ranking.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    QueryCombinationDetailListener listener = new QueryCombinationDetailListener();

                    listener.setLoadingDialog(PrivacySettingActivity.this);

                    mMyCombinationEngineImpl.setCombinationRank(mCombinationBean.getId(), "0", listener);

                    open_position.setClickable(true);
                } else {
                    QueryCombinationDetailListener listener = new QueryCombinationDetailListener();

                    listener.setLoadingDialog(PrivacySettingActivity.this);
                    mMyCombinationEngineImpl.setCombinationRank(mCombinationBean.getId(), "1", listener);
                    open_position.setChecked(false);
                    open_position.setClickable(false);
                }
            }
        });
        open_position.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if (is_passive) {
                if (isChecked) {
                    QueryCombinationDetailListener listener = new QueryCombinationDetailListener();

                    listener.setLoadingDialog(PrivacySettingActivity.this);

                    mMyCombinationEngineImpl.changeCombinationIsPublic(mCombinationBean.getId(), "0", listener);
                } else {
                    QueryCombinationDetailListener listener = new QueryCombinationDetailListener();

                    listener.setLoadingDialog(PrivacySettingActivity.this);
                    mMyCombinationEngineImpl.changeCombinationIsPublic(mCombinationBean.getId(), "1", listener);
                }
            }

            // }
        });
    }

    class QueryCombinationDetailListener extends ParseHttpListener<List<CombinationBean>> {

        @Override
        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
            is_passive = false;
            if (mCombinationBean.getIspublic().equals("0")) {
                combination_ranking.setChecked(true);
                open_position.setClickable(true);
            } else {
                combination_ranking.setChecked(false);
                open_position.setClickable(false);
            }
        }

        @Override
        protected List<CombinationBean> parseDateTask(String jsonData) {
            JSONArray jsonObject = null;
            try {
                jsonObject = new JSONArray(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<CombinationBean> object = DataParse.parseArrayJson(CombinationBean.class, jsonObject);
            return object;
        }

        @Override
        protected void afterParseData(List<CombinationBean> object) {
            if (null != object) {
                mCombinationBean = object.get(0);
                if (mCombinationBean.getIspublic().equals("0")) {
                    // PromptManager.showToast("已开启参与基金排行");
                    open_position.setClickable(true);
                } else {
                    // PromptManager.showToast("已关闭参与基金排行");
                    open_position.setClickable(false);
                    open_position.setChecked(false);
                }
                is_passive = true;
            }

        }
    }

    public static final String EXTRA_COMBINATION = "extra_combination";
    private Switch combination_ranking;
    private Switch open_position;

    public static Intent newIntent(Context context, CombinationBean combinationBean) {
        Intent intent = new Intent(context, PrivacySettingActivity.class);

        intent.putExtra(EXTRA_COMBINATION, combinationBean);

        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

}
