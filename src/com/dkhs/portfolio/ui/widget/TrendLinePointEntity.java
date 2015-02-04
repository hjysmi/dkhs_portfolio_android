/**
 * @Title FSLinePointEntity.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-17 下午3:53:57
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName FSLinePointEntity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-17 下午3:53:57
 * @version 1.0
 */
public class TrendLinePointEntity extends LinePointEntity {
    private String time;
    private float increaseRange;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getIncreaseRange() {
        return increaseRange;
    }

    public String getIncreaseRangeDesc() {
        return "涨幅: " + StringFromatUtils.get2PointPercent(increaseRange);
    }

    public String getDataDesc() {
        return "当前净值: " + StringFromatUtils.get4Point(getValue());
    }

    public void setIncreaseRange(float increaseRange) {
        this.increaseRange = increaseRange;
    }

}
