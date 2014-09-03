package com.dkhs.portfolio.ui.fragment;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.ColorTemplate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TestFragment extends Fragment {

    
    public static TestFragment getInstance(){
        return new TestFragment();
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_test, null);
		initView(view);
		view.setBackgroundColor(ColorTemplate.getRaddomColor());
		return view;
	}

	private void initView(View view) {
	}

}
