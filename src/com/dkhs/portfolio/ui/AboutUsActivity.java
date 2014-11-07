/**
 * @Title AboutUsActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-7 下午2:32:21
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import com.dkhs.portfolio.R;

import android.os.Bundle;

/**
 * @ClassName AboutUsActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-11-7 下午2:32:21
 * @version 1.0
 */
public class AboutUsActivity extends ModelAcitivity {
    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param arg0
     * @return
     */
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setTitle("关于我们");
        setContentView(R.layout.activity_about_us);
    }

}
