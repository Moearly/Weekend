package com.martn.weekend;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.martn.weekend.adapter.CourseListAdapter;
import com.martn.weekend.base.BaseActivity;
import com.martn.weekend.model.SeachByTagModel;
import com.martn.weekend.model.TcrModel;
import com.martn.weekend.request.IRecommendServlet;
import com.martn.weekend.result.SeachByTagResult;
import com.martn.weekend.view.CusTextView;
import com.qmusic.uitls.Helper;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("活动列表")
 * Date 2014/10/5 14:40
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class CourseListActivity extends BaseActivity implements AdapterView.OnItemClickListener
        , PullToRefreshBase.OnRefreshListener2<ListView> {

    public static final int TYPE_ATTENTION = 2;
    public static final int TYPE_JOINED = 1;
    public static final int TYPE_OVER = 0;

    IRecommendServlet iRecommendServletRequest = new IRecommendServlet();

    CourseListAdapter adapter;


    int pageIndex = 1;
    @Bind(R.id.topbar_mid_textview)
    CusTextView topbarMidTextview;
    @Bind(R.id.listview)
    PullToRefreshListView listview;

    private SeachByTagResult result = new SeachByTagResult();
    private String title;
    private int type = -1;
    private int userid;

    private ErrorListener errorListener = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            listview.onRefreshComplete();
            Helper.showToast("服务器异常，请稍后再试");
        }
    };

    private Listener<JSONObject> responseListener = new Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            boolean z = true;
            KLog.e("responseListener : " + response.toString());
            if (response == null || !"success".equals(response.optString("code"))) {
                Helper.showToast(response.optString("description"));
            } else if (type == 0) {
                result = new SeachByTagModel(response).getResult();
                List list = result.courseUserList;
                if (pageIndex != 1) {
                    z = false;
                }
                adapter.setList(list, z);
            } else if (type == 1) {
                List list = new ArrayList();
                JSONArray jsonArray = response.optJSONArray("teacher_takecourse_arr");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TcrModel tcr = new TcrModel();
                        tcr.parse(jsonArray.optJSONObject(i));
                        list.add(tcr);
                    }
                    if (!list.isEmpty()) {
                        if (pageIndex != 1) {
                            z = false;
                        }
                        adapter.setList(list, z);
                    }
                }
            } else if (type == CourseListActivity.TYPE_ATTENTION) {
                List list = new ArrayList();
                JSONArray jsonArray = response.optJSONArray("collect_course_arr");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TcrModel tcr = new TcrModel();
                        tcr.parse(jsonArray.optJSONObject(i));
                        list.add(tcr);
                    }
                    if (!list.isEmpty()) {
                        if (pageIndex != 1) {
                            z = false;
                        }
                        adapter.setList(list, z);
                    }
                }
            }
            listview.onRefreshComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_list);
        ButterKnife.bind(this);
        title = getIntent().getExtras().getString("title", "");
        userid = getIntent().getExtras().getInt("userid", 0);
        type = getIntent().getExtras().getInt("type", -1);
        initView();
        getCourseInfo();

    }


    private void getCourseInfo() {
        if (type == TYPE_OVER) {
            iRecommendServletRequest.sendSeachByTag(userid, pageIndex, responseListener, errorListener);
        } else if (type == TYPE_JOINED) {
            iRecommendServletRequest.findTakePartCourseByUserId(userid, pageIndex, responseListener, errorListener);
        } else if (type == TYPE_ATTENTION) {
            iRecommendServletRequest.findCollectCourseByUserId(userid, pageIndex, responseListener, errorListener);
        }
    }


    private void initView() {
        topbarMidTextview.setText(title);
        adapter = new CourseListAdapter(this, result.courseUserList);
        listview.setOnItemClickListener(this);
        listview.setOnRefreshListener(this);
        listview.setAdapter(adapter);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        if (type == TYPE_ATTENTION) {
            listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int index = position - 1;
        if (index >= 0) {
            ActDetailActivity.comeBady(this, this.adapter.getList().get(index).courseid);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        this.pageIndex = 1;
        getCourseInfo();

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        this.pageIndex++;
        getCourseInfo();

    }

    @OnClick(R.id.topbar_left_textview)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topbar_left_textview:
                finish();
            default:
        }

    }
}
