package com.martn.weekend.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.martn.weekend.R;
import com.martn.weekend.utility.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.qmusic.uitls.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Title: Weekend
 * Package: com.martn.weekend.view
 * Description: ("图文混合的显示区域")
 * Date 2014/10/5 23:17
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class CustomTextView extends LinearLayout {
    float imageheight;
    float imagewidth;
    private ArrayList<String> list;
    private Context mContext;
    private int padding;
    private int paddingBottom;
    private int paddingTop;

    class MyRun implements Runnable {
        Bitmap bt;
        CircleImageView iv;

        public MyRun(CircleImageView iv, Bitmap bt) {
            this.iv = iv;
            this.bt = bt;
        }

        public void run() {
            LayoutParams params = (LayoutParams) iv.getLayoutParams();
            params.height = getIvHeight(iv.getWidth(), bt);
            this.iv.setLayoutParams(params);
            this.iv.setImageBitmap(bt);
        }
    }

    public CustomTextView(Context context) {
        super(context);
        this.padding = 0;
        this.paddingBottom = 0;
        this.paddingTop = 0;
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.padding = 0;
        this.paddingBottom = 0;
        this.paddingTop = 0;
        this.mContext = context;
        setOrientation(VERTICAL);
    }

    public void setText(ArrayList<HashMap<String, String>> datas) {
        int index = 0;
        this.padding = (int) (((double) AppUtils.getScreenWidth()) * 0.04d);
        this.paddingBottom = (int) (((double) AppUtils.getScreenWidth()) * 0.03d);
        this.paddingTop = (int) (((double) AppUtils.getScreenWidth()) * 0.02d);
        this.list = new ArrayList();
        Iterator it = datas.iterator();
        while (it.hasNext()) {
            HashMap<String, String> hashMap = (HashMap) it.next();
            if (((String) hashMap.get("type")).equals("image")) {
                ImageView imageView = new CircleImageView(mContext);
                addView(imageView);
                if (!TextUtils.isEmpty(hashMap.get("value"))) {
                    String url = hashMap.get("value");
                    LayoutParams params = new LayoutParams(-1, -1);
                    params.gravity = 1;
                    params.topMargin = this.paddingTop;
                    params.bottomMargin = this.paddingBottom;
                    imageView.setLayoutParams(params);
//                    imageView.setIndex(index);
                    imageView.setScaleType(ScaleType.CENTER_CROP);
                    this.list.add(url);
                    ImageLoader.getInstance().displayImage(url, imageView, AnimateFirstDisplayListener.getOptions(), new SimpleImageLoadingListener() {
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            view.post(new MyRun((CircleImageView) view, loadedImage));
                        }
                    });
                }
                index++;
            } else if (!TextUtils.isEmpty(hashMap.get("value"))) {
                CusTextView textView = new CusTextView(this.mContext);
                textView.setPadding(this.padding, 0, this.padding, this.paddingBottom);
                textView.setText((CharSequence) hashMap.get("value"));
                textView.setTextSize(14.0f);
                textView.setTextColor(this.mContext.getResources().getColor(R.color.main_text_gray));
                textView.setLineSpacing(1.0f, 1.3f);
                addView(textView);
            }
        }
    }

    private int getIvHeight(int ivWidth, Bitmap bitmap) {
        return (int) (((float) ivWidth) * (((float) bitmap.getHeight()) / Float.parseFloat(bitmap.getWidth() + "")));
    }
}