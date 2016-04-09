package com.martn.weekend.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.martn.weekend.R;
import com.martn.weekend.adapter.HomeListNewAdapter;
import com.martn.weekend.base.BaseFragment;
import com.martn.weekend.dialogs.CityNewDialog;
import com.martn.weekend.request.IRecommendServlet;
import com.martn.weekend.view.RefreshListView;
import com.martn.weekend.view.circlerefresh.CircleRefreshLayout;
import com.martn.weekend.app.App;
import com.martn.weekend.db.UserPreference;
import com.martn.weekend.result.MainsResult;
import com.qmusic.uitls.AppUtils;
import com.qmusic.uitls.Helper;
import com.qmusic.uitls.StringUtils;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Title: Weekend
 * Package: com.martn.weekend.fragment
 * Description: ("首页列表显示")
 * Date 2016/3/21 17:35
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class HomeListNewFragment extends BaseFragment {

    private final String KEY_COURSE_LIST = "key_course_list_";
    private static final String TAG_ID = "tag_id";
    @Bind(R.id.rf_list)
    RefreshListView listview;
    @Bind(R.id.refresh_layout)
    CircleRefreshLayout refreshLayout;//下拉刷新区域
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.spb_load_more)
    SmoothProgressBar spbLoadMore;
    @Bind(R.id.rl_root)
    RelativeLayout rlRoot;
    private int tagId;
    private int pageIndex = 1;
    int position;

    private CityNewDialog cityDialog;
    private HomeListNewAdapter adapter;
    private MainsResult mainResult;//请求首页--返回数据、


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    App.isRefresh = true;
                    break;
            }
            registMoneyHandler.sendEmptyMessageDelayed(0, 3000);
        }
    };

    private Handler registMoneyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            showRegistMoney();
        }
    };


    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            dismissLoading();
            Helper.showToast("服务器异常，请稍后再试");
            listview.completeRefresh();
        }
    };

    private Response.Listener<JSONObject> mainsListener = new Response.Listener<JSONObject>() {
        public void onResponse(JSONObject response) {
            boolean isClean = true;
            KLog.json(response.toString());
            MainsResult mainResult = new MainsResult(response);
            if (mainResult.success) {
                if (mainResult.tcrList.isEmpty() && pageIndex != 1) {
                    Helper.showToast("没有更多的活动了");
                } else if (pageIndex == 1) {
                    mCache.put(cacheKey(), response.toString());
                }
                if (pageIndex != 1) {
                    isClean = false;
                }
                adapter.setData(mainResult, isClean);
            } else {
                Helper.showToast(mainResult.description);
            }
            listview.completeRefresh();
            dismissLoading();
        }
    };

    public String cacheKey() {
        return new StringBuilder(KEY_COURSE_LIST).append(tagId).append("_").append(UserPreference.getCity()).toString();
    }


    public static HomeListNewFragment newInstance(int position, int id) {
        HomeListNewFragment f = new HomeListNewFragment();
        Bundle b = new Bundle();
        b.putInt(TAG_ID, id);
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }


    public void setTagId(int tagId) {
        this.tagId = tagId;
    }


    public void scrollTop() {
        listview.smoothScrollToPosition(0);
    }

    public void onRefresh() {
        pageIndex = 1;
        scrollTop();
    }

    private void showRegistMoney() {
//        if (Utils.isFirst() && isResumed()) {
//            new RegistMoneyDialog().show(getFragmentManager(), CoinPacketExtension.NAMESPACE);
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagId = getArguments().getInt(TAG_ID);
        position = getArguments().getInt("position", -1);

        mainResult = new MainsResult();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_list, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        showCityDialog();
        return view;

    }

    private void showCityDialog() {
        if (position == 0 && TextUtils.isEmpty(UserPreference.getCity())) {
            cityDialog = new CityNewDialog();
            cityDialog.setCity("北京");
            cityDialog.setHandler(handler);
            cityDialog.show(getFragmentManager(), "");
        }
    }


    private void initData() {
        boolean isClean = true;
        try {
            pageIndex = 1;
            showLoading();
            String jsonS = mCache.getAsString(cacheKey());
            if (StringUtils.isEmpty(jsonS)) {
                IRecommendServlet.mains(pageIndex, AppUtils.getChannelName(getContext()), tagId, mainsListener, errorListener);
                return;
            }
            JSONObject json =  new JSONObject(jsonS);
            dismissLoading();
            mainResult.parse(json);
            MainsResult mainsResult = mainResult;
            if (pageIndex != 1) {
                isClean = false;
            }
            adapter.setData(mainsResult, isClean);
        } catch (JSONException e) {
            e.printStackTrace();
            IRecommendServlet.mains(pageIndex, AppUtils.getChannelName(getContext()), tagId, mainsListener, errorListener);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initView() {
        adapter = new HomeListNewAdapter(getActivity(), mainResult);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int index = adapter.getIndex(position);
                if (index >= 0) {
                    int type = adapter.getItemViewType(position);
                    if (type == 0) {
                        Intent intent = new Intent();
//                        判断广告类型
                        int itemType = adapter.getResult().guanggaoList.get(index).type;
                        KLog.e("itemType ----->" + itemType);
                        if (itemType == 0) {
                            //活动详情界面
                            //ActDetailActivity.startActivity(activity, Integer.parseInt(adapter.getResult().guanggaoList.get(index).address));
                        } else if (itemType == 1) {
                            // intent.setClass(adapter, ConformActivity.class);
//                            intent.putExtra("tagtype", (adapter.getResult().guanggaoList.get(index).tagtype);
//                            intent.putExtra("tagname", (adapter.getResult().guanggaoList.get(index).tagname);
//                            activity.startActivity(intent);
                        } else if (itemType == 2) {
                            //BWebActivity.startActivity(ActListNewFragment.getActivity(), 0, ((GuanggaoModel) ActListNewFragment.actListAdapter.getResult().guanggaoList.get(index)).address, ((GuanggaoModel) ActListNewFragment.actListAdapter.getResult().guanggaoList.get(index)).title, ((GuanggaoModel) ActListNewFragment.actListAdapter.getResult().guanggaoList.get(index)).des);
                        } else if (itemType == 3) {
//                            intent.setClass(ActListNewFragment.getActivity(), SpecialActivity.class);
//                            intent.putExtra("dissertationid", ((GuanggaoModel) ActListNewFragment.actListAdapter.getResult().guanggaoList.get(index)).address);
//                            intent.putExtra(BWebActivity.TITLE, ((GuanggaoModel) ActListNewFragment.actListAdapter.getResult().guanggaoList.get(index)).title);
//                            ActListNewFragment.startActivity(intent);
                        }
                    } else if (type == 1) {
                        KLog.e("course info----->" + adapter.getResult().tcrList.get(index).toString());
//                         ActDetailActivity.startActivity(activity, adapter.getResult().tcrList.get(index).courseid);
                    }
                }
            }
        });

        listview.setRefreshLayout(refreshLayout, spbLoadMore, new RefreshListView.RefreshCallback() {
            @Override
            public void refresh() {
                pageIndex = 1;
                IRecommendServlet.mains(pageIndex, AppUtils.getChannelName(getContext()), tagId, mainsListener, errorListener);
            }

            @Override
            public void loadmore() {
                pageIndex++;
                IRecommendServlet.mains(pageIndex, AppUtils.getChannelName(getContext()), tagId, mainsListener, errorListener);
            }
        });

    }


    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        if (listview != null) {
            listview.cancel();
        }
        super.onDestroyView();
    }
}
