package com.martn.weekend.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.martn.weekend.R;
import com.martn.weekend.model.TagModel;

import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.adapter
 * Description: ("请描述功能")
 * Date 2014/10/5 11:35
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class TagsListAdapter extends BaseAdapter {
    private Context context;
    private List<TagModel> list;
    private int tagId;

    class ViewHolder {
        TextView tagNameTV;
    }

    public TagsListAdapter(Context context, List<TagModel> list) {
        this.tagId = 0;
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return this.list == null ? 0 : this.list.size() + 1;

    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);

    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = View.inflate(this.context, R.layout.item_tags_list, null);
            holder = new ViewHolder();
            holder.tagNameTV = (TextView) view.findViewById(R.id.tv_tag_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (position == 0) {
            holder.tagNameTV.setText("全部");
        } else {
            holder.tagNameTV.setText(list.get(position - 1).name);
        }
        if (this.tagId == getTagId(position)) {
            holder.tagNameTV.setTextColor(context.getResources().getColor(R.color.blue));
        } else {
            holder.tagNameTV.setTextColor(context.getResources().getColor(R.color.main_text_gray));
        }
        return view;
    }

    public int getTagId(int position) {
        return position == 0 ? 0 : list.get(position - 1).id;
    }

    public void setTagId(int id) {
        this.tagId = id;
        notifyDataSetChanged();
    }

}
