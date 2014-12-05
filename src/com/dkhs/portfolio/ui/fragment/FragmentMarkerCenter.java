package com.dkhs.portfolio.ui.fragment;

import java.util.Timer;
import java.util.TimerTask;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentMarkerCenter extends Fragment implements OnClickListener {
	private static final String TYPE = "type";
	public static final int INSIDE_COUNT = 0;
	public static final int SHEN_HU = 1;

	private FragmentSelectStockFund loadDataListFragment;
	private boolean ace = true;
	private ImageView marketIconUpDown;
	private TextView marketTextIndex;
	private TextView marketTextEdition;
	private LinearLayout marketLayoutUpanddown;
	private int type;
	private Timer mMarketTimer;
	private static final long mPollRequestTime = 1000 * 5;
	private boolean start = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_market_canter, null);
		Bundle b = getArguments();
		if (null != b) {
			type = b.getInt(TYPE);
		}
		initView(view);
		initFragment();
		return view;
	}

	public void initView(View view) {
		marketIconUpDown = (ImageView) view
				.findViewById(R.id.market_icon_up_down);
		marketTextIndex = (TextView) view.findViewById(R.id.market_text_index);
		marketTextEdition = (TextView) view
				.findViewById(R.id.market_text_edition);
		marketLayoutUpanddown = (LinearLayout) view
				.findViewById(R.id.market_layout_upanddown);
		marketLayoutUpanddown.setOnClickListener(this);
		marketIconUpDown.setVisibility(View.GONE);
		if (type == 1) {
			marketTextIndex.setText("股票");
			marketTextEdition.setText("最新");
			marketIconUpDown.setVisibility(View.VISIBLE);
		}
	}

	public static Fragment initFrag(int type) {
		FragmentMarkerCenter fragment = new FragmentMarkerCenter();
		Bundle args = new Bundle();
		args.putInt(TYPE, type);
		fragment.setArguments(args);
		return fragment;
	}

	private void initFragment() {
		switch (type) {
		case 0:
			if (null == loadDataListFragment) {
				loadDataListFragment = loadDataListFragment
						.getStockFragment(ViewType.STOC_INDEX_MARKET_CURRENT);

			}
			break;
		case 1:
			if (null == loadDataListFragment) {
				loadDataListFragment = loadDataListFragment
						.getStockFragment(ViewType.STOC_INDEX_POSITION);
				ace = false;
			}
			break;
		default:
			break;
		}
		getChildFragmentManager().beginTransaction()
				.replace(R.id.fragment_market, loadDataListFragment).commit();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.market_layout_upanddown:
			marketIconUpDown.setVisibility(View.VISIBLE);
			if (type == 0) {
				if (ace) {
					loadDataListFragment = loadDataListFragment
							.getStockFragment(ViewType.STOC_INDEX_MARKET);
					marketIconUpDown
							.setImageResource(R.drawable.market_icon_down);
					ace = false;
				} else {
					loadDataListFragment = loadDataListFragment
							.getStockFragment(ViewType.STOC_INDEX_MARKET_ACE);
					marketIconUpDown
							.setImageResource(R.drawable.market_icon_up);
					ace = true;
				}

			} else {
				if (ace) {

					loadDataListFragment = loadDataListFragment
							.getStockFragment(ViewType.STOC_INDEX_POSITION);
					marketIconUpDown
							.setImageResource(R.drawable.market_icon_down);
					ace = false;
				} else {
					loadDataListFragment = loadDataListFragment
							.getStockFragment(ViewType.STOC_INDEX_POSITION_ACE);
					marketIconUpDown
							.setImageResource(R.drawable.market_icon_up);
					ace = true;
				}

			}
			getChildFragmentManager().beginTransaction()
					.replace(R.id.fragment_market, loadDataListFragment)
					.commit();
			break;

		default:
			break;
		}
	}
	@Override
    public void onResume() {
		if (mMarketTimer == null && start) {
            mMarketTimer = new Timer(true);
            mMarketTimer.schedule(new RequestMarketTask(), mPollRequestTime, mPollRequestTime);
        }
        super.onResume();

        

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

            loadDataListFragment.refreshForMarker();
        }
    }

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		if(isVisibleToUser){
			start = true;
		}else{
			start = false;
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
    
}
