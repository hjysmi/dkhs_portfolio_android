package com.dkhs.portfolio.ui.fragment;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.ITitleButtonListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment implements OnClickListener {

	private ITitleButtonListener mTitleClickListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, null);
		Button btnLeft = (Button) view.findViewById(R.id.btn_back);
		btnLeft.setOnClickListener(this);
		btnLeft.setText("投资组合");
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back: {
			if (null != mTitleClickListener) {
				mTitleClickListener.leftButtonClick();
			}
		}
			break;

		default:
			break;
		}
	}

	public void setTitleClickListener(ITitleButtonListener listener) {
		this.mTitleClickListener = listener;
	}

}
