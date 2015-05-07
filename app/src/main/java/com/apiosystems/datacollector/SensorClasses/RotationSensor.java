package com.apiosystems.datacollector.SensorClasses;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.apiosystems.datacollector.ui.SensorActivity;
import com.apiosystems.datacollector.util.Helper;

public class RotationSensor extends SensorBaseClass implements SensorEventListener{
    private static String LOG_TAG = RotationSensor.class.getSimpleName();
    public Sensor mSensor;
    public Context mContext;
    public static float values[] = {-1,-1,-1};//-1 tells the SAL that there is no sensor
    public SensorUpdateTaskHandler mTaskHandler;

    public RotationSensor(Context context) {
        super(context);
        this.mContext = context;
        mSensorType = Sensor.TYPE_ROTATION_VECTOR;
        if(isSensorAvailable(mSensorType, mContext)) {
            Log.i(LOG_TAG, "ROTATION_SENSOR PRESENT");
            mSensorManager = getSensorManager(context);
            mSensor = mSensorManager.getDefaultSensor(mSensorType);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Log.i(LOG_TAG, "ROTATION_SENSOR ABSENT");
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
            values[0] = (float) (sensorEvent.values[0] / 9.81);
            values[1] = (float) (sensorEvent.values[1] / 9.81);
            values[2] = (float) (sensorEvent.values[2] / 9.81);
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
