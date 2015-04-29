package com.apiosystems.datacollector.ui;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apiosystems.datacollector.Logger.SensorLogger;

import java.util.List;
import java.util.Timer;

import datacollector.apiosystems.com.datacollector.R;

public class SensorActivity extends Activity {

    private EditText mEditText;
    private Button mStartButton;
    private Button mEndButton;
    //private SensorManager mSensorManager;
    private List<Sensor> mSensorList;
    private TextView mSensorDataTextView;
    private Button mOkBtn;

    public static Timer timer ;
    private static final String LOG_TAG = "SENSOR_ACTIVITY";

    public SensorLogger mSensorLogger;
    private String mExperimentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG,"Before super.onCreate()");
        super.onCreate(savedInstanceState);

        Log.i(LOG_TAG,"OnCreate");
        setContentView(R.layout.data_capture);

        mEditText = (EditText) findViewById(R.id.EditText);
        mStartButton = (Button) findViewById(R.id.StartButton);
        mEndButton = (Button) findViewById(R.id.EndButton);
        mSensorDataTextView = (TextView)findViewById(R.id.textView1);
        mOkBtn = (Button) findViewById(R.id.BtnOk);



        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mExperimentName = mEditText.getText().toString();
                Log.i("EXPERIMENT_NAME",mExperimentName);
                mSensorLogger = new SensorLogger(getApplicationContext());
                mSensorLogger.startLogging(mExperimentName);
                timer = new Timer();
                timer.schedule(mSensorLogger, 0, 30);
            }
        });

        mEndButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO Auto-generated method stub
                mSensorLogger.stopLogging();
                timer.cancel();
                timer.purge();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorLogger.stopLogging();
        //timer.cancel();
        //timer.purge();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}