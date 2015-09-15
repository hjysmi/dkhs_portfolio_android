package com.dkhs.portfolio.engine.LocalDataEngine.DBLoader;

import android.database.Cursor;
import android.os.Bundle;

/**
 * Created by zjz on 2015/9/10.
 */
public interface ICursorCreate {

    public Cursor createCusor(Bundle args);

    public Class<?> getCursorClass(Bundle args);
}
