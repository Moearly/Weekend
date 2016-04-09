package com.martn.weekend.result;

import com.martn.weekend.model.GuanggaoModel;
import com.martn.weekend.model.TcrModel;
import com.qmusic.result.BaseResult;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainsResult extends BaseResult {
    public List<GuanggaoModel> guanggaoList;
    public List<TcrModel> tcrList;

    public MainsResult() {
        guanggaoList = new ArrayList<>();
        tcrList = new ArrayList<>();

    }

    public MainsResult(JSONObject json) {
        super.bparse(json);
        parse(json);
    }

    public void parse(JSONObject json) {
        if (json != null) {
            int i;
            super.bparse(json);
            JSONArray guanggaoArr = json.optJSONArray("guanggaoarr");
            if (guanggaoArr != null) {
                this.guanggaoList = new ArrayList();
                for (i = 0; i < guanggaoArr.length(); i++) {
                    this.guanggaoList.add(new GuanggaoModel(guanggaoArr.optJSONObject(i)));
                }
            }
            JSONArray trcArr = json.optJSONArray("tcrarr");
            if (trcArr != null) {
                this.tcrList = new ArrayList();
                for (i = 0; i < trcArr.length(); i++) {
                    this.tcrList.add(new TcrModel(trcArr.optJSONObject(i)));
                }
            }
        }
    }

    public boolean isEmpty() {
        return this.guanggaoList.isEmpty() && this.tcrList.isEmpty();
    }
}