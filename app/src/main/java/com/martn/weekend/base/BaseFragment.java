package com.martn.weekend.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.martn.weekend.utility.cache.ACache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.martn.weekend.app.App;
import com.squareup.leakcanary.RefWatcher;


import java.lang.reflect.Field;

/**
 * Title: LeiFuUIMainDemo
 * Package: com.martn.leifuuimaindemo.one.ui
 * Description: ("fragement的基础类----带acbar")
 * Date 2015/7/31 9:31
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class BaseFragment extends Fragment {

    static final String TAG = BaseFragment.class.getName();
    //所属的ac
    protected Activity activity;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
//    private LeiFuLoading loading;
    private ProgressDialog mLoading;
//    //文件缓存
    protected ACache mCache;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mCache = ACache.get(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        if (refWatcher != null)
            refWatcher.watch(this);
//        RequestManager.cancelAll(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //解决fragment嵌套引起崩溃
        try {
            Field field = Fragment.class.getDeclaredField("mChildFragmentManager");
            field.setAccessible(true);
            field.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }



    protected int getResId() {
        return 0;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = getActivity();
//        initTitle();
    }


//    public void showLoading() {
//
//        try {
//            if (loading == null) {
//                loading = new LeiFuLoading(activity);
//                loading.show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 显示锁定-不可点击的加载
//     */
//    public void showLockableLoading() {
//
//        try {
//            if (loading == null) {
//                loading = new LeiFuLoading(activity, false);
//                loading.show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    /**
//     * 关闭加载
//     */
//    public void dismissLoading() {
//
//        try {
//            if ((loading != null) && (loading.isShowing()))
//                loading.dismiss();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 显示加载
     */
    public void showLoading() {
        if (mLoading == null) {
            mLoading = new ProgressDialog(activity);
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



    public void loadFirst() {

    }

}
