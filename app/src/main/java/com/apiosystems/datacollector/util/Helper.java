package com.apiosystems.datacollector.util;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

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
    public static long mAbsTime;
    public static long timeOnStart;
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

    public static String getTimeInSalLogFormat(long time){
        long mTime = time;
        String millistr;
        long seconds =  mTime/1000;
        long milli  =  mTime%1000;
        if( milli < 10 ){
            millistr = "00" + String.valueOf(milli);
        }else if( milli < 100 ){
            millistr = "0" + String.valueOf(milli);
        }else{
            millistr = String.valueOf(milli);
        }
        String formattedTime = String.valueOf(seconds) + "." + millistr;
        return formattedTime;
    }
    /*
       This method returns the absolute time
     */
    public static String getAbsoluteTime(){
        mAbsTime = System.currentTimeMillis();
//        String absoluteTime = getTimeInSalLogFormat(mAbsTime);
//        Log.i(LOG_TAG, "ABSOLUTE_TIME : " + absoluteTime);
        return String.valueOf(mAbsTime);
    }
    /*
       This method sets the ElapsedTime variable. The values is set when we capture a new experiment
       and when we end an experiment.
     */

    public static void setTimeOnStart(long elapsedTime){
        timeOnStart = elapsedTime;
    }

    /*
       This method computes the ElapsedTime since the beginning of the experiment.
     */
    public static String getElapsedTime(){
        String elapsedTimeString = getTimeInSalLogFormat(mAbsTime - timeOnStart);
        return elapsedTimeString;
    }

    /*
       This method retrieves the phone Name, if any assigned by the user.
       The phone name is retrieved from the BluetoothAdapter.
     */
    public static String getPhoneName(){
        myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = myDevice.getName();
        Log.i("DEVICE_NAME : ", deviceName);
        return  deviceName;
    }
}
