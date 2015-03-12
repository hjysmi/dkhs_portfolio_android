package com.dkhs.portfolio.ui.draglist;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.FollowComEngineImpl;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.utils.PromptManager;

/***
 * 自定义适配器
 * 
 * @author zhangjia
 * 
 */
public class DragFundListAdapter extends BaseAdapter {
    private static final String TAG = "DragListAdapter";
    private List<CombinationBean> dataList = new ArrayList<CombinationBean>();
    // private ArrayList<Integer> arrayDrawables;
    private Context context;
    public boolean isHidden;
    private FollowComEngineImpl mQuotesEngine;
    private int station = 0;
    private int his = 0;

    // private DragListView mDragListView;

    public DragFundListAdapter(Context context, List<CombinationBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        // this.mDragListView = mDragListView;
        mQuotesEngine = new FollowComEngineImpl();
        // this.arrayDrawables = arrayDrawables;
    }

    public void showDropItem(boolean showItem) {
        this.ShowItem = showItem;
    }

    public void setInvisiblePosition(int position) {
        invisilePosition = position;
    }

    IHttpListener baseListener = new BasicHttpListener() {

        @Override
        public void onSuccess(String result) {
            try {
                PromptManager.closeProgressDialog();
                dataList.remove(station);
                notifyDataSetChanged();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /***
         * 在这里尽可能每次都进行实例化新的，这样在拖拽ListView的时候不会出现错乱.
         * 具体原因不明，不过这样经过测试，目前没有发现错乱。虽说效率不高，但是做拖拽LisView足够了。
         */
        convertView = LayoutInflater.from(context).inflate(R.layout.drag_list_item, null);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        TextView textView = (TextView) convertView.findViewById(R.id.drag_list_item_text);
        TextView tvId = (TextView) convertView.findViewById(R.id.drag_list_item_text_id);
        ImageView ivDrag = (ImageView) convertView.findViewById(R.id.drag_list_item_image);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        final Button btn = (Button) convertView.findViewById(R.id.button_delete);
        TextView txv = (TextView) convertView.findViewById(R.id.drag_text_delet_pad);
        ImageView imageUp = (ImageView) convertView.findViewById(R.id.drag_item_up);
        RelativeLayout layoutCover = (RelativeLayout) convertView.findViewById(R.id.drag_cover);
        // imageView.setImageResource(arrayDrawables.get(position));

        imageUp.setOnClickListener(new ClickForUp(position));
        image.setOnClickListener(new OnDele(btn, txv));
        btn.setOnClickListener(new Click(position, btn));
        textView.setText(dataList.get(position).getName());
        tvId.setText(dataList.get(position).getUser().getUsername());
        // layoutCover.setOnTouchListener(new OnCover(image,btn));
        if (isChanged) {
            // Log.i("wanggang", "position == " + position);
            // Log.i("wanggang", "holdPosition == " + invisilePosition);
            if (position == invisilePosition) {
                if (!ShowItem) {
                    convertView.findViewById(R.id.drag_list_item_text).setVisibility(View.INVISIBLE);
                    convertView.findViewById(R.id.drag_list_item_image).setVisibility(View.INVISIBLE);
                    convertView.findViewById(R.id.image).setVisibility(View.INVISIBLE);
                    convertView.findViewById(R.id.drag_list_item_text_id).setVisibility(View.INVISIBLE);
                    convertView.findViewById(R.id.drag_item_up).setVisibility(View.INVISIBLE);
                    // convertView.findViewById(R.id.check_del).setVisibility(View.INVISIBLE);
                    // convertView.setVisibility(View.INVISIBLE);
                }
            }
            if (lastFlag != -1) {
                if (lastFlag == 1) {
                    if (position > invisilePosition) {
                        Animation animation;
                        animation = getFromSelfAnimation(0, -height);
                        convertView.startAnimation(animation);
                    }
                } else if (lastFlag == 0) {
                    if (position < invisilePosition) {
                        Animation animation;
                        animation = getFromSelfAnimation(0, height);
                        convertView.startAnimation(animation);
                    }
                }
            }

            // if(lastFlag != -1){
            // if(lastFlag == 1){
            // if(position < invisilePosition){
            // if(position == invisilePosition - 1){
            // convertView.findViewById(R.id.drag_list_item_text).setVisibility(View.INVISIBLE);
            // convertView.findViewById(R.id.drag_list_item_image).setVisibility(View.INVISIBLE);
            // convertView.findViewById(R.id.check_del).setVisibility(View.INVISIBLE);
            // }
            // // Animation animation;
            // // if(isSameDragDirection){
            // // animation = getToSelfAnimation(0, height);
            // // }else{
            // // animation = getFromSelfAnimation(0, -height);
            // // }
            // // convertView.startAnimation(animation);
            // }
            // }else{
            //
            // }
            // }
        }
        return convertView;
    }

    class OnDele implements OnClickListener {
        Button btn;
        TextView tv;

        public OnDele(Button btn, TextView tv) {
            this.btn = btn;
            this.tv = tv;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            v.setVisibility(View.GONE);
            btn.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
        }

    }

    public List<CombinationBean> getList() {
        return dataList;
    }

    class OnCover implements OnTouchListener {
        ImageView iv;
        Button bt;

        public OnCover(ImageView iv, Button bt) {
            this.iv = iv;
            this.bt = bt;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setVisibility(View.GONE);
                iv.setVisibility(View.VISIBLE);
                bt.setVisibility(View.GONE);
            }
            return false;
        }

    }

    public void setAnima(int startX, int endX, LinearLayout ly) {
        Animation animation = null;
        animation = new TranslateAnimation(startX, endX, 0, 0);
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(300);
        ly.startAnimation(animation);
    }

    class Click implements OnClickListener {
        int position;
        Button btn;

        public Click(int position, Button btn) {
            this.position = position;
            this.btn = btn;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            PromptManager.showProgressDialog(context, null);
            // PromptManager.showToast("删除自选组合");
            if (PortfolioApplication.getInstance().hasUserLogin()) {

                mQuotesEngine.defFollowCombinations(dataList.get(position).getId(), baseListener);
            } else {
                new VisitorDataEngine().delCombinationBean(dataList.get(position));
                PromptManager.closeProgressDialog();
                dataList.remove(station);
                notifyDataSetChanged();
            }
            // 未完成
            // mQuotesEngine.delfollow(dataList.get(position).id, baseListener);
            station = position;
            // dataList.remove(station);
            // notifyDataSetChanged();
        }

    }

    class ClickForUp implements OnClickListener {
        int position;

        public ClickForUp(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            putUp(position);
        }

    }

    /***
     * 动态修改ListVIiw的方位.
     * 
     * @param start
     * 点击移动的position
     * @param down
     * 松开时候的position
     */
    private int invisilePosition = -1;
    private boolean isChanged = true;
    private boolean ShowItem = false;

    public void exchange(int startPosition, int endPosition) {
        System.out.println(startPosition + "--" + endPosition);
        // holdPosition = endPosition;
        CombinationBean startObject = getItem(startPosition);
        System.out.println(startPosition + "========" + endPosition);
        Log.d("ON", "startPostion ==== " + startPosition);
        Log.d("ON", "endPosition ==== " + endPosition);
        if (startPosition < endPosition) {
            dataList.add(endPosition + 1, startObject);
            dataList.remove(startPosition);
        } else {
            dataList.add(endPosition, startObject);
            dataList.remove(startPosition + 1);
        }
        isChanged = true;
        // notifyDataSetChanged();
    }

    public void exchangeCopy(int startPosition, int endPosition) {
        System.out.println(startPosition + "--" + endPosition);
        // holdPosition = endPosition;
        CombinationBean startObject = getCopyItem(startPosition);
        System.out.println(startPosition + "========" + endPosition);
        Log.d("ON", "startPostion ==== " + startPosition);
        Log.d("ON", "endPosition ==== " + endPosition);
        if (startPosition < endPosition) {
            mCopyList.add(endPosition + 1, startObject);
            mCopyList.remove(startPosition);
        } else {
            mCopyList.add(endPosition, startObject);
            mCopyList.remove(startPosition + 1);
        }
        isChanged = true;
        // notifyDataSetChanged();
    }

    public CombinationBean getCopyItem(int position) {
        return mCopyList.get(position);
    }

    @Override
    public int getCount() {
        return dataList.size();
        // return 4;
    }

    @Override
    public CombinationBean getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addDragItem(int start, CombinationBean obj) {
        Log.i(TAG, "start" + start);
        CombinationBean title = dataList.get(start);
        dataList.remove(start);// 删除该项
        dataList.add(start, obj);// 添加删除项
    }

    private ArrayList<CombinationBean> mCopyList = new ArrayList<CombinationBean>();

    public void copyList() {
        mCopyList.clear();
        for (CombinationBean str : dataList) {
            mCopyList.add(str);
        }
    }

    public void putUp(int position) {
        CombinationBean tmp = dataList.get(position);
        mCopyList.clear();
        mCopyList.add(tmp);
        dataList.remove(position);
        mCopyList.addAll(dataList);
        dataList.clear();
        dataList.addAll(mCopyList);
        notifyDataSetChanged();
    }

    public void pastList() {
        dataList.clear();
        for (CombinationBean str : mCopyList) {
            dataList.add(str);
        }
    }

    private boolean isSameDragDirection = true;
    private int lastFlag = -1;
    private int height;
    private int dragPosition = -1;

    public void setIsSameDragDirection(boolean value) {
        isSameDragDirection = value;
    }

    public void setLastFlag(int flag) {
        lastFlag = flag;
    }

    public void setHeight(int value) {
        height = value;
    }

    public void setCurrentDragPosition(int position) {
        dragPosition = position;
    }

    public Animation getFromSelfAnimation(int x, int y) {
        TranslateAnimation go = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, x,
                Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y);
        go.setInterpolator(new AccelerateDecelerateInterpolator());
        go.setFillAfter(true);
        go.setDuration(100);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }

    public Animation getToSelfAnimation(int x, int y) {
        TranslateAnimation go = new TranslateAnimation(Animation.ABSOLUTE, x, Animation.RELATIVE_TO_SELF, 0,
                Animation.ABSOLUTE, y, Animation.RELATIVE_TO_SELF, 0);
        go.setInterpolator(new AccelerateDecelerateInterpolator());
        go.setFillAfter(true);
        go.setDuration(100);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }
}
