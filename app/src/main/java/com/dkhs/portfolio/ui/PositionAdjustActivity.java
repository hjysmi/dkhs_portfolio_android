/**
 * @Title PositionAdjustActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-25 上午9:55:53
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.bean.PositionDetail;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.SubmitSymbol;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.OptionalStockAdapter;
import com.dkhs.portfolio.ui.adapter.OptionalStockAdapter.IDutyNotify;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.UpdatePositinoEvent;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.ui.widget.PieGraph;
import com.dkhs.portfolio.ui.widget.PieSlice;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName PositionAdjustActivity
 * @Description 持仓组合调整
 * @date 2014-8-25 上午9:55:53
 */
public class PositionAdjustActivity extends ModelAcitivity implements IDutyNotify, OnClickListener {

    public static final String EXTRA_POSITIONDETAIL = "extra_positiondetail";
    public static final String EXTRA_ISADJUSTCOMBINATION = "EXTRA_ISADJUSTCOMBINATION";
    public static final String EXTRA_COMBINATION_ID = "key_combination_id";
    private final int REQUESTCODE_SELECT_STOCK = 901;
    public static final String COME_FROM = "come_frome";
    public static final int COME_MAIN = 0;
    public static final int COME_COMBOLE = 1;
    private PieGraph pgView;
    private List<ConStockBean> stockList = new ArrayList<ConStockBean>();
    private ListView lvStock;
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
    private TextView btnConfirm;
    private TextView btnAverage;

    private PositionDetail mPositionDetailBean;
    private String mCombinationId;
    private boolean isAdjustCombination;
    //    private TextView positionTextValue;
//    private TextView positionTextCreatedate;
    private boolean firse = false;

