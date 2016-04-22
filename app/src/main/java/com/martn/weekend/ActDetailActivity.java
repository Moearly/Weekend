package com.martn.weekend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.martn.weekend.app.App;
import com.martn.weekend.base.BaseActivity;
import com.martn.weekend.request.IQueryCourseReleaseServlet;
import com.martn.weekend.result.HotCourseNewResult;
import com.martn.weekend.result.ToDetailNewResult;
import com.martn.weekend.utility.cache.ACache;
import com.martn.weekend.view.CusTextView;
import com.martn.weekend.view.CustomTextView;
import com.qmusic.uitls.Helper;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("活动详情")
 * Date 2014/10/5 12:40
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class ActDetailActivity extends BaseActivity {

    public static final String CACHE_KEY = "key_course_detail_";
    public static final String COURSE_ID = "courseid";
    public static final int REQUEST_CODE = 1232;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_topbar_title)
    ImageView ivTopbarTitle;
    @Bind(R.id.iv_topbar_share)
    ImageView ivTopbarShare;
    @Bind(R.id.iv_topbar_message)
    ImageView ivTopbarMessage;
    @Bind(R.id.iv_topbar_unread)
    CircleImageView ivTopbarUnread;
    @Bind(R.id.vp_imgs)
    ViewPager vpImgs;
    @Bind(R.id.ll_point)
    LinearLayout llPoint;
    @Bind(R.id.fl_vp)
    FrameLayout flVp;
    @Bind(R.id.tv_page_num)
    CusTextView tvPageNum;
    @Bind(R.id.iv_fav)
    ImageView ivFav;
    @Bind(R.id.tv_title)
    CusTextView tvTitle;
    @Bind(R.id.tv_date)
    CusTextView tvDate;
    @Bind(R.id.tv_old_price)
    CusTextView tvOldPrice;
    @Bind(R.id.tv_price)
    CusTextView tvPrice;
    @Bind(R.id.line1_view)
    View line1View;
    @Bind(R.id.tv_light_point_title)
    CusTextView tvLightPointTitle;
    @Bind(R.id.ll_light_point_content)
    LinearLayout llLightPointContent;
    @Bind(R.id.line2_view)
    View line2View;
    @Bind(R.id.center_view)
    View centerView;
    @Bind(R.id.tv_detail)
    CusTextView tvDetail;
    @Bind(R.id.iv_detail_line)
    ImageView ivDetailLine;
    @Bind(R.id.tv_notice)
    CusTextView tvNotice;
    @Bind(R.id.iv_notice_line)
    ImageView ivNoticeLine;
    @Bind(R.id.rl_info)
    RelativeLayout rlInfo;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.iv_sender_head)
    CircleImageView ivSenderHead;
    @Bind(R.id.iv_sender_arrow)
    ImageView ivSenderArrow;
    @Bind(R.id.tv_sender_name)
    CusTextView tvSenderName;
    @Bind(R.id.tv_sender_type)
    CusTextView tvSenderType;
    @Bind(R.id.tv_sender_signature)
    CusTextView tvSenderSignature;
    @Bind(R.id.rl_sender_info)
    RelativeLayout rlSenderInfo;
    @Bind(R.id.rl_sender)
    RelativeLayout rlSender;
    @Bind(R.id.tv_location)
    CusTextView tvLocation;
    @Bind(R.id.iv_center_location)
    ImageView ivCenterLocation;
    @Bind(R.id.iv_location)
    ImageView ivLocation;
    @Bind(R.id.iv_phone)
    ImageView ivPhone;
    @Bind(R.id.rl_location)
    RelativeLayout rlLocation;
    @Bind(R.id.tv_description)
    CustomTextView tvDescription;
    @Bind(R.id.tv_hint_title)
    CusTextView tvHintTitle;
    @Bind(R.id.hint_content_layout)
    LinearLayout hintContentLayout;
    @Bind(R.id.hint_img_layout)
    LinearLayout hintImgLayout;
    @Bind(R.id.iv_system_line)
    ImageView ivSystemLine;
    @Bind(R.id.tv_online_system)
    CusTextView tvOnlineSystem;
    @Bind(R.id.tv_interest_title)
    CusTextView tvInterestTitle;
    @Bind(R.id.tv_interest_num)
    CusTextView tvInterestNum;
    @Bind(R.id.interest_num_layout)
    FrameLayout interestNumLayout;
    @Bind(R.id.interest_head_layout)
    LinearLayout interestHeadLayout;
    @Bind(R.id.sv_interest_head)
    HorizontalScrollView svInterestHead;
    @Bind(R.id.interest_layout)
    RelativeLayout interestLayout;
    @Bind(R.id.tv_question_title)
    CusTextView tvQuestionTitle;
    @Bind(R.id.tv_answer)
    CusTextView tvAnswer;
    @Bind(R.id.question_title_layout)
    RelativeLayout questionTitleLayout;
    @Bind(R.id.iv_question_head)
    CircleImageView ivQuestionHead;
    @Bind(R.id.tv_question_name)
    CusTextView tvQuestionName;
    @Bind(R.id.tv_question_date)
    CusTextView tvQuestionDate;
    @Bind(R.id.question_info_layout)
    RelativeLayout questionInfoLayout;
    @Bind(R.id.question_user_layout)
    RelativeLayout questionUserLayout;
    @Bind(R.id.tv_question_content)
    CusTextView tvQuestionContent;
    @Bind(R.id.iv_reply_triangle)
    ImageView ivReplyTriangle;
    @Bind(R.id.tv_reply_name)
    CusTextView tvReplyName;
    @Bind(R.id.tv_reply_content)
    CusTextView tvReplyContent;
    @Bind(R.id.reply_layout)
    RelativeLayout replyLayout;
    @Bind(R.id.tv_more_question)
    CusTextView tvMoreQuestion;
    @Bind(R.id.tv_recommend_act_title)
    CusTextView tvRecommendActTitle;
    @Bind(R.id.recommend_act_layout)
    LinearLayout recommendActLayout;
    @Bind(R.id.rl_content)
    RelativeLayout rlContent;
    @Bind(R.id.sv_content)
    ScrollView svContent;
    @Bind(R.id.tv_count)
    CusTextView tvCount;
    @Bind(R.id.count_layout)
    RelativeLayout countLayout;
    @Bind(R.id.tv_sign)
    CusTextView tvSign;
    @Bind(R.id.bottom_buy_layout)
    LinearLayout bottomBuyLayout;
    @Bind(R.id.packages_layout)
    LinearLayout packagesLayout;
    @Bind(R.id.packages_scrollview)
    ScrollView packagesScrollview;
    @Bind(R.id.bottom_layout)
    LinearLayout bottomLayout;
    @Bind(R.id.center_top_view)
    View centerTopView;
    @Bind(R.id.tv_detail_top)
    CusTextView tvDetailTop;
    @Bind(R.id.iv_detail_line_top)
    ImageView ivDetailLineTop;
    @Bind(R.id.tv_notice_top)
    CusTextView tvNoticeTop;
    @Bind(R.id.iv_notice_line_top)
    ImageView ivNoticeLineTop;
    @Bind(R.id.info_top_layout)
    RelativeLayout infoTopLayout;
    @Bind(R.id.iv_guide_detail)
    ImageView ivGuideDetail;
    @Bind(R.id.rl_main)
    RelativeLayout rlMain;
    private int courseid;
    private int inway;

    int[] topLocation = new int[2];
    int[] location = new int[2];
    private int infoType = -1;
    private boolean isFinsish = false;
    private boolean isStart = false;

    private ACache aCache;
    private ToDetailNewResult detailResult;
    private HotCourseNewResult hotCourseResult;

    public static void comeBady(Context context, int courseid) {
        Intent intent = new Intent(context, ActDetailActivity.class);
        intent.putExtra(COURSE_ID, courseid);
        context.startActivity(intent);
    }

    public static void comeBady(Context context, int courseid, int inway) {
        Intent intent = new Intent(context, ActDetailActivity.class);
        intent.putExtra(COURSE_ID, courseid);
        intent.putExtra("inway", inway);
        context.startActivity(intent);
    }

    public static void comeBadyForResult(Context context, int courseid) {
        Intent intent = new Intent(context, ActDetailActivity.class);
        intent.putExtra(COURSE_ID, courseid);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
    }

    private Response.Listener<JSONObject> toDetailListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            KLog.e("ActListFragment : detailResult-----> response = " + response);
            detailResult = new ToDetailNewResult(response);
            if (detailResult.success) {
                //将数据保存在缓存中
                aCache.put(CACHE_KEY+courseid,response.toString());
                setupView();
                setupGuide();
                IQueryCourseReleaseServlet.hotCourse(
                        courseid, hotCourseListener, errorListener);
            } else {
                Helper.showToast(detailResult.description);
            }
            dismissLoading();
        }
    };

    private Response.Listener<JSONObject> hotCourseListener =  new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            hotCourseResult = new HotCourseNewResult(response);
            KLog.json(response.toString());
            if (hotCourseResult.success) {
                initRecommendView();
            } else {
                Helper.showToast(detailResult.description);
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Helper.showToast("服务器异常，请稍后再试");
            dismissLoading();
        }
    };


    /**
     * 初始化热门
     */
    private void initRecommendView() {

    }


    private void setupGuide() {

    }

    private void setupView() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detail);
        ButterKnife.bind(this);
        App.addPayActivity(this);
        aCache = ACache.get(ctx);
        Uri uri = getIntent().getData();
        if (uri != null) {
            courseid = Integer.parseInt(uri.getQueryParameter("courseid"));
            inway = Integer.parseInt(uri.getQueryParameter("inway"));
        } else {
            courseid = getIntent().getIntExtra("courseid", -10086);
            inway = getIntent().getExtras().getInt("inway", 0);
        }
        initView();
        initData();
