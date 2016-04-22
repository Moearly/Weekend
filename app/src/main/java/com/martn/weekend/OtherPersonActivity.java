package com.martn.weekend;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.martn.weekend.model.FavFocusModel;
import com.martn.weekend.request.IUserCenterServlet;
import com.martn.weekend.result.FavFocusResult;
import com.martn.weekend.result.GetOtherUserDetailV3Result;
import com.martn.weekend.view.ToastCommom;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmusic.common.BEnvironment;
import com.qmusic.uitls.Helper;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

import org.json.JSONObject;

import com.martn.weekend.base.BaseActivity;
import com.socks.library.KLog;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("其他成员")
 * Date 2016/4/18 14:31
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class OtherPersonActivity extends BaseActivity {

    public static void comeBady(Context context, int userid) {
        Intent intent = new Intent(context, OtherPersonActivity.class);
        intent.putExtra("userid", userid);
        context.startActivity(intent);
    }
//
//    private ImageView attentionIV;
//    private RoundedImageView attentionLIV;
//    private FrameLayout attentionLLayout;
//    private RelativeLayout attentionLayout;
//    private RoundedImageView attentionRIV;
//    private FrameLayout attentionRLayout;
//    private TextView desTV;
//
//
//    private CircleImageView headIV;
//    private RoundedImageView joinedLIV;
//    private FrameLayout joinedLLayout;
//    private RelativeLayout joinedLayout;
//    private RoundedImageView joinedRIV;
//    private FrameLayout joinedRLayout;
//    private RoundedImageView overLIV;
//    private FrameLayout overLLayout;
//    private RelativeLayout overLayout;
//    private RoundedImageView overRIV;
//    private FrameLayout overRLayout;
//    ProgressDialog pDialog;
//    private LayoutParams photoParams;
//    HorizontalScrollView photoScrollView;
//    private LinearLayout photosLayout;
//    private GetOtherUserDetailV3Result result;
//    private ToastCommom toastCommom;
//    private ArrayList<String> urllist;
//
//    private int photoWidth = 0;
//    private int userId;
//    private int photoMargin = 0;
//
//    private IUserCenterServlet iUserCenterV2ServletRequest = new IUserCenterServlet();
//    Intent intent = new Intent();
//
//    private Listener<JSONObject> favTeacherListener = new Listener<JSONObject>() {
//        @Override
//        public void onResponse(JSONObject response) {
//            KLog.json(response.toString());
//            if ("success".equals(response.optString("code"))) {
//                FavFocusResult fResult = new FavFocusModel(response).getResult();
//                result.teacherIscollect = fResult.iscollect;
//                setupAttentionIV();
//                if (result.teacherIscollect == 1) {
//                    toastCommom.ToastShow(OtherPersonActivity.this, (ViewGroup) findViewById(R.layout.activity_other_person), "关注成功");
//                } else if (result.teacherIscollect == 0) {
//                    toastCommom.ToastShow(OtherPersonActivity.this, (ViewGroup) findViewById(R.layout.activity_other_person), "取消关注成功");
//                }
//            } else {
//                Helper.showToast(response.optString("description"));
//            }
//            dismissPDialog();
//        }
//    };
//    private Listener<JSONObject> getOtherUserDetailV3Listener = new Listener<JSONObject>() {
//        @Override
//        public void onResponse(JSONObject response) {
//            KLog.json(response.toString());
//            if ("success".equals(response.optString("code"))) {
//                result = new GetOtherUserDetailV2Model(response).getResult();
//                onRefresh();
//            } else {
//                Helper.showToast(response.optString("description"));
//            }
//            dismissPDialog();
//        }
//    };
//
//    private ErrorListener errorListener = new ErrorListener() {
//        public void onErrorResponse(VolleyError error) {
//            dismissPDialog();
//            Helper.showToast("服务器异常，请稍后再试");
//        }
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_other_person);
//        if (VERSION.SDK_INT >= 19) {
//            getWindow().addFlags(67108864);
//            getWindow().addFlags(134217728);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintColor(getResources().getColor(R.color.t));
//            tintManager.setStatusBarTintEnabled(true);
//        }
//        this.userId = getIntent().getIntExtra("userid", -1);
//        this.pDialog = new ProgressDialog(this);
//        this.photoMargin = (int) TypedValue.applyDimension(1, 9.0f, getResources().getDisplayMetrics());
//        this.photoParams = new LayoutParams(-2, -2);
//        this.urllist = new ArrayList();
//        this.toastCommom = ToastCommom.createToastConfig();
//        initView();
//        getFinderDetail();
//    }

