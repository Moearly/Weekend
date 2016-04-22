package com.martn.weekend.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.martn.weekend.R;

/**
 * Title: Weekend
 * Package: com.martn.weekend.view
 * Description: ("请描述功能")
 * Date 2016/4/18 14:46
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
@SuppressLint({"ResourceAsColor"})
public class ToastCommom {
    private static ToastCommom toastCommom;
    private Toast toast;

    private ToastCommom() {
    }

    public static ToastCommom createToastConfig() {
        if (toastCommom == null) {
            toastCommom = new ToastCommom();
        }
        return toastCommom;
    }

    public void ToastShow(Context context, ViewGroup root, String tvString) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_toast, null);
        ((TextView) layout.findViewById(R.id.tv_text)).setText(tvString);
        this.toast = new Toast(context);
        this.toast.setGravity(16, 0, 0);
        this.toast.setDuration(0);
        this.toast.setView(layout);
        this.toast.show();
    }

}
