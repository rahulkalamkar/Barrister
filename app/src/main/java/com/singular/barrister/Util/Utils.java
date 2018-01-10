package com.singular.barrister.Util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by rahul.kalamkar on 11/23/2017.
 */

public class Utils {

    public static String getDateFormat(String date) {
        if (date.length() == 10) {
            return getDateFormatTime(date);
        }
        try {
            String[] temp = date.split("-");
            String month = getMonth(temp[1]);
            String dateOfMonth = temp[2].substring(0, 2);
            String time = temp[2].substring(3);
            String formattedDate = dateOfMonth + " " + getMonth(temp[1]) + " " + temp[0] + " " + time;
            return formattedDate;
        } catch (Exception e) {
            return getDateFormatTime(date);
        }
    }

    public static String getDateFormatTime(String date) {
        String[] temp = date.split("-");
        String dateOfMonth = temp[2];
        String formattedDate = dateOfMonth + " " + getMonth(temp[1]) + " " + temp[0];
        return formattedDate;
    }


    public static boolean compareDate(String date) {
        Calendar c = Calendar.getInstance();
        String[] dateSplit = date.split(" ");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        if (dateSplit[0].equalsIgnoreCase(formattedDate)) {
            return true;
        }/* else if (compareMonth(dateSplit[0], formattedDate)) {
            return true;
        }*/ else
            return false;
    }

    public static boolean compareMonth(String dateToCompare) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String currentDate = df.format(c.getTime());

        String[] dateSplit = dateToCompare.split(" ");

        String[] current = currentDate.split("-");
        String[] hearing = dateSplit[0].split("-");

        if (Integer.parseInt(hearing[0]) < Integer.parseInt(current[0])) {
            return true;
        } else if (Integer.parseInt(hearing[0]) == Integer.parseInt(current[0])) {
            if (Integer.parseInt(hearing[1]) == Integer.parseInt(current[1])) {
                if (Integer.parseInt(hearing[2]) > Integer.parseInt(current[2])) {
                    return false;
                } else
                    return true;
            } else if (Integer.parseInt(hearing[1]) < Integer.parseInt(current[1])) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getMonth(String month) {
        String str = null;
        switch (month) {
            case "01":
            case "1":
                str = "Jan";
                break;
            case "02":
            case "2":
                str = "Feb";
                break;
            case "03":
            case "3":
                str = "Mar";
                break;
            case "04":
            case "4":
                str = "Apr";
                break;
            case "05":
            case "5":
                str = "May";
                break;
            case "06":
            case "6":
                str = "Jun";
                break;
            case "07":
            case "7":
                str = "Jul";
                break;
            case "08":
            case "8":
                str = "Aug";
                break;
            case "09":
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