    public static Intent newIntent(Context context, String combinationId) {
        Intent intent = new Intent(context, PositionAdjustActivity.class);

        // extras
        if (null == combinationId) {
            intent.putExtra(EXTRA_ISADJUSTCOMBINATION, false);
        } else {
            intent.putExtra(EXTRA_ISADJUSTCOMBINATION, true);
            // intent.putExtra(EXTRA_POSITIONDETAIL, positionId);
            intent.putExtra(EXTRA_COMBINATION_ID, combinationId);
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
        getSwipeBackLayout().setEnableGesture(false);

        if (isAdjustCombination) {

            QueryCombinationDetailListener listener = new QueryCombinationDetailListener();
            listener.setLoadingDialog(this);
            new MyCombinationEngineImpl().queryCombinationDetail(mCombinationId, listener);
        }

    }

    /**
     * @param extras
     * @return void
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    private void handleExtras(Bundle extras) {

        isAdjustCombination = extras.getBoolean(EXTRA_ISADJUSTCOMBINATION);
        // mPositionDetailBean = (PositionDetail) extras.getSerializable(EXTRA_POSITIONDETAIL);
        mCombinationId = extras.getString(EXTRA_COMBINATION_ID);

        if (!isAdjustCombination) {
            startSelectStockActivity(true);
        }

    }

    private void copyDefalutList(List<ConStockBean> mList) {
        stockList.clear();
        if (null != mList && mList.size() > 0) {

            int listSize = 0;

            for (ConStockBean bean : mList) {
                bean.setDutyColor(ColorTemplate.getDefaultColor(listSize++));
                stockList.add(bean.clone());
            }
        }

    }

    private View headerView;
    private FloatingActionMenu mFloatingActionMenu;

    /**
     * @return void
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    private void initView() {

        btnConfirm = getRightButton();
        btnConfirm.setText(R.string.finish);
        btnConfirm.setOnClickListener(this);

        headerView = View.inflate(this, R.layout.layout_postionadjust_header, null);
        mFloatingActionMenu = (FloatingActionMenu) findViewById(R.id.floatingMenu);
        mFloatingActionMenu.setOnMenuItemSelectedListener(mFloatMenuSelectListner);

//        headerView.findViewById(R.id.btn_add_postional).setOnClickListener(this);
//        positionTextValue = (TextView) headerView.findViewById(R.id.position_text_value);
//        positionTextCreatedate = (TextView) headerView.findViewById(R.id.position_text_createdate);
        btnAverage = (TextView) headerView.findViewById(R.id.btn_average);
        btnAverage.setOnClickListener(this);
        etConbinationName = (EditText) headerView.findViewById(R.id.et_myconbina_name);
        etConbinationDesc = (EditText) headerView.findViewById(R.id.et_myconbina_desc);
        initConbinationInfoView();


        etConbinationDesc.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(final View view, boolean hasFocus) {
                        if (hasFocus) {

                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    view.requestFocus();
//                                    view.requestFocusFromTouch();
                                }
                            });
                        } else {
                            view.clearFocus();
                        }
                    }
                });
        etConbinationName.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(final View view, boolean hasFocus) {
                        if (hasFocus) {


                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    view.requestFocus();
//                                    view.requestFocusFromTouch();
                                }
                            });
                        } else {
                            view.clearFocus();
                        }
                    }
                });


        // initPieView();
        pgView = (PieGraph) headerView.findViewById(R.id.piegrah);

        mFooterView = View.inflate(this, R.layout.layout_postionadjust_bottom, null);
        mFooterView.findViewById(R.id.tv_stock_num).setVisibility(View.GONE);
        mFooterView.findViewById(R.id.rl_info).setVisibility(View.INVISIBLE);
        mFooterView.findViewById(R.id.v_divline).setVisibility(View.INVISIBLE);
//        Button btnconfirm = (Button) mFooterView.findViewById(R.id.btn_confirm);
//        btnconfirm.setOnClickListener(this);
//        btnconfirm.setVisibility(View.VISIBLE);

        surSeekbar = (SeekBar) mFooterView.findViewById(R.id.seekBar);
        tvSurpusValue = (TextView) mFooterView.findViewById(R.id.tv_stock_percent);
        // surSeekbar.setEnabled(false);
        surSeekbar.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        initFooterView();

        initStockPercentView();
        initFloatingActionMenu();
        isShowAverageButton();

        etConbinationName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void initConbinationInfoView() {

        if (isAdjustCombination) {
            setTitle(R.string.adjust_combination);
            headerView.findViewById(R.id.create_portfolio_info).setVisibility(View.GONE);
//            headerView.findViewById(R.id.rl_combinationvalue).setVisibility(View.GONE);
//            headerView.findViewById(R.id.tv_myconfig_text).setVisibility(View.INVISIBLE);

        } else {
            setTitle(R.string.create_combination);
        }
    }

    private void updateViewData() {
        if (null != mPositionDetailBean) {

            etConbinationName.setText(mPositionDetailBean.getPortfolio().getName());
            etConbinationDesc.setText(mPositionDetailBean.getPortfolio().getDefDescription());
            // viewCombinationInfo.setVisibility(View.VISIBLE);
//            positionTextValue.setText(StringFromatUtils.get4Point(mPositionDetailBean.getPortfolio().getNetvalue()));
//            positionTextValue.setTextColor(ColorTemplate.getUpOrDrownCSL(mPositionDetailBean.getPortfolio()
//                    .getNetvalue() - 1));
//            // String time = mPositionDetailBean.getPortfolio().getCreateTime().replace("T", "-");
//            // time = time.substring(0, time.length() - 4);
//            positionTextCreatedate.setText("成立时间: "
//                    + TimeUtils.getSimpleFormatTime(mPositionDetailBean.getPortfolio().getCreateTime()));

            setPieList(mPositionDetailBean.getFund_percent());
            setFootData(mPositionDetailBean.getFund_percent());
        }

    }

    /**
     * @return void
     * @Title
     * @Description TODO: 初始化数据
     */
    private void initData() {
        if (!isAdjustCombination) {
            surValue = 100;
            // ConStockBean stock4 = new SurpusStock(surValue);
            // stockList.add(stock4);
        } else {

            // setStockList();
        }
    }

    /**
     * @return void
     * @Title
     * @Description TODO: 初始化自选股占比
     */

    private void initStockPercentView() {
        lvStock = (ListView) findViewById(R.id.lv_optional_layout);
        mFloatingActionMenu.attachToListView(lvStock);

        stockAdapter = new OptionalStockAdapter(this, stockList);
        stockAdapter.setDutyNotifyListener(this);
//        lvStock.setItemsCanFocus(true);
        lvStock.addFooterView(mFooterView);
        lvStock.addHeaderView(headerView);
        lvStock.setAdapter(stockAdapter);

    }

    private final int MENU_ADD = 6;
    FloatingActionMenu.OnMenuItemSelectedListener mFloatMenuSelectListner = new FloatingActionMenu.OnMenuItemSelectedListener() {

        @Override
        public boolean onMenuItemSelected(int selectIndex) {
            if (selectIndex == MENU_ADD) {
                startSelectStockActivity(false);
            }
            return false;
        }

    };

    private void initFloatingActionMenu() {
        mFloatingActionMenu.removeAllItems();

        mFloatingActionMenu.addItem(MENU_ADD, R.string.float_menu_addstock, R.drawable.ic_add);


    }

    private View mFooterView;

    private void initFooterView() {

        surSeekbar.setProgress((int) (surValue));
        surSeekbar.setThumb(getResources().getDrawable(R.drawable.lucency));
        TextView tvName = (TextView) mFooterView.findViewById(R.id.tv_stock_name);
        tvName.setText(R.string.surplus_capital_ratio);
        tvSurpusValue.setText(StringFromatUtils.get2PointPercent(surValue));
        ScaleDrawable sd = (ScaleDrawable) ((LayerDrawable) surSeekbar.getProgressDrawable())
                .findDrawableByLayerId(android.R.id.progress);

        GradientDrawable gd = (GradientDrawable) sd.getDrawable();
        gd.setColor(ColorTemplate.SURP_RED);
        mFooterView.findViewById(R.id.view_color).setBackgroundColor(ColorTemplate.SURP_RED);
        // return foot;
    }

    private void setPieList(float survalue) {

        int valueSize = stockList.size();
        pieList = new ArrayList<PieSlice>();
        for (int i = 0; i < valueSize; i++) {
            PieSlice slice1 = new PieSlice();

            slice1.setColor(stockList.get(i).getDutyColor());
            slice1.setValue(stockList.get(i).getPercent());
            pieList.add(slice1);

        }

        PieSlice emptySlice = new PieSlice();
        emptySlice.setColor(ColorTemplate.SURP_RED);

        emptySlice.setValue(survalue);

        pieList.add(emptySlice);

        pgView.setSlices(pieList);
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void notifyRefresh(int position, int value) {
        pieList.get(position).setValue(value);
        pieList.get(pieList.size() - 1).setValue(surpulsValue());
        pgView.invalidate();
        isShowAverageButton();
        setFootData(surValue);
    }

    private float surpulsValue() {
        float total = 100;
        float sum = 0;
        for (int i = 0; i < stockList.size(); i++) {
            sum += stockList.get(i).getPercent();
        }
        surValue = total - sum;
        if (surValue < 0) {

            surValue = 0;
        }

        return surValue;
    }

    private void setFootData(float survalue) {

        surSeekbar.setProgress((int) (survalue));
        tvSurpusValue.setText(StringFromatUtils.get2PointPercent(survalue));
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    class QueryCombinationDetailListener extends ParseHttpListener<PositionDetail> {

        @Override
        protected PositionDetail parseDateTask(String jsonData) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            PositionDetail bean = DataParse.parseObjectJson(PositionDetail.class, jsonObject);

            // surpulsValue();
            if (null != bean) {

                copyDefalutList(bean.getPositionList());
            }

            return bean;
        }

        @Override
        protected void afterParseData(PositionDetail object) {
            if (null != object) {

                mPositionDetailBean = object;

                updateViewData();
            }

        }
    }

    ;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_right: {

                if (StringFromatUtils.isContainsEmoji(etConbinationName.getText().toString())
                        || StringFromatUtils.isContainsEmoji(etConbinationDesc.getText().toString())) {
                    PromptManager.showToast("3-10位字符:支持中英文、数字。");
                    return;
                }

                if (!isAdjustCombination) {
                    createCombinationByServer();
                } else {
                    if (mPositionDetailBean != null) {
                        adjustPositionDetailToServer();
                    }
                }
                // Toast.makeText(PositionAdjustActivity.this, "确定添加", Toast.LENGTH_SHORT).show();
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

    private void startSelectStockActivity(boolean isCreate) {
        firse = false;
        List<SelectStockBean> mSelectList = new ArrayList<SelectStockBean>();
        for (ConStockBean stockBean : stockList) {
            SelectStockBean bean = SelectStockBean.copy(stockBean);
            mSelectList.add(bean);
        }
        Intent intent = new Intent(this, SelectStockActivity.class);
        intent.putExtra(BaseSelectActivity.ARGUMENT_SELECT_LIST, Parcels.wrap(mSelectList));
        Bundle b = new Bundle();
        b.putBoolean("fromPosition", true);
        b.putBoolean("isFrist", firse);
        if (isCreate) {
            b.putString(BaseSelectActivity.FROM_CREATE_TITLE, "yes");
        }
        intent.putExtras(b);
        if (isAdjustCombination) {
            intent.putExtra(BaseSelectActivity.KEY_ISADJUST_COMBINATION, true);
        }
        startActivityForResult(intent, REQUESTCODE_SELECT_STOCK);
        UIUtils.setOverridePendingAnin(this);
    }

    private void averageValue() {
        if (null != stockList && stockList.size() > 0) {
            int length = stockList.size();
            int total = 100;
            float dutyValue = (total / length);
            float residual = (total % length);
            for (int i = 0; i < length; i++) {
                ConStockBean c = stockList.get(i);
                total -= dutyValue;
                // c.setPercent(dutyValue);
                c.setPercent((int) (dutyValue));

                c.setDutyColor(ColorTemplate.getDefaultColor(i));

            }

            stockList.get(0).setPercent((int) (dutyValue + residual));

        }

    }

    private boolean isModifyPosition;

    private void adjustPositionDetailToServer() {
        MyCombinationEngineImpl engine = new MyCombinationEngineImpl();
        String nameText = etConbinationName.getText().toString().trim();
        String descText = etConbinationDesc.getText().toString();
        List<SubmitSymbol> submitList = generateSymbols();
        isModifyPosition = submitList.size() > 0;
        boolean isModiyName = !nameText.equalsIgnoreCase(mPositionDetailBean.getPortfolio().getName())
                || !descText.equalsIgnoreCase(mPositionDetailBean.getPortfolio().getDefDescription());
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
            PromptManager.showEditSuccessToast();
        }

    };
    ParseHttpListener adjustPositionListener = new ParseHttpListener() {

        @Override
        protected Object parseDateTask(String jsonData) {

            return null;
        }

//        @Override
//        public void onFailure(int errCode, String errMsg) {
//            try {
//
//                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
//
//                BaseError<RaiseUpDown> baseErrors = gson.fromJson(errMsg, new TypeToken<BaseError<RaiseUpDown>>() {
//                }.getType());
//                RaiseUpDown raiseError = baseErrors.getErrors();
//                StringBuilder sbRaiseUp = null;
//                StringBuilder sbRaiseDown = null;
//                if (null == raiseError.getRaise_down() && null == raiseError.getRaise_up()) {
//                    super.onFailure(errCode, errMsg);
//                    return;
//                }
//                if (null != raiseError.getRaise_down() && raiseError.getRaise_down().size() > 0) {
//                    // Toast.makeText(getApplicationContext(),
//                    // "跌停股：" + raiseError.getRaise_down().size() + "无法调低占比  ", Toast.LENGTH_LONG)
//                    // .show();
//                    sbRaiseDown = new StringBuilder();
//                    for (String code : raiseError.getRaise_down()) {
//                        sbRaiseDown.append(code);
//                        sbRaiseDown.append("、");
//
//                    }
//                }
//                if (null != raiseError.getRaise_up() && raiseError.getRaise_up().size() > 0) {
//                    sbRaiseUp = new StringBuilder();
//                    for (String code : raiseError.getRaise_up()) {
//                        sbRaiseUp.append(code);
//                        sbRaiseUp.append("、");
//                    }
//
//                }
//
//                StringBuilder sbToastText = new StringBuilder();
//                if (null != sbRaiseUp && sbRaiseUp.length() > 1) {
//                    sbToastText.append("涨停股");
//                    sbToastText.append(sbRaiseUp.substring(0, sbRaiseUp.length() - 1));
//                    sbToastText.append("无法调高占比.");
//                    sbToastText.append("\n");
//
//                }
//                if (null != sbRaiseDown && sbRaiseDown.length() > 1) {
//                    sbToastText.append("跌停股");
//                    sbToastText.append(sbRaiseDown.substring(0, sbRaiseDown.length() - 1));
//                    sbToastText.append("无法调低占比.");
//                    sbToastText.append("\n");
//                }
//
//                copyDefalutList(mPositionDetailBean.getPositionList());
//                updatePieView();
//                Toast.makeText(getApplicationContext(), sbToastText, Toast.LENGTH_LONG).show();
//            } catch (Exception e) {
//                // TODO: handle exception
//            }
//        }
//


        @Override
        public void onFailure(ErrorBundle errorBundle) {


            if (!TextUtils.isEmpty(errorBundle.getErrorKey()) && errorBundle.getErrorKey().equals(MyCombinationEngineImpl.ERROR_KEY_AJUST)) {

                copyDefalutList(mPositionDetailBean.getPositionList());
                updatePieView();
                PromptManager.showLToast(errorBundle.getErrorMessage());
            } else {
                super.onFailure(errorBundle);
            }


        }

        @Override
        protected void afterParseData(Object object) {
            PromptManager.showEditSuccessToast();
            BusProvider.getInstance().post(new UpdatePositinoEvent());
            finish();
        }

    };

    private List<SubmitSymbol> generateSymbols() {
        List<SubmitSymbol> symbols = new ArrayList<SubmitSymbol>();
        List<ConStockBean> tempList = new ArrayList<ConStockBean>();
        tempList.addAll(stockList);
        if (null != mPositionDetailBean) {

            List<ConStockBean> originalBeanList = mPositionDetailBean.getPositionList();
            ConStockBean tempBean;
            for (ConStockBean originStock : originalBeanList) {
                if (tempList.contains(originStock)) {
                    int index = tempList.indexOf(originStock);
                    tempBean = tempList.get(index);
                    if (originStock.getPercent() == tempBean.getPercent()) {
                        tempList.remove(index);
                    }

                } else {

                    tempBean = originStock.clone();
                    tempBean.setPercent(0);
                    tempList.add(tempBean);

                }

            }
        }

        for (ConStockBean stock : tempList) {
            SubmitSymbol symbol = new SubmitSymbol();
            if (isAdjustCombination) {
                symbol.setSymbol(stock.getStockCode());
            } else {

                symbol.setSymbol(stock.getStockId() + "");
            }
            symbol.setPercent((int) stock.getPercent());
            symbols.add(symbol);
        }
        return symbols;
    }


    private void createCombinationByServer() {

        String combinationName = "";
        if (null != etConbinationName) {
            combinationName = etConbinationName.getText().toString().trim();
        }
        if (TextUtils.isEmpty(combinationName)) {
            PromptManager.showToast("组合名称不能为空");
            return;
        }
        if (combinationName.length() < 3 || combinationName.length() > 10) {
            PromptManager.showToast("3-10位字符:支持中英文、数字。");
            return;
        }

        String combinationDesc = "";
        if (null != etConbinationDesc) {
            combinationDesc = etConbinationDesc.getText().toString();
        }

        List<SubmitSymbol> symbolsList = generateSymbols();
        if (symbolsList.size() < 1) {

            PromptManager.showToast("请添加个股");

        } else if (surValue == 100) {
            PromptManager.showToast("持仓不能为空");
        } else {

            new MyCombinationEngineImpl().createCombination(combinationName, combinationDesc, symbolsList,
                    new ParseHttpListener<CombinationBean>() {

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
                                requestCombinationDetail(object);

                            }

                        }


                        @Override
                        public void onFailure(ErrorBundle errorBundle) {
//                            super.onFailure(errorBundle);
                            if (!TextUtils.isEmpty(errorBundle.getErrorKey()) && errorBundle.getErrorKey().equals(MyCombinationEngineImpl.ERROR_KEY_AJUST)) {

                                for (ConStockBean stock : stockList) {
                                    stock.setPercent(0);
                                }
                                updatePieView();
                                PromptManager.showLToast(errorBundle.getErrorMessage());
                            } else {
                                super.onFailure(errorBundle);
                            }

                        }

                       /* @Override
                        public void onFailure(int errCode, String errMsg) {
                            try {

                                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

                                BaseError<RaiseUpDown> baseErrors = gson.fromJson(errMsg,
                                        new TypeToken<BaseError<RaiseUpDown>>() {
                                        }.getType());
                                RaiseUpDown raiseError = baseErrors.getErrors();
                                if (null == raiseError.getRaise_down() && null == raiseError.getRaise_up()) {
                                    super.onFailure(errCode, errMsg);
                                    return;
                                }

                                if (null != raiseError.getRaise_up() && raiseError.getRaise_up().size() > 0) {
                                    StringBuilder sb = new StringBuilder();

                                    for (String code : raiseError.getRaise_up()) {
                                        sb.append(code);
                                        sb.append("、");

                                        for (ConStockBean stock : stockList) {
                                            if (stock.getStockSymbol().equalsIgnoreCase(code)) {
                                                stock.setPercent(0);
                                            }
                                        }
                                    }
                                    updatePieView();
                                    Toast.makeText(getApplicationContext(),
                                            "涨停股：" + sb.substring(0, sb.length() - 1) + "不能加入组合", Toast.LENGTH_LONG)
                                            .show();

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
*/

                        @Override
                        protected CombinationBean parseDateTask(String jsonData) {
                            return DataParse.parseObjectJson(CombinationBean.class, jsonData);

                        }

                    }.setLoadingDialog(this, "创建中..."));
        }
    }


