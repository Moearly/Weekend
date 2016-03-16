package com.martn.weekend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.martn.weekend.base.BaseActivity;
import com.martn.weekend.view.PagerSlidingTabStrip;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        getShowPages();
    }

    private void initView() {

    }

    private void getShowPages() {
//        IRecommendServlet.findShowPageList(this.getShowPagesListener, this.errorListener);
    }

}
