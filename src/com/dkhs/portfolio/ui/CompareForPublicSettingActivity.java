package com.dkhs.portfolio.ui;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.PositionDetail;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

public class CompareForPublicSettingActivity extends ModelAcitivity{
	private LinearLayout compareForpublicLayout;
	private Context context;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_compare_forpublicsetting);
		context = this;
		initView();
		loadCombinationData();
	}
	private void initView(){
		setTitle(getResources().getString(R.string.setting_string_public_title));
		compareForpublicLayout = (LinearLayout) findViewById(R.id.compare_forpublic_layout);
	}
	
	/**
	 * 添加组合股是否公开列表数据
	 * @param lsit
	 */
	public void createGroupShow(List<CombinationBean> lsit){
		for (CombinationBean combinationBean : lsit) {
			LayoutInflater l = LayoutInflater.from(context);
			View view = l.inflate(R.layout.setting_group_item, null);
			Switch s = (Switch) view.findViewById(R.id.switch1);
			s.setText(combinationBean.getName());
			if(combinationBean.isIspublics()){
				s.setSelected(true);
			}else{
				s.setSelected(false);
			}
			compareForpublicLayout.addView(view);
		}
	}
	private void loadCombinationData() {
        new MyCombinationEngineImpl().getCombinationList(new ParseHttpListener<List<CombinationBean>>() {

            @Override
            protected List<CombinationBean> parseDateTask(String jsonData) {
                Type listType = new TypeToken<List<CombinationBean>>() {
                }.getType();
                List<CombinationBean> combinationList = DataParse.parseJsonList(jsonData, listType);

                return combinationList;
            }

            @Override
            protected void afterParseData(List<CombinationBean> dataList) {
                createGroupShow(dataList);
            }

        }.setLoadingDialog(this, R.string.loading));
    }
	class QueryCombinationDetailListener extends ParseHttpListener<PositionDetail> {
		private int position;
		public QueryCombinationDetailListener(int position){
			this.position = position;
		}
        @Override
        protected PositionDetail parseDateTask(String jsonData) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return DataParse.parseObjectJson(PositionDetail.class, jsonObject);
        }

        @Override
        protected void afterParseData(PositionDetail object) {
            if (null != object) {
            	
            }

        }
    };
}
