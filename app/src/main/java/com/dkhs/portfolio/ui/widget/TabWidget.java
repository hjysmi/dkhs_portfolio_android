package com.dkhs.portfolio.ui.widget;

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
    public   TabWidget(View view){
         mBtntitletableft = (Button) view.findViewById(R.id.btn_titletab_left);
         mBtntitletabcenter = (Button) view.findViewById(R.id.btn_titletab_center);
         mBtntitletabright = (Button) view.findViewById(R.id.btn_titletab_right);
        mBtntitletableft.setOnClickListener(this);
        mBtntitletabcenter.setOnClickListener(this);
        mBtntitletabright.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_titletab_left: {
                mBtntitletabright.setEnabled(true);
                mBtntitletableft.setEnabled(false);
                mBtntitletabcenter.setEnabled(true);
                if(onSelectListener!=null){
                    onSelectListener.onSelect(0);
                }
            }
            break;
            case R.id.btn_titletab_center: {
                mBtntitletabright.setEnabled(true);
                mBtntitletableft.setEnabled(true);
                mBtntitletabcenter.setEnabled(false);
                if(onSelectListener!=null){
                    onSelectListener.onSelect(1);
                }

            }
            break;
            case R.id.btn_titletab_right: {
                mBtntitletabright.setEnabled(false);
                mBtntitletableft.setEnabled(true);
                mBtntitletabcenter.setEnabled(true);
                if(onSelectListener!=null){
                    onSelectListener.onSelect(2);
                }

                // PromptManager.showCustomToast(R.drawable.ic_toast_gantan, R.string.message_timeout);
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

    public  interface  OnSelectListener {
        public  void onSelect(int position);
    }

    public void setSelection(int position){

        switch (position){
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
