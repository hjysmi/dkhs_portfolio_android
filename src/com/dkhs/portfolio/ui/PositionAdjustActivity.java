/**
 * @Title PositionAdjustActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-25 上午9:55:53
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.bean.PositionDetail;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.SubmitSymbol;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.OptionalStockAdapter;
import com.dkhs.portfolio.ui.adapter.OptionalStockAdapter.IDutyNotify;
import com.dkhs.portfolio.ui.widget.ListViewEx;
import com.dkhs.portfolio.ui.widget.PieGraph;
import com.dkhs.portfolio.ui.widget.PieSlice;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;

/**
 * @ClassName PositionAdjustActivity
 * @Description 持仓组合调整
 * @author zjz
 * @date 2014-8-25 上午9:55:53
 * @version 1.0
 */
public class PositionAdjustActivity extends ModelAcitivity implements IDutyNotify, OnClickListener {

    // private static final String KEY_VIEW_TYPE = "key_view_type";
    public static final String EXTRA_POSITIONDETAIL = "extra_positiondetail";
    public static final String EXTRA_ISADJUSTCOMBINATION = "EXTRA_ISADJUSTCOMBINATION";
    public static final String EXTRA_COMBINATION_ID = "key_combination_id";
    // public static final String VALUE_CREATE_CONBINA = "value_create_conbina";
    // public static final String VALUE_ADJUST_CONBINA = "value_adjust_conbina";
    private final int REQUESTCODE_SELECT_STOCK = 901;

    private PieGraph pgView;
    private List<ConStockBean> stockList = new ArrayList<ConStockBean>();
    private ListViewEx lvStock;
    private OptionalStockAdapter stockAdapter;
    public static float surValue;
    private TextView tvSurpusValue;
    private SeekBar surSeekbar;
    private ArrayList<PieSlice> pieList;
    // private String mViewType = VALUE_CREATE_CONBINA;
    private TextView tvCreateTime;
    private TextView tvTodayNetvalue;
    private EditText etConbinationName;
    private EditText etConbinationDesc;
    private Button btnConfirm;
    private Button btnAverage;
    private View viewCombinationInfo;

    private PositionDetail mPositionDetailBean;
    private int mCombinationId;
    private boolean isAdjustCombination;
    private TextView positionTextValue;
    private TextView positionTextCreatedate;

    public static Intent newIntent(Context context, PositionDetail positionBean) {
        Intent intent = new Intent(context, PositionAdjustActivity.class);

        // extras
        if (null == positionBean) {
            intent.putExtra(EXTRA_ISADJUSTCOMBINATION, false);
        } else {
            intent.putExtra(EXTRA_ISADJUSTCOMBINATION, true);
            intent.putExtra(EXTRA_POSITIONDETAIL, positionBean);
            // intent.putExtra(EXTRA_COMBINATION_ID, combinationId);
        }

        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_positionadjust);

        // handle intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        initData();
        initView();
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param extras
     * @return void
     */
    private void handleExtras(Bundle extras) {
        // String typeValue = extras.getString(KEY_VIEW_TYPE);
        // if (!TextUtils.isEmpty(typeValue)) {
        // mViewType = typeValue;
        // }
        isAdjustCombination = extras.getBoolean(EXTRA_ISADJUSTCOMBINATION);
        mPositionDetailBean = (PositionDetail) extras.getSerializable(EXTRA_POSITIONDETAIL);
        if (null != mPositionDetailBean) {

            if (null != mPositionDetailBean.getPositionList()) {
                copyDefalutList();
            }
            mCombinationId = mPositionDetailBean.getPortfolio().getId();
        }

    }

