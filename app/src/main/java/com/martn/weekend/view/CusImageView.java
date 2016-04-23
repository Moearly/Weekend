package com.martn.weekend.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.martn.weekend.R;
import com.martn.weekend.utility.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmusic.common.BEnvironment;

/**
 * Title: Weekend
 * Package: com.martn.weekend.view
 * Description: ("请描述功能")
 * Date 2014/10/5 16:09
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class CusImageView extends FrameLayout {
    public final int TYPE_PHOTO = 1;
    public final int TYPE_VIDEO = 2;
    private Context context;
    private ImageView delIV;
    private ImageView imgIV;
    private int index = 0;
    private ImageView playIV;

    public CusImageView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CusImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = View.inflate(this.context, R.layout.item_my_imageview, null);
        this.imgIV = (ImageView) view.findViewById(R.id.img_imageview);
        this.delIV = (ImageView) view.findViewById(R.id.del_imageview);
        this.playIV = (ImageView) view.findViewById(R.id.play_imageview);
        addView(view);
    }

    public void setType(int type) {
        switch (type) {
            case TYPE_PHOTO:
                this.playIV.setVisibility(GONE);
            case TYPE_VIDEO:
                this.playIV.setVisibility(VISIBLE);
            default:
        }
    }

    public void setIV(String url) {
        ImageLoader.getInstance().displayImage(BEnvironment.SERVER_IMG_URL + url,
                imgIV, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());
    }

    public void setDelvisibility(int visibility) {
        this.delIV.setVisibility(visibility);
    }

    public void setDelListener(OnClickListener listener) {
        this.delIV.setOnClickListener(listener);
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
