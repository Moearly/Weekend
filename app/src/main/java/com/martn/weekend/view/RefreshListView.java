package com.martn.weekend.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.martn.weekend.view.circlerefresh.AnimationView;
import com.martn.weekend.view.circlerefresh.CircleRefreshLayout;
import com.socks.library.KLog;


public class RefreshListView extends ListView {
    private Handler finishRefreshHandler;
    int handlerIndex;
    private ProgressBar loadMorePB;
    private RefreshCallback mCallback;
    private CircleRefreshLayout refreshLayout;

    public RefreshListView(Context context) {
        this(context,null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context,attrs,0);

    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();

    }
    void init() {
        this.handlerIndex = 0;
        this.finishRefreshHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                KLog.e("finishRefreshHandler : " + refreshLayout.getAniStatus().toString());
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setAniStatus(AnimationView.AnimatorStatus.REFRESHING);
                    refreshLayout.finishRefreshing();
                }
            }
        };
    }

    public void setRefreshLayout(CircleRefreshLayout refreshLayout, ProgressBar loadPB, RefreshCallback callback) {
        this.mCallback = callback;
        this.refreshLayout = refreshLayout;
        this.loadMorePB = loadPB;
        refreshLayout.setOnRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
            @Override
            public void refreshing() {
                RefreshListView.this.mCallback.refresh();
            }
            @Override
            public void completeRefresh() {
            }
        });
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0 && view.getLastVisiblePosition() == view.getCount() - 1) {
                    loadMorePB.setVisibility(VISIBLE);
                    mCallback.loadmore();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public synchronized void completeRefresh() {
        this.handlerIndex = 0;
        finishRefreshing();
        finishLoadMore();
    }

    public void finishLoadMore() {
        this.loadMorePB.setVisibility(GONE);
    }

    public void finishRefreshing() {
        this.finishRefreshHandler.sendEmptyMessageDelayed(0, 500);
    }

    public void cancel() {
        this.finishRefreshHandler.removeMessages(0);
    }

    public interface RefreshCallback {
        void loadmore();

        void refresh();
    }
}