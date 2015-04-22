/**
 * @Title KChartsLandCallBack.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-1 上午10:06:39
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import java.util.List;

import android.view.View;

import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;

/**
 * @ClassName KChartsLandCallBack
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-1 上午10:06:39
 * @version 1.0
 */
public interface KChartsLandCallBack {

    public void loadMore();

    void onDisplayDataChange(List<OHLCEntity> entitys);

    void onLoadMoreDataStart();

    void onLoadMoreDataEnd();

    void onDoubleClick(View view);

}
