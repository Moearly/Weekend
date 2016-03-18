package com.martn.weekend.utility;

import java.text.NumberFormat;

/**
 * Title: EnjoyTime
 * Package: com.martn.enjoytime.utility
 * Description: ("格式化--校验相关")
 * Date 2016/3/8 16:34
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class FormatUtils {
    static NumberFormat formater;

    public static String format_1fra(double val) {
        formater = NumberFormat.getInstance();
        formater.setMinimumFractionDigits(1);
        formater.setMaximumFractionDigits(1);
        return formater.format(val);
    }

    public static String format_2fra(double val) {
        formater = NumberFormat.getInstance();
        formater.setMinimumFractionDigits(2);
        formater.setMaximumFractionDigits(2);
        return formater.format(val);
    }

    public static String format_0fra(double val) {
        formater = NumberFormat.getInstance();
        formater.setMinimumFractionDigits(0);
        formater.setMaximumFractionDigits(0);
        return formater.format(val);
    }


    public static boolean isMobile(String mobile) {
        if (mobile != null && mobile.startsWith("1") && mobile.length() == 11) {
            return true;
        }
        return false;
    }


}
