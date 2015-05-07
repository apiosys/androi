package com.apiosystems.datacollector.SensorClasses;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.apiosystems.datacollector.ui.SensorActivity;
import com.apiosystems.datacollector.util.Helper;

/**
 * Created by jaredsheehan on 2/15/15.
 * http://www.codingforandroid.com/2011/01/using-orientation-sensors-simple.html
 */
public class OrientationSensorKitkat extends OrientationSensor implements SensorEventListener {
    private static final String LOG_TAG = OrientationSensorKitkat.class.getSimpleName();
    public Sensor mSensor;
    public Context mContext;
    public float values[] = { -1, -1, -1 };
    Sensor mAccelerometerSensor;
    Sensor mMagnetometerSensor;
    private float[] mGravity;
    private float[] mGeomagnetic;
    public SensorUpdateTaskHandler mTaskHandler;

    public OrientationSensorKitkat(Context context) {
        super(context);
        this.mContext = context;
        if(isSensorAvailable(Sensor.TYPE_ACCELEROMETER, mContext)) {
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if(isSensorAvailable(Sensor.TYPE_MAGNETIC_FIELD, mContext)) {
                mMagnetometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                if(Helper.isDebugEnabled()) {
                    Log.d(LOG_TAG, "All sensors available for Orientation");
                }
            } else {
                if(Helper.isDebugEnabled()) {
                    Log.d(LOG_TAG, "MagnetometerSensor not available for orientation calculation.");
                }
            }
        } else {
            if(Helper.isDebugEnabled()) {
                Log.d(LOG_TAG, "Accelerometer Sensor not available for orientation calculation.");
            }
        }
    }

    @Override
    public void registerSensor(){
        if(mAccelerometerSensor != null){
            mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        if(mMagnetometerSensor != null){
            mSensorManager.registerListener(this, mMagnetometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    public void unregisterSensor(){
        if(mAccelerometerSensor != null) {
            mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        if(mMagnetometerSensor != null) {
            mSensorManager.registerListener(this, mMagnetometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = sensorEvent.values;
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = sensorEvent.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                values = new float[3];
                SensorManager.getOrientation(R, values);
            }
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
        if(isSensorAvailable(Sensor.TYPE_ACCELEROMETER,mContext)
                && isSensorAvailable(Sensor.TYPE_MAGNETIC_FIELD,mContext)) {
            valuesstr = String.valueOf(values[0]) + Helper.SPACE
                    + String.valueOf(values[1]) + Helper.SPACE
                    + String.valueOf(values[2]) + Helper.SPACE;
        }else{
            valuesstr = "- - - ";
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
