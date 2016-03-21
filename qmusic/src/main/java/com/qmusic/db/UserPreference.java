package com.qmusic.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.qmusic.base.BaseApplication;
import com.qmusic.result.ToUserCenterResult;
import com.socks.library.KLog;


/**
 * Title: Juyixia
 * Package: com.lefu.juyixia.one.database
 * Description: ("保存用户相关信息--的spf")
 * Date 2015/7/30 18:34
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class UserPreference {

    public static final String KEY_USER_ID = "key_user_id";
    public static final String KEY_USER_MOBILE = "key_user_mobile";
    public static final String KEY_USER_PASSWORD = "key_user_password";
    public static final String KEY_USER_CITY = "key_user_city";

    public static final String KEY_USER_TOKEN = "key_user_token";

    public static final String KEY_USER_NICKNAME = "key_user_nickname";
    public static final String KEY_USER_TYPE = "key_user_type";
    public static final String KEY_USER_PHOTO = "key_user_photo";
    public static final String KEY_USER_ONEABSTRACT = "key_user_oneabstract";
    public static final String KEY_USER_NOTLOOKINFO_COUNT = "key_user_notlookinfo_count";
    public static final String KEY_USER_RECOMMENDNUM = "key_user_recommendnum";
    public static final String KEY_USER_HUANXIN_USERNAME = "key_user_huanxin_username";
    public static final String KEY_USER_HUANXIN_PASSWORD = "key_user_huanxin_password";
    public static final String KEY_USER_CONSTELLATION = "key_user_constellation";
    public static final String KEY_USER_AGE = "key_user_age";
    public static final String KEY_USER_SEX = "key_user_sex";
    public static final String KEY_USER_BIRTHDAY = "key_user_birthday";
    public static final String KEY_USER_FAVE_COUNT = "key_user_fave_count";
    public static final String KEY_USER_FAVEME_COUNT = "key_user_faveme_count";


    private ToUserCenterResult userInfo;
    private String userId;
    private String mobile;
    private String password;
    private String userCity;
    private String userToken;

    static final String PREFS_NAME = UserPreference.class.getSimpleName();
    private static UserPreference userPrefs;
    private SharedPreferences setting;

    private UserPreference(Context context) {
        setting = context.getSharedPreferences(PREFS_NAME, 0);
    }

    public static UserPreference getInstance(Context context) {
        if (userPrefs == null)
            userPrefs = new UserPreference(context);
        return userPrefs;
    }


    public boolean isLogin() {
        if (TextUtils.isEmpty(getUserId()) && TextUtils.isEmpty(getUserToken())) {
            return true;
        }
        return false;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserId() {
        if (TextUtils.isEmpty(userId)) {
            userId = getString(KEY_USER_ID);
        }
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ToUserCenterResult getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(ToUserCenterResult userInfo) {
        this.userInfo = userInfo;
    }

    public String getUserCity() {
        if (TextUtils.isEmpty(userCity)) {
            userCity = getString(KEY_USER_CITY);
        }
        return userCity;
    }

    public void setUserCity(String userCity) {
        putString(KEY_USER_CITY,userCity);
        this.userCity = userCity;
    }

    public void loadLocalUserInfo(String userId) {
        if (!TextUtils.isEmpty(userId)) {
            if (userInfo == null) {
                userInfo = new ToUserCenterResult();

            }
            mobile = getString(new StringBuilder(String.valueOf(userId)).append(KEY_USER_MOBILE).toString(), "");
            password = getString(new StringBuilder(String.valueOf(userId)).append(KEY_USER_PASSWORD).toString(), "");

            userInfo.userNickname = getString(new StringBuilder(String.valueOf(userId)).append(KEY_USER_NICKNAME).toString(), "");
            userInfo.userType = getString(new StringBuilder(String.valueOf(userId)).append(KEY_USER_TYPE).toString(), "");
            userInfo.userPhoto = getString(new StringBuilder(String.valueOf(userId)).append(KEY_USER_PHOTO).toString(), "");
            userInfo.userOneabstract = getString(new StringBuilder(String.valueOf(userId)).append(KEY_USER_ONEABSTRACT).toString(), "");
            userInfo.userNotlookinfoCount = getString(new StringBuilder(String.valueOf(userId)).append(KEY_USER_NOTLOOKINFO_COUNT).toString(), "");
            userInfo.userRecommendnum = getString(new StringBuilder(String.valueOf(userId)).append(KEY_USER_RECOMMENDNUM).toString(), "");
            userInfo.userHuanxinUsername = getString(new StringBuilder(String.valueOf(userId)).append(KEY_USER_HUANXIN_USERNAME).toString(), "");
            userInfo.userHuanxinPassword = getString(new StringBuilder(String.valueOf(userId)).append(KEY_USER_HUANXIN_PASSWORD).toString(), "");
            userCity = getString(new StringBuilder(String.valueOf(userId)).append(KEY_USER_CITY).toString(), "");

            userInfo.userConstellation = getString(new StringBuilder(String.valueOf(userId)).append(KEY_USER_CONSTELLATION).toString(), "");
            userInfo.userSex = getInt(new StringBuilder(String.valueOf(userId)).append(KEY_USER_SEX).toString(), 0);
            userInfo.userAge = getString(new StringBuilder(String.valueOf(userId)).append(KEY_USER_AGE).toString(), "");
            userInfo.userBirthday = getString(new StringBuilder(String.valueOf(userId)).append(KEY_USER_BIRTHDAY).toString(), "");
        }

    }

    public void save(ToUserCenterResult result, String password, String mobile) {
        if (!TextUtils.isEmpty(result.userId)) {
            userId = result.userId;
            this.mobile = mobile;
            this.password = password;
            userInfo = result;
            saveLocaltion(result);
        }
    }

    private void saveLocaltion(ToUserCenterResult result) {
       putString(KEY_USER_ID,result.userId);

       putString(userId+KEY_USER_MOBILE,mobile);
       putString(userId+KEY_USER_PASSWORD,password);
       putString(userId+KEY_USER_NICKNAME,result.userNickname);
       putString(userId+KEY_USER_TYPE,result.userType);
       putString(userId+KEY_USER_PHOTO,result.userPhoto);
       putString(userId+KEY_USER_ONEABSTRACT,result.userOneabstract);
       putString(userId+KEY_USER_NOTLOOKINFO_COUNT,result.userNotlookinfoCount);
       putString(userId+KEY_USER_RECOMMENDNUM,result.userRecommendnum);
       putString(userId+KEY_USER_CITY,userCity);
       putString(userId+KEY_USER_HUANXIN_USERNAME,result.userHuanxinUsername);
       putString(userId+KEY_USER_HUANXIN_PASSWORD,result.userHuanxinPassword);
       putString(userId+KEY_USER_CONSTELLATION,result.userConstellation);
       putString(userId+KEY_USER_AGE,result.userAge);
       putInt(userId+KEY_USER_SEX,result.userSex);
       putString(userId+KEY_USER_BIRTHDAY,result.userBirthday);

       putInt(userId+KEY_USER_FAVE_COUNT,result.faveCount);
       putInt(userId+KEY_USER_FAVEME_COUNT,result.faveMeCount);

    }

    public void saveToken(String s) {
        userToken = s;
        putString(KEY_USER_TOKEN,s);
        KLog.e("tokenSave:"+s);
    }

    public void clean() {
        if (!TextUtils.isEmpty(userId)) {
            remove(this.userId + KEY_USER_MOBILE);
            remove(this.userId + KEY_USER_PASSWORD);
            remove(this.userId + KEY_USER_NICKNAME);
            remove(this.userId + KEY_USER_TYPE);
            remove(this.userId + KEY_USER_PHOTO);
            remove(this.userId + KEY_USER_ONEABSTRACT);
            remove(this.userId + KEY_USER_NOTLOOKINFO_COUNT);
            remove(this.userId + KEY_USER_RECOMMENDNUM);
            remove(this.userId + KEY_USER_CITY);
            remove(this.userId + KEY_USER_HUANXIN_PASSWORD);
            remove(this.userId + KEY_USER_HUANXIN_USERNAME);
            remove(this.userId + KEY_USER_CONSTELLATION);
            remove(this.userId + KEY_USER_AGE);
            remove(this.userId + KEY_USER_SEX);

            remove(this.userId + KEY_USER_BIRTHDAY);
            remove(this.userId + KEY_USER_FAVE_COUNT);
            remove(this.userId + KEY_USER_FAVEME_COUNT);

            remove(KEY_USER_ID);
            remove(KEY_USER_TOKEN);


        }
        this.userId = "";
        this.mobile = "";
        this.userCity = "";
        this.userToken = "";
        this.password = "";
        this.userInfo = new ToUserCenterResult();
    }



    public static String getUserToken() {
        return getInstance(BaseApplication.context()).getString(KEY_USER_TOKEN,"");
    }

    public static String getCity() {
        String city = getInstance(BaseApplication.context()).getString(KEY_USER_CITY,"北京");
        String pinyin ;
        if (city.equals("北京")) {
            pinyin = "beijing";
        } else if (city.equals("上海")) {
            pinyin = "shanghai";
        } else if (city.equals("广州")) {
            pinyin = "guangzhou";
        } else if (city.equals("深圳")) {
            pinyin = "shenzhen";
        } else {
            pinyin = city;
        }
        return "city=" + pinyin;
    }

    /**
     * sp--编辑
     * @return
     */
    public SharedPreferences.Editor getEditor() {
        return setting.edit();
    }

    /*******************get*******************/
    public boolean getBoolean(String key) {
        return setting.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defalut) {
        return setting.getBoolean(key, defalut);
    }

    public String getString(String key) {
        return setting.getString(key, null);
    }

    public String getString(String key1, String def) {
        return setting.getString(key1, def);
    }

    public Float getFloat(String key) {
        return Float.valueOf(setting.getFloat(key, 0.0F));
    }

    public Float getFloat(String key, float def) {
        return Float.valueOf(setting.getFloat(key, def));
    }

    public int getInt(String key) {
        return setting.getInt(key, 0);
    }

    public int getInt(String key, int def) {
        return setting.getInt(key, def);
    }


    public boolean putBoolean(String key, boolean def) {
        return getEditor().putBoolean(key, def).commit();
    }

    public boolean putFloat(String key, float def) {
        return getEditor().putFloat(key, def).commit();
    }

    public boolean putInt(String key, int def) {
        return getEditor().putInt(key, def).commit();
    }

    public boolean putString(String key1, String def) {
        return getEditor().putString(key1, def).commit();
    }

    public boolean remove(String key) {
        return getEditor().remove(key).commit();
    }



}
