package com.martn.weekend;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.martn.weekend.base.BaseActivity;
import com.martn.weekend.request.IUserServlet;
import com.martn.weekend.utility.FormatUtils;
import com.qmusic.uitls.AppUtils;
import com.qmusic.uitls.Helper;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("登陆")
 * Date 2016/3/18 16:28
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_mobile)
    EditText etMobile;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.tv_login_wx)
    TextView tvLoginWx;
    private Response.ErrorListener errorListener;
    private Response.Listener<JSONObject> loginListener;
    //    private EditText mobileET;
//    private EditText passwordET;
    //private ProgressDialog pglog;
    private Response.Listener<JSONObject> toUserCenterListener;
    private Response.Listener<JSONObject> weixinLoginListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setTitle(getString(R.string.login));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login :
                if (check()) {
                    showLoading();
                    IUserServlet.sendLogin(this, getMobile(), getPassword(), loginListener, errorListener);
                }
                break;
            case R.id.tv_regist /*2131493062*/:
//                RegistAndSetupPwdAndBoundActivity.startActivity(this, 1);
                break;
            case R.id.tv_forget_password /*2131493063*/:
//                RegistAndSetupPwdAndBoundActivity.startActivityForResult(this, 2);
                break;
            case R.id.tv_message_login /*2131493064*/:
//                RegistAndSetupPwdAndBoundActivity.startActivityForResult(this, 4);
                break;
            case R.id.tv_login_wx /*2131493065*/:
                IWXAPI api = WXAPIFactory.createWXAPI(this, WXCommon.APP_ID);
                api.registerApp(WXCommon.APP_ID);
                Req req = new Req();
                req.scope = "snsapi_base,snsapi_userinfo";
                req.state = "sm.xue";
                api.sendReq(req);
            case R.id.topbar_left_textview /*2131493401*/:
                finish();
            default:
        }
    }

    private boolean check() {
        String msg="";
        if (TextUtils.isEmpty(getMobile())) {
            msg = getString(R.string.input_mobile);
        } else if (TextUtils.isEmpty(getPassword())) {
            msg = getString(R.string.input_password);
        } else if (!FormatUtils.isMobile(getMobile())) {
            msg = getString(R.string.wrong_mobile);
        }
        if (TextUtils.isEmpty(msg)) {
            return true;
        } else {
            Helper.showToast(msg);
            return false;
        }
    }


    private String getMobile() {
        return etMobile.getText().toString().trim();
    }

    private String getPassword() {
        return etPassword.getText().toString().trim();
    }


}
