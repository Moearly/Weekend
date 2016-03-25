package com.martn.weekend.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.martn.weekend.MainActivity;
import com.martn.weekend.R;
import com.qmusic.base.BaseApplication;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;


/**
 * Title: ZeaApp
 * Package: com.martn.zeaapp.base
 * Description: ("Activity基础类")
 * Date 2014/10/5 14:58
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class BaseActivity extends AppCompatActivity {

    //全局上下文
    protected Context ctx;
    protected BaseActivity activity;
    //自定义的进度加载器
    //private LoadingFragment mLoading;
    private ProgressDialog mLoading;
    private LinearLayout rootLayout;
    private SystemBarTintManager tintManager;


    public void comeOnBaby(Class pClass) {
        startActivity(new Intent(this, pClass));
    }

    @SuppressLint({"InlinedApi"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //bug监听上传
        ctx = this;
        activity = this;
        MobclickAgent.openActivityDurationTrack(false);
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (tintManager == null) {
                tintManager = new SystemBarTintManager(this);
            }
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.blue));
            tintManager.setStatusBarTintEnabled(true);
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.topbar_left_textview /*2131493401*/:
//                finish();
            default:
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //RequestManager.cancelAll(this);
    }

    @Override
    protected void onPause() {

        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onBackPressed() {
        //MainAc还开在---则启动mainAc
        if (!((BaseApplication) getApplication()).getIsMainOpened()) {
            startActivity(new Intent(this.ctx, MainActivity.class));
            finish();
        }
        super.onBackPressed();
    }


    /**
     * 显示加载
     */
    public void showLoading() {
        if (mLoading == null) {
            mLoading = new ProgressDialog(this);
        }
        if (!mLoading.isShowing()) {
            mLoading.setMessage("请稍候...");
            mLoading.show();
        }

    }


    /**
     * 关闭加载
     */
    public void dismissLoading() {

        try {
            if (mLoading != null)
                mLoading.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected boolean getBooleanExtra(String key) {

        return getIntent().getBooleanExtra(key, false);
    }

    protected int getIntExtra(String key) {

        return getIntent().getIntExtra(key, -1);
    }

    protected String getStringExtra(String key) {

        return getIntent().getStringExtra(key);
    }

}
