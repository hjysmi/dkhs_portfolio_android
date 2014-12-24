package com.dkhs.portfolio.ui.widget.kline;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.R.color;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.ITouchListener;
import com.dkhs.portfolio.ui.widget.chart.StickChart;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;


public class KChartsLandView extends GridChart implements GridChart.OnTabClickListener {

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
	private final static int MIN_CANDLE_NUM = 50;

	/** 默认显示的Candle数 */
	private final static int DEFAULT_CANDLE_NUM = 50;
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
	private int zoomNum = 0;
	private long currentTime;
	private DisplayDataChangeListener mDisplayChangeListener; //显示数据变化监听
	private boolean go = true;
	private boolean firsttime = true;
	private String symbolType;
	private String symbol;
	public KChartsLandView(Context context) {
		super(context);
		init();
	}
	
	public KChartsLandView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public KChartsLandView(Context context, AttributeSet attrs, int defStyle) {
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
				setOnTouchOnce();
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
		boolean isB = false;
		if(symbol.contains("SH9")){
			isB = true;
		}
		if (showDetails && mStartX > 3) {
			Paint textPaintFor = new Paint();
			textPaintFor.setStyle(Paint.Style.FILL);// 充满
			textPaintFor.setColor(Color.WHITE);
			textPaintFor.setAntiAlias(true);
			FontMetrics fm = textPaintFor.getFontMetrics();
	        int textTextHeight = (int) (Math.ceil(fm.descent - fm.ascent) + 2);
			int textMargin = getResources().getDimensionPixelSize(R.dimen.float_text_margin);
			int addNum = MIN_CANDLE_NUM - mOHLCData.size();
			float width = getWidth() - PADDING_LEFT;
			float left = 3.0f + PADDING_LEFT + 20;
			float top = (float) (5.0 + DEFAULT_AXIS_TITLE_SIZE) + 20;
			float right = 3.0f + 9 * DEFAULT_AXIS_TITLE_SIZE + PADDING_LEFT + 20;
			float bottom = 5.0f + 9 * textTextHeight + 20;
			if(mOHLCData.size() < MIN_CANDLE_NUM){
				if (mStartX - addNum * (mCandleWidth + 3) < (width / 2.0f  + PADDING_LEFT)) {
					right = width - 12.0f + PADDING_LEFT;
					left = width - 12.0f - 9 * DEFAULT_AXIS_TITLE_SIZE + PADDING_LEFT;
				}
			}else{
				if (mStartX < width / 2.0f) {
					right = width - 12.0f + PADDING_LEFT;
					left = width - 12.0f - 9 * DEFAULT_AXIS_TITLE_SIZE + PADDING_LEFT;
				}
			}
			
			int selectIndext = (int) ((width - 2.0f - mStartX) / (mCandleWidth + 3) + mDataStartIndext);
			
			if(mOHLCData.size() < MIN_CANDLE_NUM){
				selectIndext = (int) ((width - 2.0f - mStartX - addNum * (mCandleWidth + 3) ) / (mCandleWidth + 3) + mDataStartIndext);
			}
			double rate = (getUperChartHeight() - 2) / (mMaxPrice - mMinPrice);
			float cl = (float) ((mMaxPrice - mOHLCData.get(selectIndext).getClose()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
			float startX = (float) (width - 3 - (mCandleWidth + 3) * (selectIndext - mDataStartIndext) - (mCandleWidth - 1) / 2);
			if(mOHLCData.size() < MIN_CANDLE_NUM){
				startX = (float) (width - 3 - (mCandleWidth + 3) * (selectIndext - mDataStartIndext) - (mCandleWidth - 1) / 2 - addNum * (mCandleWidth + 3));
			}
			// 绘制点击线条及详情区域
			Paint paint = new Paint();
			paint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.blue_line));
			paint.setAntiAlias(true);
			//paint.setAlpha(150);
			e.setLocation(startX, startX);
			mVolumnChartView.onSet(e,ismove,mDataStartIndext);
			canvas.drawLine(startX + PADDING_LEFT , 2.0f + DEFAULT_AXIS_TITLE_SIZE, startX+ PADDING_LEFT, UPER_CHART_BOTTOM,
					paint);
			canvas.drawLine(PADDING_LEFT, cl, this.getWidth(), cl, paint);//十字光标横线
			/*if(mOHLCData.size() < MIN_CANDLE_NUM){
				canvas.drawLine((int)(mStartX - addNum * (mCandleWidth + 3)), getHeight() - 2.0f, (int)(mStartX - addNum * (mCandleWidth + 3)), LOWER_CHART_TOP, paint);
			}else{
				canvas.drawLine(mStartX, getHeight() - 2.0f, mStartX, LOWER_CHART_TOP, paint);
			}*/
			
			Rect rect = new Rect((int)left, (int)top, (int)(right+4), (int)(bottom+4));
	        //由于图片的实际尺寸比显示出来的图像要大一些，因此需要适当更改下大小，以达到较好的效果     
			Paint paint1 = new Paint();
			paint1.setColor(Color.WHITE);
			paint1.setAntiAlias(true);//去除锯齿。     
	         paint1.setShadowLayer(5f, 5.0f, 5.0f, Color.BLACK); //设置阴影层，这是关键。     
	         paint1.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
	        RectF rectF = new RectF(rect);    
	        paint.setColor(Color.WHITE);
	        //canvas.drawRoundRect(rectF, 10f, 10f, paint1);
	        RectF rectF2 = new RectF((int)left, (int)top, (int)(right), (int)(bottom));  
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			//canvas.drawRoundRect(rectF2,  10f, 10f, paint);
	        Paint selectPaint = new Paint();
	        selectPaint.setAntiAlias(true);// 设置画笔的锯齿效果
	        selectPaint.setStyle(Paint.Style.FILL);// 充满
	        selectPaint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.white_lucenty));
	        RectF oval3 = new RectF(left - 10, top - 10, right, bottom + 15+ textMargin * 7);// 设置个新的长方形
	        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

