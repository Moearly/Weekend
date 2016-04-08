package com.martn.weekend.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Title: Weekend
 * Package: com.martn.weekend.view
 * Description: ("请描述功能")
 * Date 2016/3/31 17:13
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class NoScrollGridView extends GridView {
    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollGridView(Context context) {
        super(context);
    }

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
    }
}