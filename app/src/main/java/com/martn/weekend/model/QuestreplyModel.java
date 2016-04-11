package com.martn.weekend.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("问题回答")
 * Date 2014/10/5 23:02
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class QuestreplyModel implements Serializable {
    private static final long serialVersionUID = 3350769404828373893L;
    public QuestreplyQuestModel quest;
    public QuestreplyReplyModel reply;

    public QuestreplyModel(JSONObject json) {
        parse(json);
    }

    public void parse(JSONObject json) {
        this.quest = new QuestreplyQuestModel();
        this.quest.parse(json.optJSONObject("questreply_quest"));
        JSONObject replyJson = json.optJSONObject("questreply_reply");
        this.reply = new QuestreplyReplyModel();
        this.reply.parse(replyJson);
    }

    public String toString() {
        return "QuestreplyModel [quest=" + this.quest + ", reply=" + this.reply + "]";
    }
}
