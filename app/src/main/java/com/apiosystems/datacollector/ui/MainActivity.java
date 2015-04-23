package com.apiosystems.datacollector.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import datacollector.apiosystems.com.datacollector.R;

public class MainActivity extends Activity implements SensorEventListener{

    private EditText mEditText;
    private Button mStartButton;
    private Button mEndButton;
    private SensorManager mSensorManager;
    private List<Sensor> mSensorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        mEditText = (EditText) findViewById(R.id.EditText);
        mStartButton = (Button) findViewById(R.id.StartButton);
        mEndButton = (Button) findViewById(R.id.EndButton);

        mSensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        mSensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mSensorManager.registerListener(MainActivity.this,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        mSensorManager.SENSOR_DELAY_NORMAL);
            }
        });

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
