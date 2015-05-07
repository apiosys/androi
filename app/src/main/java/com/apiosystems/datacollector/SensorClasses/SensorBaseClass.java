package com.apiosystems.datacollector.SensorClasses;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by SatyaSri on 8/18/2014.
 */public abstract class SensorBaseClass {
    private static final String LOG_TAG = SensorBaseClass.class.getSimpleName();

    public static SensorManager mSensorManager = null;
    int mSensorType = -1;

    SensorBaseClass(Context context){
        if(mSensorManager == null){
            mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
    }
    //We wont have an onCreate method as this class does not have an UI.
    public static boolean isSensorAvailable(int type, Context context){
        if(mSensorManager.getDefaultSensor(type)!=null){
            Log.d(LOG_TAG, "Sensor: " + type + " is Available");
            return true;
        }
        Log.d(LOG_TAG, "Sensor: " + type + " is not Available");
        return false;
    }

    public static SensorManager getSensorManager(Context context) {
        if(mSensorManager == null){
            mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
        return mSensorManager;
    }
}

