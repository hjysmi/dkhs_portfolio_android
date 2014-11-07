package com.dkhs.portfolio.ui.adapter;

import java.util.List;
import java.util.Map;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.adapter.MarketCenterItemAdapter.ViewHodler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WebListAdapter extends BaseAdapter{
	private Context context;
	private List<Map<String, String>> titles;
	private LayoutInflater inflater;
	public static final String KEY_TITLE = "title";
    public static final String KEY_URL = "url";
    public static final String KEY_TEXT = "text";
	public WebListAdapter(Context context,List<Map<String, String>> titles){
		this.context = context;
		this.titles = titles;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return titles.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Map<String, String> group = titles.get(position);
		ViewHodler viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHodler();
			convertView = inflater.inflate(R.layout.adapter_layout_weblist, null);
			viewHolder.tvLayoutTitle = (LinearLayout) convertView.findViewById(R.id.web_layout);
			viewHolder.tvUpDown = (ImageView) convertView.findViewById(R.id.webadapter_image);
			viewHolder.tvText = (TextView) convertView.findViewById(R.id.web_text);
			viewHolder.tvWebView = (WebView) convertView.findViewById(R.id.adapter_web);
			convertView.setTag(viewHolder);
		}else {
            viewHolder = (ViewHodler) convertView.getTag();
        }
		viewHolder.tvText.setText(group.get(KEY_TITLE));
		return convertView;
	}
	final static class ViewHodler {
        LinearLayout tvLayoutTitle;
        WebView tvWebView;
        TextView tvText;
        ImageView tvUpDown;
    }
	private void loadText(int groupPosition, final WebView mTextView) {
        // mTextView.setVisibility(View.GONE);
        Map<String, String> group = titles.get(groupPosition);
        if (group != null) {
            String url = group.get(KEY_URL);
            // mTextView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            mTextView.loadUrl(url);
            //mTextView.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
            mTextView.getSettings().setJavaScriptEnabled(true);

            mTextView.setWebViewClient(new WebViewClient() {

                @Override
                public void onScaleChanged(WebView view, float oldScale, float newScale) {
                    super.onScaleChanged(view, oldScale, newScale);
                    // view.requestLayout();
                    //mAdapter.resetWebView(view);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                   // mAdapter.resetWebView(view);
                }
            });
        }
    }
}
