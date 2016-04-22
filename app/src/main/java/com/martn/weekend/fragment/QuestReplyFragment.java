package com.martn.weekend.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.martn.weekend.LoginActivity;
import com.martn.weekend.R;
import com.martn.weekend.adapter.QuestReplyAdapter;
import com.martn.weekend.base.BaseFragment;
import com.martn.weekend.db.UserPreference;
import com.martn.weekend.model.MoreQuestModel;
import com.martn.weekend.model.QuestreplyModel;
import com.martn.weekend.request.IQueryCourseReleaseServlet;
import com.martn.weekend.view.InputAnswerPopupWindow;
import com.qmusic.common.Common;
import com.qmusic.uitls.Helper;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.fragment
 * Description: ("请描述功能")
 * Date 2016/4/18 16:46
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class QuestReplyFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener<ListView>, View.OnClickListener {
    private QuestReplyAdapter adapter;
    private int courseid;

    private IQueryCourseReleaseServlet iQueryCourseReleaseV2ServletRequest;
    private InputAnswerPopupWindow inputWindow;
    private List<QuestreplyModel> list;
    private PullToRefreshListView listview;
    private View view;
    private int pageindex = 1;


    private Response.Listener<JSONObject> moreQuestListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            boolean isClean = true;
            if ("success".equals(response.optString("code"))) {
                list = new MoreQuestModel(response).getResult().courseQuestreplyList;
                if (pageindex != 1) {
                    isClean = false;
                }
                adapter.setList(list, isClean);
            } else {
                Helper.showToast(response.optString("description"));
            }
            onRefreshListView();
        }
    };

    private Response.Listener<JSONObject> saveQuestListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if ("success".equals(response.optString("code"))) {
                inputWindow.dismiss();
                pageindex = 1;
                getMoreQuest();
            } else {
                dismissLoading();
            }
            Helper.showToast(response.optString("description"));
        }
    };
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            onRefreshListView();
            Helper.showToast("服务器异常，请稍后再试");
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseid = getArguments().getInt(Common.Key.COURSE_ID);
        iQueryCourseReleaseV2ServletRequest = new IQueryCourseReleaseServlet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quest_reply, container, false);
        initView();
        getMoreQuest();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_answer:
                if (UserPreference.getInstance(activity).isLogin()) {
                    inputWindow = new InputAnswerPopupWindow(getActivity(), this);
                    inputWindow.showAtLocation(view.findViewById(R.id.main), 81, 0, 0);
                    return;
                }
                LoginActivity.startActivityForResult(activity);
                break;
            case R.id.iv_send:
                showLoading();
                IQueryCourseReleaseServlet.sendSaveQuest(courseid, inputWindow.getInput(), saveQuestListener, errorListener);
                break;
            default:
        }
    }

    private void initView() {
        findViewById();
        setupListView();
    }

    private void setupListView() {
        list = new ArrayList();
        adapter = new QuestReplyAdapter(getActivity(), list);
        listview.setAdapter(adapter);
    }

    private void findViewById() {
        listview = (PullToRefreshListView) view.findViewById(R.id.listview);
        listview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listview.setOnRefreshListener(this);
        view.findViewById(R.id.tv_answer).setOnClickListener(this);
    }

    private void getMoreQuest() {
        iQueryCourseReleaseV2ServletRequest.sendMoreQuest(courseid, pageindex, moreQuestListener, errorListener);
    }

    private void onRefreshListView() {
        listview.onRefreshComplete();
        dismissLoading();
    }

    public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        pageindex++;
        getMoreQuest();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }
}