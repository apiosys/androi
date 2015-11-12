package com.apiosystems.datacollector.SensorClasses;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.apiosystems.datacollector.util.Helper;

/**
 * Created by SatyaSri on 8/20/2014.
 */
public class RawMagnetometerSensor extends SensorBaseClass implements SensorEventListener {
    private static String LOG_TAG = RawMagnetometerSensor.class.getSimpleName();
    public Sensor mSensor;
    public Context mContext;
    public float values[] = { -1, -1, -1 } ;
    public SensorUpdateTaskHandler mTaskHandler;

    public RawMagnetometerSensor(Context context) {
        super(context);
        this.mContext = context;
        mSensorType = Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED;
        if(isSensorAvailable(mSensorType, mContext)) {
            Log.i(LOG_TAG, "RAW_MAGNETOMETER PRESENT");
            mSensorManager = getSensorManager(mContext);
            if(mSensorManager!=null) {
                mSensor = mSensorManager.getDefaultSensor(mSensorType);
                mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
            }
        } else {
            Log.i(LOG_TAG, "RAW_MAGNETOMETER ABSENT");
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
            values = new float[3];
            values[0] = sensorEvent.values[0];
            values[1] = sensorEvent.values[1];
            values[2] = sensorEvent.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public float[] getValues() {
        return values;
    }

    public String getValuesStr(){
        String valuesstr;
        if(isSensorAvailable(mSensorType,mContext)) {
            valuesstr = String.valueOf(values[0]) + Helper.SPACE
                    + String.valueOf(values[1]) + Helper.SPACE
                    + String.valueOf(values[2]) + Helper.SPACE;
        }else{
            valuesstr = "0 0 0 ";
        }
        return valuesstr;
    }

    public void setValues(float[] values) {
        this.values = values;
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

    public void setTaskHandler(SensorUpdateTaskHandler mTaskHandler) {
        this.mTaskHandler = mTaskHandler;
    }

}
