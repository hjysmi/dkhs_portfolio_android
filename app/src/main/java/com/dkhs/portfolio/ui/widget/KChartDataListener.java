/**
 * @Title KChartLineListener.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-1 下午1:05:02
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import java.util.List;

import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;

/**
 * @ClassName KChartLineListener
 * @Description K线横竖屏，数据监听接口
 * @author zjz
 * @date 2015-4-1 下午1:05:02
 * @version 1.0
 */
public interface KChartDataListener {
    public List<OHLCEntity> getDayLineDatas();

    public void setDayKlineDatas(List<OHLCEntity> kLineDatas);

    public List<OHLCEntity> getMonthLineDatas();

    public void setMonthKlineDatas(List<OHLCEntity> kLineDatas);

    public List<OHLCEntity> getWeekLineDatas();

    public void setWeekKlineDatas(List<OHLCEntity> kLineDatas);
}