//        reigstNewMsgBroadcastReceiver();
    }

    private void initData() {
        showLoading();
        try {
            String detailStr = aCache.getAsString(CACHE_KEY+courseid);
            if (!TextUtils.isEmpty(detailStr)) {
                detailResult = new ToDetailNewResult(new JSONObject(detailStr));
                setupView();
            }
            IQueryCourseReleaseServlet.toDetail(this.courseid, this.toDetailListener, this.errorListener);
        } catch (JSONException e) {
            IQueryCourseReleaseServlet.toDetail(this.courseid, this.toDetailListener, this.errorListener);
        }

    }


    protected void onResume() {
        super.onResume();
//        refreshRed();
        isFinsish = false;
        isStart = true;
        MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        this.isFinsish = true;
        isStart = false;
        MobclickAgent.onPause(this);
    }


    private void initView() {
        //设置类容区域的
        svContent.setOnTouchListener(new View.OnTouchListener() {
            Handler infoHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    int scrollY = svContent.getScrollY();
                    if (msg.what == touchEventId) {
                        if (lastY != scrollY) {
                            lastY = scrollY;
                            infoHandler.sendMessageDelayed(
                                    infoHandler.obtainMessage(touchEventId, svContent), 16);
                        }
                        infoTopLayout.getLocationOnScreen(topLocation);
                        rlInfo.getLocationOnScreen(location);
                        if (topLocation[1] < location[1] || scrollY > (hintContentLayout.getBottom() + rlInfo.getHeight()) + rlLocation.getHeight()) {
                            infoTopLayout.setVisibility(4);
                            return;
                        }
                        infoTopLayout.setVisibility(0);
                        if (scrollY > rlInfo.getTop() && scrollY < tvDescription.getBottom() - infoTopLayout.getHeight()) {
                            setupDetailAndNotice(0);
                        } else if (scrollY >= tvDescription.getBottom() - infoTopLayout.getHeight()) {
                            setupDetailAndNotice(1);
                        }
                    }
                }
            };

            private int lastY = 0;
            private int touchEventId;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (packagesScrollview.getVisibility() == 0) {
                    packagesScrollview.setVisibility(8);
                    tvSign.setText("立即报名");
                }
                if (rlInfo.getVisibility() == 0 && (event.getAction() == 2 || event.getAction() == 1)) {
                    lastY = 0;
                    infoHandler.sendMessageDelayed(infoHandler.obtainMessage(touchEventId, v), 5);
                }
                return false;


            }
        });
    }

    /**
     * 设置详情和注意
     * @param type
     */
    private void setupDetailAndNotice(int type) {
        int i = 0;
        if (infoType != type) {
            int vis;
            tvDetail.setTextColor(type == 0 ? getResources().getColor(R.color.blue)
                    : getResources().getColor(R.color.text_black));
            ImageView imageView = ivDetailLine;
            if (type == 0) {
                vis = View.VISIBLE;
            } else {
                vis = View.INVISIBLE;
            }
            imageView.setVisibility(vis);

            tvNotice.setTextColor(type == 1 ? getResources().getColor(R.color.blue)
                    : getResources().getColor(R.color.text_black));
            imageView = ivNoticeLine;
            if (type == 1) {
                vis = View.VISIBLE;
            } else {
                vis = View.INVISIBLE;
            }
            imageView.setVisibility(vis);

            tvDetailTop.setTextColor(type == 0 ? getResources().getColor(R.color.blue)
                    : getResources().getColor(R.color.text_black));
            imageView = ivDetailLineTop;
            if (type == 0) {
                vis = View.VISIBLE;
            } else {
                vis = View.INVISIBLE;
            }
            imageView.setVisibility(vis);

            tvNoticeTop.setTextColor(type == 1 ? getResources().getColor(R.color.blue) : getResources().getColor(R.color.text_black));
            ImageView imageView2 = ivNoticeLineTop;
            if (type != 1) {
                vis = View.INVISIBLE;
            }
            imageView2.setVisibility(vis);
            infoType = type;
        }
    }

}
