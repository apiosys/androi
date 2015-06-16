package com.apiosystems.datacollector.util;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

import com.apiosystems.datacollector.Logger.SensorLogger;
import com.apiosystems.datacollector.ui.SensorActivity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Akshayraj on 4/29/15.
 */
public class Helper {
    public static final String NO_SENSOR_VALUES = "- - - ";
    public static final String CAPTURE = "CAPTURE";
    private static boolean DEBUG_ENABLED = false;//This is the global debugging flag
    public static final String NEW_LINE = "\n";
    public static final String SPACE = " ";
    public static final String DASH = "-";
    public static final String START_TEXTING = "Enter Text Here";
    public static final String DRIVER ="NOTICE:UserIsTheDriver:YES";
    public static final String PASSENGER = "NOTICE:UserIsTheDriver:NO";
    public static final long TIMER_PERIOD = 30;//millis
    public static final long TIMER_DELAY = 2*1000;//millis
    public static final String LOG_TAG = "DATACAPTURE";
    public static BluetoothAdapter myDevice;

    public static String getCurrentDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
        Calendar cal = Calendar.getInstance();
        String currentDateTime = dateFormat.format(cal.getTime());
        return currentDateTime;
    }

    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Calendar cal = Calendar.getInstance();
        String currentDate = dateFormat.format(cal.getTime());
        return currentDate;
    }

    public static String getCurrentTime(){
        DateFormat dateFormat = new SimpleDateFormat("HH.mm.ss");
        Calendar cal = Calendar.getInstance();
        String currentTime = dateFormat.format(cal.getTime());
        return currentTime;
    }

    public static String getCurrentDateTimeinMillis(){
        /*
        long millis = System.currentTimeMillis()%1000;
        String millisStr;
        if(millis < 10){
            millisStr = "00" + String.valueOf(millis);
        }else if(millis < 100){
            millisStr = "0" + String.valueOf(millis);
        }else{
            millisStr = String.valueOf(millis);
        }
        String currentDateTimeinMillis = Helper.getCurrentDateTime()
                + "." + millisStr;*/

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss.SSS");
        Calendar cal = Calendar.getInstance();
        String currentDateTimeinMillis = dateFormat.format(cal.getTime());
        return currentDateTimeinMillis;
    }

    public static String getAbsoluteTime(){
        long time = System.currentTimeMillis();
        String absoluteTime = String.valueOf(time/1000) + "." + String.valueOf(time%1000);
        Log.i(LOG_TAG, "ABSOLUTE_TIME : " + absoluteTime);
        return absoluteTime;
    }

    public static String getRelativeTime(){
        long time = System.currentTimeMillis() - SensorLogger.timeOnLogStart;
        String relativeTime = String.valueOf(time/1000) + "." + String.valueOf(time%1000);
        Log.i(LOG_TAG, "RELATIVE_TIME : " + relativeTime);
        return relativeTime;
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
        return currentDateTimeForFile;
    }

    public static String getPhoneName(){
        myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = myDevice.getName();
        Log.i("DEVICE_NAME : ", deviceName);
        return  deviceName;
    }
}
