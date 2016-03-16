package com.martn.weekend.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.martn.weekend.R;

import java.util.Locale;

/**
 * Title: Weekend
 * Package: com.martn.weekend.view
 * Description: ("请描述功能")
 * Date 2016/3/16 16:34
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class PagerSlidingTabStrip extends HorizontalScrollView {



    private final PageListener pageListener = new PageListener();
    public ViewPager.OnPageChangeListener delegatePageListener;


    private int dividerColor =0x1A000000;
    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int tabTextColor =0xFF666666;

    private int underlineHeight = 2;
    private int indicatorHeight = 8;
    private int dividerPadding = 12;
    private int tabPadding = 18;
    private int dividerWidth =1;
    private int tabTextSize = 11;
    private int lastScrollX = 0;
    private int tabBackgroundResId = R.drawable.background_tab;
    private int scrollOffset = 52;
    private int tabCount;

    private int currentNextPosition = 0;
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;
    float k = 0.5f;
    int lastCurrentPosition = -1;


    private Paint dividerPaint;
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private Bitmap indicatorMidBit;
    private Locale locale;
    private LinearLayout tabsContainer;
    private ViewPager pager;
    private Paint rectPaint;

    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = 0;
    private boolean textAllCaps = true;
    private boolean shouldExpand = false;



    public interface IconTabProvider {
        public int getPageIconResId(int position);
    }

    private static final int[] ATTRS = new int[]{
                android.R.attr.textSize,
                android.R.attr.textColor
    };


    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR;
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        static {
            CREATOR = new Creator<SavedState>() {
                @Override
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
                }
                @Override
                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            currentPosition = position;
            currentPositionOffset = positionOffset;
            scrollToChild(position, (int) (((float) tabsContainer.getChildAt(position).getWidth()) * positionOffset));
            invalidate();
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        public void onPageScrollStateChanged(int state) {
            if (state == 0) {
                scrollToChild(pager.getCurrentItem(), 0);
            }
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        public void onPageSelected(int position) {
            currentNextPosition = position;
            updateTabStyles();
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }
    }



    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (float) tabTextSize, dm);

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);
        a.recycle();
        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, dividerPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);
        a.recycle();
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth((float) dividerWidth);
        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
        indicatorMidBit = BitmapFactory.decodeResource(getResources(), R.drawable.img_indicator_mid);
        indicatorHeight = indicatorMidBit.getHeight();
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        pager.setOnPageChangeListener(pageListener);
        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            if (pager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) pager.getAdapter()).getPageIconResId(i));
            } else {
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            }
        }
        updateTabStyles();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint({"NewApi"})
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });
    }

    private void addTextTab(int position, String title) {
        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setSingleLine();
        tab.setGravity(Gravity.CENTER);
        addTab(position, tab);
    }

    private void addIconTab(int position, int resId) {
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);
        addTab(position, tab);
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(position);
            }
        });
        if (position == 0) {
            tab.setPadding(0, 0, tabPadding, 0);
        } else if (position == tabCount - 1) {
            tab.setPadding(tabPadding, 0, 0, 0);
        } else {
            tab.setPadding(tabPadding, 0, tabPadding, 0);
        }
        ((TextView) tab).setTextColor(R.color.main_text_gray_light);
        ((TextView) tab).setTextSize(0, (float) tabTextSize);
        ((TextView) tab).setTypeface(tabTypeface, tabTypefaceStyle);
        tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
    }

    private void updateTabStyles() {
        if (tabCount > 0) {
            ((TextView) tabsContainer.getChildAt(currentNextPosition)).setTextColor(tabTextColor);
            if (lastCurrentPosition > -1 && lastCurrentPosition != currentNextPosition) {
                ((TextView) tabsContainer.getChildAt(lastCurrentPosition)).setTextColor(R.color.main_text_gray_light);
            }
            lastCurrentPosition = currentNextPosition;
        }
    }

    private void scrollToChild(int position, int offset) {
        if (tabCount != 0) {
            int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;
            if (position > 0 || offset > 0) {
                newScrollX -= scrollOffset;
            }
            if (newScrollX != lastScrollX) {
                lastScrollX = newScrollX;
                scrollTo(newScrollX, 0);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode() && tabCount != 0) {
            int height = getHeight();
            rectPaint.setColor(indicatorColor);
            View currentTab = tabsContainer.getChildAt(currentPosition);
            float lineLeft = (float) currentTab.getLeft();
            float lineRight = (float) currentTab.getRight();
            if (currentPositionOffset > 0.0f && currentPosition < tabCount - 1) {
                View nextTab = tabsContainer.getChildAt(currentPosition + 1);
                lineLeft = (currentPositionOffset * ((float) nextTab.getLeft())) + ((1.0f - currentPositionOffset) * lineLeft);
                lineRight = (currentPositionOffset * ((float) nextTab.getRight())) + ((1.0f - currentPositionOffset) * lineRight);
            }
            float dstOffset = ((((4.0f - (4.0f * k)) * currentPositionOffset) * currentPositionOffset) + (((4.0f * k) - 4.0f) * currentPositionOffset)) + 1.0f;
            Rect src = new Rect();
            src.left = 0;
            src.top = 0;
            src.right = indicatorMidBit.getWidth();
            src.bottom = (int) (((float) indicatorMidBit.getHeight()) * dstOffset);
            Rect dst = new Rect();
            dst.left = (int) lineLeft;
            dst.top = height - src.bottom;
            dst.right = (int) lineRight;
            dst.bottom = height;
            if (currentPosition == 0) {
                if (currentPositionOffset > 0.0f) {
                    src.left = (int) (((float) src.left) + (((float) indicatorMidBit.getWidth()) * (1.0f - currentPositionOffset >= 0.35f ? 0.35f : 1.0f - currentPositionOffset)));
                } else if (lineLeft == 0.0f) {
                    src.left = (int) (((float) src.left) + (((float) indicatorMidBit.getWidth()) * 0.35f));
                }
            } else if (lineRight > ((float) tabsContainer.getChildAt(tabCount - 1).getLeft())) {
                if (currentPositionOffset > 0.0f) {
                    src.right = (int) (((float) src.right) - (((float) indicatorMidBit.getWidth()) * (currentPositionOffset >= 0.35f ? 0.35f : currentPositionOffset)));
                } else if (lineRight >= ((float) getWidth())) {
                    src.right = (int) (((float) src.right) - (((float) indicatorMidBit.getWidth()) * 0.35f));
                }
            }
            canvas.drawBitmap(indicatorMidBit, src, dst, rectPaint);
            rectPaint.setColor(underlineColor);
            canvas.drawRect(0.0f, (float) (height - underlineHeight), (float) tabsContainer.getWidth(), (float) height, rectPaint);
        }
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizePx) {
        tabTextSize = textSizePx;
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setTypeface(Typeface typeface) {
        tabTypeface = typeface;
        updateTabStyles();
    }

    public void setTypeface(Typeface typeface, int style) {
        tabTypeface = typeface;
        tabTypefaceStyle = style;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        tabBackgroundResId = resId;
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        tabPadding = paddingPx;
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.currentPosition = currentPosition;
        return savedState;
    }

}
