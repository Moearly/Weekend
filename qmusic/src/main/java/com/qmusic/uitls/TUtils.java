package com.qmusic.uitls;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Title: Weekend
 * Package: com.qmusic.uitls
 * Description: ("文本相关的处理")
 * Date 2016/4/18 11:32
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class TUtils {
    public static SpannableString getSpanString(String info, int start, int end, float size, boolean isBold, int foregroundColor, int backgroundColor) {
        SpannableString spanString = new SpannableString(info);
        if (size > 0.0f) {
            spanString.setSpan(new RelativeSizeSpan(size), start, end, 33);
        }
        if (isBold) {
            spanString.setSpan(new StyleSpan(1), start, end, 33);
        }
        spanString.setSpan(new ForegroundColorSpan(foregroundColor), start, end, 33);
        spanString.setSpan(new BackgroundColorSpan(backgroundColor), start, end, 33);
        return spanString;
    }

    public static SpannableString getSpanString(String info, int start, int end, float size, boolean isBold, boolean isLine, int foregroundColor, int backgroundColor) {
        SpannableString spanString = new SpannableString(info);
        if (size > 0.0f) {
            spanString.setSpan(new RelativeSizeSpan(size), start, end, 33);
        }
        if (isBold) {
            spanString.setSpan(new StyleSpan(1), start, end, 33);
        }
        if (isLine) {
            spanString.setSpan(new UnderlineSpan(), start, end, 33);
        }
        spanString.setSpan(new ForegroundColorSpan(foregroundColor), start, end, 33);
        spanString.setSpan(new BackgroundColorSpan(backgroundColor), start, end, 33);
        return spanString;
    }

    public static void setTextViewBg(TextView tv, String type) {
        if (TextUtils.isEmpty(type)) {
            tv.setVisibility(View.GONE);
            return;
        }
        tv.setVisibility(View.VISIBLE);
        tv.setText("商户");
    }

    public static boolean stringIsNull(String str) {
        str.trim();
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean stringIsNotNull(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        return true;
    }



}
