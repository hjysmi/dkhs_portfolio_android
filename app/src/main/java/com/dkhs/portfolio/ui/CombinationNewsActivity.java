/**
 * @Title CompareFundsActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-5-6 下午1:34:22
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.ui.fragment.FragmentCombinationNews;

/**
 * @author zjz
 * @version 1.0
 * @ClassName CompareFundsActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-5-6 下午1:34:22
 */
public class CombinationNewsActivity extends ModelAcitivity {

    public static Intent newIntent(Context context, CombinationBean combinationBean) {
        Intent intent = new Intent(context, CombinationNewsActivity.class);

        intent.putExtra(NewCombinationDetailActivity.EXTRA_COMBINATION, combinationBean);

        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.related_news);
//        replaceCompareView();
        replaceContentFragment(FragmentCombinationNews.newInstance());
    }
}
