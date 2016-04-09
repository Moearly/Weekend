package com.martn.weekend.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.martn.weekend.R;
import com.martn.weekend.model.TagNewModel;

import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.adapter
 * Description: ("tag--标签适配器")
 * Date 2014/10/5 22:30
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class TagNewAdapter extends BaseAdapter{
    private Context context;
    List<TagNewModel> tagList;

    class ViewHolder {
        TextView nameTV;
    }

    public TagNewAdapter(Context context) {
        this.context = context;
    }

    public TagNewAdapter(Context context, List<TagNewModel> tags) {
        this.context = context;
        tagList = tags;
    }

    public int getCount() {
        return tagList == null ? 0 : tagList.size();
    }

    public Object getItem(int position) {
        return tagList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.item_subject, null);
            holder.nameTV = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.nameTV.setText((tagList.get(position)).name);
        return view;
    }

    public void setList(List<TagNewModel> list) {
        tagList = list;
        notifyDataSetChanged();
    }
}