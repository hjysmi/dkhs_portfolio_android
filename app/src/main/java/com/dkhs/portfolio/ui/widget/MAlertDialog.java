package com.dkhs.portfolio.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.animator.BaseEffects;
import com.dkhs.portfolio.animator.Effectstype;


/**
 * 自定义的Dialog  主要是为了更美观
 * Created by Administrator on 2014/8/13.
 */
@SuppressWarnings("unused")
public class MAlertDialog {


    private final TextView alertTitle;
    private final TextView message;
    private final FrameLayout customPanel;
    private final Button button1;
    private final Button button2;
    private final Button button3;
    private final RelativeLayout main;
    private final View topPanel;
    private final View bottomPanel;
    protected Context context;
    private int mDuration = 500;
    RelativeLayout title_template;

    private Dialog dialog;

    public MAlertDialog(Context context) {
        //初始化
        this.context = context;
        dialog = new Dialog(context, R.style.custom_dialog);
        View root = LayoutInflater.from(context).inflate(R.layout.layout_dialog, null);

        alertTitle = (TextView) root.findViewById(R.id.alertTitle);
        title_template = (RelativeLayout) root.findViewById(R.id.title_template);
//        View titleDivider = (View) root.findViewById(R.id.titleDivider);
        topPanel = (LinearLayout) root.findViewById(R.id.topPanel);
        bottomPanel = (LinearLayout) root.findViewById(R.id.bottomPanel);
        message = (TextView) root.findViewById(R.id.message);
        LinearLayout contentPanel = (LinearLayout) root.findViewById(R.id.contentPanel);
        customPanel = (FrameLayout) root.findViewById(R.id.customPanel);
        button1 = (Button) root.findViewById(R.id.button1);
        button2 = (Button) root.findViewById(R.id.button2);
        button3 = (Button) root.findViewById(R.id.button3);
        LinearLayout parentPanel = (LinearLayout) root.findViewById(R.id.parentPanel);
        main = (RelativeLayout) root.findViewById(R.id.main);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(context.getResources().getDisplayMetrics().widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(root, params);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                start(Effectstype.RotateBottom);
            }
        });

    }

    public MAlertDialog setCustomView(View view) {
        message.setVisibility(View.GONE);
        if (customPanel.getChildCount() > 0) {
            customPanel.removeAllViews();
        }
        customPanel.addView(view);
        return this;
    }

    public MAlertDialog setCancelable(boolean flag) {
        dialog.setCancelable(flag);
        return this;
    }

    public void hideTitle() {
        title_template.setVisibility(View.GONE);
    }
    public void hideBottom() {
        bottomPanel.setVisibility(View.GONE);
    }
    public void hideMessage() {
        message.setVisibility(View.GONE);
    }

    private void start(Effectstype type) {
        BaseEffects animator = type.getAnimator();
        if (mDuration != -1) {
            animator.setDuration(Math.abs(mDuration));
        }
        animator.start(main);
    }

    public void setContentView(View view) {
        this.setCustomView(view);
    }


    public MAlertDialog setCanceledOnTouchOutside(boolean enable) {
        dialog.setCanceledOnTouchOutside(enable);
        return this;
    }

    public MAlertDialog setNoTitle() {
        return this;
    }

    public MAlertDialog setMessage(String message) {
        this.message.setText(message);
        this.message.setVisibility(View.VISIBLE);
        return this;
    }

    public MAlertDialog setMessage(int message) {
        this.message.setText(message);
        this.message.setVisibility(View.VISIBLE);
        return this;
    }


    public MAlertDialog setTitle(String title) {
        alertTitle.setText(title);
        return this;
    }

    public MAlertDialog setTitle(int title) {
        alertTitle.setText(title);
        return this;
    }


    public MAlertDialog setSingleChoiceItems(CharSequence[] items, final int checkedItem,
                                             final DialogInterface.OnClickListener listener) {


        final ListView listView = (ListView)
                LayoutInflater.from(context).inflate(R.layout.layout_dialog_list, null);


        ArrayAdapter adapter = new ArrayAdapter<CharSequence>(
                context, R.layout.dialog_single_choice, R.id.text1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                if (position == checkedItem) {
                    listView.setItemChecked(position, true);
                }
                return view;
            }
        };
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                listener.onClick(
                        dialog, position);
                dismiss();
            }
        });
        setCustomView(listView);
        topPanel.setVisibility(View.GONE);
        bottomPanel.setVisibility(View.GONE);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                start(Effectstype.Fadein);
            }
        });
        return this;

    }

    public MAlertDialog setMultiChoiceItems(CharSequence[] items, final boolean[] checkedItems,
                                            final DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener) {

        final ListView listView = (ListView)
                LayoutInflater.from(context).inflate(R.layout.layout_list, null);


        ArrayAdapter adapter = new ArrayAdapter<CharSequence>(
                context, R.layout.dialog_select_multichoice, R.id.text1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (checkedItems != null) {
                    boolean isItemChecked = checkedItems[position];
                    if (isItemChecked) {
                        listView.setItemChecked(position, true);
                    }
                }
                return view;
            }
        };
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                mOnCheckboxClickListener.onClick(
                        dialog, position, listView.isItemChecked(position));
            }
        });
        setCustomView(listView);
        return this;

    }


    public void show() {


        if (context instanceof Activity) {
            if (!((Activity) context).isFinishing()) {
                dialog.show();
            }
        } else {
            dialog.show();
        }


    }


    public void dismiss() {
        dialog.dismiss();

    }


    public MAlertDialog setPositiveButton(CharSequence btnName, final DialogInterface.OnClickListener onClickListener) {

        if (TextUtils.isEmpty(btnName)) {
            button1.setVisibility(View.GONE);
        } else {
            button1.setVisibility(View.VISIBLE);
            button1.setText(btnName);
        }


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onClickListener == null) {
                    dismiss();
                } else {
                    onClickListener.onClick(dialog, 0);
                }
            }
        });
        return this;
    }

    public MAlertDialog setPositiveButton(int btnName, final DialogInterface.OnClickListener onClickListener) {


        button1.setVisibility(View.VISIBLE);
        button1.setText(btnName);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onClickListener == null) {
                    dismiss();
                } else {
                    onClickListener.onClick(dialog, 0);
                }
            }
        });
        return this;
    }

    public MAlertDialog setButton1(CharSequence btnName, final DialogInterface.OnClickListener onClickListener) {

        if (TextUtils.isEmpty(btnName)) {
            button1.setVisibility(View.GONE);
        } else {
            button1.setVisibility(View.VISIBLE);
            button1.setText(btnName);
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener == null) {
                    dismiss();
                } else {
                    onClickListener.onClick(dialog, 0);
                }
            }
        });
        return this;
    }

    public MAlertDialog setButton2(CharSequence btnName, final DialogInterface.OnClickListener onClickListener) {

        if (TextUtils.isEmpty(btnName)) {
            button2.setVisibility(View.GONE);
        } else {
            button2.setVisibility(View.VISIBLE);
            button2.setText(btnName);
        }

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener == null) {
                    dismiss();
                } else {
                    onClickListener.onClick(dialog, 0);
                }
            }
        });
        return this;
    }

    public MAlertDialog setButton3(CharSequence btnName, final DialogInterface.OnClickListener onClickListener) {

        if (TextUtils.isEmpty(btnName)) {
            button3.setVisibility(View.GONE);
        } else {
            button3.setVisibility(View.VISIBLE);
            button3.setText(btnName);
        }

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener == null) {
                    dismiss();
                } else {
                    onClickListener.onClick(dialog, 0);
                }
            }
        });
        return this;
    }

    public MAlertDialog setNegativeButton(CharSequence btnName, final DialogInterface.OnClickListener onClickListener) {

        if (TextUtils.isEmpty(btnName)) {
            button3.setVisibility(View.GONE);
        } else {
            button3.setVisibility(View.VISIBLE);
            button3.setText(btnName);
        }

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener == null) {
                    dismiss();
                } else {
                    onClickListener.onClick(dialog, 0);
                }
            }
        });
        return this;
    }

    public MAlertDialog setNegativeButton(int btnName, final DialogInterface.OnClickListener onClickListener) {

        button3.setVisibility(View.VISIBLE);
        button3.setText(btnName);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener == null) {
                    dismiss();
                } else {
                    onClickListener.onClick(dialog, 0);
                }
            }
        });
        return this;
    }


    public MAlertDialog create() {
        return this;
    }


}
