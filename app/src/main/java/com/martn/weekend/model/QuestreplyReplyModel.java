package com.martn.weekend.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("回答")
 * Date 2014/10/5 23:05
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class QuestreplyReplyModel implements Serializable {
    private static final long serialVersionUID = -1081308779340294533L;
    public String replyContent;
    public long replyDatetime;
    public String replyUserNickname;
    public String replyUserPhoto;
    public int replyUserid;

    public void parse(JSONObject json) {
        this.replyUserid = json.optInt("reply_userid");
        this.replyDatetime = json.optLong("reply_datetime", 0);
        this.replyContent = json.optString("reply_content");
        this.replyUserNickname = json.optString("reply_user_nickname");
        this.replyUserPhoto = json.optString("reply_user_photo");
    }

    public String toString() {
        return "QuestreplyReplyModel [replyUserid=" + this.replyUserid + ", replyDatetime=" + this.replyDatetime + ", replyContent=" + this.replyContent + ", replyUserNickname=" + this.replyUserNickname + ", replyUserPhoto=" + this.replyUserPhoto + "]";
    }
}
