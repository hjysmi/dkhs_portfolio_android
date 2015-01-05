/**
 * @Title GridViewEx.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-12-22 上午11:08:12
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.GridView;

/**
 * @ClassName GridViewEx
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-12-22 上午11:08:12
 * @version 1.0
 */
public class GridViewEx extends GridView {

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param context
     */
    public GridViewEx(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public GridViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
