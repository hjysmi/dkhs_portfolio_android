package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.ui.ITouchListener;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.fragment.FragmentMarkerCenter.RequestMarketTask;
import com.dkhs.portfolio.ui.widget.chart.StickChart;
import com.dkhs.portfolio.ui.widget.chart.StickEntity;
import com.dkhs.portfolio.ui.widget.kline.KChartsView;
import com.dkhs.portfolio.ui.widget.kline.KChartsView.DisplayDataChangeListener;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.kline.KChartsView;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

public class KChartsFragment extends Fragment {
    public static final int TYPE_CHART_DAY = 1;
    public static final int TYPE_CHART_WEEK = 2;
    public static final int TYPE_CHART_MONTH = 3;

    private KChartsView mMyChartsView;
    private StickChart mVolumnChartView; // 成交量饼图
    private Integer type = TYPE_CHART_DAY; // 类型，日K线，周k先，月k线
    private String mStockCode; // 股票code
    private QuotesEngineImpl mQuotesDataEngine;

    private ImageButton mLargerButton;
    private ImageButton mSmallerButton;

    public static final boolean testInterface = false; // 测试，使用本地数据
    private boolean first = true;
    private Timer mMarketTimer;
	private static final long mPollRequestTime = 1000 * 5;
	List<OHLCEntity> ohlcs;
	private boolean having = true;
	private String symbolType;
	private RelativeLayout pb;
    public static KChartsFragment getKChartFragment(Integer type, String stockcode,String symbolType) {
        KChartsFragment fg = new KChartsFragment();
        fg.setType(type);
        fg.setStockCode(stockcode);
        fg.setSymbolType(symbolType);
        return fg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mQuotesDataEngine == null) {
            mQuotesDataEngine = new QuotesEngineImpl();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kcharts, null);
        mMyChartsView = (KChartsView) view.findViewById(R.id.my_charts_view);
        mVolumnChartView = (StickChart) view.findViewById(R.id.chart_volumn);
        pb = (RelativeLayout) view.findViewById(android.R.id.progress);
        if(!(null != ohlcs && ohlcs.size() > 0)){
            pb.setVisibility(View.VISIBLE);
        }
        initChartView();
        initVloumnChartView();
        mMyChartsView.setStick(mVolumnChartView);
        mMyChartsView.setContext(getActivity());
        mMyChartsView.setmStockBean(((StockQuotesActivity)getActivity()).getmStockBean());
        mMyChartsView.setType(type);
        mLargerButton = (ImageButton) view.findViewById(R.id.btn_large);
        //mLargerButton.setVisibility(View.INVISIBLE);
       
        mLargerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                large(v);
            }
        });

        mSmallerButton = (ImageButton) view.findViewById(R.id.btn_small);
        mSmallerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                small(v);
            }
        });
        if(having){
	        //mSmallerButton.setClickable(false);
	    	mSmallerButton.setSelected(true);
	        //mLargerButton.setClickable(false);
	    	mLargerButton.setSelected(true);
        }
        return view;
    }

    private DisplayDataChangeListener mDisplayDataChangeListener = new DisplayDataChangeListener() {

        @Override
        public void onDisplayDataChange(List<OHLCEntity> entitys) {
            refreshVolumnCharts();
        }

    };
    private ITouchListener mTouchListener;

    public void setITouchListener(ITouchListener touchListener) {
        this.mTouchListener = touchListener;
    }

    private void initChartView() {
        mMyChartsView.setAxisColor(Color.LTGRAY);
        mMyChartsView.setLongiLatitudeColor(Color.LTGRAY);
        mMyChartsView.setBorderColor(Color.LTGRAY);
        mMyChartsView.setDisplayLongitude(false);
        mMyChartsView.setDisplayAxisXTitle(false);
        mMyChartsView.setDisplayChangeListener(mDisplayDataChangeListener);
        mMyChartsView.setITouchListener(mTouchListener);
        mMyChartsView.setSymbolType(getSymbolType());
        mMyChartsView.setSymbol(mStockCode);
        // mMyChartsView.setOnTouchListener(new OnChartListener());
    }

    /*
     * class OnChartListener implements OnTouchListener{
     * boolean g = true;
     * 
     * @Override
     * public boolean onTouch(View v, MotionEvent event) {
     * // TODO Auto-generated method stub
     * if(event.getAction() == MotionEvent.ACTION_DOWN){
     * g = true;
     * }
     * if(event.getAction() == MotionEvent.ACTION_MOVE){
     * if(g){
     * event.setLocation(0, 0);
     * mVolumnChartView.onSet(event);
     * }
     * g = false;
     * }
     * if(event.getAction() == MotionEvent.ACTION_UP && g){
     * mVolumnChartView.onSet(event);
     * }
     * return false;
     * }
     * 
     * }
     */
    private void initVloumnChartView() {
        mVolumnChartView.setAxisXColor(Color.LTGRAY);
        mVolumnChartView.setAxisYColor(Color.LTGRAY);
        mVolumnChartView.setLatitudeColor(Color.GRAY);
        mVolumnChartView.setLongitudeColor(Color.GRAY);
        mVolumnChartView.setBorderColor(Color.LTGRAY);
        mVolumnChartView.setLongtitudeFontColor(Color.GRAY);
        mVolumnChartView.setLatitudeFontColor(Color.GRAY);
        // mVolumnChartView.setStickFillColor(getResources().getColor(R.drawable.yellow));
        mVolumnChartView.setAxisMarginTop(0);
        mVolumnChartView.setAxisMarginRight(1);

        // 最大显示足数
        // mVolumnChartView.setMaxStickDataNum(52);
        // 最大纬线数
        mVolumnChartView.setLatitudeNum(1);
        // 最大经线数
        // mVolumnChartView.setLongtitudeNum(3);
        // 最大价格
        mVolumnChartView.setMaxValue(0);
        // 最小价格
        // mVolumnChartView.setMinValue(100);

        mVolumnChartView.setDisplayAxisXTitle(true);
        mVolumnChartView.setDisplayAxisYTitle(true);
        mVolumnChartView.setDisplayLatitude(false);
        mVolumnChartView.setDisplayLongitude(false);
        mVolumnChartView.setBackgroudColor(Color.WHITE);
    }

    /**
     * 刷新k线图控件
     * 
     * @param ohlc
     */
    private void refreshChartsView(List<OHLCEntity> ohlc) {
        try {
			mMyChartsView.setOHLCData(ohlc);
			mMyChartsView.setShowLowerChartTabs(false);
			mMyChartsView.setLowerChartTabTitles(new String[] { "MACD", "KDJ" });
			mMyChartsView.postInvalidate();

			// 刷新成交量
			refreshVolumnCharts();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void refreshVolumnCharts() {
        try {
            List<StickEntity> volumns = getVolumnFromOHLC(mMyChartsView.getDisplayOHLCEntitys());
            if (volumns != null && volumns.size() > 0) {
                mVolumnChartView.setStickData(volumns);
                mVolumnChartView.postInvalidate();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取成交量
     * 
     * @param ohlc
     * @return
     */
    private List<StickEntity> getVolumnFromOHLC(List<OHLCEntity> ohlc) {
        try {
            if (ohlc == null || ohlc.size() == 0) {
                return null;
            }

            List<StickEntity> volumns = new ArrayList<StickEntity>();
            StickEntity temp = null;
            OHLCEntity entity = null;
            for (int i = ohlc.size() - 1; i >= 0; i--) {
                entity = ohlc.get(i);
                temp = new StickEntity(entity.getVolume(), 0, entity.getDate());
                temp.setUp(entity.isup());
                volumns.add(temp);
            }

            return volumns;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取k线数据
     * 
     * @return
     */
    private List<OHLCEntity> getOHLCDatas() {
        // 测试，使用写死数据
        if (testInterface) {
            return getTestDatas();
        }

        // 测试
        // mStockCode = "SZ002252";
        // 获取K线类型，日，周，月
        try {
            String mtype = getKLineType();
            mQuotesDataEngine.queryKLine(mtype, mStockCode,"0", mKlineHttpListener);
            if(first){
            	//PromptManager.showProgressDialog(getActivity(), "", true);
            	first = false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取K线类型，日，周，月
     * 
     * @return
     */
    private String getKLineType() {
        switch (type) {
            case TYPE_CHART_DAY:
                return "d";
            case TYPE_CHART_WEEK:
                return "w";
            case TYPE_CHART_MONTH:
                return "m";
            default:
                break;
        }
        return "d";
    }

    private IHttpListener mKlineHttpListener = new BasicHttpListener() {

        @Override
        public void onSuccess(String result) {
            try {
                pb.setVisibility(View.GONE);
                List<OHLCEntity> ohlc = getOHLCDatasFromJson(result);
                 ohlcs = new ArrayList<OHLCEntity>();
                for(int i = ohlc.size() -1; i >= 0; i--){
                	ohlcs.add(ohlc.get(i));
                }
                refreshChartsView(ohlcs);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //PromptManager.closeProgressDialog();
        }

        public void onFailure(int errCode, String errMsg) {
            // Toast.makeText(getActivity(), "数据获取失败！", Toast.LENGTH_LONG).show();
        	//PromptManager.closeProgressDialog();
            pb.setVisibility(View.GONE);
        };
    };

    /**
     * 从json中解析k线数据
     * 
     * @param jsonObject
     * @return
     */
    private List<OHLCEntity> getOHLCDatasFromJson(String jsonObject) {
        List<OHLCEntity> entitys = new ArrayList<OHLCEntity>();
        if (jsonObject == null || jsonObject.trim().length() == 0) {
            return entitys;
        }

        try {
            JSONArray ja = new JSONArray(jsonObject);
            int len = ja.length();
            
            if (len > 0) {
                JSONObject jo = null;
                OHLCEntity ohlc = null;
                for (int i = len - 1; i >= 0; i--) {
                    jo = ja.getJSONObject(i);
                    if (jo != null) {
                        ohlc = new OHLCEntity();
                        if (jo.has("open"))
                            ohlc.setOpen(jo.getDouble("open"));
                        if (jo.has("high"))
                            ohlc.setHigh(jo.getDouble("high"));
                        if (jo.has("low"))
                            ohlc.setLow(jo.getDouble("low"));
                        if (jo.has("close"))
                            ohlc.setClose(jo.getDouble("close"));
                        if (jo.has("tradedate"))
                            ohlc.setDate(jo.getString("tradedate"));
                        if (jo.has("volume"))
                            ohlc.setVolume(jo.getDouble("volume"));
                        if (jo.has("change"))
                            ohlc.setChange(jo.getDouble("change"));
                        if (jo.has("percentage"))
                            ohlc.setPercentage(jo.getDouble("percentage"));
                        entitys.add(ohlc);
                    }
                }
            }
            if(len > 50 && having){
            	//mSmallerButton.setClickable(true);
            	mSmallerButton.setSelected(false);
            	having = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entitys;
    }

    /**
     * 测试数据
     * 
     * @return
     */
    private List<OHLCEntity> getTestDatas() {
        List<OHLCEntity> ohlc = new ArrayList<OHLCEntity>();
        ohlc.add(new OHLCEntity(100, 246, 248, 235, 235, "20110825"));
        ohlc.add(new OHLCEntity(2312, 240, 242, 236, 242, "20110824"));
        ohlc.add(new OHLCEntity(111, 236, 240, 235, 240, "20110823"));
        ohlc.add(new OHLCEntity(111, 232, 236, 231, 236, "20110822"));
        ohlc.add(new OHLCEntity(111, 240, 240, 235, 235, "20110819"));
        ohlc.add(new OHLCEntity(111, 240, 241, 239, 240, "20110818"));
        ohlc.add(new OHLCEntity(111, 242, 243, 240, 240, "20110817"));
        ohlc.add(new OHLCEntity(111, 239, 242, 238, 242, "20110816"));
        ohlc.add(new OHLCEntity(111, 239, 240, 238, 239, "20110815"));
        ohlc.add(new OHLCEntity(111, 230, 238, 230, 238, "20110812"));
        ohlc.add(new OHLCEntity(111, 236, 237, 234, 234, "20110811"));
        ohlc.add(new OHLCEntity(111, 226, 233, 223, 232, "20110810"));
        ohlc.add(new OHLCEntity(111, 239, 241, 229, 232, "20110809"));
        ohlc.add(new OHLCEntity(111, 242, 244, 240, 242, "20110808"));
        ohlc.add(new OHLCEntity(111, 248, 249, 247, 248, "20110805"));
        ohlc.add(new OHLCEntity(111, 245, 248, 245, 247, "20110804"));
        ohlc.add(new OHLCEntity(111, 249, 249, 245, 247, "20110803"));
        ohlc.add(new OHLCEntity(111, 249, 251, 248, 250, "20110802"));
        ohlc.add(new OHLCEntity(111, 250, 252, 248, 250, "20110801"));
        ohlc.add(new OHLCEntity(111, 250, 251, 248, 250, "20110729"));
        ohlc.add(new OHLCEntity(111, 249, 252, 248, 252, "20110728"));
        ohlc.add(new OHLCEntity(111, 248, 250, 247, 250, "20110727"));
        ohlc.add(new OHLCEntity(111, 256, 256, 248, 248, "20110726"));
        ohlc.add(new OHLCEntity(111, 257, 258, 256, 257, "20110725"));
        ohlc.add(new OHLCEntity(111, 259, 260, 256, 256, "20110722"));
        ohlc.add(new OHLCEntity(111, 261, 261, 257, 259, "20110721"));
        ohlc.add(new OHLCEntity(111, 260, 260, 259, 259, "20110720"));
        ohlc.add(new OHLCEntity(111, 262, 262, 260, 261, "20110719"));
        ohlc.add(new OHLCEntity(111, 260, 262, 259, 262, "20110718"));
        ohlc.add(new OHLCEntity(111, 259, 261, 258, 261, "20110715"));
        ohlc.add(new OHLCEntity(111, 255, 259, 255, 259, "20110714"));
        ohlc.add(new OHLCEntity(111, 258, 258, 255, 255, "20110713"));
        ohlc.add(new OHLCEntity(111, 258, 260, 258, 260, "20110712"));
        ohlc.add(new OHLCEntity(111, 259, 260, 258, 259, "20110711"));
        ohlc.add(new OHLCEntity(111, 261, 262, 259, 259, "20110708"));
        ohlc.add(new OHLCEntity(111, 261, 261, 258, 261, "20110707"));
        ohlc.add(new OHLCEntity(111, 261, 261, 259, 261, "20110706"));
        ohlc.add(new OHLCEntity(111, 257, 261, 257, 261, "20110705"));
        ohlc.add(new OHLCEntity(111, 256, 257, 255, 255, "20110704"));
        ohlc.add(new OHLCEntity(111, 253, 257, 253, 256, "20110701"));
        ohlc.add(new OHLCEntity(111, 255, 255, 252, 252, "20110630"));
        ohlc.add(new OHLCEntity(111, 256, 256, 253, 255, "20110629"));
        ohlc.add(new OHLCEntity(111, 254, 256, 254, 255, "20110628"));
        ohlc.add(new OHLCEntity(111, 247, 256, 247, 254, "20110627"));
        ohlc.add(new OHLCEntity(1411, 244, 249, 243, 248, "20110624"));
        ohlc.add(new OHLCEntity(1311, 244, 245, 243, 244, "20110623"));
        ohlc.add(new OHLCEntity(111, 242, 244, 241, 244, "20110622"));
        ohlc.add(new OHLCEntity(111, 243, 243, 241, 242, "20110621"));
        ohlc.add(new OHLCEntity(111, 246, 247, 244, 244, "20110620"));
        ohlc.add(new OHLCEntity(2511, 248, 249, 246, 246, "20110617"));
        ohlc.add(new OHLCEntity(2211, 251, 253, 250, 250, "20110616"));
        ohlc.add(new OHLCEntity(111, 249, 253, 249, 253, "20110615"));
        ohlc.add(new OHLCEntity(111, 248, 250, 246, 250, "20110614"));
        ohlc.add(new OHLCEntity(111, 249, 250, 247, 250, "20110613"));
        ohlc.add(new OHLCEntity(111, 254, 254, 250, 250, "20110610"));
        ohlc.add(new OHLCEntity(111, 254, 255, 251, 255, "20110609"));
        ohlc.add(new OHLCEntity(1551, 252, 254, 251, 254, "20110608"));
        ohlc.add(new OHLCEntity(111, 250, 253, 250, 252, "20110607"));
        ohlc.add(new OHLCEntity(111, 251, 252, 247, 250, "20110603"));
        ohlc.add(new OHLCEntity(111, 253, 254, 252, 254, "20110602"));
        ohlc.add(new OHLCEntity(111, 250, 254, 250, 254, "20110601"));
        ohlc.add(new OHLCEntity(111, 250, 252, 248, 250, "20110531"));
        ohlc.add(new OHLCEntity(111, 253, 254, 250, 251, "20110530"));
        ohlc.add(new OHLCEntity(111, 255, 256, 253, 253, "20110527"));
        ohlc.add(new OHLCEntity(161, 256, 257, 253, 254, "20110526"));
        ohlc.add(new OHLCEntity(111, 256, 257, 254, 256, "20110525"));
        ohlc.add(new OHLCEntity(111, 265, 265, 257, 257, "20110524"));
        ohlc.add(new OHLCEntity(111, 265, 266, 265, 265, "20110523"));
        ohlc.add(new OHLCEntity(711, 267, 268, 265, 266, "20110520"));
        ohlc.add(new OHLCEntity(111, 264, 267, 264, 267, "20110519"));
        ohlc.add(new OHLCEntity(61, 264, 266, 262, 265, "20110518"));
        ohlc.add(new OHLCEntity(111, 266, 267, 264, 264, "20110517"));
        ohlc.add(new OHLCEntity(111, 264, 267, 263, 267, "20110516"));
        ohlc.add(new OHLCEntity(671, 266, 267, 264, 264, "20110513"));
        ohlc.add(new OHLCEntity(111, 269, 269, 266, 268, "20110512"));
        ohlc.add(new OHLCEntity(181, 267, 269, 266, 269, "20110511"));
        ohlc.add(new OHLCEntity(111, 266, 268, 266, 267, "20110510"));
        ohlc.add(new OHLCEntity(181, 264, 268, 263, 266, "20110509"));
        ohlc.add(new OHLCEntity(111, 265, 268, 265, 267, "20110506"));
        ohlc.add(new OHLCEntity(111, 271, 271, 266, 266, "20110505"));
        ohlc.add(new OHLCEntity(111, 271, 273, 269, 273, "20110504"));
        ohlc.add(new OHLCEntity(116, 268, 271, 267, 271, "20110503"));
        ohlc.add(new OHLCEntity(111, 273, 275, 268, 268, "20110429"));
        ohlc.add(new OHLCEntity(111, 274, 276, 270, 272, "20110428"));
        ohlc.add(new OHLCEntity(114, 275, 277, 273, 273, "20110427"));
        ohlc.add(new OHLCEntity(111, 280, 280, 276, 276, "20110426"));
        ohlc.add(new OHLCEntity(131, 282, 283, 280, 281, "20110425"));
        ohlc.add(new OHLCEntity(111, 282, 283, 281, 282, "20110422"));
        ohlc.add(new OHLCEntity(111, 280, 281, 279, 280, "20110421"));
        ohlc.add(new OHLCEntity(131, 283, 283, 279, 279, "20110420"));
        ohlc.add(new OHLCEntity(111, 284, 286, 283, 285, "20110419"));
        ohlc.add(new OHLCEntity(111, 283, 286, 282, 285, "20110418"));
        ohlc.add(new OHLCEntity(111, 285, 285, 283, 284, "20110415"));
        ohlc.add(new OHLCEntity(121, 280, 285, 279, 285, "20110414"));
        ohlc.add(new OHLCEntity(111, 281, 283, 280, 282, "20110413"));
        ohlc.add(new OHLCEntity(171, 283, 286, 282, 282, "20110412"));
        ohlc.add(new OHLCEntity(111, 280, 283, 279, 283, "20110411"));
        ohlc.add(new OHLCEntity(161, 280, 281, 279, 280, "20110408"));
        ohlc.add(new OHLCEntity(111, 276, 280, 276, 280, "20110407"));
        ohlc.add(new OHLCEntity(111, 273, 276, 272, 276, "20110406"));
        ohlc.add(new OHLCEntity(151, 275, 276, 271, 272, "20110404"));
        ohlc.add(new OHLCEntity(114, 275, 276, 273, 275, "20110401"));
        return ohlc;
    }

    public void large(View view) {
        mMyChartsView.makeLager();
        changeButtonState();
    }

    public void small(View view) {
        mMyChartsView.makeSmaller();
        changeButtonState();
    }

    /**
     * 改变增大，缩小按钮状态
     */
    private void changeButtonState() {
        if (mMyChartsView.isSmallest()) {
            //mLargerButton.setVisibility(View.INVISIBLE);
        	//mLargerButton.setClickable(false);
        	mLargerButton.setSelected(true);
        } else {
            //mLargerButton.setVisibility(View.VISIBLE);
        	//mLargerButton.setClickable(true);
        	mLargerButton.setSelected(false);
        }
        if (mMyChartsView.isLargest()) {
            //mSmallerButton.setVisibility(View.INVISIBLE);
        	//mSmallerButton.setClickable(false);
        	mSmallerButton.setSelected(true);
        } else {
            //mSmallerButton.setVisibility(View.VISIBLE);
        	//mSmallerButton.setClickable(true);
        	mSmallerButton.setSelected(false);
        }
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStockCode() {
        return mStockCode;
    }

    public void setStockCode(String mStockCode) {
        this.mStockCode = mStockCode;
    }
    
    public String getSymbolType() {
		return symbolType;
	}

	public void setSymbolType(String symbolType) {
		this.symbolType = symbolType;
	}

	@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
            // TODO Auto-generated method stub
            if (isVisibleToUser) {
                    //fragment可见时加载数据
            	mQuotesDataEngine = new QuotesEngineImpl();
            	List<OHLCEntity> ohlc = getOHLCDatas();
            	if (mMarketTimer == null) {
                    mMarketTimer = new Timer(true);
                    mMarketTimer.schedule(new RequestMarketTask(), mPollRequestTime, mPollRequestTime);
                }
    } else {
        //不可见时不执行操作
        if (mMarketTimer != null) {
            mMarketTimer.cancel();
            mMarketTimer = null;
        }
    }
            super.setUserVisibleHint(isVisibleToUser);
    }
    @Override
    public void onResume() {

        super.onResume();

        MobclickAgent.onPageStart(mPageName);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mMarketTimer != null) {
            mMarketTimer.cancel();
            mMarketTimer = null;
        }

    }

    public class RequestMarketTask extends TimerTask {

        @Override
        public void run() {
        	StockQuotesBean m =((StockQuotesActivity) getActivity()).getmStockQuotesBean();
        	if(null != m && UIUtils.roundAble(m)){
        		mMarketTimer.cancel();
            }
        	String mtype = getKLineType();
            mQuotesDataEngine.queryKLine(mtype, mStockCode,"1", mKlineHttpListenerFlush);
        }
    }
    private IHttpListener mKlineHttpListenerFlush = new BasicHttpListener() {

        @Override
        public void onSuccess(String result) {
            try {
                List<OHLCEntity> ohlc = getOHLCDatasFromJson(result);
                if(null == ohlcs || ohlcs.size() == 0){
                	String mtype = getKLineType();
                    mQuotesDataEngine.queryKLine(mtype, mStockCode,"0", mKlineHttpListener);
                }else{
	               if(ohlc.size() > 0){
	            	   ohlcs.add(0, ohlc.get(0));
	            	   ohlcs.remove(1);
	               }
	                refreshChartsView(ohlcs);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //PromptManager.closeProgressDialog();
        }

        public void onFailure(int errCode, String errMsg) {
            // Toast.makeText(getActivity(), "数据获取失败！", Toast.LENGTH_LONG).show();
        	//PromptManager.closeProgressDialog();
            pb.setVisibility(View.GONE);
        };
    };
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_stock_Kline);
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageEnd(mPageName);
	}
}
