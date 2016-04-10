package com.martn.weekend.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.martn.weekend.R;
import com.martn.weekend.model.TcrModel;
import com.martn.weekend.view.CourseSmall1View;

import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.adapter
 * Description: ("请描述功能")
 * Date 2014/10/5 12:51
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class ActsPagerAdapter extends PagerAdapter{
    private Context context;
    private List<TcrModel> list;

    public ActsPagerAdapter(Context context, List<TcrModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(this.context, R.layout.item_course_small_1, null);
        new CourseSmall1View(this.context, view).setupView(list.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setList(List<TcrModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
