package com.apiosystems.datacollector.SensorClasses;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.apiosystems.datacollector.ui.SensorActivity;
import com.apiosystems.datacollector.util.Helper;

public class ProximitySensor extends SensorBaseClass implements SensorEventListener {
    private static String LOG_TAG = ProximitySensor.class.getSimpleName();
    public Sensor mSensor;
    public Context mContext;
    public static float distance = -1;
    public SensorUpdateTaskHandler mTaskHandler;

    public ProximitySensor (Context context) {
        super(context);
        this.mContext = context;
        mSensorType = Sensor.TYPE_PROXIMITY;
        if(isSensorAvailable(mSensorType, mContext)) {
            Log.i(LOG_TAG, "PROXIMITY PRESENT");
            mSensorManager = getSensorManager(context);
            mSensor = mSensorManager.getDefaultSensor(mSensorType);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Log.i(LOG_TAG, "PROXIMITY ABSENT");
        }
    }

    public void registerSensor(){
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unregisterSensor(){
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (mSensor.getType() == mSensorType) {
            distance = (float) (sensorEvent.values[0]);
        }
        //Log.i(LOG_TAG, "Distance : " + distance);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public String getDistanceStr() {
        String valuesstr;
        if (isSensorAvailable(mSensorType, mContext)) {
            if (distance > 0.0) {
                valuesstr = "1" + Helper.SPACE; //Distant
            } else{
                valuesstr = "0" + Helper.SPACE; //Close
            }
        }else {
            valuesstr = "- ";
        }
        //Log.i(LOG_TAG,valuesstr);
            return valuesstr;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Sensor getSensor(){
        return this.mSensor;
    }

    public void setSensor(Sensor sensor){
        this.mSensor = sensor;
    }

    public SensorUpdateTaskHandler getTaskHandler() {
        return mTaskHandler;
    }
}