	        selectPaint.setStyle(Paint.Style.STROKE);// 描边
	        selectPaint.setStrokeWidth(2);
	        selectPaint.setColor(Color.LTGRAY);
	        canvas.drawRoundRect(oval3, 20, 15, selectPaint);
			
			Paint borderPaint = new Paint();
			borderPaint.setColor(Color.LTGRAY);
			borderPaint.setStrokeWidth(2);
			//canvas.drawLine(left, top, left, bottom, borderPaint);
			//canvas.drawLine(left, top, right, top, borderPaint);
			//canvas.drawLine(right, bottom, right, top, borderPaint);
			//canvas.drawLine(right, bottom, left, bottom, borderPaint);
			
			// 绘制详情文字
			Paint textPaint = new Paint();
			textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
			//textPaint.setColor(Color.DKGRAY);
			//textPaint.setFakeBoldText(true);
			textPaint.setAntiAlias(true);
			canvas.drawText("日期: " + mOHLCData.get(selectIndext).getDate(), left + 1, top
					+ textTextHeight, textPaint);

			canvas.drawText("开盘:", left + 1, top + textTextHeight * 2+ textMargin, textPaint);
			double open = mOHLCData.get(selectIndext).getOpen();
			try {
				double ysdclose = mOHLCData.get(selectIndext + 1).getClose();
				if (open >= ysdclose) {
					//textPaint.setColor(Color.DKGRAY);
				} else {
					//textPaint.setColor(Color.DKGRAY);
				}
				if(!isB){
					canvas.drawText(new DecimalFormat("0.00").format(open), left + 1
							+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 2 + textMargin,
							textPaint);
				}else{
					canvas.drawText(new DecimalFormat("0.000").format(open), left + 1
							+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 2 + textMargin,
							textPaint);
				}
			} catch (Exception e) {
				canvas.drawText(new DecimalFormat("0.00").format(open), left + 1
						+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 2+ textMargin,
						textPaint);
			}

