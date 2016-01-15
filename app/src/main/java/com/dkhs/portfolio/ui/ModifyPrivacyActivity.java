package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

public class ModifyPrivacyActivity extends ModelAcitivity implements OnClickListener {

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
        if (mCombinationBean != null)
            mMyCombinationEngineImpl.getCombinationPortfolio(mCombinationBean.getId(),
                    new QueryCombinationDetailListener().setLoadingDialog(this));

    }

    private void handleExtras(Bundle extras) {
        mCombinationBean = Parcels.unwrap(extras.getParcelable(EXTRA_COMBINATION));

    }

    class QueryCombinationDetailListener extends ParseHttpListener<CombinationBean> {

        @Override
        protected CombinationBean parseDateTask(String jsonData) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
                jsonData = jsonObject.optString("portfolio");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return DataParse.parseObjectJson(CombinationBean.class, jsonData);
        }

        @Override
        protected void afterParseData(CombinationBean object) {
            if (null != object) {
                combination_ranking.setEnabled(true);
                // open_position.setEnabled(true);
                open_position.setChecked(object.isPubilc());
                open_position.setEnabled(object.isRank());
                combination_ranking.setChecked(object.isRank());
                combination_ranking.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ModifCombinationDetailListener listener = new ModifCombinationDetailListener();

                            listener.setLoadingDialog(ModifyPrivacyActivity.this);

                            mMyCombinationEngineImpl.setCombinationRank(mCombinationBean.getId(), "0", listener);

                            open_position.setEnabled(true);
                        } else {
                            ModifCombinationDetailListener listener = new ModifCombinationDetailListener();

                            listener.setLoadingDialog(ModifyPrivacyActivity.this);
                            mMyCombinationEngineImpl.setCombinationRank(mCombinationBean.getId(), "1", listener);
                            open_position.setChecked(false);
                            open_position.setEnabled(false);
                        }
                    }
                });
                open_position.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // if (is_passive) {
                        if (isChecked) {
                            ModifCombinationDetailListener listener = new ModifCombinationDetailListener();

                            listener.setLoadingDialog(ModifyPrivacyActivity.this);

                            mMyCombinationEngineImpl.changeCombinationIsPublic(mCombinationBean.getId(), "0", listener);
                        } else {
                            ModifCombinationDetailListener listener = new ModifCombinationDetailListener();

                            listener.setLoadingDialog(ModifyPrivacyActivity.this);
                            mMyCombinationEngineImpl.changeCombinationIsPublic(mCombinationBean.getId(), "1", listener);
                        }
                    }

                    // }
                });
            }

        }
    };

    private void initView() {
        combination_ranking = (Switch) findViewById(R.id.combination_ranking);
        open_position = (Switch) findViewById(R.id.open_position);
        combination_ranking.setEnabled(false);
        open_position.setEnabled(false);
        // if (mCombinationBean.getIspublic().equals("0")) {
        // combination_ranking.setChecked(true);
        // open_position.setClickable(true);
        // } else {
        // combination_ranking.setChecked(false);
        // open_position.setClickable(false);
        // }

    }

    class ModifCombinationDetailListener extends ParseHttpListener<List<CombinationBean>> {

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
            return DataParse.parseArrayJson(CombinationBean.class, jsonObject);
        }

        @Override
        protected void afterParseData(List<CombinationBean> object) {
            if (null != object) {
                mCombinationBean = object.get(0);
                // if (mCombinationBean.getIspublic().equals("0")) {
                // // PromptManager.showToast("已开启参与基金排行");
                // open_position.setClickable(true);
                // } else {
                // // PromptManager.showToast("已关闭参与基金排行");
                // // open_position.setClickable(false);
                // open_position.setChecked(false);
                // }
                is_passive = true;
            }

        }
    }

    public static final String EXTRA_COMBINATION = "extra_combination";
    private Switch combination_ranking;
    private Switch open_position;

    public static Intent newIntent(Context context, CombinationBean combinationBean) {
        Intent intent = new Intent(context, ModifyPrivacyActivity.class);

        intent.putExtra(EXTRA_COMBINATION, Parcels.wrap(combinationBean));

        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_privacy_setting);

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_modify_privacy;
    }
}
