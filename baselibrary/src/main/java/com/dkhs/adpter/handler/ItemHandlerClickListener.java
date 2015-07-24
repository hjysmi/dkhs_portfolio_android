package com.dkhs.adpter.handler;

import android.view.View;

/**
 * @author zwm
 * @version 2.0
 * @ClassName ItemHandlerClickListenerI
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/22.
 */
public interface ItemHandlerClickListener<T extends Object> {
        public View.OnClickListener setDate(T o);
}
