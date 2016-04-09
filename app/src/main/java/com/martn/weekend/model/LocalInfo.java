//package com.martn.weekend.model;
//
//import com.qmusic.common.Common;
//import com.qmusic.uitls.SharedPreferencesUtil;
//
//public class LocalInfo {
//    private static LocalInfo instance;
//    private final String KEY_CLASSIFY_TAG_INFO;
//    private final String KEY_CLASSIFY_TAG_TIME;
//    private final String KEY_COURSE_DETAIL;
//    private final String KEY_COURSE_LIST;
//    private final String KEY_HOT_TAG_INFO;
//    private final String KEY_HOT_TAG_TIME;
//
//    public LocalInfo() {
//        this.KEY_COURSE_LIST = Common.Key.KEY_COURSE_LIST;
//        this.KEY_COURSE_DETAIL = Common.Key.KEY_COURSE_DETAIL;
//        this.KEY_CLASSIFY_TAG_TIME = "key_classify_tag_time";
//        this.KEY_CLASSIFY_TAG_INFO = "key_classify_tag_info";
//        this.KEY_HOT_TAG_TIME = "key_hot_tag_time";
//        this.KEY_HOT_TAG_INFO = "key_hot_tag_info";
//    }
//
//    public static LocalInfo getInstance() {
//        if (instance == null) {
//            synchronized (LocalInfo.class) {
//                if (instance == null) {
//                    instance = new LocalInfo();
//                }
//            }
//        }
//        return instance;
//    }
//
//    public String getCourseListInfo(int tagId) {
//        return SPUtil.getString("key_course_list_" + tagId + "_" + SharedPreferencesUtil.getStringSharedPreference(null, Common.Key.CITY, CoinPacketExtension.NAMESPACE));
//    }
//
//    public void setCourseListInfo(int tagId, String value) {
//        SPUtil.putString("key_course_list_" + tagId + "_" + SharedPreferencesUtil.getStringSharedPreference(null, Common.Key.CITY, CoinPacketExtension.NAMESPACE), value);
//    }
//
//    public String getCourseDetailInfo(int courseid) {
//        return SPUtil.getString("key_course_detail_" + courseid);
//    }
//
//    public void setCourseDetailInfo(int courseid, String value) {
//        SPUtil.putString("key_course_detail_" + courseid, value);
//    }
//
//    public String getClassifyTagInfo() {
//        return SPUtil.getString("key_classify_tag_info");
//    }
//
//    public void setClassifyTag(long time, String value) {
//        SPUtil.putLong("key_classify_tag_time", time);
//        SPUtil.putString("key_classify_tag_info", value);
//    }
//
//    public String getHotTagInfo() {
//        return SPUtil.getString("key_hot_tag_info");
//    }
//
//    public void setHotTag(long time, String value) {
//        SPUtil.putLong("key_hot_tag_time", time);
//        SPUtil.putString("key_hot_tag_info", value);
//    }
//}