package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.dkhs.portfolio.R;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.emoji.Emojicon;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangcm on 2015/7/21.
 */
public class DKHSEmojiFragment extends Fragment implements AdapterView.OnItemClickListener {

    private OnEmojiconBackspaceClickedListener mOnEmojiconBackspaceClickedListener;
    private OnEmojiconClickedListener mOnEmojiconClickedListener;

    private static final String EMOJI_DATA = "emoji_data";
    private List<Emojicon> emojis;
    public  static DKHSEmojiFragment newInstance(List<Emojicon> emojis) {
        DKHSEmojiFragment fragment = new DKHSEmojiFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(EMOJI_DATA, emojis.toArray(new Emojicon[emojis.size()]));
        fragment.setArguments(arguments);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dkhs_emoji, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        GridView gvEmoji = (GridView) view.findViewById(R.id.gv_emoji);
        Bundle bundle = getArguments();
        Emojicon[] o = (Emojicon[]) getArguments().getSerializable(EMOJI_DATA);
        emojis = Arrays.asList(o);
        gvEmoji.setOnItemClickListener(this);
        gvEmoji.setAdapter(new EmojiAdapter());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == emojis.size() -1){
            if(mOnEmojiconBackspaceClickedListener != null)
                mOnEmojiconBackspaceClickedListener.onEmojiconBackSpaceClicked(emojis.get(position));
        }else{
            if(mOnEmojiconClickedListener != null)
                mOnEmojiconClickedListener.onEmojiconClicked(emojis.get(position));
        }
    }

    public class EmojiAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return emojis.size();
        }

        @Override
        public Object getItem(int position) {
            return emojis.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            v = convertView;
            if(v == null || convertView instanceof ImageView){
                v = View.inflate(getActivity(), com.rockerhieu.emojicon.R.layout.emojicon_item, null);
                ViewHolder holder = new ViewHolder();
                holder.icon = (EmojiconTextView) v.findViewById(com.rockerhieu.emojicon.R.id.emojicon_icon);
                v.setTag(holder);
            }
            ViewHolder holder = (ViewHolder) v.getTag();
            Emojicon emoji = emojis.get(position);;
            holder.icon.setText(emoji.getEmoji());
            return v;
        }

        class ViewHolder {
            EmojiconTextView icon;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getActivity() instanceof OnEmojiconBackspaceClickedListener) {
            mOnEmojiconBackspaceClickedListener = (OnEmojiconBackspaceClickedListener) getActivity();
        } else if(getParentFragment() instanceof  OnEmojiconBackspaceClickedListener) {
            mOnEmojiconBackspaceClickedListener = (OnEmojiconBackspaceClickedListener) getParentFragment();
        } else {
            throw new IllegalArgumentException(activity + " must implement interface " + OnEmojiconBackspaceClickedListener.class.getSimpleName());
        }
        if (activity instanceof OnEmojiconClickedListener) {
            mOnEmojiconClickedListener = (OnEmojiconClickedListener) activity;
        } else if (getParentFragment() instanceof OnEmojiconClickedListener) {
            mOnEmojiconClickedListener = (OnEmojiconClickedListener) getParentFragment();
        } else {
            throw new IllegalArgumentException(activity + " must implement interface " + OnEmojiconClickedListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        mOnEmojiconBackspaceClickedListener = null;
        mOnEmojiconClickedListener = null;
        super.onDetach();
    }

    public interface OnEmojiconClickedListener {
        void onEmojiconClicked(Emojicon emojicon);
    }
    public interface OnEmojiconBackspaceClickedListener {
        void onEmojiconBackSpaceClicked(Emojicon emojicon);
    }

    public static void backspace(EditText editText) {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }

    public static void input(EditText editText, Emojicon emojicon) {
        if (editText == null || emojicon == null) {
            return;
        }

        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start < 0) {
            editText.append(emojicon.getEmoji());
        } else {
            editText.getText().replace(Math.min(start, end), Math.max(start, end), emojicon.getEmoji(), 0, emojicon.getEmoji().length());
        }
    }
}