			//textPaint.setColor(Color.DKGRAY);
			canvas.drawText("最高:", left + 1, top + textTextHeight * 3+ textMargin * 2, textPaint);
			double high = mOHLCData.get(selectIndext).getHigh();
			if (open < high) {
				//textPaint.setColor(Color.DKGRAY);
			} else {
				//textPaint.setColor(Color.DKGRAY);
			}
			if(!isB){
			canvas.drawText(new DecimalFormat("0.00").format(high), left + 1
					+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 3+ textMargin * 2,
					textPaint);
			}else{
				canvas.drawText(new DecimalFormat("0.000").format(high), left + 1
						+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 3+ textMargin * 2,
						textPaint);
			}
			//textPaint.setColor(Color.DKGRAY);
			canvas.drawText("最低:", left + 1, top + textTextHeight * 4+ textMargin * 3, textPaint);
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
			if(!isB){
			canvas.drawText(new DecimalFormat("0.00").format(low), left + 1
					+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 4+ textMargin * 3,
					textPaint);
			}else{
				canvas.drawText(new DecimalFormat("0.000").format(low), left + 1
						+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 4+ textMargin * 3,
						textPaint);
			}
			//textPaint.setColor(Color.DKGRAY);
			canvas.drawText("收盘:", left + 1, top + textTextHeight * 5+ textMargin * 4, textPaint);
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
			if(!isB){
			canvas.drawText(new DecimalFormat("0.00").format(close), left + 1
					+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 5+ textMargin * 4,
					textPaint);
			}else{
				canvas.drawText(new DecimalFormat("0.000").format(close), left + 1
						+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 5+ textMargin * 4,
						textPaint);
			}
			
			//textPaint.setColor(Color.DKGRAY);
			canvas.drawText("涨跌:", left + 1, top + textTextHeight * 6+ textMargin * 5, textPaint);
			try {
				if(!isB){
				canvas.drawText(new DecimalFormat("0.00").format(mOHLCData.get(selectIndext).getChange()), left + 1
						+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 6+ textMargin * 5,
						textPaint);
				}else{
					canvas.drawText(new DecimalFormat("0.000").format(mOHLCData.get(selectIndext).getChange()), left + 1
							+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 6+ textMargin * 5,
							textPaint);
				}
			} catch (Exception e) {
				/*canvas.drawText("--", left + 1 + DEFAULT_AXIS_TITLE_SIZE * 3.5f, top
						+ DEFAULT_AXIS_TITLE_SIZE * 6.0f, textPaint);*/
			}
			
			//textPaint.setColor(Color.DKGRAY);
			canvas.drawText("涨幅:", left + 1, top + textTextHeight * 7+ textMargin * 6, textPaint);
			try {
				canvas.drawText(new DecimalFormat("0.00").format(mOHLCData.get(selectIndext).getPercentage()) + "%", left + 1
						+ DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 7+ textMargin * 6,
						textPaint);
			} catch (Exception e) {
				canvas.drawText("--", left + 1 + DEFAULT_AXIS_TITLE_SIZE * 3.5f, top
						+ DEFAULT_AXIS_TITLE_SIZE * 6.0f, textPaint);
			}
			
