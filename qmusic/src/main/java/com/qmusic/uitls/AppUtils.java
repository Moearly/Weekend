package com.qmusic.uitls;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Display;
import android.view.WindowManager;


import com.github.johnpersano.supertoasts.SuperToast;
import com.qmusic.base.BaseApplication;

import java.util.List;
import java.util.Random;

/**
 * Title: EnjoyTime
 * Package: com.martn.enjoytime.utility
 * Description: ("app的工具类")
 * Date 2016/3/1 17:19
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class AppUtils {
    /**
     * bmob--Application ID
     */
    public static String APPLICATION_ID="921464a0f919613e741e1981e187bce3";


    /**
     * bugly--id
     */
    public static String BUGLY_APP_ID = "900021188";//9eqAoa7XdDDEXrrJ
    public static SuperToast.Animations TOAST_ANIMATION = SuperToast.Animations.FLYIN;

    public static RelativeSizeSpan relativeSizeSpan;
    public static ForegroundColorSpan redForegroundSpan;
    public static ForegroundColorSpan greenForegroundSpan;
    public static ForegroundColorSpan whiteForegroundSpan;

//    public static Typeface typefaceLatoRegular = null;
//    public static Typeface typefaceLatoHairline = null;
//    public static Typeface typefaceLatoLight = null;
//    public static Typeface typefaceZiTiRegular = null;
    private static Random random;

    private static String lastColor0, lastColor1, lastColor2;

    public static int THEME_COLOR;

    public static String[] addColors;


    public static final int ADD_COLOR_COUNT = 3;
    private static int screenHeight;
    private static int screenWidth;
    public static final int SCREEN_HEIGHT = 101;
    public static final int SCREEN_WIDTH = 100;


    public static void init(Context context) {
//
//        typefaceZiTiRegular = Typeface.createFromAsset(
//                context.getAssets(), "fonts/ziti.otf");
//        typefaceLatoRegular = Typeface.createFromAsset(
//                context.getAssets(), "fonts/Lato-Regular.ttf");
//        typefaceLatoHairline = Typeface.createFromAsset(
//                context.getAssets(), "fonts/Lato-Hairline.ttf");
//        typefaceLatoLight = Typeface.createFromAsset(
//                context.getAssets(), "fonts/LatoLatin-Light.ttf");
//        relativeSizeSpan = new RelativeSizeSpan(2f);
//        redForegroundSpan = new ForegroundColorSpan(Color.parseColor("#ff5252"));
//        greenForegroundSpan = new ForegroundColorSpan(Color.parseColor("#4ca550"));
//        whiteForegroundSpan = new ForegroundColorSpan(Color.parseColor("#ffffff"));
//
//        lastColor0 = "";
//        lastColor1 = "";
//        lastColor2 = "";
//
//        random = new Random();
//
//        THEME_COLOR = ContextCompat.getColor(BaseApplication.context(), R.color.theme_main_color);
//
//        addColors = new String[ADD_COLOR_COUNT];
//        addColors[0] = "bg_green";
//        addColors[1] = "bg_purple";
//        addColors[2] = "bg_yellow";

    }

    public static Typeface getTypefaceZiTi() {
        return Typeface.createFromAsset(
                BaseApplication.context().getAssets(), "fonts/ziti.otf");
    }
    public static SpannableString getSpanString(String info, int start, int end, float size, boolean isBold, int foregroundColor, int backgroundColor) {
        SpannableString spanString = new SpannableString(info);
        if (size > 0.0f) {
            spanString.setSpan(new RelativeSizeSpan(size), start, end, 33);
        }
        if (isBold) {
            spanString.setSpan(new StyleSpan(1), start, end, 33);
        }
        spanString.setSpan(new ForegroundColorSpan(foregroundColor), start, end, 33);
        spanString.setSpan(new BackgroundColorSpan(backgroundColor), start, end, 33);
        return spanString;
    }


    public static final ActivityManager.RunningAppProcessInfo getCurProcess(Context context) {
        final int pid = android.os.Process.myPid();
        final ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = mActivityManager.getRunningAppProcesses();
        ActivityManager.RunningAppProcessInfo result = null;
        for (ActivityManager.RunningAppProcessInfo appProcess : runningAppProcesses) {
            if (appProcess.pid == pid) {
                result = appProcess;
                break;
            }
        }
        return result;
    }



    public static String getExceptionString(Exception e) {
        String errorString = e.toString() + "\r\n";
        StackTraceElement[] stackArr = e.getStackTrace();
        for (int i = 0; i < stackArr.length; i++) {
            errorString = errorString + stackArr[i].toString();
            if (i + 1 != stackArr.length) {
                errorString = errorString + "\r\n";
            }
        }
        return errorString;
    }

    public static int getScreenWidth() {
        if (screenWidth > 0) {
            return screenWidth;
        }
        screenWidth = getScreenDimens(SCREEN_WIDTH);
        return screenWidth;
    }

    public static int getScreenHeight() {
        if (screenHeight > 0) {
            return screenHeight;
        }
        screenHeight = getScreenDimens(SCREEN_HEIGHT);
        return screenHeight;
    }


    public static int getScreenDimens(int type) {
        Display display;
        Point size;
        if (type == SCREEN_WIDTH) {
            display = ((WindowManager) BaseApplication.context().getSystemService("window")).getDefaultDisplay();
            size = new Point();
            display.getSize(size);
            return size.x;
        } else if (type != SCREEN_HEIGHT) {
            return 0;
        } else {
            display = ((WindowManager) BaseApplication.context().getSystemService("window")).getDefaultDisplay();
            size = new Point();
            display.getSize(size);
            return size.y;
        }
    }




    public static String getPhoneInfo() {
        return Build.MODEL;
    }


}
