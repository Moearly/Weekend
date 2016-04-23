package com.martn.weekend.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.martn.weekend.LoginActivity;
import com.martn.weekend.R;
import com.martn.weekend.app.App;
import com.martn.weekend.db.UserPreference;
import com.martn.weekend.model.FavFocusModel;
import com.martn.weekend.model.TcrModel;
import com.martn.weekend.request.IUserCenterServlet;
import com.martn.weekend.utility.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmusic.common.BEnvironment;
import com.qmusic.uitls.AppUtils;
import com.qmusic.uitls.Helper;
import com.sina.weibo.sdk.exception.WeiboAuthException;
import com.sina.weibo.sdk.register.mobile.SelectCountryActivity;

import org.json.JSONObject;

import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.adapter
 * Description: ("课程列表适配器")
 * Date 2014/10/5 14:52
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class CourseListAdapter extends BaseAdapter {
    private Context context;
    private ErrorListener errorListener;
    IUserCenterServlet iUserCenterV2ServletRequest;
    private List<TcrModel> list;
    class FavListener implements Listener<JSONObject> {
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

        @Override
        public void onResponse(JSONObject response) {
            if ("success".equals(response.optString("code"))) {
                this.tcrModel.iscollect = new FavFocusModel(response).getResult().iscollect;
                TcrModel tcrModel;
                if (this.tcrModel.iscollect == 1) {
                    tcrModel = this.tcrModel;
                    tcrModel.collectcount++;
                } else if (this.tcrModel.iscollect == 0) {
                    tcrModel = this.tcrModel;
                    tcrModel.collectcount--;
                }
                favIV.setImageResource(this.tcrModel.iscollect == 1 ?
                        R.drawable.img_balloon_yellow : R.drawable.img_balloon_gray);
                favNumTV.setText(this.tcrModel.collectcount + "个关注");
                favLineIV.setImageResource(this.tcrModel.iscollect == 1 ? 
                        R.drawable.bg_corner_blue_tr_br : R.drawable.bg_corner_gray_tr_br);
                return;
            }
            Helper.showToast(response.optString("description"));
        }
    }

    class ViewHolder {
        private TextView countTV;
        private TextView dateTV;
        private TextView distanceTV;
        private ImageView favIV;
        private ImageView favLineIV;
        private TextView favNumTV;
        private ImageView fullIV;
        private ImageView imgIV;
        private TextView locationTV;
        private TextView oldPriceTV;
        private TextView priceTV;
        private TextView titleTV;

    }

    public CourseListAdapter(Context context, List<TcrModel> list) {
        this.iUserCenterV2ServletRequest = new IUserCenterServlet();
        this.errorListener = new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Helper.showToast("服务器异常，请稍后再试");
            }
        };
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(this.context, R.layout.item_main_act, null);
            initCourseView(holder, view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        setupCourseView(holder, position);
        return view;
    }

    private void setupCourseView(final ViewHolder holder, int position) {
        final int index = position;
        holder.favIV.setImageResource(list.get(index).iscollect == 1 ?
                R.drawable.img_balloon_yellow : R.drawable.img_balloon_gray);
        holder.favNumTV.setText(list.get(index).collectcount + "个关注");
        ImageLoader.getInstance().displayImage(
                BEnvironment.SERVER_IMG_URL + list.get(index).headphoto, 
                holder.imgIV, 
                AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());
        
        holder.titleTV.setText(list.get(index).title);
        holder.dateTV.setText(list.get(index).courseTime);
        holder.locationTV.setText(list.get(index).coursesite);
        setupPriceTV(holder, index);
        setupCountTV(holder, index);
        holder.distanceTV.setText("·距离" + list.get(index).coursedistance);
        holder.favLineIV.setImageResource(list.get(index).iscollect == 1 ?
                R.drawable.bg_corner_blue_tr_br : R.drawable.bg_corner_gray_tr_br);
        
        holder.fullIV.setVisibility(list.get(index).courseHavecount == 0 ? 0 : 4);
        holder.favNumTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserPreference.getInstance(context).isLogin()) {
                    IUserCenterServlet.sendFavFocus(
                            list.get(index).courseid, 2,
                            new FavListener(list.get(index), holder.favIV, holder.favLineIV, holder.favNumTV),
                            errorListener);
                } else {
                    LoginActivity.startActivityForResult(context);
                }
            }
        });
        holder.favIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserPreference.getInstance(context).isLogin()) {
                    IUserCenterServlet.sendFavFocus(list.get(index).courseid, 2,
                            new FavListener(list.get(index), holder.favIV, holder.favLineIV, holder.favNumTV),
                            errorListener);
                } else {
                    LoginActivity.startActivityForResult(context);
                }
            }
        });
    }

    private void setupPriceTV(ViewHolder holder, int position) {
        String oldPriceStr = list.get(position).oldCourseprice;
        if (TextUtils.isEmpty(oldPriceStr) || oldPriceStr.equals("0") || oldPriceStr.equals("-1")) {
            holder.oldPriceTV.setVisibility(View.GONE);
        } else {
            holder.oldPriceTV.setVisibility(View.VISIBLE);
            holder.oldPriceTV.setText(oldPriceStr + "元");
        }
        String priceStr = list.get(position).courseprice;
        if (!priceStr.equals("-1")) {
            if (priceStr.equals("0")) {
                holder.priceTV.setVisibility(View.VISIBLE);
                holder.priceTV.setText("免费");
                return;
            }
            holder.priceTV.setVisibility(View.VISIBLE);
            String startSuffixStr = "";
            holder.priceTV.setText(AppUtils.getSpanString(
                    startSuffixStr + list.get(position).suffix, 0,
                    startSuffixStr.length() + priceStr.length(), 2.0f,
                    false, context.getResources().getColor(R.color.text_red), 0));
        }
    }

    private void setupCountTV(ViewHolder holder, int position) {
        int count = list.get(position).courseHavecount;
        if (count <= 0) {
            holder.countTV.setText("");
        } else if (count < 5) {
            holder.countTV.setText("仅剩" + Integer.toString(list.get(position).courseHavecount) + "份");
        } else {
            holder.countTV.setText("剩余" + Integer.toString(list.get(position).courseHavecount) + "份");
        }
    }

    private void initCourseView(ViewHolder holder, View view) {
        holder.imgIV = (ImageView) view.findViewById(R.id.img_imageview);
        holder.favIV = (ImageView) view.findViewById(R.id.fav_imageview);
        holder.titleTV = (TextView) view.findViewById(R.id.title_textview);
        holder.dateTV = (TextView) view.findViewById(R.id.date_textview);
        holder.locationTV = (TextView) view.findViewById(R.id.location_textview);
        holder.favNumTV = (TextView) view.findViewById(R.id.favnum_textview);
        holder.priceTV = (TextView) view.findViewById(R.id.price_textview);
        holder.distanceTV = (TextView) view.findViewById(R.id.distance_textview);
        holder.oldPriceTV = (TextView) view.findViewById(R.id.old_price_textview);
        holder.countTV = (TextView) view.findViewById(R.id.count_textview);
        holder.favLineIV = (ImageView) view.findViewById(R.id.fav_line_imageview);
        holder.fullIV = (ImageView) view.findViewById(R.id.full_imageview);
        holder.imgIV.getLayoutParams().height = AppUtils.getImgItemHeight();
    }

    public void setList(List<TcrModel> list, boolean isClean) {
        if (isClean) {
            this.list.clear();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<TcrModel> getList() {
        return this.list;
    }
}
