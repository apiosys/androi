package com.apiosystems.datacollector.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.apiosystems.datacollector.Logger.SensorLogger;
import com.apiosystems.datacollector.util.Helper;
import java.util.Timer;
import datacollector.apiosystems.com.datacollector.R;

public class SensorActivity extends Activity {

    private Button mStartButton;
    private Button mEndButton;

    public static  boolean mThreadFlag = false;
    public static Timer timer ;
    public static final String LOG_TAG = "SENSOR_ACTIVITY";

    public SensorLogger mSensorLogger;
    public String mExperimentName;
    public boolean mStartLog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.data_capture);

        mStartButton = (Button) findViewById(R.id.StartButton);
        mEndButton   = (Button) findViewById(R.id.EndButton);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartLog = true;
                Toast.makeText(getApplicationContext(),Helper.CAPTURE + " STARTED :-) ", Toast.LENGTH_SHORT).show();
                mSensorLogger = new SensorLogger(getApplicationContext(), SensorActivity.this);
                mSensorLogger.startLogging(mExperimentName);
                timer = new Timer();
                timer.schedule(mSensorLogger, Helper.TIMER_DELAY, Helper.TIMER_PERIOD);
            }
        });

        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), Helper.CAPTURE + " STOPPED :-( ", Toast.LENGTH_SHORT).show();
                mSensorLogger.stopLogging();
                timer.cancel();
                timer.purge();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mStartLog){
            mStartLog = false;
            mSensorLogger.stopLogging();
            timer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}