    private void requestCombinationDetail(CombinationBean combinationBean) {
        new MyCombinationEngineImpl().queryCombinationDetail(combinationBean.getId(), new ParseHttpListener<PositionDetail>() {

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
                            PositionAdjustActivity.this.startActivity(CombinationDetailActivity.newIntent(
                                    PositionAdjustActivity.this, object.getPortfolio()));
                            finish();
                        }

                    }
                }.setLoadingDialog(this)
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            Bundle b = data.getExtras(); // data为B中回传的Intent
            switch (requestCode) {
                case REQUESTCODE_SELECT_STOCK:
                    ArrayList<SelectStockBean> listStock = Parcels.unwrap(data
                            .getParcelableExtra(BaseSelectActivity.ARGUMENT_SELECT_LIST));

                    int createType = data.getIntExtra(BaseSelectActivity.ARGUMENT_CRATE_TYPE,
                            BaseSelectActivity.CRATE_TYPE_FAST);
                    if (null != listStock) {

                        setAddStockBack(listStock);
                        updatePieView();

                    }
                    break;
            }
        } else if (resultCode == 999) {
            finish();
        } else if (resultCode == RESULT_CANCELED) {
            if (firse) {
                finish();
            }
        }
    }

    private void updatePieView() {
        surpulsValue();
        setPieList(surValue);
        stockAdapter.setList(stockList);
        setFootData(surValue);

        isShowAverageButton();
        lvStock.invalidate();
    }

    private void isShowAverageButton() {
        if (null != stockList && stockList.size() > 0 && surValue != 0 && !isAdjustCombination) {
            btnAverage.setVisibility(View.VISIBLE);
        } else {
            btnAverage.setVisibility(View.INVISIBLE);
        }
    }

    private static final String TAG = PositionAdjustActivity.class.getSimpleName();

    private void setAddStockBack(List<SelectStockBean> listStock) {
        int i = 0;
        List<ConStockBean> tempList = new ArrayList<ConStockBean>();
        for (SelectStockBean selectBean : listStock) {
            ConStockBean csBean = selectBean.parseStock();
            csBean.setPercent(0);
            // csBean.setPercent(0);
            csBean.setDutyColor(ColorTemplate.getDefaultColor(i));
            if (stockList.contains(csBean)) {
                int index = stockList.indexOf(csBean);
                csBean.setPercent(stockList.get(index).getPercent());

            }

            tempList.add(csBean);
            i++;
        }
        stockList.clear();
        stockList.addAll(tempList);

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();
        List<SubmitSymbol> submitList = generateSymbols();
        isModifyPosition = submitList.size() > 0;
        if (isModifyPosition) {
            showAlertDialog();
        } else {
            finish();
        }
    }

    private void showAlertDialog() {

        MAlertDialog builder = PromptManager.getAlertDialog(this);

        builder.setMessage(R.string.adjust_back_message)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                finish();

            }
        });


        builder.show();

    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_option_adjust);


    @Override
    public void updateSurpus(int value) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getPageStatisticsStringId() {
        if(isAdjustCombination){
            return R.string.statistics_position_adjust;
        }else{
            return R.string.statistics_position_configure;
        }
    }
}
