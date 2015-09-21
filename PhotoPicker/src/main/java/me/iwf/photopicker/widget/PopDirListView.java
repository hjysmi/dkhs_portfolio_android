package me.iwf.photopicker.widget;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import me.iwf.photopicker.R;
import me.iwf.photopicker.adapter.PopupDirectoryListAdapter;
import me.iwf.photopicker.entity.PhotoDirBean;
import me.iwf.photopicker.fragment.PhotoPickerFragment;

/**
 * Created by zjz on 2015/9/11.
 * 文件夹列表UI
 * 浮动显示相册目录
 */
public class PopDirListView {

    private PopupDirectoryListAdapter listAdapter;
    private List<PhotoDirBean> directories;
    private Context mContext;
    private View dirPopWindow;
    private OnPopItemClickListener itemClickListener;

    public PopDirListView(Context context, List<PhotoDirBean> directories, View parenview) {
        this.directories = directories;
        this.mContext = context;
        listAdapter = new PopupDirectoryListAdapter(context, directories);
        initPopwindow(parenview);
    }


    private void initPopwindow(View rootView) {
        dirPopWindow = rootView.findViewById(R.id.pop_view);
        ListView lv_profit_loss = (ListView) dirPopWindow.findViewById(R.id.lv_more);
        lv_profit_loss.setAdapter(listAdapter);
        lv_profit_loss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != itemClickListener) {
                    itemClickListener.onItemClick(position);
                }
            }
        });
        dirPopWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }


    public void dismiss() {
        if (null != dirItemClickListener) {
            dirItemClickListener.onDirListHide();
        }
        Animation bottomUp = AnimationUtils.loadAnimation(mContext,
                R.anim.dir_pop_out);
        dirPopWindow.findViewById(R.id.lv_more).startAnimation(bottomUp);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dirPopWindow.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        dirPopWindow.startAnimation(alphaAnimation);
    }

    public void show() {
        if (null != dirItemClickListener) {
            dirItemClickListener.onDirListShow();
        }
        Animation bottomUp = AnimationUtils.loadAnimation(mContext,
                R.anim.dir_pop_show);
        dirPopWindow.findViewById(R.id.lv_more).startAnimation(bottomUp);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1f);
        alphaAnimation.setDuration(300);
        dirPopWindow.startAnimation(alphaAnimation);
        dirPopWindow.setVisibility(View.VISIBLE);
    }

    public void toggle() {
        if (isShown()) {
            dismiss();
        } else {
            show();
        }
    }

    public boolean isShown() {
        return dirPopWindow.isShown();
    }

    public PopupDirectoryListAdapter getListAdapter()

    {
        return listAdapter;
    }


    public OnPopItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    private PhotoPickerFragment.DirItemClickListener dirItemClickListener;

    public void setDirItemClickListener(PhotoPickerFragment.DirItemClickListener dirListener) {
        this.dirItemClickListener = dirListener;
    }

    public void setItemClickListener(OnPopItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnPopItemClickListener {
        public void onItemClick(int position);
    }
}
