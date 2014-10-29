package com.dkhs.portfolio.ui.widget.kline;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.R.color;
import com.dkhs.portfolio.ui.ITouchListener;
import com.dkhs.portfolio.ui.widget.chart.StickChart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;


public class KChartsView extends GridChart implements GridChart.OnTabClickListener {

	/** 触摸模式 */
	private static int TOUCH_MODE;
	private final static int NONE = 0;
	private final static int DOWN = 1;
	private final static int MOVE = 2;
	private final static int ZOOM = 3;

	/** 默认Y轴字体颜色 **/
	private static final int DEFAULT_AXIS_Y_TITLE_COLOR = Color.GRAY;

	/** 默认X轴字体颜色 **/
	private static final int DEFAULT_AXIS_X_TITLE_COLOR = Color.GRAY;

	/** 显示的最小Candle数 */
	private final static int MIN_CANDLE_NUM = 30;

	/** 默认显示的Candle数 */
	private final static int DEFAULT_CANDLE_NUM = 60;
	/**显示最多的candle数*/
	private final static int MAX_CANDLE_NUM = 300;
	/** 最小可识别的移动距离 */
	private final static int MIN_MOVE_DISTANCE = 15;

	/** Candle宽度 */
	private double mCandleWidth;

	/** 触摸点 */
	private float mStartX = 6;
	private float mStartY;

	/** OHLC数据 */
	private List<OHLCEntity> mOHLCData;

	/** 显示的OHLC数据起始位置 */
	private int mDataStartIndext;

	/** 显示的OHLC数据个数 */
	private int mShowDataNum;

	/** 是否显示蜡烛详情 */
	private boolean showDetails;

	/** 当前数据的最大最小值 */
	private double mMaxPrice;
	private double mMinPrice;

	/** MA数据 */
	private List<MALineEntity> MALineData;

	private String mTabTitle;
	private StickChart mVolumnChartView; 
	// 下部表的数据
	MACDEntity mMACDData;
	KDJEntity mKDJData;
	RSIEntity mRSIData;
	private MotionEvent e;
	private boolean ismove = true;
	private int zoomNum = 5;
	private long currentTime;
	private DisplayDataChangeListener mDisplayChangeListener; //显示数据变化监听
	private boolean go = true;
	private boolean firsttime = true;
	public KChartsView(Context context) {
		super(context);
		init();
	}
	
	public KChartsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public KChartsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		super.setOnTabClickListener(this);
		mShowDataNum = DEFAULT_CANDLE_NUM;
		mDataStartIndext = 0;
		showDetails = false;
		mMaxPrice = -1;
		mMinPrice = -1;
//		mTabTitle = null;

