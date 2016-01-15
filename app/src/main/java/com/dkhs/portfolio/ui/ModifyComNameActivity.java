package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.UpdateComDescEvent;
import com.dkhs.portfolio.utils.PromptManager;

import org.parceler.Parcels;

public class ModifyComNameActivity extends ModelAcitivity implements OnClickListener {

    private CombinationBean mCombinationBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_combination_name);
        setTitle(R.string.change_combination_name);
        // handle intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        initView();
    }

    private void handleExtras(Bundle extras) {
//        mCombinationBean = (CombinationBean) extras.getParcelable(EXTRA_COMBINATION);
        mCombinationBean = Parcels.unwrap(extras.getParcelable(EXTRA_COMBINATION));


    }

    private void initView() {
        TextView btnSave = getRightButton();
        btnSave.setText("确定");
        btnSave.setBackgroundDrawable(null);
        btnSave.setOnClickListener(this);
        combination_desc = (EditText) findViewById(R.id.combination_desc);
        combination_name = (EditText) findViewById(R.id.combination_name);
        combination_name.setText(mCombinationBean.getName());
        if (TextUtils.isEmpty(mCombinationBean.getDescription())) {
            combination_desc.setHint(R.string.desc_def_text);
        } else {
            combination_desc.setText(mCombinationBean.getDescription());
        }

        // combination_desc.setText(TextUtils.isEmpty(mCombinationBean.getDescription()) ? "" : mCombinationBean
        // .getDescription());
        // String text = TextUtils.isEmpty(mCombinationBean.getDescription()) ? "" : mCombinationBean
        // .getDescription();
        // if(text.equals("还没有描述内容呢")){
        // combination_desc.setText("");
        // }
    }

    public static final String EXTRA_COMBINATION = "extra_combination";
    private EditText combination_desc;
    private EditText combination_name;

    public static Intent newIntent(Context context, CombinationBean combinationBean) {
        Intent intent = new Intent(context, ModifyComNameActivity.class);

        intent.putExtra(EXTRA_COMBINATION, Parcels.wrap(combinationBean));

        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                //
                final String combinationName = combination_name.getText().toString().trim();
                if (TextUtils.isEmpty(combinationName)) {
                    PromptManager.showToast("组合名称不能为空");
                    return;
                }
                if (combinationName.length() < 3 || combinationName.length() > 10) {
                    PromptManager.showToast("3-10位字符:支持中英文、数字。");
                    return;
                }
                final String combinationDesc = combination_desc.getText().toString().trim();
                new MyCombinationEngineImpl().updateCombination(mCombinationBean.getId(), combinationName,
                        combinationDesc, new ParseHttpListener<CombinationBean>() {

                            @Override
                            protected CombinationBean parseDateTask(String jsonData) {
                                return DataParse.parseObjectJson(CombinationBean.class, jsonData);
                            }

                            @Override
                            protected void afterParseData(CombinationBean object) {
                                if (null != object) {
                                    setSelectBack(object);
                                }


                                BusProvider.getInstance().post(new UpdateComDescEvent(combinationName,combinationDesc));
                            }
                        }.setLoadingDialog(this, "修改中..."));
                break;

            default:
                break;
        }
    }

    public static final String ARGUMENT_COMBINATION_BEAN = "combination_bean";

    private void setSelectBack(CombinationBean bean) {
        PromptManager.showEditSuccessToast();
        Intent intent = new Intent();
        intent.putExtra(ARGUMENT_COMBINATION_BEAN, Parcels.wrap(bean));
        // intent.putExtra(ARGUMENT_CRATE_TYPE, type);
        setResult(RESULT_OK, intent);

        finish();
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(
            R.string.count_combination_name_change);

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_modify_com_name;
    }
}
