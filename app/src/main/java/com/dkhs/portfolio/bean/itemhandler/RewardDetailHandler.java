package com.dkhs.portfolio.bean.itemhandler;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ReplacementSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dkhs.adpter.handler.ItemHandlerClickListenerImp;
import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.common.Spanny;
import com.dkhs.portfolio.engine.TopicsCommendEngineImpl;
import com.dkhs.portfolio.ui.PhotoViewActivity;
import com.dkhs.portfolio.ui.PostTopicActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.UserHomePageActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.TopicsDetailRefreshEvent;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.mingle.bean.PhotoBean;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */

public class RewardDetailHandler extends SimpleItemHandler<TopicsBean> implements AdapterView.OnItemSelectedListener {


    private Context mContext;
    private TopicsCommendEngineImpl.SortType mSortType;
    private CommentBean mCommentBean;

    public RewardDetailHandler(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_reward_detail;
    }

    public void setCommentBean(CommentBean commentBean){
        mCommentBean = commentBean;
    }


    @Override
    public void onBindView(final ViewHolder vh, final TopicsBean data, int position) {
        setClickListener(vh.get(R.id.iv_avatar), data);
//        setClickListener(vh.get(R.id.iv), data);

        setClickListener(vh.get(R.id.name), data);
        if (TextUtils.isEmpty(data.title)) {
            vh.get(R.id.titleTV).setVisibility(View.GONE);
        } else {
            vh.get(R.id.titleTV).setVisibility(View.VISIBLE);
            vh.setTextView(R.id.titleTV, data.title);
        }

        PeopleBean user = data.user;
        if (null != user) {
            vh.setTextView(R.id.name, user.getUsername());
        }
        if(TextUtils.isEmpty(data.text)){
            vh.get(R.id.content).setVisibility(View.GONE);
        }else {
            vh.get(R.id.content).setVisibility(View.VISIBLE);
        }
        vh.setTextView(R.id.content, data.text);
//        vh.get(R.id.iv).setVisibility(View.GONE);


        TopicsImageViewHandler topicsImageViewHandler;
        if(vh.get(R.id.titleTV).getTag()!= null && vh.get(R.id.titleTV).getTag() instanceof TopicsImageViewHandler){
            topicsImageViewHandler= (TopicsImageViewHandler) vh.get(R.id.titleTV).getTag();
        }else {
              topicsImageViewHandler=new TopicsImageViewHandler();

            vh.get(R.id.titleTV).setTag(topicsImageViewHandler);
        }
        topicsImageViewHandler.handleMedias(vh, data, true);
        vh.setTextView(R.id.tv_like, mContext.getString(R.string.like) + " " + data.attitudes_count);
        vh.setTextView(R.id.comment, mContext.getString(R.string.answer) + " " + data.comments_count);

        if (data.state == -1) {
            vh.setTextView(R.id.tv_empty, mContext.getString(R.string.topics_already_delete));
            vh.get(R.id.main_ll).setVisibility(View.GONE);
            vh.get(R.id.emptyRl).setVisibility(View.VISIBLE);
        } else {
            vh.get(R.id.main_ll).setVisibility(View.VISIBLE);
            vh.get(R.id.emptyRl).setVisibility(View.GONE);
        }
        /**
         *  CONTENT_TYPE = (
         (0, '话题'),
         (10, '新闻'),
         (20, '公告'),
         (30, '研报'),
         )
         */
        if (data.content_type != 0) {
            setRelatedSymbols(vh.getTextView(R.id.relatedSymbolsTV), data.symbols);
            vh.setTextView(R.id.tv_time, TimeUtils.getBriefTimeString(data.publish_at) + getFromOrigin(data.source));

            switch (data.content_type) {
                case 10:
                    vh.getImageView(R.id.iv_avatar).setImageResource(R.drawable.ic_announcement);
                    break;
                case 20:
                    // FIXME: 2015/8/12 新闻图标暂缺
                    vh.getImageView(R.id.iv_avatar).setImageResource(R.drawable.ic_announcement);
                    break;
                case 30:
                    vh.getImageView(R.id.iv_avatar).setImageResource(R.drawable.ic_yanbao);
                    break;
            }
        } else {
            vh.setTextView(R.id.tv_time, TimeUtils.getBriefTimeString(data.created_at));
            vh.getTextView(R.id.relatedSymbolsTV).setVisibility(View.GONE);
            if (user != null && !TextUtils.isEmpty(user.getAvatar_md())) {
                ImageLoaderUtils.setHeanderImage(user.getAvatar_md(), vh.getImageView(R.id.iv_avatar));
            } else {
                vh.getImageView(R.id.iv_avatar).setImageResource(R.drawable.ic_user_head);
            }
        }

        final Spinner spinner = vh.get(R.id.spinner);


        if (spinner.getAdapter() == null) {
            spinner.setAdapter(new ArrayAdapter<String>(mContext, R.layout.item_spinner, mContext.getResources().getStringArray(R.array.rewards_reply_sort)));
            spinner.setOnItemSelectedListener(this);
        }

        if (mSortType != null) {
            switch (mSortType) {
                case latest:
                    spinner.setSelection(0);
                    break;
                case best:
                    spinner.setSelection(1);
                    break;
                case earliest:
                    spinner.setSelection(2);
                    break;
            }
        }
        vh.getTextView(R.id.tv_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.getTextView(R.id.tv_like).setTextColor(v.getResources().getColor(R.color.theme_color));
                vh.getTextView(R.id.comment).setTextColor(v.getResources().getColor(R.color.tag_gray));
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(vh.get(R.id.indicate), "translationX", (v.getLeft() + v.getWidth() / 2 - vh.get(R.id.indicate).getWidth() / 2));
                objectAnimator.setDuration(200);
                objectAnimator.start();
                TopicsDetailRefreshEvent topicsDetailRefreshEvent = new TopicsDetailRefreshEvent();
                mSortType = TopicsCommendEngineImpl.SortType.like;
                topicsDetailRefreshEvent.sortType = mSortType;
                BusProvider.getInstance().post(topicsDetailRefreshEvent);
                spinner.setVisibility(View.INVISIBLE);
            }
        });
        vh.getTextView(R.id.comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                vh.getTextView(R.id.comment).setTextColor(v.getResources().getColor(R.color.theme_color));
                vh.getTextView(R.id.tv_like).setTextColor(v.getResources().getColor(R.color.tag_gray));

                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(vh.get(R.id.indicate), "translationX", (v.getLeft() + v.getWidth() / 2 - vh.get(R.id.indicate).getWidth() / 2));
                objectAnimator.setDuration(200);
                objectAnimator.start();
                postRefreshEvent(spinner.getSelectedItemPosition());
                spinner.setVisibility(View.VISIBLE);


            }
        });
        if (mSortType == TopicsCommendEngineImpl.SortType.like) {

            vh.getTextView(R.id.tv_like).setTextColor(vh.getConvertView().getResources().getColor(R.color.theme_color));
            vh.getTextView(R.id.comment).setTextColor(vh.getConvertView().getResources().getColor(R.color.tag_gray));
            vh.getTextView(R.id.comment).post(new Runnable() {
                @Override
                public void run() {
                    ViewHelper.setTranslationX(vh.get(R.id.indicate), vh.getTextView(R.id.tv_like).getLeft() + vh.getTextView(R.id.tv_like).getWidth() / 2 - vh.get(R.id.indicate).getWidth() / 2);

                }
            });
        } else {
            vh.getTextView(R.id.tv_like).setTextColor(vh.getConvertView().getResources().getColor(R.color.tag_gray));
            vh.getTextView(R.id.comment).setTextColor(vh.getConvertView().getResources().getColor(R.color.theme_color));

            vh.getTextView(R.id.comment).post(new Runnable() {
                @Override
                public void run() {
                    ViewHelper.setTranslationX(vh.get(R.id.indicate), vh.getTextView(R.id.comment).getLeft() + vh.getTextView(R.id.comment).getWidth() / 2 - vh.get(R.id.indicate).getWidth() / 2);

                }
            });
        }

        TextView stateTv = vh.getTextView(R.id.tv_reward_state);
        TextView amountTv = vh.getTextView(R.id.tv_reward_amount);
        TextView amountUnit = vh.getTextView(R.id.tv_reward_amount_unit);
        ImageView moneyIv = vh.getImageView(R.id.iv_money);
        int state;
        int amountStyle;
        int unitStyle;
        int leftDrawable;
        if(data.reward_state == 0){
            state = R.string.reward_on_going;
            amountStyle = R.style.reward_amount_on_going;
            unitStyle = R.style.reward_unit_on_going;
            leftDrawable = R.drawable.ic_money_highlight;
            stateTv.setBackgroundResource(R.drawable.bg_reward_on_going);
        }else if(data.reward_state == 1){
            state = R.string.reward_close;
            amountStyle = R.style.reward_amount_finish;
            unitStyle = R.style.reward_unit_finish;
            leftDrawable = R.drawable.ic_money_normal;
            stateTv.setBackgroundResource(R.drawable.bg_reward_finish);
        }else{
            state =  R.string.reward_finish;
            amountStyle = R.style.reward_amount_finish;
            unitStyle = R.style.reward_unit_finish;
            leftDrawable = R.drawable.ic_money_normal;
            stateTv.setBackgroundResource(R.drawable.bg_reward_finish);
        }
        stateTv.setText(state);
        amountTv.setTextAppearance(mContext, amountStyle);
        amountUnit.setTextAppearance(mContext, unitStyle);
        moneyIv.setImageResource(leftDrawable);
        amountTv.setText(data.reward_amount);



    }



    private void setRelatedSymbols(TextView textView, List<TopicsBean.SymbolsBean> symbols) {

        if (symbols != null && symbols.size() > 0) {
            Spanny spany = new Spanny();
            spany.append("相关股票: ", new ForegroundColorSpan(mContext.getResources().getColor(R.color.tag_gray)));

            for (int i = 0; i < symbols.size(); i++) {
                TopicsBean.SymbolsBean item = symbols.get(i);
                int star = spany.length();
                spany.append(" " + item.abbr_name + " ", new SymbolsClickSpan(item));
//                spany.setSpan(new RoundSpan(mContext), star, spany.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(spany);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }


    }

    private String getFromOrigin(TopicsBean.SourceBean source) {

        String origin = "";
        if (source != null) {
            origin = origin.concat(" 来自:").concat(source.title);
        }
        return origin;
    }

    public void setClickListener(View view, TopicsBean data) {
        ItemHandlerClickListenerImp<TopicsBean> itemHandlerClickListener = null;
        if (null != view.getTag() && view.getTag() instanceof ItemHandlerClickListenerImp) {
            itemHandlerClickListener = (ItemHandlerClickListenerImp<TopicsBean>) view.getTag();
        } else {
            switch (view.getId()) {
                case R.id.fl_star:
                    itemHandlerClickListener = new StarClickListenerImp();
                    break;
                case R.id.fl_commend:
                    itemHandlerClickListener = new CommendClickListenerImp();
                    break;
                case R.id.iv_avatar:
                case R.id.name:
                    itemHandlerClickListener = new AvatarClickListenerImp();
                    break;
                case R.id.iv:
                    itemHandlerClickListener = new ImageViewClickListenerImp();
                    break;
                case R.id.main_ll:
                    itemHandlerClickListener = new ItemClickListenerImp();
                    break;
                default:
                    itemHandlerClickListener = new ItemHandlerClickListenerImp<TopicsBean>();
                    break;
            }
            view.setOnClickListener(itemHandlerClickListener);
            view.setTag(itemHandlerClickListener);
        }

        itemHandlerClickListener.setDate(data);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        postRefreshEvent(position);
    }

    private void postRefreshEvent(int position) {
        TopicsDetailRefreshEvent topicsDetailRefreshEvent = new TopicsDetailRefreshEvent();
        switch (position) {
            case 0:
                topicsDetailRefreshEvent.sortType = TopicsCommendEngineImpl.SortType.latest;
                break;
            case 1:
                topicsDetailRefreshEvent.sortType = TopicsCommendEngineImpl.SortType.best;
                break;
            case 2:
                topicsDetailRefreshEvent.sortType = TopicsCommendEngineImpl.SortType.earliest;
                break;
        }

        if (mSortType != topicsDetailRefreshEvent.sortType) {
            mSortType = topicsDetailRefreshEvent.sortType;
            BusProvider.getInstance().post(topicsDetailRefreshEvent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class StarClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {
        private TopicsBean topicsBean;

        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            ImageView imageView = (ImageView) v.findViewById(R.id.iv_like);
            if (topicsBean.like) {
                imageView.setImageResource(R.drawable.praised);
            } else {
                imageView.setImageResource(R.drawable.praise);
            }
            topicsBean.like = !topicsBean.like;
        }
    }

    class CommendClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {


        private TopicsBean topicsBean;


        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {

            if (UIUtils.iStartLoginActivity(mContext)) {
                return;
            }
            UIUtils.startAnimationActivity((Activity) mContext, (PostTopicActivity.getIntent(mContext, PostTopicActivity.TYPE_COMMENT, topicsBean.id + "", topicsBean.user.getUsername())));
        }
    }

    class AvatarClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {
        private TopicsBean topicsBean;

        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            if (topicsBean.user != null) {
                UIUtils.startAnimationActivity((Activity) mContext,
                        UserHomePageActivity.getIntent(mContext, topicsBean.user.getUsername(), topicsBean.user.getId() + ""));

            }
        }
    }

    class ImageViewClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {
        private TopicsBean topicsBean;

        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            ArrayList<PhotoBean> arrayList = new ArrayList<>();
            PhotoBean photoBean = new PhotoBean();
            photoBean.title = topicsBean.id + "";
            photoBean.loadingURl = topicsBean.medias.get(0).getImage_sm();
            photoBean.imgUrl = topicsBean.medias.get(0).getImage_md();
            arrayList.add(photoBean);
            PhotoViewActivity.startPhotoViewActivity(mContext, arrayList, v, 0);
        }
    }

    class ItemClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {

        private TopicsBean topicsBean;

        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            TopicsDetailActivity.startActivity(mContext, topicsBean);
        }
    }


    class SymbolsClickSpan extends ClickableSpan {

        TopicsBean.SymbolsBean symbolsBean;

        public SymbolsClickSpan(TopicsBean.SymbolsBean symbolsBean) {
            this.symbolsBean = symbolsBean;
        }

        @Override
        public void onClick(View widget) {
            SelectStockBean selectStockBean = new SelectStockBean();
            selectStockBean.setName(symbolsBean.abbr_name);
            selectStockBean.setId(symbolsBean.id);
            selectStockBean.setSymbol(symbolsBean.symbol);
            //设置类型为股票
            selectStockBean.setSymbol_type("1");
            mContext.startActivity(StockQuotesActivity.newIntent(mContext, selectStockBean));

        }

        /**
         * Makes the text without underline.
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(mContext.getResources().getColor(R.color.blue));
            ds.setUnderlineText(false);
        }

    }


    public class RoundSpan extends ReplacementSpan {


        private Context mContext;

        public RoundSpan(Context context) {
            mContext = context;
        }

        @Override
        public int getSize(Paint paint,
                           CharSequence text,
                           int start, int end,
                           Paint.FontMetricsInt fm) {
            return (int) MeasureText(paint, text, start, end);
        }

        private float MeasureText(Paint paint, CharSequence text, int start, int end) {
            return paint.measureText(text, start, end);
        }

        @Override
        public void draw(Canvas canvas,
                         CharSequence text,
                         int start, int end,
                         float x, int top,
                         int y, int bottom,
                         Paint paint) {

            RectF rect = new RectF(x, top, x + MeasureText(paint, text, start, end), bottom);
            paint.setColor(mContext.getResources().getColor(R.color.activity_bg_color));
            canvas.drawRoundRect(rect, mContext.getResources().getDimensionPixelOffset(R.dimen.radius)
                    , mContext.getResources().getDimensionPixelOffset(R.dimen.radius),
                    paint);
            paint.setColor(mContext.getResources().getColor(R.color.tag_gray));
            canvas.drawText(text, start, end, x, y, paint);
        }
    }


}
