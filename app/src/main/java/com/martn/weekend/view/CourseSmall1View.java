package com.martn.weekend.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.martn.weekend.ActDetailActivity;
import com.martn.weekend.R;
import com.martn.weekend.model.TcrModel;
import com.martn.weekend.utility.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmusic.common.BEnvironment;

/**
 * Title: Weekend
 * Package: com.martn.weekend.view
 * Description: ("请描述功能")
 * Date 2014/10/5 12:44
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class CourseSmall1View {
    private ClickCallback callback;
    private boolean clickable;
    private Context context;
    private TextView dateTV;
    private TextView distanceTV;
    private ImageView imgIV;
    private TextView locationTV;
    private TextView priceTV;
    private TextView titleTV;
    private View view;


    public CourseSmall1View(Context context, View view) {
        this.clickable = true;
        this.context = context;
        this.view = view;
        init();
    }

    public void init() {
        imgIV = (ImageView) view.findViewById(R.id.iv_img);
        titleTV = (TextView) view.findViewById(R.id.tv_title);
        distanceTV = (TextView) view.findViewById(R.id.tv_distance);
        dateTV = (TextView) view.findViewById(R.id.tv_date);
        locationTV = (TextView) view.findViewById(R.id.location_tv);
        priceTV = (TextView) view.findViewById(R.id.tv_price);
    }

    public void setupView(final TcrModel tcr) {
        ImageLoader.getInstance().displayImage(BEnvironment.SERVER_IMG_URL + tcr.headphoto,
                imgIV, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());
        titleTV.setText(tcr.title);
        distanceTV.setText(tcr.coursedistance);
        dateTV.setText(tcr.courseTime);
        locationTV.setText(tcr.coursesite);
        priceTV.setText(tcr.coursepriceSuffix);
        if (clickable) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActDetailActivity.comeBady(context, tcr.courseid);
                }
            });
        }
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public void setCallback(ClickCallback callback) {
        this.callback = callback;
    }

    public interface ClickCallback {
        void onClick();
    }
}