    private void copyDefalutList() {
        stockList.clear();
        for (ConStockBean bean : mPositionDetailBean.getPositionList()) {
            stockList.add(bean.clone());
        }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initView() {

        btnConfirm = getRightButton();
        btnConfirm.setText(R.string.confirm);
        btnConfirm.setOnClickListener(this);
        findViewById(R.id.btn_add_postional).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        positionTextValue = (TextView) findViewById(R.id.position_text_value);
        positionTextCreatedate = (TextView) findViewById(R.id.position_text_createdate);
        viewCombinationInfo = findViewById(R.id.rl_combinationvalue);
        btnAverage = (Button) findViewById(R.id.btn_average);
        btnAverage.setOnClickListener(this);

        initConbinationInfoView();

        initPieView();
        initFooterView();
        initStockPercentView();

        if (null != mPositionDetailBean) {
            viewCombinationInfo.setVisibility(View.VISIBLE);
            positionTextValue.setText(StringFromatUtils.get4Point(mPositionDetailBean.getPortfolio().getNetvalue()));
            positionTextValue.setTextColor(ColorTemplate.getUpOrDrownCSL(mPositionDetailBean.getPortfolio()
                    .getNetvalue() - 1));
            // String time = mPositionDetailBean.getPortfolio().getCreateTime().replace("T", "-");
            // time = time.substring(0, time.length() - 4);
            positionTextCreatedate.setText("成立时间:"
                    + TimeUtils.getSimpleFormatTime(mPositionDetailBean.getPortfolio().getCreateTime()));
        }
        isShowAverageButton();
    }

    private void initConbinationInfoView() {
        etConbinationName = (EditText) findViewById(R.id.et_myconbina_name);
        etConbinationDesc = (EditText) findViewById(R.id.et_myconbina_desc);
        if (isAdjustCombination) {
            setTitle(R.string.adjust_combination);
            // ViewStub viewstub = (ViewStub) findViewById(R.id.portfolio_info);
            // if (viewstub != null) {
            // viewstub.inflate();
            // View inflatedView = viewstub.inflate();
            // tvCreateTime = (TextView) inflatedView.findViewById(R.id.tv_create_time);
            // tvTodayNetvalue = (TextView) inflatedView.findViewById(R.id.tv_today_netvalue);
            // // tvCreateTime.setText(text)
            // tvTodayNetvalue.setText(mPositionDetailBean.getPortfolio().getCurrentValue() + "");
            // // tvCreateTime.setText(mPositionDetailBean.getPortfolio().getCreateTime());
            // tvCreateTime.setText(TimeUtils.getSimpleFormatTime(mPositionDetailBean.getPortfolio().getCreateTime()));
            // TextView tvCombinationName = (TextView) inflatedView.findViewById(R.id.tv_portfolio_name);
            // tvCombinationName.setText(mPositionDetailBean.getPortfolio().getName());
            // }
            etConbinationName.setText(mPositionDetailBean.getPortfolio().getName());
            etConbinationDesc.setText(mPositionDetailBean.getPortfolio().getDescription());
        } else {
            setTitle(R.string.create_combination);
            // ViewStub viewstub = (ViewStub) findViewById(R.id.create_portfolio_info);

            // if (viewstub != null) {
            // View inflatedView = viewstub.inflate();
            //
            // }
        }
    }

    /**
     * @Title
     * @Description TODO: 初始化数据
     * @return void
     */
    private void initData() {
        if (!isAdjustCombination) {
            surValue = 100;
            // ConStockBean stock4 = new SurpusStock(surValue);
            // stockList.add(stock4);
        } else {
            setStockList();
        }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void setStockList() {

        // System.out.println("PositionDetailBean.getCurrentDate():" + mPositionDetailBean.getCurrentDate());
        //
        // ConStockBean stock1 = new ConStockBean(1, 0.3f, getResources().getColor(ColorTemplate.DEFAULTCOLORS[0]),
        // "沪深大盘", "600123");
        // ConStockBean stock2 = new ConStockBean(2, 0.4f, getResources().getColor(ColorTemplate.DEFAULTCOLORS[1]),
        // "苏宁云商", "622123");
        // ConStockBean stock3 = new ConStockBean(3, 0.3f, getResources().getColor(ColorTemplate.DEFAULTCOLORS[2]),
        // "阿里巴巴", "666666");
        // // ConStockBean stock4 = new SurpusStock(surValue);
        // stockList.add(stock1);
        // stockList.add(stock2);
        // stockList.add(stock3);
        // stockList.add(stock4);

    }

    /**
     * @Title
     * @Description TODO: 初始化自选股占比
     * @return void
     */

    private void initStockPercentView() {
        lvStock = (ListViewEx) findViewById(R.id.lv_optional_layout);
        stockAdapter = new OptionalStockAdapter(this, stockList);
        stockAdapter.setDutyNotifyListener(this);
        lvStock.addFooterView(mFooterView);
        lvStock.setAdapter(stockAdapter);
        setFootData();

    }

    private View mFooterView;

    private void initFooterView() {
        mFooterView = View.inflate(this, R.layout.layout_optional_percent, null);
        mFooterView.findViewById(R.id.tv_stock_num).setVisibility(View.GONE);
        surSeekbar = (SeekBar) mFooterView.findViewById(R.id.seekBar);
        // surSeekbar.setEnabled(false);
        surSeekbar.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        surSeekbar.setProgress((int) (surValue));
        surSeekbar.setThumb(getResources().getDrawable(R.drawable.lucency));
        TextView tvName = (TextView) mFooterView.findViewById(R.id.tv_stock_name);
        tvName.setText(R.string.surplus_capital_ratio);
        tvSurpusValue = (TextView) mFooterView.findViewById(R.id.tv_stock_percent);
        tvSurpusValue.setText(StringFromatUtils.getPercentValue((int) (surValue)) + "");
        ScaleDrawable sd = (ScaleDrawable) ((LayerDrawable) surSeekbar.getProgressDrawable())
                .findDrawableByLayerId(android.R.id.progress);

        GradientDrawable gd = (GradientDrawable) sd.getDrawable();
        gd.setColor(ColorTemplate.DEF_RED);
        mFooterView.findViewById(R.id.view_color).setBackgroundColor(ColorTemplate.DEF_RED);
        // return foot;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initPieView() {
        pgView = (PieGraph) findViewById(R.id.piegrah);
        // pieList = new ArrayList<PieSlice>();
        setPieList();
    }

    private void setPieList() {

        int valueSize = stockList.size();
        pieList = new ArrayList<PieSlice>();
        for (int i = 0; i < valueSize; i++) {
            PieSlice slice1 = new PieSlice();

            slice1.setColor(stockList.get(i).getDutyColor());
            slice1.setValue(stockList.get(i).getDutyValue());
            pieList.add(slice1);

        }
        surpulsValue();
        PieSlice emptySlice = new PieSlice();
        emptySlice.setColor(ColorTemplate.DEF_RED);
        emptySlice.setValue(surValue);
        pieList.add(emptySlice);

        pgView.setSlices(pieList);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void notifyRefresh(int position, int value) {
        pieList.get(position).setValue(value);
        pieList.get(pieList.size() - 1).setValue(surpulsValue());
        pgView.invalidate();
        isShowAverageButton();
        setFootData();
    }

    private int surpulsValue() {
        int total = 100;
        for (int i = 0; i < stockList.size(); i++) {
            total -= stockList.get(i).getDutyValue();
        }
        surValue = total;

        return total;
    }

    private void setFootData() {
        surSeekbar.setProgress((int) (surValue));
        tvSurpusValue.setText(StringFromatUtils.getPercentValue((int) (surValue)) + "");
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param value
     * @return
     */
    @Override
    public void updateSurpus(int value) {

        // System.out.println("surValue:" + surValue);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_confirm:
            case R.id.btn_right: {
                if (!isAdjustCombination) {
                    createCombinationByServer();
                } else {
                    adjustPositionDetailToServer();
                }
                // Toast.makeText(PositionAdjustActivity.this, "确定添加", Toast.LENGTH_SHORT).show();
            }
                break;
            case R.id.btn_add_postional: {

                List<SelectStockBean> mSelectList = new ArrayList<SelectStockBean>();
                for (ConStockBean stockBean : stockList) {
                    SelectStockBean bean = SelectStockBean.copy(stockBean);
                    mSelectList.add(bean);
                }

                Intent intent = new Intent(this, SelectStockActivity.class);
                intent.putExtra(BaseSelectActivity.ARGUMENT_SELECT_LIST, (Serializable) mSelectList);
                Bundle b = new Bundle();
                b.putBoolean("fromPosition", true);
                intent.putExtras(b);
                if (isAdjustCombination) {
                    intent.putExtra(BaseSelectActivity.KEY_ISADJUST_COMBINATION, true);
                }
                startActivityForResult(intent, REQUESTCODE_SELECT_STOCK);

            }
                break;
            case R.id.btn_average: {
                averageValue();
                updatePieView();
            }
                break;

            default:
                break;
        }
    }

    private void averageValue() {
        if (null != stockList && stockList.size() > 0) {
            int length = stockList.size();
            float total = 1.0f;
            float dutyValue = (total / length);
            float residual = (100f % length);
            for (int i = 0; i < length; i++) {
                ConStockBean c = stockList.get(i);
                total -= dutyValue;
                c.setPercent(dutyValue);

                c.setDutyColor(ColorTemplate.getDefaultColor(i));

            }

            stockList.get(0).setPercent(dutyValue + residual / 100);

        }

    }

    private boolean isModiyName;
    private boolean isModifyPosition;

    private void adjustPositionDetailToServer() {
        MyCombinationEngineImpl engine = new MyCombinationEngineImpl();
        String nameText = etConbinationName.getText().toString();
        String descText = etConbinationDesc.getText().toString();
        List<SubmitSymbol> submitList = generateSymbols();
        isModiyName = !nameText.equalsIgnoreCase(mPositionDetailBean.getPortfolio().getName())
                || !descText.equalsIgnoreCase(mPositionDetailBean.getPortfolio().getDescription());
        isModifyPosition = submitList.size() > 0;
        if (!isModifyPosition && !isModiyName) {

            PromptManager.showToast("持仓信息没有修改");
            return;
        }
        if (isModiyName)
            engine.updateCombination(mCombinationId, nameText, descText, adjustNameListener);

        if (isModifyPosition) {
            engine.adjustCombination(mCombinationId, generateSymbols(),
                    adjustPositionListener.setLoadingDialog(this, "修改中..."));
        }

    }

    ParseHttpListener adjustNameListener = new ParseHttpListener() {

        @Override
        protected Object parseDateTask(String jsonData) {

            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            mPositionDetailBean.getPortfolio().setName(etConbinationName.getText().toString());
            mPositionDetailBean.getPortfolio().setDescription(etConbinationDesc.getText().toString());
            Toast.makeText(PositionAdjustActivity.this, "名称修改成功", Toast.LENGTH_SHORT).show();
        }

    };
    ParseHttpListener adjustPositionListener = new ParseHttpListener() {

        @Override
        protected Object parseDateTask(String jsonData) {

            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            Toast.makeText(PositionAdjustActivity.this, "持仓调整成功", Toast.LENGTH_SHORT).show();
            finish();
        }

    };

    private List<SubmitSymbol> generateSymbols() {
        List<SubmitSymbol> symbols = new ArrayList<SubmitSymbol>();
        List<ConStockBean> tempList = new ArrayList<ConStockBean>();
        tempList.addAll(stockList);
        if (null != mPositionDetailBean) {

            List<ConStockBean> originalBeanList = mPositionDetailBean.getPositionList();

            for (ConStockBean originStock : originalBeanList) {
                if (tempList.contains(originStock)) {
                    int index = tempList.indexOf(originStock);
                    ConStockBean bean = tempList.get(index);
                    if (originStock.getDutyValue() == bean.getDutyValue()) {
                        tempList.remove(index);
                    }

                } else {
                    originStock.setDutyColor(0);
                    tempList.add(originStock);

                }

            }
        }

        for (ConStockBean stock : tempList) {
            SubmitSymbol symbol = new SubmitSymbol();
            symbol.setSymbol(stock.getStockId());
            symbol.setPercent(stock.getDutyValue() / 100.0f);
            // System.out.println("symbols stock id:" + symbol.getSymbol() + " value:" + symbol.getPercent());
            symbols.add(symbol);
        }
        return symbols;
    }

    private void createCombinationByServer() {

        String combinationName = "";
        if (null != etConbinationName) {
            combinationName = etConbinationName.getText().toString();
        }
        if (TextUtils.isEmpty(combinationName)) {
            PromptManager.showToast("基金名称不能为空");
        }

        String combinationDesc = "";
        if (null != etConbinationDesc) {
            combinationDesc = etConbinationDesc.getText().toString();
        }

        List<SubmitSymbol> symbolsList = generateSymbols();
        if (symbolsList.size() < 1) {

            PromptManager.showToast("请添加个股");

        } else {

            new MyCombinationEngineImpl().createCombination(combinationName, combinationDesc, symbolsList,
                    new ParseHttpListener<CombinationBean>() {

                        /**
                         * @Title
                         * @Description TODO: (用一句话描述这个方法的功能)
                         * @return
                         */
                        @Override
                        public void beforeRequest() {
                            // TODO Auto-generated method stub
                            super.beforeRequest();
                            btnConfirm.setEnabled(false);
                        }

                        @Override
                        public void requestCallBack() {
                            // TODO Auto-generated method stub
                            super.requestCallBack();

                            btnConfirm.setEnabled(true);
                        }

                        @Override
                        protected void afterParseData(CombinationBean object) {
                            if (null != object && !isAdjustCombination) {
                                PositionAdjustActivity.this.startActivity(CombinationDetailActivity.newIntent(
                                        PositionAdjustActivity.this, object));
                            }
                            finish();

                        }

                        @Override
                        protected CombinationBean parseDateTask(String jsonData) {
                            return DataParse.parseObjectJson(CombinationBean.class, jsonData);

                        }

                    }.setLoadingDialog(this, "创建中..."));
        }
    }

    private void submitAdjustToServer() {
        // new MyCombinationEngineImpl().adjustCombination(mCombinationId, symbols, new BasicHttpListener() {
        //
        // @Override
        // public void onSuccess(String result) {
        // finish();
        // }
        // });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            Bundle b = data.getExtras(); // data为B中回传的Intent
            switch (requestCode) {
                case REQUESTCODE_SELECT_STOCK:
                    ArrayList<SelectStockBean> listStock = (ArrayList<SelectStockBean>) data
                            .getSerializableExtra(BaseSelectActivity.ARGUMENT_SELECT_LIST);
                    int createType = data.getIntExtra(BaseSelectActivity.ARGUMENT_CRATE_TYPE,
                            BaseSelectActivity.CRATE_TYPE_FAST);
                    if (null != listStock) {
                        // if (createType != -1) {
                        //
                        // if (stockList == null) {
                        // stockList = new ArrayList<ConStockBean>();
                        // }
                        // stockList.clear();
                        // for (SelectStockBean selectBean : listStock) {
                        // stockList.add(selectBean.parseStock());
                        // }
                        //
                        // // setCombinationBack(createType);
                        //
                        // // } else {
                        // }
                        setAddStockBack(listStock);
                        updatePieView();

                    }
                    break;
            }
        }
    }

    private void updatePieView() {
        setPieList();
        // lvStock.removeFooterView(mFooterView);
        stockAdapter.setList(stockList);
        surpulsValue();
        setFootData();
        // stockAdapter.
        // stockAdapter.notifyDataSetChanged();
        isShowAverageButton();
        lvStock.invalidate();
    }

    private void isShowAverageButton() {
        if (null != stockList && stockList.size() > 0 && surValue != 0) {
            btnAverage.setVisibility(View.VISIBLE);
        } else {
            btnAverage.setVisibility(View.GONE);
        }
    }

    // private void setCombinationBack(int which) {
    // if (null != stockList && stockList.size() > 0) {
    // int length = stockList.size();
    // float dutyValue = (1.0f / length);
    // for (int i = 0; i < length; i++) {
    // ConStockBean c = stockList.get(i);
    // if (0 == which) {// 快速
    // c.setPercent(dutyValue);
    // }
    // c.setDutyColor(ColorTemplate.getDefaultColor(i));
    //
    // }
    // }
    //
    // }

    private void setAddStockBack(List<SelectStockBean> listStock) {
        int i = 0;
        List<ConStockBean> tempList = new ArrayList<ConStockBean>();
        for (SelectStockBean selectBean : listStock) {
            ConStockBean csBean = selectBean.parseStock();
            csBean.setDutyValue(0);
            csBean.setDutyColor(ColorTemplate.getDefaultColor(i));
            if (stockList.contains(csBean)) {
                if (i < stockList.size()) {
                    csBean.setDutyValue(stockList.get(i).getDutyValue());
                    // stockList.get(i).setDutyColor(ColorTemplate.getDefaultColor(i));
                }

            }

            tempList.add(csBean);
            i++;
        }
        stockList.clear();
        stockList.addAll(tempList);

    }
}
