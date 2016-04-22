package com.martn.weekend;

import com.martn.weekend.base.BaseActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.martn.weekend.utility.AnimateFirstDisplayListener;
import com.martn.weekend.view.HackyViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmusic.uitls.AppUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("横向列表图片展示")
 * Date 2016/4/18 10:47
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class ViewPagerActivity extends BaseActivity{
    int index = -1;
    ArrayList<String> list;
    private ViewPager mViewPager;
    private LinearLayout pointLayout;
    LayoutParams pointParams;

    class SamplePagerAdapter extends PagerAdapter {
        ArrayList<String> imgs;

        public SamplePagerAdapter(Context context, ArrayList<String> list) {
            this.imgs = list;
        }

        public int getCount() {
            return imgs.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());

            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    finish();
                }

                @Override
                public void onOutsidePhotoTap() {
                    finish();
                }
            });
            ImageLoader.getInstance().displayImage(imgs.get(position), photoView, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());
            container.addView(photoView, -1, -1);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        Bundle b = getIntent().getBundleExtra("bundle");
        list = b.getStringArrayList("imgs");
        index = b.getInt("index", -1);
        initView();
    }

    private void initView() {
        pointLayout = (LinearLayout) findViewById(R.id.ll_point);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        setupVP();
        setupPoint(index);
    }

    private void setupVP() {
        mViewPager.setAdapter(new SamplePagerAdapter(this, list));
        mViewPager.setCurrentItem(index);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                setupPoint(arg0);
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void setupPoint(int index) {
        int size = list.size();
        if (size > 0) {
            if (pointParams == null) {
                pointParams = new LayoutParams((int) (((double) AppUtils.getScreenWidth()) * 0.02d), (int) (((double) AppUtils.getScreenWidth()) * 0.02d));
                pointParams.setMargins(5, 0, 5, 0);
            }
            pointLayout.removeAllViews();
            for (int i = 0; i < size; i++) {
                CircleImageView civ = new CircleImageView(this);
                civ.setLayoutParams(pointParams);
                if (i == index) {
                    civ.setImageResource(R.color.blue);
                } else {
                    civ.setImageResource(R.color.gray);
                }
                pointLayout.addView(civ);
            }
        }
    }

}
