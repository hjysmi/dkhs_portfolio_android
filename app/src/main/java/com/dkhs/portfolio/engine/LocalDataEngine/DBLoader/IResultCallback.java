package com.dkhs.portfolio.engine.LocalDataEngine.DBLoader;

import java.util.List;

/**
 * Created by zjz on 2015/9/10.
 */
public interface IResultCallback<T> {
    void onResultCallback(List<T> resultList);
}