//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.back_imageview /*2131493094*/:
//                finish();
//            case R.id.like_imageview /*2131493095*/:
//                if (LocalUserInfo.getInstance().isLogin()) {
//                    showPDialog();
//                    IUserCenterServlet.sendFavFocus(this.result.teacherId, 1, this.favTeacherListener, this.errorListener);
//                    return;
//                }
//                LoginOrRegistActivity.startActivityForResult(this);
//            case R.id.chat_textview /*2131493096*/:
//                ChatActivity.startActivity(this, this.result.teacherHuanxinUsername, this.result.teacherNickname, 1);
//            case R.id.head_imageview /*2131493098*/:
//                this.intent.setClass(this, ViewPagerActivity.class);
//                ArrayList<String> list = new ArrayList();
//                list.add(new StringBuilder(BEnvironment.SERVER_IMG_URL).append(this.result.teacherPhoto).toString());
//                Bundle b = new Bundle();
//                b.putInt("index", 0);
//                b.putStringArrayList("imgs", list);
//                this.intent.putExtra("bundle", b);
//                startActivity(this.intent);
//            case R.id.over_layout /*2131493101*/:
//                this.intent.setClass(this, CourseListActivity.class);
//                this.intent.putExtra(BWebActivity.TITLE, "\u53d1\u5e03\u7684\u6d3b\u52a8");
//                this.intent.putExtra("userid", this.result.teacherId);
//                this.intent.putExtra(CandidatePacketExtension.TYPE_ATTR_NAME, 0);
//                startActivity(this.intent);
//            case R.id.over_left_layout /*2131493104*/:
//                ActDetailActivity.startActivity(this, ((CourseBean) this.result.teacherOvercourseArr.get(0)).id);
//            case R.id.over_right_layout /*2131493107*/:
//                ActDetailActivity.startActivity(this, ((CourseBean) this.result.teacherOvercourseArr.get(1)).id);
//            case R.id.joined_layout /*2131493110*/:
//                this.intent.setClass(this, CourseListActivity.class);
//                this.intent.putExtra(BWebActivity.TITLE, "\u53c2\u52a0\u7684\u6d3b\u52a8");
//                this.intent.putExtra("userid", this.result.teacherId);
//                this.intent.putExtra(CandidatePacketExtension.TYPE_ATTR_NAME, 1);
//                startActivity(this.intent);
//            case R.id.joined_left_layout /*2131493113*/:
//                ActDetailActivity.startActivity(this, ((CourseBean) this.result.teacherTakecourseArr.get(0)).id);
//            case R.id.joined_right_layout /*2131493116*/:
//                ActDetailActivity.startActivity(this, ((CourseBean) this.result.teacherTakecourseArr.get(1)).id);
//            case R.id.attention_layout /*2131493119*/:
//                this.intent.setClass(this, CourseListActivity.class);
//                this.intent.putExtra(BWebActivity.TITLE, "\u5173\u6ce8\u7684\u6d3b\u52a8");
//                this.intent.putExtra("userid", this.result.teacherId);
//                this.intent.putExtra(CandidatePacketExtension.TYPE_ATTR_NAME, 2);
//                startActivity(this.intent);
//            case R.id.attention_left_layout /*2131493123*/:
//                ActDetailActivity.startActivity(this, ((CourseBean) this.result.collectCourseArr.get(0)).id);
//            case R.id.attention_right_layout /*2131493126*/:
//                ActDetailActivity.startActivity(this, ((CourseBean) this.result.collectCourseArr.get(1)).id);
//            default:
//        }
//    }
//
//    private void initView() {
//        findViewById();
//    }
//
//    private void onRefresh() {
//        setupUserInfo();
//        setupOverLayout();
//        setupJoinedLayout();
//        setupAttentionLayout();
//    }
//
//    private void setupUserInfo() {
//        ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL).append(this.result.teacherPhoto).toString(), this.headIV, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());
//        ((TextView) findViewById(R.id.name_textview)).setText(this.result.teacherNickname);
//        ((TextView) findViewById(R.id.tag_textview)).setText(this.result.teacherOneabstract);
//        TextView sexTV = (TextView) findViewById(R.id.sex_textview);
//        sexTV.setText(this.result.teacherAge);
//        sexTV.setCompoundDrawablesWithIntrinsicBounds(this.result.teacherSex == 1 ? R.drawable.img_male_white : R.drawable.img_female_white, 0, 0, 0);
//        sexTV.setBackgroundResource(this.result.teacherSex == 1 ? R.drawable.bg_corner_male : R.drawable.bg_corner_female);
//        ((TextView) findViewById(R.id.con_textview)).setText(this.result.teacherConstellation);
//        setupAttentionIV();
//        setupPhotoLayout();
//        if (!TextUtils.isEmpty(this.result.teacherRemark)) {
//            this.desTV.setVisibility(0);
//            this.desTV.setText(this.result.teacherRemark);
//        }
//        this.headIV.setOnClickListener(this);
//    }
//
//    private void setupPhotoLayout() {
//        if (this.result.videolist.size() + this.result.piclist.size() > 0) {
//            this.photoScrollView.setVisibility(0);
//            this.photoScrollView.post(new Runnable() {
//
//                /* renamed from: com.qmusic.activities.OtherPersonActivity.4.1 */
//                class AnonymousClass1 implements OnClickListener {
//                    private final /* synthetic */ MyIV val$iv;
//
//                    AnonymousClass1(MyIV myIV) {
//                        this.val$iv = myIV;
//                    }
//
//                    public void onClick(View v) {
//                        Intent intent = new Intent("android.intent.action.VIEW");
//                        intent.setDataAndType(Uri.parse(new StringBuilder(BEnvironment.SERVER_IMG_URL).append(((VideoBean) result.videolist.get(this.val$iv.getIndex())).videovidurl).toString()), "video/*");
//                        startActivity(intent);
//                    }
//                }
//
//                /* renamed from: com.qmusic.activities.OtherPersonActivity.4.2 */
//                class AnonymousClass2 implements OnClickListener {
//                    private final /* synthetic */ MyIV val$iv;
//
//                    AnonymousClass2(MyIV myIV) {
//                        this.val$iv = myIV;
//                    }
//
//                    public void onClick(View v) {
//                        Intent intent = new Intent(OtherPersonActivity.this, ViewPagerActivity.class);
//                        Bundle b = new Bundle();
//                        b.putInt("index", this.val$iv.getIndex());
//                        b.putStringArrayList("imgs", urllist);
//                        intent.putExtra("bundle", b);
//                        startActivity(intent);
//                    }
//                }
//
//                public void run() {
//                    if (photoWidth == 0) {
//                        photoWidth = (photoScrollView.getWidth() - (photoMargin * 3)) / 4;
//                        photoParams.width = photoWidth;
//                        photoParams.height = photoWidth;
//                        photoParams.setMargins(0, 0, photoMargin, 0);
//                    }
//                    if (photoWidth != 0) {
//                        int i;
//                        MyIV iv;
//                        photosLayout.removeAllViews();
//                        for (i = 0; i < result.videolist.size(); i++) {
//                            iv = new MyIV(OtherPersonActivity.this);
//                            iv.setLayoutParams(photoParams);
//                            iv.getClass();
//                            iv.setType(2);
//                            iv.setDelvisibility(8);
//                            iv.setIV(((VideoBean) result.videolist.get(i)).videopicurl);
//                            iv.setIndex(i);
//                            photosLayout.addView(iv, i);
//                            iv.setOnClickListener(new AnonymousClass1(iv));
//                        }
//                        int startLength = result.videolist.size();
//                        int index = 0;
//                        for (i = startLength; i < result.piclist.size() + startLength; i++) {
//                            iv = new MyIV(OtherPersonActivity.this);
//                            iv.setLayoutParams(photoParams);
//                            iv.setIV(((UserPicBean) result.piclist.get(index)).picbigurl);
//                            iv.setDelvisibility(8);
//                            iv.setIndex(index);
//                            urllist.add(new StringBuilder(BEnvironment.SERVER_IMG_URL).append(((UserPicBean) result.piclist.get(index)).picbigurl).toString());
//                            photosLayout.addView(iv, i);
//                            iv.setOnClickListener(new AnonymousClass2(iv));
//                            index++;
//                        }
//                    }
//                }
//            });
//            return;
//        }
//        this.photoScrollView.setVisibility(8);
//    }
//
//    private void setupOverLayout() {
//        int count = this.result.teacherOvercourseCount;
//        if (count > 0) {
//            this.overLayout.setVisibility(0);
//            findViewById(R.id.line_1).setVisibility(0);
//            ((TextView) findViewById(R.id.over_textview)).setText("\u5df2\u53d1\u5e03\u6d3b\u52a8(" + count + Separators.RPAREN);
//            int size = this.result.teacherOvercourseArr == null ? 0 : this.result.teacherOvercourseArr.size();
//            if (size > 0) {
//                for (int i = 0; i < size; i++) {
//                    if (i == 0) {
//                        this.overLLayout.setVisibility(0);
//                        ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL).append(((CourseBean) this.result.teacherOvercourseArr.get(i)).photo).toString(), this.overLIV, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());
//                        ((TextView) findViewById(R.id.over_title_left_textview)).setText(((CourseBean) this.result.teacherOvercourseArr.get(i)).title);
//                    } else if (i == 1) {
//                        this.overRLayout.setVisibility(0);
//                        ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL).append(((CourseBean) this.result.teacherOvercourseArr.get(i)).photo).toString(), this.overRIV, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());
//                        ((TextView) findViewById(R.id.over_title_right_textview)).setText(((CourseBean) this.result.teacherOvercourseArr.get(i)).title);
//                    } else {
//                        return;
//                    }
//                }
//            }
//        }
//    }
//
//    private void setupJoinedLayout() {
//        int count = this.result.teacherTakecourseCount;
//        if (count > 0) {
//            this.joinedLayout.setVisibility(0);
//            ((TextView) findViewById(R.id.joined_textview)).setText("\u5df2\u53c2\u4e0e\u6d3b\u52a8(" + count + Separators.RPAREN);
//        }
//        int size = this.result.teacherTakecourseArr == null ? 0 : this.result.teacherTakecourseArr.size();
//        if (size > 0) {
//            for (int i = 0; i < size; i++) {
//                if (i == 0) {
//                    this.joinedLLayout.setVisibility(0);
//                    ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL).append(((CourseBean) this.result.teacherTakecourseArr.get(i)).photo).toString(), this.joinedLIV, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());
//                    ((TextView) findViewById(R.id.joined_title_left_textview)).setText(((CourseBean) this.result.teacherTakecourseArr.get(i)).title);
//                } else if (i == 1) {
//                    this.joinedRLayout.setVisibility(0);
//                    ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL).append(((CourseBean) this.result.teacherTakecourseArr.get(i)).photo).toString(), this.joinedRIV, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());
//                    ((TextView) findViewById(R.id.joined_title_right_textview)).setText(((CourseBean) this.result.teacherTakecourseArr.get(i)).title);
//                } else {
//                    return;
//                }
//            }
//        }
//    }
//
//    private void setupAttentionLayout() {
//        int count = this.result.teacherCollectcourseCount;
//        if (count > 0) {
//            this.attentionLayout.setVisibility(0);
//            ((TextView) findViewById(R.id.attention_textview)).setText("\u5df2\u5173\u6ce8\u6d3b\u52a8(" + count + Separators.RPAREN);
//        }
//        int size = this.result.collectCourseArr == null ? 0 : this.result.collectCourseArr.size();
//        if (size > 0) {
//            for (int i = 0; i < size; i++) {
//                if (i == 0) {
//                    this.attentionLLayout.setVisibility(0);
//                    ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL).append(((CourseBean) this.result.collectCourseArr.get(i)).photo).toString(), this.attentionLIV, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());
//                    ((TextView) findViewById(R.id.attention_title_left_textview)).setText(((CourseBean) this.result.collectCourseArr.get(i)).title);
//                } else if (i == 1) {
//                    this.attentionRLayout.setVisibility(0);
//                    ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL).append(((CourseBean) this.result.collectCourseArr.get(i)).photo).toString(), this.attentionRIV, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());
//                    ((TextView) findViewById(R.id.attention_title_right_textview)).setText(((CourseBean) this.result.collectCourseArr.get(i)).title);
//                } else {
//                    return;
//                }
//            }
//        }
//    }
//
//    private void setupAttentionIV() {
//        if (this.result.teacherIsown == 0) {
//            this.attentionIV.setVisibility(0);
//            if (this.result.teacherIscollect == 0) {
//                this.attentionIV.setImageResource(R.drawable.img_attention);
//            } else if (this.result.teacherIscollect == 1) {
//                this.attentionIV.setImageResource(R.drawable.img_attentioned);
//            }
//        }
//    }
//
//    private void findViewById() {
//        this.headIV = (CircleImageView) findViewById(R.id.head_imageview);
//        this.attentionIV = (ImageView) findViewById(R.id.like_imageview);
//        this.desTV = (TextView) findViewById(R.id.des_textview);
//        this.photoScrollView = (HorizontalScrollView) findViewById(R.id.photo_scroll_layout);
//        this.photosLayout = (LinearLayout) findViewById(R.id.photos_layout);
//        this.overLayout = (RelativeLayout) findViewById(R.id.over_layout);
//        this.joinedLayout = (RelativeLayout) findViewById(R.id.joined_layout);
//        this.attentionLayout = (RelativeLayout) findViewById(R.id.attention_layout);
//        this.overLayout.setOnClickListener(this);
//        this.joinedLayout.setOnClickListener(this);
//        this.attentionLayout.setOnClickListener(this);
//        this.overLIV = (RoundedImageView) findViewById(R.id.over_left_imageview);
//        this.overRIV = (RoundedImageView) findViewById(R.id.over_right_imageview);
//        this.joinedLIV = (RoundedImageView) findViewById(R.id.joined_left_imageview);
//        this.joinedRIV = (RoundedImageView) findViewById(R.id.joined_right_imageview);
//        this.attentionLIV = (RoundedImageView) findViewById(R.id.attention_left_imageview);
//        this.attentionRIV = (RoundedImageView) findViewById(R.id.attention_right_imageview);
//        this.overLLayout = (FrameLayout) findViewById(R.id.over_left_layout);
//        this.overRLayout = (FrameLayout) findViewById(R.id.over_right_layout);
//        this.joinedLLayout = (FrameLayout) findViewById(R.id.joined_left_layout);
//        this.joinedRLayout = (FrameLayout) findViewById(R.id.joined_right_layout);
//        this.attentionLLayout = (FrameLayout) findViewById(R.id.attention_left_layout);
//        this.attentionRLayout = (FrameLayout) findViewById(R.id.attention_right_layout);
//        this.overLLayout.setOnClickListener(this);
//        this.overRLayout.setOnClickListener(this);
//        this.joinedLLayout.setOnClickListener(this);
//        this.joinedRLayout.setOnClickListener(this);
//        this.attentionLLayout.setOnClickListener(this);
//        this.attentionRLayout.setOnClickListener(this);
//        this.attentionIV.setOnClickListener(this);
//        ((ImageView) findViewById(R.id.back_imageview)).setOnClickListener(this);
//        ((ImageView) findViewById(R.id.edit_imageview)).setVisibility(8);
//    }
//
//    private void getFinderDetail() {
//        showPDialog();
//        this.iUserCenterV2ServletRequest.sendGetOtherUserDetailV3(this.userId, this.getOtherUserDetailV3Listener, this.errorListener);
//    }
//
//    private void showPDialog() {
//        if (this.pDialog != null && !this.pDialog.isShowing()) {
//            this.pDialog.show();
//        }
//    }
//
//    private void dismissPDialog() {
//        if (this.pDialog != null && this.pDialog.isShowing()) {
//            this.pDialog.dismiss();
//        }
//    }



}
