/**
 * @Title IBaseDataSource.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-10 下午2:05:28
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.List;

/**
 * @ClassName IBaseDataSource
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-10 下午2:05:28
 * @version 1.0
 */
public interface IBaseDataSource<T> {

    public void setDataList(List<T> list);

    public List<T> getAll();
}
