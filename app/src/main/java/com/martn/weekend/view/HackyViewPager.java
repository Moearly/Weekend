package com.martn.weekend.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Title: Weekend
 * Package: com.martn.weekend.view
 * Description: ("请描述功能")
 * Date 2016/4/18 11:04
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class HackyViewPager extends ViewPager {
    private boolean isLocked;

    public HackyViewPager(Context context) {
        super(context);
        this.isLocked = false;
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isLocked = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean z = false;
        if (!isLocked) {
            try {
                z = super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return z;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.isLocked) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    public void toggleLock() {
        this.isLocked = !this.isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isLocked() {
        return this.isLocked;
    }
}