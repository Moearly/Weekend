package com.martn.weekend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.martn.weekend.adapter.ActImgsAdapter;
import com.martn.weekend.app.App;
import com.martn.weekend.base.BaseActivity;
import com.martn.weekend.db.UserPreference;
import com.martn.weekend.model.FavFocusModel;
import com.martn.weekend.model.QuestreplyQuestModel;
import com.martn.weekend.model.QuestreplyReplyModel;
import com.martn.weekend.model.SenderModel;
import com.martn.weekend.request.IQueryCourseReleaseServlet;
import com.martn.weekend.request.IUserCenterServlet;
import com.martn.weekend.result.FavFocusResult;
import com.martn.weekend.result.HotCourseNewResult;
import com.martn.weekend.result.ToDetailNewResult;
import com.martn.weekend.utility.AnimateFirstDisplayListener;
import com.martn.weekend.utility.DateHelper;
import com.martn.weekend.utility.cache.ACache;
import com.martn.weekend.view.CusTextView;
import com.martn.weekend.view.CustomTextView;
import com.martn.weekend.view.InputAnswerPopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.qmusic.common.BEnvironment;
import com.qmusic.uitls.AppUtils;
import com.qmusic.uitls.Helper;
import com.qmusic.uitls.TUtils;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    LinearLayout llPoint;//实现图片的小点区域（滑动）
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
    private boolean isFinsish = false;//控制--暂停和运行
    private boolean isStart = false;

    private ACache aCache;
    private ToDetailNewResult detailResult;
    private HotCourseNewResult hotCourseResult;
    private ActImgsAdapter imgsAdapter;
    private Bitmap shareBit;
    private LayoutParams pointParams;
    private InputAnswerPopupWindow inputWindow;


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
                aCache.put(CACHE_KEY + courseid, response.toString());
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

    private Response.Listener<JSONObject> hotCourseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            hotCourseResult = new HotCourseNewResult(response);
            KLog.json(response.toString());
            if (hotCourseResult.success) {
                setupRecommendView();
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

    private Response.Listener<JSONObject> saveQuestListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if ("success".equals(response.optString("code"))) {
                inputWindow.dismiss();
                IQueryCourseReleaseServlet.toDetail(courseid, toDetailListener, errorListener);
            } else {
                dismissLoading();
            }
            Helper.showToast(response.optString("description"));
        }
    };


    /**
     * 设置引导介绍
     */
    private void setupGuide() {
        if (UserPreference.getInstance(ctx).isGuideDetail()) {
            ImageView guideIV = (ImageView) findViewById(R.id.img_guide_detail);
            guideIV.setVisibility(0);
            guideIV.setOnTouchListener(new AnonymousClass7(guideIV));
        }

    }

    /**
     * 根据返回的课程详情的数据初始化各个view
     */
    private void setupView() {
        if (!isFinsish) {
            setupImgsVP();
            setupPoint(0);
            tvTitle.setText(detailResult.courseDetail.title);
            tvDate.setText(detailResult.courseDetail.datetime);
            setupFavIV();
            setupPriceTV();
            setupLocationTV();
            setupLightPoint();
            setupInfoLayout();
            setupSender();
            setupDescriptionTV();
            setupHint();
            setupSystemInfo();
            setupInterestLayout();
            setupQuestion();
            setupCountTV();
            setupSignUPTV();
            setupPackage();
        }
    }

    /**
     * 设置参加人的显示区域
     */
    private void setupInterestLayout() {
        if ((detailResult.courseDetail.userFavePhotoList == null ? 0 : detailResult.courseDetail.userFavePhotoList.size()) > 0) {
            interestLayout.setVisibility(View.VISIBLE);
            tvInterestNum.setText(String.valueOf(detailResult.courseDetail.userFaveCount) + "人参与");

            svInterestHead.post(new Runnable() {
                @Override
                public void run() {
                    float width = (float) svInterestHead.getHeight();
                    LinearLayout interestHeadLayout = (LinearLayout) findViewById(R.id.interest_head_layout);
                    interestHeadLayout.removeAllViews();
                    for (int i = 0; i < detailResult.courseDetail.userFavePhotoList.size(); i++) {
                        ImageView headIV = new CircleImageView(ctx);
                        LayoutParams params = new LayoutParams((int) width, (int) width);
                        params.setMargins(0, 0, 20, 0);
                        headIV.setLayoutParams(params);
                        interestHeadLayout.addView(headIV);
                        ImageLoader.getInstance().displayImage(
                                BEnvironment.SERVER_IMG_URL + detailResult.courseDetail.userFavePhotoList.get(i),
                                headIV,
                                AnimateFirstDisplayListener.getHeadOptions(), AnimateFirstDisplayListener.getListener());
                    }

                }
            });
        }
    }

    /**
     * 设置问题区域
     */
    private void setupQuestion() {
        int questionSize;
        questionTitleLayout.setVisibility(View.VISIBLE);
        if (detailResult.courseDetail.questreplyList == null) {
            questionSize = 0;
        } else {
            questionSize = detailResult.courseDetail.questreplyList.size();
        }
        if (questionSize > 0) {

            questionUserLayout.setVisibility(View.VISIBLE);

            final QuestreplyQuestModel quest = detailResult.courseDetail.questreplyList.get(0).quest;
            ImageLoader.getInstance().displayImage(
                    BEnvironment.SERVER_IMG_URL + quest.questUserPhoto,
                    ivQuestionHead,
                    AnimateFirstDisplayListener.getHeadOptions(), AnimateFirstDisplayListener.getListener());

            tvQuestionName.setText(quest.questUserNickname);
            tvQuestionDate.setText(DateHelper.format(new Date(quest.questDatetime), "yyyy-MM-dd HH:mm"));
            tvQuestionContent.setText(quest.questContent);
            tvQuestionContent.setVisibility(View.VISIBLE);
            questionUserLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (detailResult.courseDetail.isapply == 0 && packagesLayout.getVisibility() == View.VISIBLE) {
                        packagesLayout.setVisibility(View.GONE);
                        tvSign.setText("立即报名");
                        return;
                    }
                    OtherPersonActivity.comeBady(ctx, quest.questUserid);
                }
            });

            QuestreplyReplyModel reply = detailResult.courseDetail.questreplyList.get(0).reply;
            String replyContent = reply.replyContent;
            if (!TextUtils.isEmpty(replyContent)) {
                ivReplyTriangle.setVisibility(View.VISIBLE);
                replyLayout.setVisibility(View.VISIBLE);
                tvReplyName.setText(reply.replyUserNickname + "回复:");
                tvReplyContent.setText(replyContent);
            }
            tvMoreQuestion.setText("查看更多提问 >");
        } else {
            tvMoreQuestion.setText("暂时没有提问");
        }
    }

    /**
     * 热门课程区域
     */
    private void setupRecommendView() {
        if (!isFinsish) {
            int size = hotCourseResult.tcrList == null ? 0 : hotCourseResult.tcrList.size();
            if (size > 0) {
                tvRecommendActTitle.setVisibility(View.VISIBLE);
                tvRecommendActTitle.setText(hotCourseResult.courseHotType);


                recommendActLayout.setVisibility(View.VISIBLE);
                recommendActLayout.removeAllViews();
                for (int i = 0; i < size; i++) {
                    View view = View.inflate(this, R.layout.item_hot_course, null);
                    ImageLoader.getInstance().displayImage(
                            BEnvironment.SERVER_IMG_URL + hotCourseResult.tcrList.get(i).headphoto,
                            (ImageView) view.findViewById(R.id.iv_img), AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());
                    ((TextView) view.findViewById(R.id.tv_title)).setText(hotCourseResult.tcrList.get(i).title);
                    ((TextView) view.findViewById(R.id.tv_date)).setText(hotCourseResult.tcrList.get(i).courseTime);
                    ((TextView) view.findViewById(R.id.tv_location)).setText(hotCourseResult.tcrList.get(i).coursesite);
                    TextView priceTV = (TextView) view.findViewById(R.id.tv_price);
                    ImageView favIV = (ImageView) view.findViewById(R.id.iv_fav);
                    favIV.setImageResource(hotCourseResult.tcrList.get(i).iscollect == 1 ? R.drawable.img_detail_fav_yellow : R.drawable.img_detail_fav_gray);
                    final int index = i;
                    favIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (detailResult.courseDetail.isapply == 0 && packagesLayout.getVisibility() == View.VISIBLE) {
                                packagesLayout.setVisibility(View.GONE);
                                tvSign.setText("立即报名");
                            } else if (UserPreference.getInstance(ctx).isLogin()) {
                                IUserCenterServlet.sendFavFocus(courseid, 2, new FavRecommendListener((ImageView) view, hotCourseResult.tcrList.get(index).courseid), errorListener);
                            } else {
                                LoginActivity.startActivityForResult(ctx);
                            }

                        }
                    });
                    String priceStr = hotCourseResult.tcrList.get(i).courseprice;
                    if (!priceStr.equals("-1")) {
                        if (priceStr.equals("0")) {
                            priceTV.setVisibility(View.VISIBLE);
                            priceTV.setText("免费");
                        } else {
                            priceTV.setVisibility(View.VISIBLE);
                            String startSuffixStr = "";
                            priceTV.setText(TUtils.getSpanString(
                                    new StringBuilder(String.valueOf(startSuffixStr))
                                            .append(hotCourseResult.tcrList.get(i).suffix).toString()
                                    , 0, startSuffixStr.length() + priceStr.length(), 2.0f, false, getResources().getColor(R.color.text_red), 0));
                        }
                    }
                    recommendActLayout.addView(view);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (detailResult.courseDetail.isapply == 0 && packagesLayout.getVisibility() == View.VISIBLE) {
                                packagesLayout.setVisibility(View.GONE);
                                tvSign.setText("立即报名");
                                return;
                            }
                            ActDetailActivity.comeBady(ctx, hotCourseResult.tcrList.get(index).courseid);
                        }
                    });
                }
            }
        }
    }

    /**
     * 设置计数显示---显示活动剩余数量
     */
    private void setupCountTV() {
        String countStr = new StringBuilder(String.valueOf(detailResult.courseDetail.newHavecount)).append("份").toString();
        SpannableString sp1 = TUtils.getSpanString(countStr, 0, countStr.length(), 1.5f, false, getResources().getColor(R.color.main_text_gray), 0);
        tvCount.setText("");
        tvCount.append("剩余");
        tvCount.append(sp1);
    }

    /**
     *
     */
    private void setupSignUPTV() {
        if (detailResult.courseDetail.isapply == 0) {
            tvSign.setText(detailResult.courseDetail.isapplydes);
            tvSign.setVisibility(View.VISIBLE);
            tvSign.setBackgroundResource(R.color.blue);
            bottomLayout.setVisibility(View.VISIBLE);
            tvSign.setClickable(true);
        } else if (detailResult.courseDetail.isapply != -1) {
            tvSign.setText(detailResult.courseDetail.isapplydes);
            tvSign.setVisibility(View.VISIBLE);
            tvSign.setBackgroundResource(R.color.gray_new);
            bottomLayout.setVisibility(View.VISIBLE);
            tvSign.setClickable(false);
        }
    }

    private void setupPackage() {
        int size = detailResult.courseDetail.priceList == null ? 0 : detailResult.courseDetail.priceList.size();
        if (size > 1) {
            LinearLayout packageLayout = (LinearLayout) findViewById(R.id.packages_layout);
            packageLayout.removeAllViews();
            for (int i = 0; i < size; i++) {
                View view = View.inflate(this, R.layout.item_package, null);
                ((TextView) view.findViewById(R.id.tv_price)).setText(detailResult.courseDetail.priceList.get(i).once);
                ((TextView) view.findViewById(R.id.tv_content)).setText(
                        new StringBuilder(String.valueOf(detailResult.courseDetail.priceList.get(i).desc))
                                .append(" (剩余")
                                .append(detailResult.courseDetail.priceList.get(i).count)
                                .append("份) ")
                                .toString());
                TextView selectTV = (TextView) view.findViewById(R.id.tv_select);
                if (detailResult.courseDetail.priceList.get(i).count > 0) {
                    selectTV.setText("选这个");
                    selectTV.setTextColor(getResources().getColor(R.color.blue));
                    selectTV.setBackgroundResource(R.drawable.bg_frame_corner_blue);
                    final int finalI = i;
                    selectTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PayActivity.comeBady(ctx, courseid, detailResult.courseDetail.priceList.get(finalI).id);
                        }
                    });
                } else {
                    selectTV.setText("已售完");
                    selectTV.setTextColor(getResources().getColor(R.color.main_text_gray));
                    selectTV.setBackgroundResource(R.drawable.bg_frame_corner_gray);
                }
                packageLayout.addView(view);
            }
        }
    }


    private void setupSystemInfo() {
        if (TextUtils.isEmpty(detailResult.courseDetail.hint)) {
            ivSystemLine.setVisibility(View.GONE);
        }
    }


    /**
     *
     */
    private void setupHint() {
        int i;
        String hint = detailResult.courseDetail.hint;
        int imgsSize = detailResult.courseDetail.hintPhotoList == null ? 0 : detailResult.courseDetail.hintPhotoList.size();
        if (!TextUtils.isEmpty(hint) || imgsSize > 0) {
            tvHintTitle.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(hint)) {
            String[] hints = hint.split("\\|");
            if (hints.length > 0) {
                hintContentLayout.setVisibility(View.VISIBLE);
                hintContentLayout.removeAllViews();
                for (CharSequence text : hints) {
                    View view = View.inflate(this, R.layout.item_hint_content, null);
                    ((TextView) view.findViewById(R.id.tv_info)).setText(text);
                    hintContentLayout.addView(view);
                }
            }
        }
        if (imgsSize > 0) {
            int itemCount = imgsSize < 4 ? imgsSize : 4;
            int imgWidth = (int) (((double) AppUtils.getScreenWidth()) * 0.1d);
            hintImgLayout.setVisibility(0);
            LayoutParams rowParams = new LayoutParams(-1, -2);
            i = 0;
            while (i < imgsSize) {
                if (i % itemCount == 0) {
                    LinearLayout rowLayout = new LinearLayout(this);
                    rowLayout.setLayoutParams(rowParams);
                    rowLayout.setOrientation(0);
                    rowLayout.setGravity(imgsSize < 4 ? 1 : 3);
                    itemCount = imgsSize - i < 4 ? imgsSize - i : 4;
                    for (int j = 0; j < itemCount; j++) {
                        View view = View.inflate(this, R.layout.item_hint_img, null);
                        ((TextView) view.findViewById(R.id.tv_name)).setText(detailResult.courseDetail.hintPhotoList.get(i).name);
                        ImageView img = (ImageView) view.findViewById(R.id.iv_img);
                        ViewGroup.LayoutParams params = img.getLayoutParams();
                        params.width = imgWidth;
                        params.height = imgWidth;
                        img.setLayoutParams(params);
                        ImageLoader.getInstance().displayImage(
                                BEnvironment.SERVER_IMG_URL + detailResult.courseDetail.hintPhotoList.get(i).photo,
                                img,
                                AnimateFirstDisplayListener.getOptions(),
                                AnimateFirstDisplayListener.getListener());
                        rowLayout.addView(view, AppUtils.getScreenWidth() / 4, -2);
                    }
                    hintImgLayout.addView(rowLayout);
                }
                i++;
            }
        }
    }


    /**
     * 设置活动描述
     */
    private void setupDescriptionTV() {
        ArrayList<HashMap<String, String>> datas = new ArrayList();
        if (detailResult.courseDetail.descriptionList != null && !detailResult.courseDetail.descriptionList.isEmpty()) {
            for (int i = 0; i < detailResult.courseDetail.descriptionList.size(); i++) {
                HashMap<String, String> hashMap = new HashMap();
                hashMap.put("type", "image");
                hashMap.put("value", BEnvironment.SERVER_IMG_URL +
                        detailResult.courseDetail.descriptionList.get(i).title);

                datas.add(hashMap);
                datas.addAll(getDescriptionData(detailResult.courseDetail.descriptionList.get(i).content));
            }
        } else if (!TextUtils.isEmpty(detailResult.courseDetail.description)) {
            datas.addAll(getDescriptionData(detailResult.courseDetail.description));
        }
        if (!datas.isEmpty()) {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.removeAllViews();
            tvDescription.setText(datas);
        }
    }

    /**
     * 获取描述信息
     *
     * @param description
     * @return
     */
    private ArrayList<HashMap<String, String>> getDescriptionData(String description) {
        ArrayList<HashMap<String, String>> datas = new ArrayList();
        KLog.i(description);
        String[] des = description.replaceAll(":img]\n", ":img]").replaceAll(":img]", ":img]\n").split("\\[img:|\\n");
        int i = 0;
        while (i < des.length) {
            HashMap<String, String> hashMap = new HashMap();
            hashMap.put("type", des[i].indexOf(":img]") != -1 ? "image" : "text");
            hashMap.put("value", des[i].indexOf(":img]") != -1 ? BEnvironment.SERVER_IMG_URL + des[i].replaceAll(":img]", "") : des[i]);
            datas.add(hashMap);
            i++;
        }
        return datas;
    }


    /**
     * 活动的发布者
     */
    private void setupSender() {
        SenderModel sender = detailResult.courseDetail.sender;
        if (sender != null) {

            rlSender.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(
                    BEnvironment.SERVER_IMG_URL + sender.photo,
                    ivSenderHead,
                    AnimateFirstDisplayListener.getHeadOptions(), AnimateFirstDisplayListener.getListener());
            tvSenderName.setText(sender.nickname);
            TUtils.setTextViewBg(tvSenderType, sender.type);
            tvSenderSignature.setText(sender.oneabstract);
        }
    }


    /**
     * 活动的信息
     */
    private void setupInfoLayout() {
        String des = detailResult.courseDetail.description;
        String hint = detailResult.courseDetail.hint;
        int size = detailResult.courseDetail.descriptionList == null ? 0 : detailResult.courseDetail.descriptionList.size();
        if (size > 0 && !TextUtils.isEmpty(hint)) {
            rlInfo.setVisibility(View.VISIBLE);
        } else if (size == 0 && !TextUtils.isEmpty(des) && !TextUtils.isEmpty(hint)) {
            rlInfo.setVisibility(View.VISIBLE);
        }
    }


    private void setupLightPoint() {
        String lightpTitle = detailResult.courseDetail.lightspotTitle;
        String lightpContent = detailResult.courseDetail.lightspotContent;
        if (!(TextUtils.isEmpty(lightpTitle) || TextUtils.isEmpty(lightpContent))) {
            tvLightPointTitle.setVisibility(View.VISIBLE);
            tvLightPointTitle.setText(lightpTitle);
        }
        if (!TextUtils.isEmpty(lightpContent)) {
            String[] lightpContents = lightpContent.split("\\|");
            if (lightpContents.length > 0) {
                llLightPointContent.setVisibility(View.VISIBLE);
                llLightPointContent.removeAllViews();
                for (CharSequence text : lightpContents) {
                    View view = View.inflate(this, R.layout.item_hint_content, null);
                    ((TextView) view.findViewById(R.id.tv_info)).setText(text);
                    llLightPointContent.addView(view);
                }
            }
        }
    }

    /**
     * 设置收藏喜欢按钮
     */
    private void setupFavIV() {
        ivFav.setImageResource(detailResult.courseDetail.iscollect == 1 ? R.drawable.img_detail_fav_yellow : R.drawable.img_detail_fav_gray);
    }

    /**
     * 设置价格显示
     */
    private void setupPriceTV() {
        String oldPriceStr = detailResult.courseDetail.oldPrice;
        if (!(TextUtils.isEmpty(oldPriceStr) || oldPriceStr.equals("0") || oldPriceStr.equals("-1"))) {
            tvOldPrice.setVisibility(View.VISIBLE);
            tvOldPrice.setText(new StringBuilder(String.valueOf(oldPriceStr)).append("元").toString());
        }
        String priceStr = detailResult.courseDetail.price;
        if (!priceStr.equals("-1")) {
            if (priceStr.equals("0")) {
                tvPrice.setVisibility(View.VISIBLE);
                tvPrice.setText("免费");
                return;
            }
            tvPrice.setVisibility(View.VISIBLE);
            String startSuffixStr = "";
            tvPrice.setText(TUtils.getSpanString(
                    new StringBuilder(String.valueOf(startSuffixStr)).append(detailResult.courseDetail.suffix).toString(),
                    0,
                    startSuffixStr.length() + priceStr.length(),
                    2.0f,
                    false,
                    getResources().getColor(R.color.text_red), -1));
        }
    }

    /**
     * 设置活动位置的显示
     */
    private void setupLocationTV() {
        String location = detailResult.courseDetail.detailPlace;
        if (TextUtils.isEmpty(location)) {
            rlLocation.setVisibility(View.GONE);
            return;
        }
        tvLocation.setText(location);
        tvLocation.setVisibility(View.VISIBLE);
//        tvLocation.setOnClickListener(this);
    }


    /**
     * 设置图片显示的viewpager
     */
    private void setupImgsVP() {
        int size = detailResult.courseDetail.photoList == null ? 0 : detailResult.courseDetail.photoList.size();
        if (size == 0 && !TextUtils.isEmpty(detailResult.courseDetail.photo)) {
            detailResult.courseDetail.photoList = new ArrayList();
            detailResult.courseDetail.photoList.add(detailResult.courseDetail.photo);//处理没有列表图片--只有单张图片的显示
            size = 1;
        }
        if (size > 0) {
            ViewGroup.LayoutParams vpParams = vpImgs.getLayoutParams();
            vpParams.height = (int) (((double) AppUtils.getScreenHeight()) * 0.45d);
            vpImgs.setLayoutParams(vpParams);
            vpImgs.setVisibility(View.VISIBLE);
            imgsAdapter = new ActImgsAdapter(this, detailResult.courseDetail.photoList, new PackageVisible());

            ViewGroup.LayoutParams imgsParams = vpImgs.getLayoutParams();
            imgsParams.height = (int) (((double) AppUtils.getScreenHeight()) * 0.45d);
            vpImgs.setLayoutParams(imgsParams);
            vpImgs.setAdapter(imgsAdapter);
            vpImgs.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    tvPageNum.setText((position + 1) + "/" + detailResult.courseDetail.photoList.size());
                    setupPoint(position);
                }

                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                public void onPageScrollStateChanged(int arg0) {
                }
            });
            tvPageNum.setVisibility(View.INVISIBLE);
            tvPageNum.setText("1/" + detailResult.courseDetail.photoList.size());
            //分享的图片
            ImageLoader.getInstance().loadImage(detailResult.courseDetail.photoList.get(0), new ImageLoadingListener() {
                public void onLoadingStarted(String arg0, View arg1) {
                }

                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                }

                public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                    shareBit = arg2;
                }

                public void onLoadingCancelled(String arg0, View arg1) {
                }
            });
        }
    }

    private void setupPoint(int index) {
        int size = detailResult.courseDetail.photoList.size();
        KLog.e("setupPoint size : " + size);
        if (size > 0) {
            if (pointParams == null) {
                pointParams = new LayoutParams((int) (((double) AppUtils.getScreenWidth()) * 0.02d), (int) (((double) AppUtils.getScreenWidth()) * 0.02d));
                pointParams.setMargins(5, 0, 5, 0);
            }
            llPoint.removeAllViews();
            for (int i = 0; i < size; i++) {
                CircleImageView civ = new CircleImageView(this);
                civ.setLayoutParams(pointParams);
                if (i == index) {
                    civ.setImageResource(R.color.blue);
                } else {
                    civ.setImageResource(R.color.gray);
                }
                llPoint.addView(civ);
            }
        }
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
            String detailStr = aCache.getAsString(CACHE_KEY + courseid);
            if (!TextUtils.isEmpty(detailStr)) {
                detailResult = new ToDetailNewResult(new JSONObject(detailStr));//活动详情返回的数据
                setupView();
            }
            IQueryCourseReleaseServlet.toDetail(courseid, toDetailListener, errorListener);
        } catch (JSONException e) {
            IQueryCourseReleaseServlet.toDetail(courseid, toDetailListener, errorListener);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        refreshRed();
        isFinsish = false;
        isStart = true;
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFinsish = true;
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
     *
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

    @OnClick({R.id.iv_back, R.id.tv_location, R.id.iv_location, R.id.interest_layout,R.id.rl_sender,R.id.interest_num_layout,R.id.tv_answer,R.id.tv_more_question,R.id.tv_sign})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_location:
            case R.id.iv_location:
                MapActivity.comeBady(this,
                        TextUtils.isEmpty(detailResult.courseDetail.detailPlace) ? "上课地点" : detailResult.courseDetail.detailPlace,
                        detailResult.courseDetail.placeX,
                        detailResult.courseDetail.placeY);
                break;
            case R.id.rl_sender:
                //其他人
                OtherPersonActivity.comeBady(this, detailResult.courseDetail.sender.id);
                break;
            case R.id.interest_layout:
            case R.id.interest_num_layout:
                //感兴趣的
                InterestPeopleActivity.startActivity(this, courseid);
                break;
            case R.id.tv_answer:
                if (UserPreference.getInstance(ctx).isLogin()) {
                    inputWindow = new InputAnswerPopupWindow(ctx, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showLoading();
                            IQueryCourseReleaseServlet.sendSaveQuest(courseid, inputWindow.getInput(), saveQuestListener, errorListener);

                        }
                    });
                    inputWindow.showAtLocation(rlMain, 81, 0, 0);
                    return;
                }
                LoginActivity.startActivityForResult(this);
                break;
            case R.id.tv_more_question:
                QuestReplyActivity.comeBady(this, courseid);
                break;

            case R.id.tv_sign:
                //购买
                if (detailResult.courseDetail.isapply != 0) {
                    return;
                }
                if (detailResult.courseDetail.priceList.size() == 1) {
                    PayActivity.comeBady(this, courseid, detailResult.courseDetail.priceList.get(0).id);
                } else if (this.detailResult.courseDetail.priceList.isEmpty()) {
                    PayActivity.comeBady(this, courseid, detailResult.courseDetail.priceList.get(0).id);
                } else if (packagesLayout.getVisibility() == View.VISIBLE) {
                    packagesLayout.setVisibility(View.GONE);
                    tvSign.setText("立即报名");
                } else {
                    packagesLayout.setVisibility(View.VISIBLE);
                    tvSign.setText("请选择套餐");
                }
                break;
        }

    }

    class PackageVisible implements ActImgsAdapter.PackageVisibleCallback {
        @Override
        public boolean visible() {
            if (detailResult.courseDetail.isapply != 0 || packagesLayout.getVisibility() != View.VISIBLE) {
                return false;
            }
            packagesLayout.setVisibility(View.GONE);
            tvSign.setText("立即报名");
            return true;
        }
    }


    private class FavRecommendListener implements Response.Listener<JSONObject> {
        int courseid;
        ImageView favIV;

        public FavRecommendListener(ImageView favIV, int courseid) {
            this.courseid = courseid;
            this.favIV = favIV;
        }

        @Override
        public void onResponse(JSONObject response) {
            if ("success".equals(response.optString("code"))) {
                FavFocusResult favResult = new FavFocusModel(response).getResult();
                Helper.showToast(favResult.iscollect == 1 ? "已关注" : "已取消");
                favIV.setImageResource(favResult.iscollect == 1 ? R.drawable.img_detail_fav_yellow : R.drawable.img_detail_fav_gray);
                return;
            }
            Helper.showToast(response.optString("description"));
        }
    }

}

