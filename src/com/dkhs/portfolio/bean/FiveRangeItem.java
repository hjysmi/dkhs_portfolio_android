/**
 * @Title FiveRangeItem.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-23 下午9:02:39
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

/**
 * @ClassName FiveRangeItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-23 下午9:02:39
 * @version 1.0
 */
public class FiveRangeItem {
    public String tag;
    public String vol;
    public String price;

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "tag:" + tag + " price:" + price + " vol:" + vol;
    }
}
