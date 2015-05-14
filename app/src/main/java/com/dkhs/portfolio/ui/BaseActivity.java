package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // this.getWindow().getDecorView().setOnTouchListener(this);

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {  
  
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）  
            View v = getCurrentFocus();  
  
            if (isShouldHideInput(v, ev)) {  
                hideSoftInput(v.getWindowToken());  
            }  
        }  
        return super.dispatchTouchEvent(ev);  
    }  
    
    
    /** 
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏 
     *  
     * @param v 
     * @param event 
     * @return 
     */  
    private boolean isShouldHideInput(View v, MotionEvent event) {  
        if (v != null && (v instanceof EditText)) {  
            int[] l = { 0, 0 };  
            v.getLocationInWindow(l);  
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left  
                    + v.getWidth();  
            if (event.getX() > left && event.getX() < right  
                    && event.getY() > top && event.getY() < bottom) {  
                // 点击EditText的事件，忽略它。  
                return false;  
            } else {  
                v.clearFocus();
                return true;  
            }  
        }  
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点  
        return false;  
    }  
  
    /** 
     * 多种隐藏软件盘方法的其中一种 
     *  
     * @param token 
     */  
    private void hideSoftInput(IBinder token) {  
        if (token != null) {  
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
            im.hideSoftInputFromWindow(token,  
                    InputMethodManager.HIDE_NOT_ALWAYS);  
        }  
    }  
}