			//textPaint.setColor(Color.DKGRAY);
			canvas.drawText("成交量:", left + 1, top + textTextHeight * 8+ textMargin * 7, textPaint);
			try {
				double volume = mOHLCData.get(selectIndext).getVolume()/100;
				if (volume < 10000) {
					canvas.drawText(new DecimalFormat("0.00").format(volume), left + 1
							+ DEFAULT_AXIS_TITLE_SIZE * 3.5f, top + textTextHeight * 8+ textMargin * 7,
							textPaint);
				} else if(volume > 10000 && volume < 100000000){
					volume = volume/10000;
					canvas.drawText(new DecimalFormat("0.00").format(volume) + "万", left + 1
							+ DEFAULT_AXIS_TITLE_SIZE * 3.5f, top + textTextHeight * 8+ textMargin * 7,
							textPaint);
				}else{
					volume = volume/100000000;
					canvas.drawText(new DecimalFormat("0.00").format(volume) + "亿", left + 1
							+ DEFAULT_AXIS_TITLE_SIZE * 3.5f, top + textTextHeight * 8+ textMargin * 7,
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
					if(i == 0){
						String t;
						if(symbolType.equals("5")){
							t = new DecimalFormat("0").format(mMinPrice + (mMaxPrice - mMinPrice) / len * i);
						}else{
							t = new DecimalFormat("0.00").format(mMinPrice + (mMaxPrice - mMinPrice) / len * i);
						}
						
						if(t.length() > 6){
							t = t.substring(0, 6);
							if(t.substring(5, 6).equals(".")){
								t = t.substring(0,5);
							}
						}
						Paint p= new Paint(); 
						Rect rect = new Rect();
						p.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
						p.getTextBounds(t, 0, t.length(), rect); 
						canvas.drawText(t, PADDING_LEFT - rect.width() - 3,
								UPER_CHART_BOTTOM - getLatitudeSpacing() * i, textPaint);
					}else{
						String t;
						if(symbolType.equals("5")){
							t = new DecimalFormat("0").format(mMinPrice + (mMaxPrice - mMinPrice) / len * i);
						}else{
							t = new DecimalFormat("0.00").format(mMinPrice + (mMaxPrice - mMinPrice) / len * i);
						}
						if(t.length() > 6){
							t = t.substring(0, 6);
							if(t.substring(5, 6).equals(".")){
								t = t.substring(0,5);
							}
						}
						Paint p= new Paint(); 
						Rect rect = new Rect();
						p.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
						p.getTextBounds(t, 0, t.length(), rect); 
						canvas.drawText(t,PADDING_LEFT - rect.width() - 3,
							UPER_CHART_BOTTOM - getLatitudeSpacing() * i + DEFAULT_AXIS_TITLE_SIZE, textPaint);
					}
				}
			}
			String t;
			if(symbolType.equals("5")){
				t = new DecimalFormat("0.00").format(mMaxPrice);
			}else{
				t = new DecimalFormat("0.00").format(mMaxPrice);
			}
			if(t.length() > 6){
				t = t.substring(0, 6);
				if(t.substring(5, 6).equals(".")){
					t = t.substring(0,5);
				}
			}
			Paint p= new Paint(); 
			Rect rect = new Rect();
			p.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
			p.getTextBounds(t, 0, t.length(), rect); 
			canvas.drawText(t, PADDING_LEFT - rect.width() - 3,
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
		
		try {
			Paint redPaint = new Paint();
			redPaint.setColor(Color.RED);
			Paint greenPaint = new Paint();
			greenPaint.setColor(getResources().getColor(R.color.dark_green));
			Paint grayPaint = new Paint();
			grayPaint.setColor(getResources().getColor(R.color.def_gray));
			int width = getWidth() - PADDING_LEFT;
			mCandleWidth = (width - 4) / 10.0 * 10.0 / mShowDataNum - 3;
			double rate = (getUperChartHeight() - 2) / (mMaxPrice - mMinPrice);
			if(mOHLCData.size() >= MIN_CANDLE_NUM){
				for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
					OHLCEntity entity = mOHLCData.get(mDataStartIndext + i);
					float open = (float) ((mMaxPrice - entity.getOpen()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
					float close = (float) ((mMaxPrice - entity.getClose()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
					float high = (float) ((mMaxPrice - entity.getHigh()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
					float low = (float) ((mMaxPrice - entity.getLow()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);

					float left = (float) (width - 2 - mCandleWidth * (i + 1) - i*3 + PADDING_LEFT);
					float right = (float) (width - 3 - mCandleWidth * i - i*3 + PADDING_LEFT);
					float startX = (float) (width - 3 - mCandleWidth * i - (mCandleWidth - 1) / 2 - i * 3 + PADDING_LEFT);
					if(entity.getOpen()==26.73 ){
						float a = open;
						Log.e("asa", a+"");
					}
					if (open < close) {
						canvas.drawRect(left, close, right, open, greenPaint);
						
						canvas.drawLine(startX, high, startX, low, greenPaint);
					} else if (open == close) {
					    double hisClose = mOHLCData.get(mDataStartIndext + i + 1).getClose();
                        if(entity.getOpen() > hisClose){
                            canvas.drawLine(left, open, right, open, redPaint);
                            canvas.drawLine(startX, high, startX, low, redPaint);
                        }else if(entity.getOpen() < hisClose){
                            canvas.drawLine(left, open, right, open, greenPaint);
                            canvas.drawLine(startX, high, startX, low, greenPaint);
                        }else{
                            canvas.drawLine(left, open, right, open, grayPaint);
                            canvas.drawLine(startX, high, startX, low, grayPaint);
                        }
					} else {
						canvas.drawRect(left, open, right, close, redPaint);
						canvas.drawLine(startX, high, startX, low, redPaint);
					}
				}
				// 绘制上部曲线图及上部分MA值
				//float MATitleWidth = width / 10.0f * 10.0f / MALineData.size();
				String text = "";
				float wid = 0;
				for (int j = 0; j < MALineData.size(); j++) {
					MALineEntity lineEntity = MALineData.get(j);

					float startX = PADDING_LEFT;
					float startY = 0;
					Paint paint = new Paint();
					paint.setColor(lineEntity.getLineColor());
					paint.setAntiAlias(true);
					paint.setTextSize( getResources().getDimensionPixelOffset(R.dimen.title_text_font));
					int selectIndext = (int) ((width - 2.0f - mStartX) / (mCandleWidth + 3) + mDataStartIndext);
					mVolumnChartView.setCurrentIndex(selectIndext);
					mVolumnChartView.setmShowDate(mShowDataNum);
					if(selectIndext - mDataStartIndext > lineEntity.getLineData().size() -1 || selectIndext - mDataStartIndext < 0){
						text = lineEntity.getTitle() + ":0.00";
					}else
						text = lineEntity.getTitle() + ":" + new DecimalFormat("0.00").format(lineEntity.getLineData().get(selectIndext - mDataStartIndext));
					Paint p= new Paint(); 
					Rect rect = new Rect();
					p.setTextSize( getResources().getDimensionPixelOffset(R.dimen.title_text_font));
					p.getTextBounds(text, 0, text.length(), rect); 
					if(j == 0){
						wid = 2;
					}/*else{
						wid = 2 + rect.width()*2/3 + wid + 5;
					}*/
					canvas.drawText(text, wid + PADDING_LEFT,DEFAULT_AXIS_TITLE_SIZE, paint);
					wid = wid +  32 + rect.width() ;
					paint.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.line_kline));
					for (int i = 0; i < mShowDataNum
							&& mDataStartIndext + i < lineEntity.getLineData().size(); i++) {
						if (i != 0) {
							canvas.drawLine(
									startX,
									startY + DEFAULT_AXIS_TITLE_SIZE + 4,
									(float) (width - 2 - (3 + mCandleWidth) * i - mCandleWidth * 0.5f + PADDING_LEFT),
									(float) ((mMaxPrice - lineEntity.getLineData()
											.get(mDataStartIndext + i)) * rate + DEFAULT_AXIS_TITLE_SIZE + 4),
									paint);
						}
						startX = (float) (width - 2 - (3 + mCandleWidth) * i - mCandleWidth * 0.5f + PADDING_LEFT);
						startY = (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate);
					}
				}
				if(null != e && !showDetails){
					e.setLocation(getWidth() - 6, 0);
					mVolumnChartView.onSet(e,ismove,mDataStartIndext);
				}
			}else{
				int addNum = MIN_CANDLE_NUM - mOHLCData.size();
				for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
					OHLCEntity entity = mOHLCData.get(mDataStartIndext + i);
					float open = (float) ((mMaxPrice - entity.getOpen()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
					float close = (float) ((mMaxPrice - entity.getClose()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
					float high = (float) ((mMaxPrice - entity.getHigh()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
					float low = (float) ((mMaxPrice - entity.getLow()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);

					float left = (float) (width - 2 - mCandleWidth * (i + 1 + addNum) - (i + addNum)*3 + PADDING_LEFT);
					float right = (float) (width - 3 - mCandleWidth * (i + addNum) - (i + addNum)*3 + PADDING_LEFT);
					float startX = (float) (width - 3 - mCandleWidth * (i + addNum) - (mCandleWidth - 1) / 2 - (i + addNum) * 3 + PADDING_LEFT);
					if (open < close) {
						canvas.drawRect(left, close, right, open, greenPaint);
						
						canvas.drawLine(startX, high, startX, low, greenPaint);
					} else if (open == close) {
					    double hisClose = mOHLCData.get(mDataStartIndext + i + 1).getClose();
                        if(entity.getOpen() > hisClose){
                            canvas.drawLine(left, open, right, open, redPaint);
                            canvas.drawLine(startX, high, startX, low, redPaint);
                        }else if(entity.getOpen() < hisClose){
                            canvas.drawLine(left, open, right, open, greenPaint);
                            canvas.drawLine(startX, high, startX, low, greenPaint);
                        }else{
                            canvas.drawLine(left, open, right, open, grayPaint);
                            canvas.drawLine(startX, high, startX, low, grayPaint);
                        }
					} else {
						canvas.drawRect(left, open, right, close, redPaint);
						canvas.drawLine(startX, high, startX, low, redPaint);
					}
				}
				
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
					if(selectIndext - mDataStartIndext > lineEntity.getLineData().size() -1 || selectIndext - mDataStartIndext < 0){
						text = lineEntity.getTitle() + ":0.00";
					}else
						text = lineEntity.getTitle() + ":" + new DecimalFormat("0.00").format(lineEntity.getLineData().get(selectIndext - mDataStartIndext));
					Paint p= new Paint(); 
					Rect rect = new Rect();
					p.setTextSize( getResources().getDimensionPixelOffset(R.dimen.title_text_font));
					p.getTextBounds(text, 0, text.length(), rect); 
					if(j == 0){
						wid = 2;
					}else{
						//wid = 2 + rect.width()*2/3 + wid + 5;
					}
					canvas.drawText(text, wid+PADDING_LEFT,DEFAULT_AXIS_TITLE_SIZE, paint);
					wid = wid +  32 + rect.width() ;
					paint.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.line_kline));
					for (int i = 0; i < mShowDataNum
							&& mDataStartIndext + i < lineEntity.getLineData().size(); i++) {
						if (i != 0) {
							canvas.drawLine(
									startX + PADDING_LEFT,
									startY + DEFAULT_AXIS_TITLE_SIZE + 4,
									(float) (width - 2 - (3 + mCandleWidth) * (i + addNum) - mCandleWidth * 0.5f + PADDING_LEFT),
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}

	private void drawLowerRegion(Canvas canvas) {
		if(!isShowLowerChartTabs()) {
			return;
		}
		float lowertop = LOWER_CHART_TOP + 1;
		float lowerHight = getHeight() - lowertop - 4;
		float viewWidth = getWidth() - PADDING_LEFT;

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
	float timeX = 0;
	float timeY = 0;
	int currentDate = mDataStartIndext;
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		e = event;
		
		switch (event.getAction()) {
		// 设置触摸模式
		case MotionEvent.ACTION_DOWN:
			if (null != mTouchListener) {
                mTouchListener.chartTounching();
            }
			currentDate = mDataStartIndext;
			ismove = true;
			go = true;
			currentTime = System.currentTimeMillis();
			//TOUCH_MODE = DOWN;
			showDetails = false;
			timeX = event.getX();
			timeY = event.getY();
			Log.e("xyxyxyx", timeX + " ----" + timeY);
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(300);
						if(go){
							if (null != mTouchListener) {
				                mTouchListener.chartTounching();
				            }
							mStartX = (int)(event.getX() - 2 * mCandleWidth - 6  - PADDING_LEFT);
							if(mOHLCData.size() < MIN_CANDLE_NUM){
								mStartX = (int)(event.getX() -  mCandleWidth - 3 - PADDING_LEFT);
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
				mStartX = getWidth() - 6 - PADDING_LEFT;
				if( mOHLCData.size() < MIN_CANDLE_NUM){
					mStartX = (int)(getWidth() - 6 - (mCandleWidth + 3) * (MIN_CANDLE_NUM - mOHLCData.size()) - PADDING_LEFT);
				}
				/*e.setLocation(getWidth() - 6, 0);
				mVolumnChartView.onSet(e,ismove,mDataStartIndext);*/
				postInvalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			float horizontalSpacing = event.getX() - timeX;
			float hor = event.getY() - timeY;
			if (Math.abs(horizontalSpacing) > MIN_MOVE_DISTANCE || Math.abs(hor) > MIN_MOVE_DISTANCE && go) {
				go = false;
				if (null != mTouchListener) {
	                mTouchListener.loseTouching();
	            }
			}
			if(!go && !showDetails){
				ismove = false;
				if(event.getX() - PADDING_LEFT  >= 0){
					mStartX = event.getX() - PADDING_LEFT;
					mStartY = event.getY();
				}
				mDataStartIndext = (int) (currentDate + (horizontalSpacing / (mCandleWidth + 3)));
				if (mDataStartIndext < 0) {
					mDataStartIndext = 0;
				}
				/*if (horizontalSpacing < 0) {
					mDataStartIndext--;
					if (mDataStartIndext < 0) {
						mDataStartIndext = 0;
					}
				} else if (horizontalSpacing > 0) {
					mDataStartIndext++;
				}*/
				setCurrentData();
				postInvalidate();
				mVolumnChartView.onSet(e,false,mDataStartIndext);
				
			}
			Log.e("hor", hor + " ----" + horizontalSpacing);
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
				if (null != mTouchListener) {
	                mTouchListener.chartTounching();
	            }
				if(event.getX() - PADDING_LEFT  >= 0){
					mStartX = event.getX() - PADDING_LEFT;
					mStartY = event.getY();
				}
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
    private void setOnTouchOnce(){
    	showDetails = false;
		go = false;
		mStartX = getWidth() - 6 - PADDING_LEFT;
		if( mOHLCData.size() < MIN_CANDLE_NUM){
			mStartX = (int)(getWidth() - 6 - (mCandleWidth + 3) * (MIN_CANDLE_NUM - mOHLCData.size()) - PADDING_LEFT);
		}
		/*e.setLocation(getWidth() - 6, 0);
		mVolumnChartView.onSet(e,ismove,mDataStartIndext);*/
		postInvalidate();
    }
	private void setCurrentData() {
		try {
			if (mShowDataNum > mOHLCData.size()) {
				mShowDataNum = mOHLCData.size();
			}
			if (MIN_CANDLE_NUM > mOHLCData.size()) {
				mShowDataNum = MIN_CANDLE_NUM;
			}
			mVolumnChartView.setmShowDate(mShowDataNum);
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
			double value = mMaxPrice -mMinPrice;
			mMinPrice = mMinPrice - (value * 0.1);
			mMaxPrice = mMaxPrice + (value * 0.1);
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
		for (int i = 0; i < mOHLCData.size()
				; i++) {
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
		if(zoomNum < 5){
			mShowDataNum += size;
			if (mShowDataNum > mOHLCData.size() && mOHLCData.size() <= MAX_CANDLE_NUM) {
				mShowDataNum = MIN_CANDLE_NUM > mOHLCData.size() ? MIN_CANDLE_NUM : mOHLCData.size();
			}else if(mShowDataNum > mOHLCData.size() && mOHLCData.size() > MAX_CANDLE_NUM){
				mShowDataNum = MAX_CANDLE_NUM;
			}
			/*if(zoomNum == 0){
				mShowDataNum = 40;
			}
			if(zoomNum == 9){
				if(mOHLCData.size() > MAX_CANDLE_NUM){
					mShowDataNum = MAX_CANDLE_NUM;
				}else{
					mShowDataNum = mOHLCData.size();
				}
				
			}*/
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
			/*if(zoomNum == 1){
				mShowDataNum = MIN_CANDLE_NUM;
			}
			if(zoomNum == 10){
				mShowDataNum = 80;
			}*/
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
			sum = 0;
			avg = 0;
			if (i - days >= -1) {
				for(int k = 0; k < days; k++){
					sum = (float) (sum + entityList.get(i-k).getClose());
				}
				avg = sum / days;
			} else{
				break;
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
		zoomOut(MIN_CANDLE_NUM);
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
		if(mShowDataNum == mOHLCData.size()){
			return true;
		}
		return mShowDataNum == MAX_CANDLE_NUM;
	}
	
	/**
	 * 是否是最小
	 * @return
	 */
	public boolean isSmallest() {
		return mShowDataNum == MIN_CANDLE_NUM;
	}
	public boolean iscanSmoll() {
		if(mOHLCData == null || mOHLCData.size() < MIN_CANDLE_NUM) {
			return true;
		}
		return false;
	}
	/**
	 * 缩小
	 */
	public void makeSmaller() {
		zoomIn(MIN_CANDLE_NUM);
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

	public String getSymbolType() {
		return symbolType;
	}

	public void setSymbolType(String symbolType) {
		this.symbolType = symbolType;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
}
