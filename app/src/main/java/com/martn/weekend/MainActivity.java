package com.martn.weekend;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.martn.weekend.adapter.MainPagerNewAdapter;
import com.martn.weekend.base.BaseActivity;
import com.martn.weekend.request.IRecommendServlet;
import com.martn.weekend.request.IUserCenterServlet;
import com.martn.weekend.utility.AnimateFirstDisplayListener;
import com.martn.weekend.view.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.martn.weekend.app.App;
import com.qmusic.base.BaseApplication;
import com.qmusic.bean.LocalNewsBean;
import com.qmusic.common.BEnvironment;
import com.qmusic.common.Common;
import com.martn.weekend.db.UserPreference;
import com.martn.weekend.result.TagsResult;
import com.qmusic.uitls.AppUtils;
import com.qmusic.uitls.Helper;
import com.qmusic.uitls.SharedPreferencesUtil;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    private MainPagerNewAdapter mainPagerNewAdapter;
    private RunHandler runHandler;
    private long nowTime;
    private long lastTime;

    @OnClick({R.id.iv_user_head,R.id.tv_error})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_head:
                if (UserPreference.getInstance(ctx).isLogin()) {
                    UserCenterActivity.comeBaby(this);
                    overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
                    return;
                }

                break;
            case R.id.tv_error:
                tvError.setVisibility(View.INVISIBLE);
                showLoading();
                IRecommendServlet.tags(tagsListener, errorListener);

        }
    }

    private class MyThread extends Thread {

        public void run() {
            while (true) {
                try {
                    IUserCenterServlet.getNewsCount(getNewsCountListener, errorListener);
                    sleep(120000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private MyThread myThread;

    private class RunHandler extends Handler {
        private RunHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            if (myThread == null) {
                myThread = new MyThread();
            }
            if (myThread.isInterrupted() || !myThread.isAlive()) {
                myThread.start();
            }
        }
    }

    private Response.Listener<JSONObject> getNewsCountListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if ("success".equals(response.optString("code"))) {
                LocalNewsBean.getInstance().parse(response);
            } else if ("error_1".equals(response.optString("code"))) {
                UserPreference.getInstance(ctx).clean();
                LocalNewsBean.getInstance().clean();
            }
            //刷新未读消息
            refreshRed();
        }
    };

    private Response.Listener<JSONObject> tagsListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            KLog.json("tagsListener : ", response.toString());
            tagsResult = new TagsResult(response);
            if (tagsResult.success) {
                setupView();
            } else {
                Helper.showToast(tagsResult.description);
            }
            dismissLoading();
        }
    };


    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        public void onErrorResponse(VolleyError error) {
            dismissLoading();
            Helper.showToast("服务器异常，请稍后再试");
            if (tagsResult == null || tagsResult.tagList == null || tagsResult.tagList.isEmpty()) {
                tvError.setVisibility(View.VISIBLE);
            } else {
                tvError.setVisibility(View.GONE);
            }
        }
    };


    private Response.Listener<JSONObject> getShowPagesListener = new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            KLog.json(response.toString());
            if ("success".equals(response.optString("code"))) {
                SharedPreferencesUtil.saveStringSharedPreference(BaseApplication.context(), Common.Key.KEY_SHOW_PAGES, response.toString());
                downImage(response);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        getNewsCount();
        getShowPages();
        showLoading();
        IRecommendServlet.tags(tagsListener, errorListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IUserCenterServlet.getNewsCount(getNewsCountListener, errorListener);
        refreshRed();

        if (UserPreference.getInstance(ctx).isLogin()) {
            ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL)
                    .append(UserPreference.getInstance(ctx).getUserPhoto()).toString(), ivUserHead, AnimateFirstDisplayListener.getHeadOptions(), AnimateFirstDisplayListener.getListener());
        } else {
            ivUserHead.setImageResource(R.drawable.img_head_logout);
        }

        if (App.isRefresh) {
            showLoading();
            IRecommendServlet.tags(tagsListener, errorListener);
        }


    }

    private void getNewsCount() {
        if (UserPreference.getInstance(ctx).isLogin()) {
            if (runHandler == null) {
                runHandler = new RunHandler();
            }
            runHandler.sendEmptyMessage(0);
        }
    }


    private void initView() {

    }

    private void setupView() {
        if (App.isRefresh && mainPagerNewAdapter != null) {
            mainPagerNewAdapter.onRefresh(tagsResult.tagList);
            App.isRefresh = false;
        }
        mainPagerNewAdapter = new MainPagerNewAdapter(getSupportFragmentManager(), tagsResult.tagList);
        viewpager.setAdapter(mainPagerNewAdapter);
        tabs.setViewPager(viewpager);
        tabs.setUnderlineColorResource(R.color.blue);
        tabs.setUnderlineHeight(2);
        tabs.setDividerColorResource(R.color.transparent);
        tabs.setTextColorResource(R.color.main_text_black);
        tabs.setTextSize((int) (((double) AppUtils.getScreenWidth()) * 0.03d));
        tabs.setTypeface(AppUtils.getTypefaceZiTi());
    }


    private void getShowPages() {
        IRecommendServlet.findShowPageList(this.getShowPagesListener, this.errorListener);
    }

    private void downImage(JSONObject json) {
        JSONArray jsonArr = json.optJSONArray("showpage_arr");
        if (jsonArr != null) {
            for (int i = 0; i < jsonArr.length(); i++) {
                ImageLoader.getInstance().loadImage(jsonArr.optJSONObject(i).optString("showpage_photo"), new ImageLoadingListener() {
                    public void onLoadingStarted(String arg0, View arg1) {
                    }

                    public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                    }

                    public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                        try {
                            ImageLoader.getInstance().getDiskCache().save(arg0, arg2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    public void onLoadingCancelled(String arg0, View arg1) {
                    }
                });
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        nowTime = System.currentTimeMillis();
        if (nowTime - lastTime >= 2000 || lastTime == 0) {
            Helper.showToast(getString(R.string.tip_double_click_exit));
            lastTime = nowTime;
            return false;
        }
        MobclickAgent.onKillProcess(this);
        finish();
        return false;
    }

    public void refreshRed() {
        runOnUiThread(new Runnable() {
            public void run() {
                //控制未读消息显示
//                if (LocalNewsBean.getInstance().getAllNewsCount() > 0 || LocalNewsBean.getInstance().getUnreadMsgCountTotal() > 0) {
//                    redIV.setVisibility(0);
//                } else {
//                    redIV.setVisibility(4);
//                }
            }
        });
    }


}