		mOHLCData = new ArrayList<OHLCEntity>();
		mMACDData = new MACDEntity(null);
		mKDJData = new KDJEntity(null);
		mRSIData = new RSIEntity(null);
		/*if(mOHLCData.size() >= 30){
			mStartX = getWidth() - 6;
		}else{
			mStartX =(int) (getWidth() - 6 - ( 30 - mOHLCData.size()) * (mCandleWidth *3));
		}*/
	}

	/*@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		super.dispatchTouchEvent(event);
		return true;
	}*/
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mOHLCData == null || mOHLCData.size() <= 0) {
			return;
		}
		try {
			drawUpperRegion(canvas);
			drawLowerRegion(canvas);
			drawTitles(canvas);
			drawCandleDetails(canvas);
			if(firsttime){
				showDetails = false;
				go = false;
				//mStartX = getWidth() - 6;
				
				postInvalidate();
				firsttime = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public DisplayDataChangeListener getDisplayChangeListener() {
		return mDisplayChangeListener;
	}

	public void setDisplayChangeListener(
			DisplayDataChangeListener mDisplayChangeListener) {
		this.mDisplayChangeListener = mDisplayChangeListener;
	}

	private void drawCandleDetails(Canvas canvas) {
		if (showDetails) {
			int addNum = 30 - mOHLCData.size();
			float width = getWidth();
			float left = 3.0f;
			float top = (float) (5.0 + DEFAULT_AXIS_TITLE_SIZE);
			float right = 3.0f + 9 * DEFAULT_AXIS_TITLE_SIZE;
			float bottom = 8.0f + 9 * DEFAULT_AXIS_TITLE_SIZE;
			
			if(mOHLCData.size() < 30){
				if (mStartX - addNum * (mCandleWidth + 3) < width / 2.0f) {
					right = width - 12.0f;
					left = width - 12.0f - 9 * DEFAULT_AXIS_TITLE_SIZE;
				}
			}else{
				if (mStartX < width / 2.0f) {
					right = width - 12.0f;
					left = width - 12.0f - 9 * DEFAULT_AXIS_TITLE_SIZE;
				}
			}
			int selectIndext = (int) ((width - 2.0f - mStartX) / (mCandleWidth + 3) + mDataStartIndext);
			
			if(mOHLCData.size() < 30){
				selectIndext = (int) ((width - 2.0f - mStartX - addNum * (mCandleWidth + 3)) / (mCandleWidth + 3) + mDataStartIndext);
			}
			double rate = (getUperChartHeight() - 2) / (mMaxPrice - mMinPrice);
			float cl = (float) ((mMaxPrice - mOHLCData.get(selectIndext).getClose()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
			float startX = (float) (width - 3 - (mCandleWidth + 3) * (selectIndext - mDataStartIndext) - (mCandleWidth - 1) / 2);
			if(mOHLCData.size() < 30){
				startX = (float) (width - 3 - (mCandleWidth + 3) * (selectIndext - mDataStartIndext) - (mCandleWidth - 1) / 2 - addNum * (mCandleWidth + 3));
			}
			// 绘制点击线条及详情区域
			Paint paint = new Paint();
			paint.setColor(Color.LTGRAY);
			paint.setAntiAlias(true);
			//paint.setAlpha(150);
			e.setLocation(startX, startX);
			mVolumnChartView.onSet(e,ismove,mDataStartIndext);
			canvas.drawLine(startX, 2.0f + DEFAULT_AXIS_TITLE_SIZE, startX, UPER_CHART_BOTTOM,
					paint);
			canvas.drawLine(0, cl, this.getWidth(), cl, paint);//十字光标横线
			if(mOHLCData.size() < 30){
				canvas.drawLine((int)(mStartX - addNum * (mCandleWidth + 3)), getHeight() - 2.0f, (int)(mStartX - addNum * (mCandleWidth + 3)), LOWER_CHART_TOP, paint);
			}else{
				canvas.drawLine(mStartX, getHeight() - 2.0f, mStartX, LOWER_CHART_TOP, paint);
			}
			
			Rect rect = new Rect((int)left, (int)top, (int)(right+4), (int)(bottom+4));
	        //由于图片的实际尺寸比显示出来的图像要大一些，因此需要适当更改下大小，以达到较好的效果     
			Paint paint1 = new Paint();
			paint1.setColor(Color.WHITE);
			paint1.setAntiAlias(true);//去除锯齿。     
	         paint1.setShadowLayer(5f, 5.0f, 5.0f, Color.BLACK); //设置阴影层，这是关键。     
	         paint1.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
	        RectF rectF = new RectF(rect);    
	        paint.setColor(Color.WHITE);
	        canvas.drawRoundRect(rectF, 10f, 10f, paint1);
			canvas.drawRect(left, top, right, bottom, paint);

			Paint borderPaint = new Paint();
			borderPaint.setColor(Color.LTGRAY);
			borderPaint.setStrokeWidth(2);
			canvas.drawLine(left, top, left, bottom, borderPaint);
			canvas.drawLine(left, top, right, top, borderPaint);
			//canvas.drawLine(right, bottom, right, top, borderPaint);
			//canvas.drawLine(right, bottom, left, bottom, borderPaint);

			// 绘制详情文字
			Paint textPaint = new Paint();
			textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
			//textPaint.setColor(Color.DKGRAY);
			//textPaint.setFakeBoldText(true);
			textPaint.setAntiAlias(true);
			canvas.drawText("日期: " + mOHLCData.get(selectIndext).getDate(), left + 1, top
					+ DEFAULT_AXIS_TITLE_SIZE, textPaint);

			canvas.drawText("开盘:", left + 1, top + DEFAULT_AXIS_TITLE_SIZE * 2.0f, textPaint);
			double open = mOHLCData.get(selectIndext).getOpen();
			try {
				double ysdclose = mOHLCData.get(selectIndext + 1).getClose();
				if (open >= ysdclose) {
					//textPaint.setColor(Color.DKGRAY);
				} else {
					//textPaint.setColor(Color.DKGRAY);
				}
				canvas.drawText(new DecimalFormat("0.00").format(open), left + 1
						+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + DEFAULT_AXIS_TITLE_SIZE * 2.0f,
						textPaint);
			} catch (Exception e) {
				canvas.drawText(new DecimalFormat("0.00").format(open), left + 1
						+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + DEFAULT_AXIS_TITLE_SIZE * 2.0f,
						textPaint);
			}

			//textPaint.setColor(Color.DKGRAY);
			canvas.drawText("最高:", left + 1, top + DEFAULT_AXIS_TITLE_SIZE * 3.0f, textPaint);
			double high = mOHLCData.get(selectIndext).getHigh();
			if (open < high) {
				//textPaint.setColor(Color.DKGRAY);
			} else {
				//textPaint.setColor(Color.DKGRAY);
			}
			canvas.drawText(new DecimalFormat("0.00").format(high), left + 1
					+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + DEFAULT_AXIS_TITLE_SIZE * 3.0f,
					textPaint);

			//textPaint.setColor(Color.DKGRAY);
			canvas.drawText("最低:", left + 1, top + DEFAULT_AXIS_TITLE_SIZE * 4.0f, textPaint);
			double low = mOHLCData.get(selectIndext).getLow();
			try {
				double yesterday = (mOHLCData.get(selectIndext + 1).getLow() + mOHLCData.get(
						selectIndext + 1).getHigh()) / 2.0f;
				if (yesterday <= low) {
					//textPaint.setColor(Color.DKGRAY);
				} else {
					//textPaint.setColor(Color.DKGRAY);
				}
			} catch (Exception e) {

			}
			canvas.drawText(new DecimalFormat("0.00").format(low), left + 1
					+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + DEFAULT_AXIS_TITLE_SIZE * 4.0f,
					textPaint);

			//textPaint.setColor(Color.DKGRAY);
			canvas.drawText("收盘:", left + 1, top + DEFAULT_AXIS_TITLE_SIZE * 5.0f, textPaint);
			double close = mOHLCData.get(selectIndext).getClose();
			try {
				double yesdopen = (mOHLCData.get(selectIndext + 1).getLow() + mOHLCData.get(
						selectIndext + 1).getHigh()) / 2.0f;
				if (yesdopen <= close) {
					//textPaint.setColor(Color.DKGRAY);
				} else {
					//textPaint.setColor(Color.DKGRAY);
				}
			} catch (Exception e) {

			}
			canvas.drawText(new DecimalFormat("0.00").format(close), left + 1
					+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + DEFAULT_AXIS_TITLE_SIZE * 5.0f,
					textPaint);
			
			//textPaint.setColor(Color.DKGRAY);
			canvas.drawText("涨跌:", left + 1, top + DEFAULT_AXIS_TITLE_SIZE * 6.0f, textPaint);
			try {
				double yesdclose = mOHLCData.get(selectIndext + 1).getClose();
				double priceRate = (close - yesdclose);
				if (priceRate >= 0) {
					//textPaint.setColor(Color.DKGRAY);
				} else {
					//textPaint.setColor(Color.DKGRAY);
				}
				canvas.drawText(new DecimalFormat("0.00").format(priceRate), left + 1
						+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + DEFAULT_AXIS_TITLE_SIZE * 6.0f,
						textPaint);
			} catch (Exception e) {
				/*canvas.drawText("--", left + 1 + DEFAULT_AXIS_TITLE_SIZE * 3.5f, top
						+ DEFAULT_AXIS_TITLE_SIZE * 6.0f, textPaint);*/
			}
			
			//textPaint.setColor(Color.DKGRAY);
			canvas.drawText("涨幅:", left + 1, top + DEFAULT_AXIS_TITLE_SIZE * 7.0f, textPaint);
			try {
				double yesdclose = mOHLCData.get(selectIndext + 1).getClose();
				double priceRate = (close - yesdclose) / yesdclose;
				if (priceRate >= 0) {
					//textPaint.setColor(Color.DKGRAY);
				} else {
					//textPaint.setColor(Color.DKGRAY);
				}
				canvas.drawText(new DecimalFormat("0.00%").format(priceRate), left + 1
						+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + DEFAULT_AXIS_TITLE_SIZE * 7.0f,
						textPaint);
			} catch (Exception e) {
				canvas.drawText("--", left + 1 + DEFAULT_AXIS_TITLE_SIZE * 3.5f, top
						+ DEFAULT_AXIS_TITLE_SIZE * 6.0f, textPaint);
			}
			
			//textPaint.setColor(Color.DKGRAY);
			canvas.drawText("成交量:", left + 1, top + DEFAULT_AXIS_TITLE_SIZE * 8.0f, textPaint);
			try {
				double volume = mOHLCData.get(selectIndext).getVolume()/100;
				if (volume < 10000) {
					canvas.drawText(new DecimalFormat("0.00").format(volume), left + 1
							+ DEFAULT_AXIS_TITLE_SIZE * 3.5f, top + DEFAULT_AXIS_TITLE_SIZE * 8.0f,
							textPaint);
				} else if(volume > 10000 && volume < 10000000){
					volume = volume/10000;
					canvas.drawText(new DecimalFormat("0.00").format(volume) + "万", left + 1
							+ DEFAULT_AXIS_TITLE_SIZE * 3.5f, top + DEFAULT_AXIS_TITLE_SIZE * 8.0f,
							textPaint);
				}else{
					volume = volume/10000000;
					canvas.drawText(new DecimalFormat("0.00").format(volume) + "千万", left + 1
							+ DEFAULT_AXIS_TITLE_SIZE * 3.5f, top + DEFAULT_AXIS_TITLE_SIZE * 8.0f,
							textPaint);
				}
				
			} catch (Exception e) {
				
			}
		}

	}

	private void drawTitles(Canvas canvas) {
		Paint textPaint = new Paint();
		textPaint.setColor(DEFAULT_AXIS_Y_TITLE_COLOR);
		textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

		if(isDisplayAxisYTitle()) {
			// Y轴Titles
			int len = getUpperLatitudeNum() +1;
			if(len > 0) {
				for(int i=0; i<len; i++) {
					canvas.drawText(
							new DecimalFormat("0.00").format(mMinPrice + (mMaxPrice - mMinPrice) / len * i), 1,
							UPER_CHART_BOTTOM - getLatitudeSpacing() * i + DEFAULT_AXIS_TITLE_SIZE, textPaint);
				}
			}
			canvas.drawText(new DecimalFormat("0.00").format(mMaxPrice), 1,
					DEFAULT_AXIS_TITLE_SIZE * 2 + 2, textPaint);
		}
		
		if(isDisplayAxisXTitle()) {
			// X轴Titles
			textPaint.setColor(DEFAULT_AXIS_X_TITLE_COLOR);
			canvas.drawText(mOHLCData.get(mDataStartIndext).getDate(), getWidth() - 4 - 4.5f
					* DEFAULT_AXIS_TITLE_SIZE, UPER_CHART_BOTTOM + DEFAULT_AXIS_TITLE_SIZE, textPaint);
			try {
				canvas.drawText(
						String.valueOf(mOHLCData.get(mDataStartIndext + mShowDataNum / 2).getDate()),
						getWidth() / 2 - 2.25f * DEFAULT_AXIS_TITLE_SIZE, UPER_CHART_BOTTOM
								+ DEFAULT_AXIS_TITLE_SIZE, textPaint);
				canvas.drawText(
						String.valueOf(mOHLCData.get(mDataStartIndext + mShowDataNum - 1).getDate()),
						2, UPER_CHART_BOTTOM + DEFAULT_AXIS_TITLE_SIZE, textPaint);
			} catch (Exception e) {

			}
		}
		
	}

	private void drawUpperRegion(Canvas canvas) {
		// 绘制蜡烛图
		Paint redPaint = new Paint();
		redPaint.setColor(Color.RED);
		Paint greenPaint = new Paint();
		greenPaint.setColor(getResources().getColor(R.color.dark_green));
		Paint grayPaint = new Paint();
		grayPaint.setColor(getResources().getColor(R.color.def_gray));
		int width = getWidth();
		mCandleWidth = (width - 4) / 10.0 * 10.0 / mShowDataNum - 3;
		double rate = (getUperChartHeight() - 2) / (mMaxPrice - mMinPrice);
		if(mOHLCData.size() >= 30){
			for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
				OHLCEntity entity = mOHLCData.get(mDataStartIndext + i);
				float open = (float) ((mMaxPrice - entity.getOpen()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
				float close = (float) ((mMaxPrice - entity.getClose()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
				float high = (float) ((mMaxPrice - entity.getHigh()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
				float low = (float) ((mMaxPrice - entity.getLow()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
	
				float left = (float) (width - 2 - mCandleWidth * (i + 1) - i*3);
				float right = (float) (width - 3 - mCandleWidth * i - i*3);
				float startX = (float) (width - 3 - mCandleWidth * i - (mCandleWidth - 1) / 2 - i * 3);
				if(entity.getOpen()==26.73 ){
					float a = open;
					Log.e("asa", a+"");
				}
				if (open < close) {
					canvas.drawRect(left, close, right, open, greenPaint);
					
					canvas.drawLine(startX, high, startX, low, greenPaint);
				} else if (open == close) {
					canvas.drawLine(left, open, right, open, grayPaint);
					canvas.drawLine(startX, high, startX, low, grayPaint);
				} else {
					canvas.drawRect(left, open, right, close, redPaint);
					canvas.drawLine(startX, high, startX, low, redPaint);
				}
	
			}
			// 绘制上部曲线图及上部分MA值
			float MATitleWidth = width / 10.0f * 10.0f / MALineData.size();
			String text = "";
			float wid = 0;
			for (int j = 0; j < MALineData.size(); j++) {
				MALineEntity lineEntity = MALineData.get(j);

				float startX = 0;
				float startY = 0;
				Paint paint = new Paint();
				paint.setColor(lineEntity.getLineColor());
				paint.setAntiAlias(true);
				paint.setTextSize( getResources().getDimensionPixelOffset(R.dimen.title_text_font));
				int selectIndext = (int) ((width - 2.0f - mStartX) / (mCandleWidth + 3) + mDataStartIndext);
				mVolumnChartView.setCurrentIndex(selectIndext);
				text = lineEntity.getTitle() + ":" + new DecimalFormat("0.00").format(lineEntity.getLineData().get(selectIndext - mDataStartIndext));
				Paint p= new Paint(); 
				Rect rect = new Rect();
				p.setTextSize( getResources().getDimensionPixelOffset(R.dimen.title_text_font));
				p.getTextBounds(text, 0, text.length(), rect); 
				if(j == 0){
					wid = 2;
				}else{
					wid = 2 + rect.width()*2/3 + wid + 5;
				}
				canvas.drawText(text, wid,DEFAULT_AXIS_TITLE_SIZE, paint);
				wid = wid +  2 + rect.width() ;
				for (int i = 0; i < mShowDataNum
						&& mDataStartIndext + i < lineEntity.getLineData().size(); i++) {
					if (i != 0) {
						canvas.drawLine(
								startX,
								startY + DEFAULT_AXIS_TITLE_SIZE + 4,
								(float) (width - 2 - (3 + mCandleWidth) * i - mCandleWidth * 0.5f),
								(float) ((mMaxPrice - lineEntity.getLineData()
										.get(mDataStartIndext + i)) * rate + DEFAULT_AXIS_TITLE_SIZE + 4),
								paint);
					}
					startX = (float) (width - 2 - (3 + mCandleWidth) * i - mCandleWidth * 0.5f);
					startY = (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate);
				}
			}
			if(null != e && !showDetails){
				e.setLocation(getWidth() - 6, 0);
				mVolumnChartView.onSet(e,ismove,mDataStartIndext);
			}
		}else{
			int addNum = 30 - mOHLCData.size();
			for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
				OHLCEntity entity = mOHLCData.get(mDataStartIndext + i);
				float open = (float) ((mMaxPrice - entity.getOpen()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
				float close = (float) ((mMaxPrice - entity.getClose()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
				float high = (float) ((mMaxPrice - entity.getHigh()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
				float low = (float) ((mMaxPrice - entity.getLow()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
	
				float left = (float) (width - 2 - mCandleWidth * (i + 1 + addNum) - (i + addNum)*3);
				float right = (float) (width - 3 - mCandleWidth * (i + addNum) - (i + addNum)*3);
				float startX = (float) (width - 3 - mCandleWidth * (i + addNum) - (mCandleWidth - 1) / 2 - (i + addNum) * 3);
				if(entity.getOpen()==26.73 ){
					float a = open;
					Log.e("asa", a+"");
				}
				if (open < close) {
					canvas.drawRect(left, close, right, open, greenPaint);
					
					canvas.drawLine(startX, high, startX, low, greenPaint);
				} else if (open == close) {
					canvas.drawLine(left, open, right, open, grayPaint);
					canvas.drawLine(startX, high, startX, low, grayPaint);
				} else {
					canvas.drawRect(left, open, right, close, redPaint);
					canvas.drawLine(startX, high, startX, low, redPaint);
				}
	
			}
			
			float MATitleWidth = width / 10.0f * 10.0f / MALineData.size();
			String text = "";
			float wid = 0;
			for (int j = 0; j < MALineData.size(); j++) {
				MALineEntity lineEntity = MALineData.get(j);

				float startX = 0;
				float startY = 0;
				Paint paint = new Paint();
				paint.setColor(lineEntity.getLineColor());
				paint.setAntiAlias(true);
				paint.setTextSize( getResources().getDimensionPixelOffset(R.dimen.title_text_font));
				int selectIndext = (int) ((width - 2.0f - mStartX - mCandleWidth * addNum - 3 * addNum) / (mCandleWidth + 3) + mDataStartIndext);
				
				mVolumnChartView.setCurrentIndex(selectIndext);
				text = lineEntity.getTitle() + ":" + new DecimalFormat("0.00").format(lineEntity.getLineData().get(selectIndext - mDataStartIndext));
				Paint p= new Paint(); 
				Rect rect = new Rect();
				p.setTextSize( getResources().getDimensionPixelOffset(R.dimen.title_text_font));
				p.getTextBounds(text, 0, text.length(), rect); 
				if(j == 0){
					wid = 2;
				}else{
					wid = 2 + rect.width()*2/3 + wid + 5;
				}
				canvas.drawText(text, wid,DEFAULT_AXIS_TITLE_SIZE, paint);
				wid = wid +  2 + rect.width() ;
				for (int i = 0; i < mShowDataNum
						&& mDataStartIndext + i < lineEntity.getLineData().size(); i++) {
					if (i != 0) {
						canvas.drawLine(
								startX,
								startY + DEFAULT_AXIS_TITLE_SIZE + 4,
								(float) (width - 2 - (3 + mCandleWidth) * (i + addNum) - mCandleWidth * 0.5f),
								(float) ((mMaxPrice - lineEntity.getLineData()
										.get(mDataStartIndext + i)) * rate + DEFAULT_AXIS_TITLE_SIZE + 4),
								paint);
					}
					startX = (float) (width - 2 - (3 + mCandleWidth) * (i + addNum) - mCandleWidth * 0.5f);
					startY = (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate);
				}
			}
			if(null != e && !showDetails){
				e.setLocation(getWidth() - 6, 0);
				mVolumnChartView.onSet(e,ismove,mDataStartIndext);
			}
		}

		
		
	}

	private void drawLowerRegion(Canvas canvas) {
		if(!isShowLowerChartTabs()) {
			return;
		}
		float lowertop = LOWER_CHART_TOP + 1;
		float lowerHight = getHeight() - lowertop - 4;
		float viewWidth = getWidth();

		// 下部表的数据
		// MACDData mMACDData;
		// KDJData mKDJData;
		// RSIData mRSIData;
		Paint whitePaint = new Paint();
		whitePaint.setColor(Color.WHITE);
		Paint yellowPaint = new Paint();
		yellowPaint.setColor(Color.YELLOW);
		Paint magentaPaint = new Paint();
		magentaPaint.setColor(Color.MAGENTA);

		Paint textPaint = new Paint();
		textPaint.setColor(DEFAULT_AXIS_Y_TITLE_COLOR);
		textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
		if (mTabTitle.trim().equalsIgnoreCase("MACD")) {
			List<Double> MACD = mMACDData.getMACD();
			List<Double> DEA = mMACDData.getDEA();
			List<Double> DIF = mMACDData.getDIF();

			double low = DEA.get(mDataStartIndext);
			double high = low;
			double rate = 0.0;
			for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < MACD.size(); i++) {
				low = low < MACD.get(i) ? low : MACD.get(i);
				low = low < DEA.get(i) ? low : DEA.get(i);
				low = low < DIF.get(i) ? low : DIF.get(i);

				high = high > MACD.get(i) ? high : MACD.get(i);
				high = high > DEA.get(i) ? high : DEA.get(i);
				high = high > DIF.get(i) ? high : DIF.get(i);
			}
			rate = lowerHight / (high - low);

			Paint paint = new Paint();
			float zero = (float) (high * rate) + lowertop;
			if (zero < lowertop) {
				zero = lowertop;
			}
			// 绘制双线
			float dea = 0.0f;
			float dif = 0.0f;
			for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < MACD.size(); i++) {
				// 绘制矩形
				if (MACD.get(i) >= 0.0) {
					paint.setColor(Color.RED);
					float top = (float) ((high - MACD.get(i)) * rate) + lowertop;
					if (zero - top < 0.55f) {
						canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
								* (i + 1 - mDataStartIndext), zero, viewWidth - 2
								- (float) mCandleWidth * (i - mDataStartIndext), zero, paint);
					} else {
						canvas.drawRect(viewWidth - 1 - (float) mCandleWidth
								* (i + 1 - mDataStartIndext), top, viewWidth - 2
								- (float) mCandleWidth * (i - mDataStartIndext), zero, paint);
					}

				} else {
					paint.setColor(getResources().getColor(R.color.dark_green));
					float bottom = (float) ((high - MACD.get(i)) * rate) + lowertop;

					if (bottom - zero < 0.55f) {
						canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
								* (i + 1 - mDataStartIndext), zero, viewWidth - 2
								- (float) mCandleWidth * (i - mDataStartIndext), zero, paint);
					} else {
						canvas.drawRect(viewWidth - 1 - (float) mCandleWidth
								* (i + 1 - mDataStartIndext), zero, viewWidth - 2
								- (float) mCandleWidth * (i - mDataStartIndext), bottom, paint);
					}
				}

				if (i != mDataStartIndext) {
					canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
							* (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2,
							(float) ((high - DEA.get(i)) * rate) + lowertop, viewWidth - 2
									- (float) mCandleWidth * (i - mDataStartIndext)
									+ (float) mCandleWidth / 2, dea, whitePaint);

					canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
							* (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2,
							(float) ((high - DIF.get(i)) * rate) + lowertop, viewWidth - 2
									- (float) mCandleWidth * (i - mDataStartIndext)
									+ (float) mCandleWidth / 2, dif, yellowPaint);
				}
				dea = (float) ((high - DEA.get(i)) * rate) + lowertop;
				dif = (float) ((high - DIF.get(i)) * rate) + lowertop;
			}

			canvas.drawText(new DecimalFormat("0.00").format(high), 2, lowertop
					+ DEFAULT_AXIS_TITLE_SIZE - 2, textPaint);
			canvas.drawText(new DecimalFormat("0.00").format((high + low) / 2), 2, lowertop
					+ lowerHight / 2 + DEFAULT_AXIS_TITLE_SIZE, textPaint);
			canvas.drawText(new DecimalFormat("0.00").format(low), 2, lowertop + lowerHight,
					textPaint);

		} else if (mTabTitle.trim().equalsIgnoreCase("KDJ")) {
			List<Double> Ks = mKDJData.getK();
			List<Double> Ds = mKDJData.getD();
			List<Double> Js = mKDJData.getJ();

			double low = Ks.get(mDataStartIndext);
			double high = low;
			double rate = 0.0;
			for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < Ks.size(); i++) {
				low = low < Ks.get(i) ? low : Ks.get(i);
				low = low < Ds.get(i) ? low : Ds.get(i);
				low = low < Js.get(i) ? low : Js.get(i);

				high = high > Ks.get(i) ? high : Ks.get(i);
				high = high > Ds.get(i) ? high : Ds.get(i);
				high = high > Js.get(i) ? high : Js.get(i);
			}
			rate = lowerHight / (high - low);

			// 绘制白、黄、紫线
			float k = 0.0f;
			float d = 0.0f;
			float j = 0.0f;
			for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < Ks.size(); i++) {

				if (i != mDataStartIndext) {
					canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
							* (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2,
							(float) ((high - Ks.get(i)) * rate) + lowertop, viewWidth - 2
									- (float) mCandleWidth * (i - mDataStartIndext)
									+ (float) mCandleWidth / 2, k, whitePaint);

					canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
							* (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2,
							(float) ((high - Ds.get(i)) * rate) + lowertop, viewWidth - 2
									- (float) mCandleWidth * (i - mDataStartIndext)
									+ (float) mCandleWidth / 2, d, yellowPaint);

					canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
							* (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2,
							(float) ((high - Js.get(i)) * rate) + lowertop, viewWidth - 2
									- (float) mCandleWidth * (i - mDataStartIndext)
									+ (float) mCandleWidth / 2, j, magentaPaint);
				}
				k = (float) ((high - Ks.get(i)) * rate) + lowertop;
				d = (float) ((high - Ds.get(i)) * rate) + lowertop;
				j = (float) ((high - Js.get(i)) * rate) + lowertop;
			}

			canvas.drawText(new DecimalFormat("0.00").format(high), 2, lowertop
					+ DEFAULT_AXIS_TITLE_SIZE - 2, textPaint);
			canvas.drawText(new DecimalFormat("0.00").format((high + low) / 2), 2, lowertop
					+ lowerHight / 2 + DEFAULT_AXIS_TITLE_SIZE, textPaint);
			canvas.drawText(new DecimalFormat("0.00").format(low), 2, lowertop + lowerHight,
					textPaint);

		} else if (mTabTitle.trim().equalsIgnoreCase("RSI")) {

		}

	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		e = event;
		switch (event.getAction()) {
		// 设置触摸模式
		case MotionEvent.ACTION_DOWN:
			if (null != mTouchListener) {
                mTouchListener.chartTounching();
            }
			ismove = true;
			go = true;
			currentTime = System.currentTimeMillis();
			//TOUCH_MODE = DOWN;
			showDetails = false;
			mStartX = event.getX();
			mStartY = event.getY();
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(700);
						if(go){
							mStartX = (int)(event.getX() - 2 * mCandleWidth - 6);
							if(mOHLCData.size() < 30){
								mStartX = (int)(event.getX() -  mCandleWidth - 3);
							}
							mStartY = event.getY();
							showDetails = true;
							setCurrentData();
							postInvalidate();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			t.start();
			break;
		case MotionEvent.ACTION_UP:
			if (null != mTouchListener) {
                mTouchListener.loseTouching();
            }
				showDetails = false;
				go = false;
				mStartX = getWidth() - 6;
				if( mOHLCData.size() < 30){
					mStartX = (int)(getWidth() - 6 - (mCandleWidth + 3) * (30 - mOHLCData.size()));
				}
				/*e.setLocation(getWidth() - 6, 0);
				mVolumnChartView.onSet(e,ismove,mDataStartIndext);*/
				postInvalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			float horizontalSpacing = event.getX() - mStartX;
			if (Math.abs(horizontalSpacing) > MIN_MOVE_DISTANCE) {
				go = false;
			}
			/*if (mOHLCData == null || mOHLCData.size() <= 0) {
				return true;
			}
			if (!showDetails && TOUCH_MODE == ZOOM) {
				
				mStartX = event.getX();
				mStartY = event.getY();
				
				setCurrentData();
				postInvalidate();

			} else if (TOUCH_MODE == DOWN) {
				//setTouchMode(event);
			}*/
			if(showDetails){
				mStartX = event.getX();
				mStartY = event.getY();
				setCurrentData();
				postInvalidate();
			}
			break;
		}
		/*float horizontalSpacing = event.getX() - mStartX;
		if (Math.abs(horizontalSpacing) < MIN_MOVE_DISTANCE) {
			if( System.currentTimeMillis() - currentTime > 500 && go){
				showDetails = true;
			}
			if(!showDetails)
			return true;
		}*/
		
		return true;
	}
	private ITouchListener mTouchListener;

    public void setITouchListener(ITouchListener touchListener) {
        this.mTouchListener = touchListener;
    }
	private void setCurrentData() {
		try {
			if (mShowDataNum > mOHLCData.size()) {
				mShowDataNum = mOHLCData.size();
			}
			if (MIN_CANDLE_NUM > mOHLCData.size()) {
				mShowDataNum = MIN_CANDLE_NUM;
			}

			if (mShowDataNum > mOHLCData.size()) {
				mDataStartIndext = 0;
			} else if (mShowDataNum + mDataStartIndext > mOHLCData.size()) {
				mDataStartIndext = mOHLCData.size() - mShowDataNum;
			}
			mMinPrice = mOHLCData.get(mDataStartIndext).getLow();
			mMaxPrice = mOHLCData.get(mDataStartIndext).getHigh();
			for (int i = mDataStartIndext + 1; i < mOHLCData.size()
					&& i < mShowDataNum + mDataStartIndext; i++) {
				OHLCEntity entity = mOHLCData.get(i);
				mMinPrice = mMinPrice < entity.getLow() ? mMinPrice : entity.getLow();
				mMaxPrice = mMaxPrice > entity.getHigh() ? mMaxPrice : entity.getHigh();
			}

			for (MALineEntity lineEntity : MALineData) {
				for (int i = mDataStartIndext; i < lineEntity.getLineData().size()
						&& i < mShowDataNum + mDataStartIndext; i++) {
					mMinPrice = mMinPrice < lineEntity.getLineData().get(i) ? mMinPrice : lineEntity
							.getLineData().get(i);
					mMaxPrice = mMaxPrice > lineEntity.getLineData().get(i) ? mMaxPrice : lineEntity
							.getLineData().get(i);
				}
			}

			if(mDisplayChangeListener != null) {
				mDisplayChangeListener.onDisplayDataChange(getDisplayOHLCEntitys());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取显示的数据
	 * @return
	 */
	public List<OHLCEntity> getDisplayOHLCEntitys() {
		if(mOHLCData == null || mOHLCData.size() == 0) {
			return null;
		}
		
		List<OHLCEntity> result = new ArrayList<OHLCEntity>();
		for (int i = mDataStartIndext; i < mOHLCData.size()
				&& i < mShowDataNum + mDataStartIndext; i++) {
			OHLCEntity entity = mOHLCData.get(i);
			result.add(entity);
		}
		
		return result;
	}

	private void zoomIn() {
		mShowDataNum++;
		if (mShowDataNum > mOHLCData.size()) {
			mShowDataNum = MIN_CANDLE_NUM > mOHLCData.size() ? MIN_CANDLE_NUM : mOHLCData.size();
		}

	}

	private void zoomOut() {
		mShowDataNum--;
		if (mShowDataNum < MIN_CANDLE_NUM) {
			mShowDataNum = MIN_CANDLE_NUM;
		}

	}
	
	/**缩小*/
	private void zoomIn(int size) {
		if(zoomNum < 10){
			mShowDataNum += size;
			if (mShowDataNum > mOHLCData.size() && mOHLCData.size() <= MAX_CANDLE_NUM) {
				mShowDataNum = MIN_CANDLE_NUM > mOHLCData.size() ? MIN_CANDLE_NUM : mOHLCData.size();
			}else if(mShowDataNum > mOHLCData.size() && mOHLCData.size() > MAX_CANDLE_NUM){
				mShowDataNum = MAX_CANDLE_NUM;
			}
			if(zoomNum == 0){
				mShowDataNum = 40;
			}
			if(zoomNum == 9){
				if(mOHLCData.size() > MAX_CANDLE_NUM){
					mShowDataNum = MAX_CANDLE_NUM;
				}else{
					mShowDataNum = mOHLCData.size();
				}
				
			}
			zoomNum++;
		}
	}
	/**放大*/
	private void zoomOut(int size) {
		if(zoomNum > 0){
			
			mShowDataNum -= size;
			if (mShowDataNum < MIN_CANDLE_NUM) {
				mShowDataNum = MIN_CANDLE_NUM;
				
			}
			if(zoomNum == 1){
				mShowDataNum = MIN_CANDLE_NUM;
			}
			if(zoomNum == 10){
				mShowDataNum = 80;
			}
			/*if(zoomNum == 1){
				mShowDataNum = MIN_CANDLE_NUM;
			}
			zoomNum--;*/
			zoomNum--;
		}
	}

	private void setTouchMode(MotionEvent event) {
		float daltX = Math.abs(event.getX() - mStartX);
		float daltY = Math.abs(event.getY() - mStartY);
		if (FloatMath.sqrt(daltX * daltX + daltY * daltY) > MIN_MOVE_DISTANCE) {
			if (daltX < daltY) {
				TOUCH_MODE = ZOOM;
			} else {
				TOUCH_MODE = MOVE;
			}
			mStartX = event.getX();
			mStartY = event.getY();
		}
	}

	/**
	 * 初始化MA值，从数组的最后一个数据开始初始化
	 * 
	 * @param entityList
	 * @param days
	 * @return
	 */
	private List<Float> initMA(List<OHLCEntity> entityList, int days) {
		if (days < 2 || entityList == null || entityList.size() <= 0) {
			return null;
		}
		List<Float> MAValues = new ArrayList<Float>();

		float sum = 0;
		float avg = 0;
		for (int i = entityList.size() - 1; i >= 0; i--) {
			float close = (float) entityList.get(i).getClose();
			if (i > entityList.size() - days) {
				sum = sum + close;
				avg = sum / (entityList.size() - i);
			} else {
				sum = close + avg * (days - 1);
				avg = sum / days;
			}
			MAValues.add(avg);
		}

		List<Float> result = new ArrayList<Float>();
		for (int j = MAValues.size() - 1; j >= 0; j--) {
			result.add(MAValues.get(j));
		}
		return result;
	}

	public List<OHLCEntity> getOHLCData() {
		return mOHLCData;
	}

	public void setOHLCData(List<OHLCEntity> OHLCData) {
		if (OHLCData == null || OHLCData.size() <= 0) {
			return;
		}
		this.mOHLCData = OHLCData;
		initMALineData();
		mMACDData = new MACDEntity(mOHLCData);
		mKDJData = new KDJEntity(mOHLCData);
		mRSIData = new RSIEntity(mOHLCData);

		setCurrentData();
		postInvalidate();
	}

	private void initMALineData() {
		MALineEntity MA5 = new MALineEntity();
		MA5.setTitle("MA5");
		MA5.setLineColor(getResources().getColor(R.color.ma5_color));
		MA5.setLineData(initMA(mOHLCData, 5));

		MALineEntity MA10 = new MALineEntity();
		MA10.setTitle("MA10");
		MA10.setLineColor(getResources().getColor(R.color.ma10_color));
		MA10.setLineData(initMA(mOHLCData, 10));

		MALineEntity MA20 = new MALineEntity();
		MA20.setTitle("MA20");
		MA20.setLineColor(getResources().getColor(R.color.ma20_color));
		MA20.setLineData(initMA(mOHLCData, 20));

		MALineData = new ArrayList<MALineEntity>();
		MALineData.add(MA5);
		MALineData.add(MA10);
		MALineData.add(MA20);

	}

	public void onTabClick(int indext) {
		String[] titles = getLowerChartTabTitles();
		mTabTitle = titles[indext];
		postInvalidate();
	}
	
	
	public void setLowerChartTabTitles(String[] LowerChartTabTitles) {
		super.setLowerChartTabTitles(LowerChartTabTitles);
		if(LowerChartTabTitles != null && LowerChartTabTitles.length > 0) {
			mTabTitle = LowerChartTabTitles[0];
		}
	}
	
	/**
	 * 放大
	 */
	public void makeLager() {
		zoomOut(5);
		setCurrentData();
		postInvalidate();
	}
	
	/**
	 * 判断是否是最大
	 * @return
	 */
	public boolean isLargest() {
		if(mOHLCData == null || mOHLCData.size() == 0) {
			return false;
		}
		return mShowDataNum == mOHLCData.size();
	}
	
	/**
	 * 是否是最小
	 * @return
	 */
	public boolean isSmallest() {
		return mShowDataNum == MIN_CANDLE_NUM;
	}
	
	/**
	 * 缩小
	 */
	public void makeSmaller() {
		zoomIn(5);
		setCurrentData();
		postInvalidate();
	}
	
	
	
	public int getShowDataNum() {
		return mShowDataNum;
	}

	public void setShowDataNum(int ShowDataNum) {
		this.mShowDataNum = ShowDataNum;
	}



	/**
	 * 显示数据变化
	 * @author linbing
	 *
	 */
	public interface DisplayDataChangeListener {
		
		/**
		 * 显示的数据变化
		 * @param entitys
		 */
		void onDisplayDataChange(List<OHLCEntity> entitys);
	}
	public void setStick(StickChart mVolumnChartView){
		this.mVolumnChartView = mVolumnChartView;
	}
}
