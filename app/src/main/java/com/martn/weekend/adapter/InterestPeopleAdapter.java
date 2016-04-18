package com.martn.weekend.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.martn.weekend.R;
import com.martn.weekend.model.UserfaveBean;
import com.martn.weekend.utility.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmusic.common.BEnvironment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Title: Weekend
 * Package: com.martn.weekend.adapter
 * Description: ("请描述功能")
 * Date 2016/4/18 15:46
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class InterestPeopleAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<UserfaveBean> list;

    class ViewHolder {
        CircleImageView headIV;
        TextView nameTV;
        TextView sexTV;
        TextView stateTV;
        TextView tagTV;
    }

    public InterestPeopleAdapter(Context context, List<UserfaveBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
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
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_interest_people, parent, false);
            holder.headIV = (CircleImageView) view.findViewById(R.id.iv_head);
            holder.nameTV = (TextView) view.findViewById(R.id.tv_name);
            holder.tagTV = (TextView) view.findViewById(R.id.tv_tag);
            holder.stateTV = (TextView) view.findViewById(R.id.tv_state);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        setupView(holder, position);
        return view;
    }


    private void setupView(ViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(
                BEnvironment.SERVER_IMG_URL+list.get(position).photo,
                holder.headIV, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());

        holder.nameTV.setText(list.get(position).nickname);
        holder.nameTV.setCompoundDrawablesWithIntrinsicBounds(list.get(position).sex == 1 ? R.drawable.img_male_blue : R.drawable.img_female_red, 0, 0, 0);
        String tag = list.get(position).oneabstract;
        if (TextUtils.isEmpty(tag)) {
            holder.tagTV.setVisibility(View.INVISIBLE);
            holder.tagTV.setText("");
        } else {
            holder.tagTV.setVisibility(View.VISIBLE);
            holder.tagTV.setText(tag);
        }
        int status = list.get(position).status;
        if (status == 1) {
            holder.stateTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_circle_clock, 0, 0);
        } else if (status == 2) {
            holder.stateTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_circle_like, 0, 0);
        } else if (status == 3) {
            holder.stateTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_circle_j, 0, 0);
        } else {
            holder.stateTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        holder.stateTV.setText(list.get(position).statusDesc);
    }

    public void setList(List<UserfaveBean> tList, boolean isClean) {
        if (isClean) {
            this.list.clear();
        }
        this.list.addAll(tList);
        notifyDataSetChanged();
    }
}