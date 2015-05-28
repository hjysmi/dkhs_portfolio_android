package com.dkhs.portfolio.ui.widget;

import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;

import com.dkhs.portfolio.R;


/**
 * @author zwm
 * @version 2.0
 * @ClassName TabWidget
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/5/27.
 */
public class TabWidget implements View.OnClickListener {


    public Button mBtntitletableft;
    public Button mBtntitletabcenter;
    public Button mBtntitletabright;

    private View view;

    public TabWidget(View view) {
        this.view=view;
        mBtntitletableft = (Button) view.findViewById(R.id.btn_titletab_left);
        mBtntitletabcenter = (Button) view.findViewById(R.id.btn_titletab_center);
        mBtntitletabright = (Button) view.findViewById(R.id.btn_titletab_right);
        mBtntitletableft.setOnClickListener(this);
        mBtntitletabcenter.setOnClickListener(this);
        mBtntitletabright.setOnClickListener(this);
    }

    public void  setTitles(String[] titles ){
        if(titles.length==3){

            mBtntitletableft.setText(titles[0]);
            mBtntitletabcenter.setText(titles[1]);
            mBtntitletabright.setText(titles[2]);
        }
    }

    public void  setTitles(@ArrayRes int titlesRes ) {
        setTitles(  view.getContext().getResources().getStringArray(titlesRes));
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_titletab_left: {
                mBtntitletabright.setEnabled(true);
                mBtntitletableft.setEnabled(false);
                mBtntitletabcenter.setEnabled(true);
                if (onSelectListener != null) {
                    onSelectListener.onSelect(0);
                }
            }
            break;
            case R.id.btn_titletab_center: {
                mBtntitletabright.setEnabled(true);
                mBtntitletableft.setEnabled(true);
                mBtntitletabcenter.setEnabled(false);
                if (onSelectListener != null) {
                    onSelectListener.onSelect(1);
                }

            }
            break;
            case R.id.btn_titletab_right: {
                mBtntitletabright.setEnabled(false);
                mBtntitletableft.setEnabled(true);
                mBtntitletabcenter.setEnabled(true);
                if (onSelectListener != null) {
                    onSelectListener.onSelect(2);
                }
            }
            break;
            default:
                break;
        }
    }


    private OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        public void onSelect(int position);
    }

    public void setSelection(int position) {

        switch (position) {
            case 0:
                mBtntitletableft.performClick();
                break;
            case 1:
                mBtntitletabcenter.performClick();
                break;
            case 2:
                mBtntitletabright.performClick();
                break;
        }
    }
}
