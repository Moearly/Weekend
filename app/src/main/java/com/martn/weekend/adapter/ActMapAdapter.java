package com.martn.weekend.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.martn.weekend.R;
import com.martn.weekend.model.TcrModel;
import com.martn.weekend.view.CourseSmall1View;

import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.adapter
 * Description: ("请描述功能")
 * Date 2014/10/5 12:42
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class ActMapAdapter extends BaseAdapter{

    private Context context;
    private List<TcrModel> list;

    public ActMapAdapter(Context context, List<TcrModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CourseSmall1View courseSmall1view;
        View view = convertView;
        if (view == null) {
            view = View.inflate(context, R.layout.item_course_small_1, null);
            courseSmall1view = new CourseSmall1View(context, view);
            courseSmall1view.init();
            courseSmall1view.setClickable(false);
            view.setTag(courseSmall1view);
        } else {
            courseSmall1view = (CourseSmall1View) view.getTag();
        }
        courseSmall1view.setupView(list.get(position));
        return view;
    }

    public void setList(List<TcrModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
