package com.martn.weekend.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.qmusic.uitls.AppUtils;

/**
 * Title: Weekend
 * Package: com.martn.weekend.view
 * Description: ("请描述功能")
 * Date 2016/3/25 17:34
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class CusTextView extends TextView{

    public CusTextView(Context context) {
        super(context);
        setupView();
    }

    public CusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    private void setupView() {
        setTypeface(AppUtils.getTypefaceZiTi());
    }

}
