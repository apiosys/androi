package com.apiosystems.datacollector.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.apiosystems.datacollector.ui.SensorActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Akshayraj on 4/29/15.
 */
public class Helper {
    public static final String NO_SENSOR_VALUES = "- - - ";
    private static boolean DEBUG_ENABLED = false;//This is the global debugging flag
    public static final String NEW_LINE = "\n";
    public static final String SPACE = " ";
    public static final String DASH = "-";
    public static final long TIMER_PERIOD = 20;//millis
    public static final long TIMER_DELAY = 1*1000;//millis

    public static String getCurrentDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
        Calendar cal = Calendar.getInstance();
        String currentDateTime = dateFormat.format(cal.getTime());
        //Log.i("Current DateTime ", currentDateTime);
        return currentDateTime;
    }

    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Calendar cal = Calendar.getInstance();
        String currentDate = dateFormat.format(cal.getTime());
        //Log.i("Current Date ", currentDate);
        return currentDate;
    }

    public static String getCurrentTime(){
        DateFormat dateFormat = new SimpleDateFormat("HH.mm.ss");
        Calendar cal = Calendar.getInstance();
        String currentTime = dateFormat.format(cal.getTime());
        //Log.i("Current Time ", currentTime);
        return currentTime;
    }

    public static String getCurrentDateTimeinMillis(){
        long millis = System.currentTimeMillis()%1000;
        String millisStr = String.valueOf(millis);
        String currentDateTimeinMillis = Helper.getCurrentDateTime()
                + "." + millisStr;
        //Log.i("DateTimeinMillis : ", currentDateTimeinMillis);
        return currentDateTimeinMillis;
    }

    public synchronized boolean getFlag(){
        return SensorActivity.mThreadFlag;
    }

    public synchronized void setFlag(boolean flag, boolean value){
        flag=value;
    }

    public static boolean isDebugEnabled() {
        return DEBUG_ENABLED;
    }

    public static String getCurrentDateTimeForFile() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        Calendar cal = Calendar.getInstance();
        String currentDateTimeForFile = dateFormat.format(cal.getTime());
        //Log.i("Current DateTime ", currentDateTimeForFile);
        return currentDateTimeForFile;
    }
}
