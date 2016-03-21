package com.martn.weekend.fragment;

import android.os.Bundle;

import com.martn.weekend.base.BaseActivity;
import com.martn.weekend.base.BaseFragment;
import com.martn.weekend.view.RefreshListView;

/**
 * Title: Weekend
 * Package: com.martn.weekend.fragment
 * Description: ("请描述功能")
 * Date 2016/3/21 17:35
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class HomeListNewFragment extends BaseFragment{

    private static final String TAG_ID = "tag_id";
    private int tagId;
    private RefreshListView listview;
    private int pageIndex = 1;

    public static HomeListNewFragment newInstance(int position, int id) {
        HomeListNewFragment f = new HomeListNewFragment();
        Bundle b = new Bundle();
        b.putInt(TAG_ID, id);
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }


    public void setTagId(int tagId) {
        this.tagId = tagId;
    }




    public void scrollTop() {
        this.listview.smoothScrollToPosition(0);
    }

    public void onRefresh() {
        pageIndex = 1;
        scrollTop();
    }
}
