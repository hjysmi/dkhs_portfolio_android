/**   
 * @Title FragmentCompare.java 
 * @Package com.dkhs.portfolio.ui.fragment 
 * @Description TODO(用一句话描述该文件做什么) 
 * @author zjz  
 * @date 2014-9-3 上午9:32:29 
 * @version V1.0   
 */
package com.dkhs.portfolio.ui.fragment;

import com.dkhs.portfolio.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/** 
 * @ClassName FragmentCompare 
 * @Description TODO(这里用一句话描述这个类的作用) 
 * @author zjz 
 * @date 2014-9-3 上午9:32:29 
 * @version 1.0 
 */
public class FragmentCompare extends Fragment {
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compare, null);
        return view;
    }
}
