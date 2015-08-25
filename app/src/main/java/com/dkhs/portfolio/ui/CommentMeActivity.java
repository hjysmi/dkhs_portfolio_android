package com.dkhs.portfolio.ui;

import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.CommentMeFragment;

public class CommentMeActivity extends ModelAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_me);
        setTitle(R.string.title_activity_comment_me);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL,new CommentMeFragment()).commitAllowingStateLoss();
    }

}
