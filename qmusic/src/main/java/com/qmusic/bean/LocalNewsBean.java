package com.qmusic.bean;

import org.json.JSONObject;

public class LocalNewsBean {
    private static LocalNewsBean instance;
    private int allNewsCount;
    private int dynamicCount;
    private String dynamicDesc;
    private int noticeCount;
    private String noticeDesc;

    public static LocalNewsBean getInstance() {
        if (instance == null) {
            synchronized (LocalNewsBean.class) {
                if (instance == null) {
                    instance = new LocalNewsBean();
                }
            }
        }
        return instance;
    }

    public void parse(JSONObject json) {
        this.dynamicCount = json.optInt("dynamic_count", 0);
        this.noticeCount = json.optInt("notice_count", 0);
        this.allNewsCount = json.optInt("all_news_count", 0);
        this.dynamicDesc = json.optString("dynamic_desc", "暂无新的动态");
        this.noticeDesc = json.optString("notice_desc", "暂无新的通知");
    }

    public void clean() {
        this.dynamicCount = 0;
        this.noticeCount = 0;
        this.allNewsCount = 0;
        this.dynamicDesc = "";
        this.noticeDesc = "";
    }

    public int getDynamicCount() {
        return this.dynamicCount;
    }

    public void setDynamicCount(int dynamicCount) {
        this.dynamicCount = dynamicCount;
    }

    public int getNoticeCount() {
        return this.noticeCount;
    }

    public void setNoticeCount(int noticeCount) {
        this.noticeCount = noticeCount;
    }

    public int getAllNewsCount() {
        return this.allNewsCount;
    }

//    public int getUnreadMsgCountTotal() {
//        int chatroomUnreadMsgCount = 0;
//        int unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
//        for (EMConversation conversation : EMChatManager.getInstance().getAllConversations().values()) {
//            if (conversation.getType() == EMConversationType.ChatRoom) {
//                chatroomUnreadMsgCount += conversation.getUnreadMsgCount();
//            }
//        }
//        return unreadMsgCountTotal - chatroomUnreadMsgCount;
//    }

    public void setAllNewsCount(int allNewsCount) {
        this.allNewsCount = allNewsCount;
    }

    public String getDynamicDesc() {
        return this.dynamicDesc;
    }

    public void setDynamicDesc(String dynamicDesc) {
        this.dynamicDesc = dynamicDesc;
    }

    public String getNoticeDesc() {
        return this.noticeDesc;
    }

    public void setNoticeDesc(String noticeDesc) {
        this.noticeDesc = noticeDesc;
    }
}