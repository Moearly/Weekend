package com.martn.weekend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.martn.weekend.adapter.PersonalAdapter;
import com.martn.weekend.base.BaseActivity;
import com.martn.weekend.request.IUserCenterServlet;
import com.martn.weekend.utility.AnimateFirstDisplayListener;
import com.martn.weekend.view.CusTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmusic.common.BEnvironment;
import com.martn.weekend.db.UserPreference;
import com.martn.weekend.result.ToUserCenterModel;
import com.qmusic.uitls.Helper;
import com.socks.library.KLog;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("个人用户中心")
 * Date 2016/3/25 17:13
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class UserCenterActivity extends BaseActivity implements AdapterView.OnItemClickListener{


    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.iv_message)
    ImageView ivMessage;
    @Bind(R.id.iv_red)
    CircleImageView ivRed;
    @Bind(R.id.iv_head)
    CircleImageView ivHead;
    @Bind(R.id.tv_name)
    CusTextView tvName;
    @Bind(R.id.tv_tag)
    CusTextView tvTag;
    @Bind(R.id.rl_personal)
    RelativeLayout rlPersonal;
    @Bind(R.id.tv_attention)
    CusTextView tvAttention;
    @Bind(R.id.tv_i_attention)
    CusTextView tvIAttention;
    @Bind(R.id.listview)
    ListView listview;
    private UserPreference preference;
    private PersonalAdapter adapter;


    private Response.Listener<JSONObject> toUserCenterListener = new Response.Listener<JSONObject>() {
        public void onResponse(JSONObject response) {
            KLog.e("toUserCenterListener : " + response);
            if (response != null && "success".equals(response.optString("code"))) {
                preference.save(new ToUserCenterModel(response).getResult());
                //EMChatManager.getInstance().login(LocalUserInfo.getInstance().getUserHuanxinUsername(), LocalUserInfo.getInstance().getUserHuanxinPassword(), new EMLoginCallBack());
                onRefresh();
            }
        }
    };
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        public void onErrorResponse(VolleyError error) {
            Helper.showToast("服务器错误，请稍后再试");
        }
    };


    public static void comeBaby(Context context) {
        context.startActivity(new Intent(context, UserCenterActivity.class));
    }

    public UserCenterActivity() {
        preference = UserPreference.getInstance(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        ButterKnife.bind(activity);
        initView();
        IUserCenterServlet.sendToUserCenter(toUserCenterListener, errorListener);
    }

    @Override
    protected int getStatusBarColor() {
        return getResources().getColor(R.color.transparent);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
        refreshRed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == 6) {
            finish();
        }
    }


    public void refreshRed() {
//        if (UserPreference.getInstance(ctx).getAllNewsCount() > 0 || UserPreference.getInstance(ctx).getUnreadMsgCountTotal() > 0) {
//            ivRed.setVisibility(View.VISIBLE);
//        } else {
//            ivRed.setVisibility(View.INVISIBLE);
//        }
    }


    private void initView() {
        onRefresh();
        setupListView();
    }

    private void setupListView() {
        adapter = new PersonalAdapter(this);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }


    private void onRefresh() {
        ImageLoader.getInstance().displayImage(new StringBuilder(BEnvironment.SERVER_IMG_URL)
                        .append(preference.getUserPhoto()).toString(),
                ivHead, AnimateFirstDisplayListener.getOptions(), AnimateFirstDisplayListener.getListener());
        tvName.setText(preference.getUserInfo().userNickname);
        tvTag.setText(preference.getUserInfo().userOneabstract);
        tvAttention.setText("关注 " + preference.getUserInfo().faveCount);
        tvIAttention.setText("被关注 " + preference.getUserInfo().faveMeCount);
    }


    @OnClick({R.id.iv_close, R.id.iv_message, R.id.iv_head, R.id.tv_name, R.id.rl_personal, R.id.tv_attention, R.id.tv_i_attention})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.iv_message:
                break;
            case R.id.iv_head:
                break;
            case R.id.tv_name:
                break;
            case R.id.rl_personal:
                break;
            case R.id.tv_attention:
                break;
            case R.id.tv_i_attention:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i >= 0) {
            switch (adapter.getTitleResId(i)) {
                case R.string.wallet:
                   // WalletActivity.startActivity(this);
                    break;
                case R.string.info_like:
//                    intent.setClass(this, CollectionV2Activity.class);
//                    startActivity(intent);
                    break;
                case R.string.info_coupons :
//                    CouponsActivity.startActivity(this, false);
                    break;
                case R.string.info_setup :
//                    SetupNewActivity.startActivityForResult(this);
                    break;
                case R.string.info_contact_service:
//                    new ContactServicePopupWindow(this).show(findViewById(R.id.main));
                    break;
                case R.string.info_i_will_release:
//                    intent.setClass(this, IWillReleaseActivity.class);
//                    startActivity(intent);
                    break;
                case R.string.info_recommend_friend:
//                    intent.setClass(this, RecommendActivity.class);
//                    startActivity(intent);
                    break;
                case R.string.joined_act:
//                    MyOrderActivity.startActivity(this);
                    break;
                case R.string.attention_act :
//                    intent.setClass(this, AttentionActActivity.class);
//                    startActivity(intent);
                    break;
                default:
            }
        }
    }
}
