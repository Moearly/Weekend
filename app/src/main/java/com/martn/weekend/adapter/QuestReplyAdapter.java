package com.martn.weekend.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.martn.weekend.R;
import com.martn.weekend.model.QuestreplyModel;

import java.util.Date;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.martn.weekend.utility.AnimateFirstDisplayListener;
import com.martn.weekend.utility.DateHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmusic.common.BConstants;
import com.qmusic.common.BEnvironment;
import com.qmusic.uitls.TUtils;

/**
 * Title: Weekend
 * Package: com.martn.weekend.adapter
 * Description: ("请描述功能")
 * Date 2016/4/18 16:49
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class QuestReplyAdapter extends BaseAdapter {
    private Context context;
    private List<QuestreplyModel> list;

    class ViewHolder {
        TextView questContentTV;
        TextView questDateTV;
        ImageView questHeadIV;
        TextView questNameTV;
        TextView replyContentTV;
        TextView replyDateTV;
        ImageView replyHeadIV;
        RelativeLayout replyLayout;
        TextView replyNameTV;
        ImageView triangleIV;

    }

    public QuestReplyAdapter(Context context, List<QuestreplyModel> list) {
        this.context = context;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.item_questreply_1, null);
            initView(view, holder);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        setupQuestHeadIV(holder, position);
        setupQuestNameTV(holder, position);
        setupQuestDateTV(holder, position);
        setupQuestContentTV(holder, position);
        setupTriangleIV(holder, position);
        setupReplyLayout(holder, position);
        setupReplyHeadIV(holder, position);
        setupReplyNameTV(holder, position);
        setupReplyDateTV(holder, position);
        setupReplyContentTV(holder, position);
        return view;
    }

    private void setupQuestHeadIV(ViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(
                BEnvironment.SERVER_IMG_URL+list.get(position).quest.questUserPhoto,
                holder.questHeadIV, AnimateFirstDisplayListener.getHeadOptions(), AnimateFirstDisplayListener.getListener());
    }

    private void setupQuestNameTV(ViewHolder holder, int position) {
        holder.questNameTV.setText(list.get(position).quest.questUserNickname);
    }

    private void setupQuestDateTV(ViewHolder holder, int position) {
        holder.questDateTV.setText(DateHelper.format(new Date(list.get(position).quest.questDatetime), "yyyy-MM-dd"));
    }

    private void setupQuestContentTV(ViewHolder holder, int position) {
        holder.questContentTV.setText(list.get(position).quest.questContent);
    }

    private void setupTriangleIV(ViewHolder holder, int position) {
        if (TUtils.stringIsNotNull(list.get(position).reply.replyUserNickname)) {
            holder.triangleIV.setVisibility(View.VISIBLE);
        } else {
            holder.triangleIV.setVisibility(View.GONE);
        }
    }

    private void setupReplyLayout(ViewHolder holder, int position) {
        if (TUtils.stringIsNotNull(list.get(position).reply.replyUserNickname)) {
            holder.replyLayout.setVisibility(View.VISIBLE);
        } else {
            holder.replyLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void setupReplyHeadIV(ViewHolder holder, int position) {
        if (TUtils.stringIsNotNull(list.get(position).reply.replyUserNickname)) {
            ImageLoader.getInstance().displayImage(BEnvironment.SERVER_IMG_URL + list.get(position).reply.replyUserPhoto,
                    holder.replyHeadIV,
                    AnimateFirstDisplayListener.getHeadOptions(), AnimateFirstDisplayListener.getListener());
        } else {
            holder.replyHeadIV.setVisibility(View.GONE);
        }
    }

    private void setupReplyNameTV(ViewHolder holder, int position) {
        if (TUtils.stringIsNotNull(list.get(position).reply.replyUserNickname)) {
            holder.replyNameTV.setVisibility(View.VISIBLE);
            holder.replyNameTV.setText(list.get(position).reply.replyUserNickname);
            return;
        }
        holder.replyNameTV.setVisibility(View.GONE);
    }

    private void setupReplyDateTV(ViewHolder holder, int position) {
        if (TUtils.stringIsNotNull(list.get(position).reply.replyUserNickname)) {
            holder.replyDateTV.setVisibility(View.VISIBLE);
            holder.replyDateTV.setText(DateHelper.format(new Date(list.get(position).reply.replyDatetime), "yyyy/MM/dd"));
            return;
        }
        holder.replyDateTV.setVisibility(View.GONE);
    }

    private void setupReplyContentTV(ViewHolder holder, int position) {
        if (TUtils.stringIsNotNull(list.get(position).reply.replyUserNickname)) {
            holder.replyContentTV.setVisibility(View.VISIBLE);
            holder.replyContentTV.setText(list.get(position).reply.replyContent);
            return;
        }
        holder.replyContentTV.setVisibility(View.GONE);
    }

    private void initView(View view, ViewHolder holder) {
        holder.questHeadIV = (ImageView) view.findViewById(R.id.quest_head_imageview);
        holder.questNameTV = (TextView) view.findViewById(R.id.quest_name_textview);
        holder.questDateTV = (TextView) view.findViewById(R.id.quest_date_textview);
        holder.questContentTV = (TextView) view.findViewById(R.id.quest_content_textview);
        holder.triangleIV = (ImageView) view.findViewById(R.id.triangle_imageview);
        holder.replyLayout = (RelativeLayout) view.findViewById(R.id.reply_layout);
        holder.replyHeadIV = (ImageView) view.findViewById(R.id.reply_head_imageview);
        holder.replyNameTV = (TextView) view.findViewById(R.id.reply_name_textview);
        holder.replyDateTV = (TextView) view.findViewById(R.id.reply_date_textview);
        holder.replyContentTV = (TextView) view.findViewById(R.id.reply_content_textview);
    }

    public void setList(List<QuestreplyModel> list, boolean isClean) {
        if (isClean) {
            list.clear();
        }
        list.addAll(list);
        notifyDataSetChanged();
    }
}
