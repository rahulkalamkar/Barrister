package com.singular.barrister.Util;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by rahul.kalamkar on 11/23/2017.
 */

public class Utils {

    public static String getDateFormat(String date) {
        if (date.length() == 10) {
            return getDateFormatTime(date);
        }
        String[] temp = date.split("-");
        String month = getMonth(temp[1]);
        String dateOfMonth = temp[2].substring(0, 2);
        String time = temp[2].substring(3);
        String formattedDate = dateOfMonth + " " + getMonth(temp[1]) + " " + temp[0] + " " + time;
        return formattedDate;
    }

    public static String getDateFormatTime(String date) {
        String[] temp = date.split("-");
        String dateOfMonth = temp[2];
        String formattedDate = dateOfMonth + " " + getMonth(temp[1]) + " " + temp[0];
        return formattedDate;
    }

    public static String getMonth(String month) {
        String str = null;
        switch (month) {
            case "1":
                str = "Jan";
                break;
            case "2":
                str = "Feb";
                break;
            case "3":
                str = "Mar";
                break;
            case "4":
                str = "Apr";
                break;
            case "5":
                str = "May";
                break;
            case "6":
                str = "Jun";
                break;
            case "7":
                str = "Jul";
                break;
            case "8":
                str = "Aug";
                break;
            case "9":
                str = "Sep";
                break;
            case "10":
                str = "Oct";
                break;
            case "11":
                str = "Nov";
                break;
            case "12":
                str = "Dec";
                break;
        }
        return str;
    }

}
