package com.qmusic.result;

import org.json.JSONObject;

public class BaseResult {
    public String code;
    public String description;
    public boolean success;

    public BaseResult() {
        this.success = false;
    }

    public void bparse(JSONObject json) {
        this.code = json.optString("code");
        this.description = json.optString("description", "服务器异常，请稍后再试");
        if (this.code.equalsIgnoreCase("success")) {
            this.success = true;
        } else {
            this.success = false;
        }
    }
}