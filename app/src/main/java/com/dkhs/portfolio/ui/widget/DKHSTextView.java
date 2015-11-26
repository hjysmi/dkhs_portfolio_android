package com.dkhs.portfolio.ui.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.rockerhieu.emojicon.EmojiconTextView;

/**
 * @author zhangcm
 */
public class DKHSTextView extends EmojiconTextView implements View.OnTouchListener{

	private static String FACE_IMG_PATTERN = "\\[[\u4E00-\u9FA5]+\\]";
	private static String AT_PATTERN1 = "@{1}\\S+\\s+";
	private static String AT_PATTERN2 = "@{1}\\S+:";
	private static String AT_HREF_PATTERN = "<a\\shref='http.{20,30}'>@.{4,20}</a>";
	private static String STOCK_HREF_PATTERN = "@{1}\\S+:";
	private static String STOCK_PATTERN = "\\$\\S+\\([A-Z]+\\)\\$";


	public DKHSTextView(Context context) {
		super(context);
	}

	public DKHSTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DKHSTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		CharSequence charSequence = getText();
		if (event.getAction() == MotionEvent.ACTION_UP && charSequence instanceof Spanned) {

			if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN) {
				ClickableSpan[] spans= getClickSpans(event, (Spanned) charSequence);
				if (spans.length != 0) {
					spans[0].onClick(this);
					return false;
				}
			}
		}/*else if(event.getAction() == MotionEvent.ACTION_DOWN && charSequence instanceof Spanned){

			ClickableSpan[] spans= getClickSpans(event, (Spanned) charSequence);
			if (spans.length == 0) {
				return false;
			}
		}*/

		return super.onTouchEvent(event);
	}

	private ClickableSpan[] getClickSpans(MotionEvent event, Spanned charSequence) {
		Spanned span = charSequence;
		int i = (int) event.getX();
		int j = (int) event.getY();
		int k = i - getTotalPaddingLeft();
		int l = j - getTotalPaddingTop();
		int m = k + getScrollX();
		int n = l + getScrollY();
		Layout layout = getLayout();
		int g = layout.getOffsetForHorizontal(layout.getLineForVertical(n), m);
		return  span.getSpans(g, g, ClickableSpan.class);
	}


	@Override
	public void setText(CharSequence text, BufferType type) {
		if (!TextUtils.isEmpty(text)) {
			text = Html.fromHtml(text.toString());
			if(!TextUtils.isEmpty(rewardValue)){
				TextView image = (TextView) View.inflate(getContext(), R.layout.dkhstextview_reward_money, null);
				image.setText(rewardValue);
				Bitmap bitmap = createViewBitmap(image);
				VerticalImageSpan imgSpan = new VerticalImageSpan(getContext(),bitmap,lineSpacing/2);
				SpannableStringBuilder spannable = new SpannableStringBuilder();
				spannable.append(rewardValue);
				spannable.append(text);
				spannable.setSpan(imgSpan, 0, rewardValue.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				super.setText(spannable, type);
			}else{
				if (text instanceof Spannable) {
					int end = text.length();
					Spannable sp = (Spannable) text;
					URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
					SpannableStringBuilder builder = new SpannableStringBuilder(text);
					builder.clearSpans();
					for (URLSpan url : urls) {
						NoUnderLineSpan mySpan = new NoUnderLineSpan(getResources().getColor(R.color.theme_blue), getContext());
						mySpan.url = url.getURL();
						builder.setSpan(mySpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
//				setMovementMethod(LinkMovementMethod.getInstance());
					setOnTouchListener(this);
					super.setText(builder, type);
				} else {
					super.setText(text, type);
				}
			}
		} else {
			super.setText(text, type);
		}

	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		boolean ret = false;
		CharSequence text = ((TextView)v).getText();
		Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
		TextView widget = (TextView)v;
		int action = event.getAction();
		if(action == MotionEvent.ACTION_UP||action == MotionEvent.ACTION_DOWN){
			int x = (int)event.getX();
			int y = (int)event.getY();
			x -= widget.getTotalPaddingLeft();
			y -= widget.getTotalPaddingTop();
			x += widget.getScrollX();
			y += widget.getScrollY();
			Layout layout = widget.getLayout();
			int line = layout.getLineForVertical(y);
			int off = layout.getOffsetForHorizontal(line,x);
			ClickableSpan[] link = stext.getSpans(off,off,ClickableSpan.class);
			if(link.length != 0){
				if(action == MotionEvent.ACTION_UP){
					link[0].onClick(widget);
				}
				ret = true;
			}
		}
		return ret;
	}

	private String rewardValue;;

	public void setRewardValue(String rewardValue) {
		this.rewardValue = rewardValue;
	}

	private int lineSpacing;
	@Override
	public void setLineSpacing(float add, float mult) {
		lineSpacing = (int) add;
		super.setLineSpacing(add, mult);
	}

	public Bitmap createViewBitmap(View view) {

		view.setDrawingCacheEnabled(true);

		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

		view.layout(0, 0, view.getMeasuredWidth(),view.getMeasuredHeight());

		view.buildDrawingCache();

		Bitmap bitmap = view.getDrawingCache();
		bitmap = resizeImage(bitmap, (int)(view.getMeasuredWidth()/2), (int)(view.getMeasuredHeight()/2));
		return bitmap;
	}

	public static Bitmap resizeImage(Bitmap bitmap, int w, int h)
	{
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		return resizedBitmap;
	}

	/**
	 * 垂直居中的ImageSpan
	 *
	 * @author KenChung
	 */
	public class VerticalImageSpan extends ImageSpan {

		private float space;

		public VerticalImageSpan(Drawable drawable) {
			super(drawable);
		}
		public VerticalImageSpan(Context context,Bitmap bitmap, float space){
			super(context,bitmap, ImageSpan.ALIGN_BASELINE);
			this.space = space;
		}

		public int getSize(Paint paint, CharSequence text, int start, int end,
						   Paint.FontMetricsInt fontMetricsInt) {
			this.getVerticalAlignment();
			Drawable drawable = getDrawable();
			Rect rect = drawable.getBounds();
			if (fontMetricsInt != null) {
				Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
				int fontHeight = fmPaint.bottom - fmPaint.top;
				int drHeight = rect.bottom - rect.top;

				int top = drHeight / 2 - fontHeight / 4;
				int bottom = drHeight / 2 + fontHeight / 4;

				fontMetricsInt.ascent = -bottom;
				fontMetricsInt.top = -bottom;
				fontMetricsInt.bottom = top;
				fontMetricsInt.descent = top;
			}
			return rect.right;
		}

		@Override
		public void draw(Canvas canvas, CharSequence text, int start, int end,
						 float x, int top, int y, int bottom, Paint paint) {
			Drawable drawable = getDrawable();
			canvas.save();
			int transY = 0;
			transY = (int) (((bottom - top) - drawable.getBounds().bottom) / 2 + top - space);
			canvas.translate(x, transY);
			drawable.draw(canvas);
			canvas.restore();
		}
	}
}
