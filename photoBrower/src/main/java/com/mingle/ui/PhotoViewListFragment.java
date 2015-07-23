package com.mingle.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.dkhs.utils.FileUtils;
import com.mingle.library.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PhotoViewListFragment extends Fragment implements View.OnClickListener {

    private ViewPager mVp;
    private TextView mIndexTV;
    private TextView mSaveTV;
    /**
     * url 地址列表
     */
    public static String URL_LIST = "url_list";
    /**
     * url 的标题.
     */
    public static String INDEX = "index";

    private ArrayList<String> imageUrls;

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photo_list_view, null);
        mContext = getActivity();
        mVp = (ViewPager) view.findViewById(R.id.vp);
        mIndexTV = (TextView) view.findViewById(R.id.indexTV);
        mSaveTV = (TextView) view.findViewById(R.id.saveTV);
        Intent intent = getActivity().getIntent();
        imageUrls = intent.getStringArrayListExtra(URL_LIST);
        if (imageUrls != null && imageUrls.size() > 0) {
            initView();
        }

        return view;
    }

    private void initView() {
        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < imageUrls.size(); i++) {
            list.add(PhotoViewFragment.newInstance(imageUrls.get(i)));
        }
        ViewpagerAdapter vpAdapter = new ViewpagerAdapter(getChildFragmentManager(), list, null);
        int index = getActivity().getIntent().getIntExtra(INDEX, 0);
        mVp.setAdapter(vpAdapter);
        mVp.setCurrentItem(index);
        if (imageUrls.size() == 1) {
            mIndexTV.setVisibility(View.GONE);
        }
        updateStatus(index);
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mSaveTV.setOnClickListener(this);
    }

    public void updateStatus(int position) {
        this.mIndexTV.setText(position + 1 + "/" + imageUrls.size());
    }

    @Override
    public void onClick(View v) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        String url = imageLoader.getDiskCache().get(imageUrls.get(mVp.getCurrentItem())).getAbsolutePath();
        new SaveBitMapTask().execute(url);
    }

    class SaveBitMapTask extends AsyncTask<String, Void, Boolean> {

        String TAG = "SaveBitMapTask";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSaveTV.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(String... params) {


            boolean result = false;
            if (!FileUtils.isExternalStorageEnable()) {
                Toast.makeText(getActivity(), R.string.no_sd, Toast.LENGTH_SHORT).show();

            } else {
                result = FileUtils.copyFile(params[0], getSaveFileName(), true);
            }

            return result;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mSaveTV.setEnabled(true);
            if (aBoolean) {

                if (mListener != null) {
                    mListener.onSuccess();
                } else {
                    Toast.makeText(mContext, "图片已保存(手机 > dkhs)", Toast.LENGTH_SHORT).show();
                }
                Log.e("Tag", "onPostExecute success");
            } else {
                Log.e("Tag", "onPostExecute failure");
                if (mListener != null) {
                    mListener.onFailure();
                } else {
                    Toast.makeText(mContext, "图片保存失败", Toast.LENGTH_SHORT).show();
                }
            }
            super.onPostExecute(aBoolean);
        }

        public String getSaveFileName() {
            return FileUtils.getExternalStoragePath() + File.separator + "dkhs" + File.separator + "img_" + new DateTime().toString("yyyyMMddHHssDDSSSS") + ".png";
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSaveImageAction) activity;
        } catch (ClassCastException e) {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    OnSaveImageAction mListener;

    public interface OnSaveImageAction {
        // TODO: Update argument type and name
        public void onSuccess();

        public void onFailure();
    }


}
