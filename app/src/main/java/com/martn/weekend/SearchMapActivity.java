package com.martn.weekend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.martn.weekend.adapter.SearchMapAdapter;
import com.martn.weekend.base.BaseActivity;
import com.qmusic.uitls.AppUtils;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("搜索地图")
 * Date 2014/10/5 13:32
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class SearchMapActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener, TextWatcher {
    public static final int REQUEST_CODE = 1001;
    public static final String REQUEST_POIINFO = "poi_info";
    private SearchMapAdapter adapter;
    private List<PoiInfo> list;
    private ListView listview;
    private ProgressBar searchPB;

    private PoiSearch poiSearch;
    private EditText searchET;
    private Handler searchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startSearch((String) msg.obj);
        }
    };

    private ImageView searchIV;

    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
        
        @Override
        public void onGetPoiResult(PoiResult result) {
            if (result.getAllPoi() != null) {
                list = result.getAllPoi();
            } else {
                list.clear();
            }
            adapter.setList(list);
            searchPB.setVisibility(View.INVISIBLE);
            searchIV.setVisibility(View.VISIBLE);
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult result) {
            KLog.e("PoiDetailResult : " + result.toString());
        }
    };


    public static void comeBadyForResult(Context context, String searchInfo) {
        Intent intent = new Intent(context, SearchMapActivity.class);
        intent.putExtra("searchInfo", searchInfo);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
    }

    public static void comeBadyForResult(Context context) {
        ((Activity) context).startActivityForResult(new Intent(context, SearchMapActivity.class), REQUEST_CODE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.poiSearch.destroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
            case R.id.iv_del:
                searchET.setText("");
            default:
        }
    }

    private void initView() {
        searchPB = (ProgressBar) findViewById(R.id.pb_search);
        searchIV = (ImageView) findViewById(R.id.iv_search);
        initListView();
        initSearchET();
        initPoiSearch();

    }

    private void initPoiSearch() {
       poiSearch = PoiSearch.newInstance();
       poiSearch.setOnGetPoiSearchResultListener(this.poiListener);

    }

    private void initSearchET() {
        searchET = (EditText) findViewById(R.id.et_search);
        searchET.addTextChangedListener(this);
        searchET.setOnEditorActionListener(this);

    }

    private void initListView() {
        list = new ArrayList();
        listview = (ListView) findViewById(R.id.listview);
        adapter = new SearchMapAdapter(this, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(SearchMapActivity.REQUEST_POIINFO, list.get(position));
                intent.putExtra("searchName", list.get(position).name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }


    private void startSearch(String keyword) {
        searchPB.setVisibility(View.VISIBLE);
        searchIV.setVisibility(View.INVISIBLE);
        poiSearch.searchInCity(new PoiCitySearchOption().city(AppUtils.getCity()).keyword(keyword).pageNum(20));
    }

    private String getSearchInfo() {
        return searchET.getText().toString();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId != 3) {
            return false;
        }
        startSearch(getSearchInfo());
        return true;

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        searchHandler.removeMessages(0);
        if (!TextUtils.isEmpty(s.toString())) {
            Message msg = Message.obtain();
            msg.obj = s.toString();
            searchHandler.sendMessageDelayed(msg, 1000);
        }

    }
}
