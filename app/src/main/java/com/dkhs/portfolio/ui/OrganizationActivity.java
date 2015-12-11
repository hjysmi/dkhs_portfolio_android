package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OrganizationEventBean;
import com.dkhs.portfolio.engine.OrganizationEngine;
import com.dkhs.portfolio.ui.adapter.SortAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.sortlist.SideBar;
import com.dkhs.portfolio.utils.CharacterParser;
import com.dkhs.portfolio.utils.PinyinComparator;
import com.dkhs.portfolio.utils.SortModel;
import com.dkhs.portfolio.utils.UIUtils;

import java.util.ArrayList;
import java.util.Collections;
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
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private SortAdapter adapter;
    List<SortModel> SourceDateList = new ArrayList<>();
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private OrganizationEngine engine;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle("所属机构");
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        setContentView(R.layout.activity_organization);
        initViews();
        initValues();
        initEvents();
        initData();
    }

    private void initData() {
        engine = new OrganizationEngine(this);
        //engine.getOrg(listener);
        List<String> list = new ArrayList<>();
        list.add("中信证券");
        list.add("海通证券");
        list.add("国泰君安");
        list.add("广发证券");
        list.add("华泰证券");
        list.add("招商证券");
        list.add("银河证券");
        list.add("厦门");
        list.add("国信证券");
        list.add("申万宏源");
        list.add("中信建投");
        list.add("光大证券");
        list.add("东方证券");
        list.add("安信证券");
        list.add("齐鲁证券");
        list.add("方正证券");
        list.add("齐鲁证券");
        list.add("兴业证券");
        list.add("长江证券");
        list.add("中投证券");
        list.add("平安证券");
        list.add("西南证券");
        SortModel sortModel;
        for (int i = 0; i < list.size(); i++) {
            sortModel = new SortModel();

            sortModel.setName(list.get(i));
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(list.get(i));
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            SourceDateList.add(sortModel);
        }


    }

    private void initViews() {
        lv_organization = (ListView) findViewById(R.id.lv_organization);
        tv_center_index = (TextView) findViewById(R.id.tv_center_index);
        sidrbar = (SideBar) findViewById(R.id.sidrbar);
    }

    private void initValues() {
        type = getIntent().getIntExtra(KEY_TYPE, 0);
        //实例化汉字转拼音类
        //  characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        // adapter.bindData(SourceDateList);
        lv_organization.setAdapter(adapter);
    }

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
