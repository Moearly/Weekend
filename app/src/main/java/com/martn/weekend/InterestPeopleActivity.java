package com.martn.weekend;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.martn.weekend.adapter.InterestPeopleAdapter;
import com.martn.weekend.base.BaseActivity;
import com.martn.weekend.db.UserPreference;
import com.martn.weekend.request.IQueryCourseReleaseServlet;
import com.martn.weekend.result.UserForCoureFaveResult;
import com.qmusic.common.Common.Key;
import com.qmusic.uitls.Helper;
import com.socks.library.KLog;

import java.util.List;
import org.json.JSONObject;


/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("感兴趣的人")
 * Date 2016/4/18 15:35
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class InterestPeopleActivity extends BaseActivity implements OnItemClickListener, OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {
    InterestPeopleAdapter adapter;
    private int courseid;
    IQueryCourseReleaseServlet iQueryCourseReleaseV2ServletRequest = new IQueryCourseReleaseServlet();
    Intent intent = new Intent();
    PullToRefreshListView listview;
    ProgressDialog pDialog;
    private int pageindex = 1;
    UserForCoureFaveResult result = new UserForCoureFaveResult();

    private ErrorListener errorListener = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            dissmissPDialog();
            Helper.showToast("服务器异常，请稍后再试");
            listview.onRefreshComplete();
        }
    };
    private Listener<JSONObject> findUserForCoureFaveListener = new Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            boolean z = true;
            KLog.json(response.toString());
            if ("success".equals(response.optString("code"))) {
                UserForCoureFaveResult tResult = new UserForCoureFaveResult();
                tResult.parse(response);
                KLog.e("-----> size : " + result.courseUserfaveArr.size());
                InterestPeopleAdapter interestPeopleAdapter = adapter;
                List list = tResult.courseUserfaveArr;
                if (pageindex != 1) {
                    z = false;
                }
                interestPeopleAdapter.setList(list, z);
            } else if ("error_2".equals(response.optString("code"))) {
                intent.setClass(ctx, UserEditActivity.class);//用户编辑
                startActivityForResult(intent, 1);
                Helper.showToast(response.optString("description"));
            } else {
                Helper.showToast(response.optString("description"));
            }
            listview.onRefreshComplete();
            dissmissPDialog();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_people);
        courseid = getIntent().getIntExtra(Key.COURSE_ID, 0);
        pDialog = new ProgressDialog(this);
        initView();
        if (UserPreference.getInstance(ctx).isLogin()) {
            showPDialog();
            getInterestInfo();
            return;
        }
        LoginActivity.startActivityForResult(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topbar_left_textview:
                finish();
            default:
        }
    }

    private void initView() {
        findViewById();
        setupListView();
    }

    private void setupListView() {
        adapter = new InterestPeopleAdapter(this, result.courseUserfaveArr);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }

    private void findViewById() {
        listview = (PullToRefreshListView) findViewById(R.id.listview);
        listview.setOnRefreshListener(this);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setOnItemClickListener(this);
        ((TextView) findViewById(R.id.topbar_mid_textview)).setText("感兴趣的人");
    }

    private void getInterestInfo() {
        iQueryCourseReleaseV2ServletRequest.findUserForCoureFave(courseid, pageindex, findUserForCoureFaveListener, errorListener);
    }

    private void showPDialog() {
        if (pDialog != null && !pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void dissmissPDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        pageindex = 1;
        getInterestInfo();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        pageindex++;
        getInterestInfo();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        int index = position - 1;
        if (index >= 0) {
            OtherPersonActivity.comeBady(this, result.courseUserfaveArr.get(index).id);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 2) {
                showPDialog();
                getInterestInfo();
                return;
            }
            finish();
        } else if (requestCode != 1) {
        } else {
            if (requestCode == 2) {
                showPDialog();
                getInterestInfo();
                return;
            }
            finish();
        }
    }

    public static void startActivity(Context context, int courseid) {
        Intent intent = new Intent(context, InterestPeopleActivity.class);
        intent.putExtra(Key.COURSE_ID, courseid);
        context.startActivity(intent);
    }

}