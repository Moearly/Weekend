package com.martn.weekend.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.martn.weekend.R;

import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.adapter
 * Description: ("请描述功能")
 * Date 2014/10/5 21:35
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class SearchMapAdapter extends BaseAdapter{
    private Context context;
    private List<PoiInfo> list;

    class ViewHolder {
        TextView addressDetailTV;
        TextView addressNameTV;
    }

    public SearchMapAdapter(Context context, List<PoiInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list == null ? 0 : this.list.size();
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
            holder = new ViewHolder();
            view = View.inflate(this.context, R.layout.item_search_map, null);
            holder.addressNameTV = (TextView) view.findViewById(R.id.tv_address);
            holder.addressDetailTV = (TextView) view.findViewById(R.id.tv_address_detail);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.addressNameTV.setText(list.get(position).name);
        holder.addressDetailTV.setText(list.get(position).address);
        return view;
    }

    public void setList(List<PoiInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}