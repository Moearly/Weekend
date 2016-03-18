package com.martn.weekend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.martn.weekend.base.BaseActivity;
import com.martn.weekend.request.IRecommendServlet;
import com.martn.weekend.result.TagsResult;
import com.martn.weekend.view.PagerSlidingTabStrip;
import com.qmusic.common.Common;
import com.qmusic.uitls.SharedPreferencesUtil;
import com.socks.library.KLog;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("请描述功能")
 * Date 2016/3/16 16:12
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class MainActivity extends BaseActivity {

    @Bind(R.id.iv_user_head)
    CircleImageView ivUserHead;
    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.iv_message)
    ImageView ivMessage;
    @Bind(R.id.iv_unread_tip)
    CircleImageView ivUnreadTip;
    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.tv_error)
    TextView tvError;
    TagsResult tagsResult;

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        public void onErrorResponse(VolleyError error) {
            //Utils.dissmissProgressDialog(MainActivity.this.pglog);
            //Utils.showToast("\u670d\u52a1\u5668\u5f02\u5e38\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5");
            if (tagsResult == null || tagsResult.tagList == null || tagsResult.tagList.isEmpty()) {
                tvError.setVisibility(0);
            } else {
                tvError.setVisibility(8);
            }
        }
    };



    private Response.Listener<JSONObject> getShowPagesListener = new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            KLog.e("getShowPagesListener : " + response);
            if ("success".equals(response.optString("code"))) {
                SharedPreferencesUtil.saveStringSharedPreference(MainActivity.this.getApplicationContext(), Common.Key.KEY_SHOW_PAGES, response.toString());
                //downImage(response);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(View.inflate(this, R.layout.activity_main, null));
        ButterKnife.bind(this);
        initView();
        getShowPages();
    }

    private void initView() {

    }

    private void getShowPages() {
        IRecommendServlet.findShowPageList(this.getShowPagesListener, this.errorListener);
    }

}
