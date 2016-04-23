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
import android.view.Window;
import android.view.WindowManager;
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
import com.martn.weekend.db.UserPreference;
import com.martn.weekend.model.CourseBean;
import com.martn.weekend.model.FavFocusModel;
import com.martn.weekend.model.GetOtherUserDetailV2Model;
import com.martn.weekend.model.UserPicBean;
import com.martn.weekend.model.VideoBean;
import com.martn.weekend.request.IUserCenterServlet;
import com.martn.weekend.result.FavFocusResult;
import com.martn.weekend.result.GetOtherUserDetailV3Result;
import com.martn.weekend.utility.AnimateFirstDisplayListener;
import com.martn.weekend.view.CusImageView;
import com.martn.weekend.view.ToastCommom;
import com.martn.weekend.view.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmusic.common.BEnvironment;
import com.qmusic.uitls.Helper;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

import org.json.JSONObject;

import com.martn.weekend.base.BaseActivity;
import com.socks.library.KLog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @Bind(R.id.like_imageview)
    ImageView attentionIV;
    @Bind(R.id.des_textview)
    TextView desTV;
    @Bind(R.id.head_imageview)
    CircleImageView headIV;
    @Bind(R.id.attention_left_layout)
    FrameLayout attentionLLayout;
    @Bind(R.id.attention_layout)
    RelativeLayout attentionLayout;
    @Bind(R.id.attention_right_layout)
    FrameLayout attentionRLayout;
    @Bind(R.id.attention_left_imageview)
    RoundedImageView attentionLIV;
    @Bind(R.id.attention_right_imageview)
    RoundedImageView attentionRIV;
    @Bind(R.id.joined_left_imageview)
    RoundedImageView joinedLIV;
    @Bind(R.id.joined_right_imageview)
    RoundedImageView joinedRIV;
    @Bind(R.id.over_left_imageview)
    RoundedImageView overLIV;
    @Bind(R.id.over_right_imageview)
    RoundedImageView overRIV;
    @Bind(R.id.joined_left_layout)
    FrameLayout joinedLLayout;
    @Bind(R.id.joined_layout)
    RelativeLayout joinedLayout;
    @Bind(R.id.joined_right_layout)
    FrameLayout joinedRLayout;
    @Bind(R.id.over_left_layout)
    FrameLayout overLLayout;
    @Bind(R.id.over_layout)
    RelativeLayout overLayout;
    @Bind(R.id.over_right_layout)
    FrameLayout overRLayout;
    @Bind(R.id.photo_scroll_layout)
    HorizontalScrollView photoScrollView;
    @Bind(R.id.photos_layout)
    LinearLayout photosLayout;

    private LayoutParams photoParams;

    ProgressDialog pDialog;
    private GetOtherUserDetailV3Result result;
    private ToastCommom toastCommom;
    private ArrayList<String> urllist;


    private int photoWidth = 0;
    private int userId;
    private int photoMargin = 0;

    private IUserCenterServlet iUserCenterV2ServletRequest = new IUserCenterServlet();
    Intent intent = new Intent();

    private Listener<JSONObject> favTeacherListener = new Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            KLog.json(response.toString());
            if ("success".equals(response.optString("code"))) {
                FavFocusResult fResult = new FavFocusModel(response).getResult();
                result.teacherIscollect = fResult.iscollect;
                setupAttentionIV();
                if (result.teacherIscollect == 1) {
                    toastCommom.ToastShow(OtherPersonActivity.this, (ViewGroup) findViewById(R.layout.activity_other_person), "关注成功");
                } else if (result.teacherIscollect == 0) {
                    toastCommom.ToastShow(OtherPersonActivity.this, (ViewGroup) findViewById(R.layout.activity_other_person), "取消关注成功");
                }
            } else {
                Helper.showToast(response.optString("description"));
            }
            dismissPDialog();
        }
    };
    private Listener<JSONObject> getOtherUserDetailV3Listener = new Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            KLog.json(response.toString());
            if ("success".equals(response.optString("code"))) {
                result = new GetOtherUserDetailV2Model(response).getResult();
                onRefresh();
            } else {
                Helper.showToast(response.optString("description"));
            }
            dismissPDialog();
        }
    };

    private ErrorListener errorListener = new ErrorListener() {
        public void onErrorResponse(VolleyError error) {
            dismissPDialog();
            Helper.showToast("服务器异常，请稍后再试");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_person);
        ButterKnife.bind(this);
        if (VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.transparent));
            tintManager.setStatusBarTintEnabled(true);
        }
        userId = getIntent().getIntExtra("userid", -1);
        pDialog = new ProgressDialog(this);
        photoMargin = (int) TypedValue.applyDimension(1, 9.0f, getResources().getDisplayMetrics());
        photoParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        urllist = new ArrayList();
        toastCommom = ToastCommom.createToastConfig();
        initView();
        getFinderDetail();
    }

    @OnClick({R.id.back_imageview, R.id.like_imageview, R.id.chat_textview, R.id.head_imageview,
            R.id.over_layout,R.id.over_left_layout,R.id.over_right_layout,R.id.joined_layout,
            R.id.joined_left_layout,R.id.joined_right_layout,R.id.attention_layout,R.id.attention_left_layout,
            R.id.attention_right_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_imageview:
                finish();
                break;
            case R.id.like_imageview:
                if (UserPreference.getInstance(ctx).isLogin()) {
                    showPDialog();
                    IUserCenterServlet.sendFavFocus(result.teacherId, 1, favTeacherListener, errorListener);
                    return;
                }
                LoginActivity.startActivityForResult(this);
                break;
            case R.id.chat_textview:
                //ChatActivity.startActivity(this, result.teacherHuanxinUsername, result.teacherNickname, 1);
                break;
            case R.id.head_imageview:
                intent.setClass(this, ViewPagerActivity.class);
                ArrayList<String> list = new ArrayList();
                list.add(new StringBuilder(BEnvironment.SERVER_IMG_URL)
                        .append(result.teacherPhoto).toString());
                Bundle b = new Bundle();
                b.putInt("index", 0);
                b.putStringArrayList("imgs", list);
                intent.putExtra("bundle", b);
                startActivity(intent);
                break;
            case R.id.over_layout:
                intent.setClass(this, CourseListActivity.class);
                intent.putExtra("title", "发布的活动");
                intent.putExtra("userid", result.teacherId);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.over_left_layout:
                ActDetailActivity.comeBady(this, ((CourseBean) result.teacherOvercourseArr.get(0)).id);
                break;
            case R.id.over_right_layout:
                ActDetailActivity.comeBady(this, ((CourseBean) result.teacherOvercourseArr.get(1)).id);
                break;
            case R.id.joined_layout:
                intent.setClass(this, CourseListActivity.class);
                intent.putExtra("title", "参加的活动");
                intent.putExtra("userid", result.teacherId);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.joined_left_layout:
                ActDetailActivity.comeBady(this, ((CourseBean) result.teacherTakecourseArr.get(0)).id);
                break;
            case R.id.joined_right_layout:
                ActDetailActivity.comeBady(this, ((CourseBean) result.teacherTakecourseArr.get(1)).id);
                break;
            case R.id.attention_layout:
                intent.setClass(this, CourseListActivity.class);
                intent.putExtra("title", "关注的活动");
                intent.putExtra("userid", result.teacherId);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.attention_left_layout:
                ActDetailActivity.comeBady(this, ((CourseBean) result.collectCourseArr.get(0)).id);
                break;
            case R.id.attention_right_layout:
                ActDetailActivity.comeBady(this, ((CourseBean) result.collectCourseArr.get(1)).id);
                break;
            default:
        }
    }

    private void initView() {
        findViewById(R.id.edit_imageview).setVisibility(View.GONE);
    }

    private void onRefresh() {
        setupUserInfo();
        setupOverLayout();
        setupJoinedLayout();
        setupAttentionLayout();
    }

    /**
     * 设置用户个人信息
     */
    private void setupUserInfo() {
        ImageLoader.getInstance().displayImage(
                new StringBuilder(BEnvironment.SERVER_IMG_URL)
                        .append(result.teacherPhoto).toString(), headIV,
                AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());

        ((TextView) findViewById(R.id.name_textview)).setText(result.teacherNickname);
        ((TextView) findViewById(R.id.tag_textview)).setText(result.teacherOneabstract);
        TextView sexTV = (TextView) findViewById(R.id.sex_textview);
        sexTV.setText(result.teacherAge);
        sexTV.setCompoundDrawablesWithIntrinsicBounds(result.teacherSex == 1 ?
                R.drawable.img_male_white : R.drawable.img_female_white, 0, 0, 0);
        sexTV.setBackgroundResource(result.teacherSex == 1 ?
                R.drawable.bg_corner_male : R.drawable.bg_corner_female);
        ((TextView) findViewById(R.id.con_textview)).setText(result.teacherConstellation);
        setupAttentionIV();
        setupPhotoLayout();
        if (!TextUtils.isEmpty(result.teacherRemark)) {
            desTV.setVisibility(View.VISIBLE);
            desTV.setText(result.teacherRemark);
        }
    }

    private void setupPhotoLayout() {
        if (result.videolist.size() + result.piclist.size() > 0) {
            photoScrollView.setVisibility(View.VISIBLE);
            photoScrollView.post(new Runnable() {
                @OnClick
                public void run() {
                    if (photoWidth == 0) {
                        photoWidth = (photoScrollView.getWidth() - (photoMargin * 3)) / 4;
                        photoParams.width = photoWidth;
                        photoParams.height = photoWidth;
                        photoParams.setMargins(0, 0, photoMargin, 0);
                    }
                    if (photoWidth != 0) {
                        int i;
//                        CusImageView iv;
                        photosLayout.removeAllViews();
                        for (i = 0; i < result.videolist.size(); i++) {
                            final CusImageView iv = new CusImageView(OtherPersonActivity.this);
                            iv.setLayoutParams(photoParams);
                            iv.getClass();
                            iv.setType(2);
                            iv.setDelvisibility(8);
                            iv.setIV(result.videolist.get(i).videopicurl);
                            iv.setIndex(i);
                            photosLayout.addView(iv, i);
                            iv.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent("android.intent.action.VIEW");
                                    intent.setDataAndType(
                                            Uri.parse(
                                                    new StringBuilder(BEnvironment.SERVER_IMG_URL)
                                                            .append(result.videolist.get(iv.getIndex()).videovidurl).toString()), "video/*");
                                    startActivity(intent);
                                }
                            });
                        }
                        int startLength = result.videolist.size();
                        int index = 0;
                        for (i = startLength; i < result.piclist.size() + startLength; i++) {
                            final CusImageView iv = new CusImageView(OtherPersonActivity.this);
                            iv.setLayoutParams(photoParams);
                            iv.setIV(result.piclist.get(index).picbigurl);
                            iv.setDelvisibility(View.GONE);
                            iv.setIndex(index);
                            urllist.add(new StringBuilder(BEnvironment.SERVER_IMG_URL)
                                    .append(result.piclist.get(index).picbigurl).toString());
                            photosLayout.addView(iv, i);
                            iv.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(OtherPersonActivity.this, ViewPagerActivity.class);
                                    Bundle b = new Bundle();
                                    b.putInt("index", iv.getIndex());
                                    b.putStringArrayList("imgs", urllist);
                                    intent.putExtra("bundle", b);
                                    startActivity(intent);
                                }
                            });
                            index++;
                        }
                    }
                }
            });
            return;
        }
        photoScrollView.setVisibility(View.GONE);
    }

    private void setupOverLayout() {
        int count = result.teacherOvercourseCount;
        if (count > 0) {
            overLayout.setVisibility(View.VISIBLE);
            findViewById(R.id.line_1).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.over_textview)).setText("已发布活动(" + count + ")");
            int size = result.teacherOvercourseArr == null ? 0 : result.teacherOvercourseArr.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    if (i == 0) {
                        overLLayout.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance()
                                .displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL)
                                        .append(result.teacherOvercourseArr.get(i).photo).toString(),
                                        overLIV, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());

                        ((TextView) findViewById(R.id.over_title_left_textview))
                                .setText(result.teacherOvercourseArr.get(i).title);
                    } else if (i == 1) {
                        overRLayout.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance()
                                .displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL)
                                        .append(result.teacherOvercourseArr.get(i).photo).toString(),
                                        overRIV, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());

                        ((TextView) findViewById(R.id.over_title_right_textview))
                                .setText(result.teacherOvercourseArr.get(i).title);
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /**
     * 设置已参加的人
     */
    private void setupJoinedLayout() {
        int count = result.teacherTakecourseCount;
        if (count > 0) {
            joinedLayout.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.joined_textview)).setText("已参与活动(" + count + ")");
        }
        int size = result.teacherTakecourseArr == null ? 0 : result.teacherTakecourseArr.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    joinedLLayout.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL)
                            .append(result.teacherTakecourseArr.get(i).photo).toString(), joinedLIV,
                            AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());

                    ((TextView) findViewById(R.id.joined_title_left_textview))
                            .setText(result.teacherTakecourseArr.get(i).title);
                } else if (i == 1) {
                    joinedRLayout.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL)
                            .append(result.teacherTakecourseArr.get(i).photo).toString(), joinedRIV,
                            AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());

                    ((TextView) findViewById(R.id.joined_title_right_textview))
                            .setText(result.teacherTakecourseArr.get(i).title);
                } else {
                    return;
                }
            }
        }
    }

    /**
     * 设置关注的人
     */
    private void setupAttentionLayout() {
        int count = result.teacherCollectcourseCount;
        if (count > 0) {
            attentionLayout.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.attention_textview)).setText("已关注活动(" + count + ")");
        }
        int size = result.collectCourseArr == null ? 0 : result.collectCourseArr.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    attentionLLayout.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL)
                            .append(result.collectCourseArr.get(i).photo).toString(), attentionLIV,
                            AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());

                    ((TextView) findViewById(R.id.attention_title_left_textview))
                            .setText(result.collectCourseArr.get(i).title);
                } else if (i == 1) {
                    attentionRLayout.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL)
                            .append(result.collectCourseArr.get(i).photo).toString(), attentionRIV,
                            AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());

                    ((TextView) findViewById(R.id.attention_title_right_textview))
                            .setText(result.collectCourseArr.get(i).title);
                } else {
                    return;
                }
            }
        }
    }

    private void setupAttentionIV() {
        if (result.teacherIsown == 0) {
            attentionIV.setVisibility(0);
            if (result.teacherIscollect == 0) {
                attentionIV.setImageResource(R.drawable.img_attention);
            } else if (result.teacherIscollect == 1) {
                attentionIV.setImageResource(R.drawable.img_attentioned);
            }
        }
    }


    private void getFinderDetail() {
        showPDialog();
        iUserCenterV2ServletRequest.sendGetOtherUserDetailV3(userId, getOtherUserDetailV3Listener, errorListener);
    }

    private void showPDialog() {
        if (pDialog != null && !pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void dismissPDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }


}
