package com.martn.weekend.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.martn.weekend.R;
import com.qmusic.uitls.AppUtils;

/**
 * Title: Weekend
 * Package: com.martn.weekend.view
 * Description: ("问题输入对话框")
 * Date 2016/4/18 16:13
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class InputAnswerPopupWindow extends PopupWindow {
    private ImageView cancelIV;
    private EditText inputET;
    private Context mContext;
    private View mMenuView;
    private ImageView sendTV;

    public InputAnswerPopupWindow(Context context, OnClickListener itemsOnClick) {
        super(context);
        mContext = context;
        mMenuView = View.inflate(context, R.layout.alert_dialog_input_answer, null);
        sendTV = (ImageView) mMenuView.findViewById(R.id.iv_send);
        inputET = (EditText) mMenuView.findViewById(R.id.et_input);
        inputET.setTypeface(AppUtils.getTypefaceZiTi());
        cancelIV = (ImageView) mMenuView.findViewById(R.id.iv_cancel);
        cancelIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        sendTV.setOnClickListener(itemsOnClick);
        setContentView(mMenuView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setAnimationStyle(R.style.AnimBottom);
        setInputMethodMode(1);
        setSoftInputMode(16);
        setBackgroundDrawable(new ColorDrawable(android.R.color.darker_gray));
        mMenuView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.rl_root).getTop();
                int y = (int) event.getY();
                if (event.getAction() == 1 && y < height) {
                    dismiss();
                }
                return true;
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager)mContext.getSystemService("input_method")).toggleSoftInput(0, 2);
            }
        }, 500);
    }

    public String getInput() {
        return inputET.getText().toString();
    }
}
