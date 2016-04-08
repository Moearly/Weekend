package com.martn.weekend.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Title: Weekend
 * Package: com.martn.weekend.view
 * Description: ("不能滑动的listview")
 * Date 2016/3/31 16:58
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class NoScrollListView extends ListView {
    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
    }
}
