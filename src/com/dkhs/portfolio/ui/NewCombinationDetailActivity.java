/**
 * @Title NewCombinationDetailActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-24 上午10:49:28
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.ui.FloatingActionMenu.OnMenuItemSelectedListener;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.TimeUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @ClassName NewCombinationDetailActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-24 上午10:49:28
 * @version 1.0
 */
public class NewCombinationDetailActivity extends ModelAcitivity {
    public static final String EXTRA_COMBINATION = "extra_combination";
    private CombinationBean mCombinationBean;

    public static Intent newIntent(Context context, CombinationBean combinationBean) {
        Intent intent = new Intent(context, NewCombinationDetailActivity.class);

        intent.putExtra(EXTRA_COMBINATION, combinationBean);

        return intent;
    }

    @ViewInject(R.id.floating_action_view)
    FloatingActionMenu localFloatingActionMenu;

    @ViewInject(R.id.lv_new_combination)
    ListView lvCombinationDetail;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_new_combinationdetail);
        ViewUtils.inject(this);

        // handle intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        updataTitle();
        initView();
        // showFragmentByButtonId(R.id.btn_trend);
    }

    private void updataTitle() {
        if (null != mCombinationBean) {
            setTitle(mCombinationBean.getName());
            setTitleTipString(getString(R.string.format_create_time,
                    TimeUtils.getSimpleDay(mCombinationBean.getCreateTime())));
        }
        if (null != mCombinationBean) {
            // updateTitleBackgroud(mCombinationBean.getNetvalue() - 1);
            updateTitleBackgroudByValue(mCombinationBean.getNetvalue() - 1);
        }
    }

    private void handleExtras(Bundle extras) {
        mCombinationBean = (CombinationBean) extras.getSerializable(EXTRA_COMBINATION);

    }

    private void initView() {
        initFloatingActionMenu();

        lvCombinationDetail.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData()));
        localFloatingActionMenu.attachToListView(lvCombinationDetail);
    }

    private List<String> getData() {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            data.add("测试数据集" + i);

        }
        return data;
    }

    private void initFloatingActionMenu() {
        localFloatingActionMenu.addItem(1, "测试1", R.drawable.ic_agree);
        localFloatingActionMenu.addItem(2, "测试2", R.drawable.ic_discuss);
        localFloatingActionMenu.setOnMenuItemSelectedListener(new OnMenuItemSelectedListener() {

            @Override
            public boolean onMenuItemSelected(int paramInt) {
                if (paramInt == 1) {
                    PromptManager.showToast("Menu 1 on click");
                } else {
                    PromptManager.showToast("Menu 2 on click");

                }
                return false;
            }
        });
        // localFloatingActionMenu.addItem(1, "测试", 11);
    }

}
