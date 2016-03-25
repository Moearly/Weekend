package com.martn.weekend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.martn.weekend.R;
import com.qmusic.base.BaseApplication;

/**
 * Title: Weekend
 * Package: com.martn.weekend.adapter
 * Description: ("请描述功能")
 * Date 2016/3/25 17:45
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class PersonalAdapter extends BaseAdapter {
    private final int[] drawablesId;
    private final int[] titlesResId;
    LayoutInflater inflater;

    class ViewHolder {
        View line;
        ImageView topLineIV;
        TextView tv;
    }

    public PersonalAdapter(Context context) {
        this.titlesResId = new int[]{R.string.joined_act, R.string.attention_act, R.string.info_recommend_friend, R.string.info_coupons, R.string.wallet,
                R.string.info_setup, R.string.info_contact_service, R.string.info_i_will_release};
        this.drawablesId = new int[]{R.drawable.img_icon_joined, R.drawable.img_attention_act, R.drawable.img_icon_rem_friend,
                R.drawable.img_icon_coupons, R.drawable.img_icon_wallet, R.drawable.img_icon_setup,
                R.drawable.img_contact_service, R.drawable.img_i_will_release};
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return titlesResId.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int i;
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder();
            view = this.inflater.inflate(R.layout.item_personal, parent, false);
            holder.tv = (TextView) view.findViewById(R.id.tv_text);
            holder.line = view.findViewById(R.id.line);
            holder.topLineIV = (ImageView) view.findViewById(R.id.iv_line_top);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        setupTextView(holder, position);
        View view2 = holder.line;
        if (position == getCount() - 1) {
            i = 4;
        } else {
            i = 0;
        }
        view2.setVisibility(i);
        if (getTitleResId(position) == R.string.info_contact_service) {
            holder.topLineIV.setVisibility(0);
        } else {
            holder.topLineIV.setVisibility(8);
        }
        return view;
    }

    private void setupTextView(ViewHolder holder, int position) {
        holder.tv.setText(BaseApplication.context().getString(titlesResId[position]));
        holder.tv.setCompoundDrawablesWithIntrinsicBounds(drawablesId[position], 0, R.drawable.img_arrow_right_gray, 0);
    }

    public int getTitleResId(int position) {
        return titlesResId[position];
    }
}