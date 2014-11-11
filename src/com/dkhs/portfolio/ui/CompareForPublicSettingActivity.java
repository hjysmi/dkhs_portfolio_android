package com.dkhs.portfolio.ui;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SetCombinPublicEntity;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class CompareForPublicSettingActivity extends ModelAcitivity {
    private LinearLayout compareForpublicLayout;
    private Context context;
    private MyCombinationEngineImpl mMyCombinationEngineImpl;
    private List<CombinationBean> list;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_compare_forpublicsetting);
        context = this;
        mMyCombinationEngineImpl = new MyCombinationEngineImpl();
        initView();
        loadCombinationData();
    }

    private void initView() {
        setTitle(getResources().getString(R.string.setting_string_public_title));
        compareForpublicLayout = (LinearLayout) findViewById(R.id.compare_forpublic_layout);
    }

    /**
     * 添加组合股是否公开列表数据
     * 
     * @param lsit
     */
    public void createGroupShow(List<CombinationBean> lsit) {
        int i = 0;
        for (CombinationBean combinationBean : lsit) {
            LayoutInflater l = LayoutInflater.from(context);
            View view = l.inflate(R.layout.setting_group_item, null);
            Switch s = (Switch) view.findViewById(R.id.switch1);
            //s.setSwitchTypeface(tf);
            s.setText(combinationBean.getName());
            if (combinationBean.getIspublic().equals("0")) {
                s.setChecked(true);
            } else {
                s.setChecked(false);
            }
            s.setOnCheckedChangeListener(new OnSwitchChange(i++));
            compareForpublicLayout.addView(view);
        }
    }

    private void loadCombinationData() {
        new MyCombinationEngineImpl().getCombinationList(new ParseHttpListener<MoreDataBean<CombinationBean>>() {

            @Override
            protected MoreDataBean<CombinationBean> parseDateTask(String jsonData) {

                MoreDataBean<CombinationBean> moreBean = null;
                try {

                    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

                    moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<CombinationBean>>() {
                    }.getType());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return moreBean;

            }

            @Override
            protected void afterParseData(MoreDataBean<CombinationBean> moreBean) {
                list = moreBean.getResults();
                createGroupShow(list);
            }

        }.setLoadingDialog(this, R.string.loading));
    }

    class QueryCombinationDetailListener extends ParseHttpListener<List<CombinationBean>> {
        private int position;

        public QueryCombinationDetailListener(int position) {
            this.position = position;
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

            }

        }
    };

    class OnSwitchChange implements OnCheckedChangeListener {
        int position;

        public OnSwitchChange(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                mMyCombinationEngineImpl.changeCombinationIsPublic(list.get(position).getId(), "0",
                        new QueryCombinationDetailListener(position));
            } else {
                mMyCombinationEngineImpl.changeCombinationIsPublic(list.get(position).getId(), "1",
                        new QueryCombinationDetailListener(position));
            }
        }

    }
}
