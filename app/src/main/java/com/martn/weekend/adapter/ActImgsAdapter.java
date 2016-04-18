package com.martn.weekend.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.martn.weekend.ViewPagerActivity;
import com.martn.weekend.utility.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmusic.common.BEnvironment;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.adapter
 * Description: ("详情页图片展示的viewpager--adapter")
 * Date 2016/4/18 10:41
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class ActImgsAdapter extends PagerAdapter {
    PackageVisibleCallback callback;
    private Context context;
    private List<String> list;
    private LayoutParams params;

    public ActImgsAdapter(Context context, List<String> list, PackageVisibleCallback callback) {
        this.context = context;
        this.list = list;
        this.callback = callback;
        this.params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);//填充父元素
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView iv = new ImageView(context);
        iv.setLayoutParams(params);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage(
                BEnvironment.SERVER_IMG_URL+list.get(position), iv, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());

        container.addView(iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!callback.visible()) {
                    ArrayList<String> photos = new ArrayList();
                    for (int i = 0; i < list.size(); i++) {
                        photos.add(BEnvironment.SERVER_IMG_URL+list.get(i));
                    }
                    Intent intent = new Intent(context, ViewPagerActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("index", position);
                    b.putStringArrayList("imgs", photos);
                    intent.putExtra("bundle", b);
                    context.startActivity(intent);
                }
            }
        });
        return iv;
    }

    @Override
    public int getCount() {
        KLog.e("img_url----->size : " + (list == null ? 0 : list.size()));
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public interface PackageVisibleCallback {
        boolean visible();
    }
}