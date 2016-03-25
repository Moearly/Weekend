package com.martn.weekend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.martn.weekend.request.IUserCenterServlet;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmusic.base.BaseApplication;
import com.qmusic.bean.ShowPageBean;
import com.qmusic.common.Common;
import com.qmusic.db.UserPreference;
import com.qmusic.result.ToUserCenterModel;
import com.qmusic.uitls.AppUtils;
import com.qmusic.uitls.SharedPreferencesUtil;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    @Bind(R.id.splash_imageview)
    ImageView ivSplash;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_skip)
    TextView tvSkip;
    @Bind(R.id.iv_mid)
    ImageView ivMid;
    @Bind(R.id.iv_icon)
    ImageView ivIcon;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    Intent intent;
    private int defaultTime = 3000;

    private Response.Listener<JSONObject> toUserCenterListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            KLog.e("toUserCenterListener : " + response);
            if (response != null && "success".equals(response.optString("code"))) {
                UserPreference.getInstance(SplashActivity.this).save( new ToUserCenterModel(response).getResult(), "", UserPreference.getInstance(SplashActivity.this).getMobile());
                KLog.e("user_info_get_success");
                //EMChatManager.getInstance().login(LocalUserInfo.getInstance().getUserHuanxinUsername(), LocalUserInfo.getInstance().getUserHuanxinPassword(), new EMLoginCallBack());
            } else if ("error_1".equals(response.optString("code"))) {
                UserPreference.getInstance(SplashActivity.this).clean();
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
        }
    };


    private ShowPageBean bean;//展示的活动

    private int time;

    private boolean isClick = false;
    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            time = time - 1;
            setupTimeTV(time);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        tvTime.setTypeface(AppUtils.getTypefaceZiTi());
        autoLogin();
        showSplash();

    }

    private boolean isShowPage() {
        return !TextUtils.isEmpty(SharedPreferencesUtil.getStringSharedPreference(getApplicationContext(), Common.Key.KEY_SHOW_PAGES, ""));
    }


    private void showSplash() {
        if (isShowPage()) {
            try {
                JSONArray jsonArr = new JSONObject(SharedPreferencesUtil.getStringSharedPreference(getApplicationContext(), Common.Key.KEY_SHOW_PAGES, "")).optJSONArray("showpage_arr");
                KLog.json(jsonArr.toString());
                if (jsonArr != null) {
                    long nowTime = System.currentTimeMillis();
                    int type = -1;
                    int i = 0;
                    while (i < jsonArr.length()) {
                        ShowPageBean bean = new ShowPageBean();
                        bean.parse(jsonArr.optJSONObject(i));
                        KLog.e("showPages-----> time : " + nowTime + "|" + bean.startTime + "|" + bean.endTime + "|" + (nowTime - bean.startTime) + " | " + (bean.endTime - nowTime));
                        if (nowTime < bean.startTime || nowTime > bean.endTime || TextUtils.isEmpty(bean.photo) || ImageLoader.getInstance().getDiskCache().get(bean.photo) == null) {
                            i++;
                        } else {
                            Bitmap bit = BitmapFactory.decodeFile(ImageLoader.getInstance().getDiskCache().get(bean.photo).getPath());
                            if (bit != null) {
                                bean = bean;
                                type = bean.type;
                                ivSplash.setImageBitmap(bit);
                                setupTimeTV(3);
                                startTime();
                                goToMainAct();
                            }
                            if (type == -1) {
                                ivSplash.setBackgroundColor(getResources().getColor(R.color.bg_splash));
                                ivIcon.setVisibility(0);
                                ivMid.setVisibility(0);
                                goToMainAct();
                                return;
                            } else if (type != 4) {
                                ivIcon.setVisibility(4);
                                ivMid.setVisibility(4);
                                tvVersion.setVisibility(4);
                                tvTime.setVisibility(4);
                                tvSkip.setVisibility(4);
                                return;
                            } else {
                                ivIcon.setVisibility(4);
                                ivMid.setVisibility(4);
                                tvVersion.setVisibility(4);
                                tvTime.setVisibility(0);
                                tvSkip.setVisibility(0);
                                return;
                            }
                        }
                    }
                    if (type == -1) {
                        ivSplash.setBackgroundColor(getResources().getColor(R.color.bg_splash));
                        ivIcon.setVisibility(0);
                        ivMid.setVisibility(0);
                        goToMainAct();
                        return;
                    } else if (type != 4) {
                        ivIcon.setVisibility(4);
                        ivMid.setVisibility(4);
                        tvVersion.setVisibility(4);
                        tvTime.setVisibility(0);
                        tvSkip.setVisibility(0);
                        return;
                    } else {
                        ivIcon.setVisibility(4);
                        ivMid.setVisibility(4);
                        tvVersion.setVisibility(4);
                        tvTime.setVisibility(4);
                        tvSkip.setVisibility(4);
                        return;
                    }
                }
                return;
            } catch (JSONException e) {
                 ivSplash.setImageResource(getResources().getColor(R.color.bg_splash));
                 ivIcon.setVisibility(0);
                 ivMid.setVisibility(0);
                goToMainAct();
                return;
            }
        }
         ivSplash.setImageResource(getResources().getColor(R.color.bg_splash));
         ivIcon.setVisibility(0);
         ivMid.setVisibility(0);
        goToMainAct();
    }


    @OnClick({ R.id.tv_skip, R.id.tv_time, R.id.splash_imageview })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_skip:
            case R.id.tv_time:
                isClick = true;
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.splash_imageview:
                if (bean != null) {
                    isClick = true;
                    if (bean.type == 0) {
                        //ActDetailActivity.startActivity(this, Integer.parseInt(bean.id), 1);
                        finish();
                    } else if (bean.type == 1) {
                        //SearchClassActivity.startActivity(this, bean.tagname, 1);
                        finish();
                    } else if (bean.type == 2) {
                        intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        intent = new Intent("android.intent.action.VIEW", Uri.parse(bean.url));
                        startActivity(intent);
                        finish();
                    } else if (bean.type == 3) {
//                        intent = new Intent(this, SpecialActivity.class);
//                        intent.putExtra("dissertationid", bean.id);
//                        intent.putExtra("inway", 1);
//                        startActivity(intent);
                        finish();
                    } else if (bean.type == 4) {
                        isClick = false;
                    }
                }
            break;


        }
    }

    private void setupTimeTV(int time) {
        tvTime.setText(AppUtils.getSpanString(new StringBuilder(String.valueOf(time)).append("S").toString(), 0, 1, 1, false, Color.parseColor("#e92d50"), 0));
    }


    private void goToMainAct() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (!isClick) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, (long) defaultTime);
    }



    private void startTime() {
        for (int i = 2; i >= 0; i--) {
            timeHandler.sendEmptyMessageDelayed(0, (long) ((i + 1) * 1000));
        }
    }



    /**
     * 自动登陆--其实就是获取用户的数据
     */
    private void autoLogin() {
        if (UserPreference.getInstance(this).isLogin()) {
            //开始获取用户数据
            UserPreference.getInstance(this).loadLocalUserInfo(UserPreference.getInstance(this).getUserId());
            KLog.i("start get User data---- userid："+UserPreference.getInstance(this).getUserId());
            IUserCenterServlet.sendToUserCenter(toUserCenterListener, errorListener);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
