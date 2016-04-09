package com.martn.weekend.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.martn.weekend.LoginActivity;
import com.martn.weekend.R;
import com.martn.weekend.request.IUserCenterServlet;
import com.martn.weekend.utility.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmusic.common.BEnvironment;
import com.martn.weekend.db.UserPreference;
import com.martn.weekend.model.FavFocusModel;
import com.martn.weekend.model.TcrModel;
import com.martn.weekend.result.MainsResult;
import com.qmusic.uitls.AppUtils;
import com.qmusic.uitls.Helper;
import com.socks.library.KLog;

import org.json.JSONObject;

/**
 * Title: Weekend
 * Package: com.martn.weekend.adapter
 * Description: ("请描述功能")
 * Date 2016/3/25 10:16
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class HomeListNewAdapter extends BaseAdapter {
    public final int TYPE_COURSE = 1;
    public final int TYPE_GUANGGAO = 0;
    private Context context;
    private Response.ErrorListener errorListener;
    LayoutInflater inflater;
    private MainsResult result;


    /**
     * 喜欢关注点击返回
     */
    class FavListener implements Response.Listener<JSONObject> {
        ImageView favIV;
        ImageView favLineIV;
        TextView favNumTV;
        TcrModel tcrModel;

        public FavListener(TcrModel model, ImageView favIV, ImageView favLineIV, TextView favNumTV) {
            this.tcrModel = model;
            this.favIV = favIV;
            this.favNumTV = favNumTV;
            this.favLineIV = favLineIV;
        }

        public void onResponse(JSONObject response) {
            if ("success".equals(response.optString("code"))) {
                tcrModel.iscollect = new FavFocusModel(response).getResult().iscollect;
                TcrModel tcrModel;
                if (this.tcrModel.iscollect == 1) {
                    tcrModel = this.tcrModel;
                    tcrModel.collectcount++;
                } else if (this.tcrModel.iscollect == 0) {
                    tcrModel = this.tcrModel;
                    tcrModel.collectcount--;
                }
                this.favIV.setImageResource(this.tcrModel.iscollect == 1 ? R.drawable.img_main_fav_yellow : R.drawable.img_main_fav_gray);
                this.favNumTV.setText(new StringBuilder(String.valueOf(this.tcrModel.collectcount)).append("个关注").toString());
                this.favLineIV.setImageResource(this.tcrModel.iscollect == 1 ? R.drawable.bg_corner_blue_tr_br : R.drawable.bg_corner_gray_tr_br);
                return;
            }
            Helper.showToast(response.optString("description"));
        }
    }

    public HomeListNewAdapter(final Context context, MainsResult result) {
        this.errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Helper.showToast(context.getString(R.string.service_error_mit));
            }
        };
        this.context = context;
        this.result = result;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        int i = 0;
        int count = result.guanggaoList == null ? 0 : result.guanggaoList.size();
        if (result.tcrList != null) {
            i = result.tcrList.size();
        }
        return count + i;
    }

    @Override
    public int getViewTypeCount() {
        //显示列表的类型分为两种--0:广告 1:代表一般的数据
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < (result.guanggaoList == null ? 0 : result.guanggaoList.size())) {
            return 0;
        }
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder();
            if (type == 0) {
                view = this.inflater.inflate(R.layout.item_main_guanggao, parent, false);
                initGuanggaoView(holder, view);
            } else {
                view = this.inflater.inflate(R.layout.item_main_list, parent, false);
                initCourseView(holder, view);
            }
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        switch (type) {
            case TYPE_GUANGGAO:
                setupGuangGaoView(holder, position);
                break;
            case TYPE_COURSE:
                setupCourseView(holder, position);
                break;
        }
        return view;
    }

    private void setupGuangGaoView(ViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL)
                        .append(result.guanggaoList.get(position).photo).toString()
                , holder.ivImage, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());
        holder.tvTitle.setText(result.guanggaoList.get(position).des);
    }

    /**
     * 设置活动的view
     *
     * @param holder
     * @param position
     */
    private void setupCourseView(final ViewHolder holder, int position) {
        int i = View.VISIBLE;
        final int index = position - (result.guanggaoList == null ? 0 : result.guanggaoList.size());
        TcrModel tcrmodel = result.tcrList.get(index);
        holder.ivFav.setImageResource(tcrmodel.iscollect == 1 ? R.drawable.img_main_fav_yellow : R.drawable.img_main_fav_gray);
        holder.tvFavnum.setText(tcrmodel.collectcount + "个关注");
        ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL)
                        .append(tcrmodel.headphoto).toString()
                , holder.ivImage, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());

        holder.tvTitle.setText(tcrmodel.title);
        holder.tvDate.setText(tcrmodel.courseTime);
        holder.tvLocation.setText(tcrmodel.coursesite);
        setupPrice(holder, index);
        setupCount(holder, index);
        holder.tvDistance.setText("·距离" + tcrmodel.coursedistance);
        holder.ivFavLine.setImageResource(tcrmodel.iscollect == 1 ? R.drawable.bg_corner_blue_tr_br : R.drawable.bg_corner_gray_tr_br);
        if (tcrmodel.courseHavecount != 0) {
            i = View.INVISIBLE;
        }
        holder.ivFull.setVisibility(i);
        holder.tvFavnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserPreference.getInstance(context).isLogin()) {
                    IUserCenterServlet.sendFavFocus(result.tcrList.get(index).courseid, 2, new FavListener(result.tcrList.get(index), holder.ivFav, holder.ivFavLine, holder.tvFavnum), errorListener);
                } else {
                    LoginActivity.startActivityForResult(context);
                }
            }
        });
        holder.ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserPreference.getInstance(context).isLogin()) {
                    IUserCenterServlet.sendFavFocus(result.tcrList.get(index).courseid, 2, new FavListener(result.tcrList.get(index), holder.ivFav, holder.ivFavLine, holder.tvFavnum), errorListener);
                } else {
                    LoginActivity.startActivityForResult(context);
                }
            }
        });
    }

    private void setupPrice(ViewHolder holder, int position) {
        String oldPriceStr = result.tcrList.get(position).oldCourseprice;
        if (TextUtils.isEmpty(oldPriceStr) || oldPriceStr.equals("0") || oldPriceStr.equals("-1")) {
            holder.tvOldPrice.setVisibility(View.GONE);
        } else {
            holder.tvOldPrice.setVisibility(View.VISIBLE);
            holder.tvOldPrice.setText(new StringBuilder(String.valueOf(oldPriceStr)).append("元").toString());
        }
        String priceStr = result.tcrList.get(position).courseprice;
        if (priceStr.equals("-1")) {
            holder.tvOldPrice.setVisibility(View.GONE);
        } else if (priceStr.equals("0")) {
            holder.tvPrice.setVisibility(View.VISIBLE);
            holder.tvPrice.setText("免费");
        } else {
            holder.tvPrice.setVisibility(View.VISIBLE);
            holder.tvPrice.setText(AppUtils.getSpanString(result.tcrList.get(position).suffix, 0, priceStr.length(), 2.0f, false, context.getResources().getColor(R.color.mian_text_red), 0));
        }
    }

    private void setupCount(ViewHolder holder, int position) {
        int count = result.tcrList.get(position).courseHavecount;
        if (count <= 0) {
            holder.tvCount.setText("");
        } else if (count < 5) {
            holder.tvCount.setText("仅剩" + Integer.toString(result.tcrList.get(position).courseHavecount) + "份");
        } else {
            holder.tvCount.setText("剩余" + Integer.toString(result.tcrList.get(position).courseHavecount) + "份");
        }
    }

    public int getIndex(int position) {
        KLog.e("index----->2 : type = " + getItemViewType(position) + "|" + position);
        if (getItemViewType(position) == 0) {
            return position;
        }
        return position - (result.guanggaoList == null ? 0 : result.guanggaoList.size());
    }

    static class ViewHolder {
        //TextView tvInfo;

        private ImageView ivImage;
        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvLocation;
        private TextView tvPrice;
        private TextView tvOldPrice;
        private TextView tvCount;
        private TextView tvDistance;
        private TextView tvFavnum;
        private ImageView ivFav;
        private ImageView ivFavLine;
        private ImageView ivFull;

    }

    private void initGuanggaoView(ViewHolder holder, View view) {
        holder.ivImage = (ImageView) view.findViewById(R.id.iv_image);
        holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        holder.ivImage.getLayoutParams().height = AppUtils.getImgItemHeight();
    }

    private void initCourseView(ViewHolder holder, View view) {
        holder.ivImage = (ImageView) view.findViewById(R.id.iv_image);
        holder.ivFav = (ImageView) view.findViewById(R.id.iv_fav);
        holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        holder.tvDate = (TextView) view.findViewById(R.id.tv_date);
        holder.tvLocation = (TextView) view.findViewById(R.id.tv_location);
        holder.tvFavnum = (TextView) view.findViewById(R.id.tv_favnum);
        holder.tvPrice = (TextView) view.findViewById(R.id.tv_price);
        holder.tvDistance = (TextView) view.findViewById(R.id.tv_distance);
        holder.tvOldPrice = (TextView) view.findViewById(R.id.tv_old_price);
        holder.tvCount = (TextView) view.findViewById(R.id.tv_count);
        holder.ivFavLine = (ImageView) view.findViewById(R.id.iv_fav_line);
        holder.ivFull = (ImageView) view.findViewById(R.id.iv_full);
        holder.ivImage.getLayoutParams().height = AppUtils.getImgItemHeight();
    }


    public void setData(MainsResult result, boolean isClean) {
        if (isClean) {
            this.result = result;
        } else {
            this.result.tcrList.addAll(result.tcrList);
        }
        notifyDataSetChanged();
    }

    public MainsResult getResult() {
        return this.result;
    }

}