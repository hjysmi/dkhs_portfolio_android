package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OrgBean;
import com.dkhs.portfolio.bean.OrganizationEventBean;
import com.dkhs.portfolio.engine.OrganizationEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.SortAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.sortlist.SideBar;
import com.dkhs.portfolio.utils.SortModel;
import com.dkhs.portfolio.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuetong on 2015/12/9.
 * 所属机构
 */
public class OrganizationActivity extends ModelAcitivity implements View.OnClickListener {
    private ListView lv_organization;
    private TextView tv_center_index;
    private SideBar sidrbar;
    public static final String KEY_TYPE = "type";
    private int type = 0;
    private SortAdapter adapter;
    List<SortModel> SourceDateList = new ArrayList<>();
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    //private PinyinComparator pinyinComparator;
    private OrganizationEngine engine;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle("所属机构");
        setContentView(R.layout.activity_organization);
        initViews();
        initValues();
        initEvents();
        initData();
    }

    private void initData() {
        engine = new OrganizationEngine();
        //  engine.getOrg();
        engine.getOrg(orgListener, String.valueOf(type));

    }

    private void initViews() {
        lv_organization = (ListView) findViewById(R.id.lv_organization);
        tv_center_index = (TextView) findViewById(R.id.tv_center_index);
        sidrbar = (SideBar) findViewById(R.id.sidrbar);
    }

    private void initValues() {
        type = getIntent().getIntExtra(KEY_TYPE, 0);
        //pinyinComparator = new PinyinComparator();
      //  Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        // adapter.bindData(SourceDateList);
        lv_organization.setAdapter(adapter);
    }

    private ParseHttpListener<List<OrgBean>> orgListener = new ParseHttpListener<List<OrgBean>>() {
        @Override
        protected List<OrgBean> parseDateTask(String jsonData) {

            return DataParse.parseArrayJson(OrgBean.class, jsonData);
        }

        @Override
        protected void afterParseData(List<OrgBean> object) {
            SortModel sortModel;
            for (int i = 0; i < object.size(); i++) {
                sortModel = new SortModel();

                sortModel.setName(object.get(i).getName());
                //汉字转换成拼音
                String pinyin = object.get(i).getChi_spell_all();
                //  String pinyin = characterParser.getSelling(object.get(i).getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();

                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }

                SourceDateList.add(sortModel);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private void initEvents() {
        lv_organization.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BusProvider.getInstance().post(new OrganizationEventBean(SourceDateList.get(position).getName()));
                finish();
                UIUtils.outAnimationActivity(OrganizationActivity.this);

            }
        });
        sidrbar.setTextView(tv_center_index);
        //设置右侧触摸监听
        sidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lv_organization.setSelection(position);
                }

            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
