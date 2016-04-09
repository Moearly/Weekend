package com.martn.weekend;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.martn.weekend.adapter.TagNewAdapter;
import com.martn.weekend.base.BaseActivity;
import com.martn.weekend.request.IRecommendServlet;
import com.martn.weekend.result.SubjectResult;
import com.martn.weekend.utility.cache.ACache;
import com.martn.weekend.view.CusTextView;
import com.martn.weekend.view.NoScrollGridView;
import com.qmusic.base.BaseApplication;
import com.qmusic.localplugin.BaiduMapPlug;
import com.qmusic.localplugin.PluginManager;
import com.qmusic.uitls.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("分类")
 * Date 2016/3/31 17:11
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class ClassifyActivity extends BaseActivity implements AdapterView.OnItemClickListener, TextView.OnEditorActionListener, TextWatcher {

    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.input_edittext)
    EditText inputEdittext;
    @Bind(R.id.iv_message)
    ImageView ivMessage;
    @Bind(R.id.unread_imageview)
    CircleImageView unreadImageview;
    @Bind(R.id.week_textview)
    CusTextView weekTextview;
    @Bind(R.id.near_textview)
    CusTextView nearTextview;
    @Bind(R.id.hot_tag_textview)
    CusTextView hotTagTextview;
    @Bind(R.id.hot_tag_gridview)
    NoScrollGridView hotTagGridview;
    @Bind(R.id.classify_tag_textview)
    CusTextView classifyTagTextview;
    @Bind(R.id.classify_tag_gridview)
    NoScrollGridView classifyTagGridview;

    private BaiduMapPlug plug;
    private SubjectResult hotTagResult;
    private TagNewAdapter hotTagAdapter;
    private SubjectResult classifyTagResult;
    private TagNewAdapter classTagAdapter;
    private ACache aCache;

    private static String CACHE_KEY_HOT = "key_hot_tag_info";
    private static String CACHE_KEY_CLASS = "key_classify_tag_info";


    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Helper.showToast("服务器异常，请稍后再试");
            dismissLoading();

        }
    };
    private Response.Listener<JSONObject> findSubjectForShowListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            classifyTagResult = new SubjectResult(response);
            if (!(!classifyTagResult.success || classifyTagResult.tagList == null || classifyTagResult.tagList.isEmpty())) {
                aCache.put(CACHE_KEY_CLASS, response.toString());
                initClassifyTag();
                classTagAdapter.setList(classifyTagResult.tagList);
            }
            dismissLoading();

        }
    };

    private Response.Listener<JSONObject> hotSubjectForShowListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            hotTagResult = new SubjectResult(response);
            if (!(!hotTagResult.success ||hotTagResult.tagList == null || hotTagResult.tagList.isEmpty())) {
                aCache.put(CACHE_KEY_HOT, response.toString());
                initHotTag();
                hotTagAdapter.setList(hotTagResult.tagList);
            }
            dismissLoading();
        }
    };
    private Handler searchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SearchClassActivity.comeBady(ClassifyActivity.this, getTagName(), 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        ButterKnife.bind(this);
        plug = (BaiduMapPlug) PluginManager.getPlugin(BaiduMapPlug.class.getSimpleName());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        aCache = ACache.get(this);
        initView();
        showLoading();//网络请求数据
        getHotTagInfo();
        getClassifyTagInfo();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (plug != null) {
                plug.init(BaseApplication.context());
                plug.getmLocationClient().start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getClassifyTagInfo() {
        try {
            String classifyTagInfo = aCache.getAsString(CACHE_KEY_CLASS);
            if (!TextUtils.isEmpty(classifyTagInfo)) {
                classifyTagResult = new SubjectResult(new JSONObject(classifyTagInfo));
                classTagAdapter.setList(classifyTagResult.tagList);
            }
            IRecommendServlet.findSubjectForShow(findSubjectForShowListener, errorListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getHotTagInfo() {
        try {
            String hotTagInfo = aCache.getAsString(CACHE_KEY_HOT);
            if (!TextUtils.isEmpty(hotTagInfo)) {
                hotTagResult = new SubjectResult(new JSONObject(hotTagInfo));
                hotTagAdapter.setList(hotTagResult.tagList);
            }
            IRecommendServlet.findHotSubjectForShow(hotSubjectForShowListener, errorListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        inputEdittext.addTextChangedListener(this);
        inputEdittext.setOnEditorActionListener(this);

        initHotTag();
        initClassifyTag();

    }

    private void initClassifyTag() {
        if (classifyTagResult == null || classTagAdapter == null) {
            classifyTagResult = new SubjectResult();
            classTagAdapter = new TagNewAdapter(this, classifyTagResult.tagList);
            classifyTagGridview.setAdapter(classTagAdapter);
            classifyTagGridview.setOnItemClickListener(this);
            return;
        }
        classTagAdapter.notifyDataSetChanged();
    }

    private synchronized String getTagName() {
        return inputEdittext.getText().toString();
    }


    private void initHotTag() {
        if (hotTagResult == null || hotTagAdapter == null) {
            hotTagResult = new SubjectResult();
            hotTagAdapter = new TagNewAdapter(this, hotTagResult.tagList);
            hotTagGridview.setAdapter(hotTagAdapter);
            hotTagGridview.setOnItemClickListener(this);
            return;
        }
        hotTagAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_close, R.id.iv_message, R.id.week_textview, R.id.near_textview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_to_bottom);//17432576
                break;
            case R.id.iv_message:
                MessageActivity.comeBady(this);
                overridePendingTransition(R.anim.slide_in_from_bottom, android.R.anim.fade_out);//17432577
                break;
            case R.id.week_textview:
                break;
            case R.id.near_textview:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
            searchHandler.sendEmptyMessageDelayed(0, 500);
        }

    }

}
