package com.apiosystems.datacollector.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.apiosystems.datacollector.Logger.SensorLogger;
import com.apiosystems.datacollector.util.Helper;
import java.util.Timer;
import datacollector.apiosystems.com.datacollector.R;

public class SensorActivity extends Activity {

    private static TextView mTextView;
    private static Button mAtStopBtn;
    private static Button mStartedMoveBtn;
    private static Button mPhoneCallBtn;
    private static Button mGeneralHandlingBtn;
    private static Button mStartButton;
    private static Button mEndButton;
    public static  boolean mThreadFlag = false;
    public static Timer timer ;
    public static final String LOG_TAG = "SENSOR_ACTIVITY";
    public static int BLUE;
    public static int GREY;
    public static int WHITE;

    public SensorLogger mSensorLogger;
    public String mExperimentName;
    public boolean mStartLog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.data_capture);
        mTextView = (TextView) findViewById(R.id.textView);
        mAtStopBtn = (Button) findViewById(R.id.atstop);
        mStartedMoveBtn = (Button) findViewById(R.id.startedmove);
        mPhoneCallBtn = (Button) findViewById(R.id.phonecall_btn);
        mGeneralHandlingBtn = (Button) findViewById(R.id.generalhandling_btn);
        mStartButton = (Button) findViewById(R.id.StartButton);
        mEndButton   = (Button) findViewById(R.id.EndButton);

        BLUE = getResources().getColor(R.color.BLUE);
        GREY = Color.DKGRAY;
        WHITE = Color.WHITE;


        disableView();
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartLog = true;
                enableView();
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
                disableView();
                Toast.makeText(getApplicationContext(), Helper.CAPTURE + " STOPPED :-( ", Toast.LENGTH_SHORT).show();
                mSensorLogger.stopLogging();
                timer.cancel();
                timer.purge();
            }
        });
    }

    private void enableView() {
        mTextView.setBackgroundColor(WHITE);
        mAtStopBtn.setBackgroundColor(BLUE);
        mStartedMoveBtn.setBackgroundColor(BLUE);
        mPhoneCallBtn.setBackgroundColor(BLUE);
        mGeneralHandlingBtn.setBackgroundColor(BLUE);
        mStartButton.setBackgroundColor(BLUE);
        mEndButton.setBackgroundColor(BLUE);

        mTextView.setEnabled(true);
        mAtStopBtn.setEnabled(true);
        mStartedMoveBtn.setEnabled(true);
        mPhoneCallBtn.setEnabled(true);
        mGeneralHandlingBtn.setEnabled(true);
        mStartButton.setEnabled(false);
        mEndButton.setEnabled(true);
    }

    private void disableView(){
        mTextView.setBackgroundColor(Color.GRAY);
        mAtStopBtn.setBackgroundColor(GREY);
        mStartedMoveBtn.setBackgroundColor(GREY);
        mPhoneCallBtn.setBackgroundColor(GREY);
        mGeneralHandlingBtn.setBackgroundColor(GREY);
        mStartButton.setBackgroundColor(GREY);
        mEndButton.setBackgroundColor(GREY);

        mTextView.setEnabled(false);
        mAtStopBtn.setEnabled(false);
        mStartedMoveBtn.setEnabled(false);
        mPhoneCallBtn.setEnabled(false);
        mGeneralHandlingBtn.setEnabled(false);
        mStartButton.setEnabled(true);
        mEndButton.setEnabled(false);
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